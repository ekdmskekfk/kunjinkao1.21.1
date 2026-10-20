# kunjinkao（锟斤拷烫烫烫）新增 15 项功能开发计划

> 交接对象：负责 `F:\mcmodli\tang\neoforge1.21.1` 的开发者
> 目标版本：0.9.8 起（当前 `gradle.properties` 里是 0.9.7）
> 本文件只描述"做什么、怎么做、怎么验"，不含任何代码改动
> 功能来源：第三方模组 **子枫·自定义技能树 v1.5.1** 中已实现的 15 个技能（序号见下），本工程里它们不再是"技能树节点"，而是**剑开关 / 授权玩家功能 / 全局配置**三种载体

---

## 一、要做的 15 项功能

| 序号 | 功能 | 一句话效果 | 复杂度 | 里程碑 |
| --- | --- | --- | --- | --- |
| 70 | 万载不磨 `unbreakable` | 手持本模组剑时，物品不再消耗耐久 | 中（mixin） | M2 |
| 72 | 经验飞涨 `xp_gain` | 击杀/挖矿/烧炼经验按倍率放大（默认 ×2，可配到 ×101） | 低（事件） | M2 |
| 77 | 不朽铭文 `ult_unbreak_tag` | 铁砧里两个相同物品 → 合出【无法破坏】工具 | 中（事件 + 冲突处理） | M2 |
| 80 | 星瞳夜视 `night_vision` | 常驻夜视、不闪烁 | 低（改造现有） | M1 |
| 83 | 凌空采掘 `fly_mining` | 空中挖掘不再 ÷5（水下同理可选） | 低（改造现有） | M1 |
| 84 | 烈焰不侵 `fire_protect` | 完全免疫火焰/岩浆，不着火、无火焰遮挡 | 中（mixin） | M2 |
| 85 | 鲛人之息 `water_breath` | 水下氧气永远满 | 低（tick） | M1 |
| 87 | 碧波清眸 `underwater_vision` | 水下/岩浆中视野清晰（雾减弱） | 中（客户端 + mixin） | M3 |
| 88 | 万民敬仰 `village_hero` | 常驻村庄英雄效果（交易折扣） | 低（tick） | M1 |
| 89 | 长臂善舞 `reach` | 触摸与攻击距离 +N 格 | 低（属性） | M1 |
| 94 | 无限交易 `unlimited_trades` | 交易次数永不减少、不售罄 | 低（事件） | M2 |
| 95 | 村民大师 `villager_master` | 交易一次村民直接满级、解锁全部配方 | 低（事件） | M2 |
| 96 | 寻宝大师 `treasure_hunter` | 64 格内所有容器/刷扫点显示穿透高亮框 | 高（扫描 + 渲染） | M3 |
| 119 | 战利品大爆发·机器共鸣 `machine_loot_bomb` | 假玩家机器击杀也吃额外掉落倍率 | 中（改造现有） | M3 |
| 127 | 机关拆解·选区挖掘 `machine_zone_excavate` | 框选一块区域一次性挖空（含流体），带进度与撤销 | 高（作业引擎 + UI） | M3 |

---

## 二、现状盘点：这个工程里已经有什么

| 功能 | 现有对应实现 | 结论 |
| --- | --- | --- |
| 70 | `KunJinKaoSwordItem.java:1049-1051` 已给剑挂 `DataComponents.UNBREAKABLE`；没有任何"物品耐久减免"逻辑 | 半新增：组件用法有先例，mixin 要新写 |
| 72 | 无 | 全新增 |
| 77 | 无铁砧逻辑；`UNBREAKABLE` 组件写法可复用 | 全新增 |
| 80 | `HudFunction.NIGHT_VISION` + `KunJinKaoColdDataEffectsHandler.java:139-158`（给 30 分钟夜视、开关、鉴权、状态回执） | 已有，只需改"常驻续期" |
| 83 | `SwordMiningPenaltyHandler`（`PlayerEvent.BreakSpeed`，按系数乘回空中/水下惩罚）已是同一手法，只是条件写死"手持本模组剑" | 改条件即可 |
| 84 | 无（`Entity#fireImmune` 未被改过） | 全新增（mixin） |
| 85 | 无 | 全新增 |
| 87 | 无雾效相关代码 | 全新增（客户端 + mixin） |
| 88 | 无 | 全新增 |
| 89 | 只**读取**过 `BLOCK_INTERACTION_RANGE`（`KunJinKaoSwordItem.java:764`、`SwordAutoPickupHandler.java:52`），从未加过 modifier | 全新增（属性 modifier） |
| 94 / 95 | 无任何村民/交易代码 | 全新增 |
| 96 | 无 | 全新增（客户端扫描 + 渲染） |
| 119 | `KunJinKaoExtraLootHandler` 已在 `LivingDropsEvent` 上加掉落（斩首/刷怪蛋），但只认真实玩家，没有假玩家分支 | 改造 + 新增开关 |
| 127 | 已有放置侧框架：`world/PlacementCoreHandler`、`world/SwordPlacementRegistry`、`world/PlacementUndoHistory`、`network/CollectBlockPayload`、`world/SwordToolHandler`；**没有**框选 + 批量挖掘 | 复用框架后新增 |

---

## 三、横切设计（15 项都要遵守）

### 3.1 功能载体分级（甲 + 乙 合并为一个功能面板 GUI）

这个工程没有技能树，也不该让玩家去手改配置文件。所以服务器级项与玩家级项**统一收进一个独立的功能面板 GUI**（仿子枫技能树的技能树界面：分类标签条 + 功能行列表 + 开关/数值交互 + 快捷键绑定），只保留一类不进面板：

- **面板·服务器级项**：影响世界或全服规则的（`72` 倍率、`77` 启用与经验成本、`96` 总闸与半径、`119` 倍率、`127` 总闸与参数）。面板里带"服务器级"徽章，**只有 OP（权限等级 2）能改**；改动经服务端校验后**写回 `AdminToolConfig` 并 `SPEC.save()`**，所以 toml 与面板永远一致，服主依然可以直接编辑文件或备份配置。
- **面板·个人级项**：只影响自己角色的（`80`、`84`、`85`、`87`、`88`、`89`、`94`、`95`、`96` 的个人开关、`119` 的个人开关）。需要现有密码授权；改动写进 `QolStateSavedData`（按 UUID 存），与服务器级项互不干扰。
- **不进面板·随剑生效**：`70`、`83` 沿用"手持本模组剑"的既有语义，开关放**剑的设置界面**（`SwordOptionsScreen` + `SwordSettingPayload` 新 `settingId`），因为这两项本来就是"剑的能力"；面板里也可以给它们放一行只读入口做说明与跳转。

一句话：**甲 + 乙 = 一个面板 GUI，丙（随剑）保持原样**。

### 3.2 数据存储

- 全局开关与数值：直接进 `AdminToolConfig`（新增 `[qol]` 分节），无需存档。
- 个人开关：新增 `config/QolStateSavedData`，照 `world/SwordPlacementRegistry` 的写法（挂 overworld 单例，`UUID → int 位掩码`）。也可以先复用 `PasswordAdminSavedData` 里的授权位，等位不够再拆。
- 选区数据（127）：跟放置记录放一起（`SwordPlacementRegistry`），或新建 `world/ZoneSelectionRegistry`。

### 3.3 网络

- 建议新增 **1 个 C2S + 1 个 S2C**：
  - `network/QolSettingPayload`（C2S，`int settingId, int value`，仿 `SwordSettingPayload` 的判别 id 写法）
  - `network/QolStatePayload`（S2C，`int stateId, boolean enabled, boolean authorized`，仿 `HudStatePayload`）
- **`NetworkHandler.PROTOCOL_VERSION` 当前是 `"22"`，只要动注册表就必须 +1**（现有注释里已经写明为什么：新旧端会被当兼容然后解码错位）。
- 所有 C2S 都要在服务端做权限与"是不是持剑者"的二次校验，不能信客户端。

### 3.4 Mixin（预计 4 条）

| 目标 | 注入点 | 备注 |
| --- | --- | --- |
| 70 | `ItemStack#hurtAndBreak`（`@ModifyVariable` HEAD，index 1） | 只在 `entity instanceof ServerPlayer` 时改；参考反编译的 `ItemStackMixin.java:22-36` |
| 84 | `Entity#fireImmune`（`@Inject` HEAD，cancellable） | 只对 `Player` 生效；参考 `EntityFireImmuneMixin.java:20-36` |
| 87 | `FogRenderer#setupFog` | **优先用事件**（`ViewportEvent.RenderFog` / `ComputeFogColor`），事件做不到再上 mixin |
| 83 | 不需要 mixin | 走已存在的 `PlayerEvent.BreakSpeed` |

登记与坑：
- 新 mixin 要写进 `src/main/resources/kunjinkao.mixins.json`。当前文件**没有 `client` 段**，`87` 的客户端 mixin 需要新加一个 `"client": [...]` 数组。
- 现有 `"injectors": { "defaultRequire": 1 }`：对**外部目标**（`ItemStack`、`Entity`、`FogRenderer`）的注入一律显式写 `require = 0`，避免别的模组改动导致整个注入失败崩游戏。
- 交接文档里记过：**同一调用点两个 `@Redirect` 会冲突崩溃**，能 `@Inject` 就 `@Inject`。

### 3.5 客户端表现与按键

- **新增统一功能面板 `client/gui/QolControlScreen`**：进入方式为 `client/KunJinKaoKeyBindings` 新增一个按键（默认建议 `J`，落地时避开现有按键），并在 `client/gui/SwordOptionsScreen` 里加一个入口按钮。
- 面板布局与交互照技能树界面移植（具体参数见第五节），但**配色改用本工程的 `KunJinKaoPalette` 语义色（青/琥珀/品红/红）**，不照搬对方的灰底；乱码符号与本工程主题保持一致。
- 打开面板时向服务端请求一次全量状态（`QolStatePayload` 全量），之后每次改走增量回执；客户端不做任何权威判定，服务器级项在非 OP 客户端上直接置灰。
- 现有 `HudFunction` / `HudStatePayload` 的夜视链路保留（`80` 的实现就在那里），但面板里同样给 `80` 一行，两个入口操作**同一份服务端状态**，不允许出现两套开关各记一份的情况。
- 需要按键的还有 `127` 的执行/取消键（在面板里绑，见 5.10）。

### 3.6 边界与兼容

- 服务端权威、客户端只做表现；任何"允许/生效"的判定都在服务端。
- 假玩家统一用 NeoForge `FakePlayer` 判定，并在 UI/文档里标注"弱兼容：不保证在所有机器上生效"。
- 提前做冲突检查（见第七节）。

---

## 四、逐功能计划

### 70 万载不磨

- **目标行为**：开启后，物品使用/攻击/挖掘不再扣耐久（等效耐久消耗为 0）。
- **参考实现**：`ItemStackMixin.java:22-36`（`@ModifyVariable` 改 `hurtAndBreak` 的 amount，`floor(amount × (1 - r))`，`r = 1` 时恒 0）；减免值来源 `SkillEffects.java:294-297`。
- **落点**：新增 `mixin/ItemStackDurabilityMixin.java`；开关放剑的数据标签（仿 `KunJinKaoSwordItem.BREAK_UNBREAKABLE_BLOCKS_KEY` 的写法）+ `SwordSettingPayload` 新增一个 `settingId` + `SwordOptionsScreen` 加一个按钮。
- **要点**：只处理 `ServerPlayer`；`require = 0`；与剑自身的 `UNBREAKABLE` 组件（`1049-1051`）不冲突（那条是剑永不掉耐久，这条是别的物品）。
- **验收**：手持剑挖 1000 个方块、攻击 1000 次，物品耐久条不动；关掉开关立刻恢复原版消耗。
- **风险**：`hurtAndBreak` 影响面极大（全物品），条件必须写窄。

### 72 经验飞涨

- **目标行为**：击杀、挖矿、烧炼获得的经验乘以配置倍率（默认 ×2，上限建议给到 ×101）。
- **参考实现**：`UltimateEvents.java:1244-1253`（`LivingExperienceDropEvent`，倍率 = `1 + 2 × 等级`）。
- **落点**：新增 `event/KunJinKaoXpBoostHandler.java`；倍率进 `AdminToolConfig [qol]`。
- **要点**：用 `event.setDroppedExperience(round(原值 × 倍率))`；倍率要钳制上限，避免与高倍掉落叠出 int 溢出。
- **验收**：config 设 5，杀僵尸掉 5 倍经验；改回 1 立即生效（无需重启）。

### 77 不朽铭文

- **目标行为**：铁砧左槽 + 右槽放**两个相同物品**，产出带【无法破坏】的成品（消耗可配的经验等级）。
- **参考实现**：`UltimateEvents` 里的 `AnvilUpdateEvent` 处理（与附魔三件套共用一条 `else-if` 链）。
- **落点**：新增 `event/KunJinKaoAnvilHandler.java`；成本进 `AdminToolConfig`。
- **要点**：
  - "相同"用 `ItemStack.isSameItemSameComponents`；
  - 产出 = 复制左槽 + `DataComponents.UNBREAKABLE`（写法照 `KunJinKaoSwordItem.java:1049-1051`）；
  - **必须和原版"同物品修复"分支划清**（建议：只在右侧数量 = 1 且左槽有耐久概念时接管，其余情况 `return` 放行原版），否则会吞掉正常修复；
  - `event.setLevelCost` / `setXpCost` 要显式设置，避免出现"0 级成本"的白送。
- **验收**：两个相同铁镐 → 输出"无法破坏"铁镐；两个不同物品无反应；经验不足时按原版提示。

### 80 星瞳夜视（改造）

- **目标行为**：常驻夜视、不闪烁；不被普通夜视药水覆盖或被"挤掉"。
- **参考实现**：`UltimateEvents.java:275-287`（每 20 tick 补一次无限时长夜视）。
- **落点**：`KunJinKaoColdDataEffectsHandler.java:139-158` 的 `handleToggleHudNightVision` 内，把 `20 * 60 * 30`（30 分钟一次性）改成**短时长高频续期**（例如 400 tick，每 200 tick 检查一次）；`HudFunction.NIGHT_VISION` 与回执保持不动。
- **要点**：续期前先判断当前效果是不是本模组施加的（用现有的 `KunJinKaoHudNightVision` 持久化标记），不要抢玩家自己喝的夜视药水；关闭时只移除自己施加的那条。
- **验收**：开 30 分钟后仍亮；死亡重生后行为符合预期（要么自动恢复、要么在 HUD 明示需重开）；同时喝普通夜视药水不冲突。

### 83 凌空采掘（改造）

- **目标行为**：空中挖掘不受 ÷5 惩罚（可选：水下也不受 `/SUBMERGED_MINING_SPEED` 惩罚）。
- **参考实现**：`UltimateEvents.java:540-550`（`fly_mining` 判定）；乘回手法与本工程 `SwordMiningPenaltyHandler` 完全一致。
- **落点**：直接改 `SwordMiningPenaltyHandler`，条件从"手持本模组剑"扩展到"手持本模组剑 **或** 该玩家已开启凌空采掘"。
- **要点**：继续按**实际系数**乘回（空中 ×5、水下 `/= SUBMERGED_MINING_SPEED`），不要硬写乘数；属性为 0 时跳过除法（现有注释已说明）。
- **验收**：开空中挖石头与地面同速；关掉恢复 1/5；水下同理。

### 84 烈焰不侵

- **目标行为**：免疫火焰与岩浆伤害、身上不着火、无火焰遮挡。
- **参考实现**：`EntityFireImmuneMixin.java:20-36`（`@Inject` HEAD `Entity#fireImmune`，cancellable，服务端读玩家数据、客户端读客户端镜像）。
- **落点**：新增 `mixin/EntityFireImmuneMixin.java`（名字与本工程现有 mixin 区分开）+ 面板个人开关。
- **要点**：`require = 0`；只对 `Player` 生效，别扩大到所有实体；免疫后原版火焰渲染与伤害判定会跟着走（`displayFireAnimation` 依赖 `fireImmune`），**实测确认一次是否还需要额外处理遮挡**。
- **验收**：站进岩浆不掉血、不着火；退出重进仍生效；关闭后立即恢复受伤。

### 85 鲛人之息

- **目标行为**：水下氧气条永远满。
- **参考实现**：`UltimateEvents.java:388` 附近（`airSupply < maxAirSupply` 时补满）。
- **落点**：并入 `KunJinKaoColdDataEffectsHandler` 的玩家 tick 段 + 面板个人开关。
- **要点**：直接 `player.setAirSupply(player.getMaxAirSupply())`，**不要**用水肺药水（会在 HUD 上多一个图标、且时长要续期）。
- **验收**：水下待 10 分钟氧气条不动；关掉后正常掉氧直至受伤。

### 87 碧波清眸

- **目标行为**：水下与岩浆中视野清晰，雾的距离/颜色被削弱。
- **参考实现**：`ClientVisionEvents.java:20-40`（`RenderFog` 事件把 near/far 设成 0/128、`ComputeFogColor` 设水/岩浆颜色）+ `FogRendererMixin.java:33`。
- **落点**：新增 `client/QolFogHandler.java`（优先事件）+ 必要时 `mixin/FogRendererMixin.java`（记得新增 `client` 段）+ 面板个人开关。
- **要点**：只在玩家处于水/岩浆且开关开启时改；`RenderFog` 事件里改 `setNearPlaneDistance` / `setFarPlaneDistance`，`ComputeFogColor` 里改颜色；若走 mixin，`setupFog` 里设置后再 `cancel`。
- **验收**：水下能看清 60 格外；岩浆里不再满屏橙；关掉立即恢复原版雾。
- **风险**：与光影（Iris/Oculus）冲突概率高，必须有独立关闭开关，并在文档里标注。

### 88 万民敬仰

- **目标行为**：常驻村庄英雄效果，等级可配（默认给到 5 级，对应"每级 +4 级"的换算口径）。
- **参考实现**：`UltimateEvents.java:295-305`（每 20 tick 施加，amplifier = `等级 × 4 - 1`，无限时长）。
- **落点**：并入现有 tick 段 + 面板个人开关 + config 等级。
- **要点**：amplifier 钳制在 0~255；如果不想让图标常驻，改成短时长续期；不要覆盖玩家自己喝的村庄英雄药水（比等级取高）。
- **验收**：村民交易折扣明显；图标不闪烁；关掉后折扣消失。

### 89 长臂善舞

- **目标行为**：`BLOCK_INTERACTION_RANGE`（挖/放/交互）与 `ENTITY_INTERACTION_RANGE`（攻击）各 +N 格。
- **参考实现**：`SkillEffects.java:62-63`（两个属性、`ADD` 操作、每级 +1 格）。
- **落点**：新增 `event/KunJinKaoReachHandler.java`；数值进 `AdminToolConfig`；生效条件按 3.1 的决策（持剑 / 授权）。
- **要点**：modifier 用固定 `ResourceLocation` + `addTransientModifier`，每次重算前先 `removeModifier`（幂等）；transient 不落盘，登录/重生/维度切换/配置重载时都要重新施加（复用现有 `PlayerEvent` 钩子）。
- **验收**：能隔 8 格点到方块与生物；属性面板能看到加成；关掉立即消失。

### 94 无限交易

- **目标行为**：与村民交易后，交易次数不减少、永不售罄。
- **参考实现**：`UltimateEvents.java:1259-1264`（交易后遍历 offers 调 `resetUses()`）。
- **落点**：新增 `event/KunJinKaoTradeHandler.java`（NeoForge 的交易事件）。
- **要点**：只重置商品使用次数，不动村民经验/声望；建议做成独立开关，与 95 分开（95 会永久改变村民数据，94 不改）。
- **验收**：连续交易 100 次，次数不减、不锁死；关掉后恢复原版。

### 95 村民大师

- **目标行为**：与某村民交易一次后，该村民直接满级（专家/大师），并解锁全部高级配方。
- **参考实现**：`UltimateEvents.java:1266-1280`（用反射调 `AbstractVillager#updateTrades` 逐级推到 5 级）。
- **落点**：与 94 同一个 handler。
- **要点**：**优先用公开 API**（`setVillagerXp` + `increaseMerchantCareer` / `restock`）而不是反射，反射只在公开 API 达不到效果时作为兜底；村民等级上限 5；流浪商人要么排除、要么在文档里说明。
- **验收**：新生成的村民交易一次后变为大师，商品数量与等级明显提升；重启后保持。

### 96 寻宝大师

- **目标行为**：一定半径内所有容器、考古刷扫点、刷怪笼/试炼刷怪笼显示穿透方块的高亮框。
- **参考实现**：`ClientTreasureEvents.java:79` 起（每 20 tick 扫一次，水平 64 格 / 垂直 128 格，按 81 个 chunk 遍历；判定用 `instanceof Container` 兼容模组容器，另加 `BrushableBlockEntity`、`Spawner`/`TrialSpawner`、实体侧 `ContainerEntity`；用 `DEBUG_LINES` 画穿透线框）。
- **落点**：新增 `client/QolTreasureScanner.java`（扫描 + 缓存）与 `client/render/QolTreasureRenderer.java`（`RenderLevelStageEvent` 的 `AFTER_BLOCK_ENTITIES` 阶段）+ 面板个人开关（服务端授权后才允许）。
- **要点**：
  - 扫描限流：每 20 tick 一次，单次最多处理 N 个 chunk（建议 16~32，留 config），结果缓存在客户端；
  - 只在渲染距离内绘制，超出直接跳过；
  - 用 `RenderType.lines()` + `disableDepthTest`；**画完 `endBatch()`**（交接文档坑 11）；
  - 判定集中在**一个方法**里，方便以后加容器类型；
  - 光影的 shadow pass 里跳过绘制。
- **性能指标**：扫描单次 < 1 ms；渲染单帧 < 0.3 ms；场景里 100 个容器不掉帧。
- **验收**：62 格外的箱子不显示、64 格内的显示；100 个箱子时帧数不明显下降；关掉后线条立刻消失。
- **风险**：容易被当成透视作弊，**默认关闭**、必须服务端授权；文档里写清。

### 119 战利品大爆发·机器共鸣

- **目标行为**：假玩家（模拟玩家机器）造成的击杀也享受额外掉落倍率，可单独开关。
- **参考实现**：`UltimateEvents.java:736-777`（`LivingDropsEvent` 内先做假玩家/开关判定，再套倍率与掉落模式）。
- **落点**：改造现有 `KunJinKaoExtraLootHandler`（已经解析出击杀者），加一条"击杀者是 `FakePlayer` 时走机器分支"；倍率与开关进 `AdminToolConfig [qol]`。
- **要点**：
  - 判定顺序：先 `FakePlayer` 再倍率，真人掉落逻辑一律不动；
  - 第一版**只做倍率**，不要一上来做四种掉落模式与黑名单（复杂度不成比例）；
  - UI/文档明确标注"弱兼容：不保证在所有机器上生效"。
- **验收**：用一台假玩家机器击杀生物掉落翻倍；真人击杀与原来完全一致；开关能立即生效。
- **风险**：各模组机器的假玩家实现差异大，可能部分机器无效——这是预期行为，需在文档声明。

### 127 机关拆解·选区挖掘（最大件）

- **目标行为**：手持工具左键框选一块区域，按键执行后一次性把区域挖空（含流体），有进度提示与撤销。
- **参考实现**：`ZoneSkillEvents.java:304-423`（掉落判定与批量逻辑）+ `ZoneWorkModule`（分 tick 预算与分批）。
  - 关键行为：无容器绑定时 `needDrops = false`（方块清掉但**零掉落**）；每列每 `BlockState` 只掷一次掉落骰再乘数量；基岩与流体被无条件清除；9 类"不可破"方块跳过；容器装不下的余量会销毁。
  - 该模组自己的文案写"单边 ≤64"，但代码实际是 512 —— **本工程按 64 做**。
- **落点**：复用既有框架 `PlacementCoreHandler`（批处理/撤销）、`SwordPlacementRegistry`（持久化）、`PlacementUndoHistory`（回滚）、`CollectBlockPayload`（同步）、`SwordToolHandler`（手杖交互）；新增：
  - `world/ZoneSelectionRegistry`（选区数据：维度 + 两个角点 + 所有者）
  - `world/ZoneExcavationJob`（作业队列：分批、进度、取消）
  - 客户端选区线框渲染（可复用 `SwordWorldEffects` 的绘制方式）
- **要点**：
  - **分批**：每 tick 预算 45 ms（config 10~48），批边长 256（config 32~512）；
  - **方块黑名单**：屏障、命令方块、结构方块、拼图方块、末地门框架/传送门等（照参考实现的 9 类，落成一个 tag 或 id 集合，做成可配）；
  - **掉落策略三选一**（config）：进玩家背包 / 生成掉落物 / 直接销毁；默认建议"进背包"，因为本工程没有容器绑定系统；
  - **联动顺序**：先走本工程已有的自动熔炼（`KunJinKaoAutoSmeltHandler`）与矿物掉落（`KunJinKaoOreDropHandler`），最后再套倍率；
  - **撤销**：复用 `PlacementUndoHistory`，但要为大量方块快照设上限（例如每次作业最多记 N 个方块，超出则禁用撤销并提示）；
  - **权限与防误操作**：默认仅管理员/持剑者可用；执行前二次确认；单边上限 64。
- **性能指标**：64 × 64 × 全高 的一次作业，MSPT 增量 < 10，不掉 TPS，有进度提示。
- **验收**：框选 64 格单边区域后 10 秒内挖完、不卡顿；黑名单方块保留；掉落符合配置；撤销能还原（在记录上限内）；非授权玩家无法触发。
- **风险**：地形不可逆 → 二次确认 + 撤销 + 权限 + 上限，四道缺一不可。

---

## 五、统一功能面板（甲 + 乙 的合并方案）

> 这一节是面板的施工说明：结构、布局数值、交互映射、网络与权限、逐功能呈现、以及服主如何兜底禁用。
> 面板取代了原来"服主改 toml、玩家点 HUD"的两套入口；toml 仍然存在，但降级为**存储与兜底**，不再要求人手改。

### 5.1 设计目标

1. 一个入口看全 15 项功能，不用记"哪项在 toml、哪项在 HUD"。
2. 服务器级与个人级在同一张列表里，用**徽章 + 置灰**区分权限，而不是分成两个界面。
3. 交互沿用技能树模组那套：右键开关、滚轮调数值、行尾绑定快捷键、顶部搜索，玩家零学习成本。
4. 所有改动服务端权威：客户端只发意图，服务端校验后回执并落库。

### 5.2 面板结构

- **标题栏**：面板名（例如"功能控制台"）+ 右上角两个按钮：`≡ 界面设置`、`× 关闭`。
- **分类标签条**：横向胶囊标签，建议 5 个分类，顺序按用途：
  1. `生存`（80 夜视、84 烈焰不侵、85 无限呼吸、87 水下视野）
  2. `收益`（72 经验飞涨、94 无限交易、95 村民大师、96 寻宝大师、88 万民敬仰）
  3. `便捷`（83 凌空采掘、89 长臂善舞、70 万载不磨）
  4. `机器与工程`（119 机器共鸣、127 选区挖掘）
  5. `服务器级`（把 72 倍率、77、96 总闸、119 倍率、127 参数这些服务器级行集中一列，方便管理员定位）
- **功能行列表**：单列纵向滚动，每行从左到右 = 序号徽章 / 图标 / 名称 / 状态文案 / 数值条（可调项）/ 权限徽章 / 快捷键格（🔑）。
- **搜索框**：放在列表上方，支持按中文名、英文 id、序号（如输入 `72`）过滤，实时显示"找到 N 项"。
- **子界面**：`界面设置`（缩放模式、亮度/主题、面板位置）、`执行确认`（127 的二次确认）、`进度`（127 执行中，可复用主面板行内的进度条）。

### 5.3 布局与缩放数值（照技能树界面移植，可直接复用）

| 项 | 值 |
| --- | --- |
| 整体边距 | 左右 8 |
| 标题栏 | y 13 ~ 34 |
| 分类标签条 | y 36 ~ 66；标签宽 = 文字宽 + 16，标签间距 3，可滚轮横滚 ±12 px |
| 列表区 | y 68 ~ 高度 − 41；行高 28、行步长 31、行宽上限 603 |
| 行内左右内缩 | 左 18、右 = 宽 − 29 |
| 列表滚轮步长 | 93 px |
| 缩放公式 | `r = 3z / s`（`s` 为原版 GUI 缩放）；自适应 `z = min(1, 宽/1920, 高/1080)`，手动 `z = (100 + off) / 100`（`off ∈ [-50, 100]`），最后再夹 `z ≤ max(0.5, 宽/1215)` |
| 自适应下的设计画布 | 640 × 360 |

### 5.4 交互映射

| 操作 | 行为 |
| --- | --- |
| 左键点行 / 点开关格 | 切换开或关（个人级项需已授权，否则只提示） |
| 右键点行 | 同"切换开关"（与技能树界面一致，避免玩家按错） |
| 滚轮在数值条上 | 调数值：步进 1，按住 Shift 步进 10，Ctrl + Shift 步进 100；到上下限停住 |
| 滚轮在列表空白处 | 滚动列表（步长 93 px） |
| Shift + 滚轮在分类标签条 | 横向滚动分类 |
| 点行尾 🔑 | 绑定快捷键，三格：**开关键** / **数值键**（等价滚轮）/ **执行键**（仅 127 这类有动作的） |
| Ctrl + R | 重置当前行的值到默认（服务器级项需 OP，改动同样写回 toml） |
| 顶部搜索框 | 过滤行；输入序号可直达 |
| Esc | 关闭面板 |

### 5.5 行的显示与状态

- **权限徽章**：`服务器级` 用琥珀色，`个人` 用青色；未授权/非 OP 时整行降饱和度并把交互格置灰，鼠标悬停显示原因（"需要管理员权限" / "需要先通过密码授权"）。
- **数值条**：类似技能树界面的进度条——底槽 = 当前值/上限，滑块 = 默认值位置，右侧显示实际数字与单位（倍率、格、级、方块）。
- **状态文案**：`已开启` / `已关闭` / `不可用（服务器已禁用）` / `未授权`，与聊天栏回执保持一致。
- **行内提示**：`119` 与 `127` 这种有前置关系的，行内用小字标出依赖（"依赖：机器共鸣总开关"）。

### 5.6 网络与数据流

| 方向 | 包 | 内容 |
| --- | --- | --- |
| C2S | `QolSettingPayload` | `int settingId, int value`（`value` 对布尔项用 0/1；对数值项用真实值） |
| S2C | `QolStatePayload` | 全量或单条：`settingId, enabled, value, min, max, scope(服务器级/个人), authorized` |
| C2S | 复用 `SimpleActionPayload` | 打开/关闭面板、请求全量同步这类无参动作 |

- 打开面板：客户端发一次"请求全量"，服务端回一条 `QolStatePayload` 全量（按位或按条列表，实现自选）。
- 每次改动：客户端发 `QolSettingPayload` → 服务端校验（OP 等级或授权 + 数值范围 + 前置依赖）→ 落库 → 回执单条 `QolStatePayload` → 客户端刷新该行并弹聊天提示。
- **`NetworkHandler.PROTOCOL_VERSION` 当前是 `"22"`，只要动注册表就必须 +1**（现有注释里写了原因）。落地时把新 `settingId` 表写进 `QolSettingPayload` 文件头，照 `SwordSettingPayload` 的做法。

### 5.7 权限模型

| 项类型 | 谁能改 | 校验点 |
| --- | --- | --- |
| 服务器级 | OP（权限等级 2） | 服务端 `source.hasPermission(2)`；非 OP 只读可见 |
| 个人级 | 已通过密码授权的玩家 | 复用现有 `PasswordAdminSavedData` / `SubmitAdminPasswordPayload` 流程 |
| 只读行 | 所有人 | 面板里显示状态与说明，不做交互 |

- 例外：`88`、`89` 这类既有个人开关又有服务器级上限的项，采用**两行**呈现（"我的等级"个人行 + "全服上限"服务器级行）或者一行内两个控件，落地时二选一，务必在文档里写清。
- 收回授权后，客户端下一次同步就会把个人级行置灰，服务端同时拒绝执行。

### 5.8 持久化

| 数据 | 存放 | 说明 |
| --- | --- | --- |
| 服务器级功能值 | `AdminToolConfig` 的 `[qol]` 分节（toml） | 面板改动走 `SPEC.set` + `SPEC.save()`，与手改文件双向一致 |
| 个人级开关与数值 | 新增 `config/QolStateSavedData` | 仿 `world/SwordPlacementRegistry`：挂 overworld 单例，`UUID → 位掩码/数值表` |
| 面板界面状态 | 客户端 JSON（例如 `config/kunjinkao-qol-panel.json`） | 字段照技能树模组：`selectedCategory`、`scrollY`、`catScrollX`、`guiScaleOffset`、`adaptive`、`darkMode`、`subScreenPos`、`binds`（开关键/数值键/执行键三张表） |
| 剑侧开关（70/83） | 剑的 dataTag | 不进面板存储，面板只读展示 + 跳转按钮 |

### 5.9 逐功能在面板里的呈现

| 序号 | 分类 | 行类型 | 交互 | 默认 | 权限 |
| --- | --- | --- | --- | --- | --- |
| 70 | 便捷 | 开关（镜像剑设置） | 面板只读 + "打开剑设置"跳转 | 关 | 所有人可见 |
| 72 | 收益 / 服务器级 | 数值 + 开关 | 滚轮调倍率（1.0 ~ 101.0），1.0 即关闭 | 1.0 | 服务器级 |
| 77 | 收益 / 服务器级 | 开关 + 数值 | 开关 + 滚轮调经验成本 | 关 / 10 级 | 服务器级 |
| 80 | 生存 | 开关 | 点击/右键切换（与现有 HUD 夜视同一状态） | 关 | 个人 |
| 83 | 便捷 | 开关（镜像剑设置） | 面板只读 + 跳转 | 关 | 所有人可见 |
| 84 | 生存 | 开关 | 点击/右键切换 | 关 | 个人 |
| 85 | 生存 | 开关 | 点击/右键切换 | 关 | 个人 |
| 87 | 生存 | 开关 | 点击/右键切换（纯客户端效果） | 关 | 个人 |
| 88 | 收益 | 数值 | 滚轮调等级（0 ~ 服务器上限） | 0 | 个人（上限服务器级） |
| 89 | 便捷 | 数值 | 滚轮调格数（0 ~ 服务器上限） | 0 | 个人（上限服务器级） |
| 94 | 收益 | 开关 | 点击/右键切换 | 关 | 个人 |
| 95 | 收益 | 开关 | 点击/右键切换 | 关 | 个人 |
| 96 | 收益 / 服务器级 | 开关 + 数值 | 服务器总闸（OP）+ 个人开关 + 半径 | 全关 / 64 | 服务器级 + 个人 |
| 119 | 机器与工程 / 服务器级 | 开关 + 数值 | 服务器倍率（OP）+ 个人开关 | 1.0 / 关 | 服务器级 + 个人 |
| 127 | 机器与工程 / 服务器级 | 总闸 + 参数 + 执行行 | 总闸与参数（OP）；执行行在面板里发起框选与执行 | 关 | 服务器级 + 需授权 |

### 5.10 127 在面板里的运行时流程

1. 服主（OP）在面板 `服务器级` 分类里打开 `选区挖掘` 总闸，并设定 `单边上限`（建议 64）、`掉落策略`（进背包 / 生成掉落物 / 销毁）、`每 tick 预算`（45 ms）、`批边长`（256）、`撤销上限`。
2. 获授权玩家在 `机器与工程` 分类的 `127` 行点"开始框选" → 面板收起，客户端进入框选模式（左键点两个角，画面画线框）。
3. 框选完成后自动弹出**确认子界面**：显示区域尺寸、方块数、掉落策略、预计耗时、撤销占用。
4. 确认后服务端建作业、分批执行；主面板该行显示进度条与"取消"按钮；聊天栏同步开始/进度/完成提示。
5. 点"取消"立即停止；误操作可在记录上限内点"撤销"。
6. 掉落顺序：先走本工程已有的自动熔炼与矿物掉落逻辑，最后套 `119` 的倍率（如果同时开启）。

### 5.11 服主如何彻底禁用

1. **面板里的总闸**：`96`、`127`、`119` 这类带总闸的项关掉后，客户端所有行都显示"不可用（服务器已禁用）"，个人开关也点不动，服务端同样拒绝。
2. **toml 兜底**：直接编辑 `config/kunjinkao-admin.toml` 的 `[qol]`，效果与面板一致（面板下次打开就读到），适合写进服务器初始化脚本或整合包默认配置。
3. **收回授权**：清掉玩家的密码授权后，个人级行全部置灰，服务端拒绝执行。
4. **不发剑**：`70`、`83` 依赖手持本模组剑，不发剑即等于禁用。

## 六、里程碑与排期建议

| 里程碑 | 内容 | 预估 | 可发布版本 |
| --- | --- | --- | --- |
| **M1 基础设施 + 功能面板 + 简单项** | 新增 `client/gui/QolControlScreen` 统一功能面板（分类标签条 + 行框架 + 开关/数值交互 + 搜索 + 快捷键绑定 + 客户端 JSON 持久化）；`QolSettingPayload` / `QolStatePayload`；`NetworkHandler.PROTOCOL_VERSION` +1；`AdminToolConfig` 新增 `[qol]` 分节；新增 `QolStateSavedData`；新增面板打开按键；完成 **80、83、85、88、89** | 1.5 天 | 0.9.8 |
| **M2 事件与 mixin 项** | `ItemStack` / `Entity.fireImmune` 两条 mixin 与登记；完成 **70、72、77、84、94、95** | 1 天 | 0.9.9 |
| **M3 大件** | 客户端雾效（**87**，含光影兼容）；扫描与渲染（**96**，面板里加"总闸 + 个人开关 + 半径"三行）；机器共鸣（**119**，面板里加"服务器倍率 + 个人开关"两行）；作业引擎 + 撤销 + 面板内执行与确认（**127**） | 2~3 天 | 0.10.0 |

每个里程碑结束都要：本机构建 → 自动部署到测试实例 → 自己在游戏里过一遍验收 → commit（不 push）。

---

## 七、测试计划

- **单人验收**：第五节每项功能的"验收"逐条执行（建议列成勾选表）。
- **多人/服务器**：授权与非授权玩家各测一遍；`119` 必须在真实机器上测；`96` 要在有光影与无光影两种环境下测。
- **边界**：
  - 效果叠加：`80` + 光影；`88` + 自身药水；`89` + 其它模组改过 interaction range。
  - 生命周期：死亡重生、换维度、退出重进、配置热改后是否仍正确。
  - `70`：与剑的 `UNBREAKABLE`、与其它模组的耐久减免同时存在时的叠加关系。
- **性能**：`96` 扫描耗时与帧率；`127` 作业期间的 MSPT（F3 或 spark 采样）。
- **回归（重点）**：本工程已经占用 `LivingDropsEvent`（`KunJinKaoExtraLootHandler`）与 `PlayerEvent.BreakSpeed`（`SwordMiningPenaltyHandler`）两个钩子，`119` 与 `83` 都要改这两个文件，必须回归：秒杀、斩首/刷怪蛋掉落、放置与撤销、战术 HUD、加速器。

---

## 八、风险与回滚

| 风险 | 影响 | 规避 | 回滚 |
| --- | --- | --- | --- |
| mixin 与其它模组冲突（`hurtAndBreak`、`fireImmune`） | 启动崩溃或功能失效 | 显式 `require = 0`；只判定 `ServerPlayer` / `Player`；加总开关 | 删掉 mixin 登记项并重新构建 |
| `87` 与光影冲突 | 画面异常 | 独立的"关闭"开关；实测 Iris 后再决定默认值 | config 关掉 |
| `96` 被视为作弊 | 服务器不接受 | 默认关闭 + 服务端鉴权 | config 关掉 |
| `127` 误操作毁地形 | 玩家损失 | 二次确认 + 撤销 + 权限 + 单边上限 | 撤销；无撤销时只能用备份 |
| 忘改 `PROTOCOL_VERSION` | 新旧端握手兼容但解码错位 | 把"动注册表就 +1"写进提交检查清单 | 立即 +1 重发 |
| 假玩家机器不生效 | 功能形同虚设 | 文档标注"弱兼容"，先在 1~2 台常用机器上验证 | 开关默认关 |

---

## 九、交付物与完成定义

- **代码**：第四节各功能"落点"里列出的新增/修改文件。
- **配置**：`AdminToolConfig` 的 `[qol]` 分节（每项给中文注释与合理默认值，全部默认**关**或保守）。
- **文档**：本计划 + `docs/HANDOFF.md` 增补四块：新增 mixin 清单、新增配置项（`[qol]` 全表）、新增网络 id 表（`QolSettingPayload.settingId` 与 `QolStatePayload.stateId`）、**功能面板的打开按键与界面状态文件字段**。
- **验收**：15 项逐条通过；现有功能无回归；CI success。
- **版本节奏**：0.9.8 → 0.9.9 → 0.10.0，按里程碑递增 `gradle.properties` 的 `mod_version`；每次构建后 commit，不 push。

---

## 十、施工须知（本工程已经踩过的坑，照做即可）

1. `write` / `edit` 工具会报 `EISDIR`：改用 pwsh 的 `[IO.File]::WriteAllText($p, $s, [Text.UTF8Encoding]::new($false))`。
2. `.java` 绝对不能带 BOM；`.ps1` 必须带 BOM。
3. pwsh here-string 里出现 `'@` 会提前截断外层，写 Java 文件时改用字符串数组拼接。
4. 两个 `@Redirect` 打在同一个调用点会冲突崩溃：能 `@Inject` 就 `@Inject`。
5. `RenderLevelStageEvent` 没有 `getMultiBufferSource()`：用 `renderBuffers().bufferSource()`，画完 `endBatch()`。
6. 构建（游戏必须关掉，否则部署任务故意失败）：
   `$env:JAVA_HOME = 'C:\Program Files\Java\jdk-21.0.11'`，然后用 `gradle.bat -p F:\mcmodli\tang\neoforge1.21.1 build --console=plain`。
7. 版本号在 `gradle.properties` 的 `mod_version`，它决定 jar 名。

### 参考源码（子枫技能树 v1.5.1）在哪

- 已反编译的源码树：`F:\mcmodli\tang\neoforge1.21.1\.dsh_tmp\skilltree-src\org\zifeng\skilltree\`（临时目录，可能被清理）
- jar 副本：`C:\Users\www\.dsh\attachments\v1\files\17\175a3f8ec1b81201521aeda245fafd9865ee6ea0578e4bda3c12e157cb0bd61e\zifeng_s_custom_skill_tree-1.21.1-NeoForge-1.5.1.jar`
- 需要重建反编译结果时：
  `java -jar C:\Users\www\.gradle\caches\modules-2\files-2.1\org.vineflower\vineflower\1.10.1\4f48c5947b21f9ebc743e7c80215ee839d3dc668\vineflower-1.10.1.jar -dgs=1 -hes=0 -hdc=0 <jar> <输出目录>`

---

## 十一、附录：参考实现位置速查

| 序号 | 功能 | 参考文件（子枫技能树） | 关键实现点 |
| --- | --- | --- | --- |
| 70 | 万载不磨 | `mixin/ItemStackMixin.java:22-36`、`skill/SkillEffects.java:294-297` | `@ModifyVariable` 改 `hurtAndBreak` 的 amount |
| 72 | 经验飞涨 | `event/UltimateEvents.java:1244-1253`、`SkillEffects.java:289-292` | `LivingExperienceDropEvent`，倍率 = 1 + 2×等级 |
| 77 | 不朽铭文 | `event/UltimateEvents.java` 的 `AnvilUpdateEvent` 链 | 同物品 + `UNBREAKABLE` 组件 |
| 80 | 星瞳夜视 | `event/UltimateEvents.java:275-287` | 每 20 tick 续期夜视 |
| 83 | 凌空采掘 | `event/UltimateEvents.java:540-550` | 判定 flag，实际乘回在 `BreakSpeed` |
| 84 | 烈焰不侵 | `mixin/EntityFireImmuneMixin.java:20-36` | `@Inject` `Entity#fireImmune` |
| 85 | 鲛人之息 | `event/UltimateEvents.java:388` 附近 | 每 tick 补满空气 |
| 87 | 碧波清眸 | `client/ClientVisionEvents.java:20-40`、`mixin/FogRendererMixin.java:33` | `RenderFog` + `ComputeFogColor` |
| 88 | 万民敬仰 | `event/UltimateEvents.java:295-305` | amplifier = 等级×4−1，每 20 tick |
| 89 | 长臂善舞 | `skill/SkillEffects.java:62-63` | 两个 interaction range 属性，ADD |
| 94 | 无限交易 | `event/UltimateEvents.java:1259-1264` | `MerchantOffer#resetUses()` |
| 95 | 村民大师 | `event/UltimateEvents.java:1266-1280` | 推到 5 级（参考实现用了反射） |
| 96 | 寻宝大师 | `client/ClientTreasureEvents.java:79` 起 | 每 20 tick 扫 64×128，穿透线框 |
| 119 | 机器共鸣 | `event/UltimateEvents.java:736-777` | 假玩家判定 + 倍率 |
| 127 | 选区挖掘 | `event/ZoneSkillEvents.java:304-423`、`system/ZoneWorkModule.java` | 分批 + MSPT 预算 + 掉落策略 |

---

*本计划基于对子枫技能树 v1.5.1 的反编译阅读，以及对本工程 0.9.7 现有实现（入口注册、网络、HUD 函数、SavedData、放置/撤销框架）的确认。文中标注"实测确认"的条目是唯一无法从源码直接确认的部分。*
