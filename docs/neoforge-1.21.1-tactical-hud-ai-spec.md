# NeoForge 1.21.1 战术 HUD：AI 编码任务书

> **历史文档提示**：本文是当时的实现任务书。其中「UUID 白名单 + `tactical-hud-admin.toml`」的授权设计**已被取代** ——
> 现行权限是「服务器配置密码 → 验证通过后把 UUID 记入存档」，见 `docs/kunjinkao-admin.toml.example` 与
> `src/main/java/dev/modmind/kunjinkao/config/AdminToolConfig.java`。文中其余功能描述仍然有效。

将以下内容作为 AI 编码助手的实现要求。目标是独立的 Minecraft 1.21.1 NeoForge 模组；不要复用 Forge 1.20.1 的 `SimpleChannel`、`NetworkEvent.Context`、`@Mod.EventBusSubscriber` 包名或 `ForgeConfigSpec`。

## 目标与边界

实现一个只对 UUID 白名单管理员开放的战术 HUD。

- 默认快捷键为 `H`，可在游戏控制设置中重绑。
- 按 H 时，客户端只能向服务器发起请求；服务器才是唯一的授权方。
- UUID 被服务器全局配置允许时，客户端显示可交互的透明 HUD；未授权时不显示 HUD，并给出本地提示。
- HUD 打开后鼠标必须可移动并可以点击按钮；HUD 不能暂停单人游戏。
- 再按 H 必须关闭 HUD 并清理界面；在子界面中按 H 也必须关闭，不能卡住游戏输入。
- 初始功能包括：夜视开关、实体管理。
- 不依赖 OP 权限；白名单 UUID 是唯一授权条件。
- 不实现任何剑风、投射物、伤害、物品或配方功能。

## 固定技术约束

- Loader：NeoForge 1.21.1（使用目标 NeoForge MDK 的实际 `21.1.x` 版本）。
- Minecraft：1.21.1；Java：21。
- 映射和 Gradle 配置以下载的 NeoForge 1.21.1 MDK 为准。
- 使用 `ResourceLocation.fromNamespaceAndPath(MOD_ID, path)`。
- 客户端代码必须只由物理客户端加载：用 `@OnlyIn(Dist.CLIENT)`、客户端专用事件订阅类或 NeoForge 推荐的客户端初始化方式隔离。专用服务器绝不能加载 `net.minecraft.client.*` 类。
- 网络使用 1.21.1 的 `CustomPacketPayload`、`StreamCodec`、`RegisterPayloadHandlersEvent` 和 `RegisterClientPayloadHandlersEvent`；不得使用 Forge 的 `SimpleChannel`。
- 所有注册使用 NeoForge 的 `DeferredRegister` 或对应事件；不要使用 Forge 包 `net.minecraftforge.*`。

## 建议目录

```text
src/main/java/<package>/
  TacticalHudMod.java
  config/TacticalHudConfig.java
  network/TacticalHudPayloads.java
  network/payload/ToggleHudPayload.java
  network/payload/HudStatePayload.java
  network/payload/ToggleNightVisionPayload.java
  network/payload/RequestEntityListPayload.java
  network/payload/EntityListPayload.java
  network/payload/ManageEntityPayload.java
  network/payload/EntityActionResultPayload.java
  common/AuthService.java
  common/EntityQueryService.java
  client/TacticalHudKeyMappings.java
  client/ClientHudState.java
  client/ClientPayloadHandlers.java
  client/ClientHudEvents.java
  client/gui/TacticalHudScreen.java
  client/gui/EntityManagerScreen.java
  client/gui/EntityRowData.java
src/main/resources/
  META-INF/neoforge.mods.toml
  assets/<modid>/lang/en_us.json
  assets/<modid>/lang/zh_cn.json
```

## 全局 UUID 白名单配置

创建 `TacticalHudConfig`，使用 NeoForge 1.21.1 的 `ModConfigSpec`。

```toml
# config/tactical-hud-admin.toml
admin_uuids = [
  "00000000-0000-0000-0000-000000000000"
]
```

要求：

1. 在主模组构造器中通过传入的 `ModContainer` 注册 `ModConfig.Type.COMMON`，文件名为 `tactical-hud-admin.toml`。这样服务器根目录 `config/` 中的一份配置能被所有世界共享。
2. 使用 `defineListAllowEmpty`（或 MDK 中等价的列表定义 API）并只接受可由 `UUID.fromString` 解析的字符串。
3. `AuthService.isAuthorized(UUID)` 每次服务器处理请求时都读取当前配置值；不要把客户端提供的 UUID 当作授权依据。
4. 仅配置修改后重启或按 NeoForge 配置重载事件生效即可；不要在客户端复制、同步或暴露白名单内容。

## H 键与 HUD 状态机

### 键位注册

仅物理客户端注册 `KeyMapping`：

- 翻译键：`key.<modid>.toggle_tactical_hud`
- 默认键：`InputConstants.Type.KEYSYM + GLFW.GLFW_KEY_H`
- 分类：自定义 `KeyMapping.Category`，翻译键为 `key.category.<modid>.tactical`
- 通过 `RegisterKeyMappingsEvent` 注册分类和键位。

在 `ClientTickEvent.Post` 中使用 `while (TOGGLE_HUD.consumeClick())` 消费按键。若没有本地玩家或世界，重置客户端 HUD 状态。

### 状态机

`ClientHudState` 至少维护：

- `boolean authorized`：只由服务器状态包更新。
- `boolean enabled`：只由服务器状态包更新。
- `float animationProgress`：每 tick 向 0 或 1 逼近，用于 HUD 淡入淡出。

流程：

```text
H 按下
  -> 客户端发送 ToggleHudPayload(requestedEnabled = !enabled)
  -> 服务器用 sender.getUUID() 校验白名单
  -> 服务器发送 HudStatePayload(enabled = authorized && requestedEnabled, authorized)
  -> 客户端收到成功状态才 setScreen(new TacticalHudScreen())
  -> 未授权或关闭时 setScreen(null)，并更新 ClientHudState
```

不得在客户端按 H 后立即相信自己已授权；不得仅通过隐藏按钮实现权限控制。

## 可交互 HUD 界面

`TacticalHudScreen extends Screen`，并满足：

1. 覆盖 `isPauseScreen()` 返回 `false`。
2. `render` 不调用会完全遮挡世界的背景渲染；用 `GuiGraphics.fill` 绘制半透明边框、面板和按钮。
3. 在 `init()` 中使用 `addRenderableWidget(Button.builder(...))` 创建两个按钮：`夜视`、`生物`。不能只在 `render` 中手写命中判断；应让 Screen/Widget 接管鼠标交互和可访问性。
4. 覆盖 `keyPressed`：使用键位扩展的匹配方法（目标 MDK 中的 `isActiveAndMatches` 或等价 API）判断 H；命中后向服务器发送关闭请求，并关闭当前 Screen。
5. HUD 子界面也复用同一 H 关闭逻辑。关闭实体管理子界面时，应返回战术 HUD；H 则直接退出全部 HUD。
6. `Screen` 负责鼠标交互；不要在 `ClientTickEvent` 中每 tick 重复 `setScreen`，否则会导致鼠标锁定或无法游玩。

可选：另注册一层只读 HUD 覆盖层，用于 Screen 未打开时显示淡入淡出的装饰。实际事件名称以 1.21.1 MDK 为准；不要把 Forge 1.20.1 的 GUI overlay 注册 API 直接复制过来。

## 网络载荷

每个载荷使用 Java `record` 实现 `CustomPacketPayload`，定义唯一 `TYPE` 与 `StreamCodec`。使用 `ByteBufCodecs`、`StreamCodec.composite` 和目标 MDK 的 `RegistryFriendlyByteBuf`/`FriendlyByteBuf` 泛型。

| 方向 | 载荷 | 数据 | 服务器规则 |
|---|---|---|---|
| C2S | `ToggleHudPayload` | `boolean requestedEnabled` | 用发送者 UUID 校验，再回传实际状态。 |
| S2C | `HudStatePayload` | `boolean enabled`, `boolean authorized` | 仅发给请求者。 |
| C2S | `ToggleNightVisionPayload` | 无 | 再次校验 UUID，才添加或移除夜视。 |
| C2S | `RequestEntityListPayload` | 无 | 再次校验 UUID，查询已加载实体。 |
| S2C | `EntityListPayload` | 有上限的实体 DTO 列表 | 仅发给请求者。 |
| C2S | `ManageEntityPayload` | `UUID entityUuid`, `enum action` | 再次校验、重新定位实体、执行。 |
| S2C | `EntityActionResultPayload` | UUID、动作、成功与消息 | 仅发给请求者。 |

注册规则：

1. 在模组事件总线监听 `RegisterPayloadHandlersEvent`，通过 `event.registrar("1")` 注册所有 play-phase payload 的类型与编解码器。
2. C2S 处理器只能放在 common/server 包，使用 `IPayloadContext.player()` 取得真实发送者；敏感操作在 `context.enqueueWork` 中执行。
3. S2C 的客户端处理器只能在物理客户端监听 `RegisterClientPayloadHandlersEvent` 时注册；不要让 common payload 类直接初始化客户端 UI 类。
4. 客户端发送使用 NeoForge 1.21.1 的 `PacketDistributor.sendToServer(payload)`（或该 MDK 的等价方法）；服务器单播使用 `PacketDistributor.sendToPlayer(serverPlayer, payload)`。
5. 网络版本字符串每次改变载荷集合或编码格式时递增，确保不兼容客户端/服务器不能加入同一世界。

## 夜视按钮

- 服务器再次检查 `AuthService.isAuthorized(sender.getUUID())`。
- 使用 `MobEffects.NIGHT_VISION` 添加持续 30 分钟的效果，隐藏粒子和图标。
- 在玩家 persistent data 记录本模组添加的布尔标记，例如 `<modid>:hud_night_vision`。
- 再次点击只移除带本模组标记的夜视，不应删除其他模组或药水给予的夜视。
- 服务器通过 `EntityActionResultPayload` 或专用状态包告知客户端结果。

## 实体管理按钮

点击“生物”后：

1. 客户端发送 `RequestEntityListPayload`。
2. 服务器校验 UUID 后遍历 `server.getAllLevels()`；仅收集当前已加载的实体，不强制加载区块。
3. DTO 至少包含：实体 UUID、运行时实体 id、维度 `ResourceLocation`、类型翻译键、显示名、x/y/z。
4. 总数限制为 200（或可配置上限），按维度和显示名稳定排序，避免 S2C payload 过大。
5. 客户端打开 `EntityManagerScreen`：可滚动列表、单选行、底部“结束”“传送”按钮；无选中目标时按钮禁用。
6. “结束”：服务器再次校验 UUID，并按 UUID 在所有已加载维度重新查找；找到后才结束实体。对 `ServerPlayer` 使用明确的玩家死亡/踢出策略，不能无意中用 `discard()` 绕过游戏规则；普通非玩家实体可按设计 `discard()` 或用正常伤害来源杀死。
7. “传送”：服务器再次校验 UUID，并将请求者传送到目标实体当前所在维度和坐标；不要相信客户端传入的坐标或维度。
8. 实体已卸载、死亡或跨维度时返回失败状态，不崩溃、不传送到旧坐标。

## 必须避免的问题

- 不要使用 Forge 1.20.1 的 `SimpleChannel`、`NetworkEvent.Context`、`PacketDistributor.PLAYER.with`、`ForgeConfigSpec` 或 `ModLoadingContext.get()`。
- 不要在服务器类导入或引用 `Minecraft`、`Screen`、`GuiGraphics` 或任何 `net.minecraft.client.*` 类型。
- 不要仅在客户端检查 UUID、仅隐藏按钮，或依赖 OP 权限。
- 不要让未授权用户通过伪造 C2S 载荷获得夜视、实体列表、传送或结束实体的能力。
- 不要每 tick 打开 Screen；不允许 H 键在 Screen 打开后失效。
- 不要把所有实体的完整 NBT 发给客户端；只发界面需要的 DTO，并限制列表大小。
- 不要以加载未加载区块为代价枚举世界所有实体。

## 验收清单

1. UUID 在白名单：进入单人或专用服务器后按 H，HUD 可打开、鼠标可点击、再次按 H 可关闭。
2. UUID 不在白名单：按 H 不显示 HUD；手工发送任意 C2S 载荷也不能启用夜视、获取实体数据、传送或结束实体。
3. HUD 与实体管理屏都不暂停游戏；从实体管理返回 HUD、按 H 退出全部 UI 正常。
4. 夜视按钮仅切换模组自身添加的效果。
5. 实体列表包含所有已加载维度中的实体，操作时在服务端重新查找 UUID。
6. 专用服务器启动、玩家加入、H 打开/关闭、夜视与实体操作均无 `ClassNotFoundException`、无客户端类加载错误。
7. 使用 NeoForge 1.21.1 MDK 的 `gradlew build` 构建成功，并在干净客户端和专用服务器上分别测试。

## 官方资料

- https://docs.neoforged.net/docs/1.21.1/networking/
- https://docs.neoforged.net/docs/networking/payload/
- https://docs.neoforged.net/docs/1.21.1/gui/screens/
- https://docs.neoforged.net/docs/1.21.1/concepts/sides/
- https://docs.neoforged.net/docs/1.20.6/misc/config/
- https://docs.neoforged.net/docs/1.21.11/misc/keymappings/
