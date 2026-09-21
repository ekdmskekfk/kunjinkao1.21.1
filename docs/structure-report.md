# kunjinkao（NeoForge 1.21.1）结构报告与体检结论

- **范围**：`src/main/java` 全部 102 个 `.java`（9,464 行）；另核对 `src/main/resources`、`docs/`、`gradle.properties`、`build.gradle`、`.github/workflows/`、Git 仓库状态
- **方法**：按包分 5 路逐文件通读 + 关键路径人工复核（构建与 CI、网络协议、管理员密码门、剑本体、加速方块）
- **基线**：提交 `f9b1b98`（工作区另有一处未提交的 `gradle.properties` 版本自增）
- **审计方式**：全部结论都带 `文件:行` 证据；无法当场证实的标注「待确认」

---

## 0. 一句话结论

这是一个**工程卫生相当好、但权限模型有结构性缺口**的娱乐模组：102 文件 9,464 行，`TODO/FIXME` 为 0，网络层完整用上了 1.21.1 的 `PayloadRegistrar` + `StreamCodec`，服务端对剑的全部数值都做了夹紧，密码比较用常量时间实现、空密码一律拒绝——这些都超出一般个人模组的水平。真正需要动手的是三件事：**加速方块有一条完全不设防的写通道（可卡服）**、**管理员身份是可转移且不可撤销的**、**36 个网络包有 22 个是模板克隆、手写编解码已经产生真实缺陷**。

---

## 1. 工程与构建体检

### 1.1 构建与 CI ✅

| 项 | 状态 |
|---|---|
| `build.cmd build --offline` | **BUILD SUCCESSFUL**（Gradle 8.14.3 + JDK 21.0.11，9 秒，走本地缓存） |
| CI `.github/workflows/build-and-prerelease.yml` | 可用：push 到 `main` 触发，用 ubuntu + 外置 Gradle 8.14.3 构建并更新 `prerelease` Release（实测成功） |
| 产物 | `build/libs/kunjinkao_neoforge_1.21.1-<版本>.jar` + `-sources.jar`；`build` 结束后由 `copyModJar` 任务复制主 jar 到 `F:/mcmodli/tang/neoforge1.21.1mod` |
| 本地 `gradlew` | **不可用**：被改成指向 `<project>/lib/gradle-gradle-cli-main-8.12.1.jar`，而 `lib/` 不在仓库里。CI 注释里说明这是有意为之，`build.cmd` 已按 PATH → 缓存发行版 → gradlew 的顺序兜底 |
| `test` 任务 | **NO-SOURCE**：`src/test` 不存在，一个测试都没有；`gameTestServer` run 配置已就绪但无用例 |

### 1.2 版本号机制（设计自洽，但有副作用）

`build.gradle` 的机制是：`version = nextModVersion`（`mod_version` + 1），同时 `processResources` 把占位符 `mod_version` 也映射成 `nextModVersion`，所以 **jar 文件名与 jar 内 `neoforge.mods.toml` 的版本永远一致**（实测 0.1.9 jar 内部就是 `version="0.1.9"`）；`build` 任务的 `doLast` 再把 `gradle.properties` 的 `mod_version` 改写成刚构建的版本。

- ✅ 不存在「文件名与声明版本对不上」的问题（此前怀疑过，已证伪）
- ⚠️ 副作用：**每次 `gradle build` 都会修改 `gradle.properties`**，工作区必然变脏；且 CI 每次构建都会产出「仓库里那个版本 + 1」的新版本号，本地与 CI 编号会持续错开

### 1.3 代码卫生 ✅

| 指标 | 数值 |
|---|---|
| Java 文件 / 总行数 | 102 / 9,464（均 93 行/文件） |
| `TODO` / `FIXME` / `HACK` / `XXX` | **0 / 0 / 0 / 0** |
| `printStackTrace` | 0 |
| `System.out.println` | 3（`KunJinKaoEntry.java:59`、`KunJinKaoDeathEventHandler.java` 2 处），其余 27 处日志走 log4j |
| `catch (Exception)` 吞异常 | 6 处（其中 `client/KunJinKaoClientOverwriteEffects.java:232-258` 三处音效、`:89-98` 结束闪光） |
| 网络 API | `PayloadRegistrar` + `StreamCodec`（现代写法），36 个包，`StreamCodec.composite` 使用 0 次 |
| 事件 | `@SubscribeEvent` 47 处、`@EventBusSubscriber` 6 处 |
| 权限/OP 检查 | **0 处**（`hasPermission` / `isOp` / `PermissionLevel` 全项目无匹配）——见第 3 节 |

### 1.4 资源与本地化 ✅（除工具提示）

- `zh_cn.json` 与 `en_us.json` **各 80 键、完全对齐，0 缺失**
- 代码中 44 个 `translatable()` 键里只有 2 个不在语言文件内，且均为原版键（`item.minecraft.diamond_sword`、`item.modifiers.mainhand`）——不是问题
- ⚠️ **真正的本地化缺口在工具提示**：`KunJinKaoSwordItem.java:250-283` 用 `Component.literal()` 硬编码了 15 行中文（"上古代码洪流中遗落的碎片所铸…"），英文环境下仍显示中文；`KunJinKaoSwordItem.java:402-407` 的掠夺模式提示同样用了 `§6`/`§7` 旧式颜色码
- 资源齐全：4 张贴图、15 个模型、2 个 blockstate、5 个 data 文件（自定义维度 ×2、配方 ×1、战利品表 ×2）
- `docs/kunjinkao-admin.toml.example` 与实际配置键（`["admin"] password`）一致 ✅

### 1.5 文档与实现的一致性 ⚠️

`docs/` 下有 4 份文档（合计 613 行）：两份「AI 编码任务书」+ 五主题设计表 + 管理员配置样例。

- `overwrite_themes.md` 与实现高度吻合（主题 0..4、阶段一/二/三、`intervals` 语义、NBT `OverwriteTheme` 字段都对得上）✅
- ⚠️ **规格漂移**：`neoforge-1.21.1-tactical-hud-ai-spec.md` 要求的管理员机制是 **UUID 白名单配置**（`config/tactical-hud-admin.toml`），实现改成了 **密码 + 存档 UUID 集**（`config/kunjinkao-admin.toml`）。两者都不是错的，但那份任务书没有随实现更新，会误导后续维护者

---

## 2. 架构总览

### 2.1 包结构（102 文件 / 9,464 行）

| 包 | 文件 | 行数 | 职责 |
|---|---|---|---|
| `kunjinkao`（根） | 6 | 868 | 入口、剑本体、三个注册表、主题数据表 |
| `network` | 40 | 1,283 | 36 个 payload + 注册中心 + 数据 record |
| `event` | 14 | 1,560 | 服务端玩法处理（死亡、保护、矿石掉落、覆写联动…） |
| `client`（顶层） | 15 | 1,936 | 客户端状态、每 tick 驱动、键位、覆写演出 |
| `client/gui` | 3 | 872 | 剑设置界面、管理员密码界面、加速器界面 |
| `client/hud` | 5 | 672 | 可交互战术 HUD 与实体/排除名单界面 |
| `client/render` | 6 | 728 | 眼睛 HUD 层、蜂巢护盾层、第三人称取剑层、真隐形处理 |
| `client/function` | 2 | 82 | HUD 功能开关管理 |
| `config` | 3 | 220 | 管理员配置、密码授权存档、终极死亡存档 |
| `block` + `block/entity` | 3 | 260 | 加速方块、世界门方块及其方块实体 |
| `overwrite` | 1 | 569 | 覆写·断未的 40 tick 服务端时序（最大单文件） |
| `entity` / `recipe` / `world` / `clientbridge` | 4 | 414 | 钻石投射物、管理员剑配方、主世界门、客户端间接层 |

### 2.2 启动流程

`KunJinKaoEntry`（`KunJinKaoEntry.java:34-60`）：MOD 总线上注册网络（`:35`）、三个注册表（`:36-38`）、COMMON 配置（`:39-40`）；游戏总线上注册 **12 个实例 handler + 3 个类 handler**（`:42-56`）。⚠️ `:59` 用 `System.out.println` 而不是 logger。

### 2.3 网络协议

- 单一注册中心 `NetworkHandler.register`（`network/NetworkHandler.java:17-55`），`PROTOCOL_VERSION = "19"`（`:12`，字符串精确匹配，改布局必须手动 +1）
- **36 个包：C2S 23 / S2C 13**；无 `optional()`，因此原版客户端无法连接（该模组必须双端安装，属设计选择）
- 每个包统一形状：`record implements CustomPacketPayload` + `Type` + 手写 `StreamCodec.of` + `static handle`，一律 `context.enqueueWork` 切主线程
- S2C 全部转发给 `clientbridge/ClientHooks.handleClientPayload`（`clientbridge/ClientHooks.java:38-43`），服务端不加载客户端类 ✅（`ClientHooks` 未初始化时会**静默丢弃**所有客户端包，无日志无断言 ⚠️）

### 2.4 客户端/服务端边界

- 服务端逻辑集中在 `event/`、`overwrite/`、`network/*` 的 handle；客户端逻辑集中在 `client/`，通过 `ClientHooks` 静态注入解耦 ✅
- 客户端状态**全部是静态字段的工具类**（15/15，`private` 构造），**没有任何生命周期清理钩子**（无 `ClientPlayerNetworkEvent.LoggingOut`、无 level unload 订阅）——见第 7 节，这是客户端最大的结构性问题
- 三种时间基准混用：tick 计数 / `level.getGameTime()` / `System.currentTimeMillis()`

---

## 3. 权限与信任边界（本次体检最需要关注的部分）

### 3.1 设计：密码提权 + 持剑即权限

- 管理员身份由**服务端配置里的密码**授予：玩家在游戏内提交密码 → 服务端校验 → 把 UUID 写进当前存档的 `PasswordAdminSavedData`（`config/PasswordAdminSavedData.java:42-46`）
- 管理员剑的能力**不看权限，只看手里有没有这把剑**——这是**有意设计**，`docs/kunjinkao-admin.toml.example:17` 明确写着「持有管理员剑的防护/覆写效果不看权限，只看是否持剑」
- 已授权玩家进入冒险模式时，服务端自动补发一把剑（`event/AdminAdventureSwordGrantHandler.java:39-50`，内含 `isAuthorized` 校验）
- 剑的合成受权限限制（`event/AdminSwordCraftingHandler.java:28`），未授权者合成会返还材料

### 3.2 做对的地方 ✅

- **密码存储位置正确**：`ModConfig.Type.COMMON`（`KunJinKaoEntry.java:39-40`），只存在于服务端本地，**不会同步给客户端**，也不会随 jar 分发
- **常量时间比较**：`MessageDigest.isEqual`（`config/AdminToolConfig.java:62`），避免时序侧信道
- **空密码一律拒绝**：配置缺失或留空时返回 `false`，不会退化成"空密码即管理员"（`config/AdminToolConfig.java:56-59`）；配置未加载时返回空串而非默认值（`:70-77`）
- **提交长度双重限制**：编码 `writeUtf(p.password, 64)`（`network/SubmitAdminPasswordPayload.java:17`）+ 服务端再查一次 `length() > 64`（`config/AdminToolConfig.java:52`）
- **8 个管理员入口的服务端复检**：`event/KunJinKaoColdDataEffectsHandler.java:134,140,157,174,195,202,246,253`（战术 HUD 开关、夜视、实体列表、实体管理、真隐形、磁吸、排除名单、解除排除），其中战术 HUD 回包用 `authorized && requestedEnabled`，**忽略客户端单方面声明** ✅
- **剑的全部数值在服务端夹紧**（`KunJinKaoSwordItem.java:151-155,163-167,186-190,216-220,115-117`），伪造 `Integer.MAX_VALUE` 也写不进去

### 3.3 问题清单

**P0 — 完整提权链：普通玩家 → 永久封禁全服任意玩家**（本次审计最严重发现，已对照 NeoForge 21.1.118 源码核实）

1. **拿到剑**：唯一的拦截点是 `event/AdminSwordCraftingHandler`，它挂在 `PlayerEvent.ItemCraftedEvent` 上。核实源码后确认：该事件**只有 `ResultSlot` 会触发**（`ResultSlot.checkTakeAchievements` → `EventHooks.firePlayerCraftingEvent`），而 1.21 新增的 **Crafter 方块**走的是 `CrafterBlock.dispenseFrom` → `CrafterBlockEntity.asCraftInput()` → `RecipeManager.getRecipeFor(...)`，这条路径**只按 `matches()` 过滤、没有 `isSpecial()` 过滤**。`AdminSwordRecipe` 是 `CustomRecipe`，8 木棍 + 中心圆石恒成立 → **未授权玩家用 Crafter 方块即可造出管理员剑**（产物只多一个无害的 `kunjinkao:pending_admin_sword_craft` 标签）
2. **写入致命开关**：`event/KunJinKaoColdDataEffectsHandler.java:287-291` 的 `withSword` 不校验 `isAuthorized`，10 个剑配置包任何客户端都能发 → 打开 `UltimateDeathEnabled`
3. **执行**：砍任意玩家 → `KunJinKaoSwordItem.applyExecutionMark` + `kill()` → `event/UltimateDeathHandler.java:50-51` 把对方 UUID 写进 `UltimateDeathSavedData` 并踢出 → `:67-69` 之后**永久拒绝其登录**
4. **受害者无法自救**：解除入口在管理员 HUD 的排除列表里，而那个入口**是有** `isAuthorized` 校验的 → 普通玩家打不开

**结论**：这条链把"管理员专属的永久封禁"变成了任何玩家都能用的武器，也是全项目**唯一一条"普通玩家可获得不可逆破坏力"的完整路径**。修它必须两处一起改：合成授权要覆盖 Crafter（或让 `AdminSwordRecipe` 参与 `isSpecial()`/改用 `ResultSlot` 之外的校验），同时补上 `withSword` 的授权缺口——只补一处都不够。
**P0 — 加速方块：唯一完全无授权的写通道，且可卡服**
`network/AcceleratorConfigPayload.java:30-48` 与 `network/AcceleratorShowRangePayload.java:30-51` 只校验「区块已加载」+「方块实体类型」，**无授权、无 OP、无交互距离校验**；`block/AcceleratorBlock.java:36-44` 对任何玩家无条件打开配置 GUI。任意玩家（或改造客户端直接发包）都能改写任意已加载区块内的加速方块。
放大效应（已人工复核）：倍率上限 1024、半径上限 4 → 9³=729 格，`block/entity/AcceleratorBlockEntity.java:54-85` 对每格执行 `multiplier-1 = 1023` 次 tick，即 **728 × 1023 ≈ 74.5 万次方块 tick/刻/单个方块**；随机刻路径（`:77-80`）同样按 1023 次循环。该方块实体**没有任何"主人"字段**，因此也没有天然的归属校验依据。这是稳定的服务端卡死手段。
附带问题：`:41-45` 的范围显示会把该方块实体数据发给**全服所有玩家（含其他维度）**，而不是只发给追踪该区块的玩家；`AcceleratorConfigPayload.java:41-42` 在 `sendBlockUpdated` 之后又手动重发一次方块实体数据包。

**P1 — 管理员身份"可转移 + 不可撤销 + 可暴力破解"三件套**
1. **可转移**：剑是权限凭据且是可掉落物品（`AdminAdventureSwordGrantHandler.java:39-47` 的补发逻辑本身也证明存在丢失/转移路径）。持有者丢剑给任何人，对方立刻获得全部剑能力——包括把玩家**永久加入排除名单**的"终极死亡"（`KunJinKaoSwordItem.java:411-432`、`event/UltimateDeathHandler.java`）
2. **不可撤销**：`PasswordAdminSavedData` **只有 `authorize`，没有 revoke**（`config/PasswordAdminSavedData.java:42-46`）；改 `config/kunjinkao-admin.toml` 的密码**不会**影响已授权的 UUID，`isAuthorized` 也完全不看 OP 等级 → 一旦有人知道过密码并验证成功，改密后他仍是管理员，直到换存档
3. **可暴力破解**：`network/SubmitAdminPasswordPayload.java:23-30` 每次尝试直接比对，**无限流、无失败计数、无锁定**；默认密码 `118329` 硬编码在源码里（`config/AdminToolConfig.java:25`，注释亦承认"默认值是公开的"）→ 6 位纯数字 10⁶ 空间，单连接脚本可在数小时内穷举；任何没改配置的服务器等于对外开放管理员权
4. **信息泄露（低）**：`event/AdminEyeSyncHandler.java:20-26,31` 把每个在线玩家的授权状态广播给所有客户端（`network/AdminEyeStatePayload.java`），任何玩家都能知道"谁是管理员"

**P2 — 授权检查不对称**
`KunJinKaoColdDataEffectsHandler` 里 8 个入口有 `isAuthorized` 守卫，但**另 9 个"剑设置"入口没有**（`event/KunJinKaoColdDataEffectsHandler.java:207,211,215,219,223,227,231,235,239`：蓝屏攻击、挖掘速度、不可破坏方块、范围清除模式、伤害上限、矿物掉落倍率、掠夺模式、终极死亡、退出打击）。这些包客户端可任意发送，服务端直接写进手上剑的 NBT。
目前靠"没有剑就没有这些能力"兜着（`withSword` 只判 `stack.getItem() instanceof KunJinKaoSwordItem`，`:287-291`），所以**不是可直接利用的提权**，但一旦第 P1 条的剑转移/复制路径成立，这 9 个入口就是无限增益的入口。建议补齐守卫（一条 `if (!AdminToolConfig.isAuthorized(player.getUUID())) return;`）。

---

## 4. 核心机制：管理员剑（`KunJinKaoSwordItem`，479 行）

- **属性与宣称不符**：构造器只给了 `SwordItem.createAttributes(tier, 3, -2.4F)`（`KunJinKaoSwordItem.java:63`），面板上那个 `∞ 攻击伤害`（`:251`）是**代码里直接 `target.kill()`** 实现的（`:326-358`）。功能上没问题，但排查"为什么伤害数值不对/为什么一击必杀"时会踩坑
- **状态存储**：全部放进 `DataComponents.CUSTOM_DATA` 的 NBT（11 个键，`:34-44`），伪装状态用 `CUSTOM_MODEL_DATA`（`:79-90`）。1.21.1 的正统做法是自定义 `DataComponentType`，现在是沿用了 1.20.1 的习惯（可用，但失去类型安全与自动同步语义）
- **伪装**：`getName` 返回 `item.minecraft.diamond_sword`、工具提示整段隐藏（`:234-247`），且 `use`/`hurtEnemy`/`getDestroySpeed`/`isCorrectToolForDrops` 全部分支到原版行为 ✅ 一致性好
- **范围清除**（右键，`:302-324`）：50 格半径、全高度的 AABB，主线程同步遍历 `getEntitiesOfClass`；玩家目标走 `applyExecutionMark`（`:427-432`）
- **做得很好的一处**：`applyExecutionMark` 与 `applyKunJinKaoMark` 被刻意分开，因为后者带"抢夺 25/50 级"标记，用在玩家身上会把对方整个背包复制几十份——注释（`:420-426`）把原因写清楚了 ✅
- ⚠️ **日志刷屏**：`hurtEnemy` 里每次命中都有 `LOGGER.info`（`:338,349,353`），战斗时 INFO 日志量等于攻击次数
- ⚠️ **冗余每 tick 写入**：`inventoryTick` 每 tick 把耐久写回 0（`:384-390`），而 `isDamageable()` 已返回 `false`（`:379-382`）——每个背包里的剑、每个玩家、每 tick 都做一次无意义的写入
- ⚠️ 风格不一致：`LOGGER` 常量声明在类中部（`:360`）而非顶部；掉落模式提示混用 `§6` 与 `ChatFormatting`

---

## 5. 加速方块（`block/` + `block/entity/`）

`AcceleratorBlockEntity` 的能力本身是**合规**的：倍率/半径只在白名单数组里取值（`:132-148`），NBT 载入时也夹紧（`:161-162`），避免手工改 NBT 越界 ✅。问题全部集中在**访问控制**（见 3.3 P0）与下面两点：

- **重复发包**：`network/AcceleratorConfigPayload.java:41-42` 先 `level.sendBlockUpdated(...)` 再 `player.connection.send(ClientboundBlockEntityDataPacket...)`，同一份数据发两遍
- **广播范围过大**：`network/AcceleratorShowRangePayload.java:41-45` 遍历 `getPlayerList().getPlayers()` 给全服（跨维度）发送方块实体数据，正确做法是 `sendBlockPlayersTrackingChunk` 或复用 `sendBlockUpdated`
---

## 6. `network` 包专项（40 文件 / 1,283 行）

### 6.1 协议分组（36 个包：C2S 23 / S2C 13）

| 组 | 方向 | 数量 | 服务端授权 |
|---|---|---|---|
| 剑设置（`client/gui/SwordOptionsScreen`） | C2S | 9 | ❌ **无**（仅校验手持物是 `KunJinKaoSwordItem`） |
| 剑开关（键位） | C2S | 3 | ❌ 无（同上） |
| HUD 开关（`client/hud/HudScreen`） | C2S | 4 | ✅ `KunJinKaoColdDataEffectsHandler.java:134,140,195,202` |
| 管理员实体管理 | C2S | 2 | ✅ `:157,174` |
| 管理员排除名单 | C2S | 2 | ✅ `:246,253` |
| 管理员认证 | C2S | 1 | ✅ 密码比对（`config/AdminToolConfig.java:51-67`） |
| 加速方块 GUI | C2S | 2 | ❌ **完全无校验**（见第 3 节 P0） |
| 覆写特效 / HUD 回执 / 列表 / 管理员身份 / 视觉同步 | S2C | 13 | —（服务端 → 客户端） |

### 6.2 样板重复：36 个里 22 个是 6 种模板的克隆

| 模板形状 | 数量 | 成员 |
|---|---|---|
| `(boolean enabled, boolean authorized)` | 4 | `HudMagnetStatePayload` / `HudNightVisionStatePayload` / `HudTrueInvisibilityStatePayload` / `TacticalHudStatePayload`（`…:11-26` 外壳逐字节相同） |
| `(InteractionHand, boolean)` | 4 | `ToggleBlueScreenAttackPayload` / `ToggleQuitStrikePayload` / `ToggleUltimateDeathPayload` / `ToggleUnbreakableBlockBreakingPayload` |
| `(InteractionHand, int)` | 5 | `SetAreaClearTargetModePayload` / `SetSwordAttackDamageLimitPayload` / `SetSwordLootingModePayload` / `SetSwordMiningSpeedPayload` / `SetSwordOreDropMultiplierPayload` |
| 空载荷 | 5 | `ToggleHudMagnetPayload` / `ToggleHudNightVisionPayload` / `ToggleHudTrueInvisibilityPayload` / `RequestExcludedPlayersPayload` / `RequestHudEntityListPayload` |
| `(InteractionHand)` | 2 | `ToggleDisguisePayload` / `ToggleOverwritePayload` |
| `(UUID, boolean)` | 2 | `AdminEyeStatePayload` / `HudTrueInvisibilityVisualPayload` |
| 13 个 S2C 的 `handle` 完全相同 | 13 | 全部转发 `ClientHooks.handleClientPayload` |

**收敛方案（36 → 约 12 类）**：NeoForge 要求每个 `CustomPacketPayload` 子类有唯一 `Type`，不能用泛型类注册多次，但可以按"形状 + 判别 id"合并——9 个剑设置类合成 `SwordSettingPayload(hand, settingId, value)`（服务端本来就是同一个 `withSword(...)`，`:287-291`，加个 switch 即可）；4 个 `(enabled, authorized)` 合成 `HudStatePayload(stateId, …)`；5 个空载荷合成 `SimpleActionPayload(actionId)`。同时把 36 处手写 `StreamCodec.of` 换成 `StreamCodec.composite(...)`（1.21.1 已提供），可省掉每类 6-10 行并消除字段顺序风险。
✅ 本次逐个核对了 36 个 codec 的字段顺序，**未发现错序或漏字段**（含条件字段与列表长度前缀）。

### 6.3 缺陷清单

| 级别 | 问题 | 证据 |
|---|---|---|
| 高 | 加速方块两个包无授权/无距离校验 + 74.5 万次 tick/刻的放大效应 | `AcceleratorConfigPayload.java:30-48`、`AcceleratorShowRangePayload.java:30-51`（详见第 3 节 P0） |
| 中 | **夜视状态用 `persistentData` 当真值**，效果 30 分钟自然到期（或被牛奶清除）后标记仍为 true → 玩家需按两次才能真正再开 | `KunJinKaoColdDataEffectsHandler.java:143-152`（应改判 `hasEffect(NIGHT_VISION)`） |
| 中 | **列表长度上限只在校验端**：编码不检查、解码要求 ≤16384 → 实体总数 >16384 的服务器上客户端解码抛异常，列表打不开 | `HudEntityListPayload.java:25-34` vs `:42`；`ExcludedPlayersPayload.java:27-31` vs `:38-40` 同一模式 |
| 中 | 管理员剑的战斗路径只判"手里有没有剑" | `KunJinKaoColdDataEffectsHandler.java:287-291`（详见第 3 节 P1） |
| 低 | `writeUtf(name, 128)` 硬上限：超长 CustomName 的实体会让整包发送失败 | `HudEntityListPayload.java:31`（来源 `:165` 的 `getName().getString()`） |
| 低 | 13 处用 `readEnum`（按 ordinal 取值）：改造客户端发越界序号会在 netty 解码线程抛 `AIOOBE` 掉线（自伤型，非提权） | `ManageHudEntityPayload.java:19`、`SetAreaClearTargetModePayload.java:18` 等 |
| 低 | `AreaClearTargetMode.byId` 对未知 id **静默回落 HOSTILE**，会把玩家的 ALL/PLAYERS 设置悄悄改掉 | `KunJinKaoSwordItem.java:472-477` → `KunJinKaoColdDataEffectsHandler.java:219-221` |
| 低 | 重复发包：`sendBlockUpdated` 之后又手动重发一次方块实体数据 | `AcceleratorConfigPayload.java:41-42` |
| 低 | 范围显示广播给**全服所有玩家（跨维度）** | `AcceleratorShowRangePayload.java:41-45` |
| 低 | 死字段：`terminalText` / `terminalLine` 被编解码但全项目无人读，工厂一律填 `""`/`0`；每个阶段包白带一个 UTF 串 + 一个 int（疑似移植时丢掉的"终端文本"特性） | `OverwriteEffectPayload.java:12,39-40,56-58,69-88` |
| 低 | 死方法 `NetworkHandler.modId()` 零调用；`sendToAllTracking(ServerPlayer entity, …)` 参数名与语义不符 | `NetworkHandler.java:73-75`、`:69` |

✅ 36 个注册项**没有"注册了但无人发送"**的包（已逐个核对发送点）。

### 6.4 结论

1. 加速方块是唯一"完全不设防的写通道"，且带卡服放大效应（P0）
2. 管理员身份=可转移物品 + 无速率限制的公开默认密码 + 不可撤销的存档授权（P1）
3. 样板膨胀与手写编解码已经产生真实缺陷（编解码上限不对称、超长字符串抛异常、死字段）

---

## 7. `client` 顶层包专项（15 文件 / 1,936 行）

### 7.1 状态管理形态

**15/15 全是 `private` 构造 + 全静态字段的工具类**：每个 feature 一个 State/Visual 类（`ClientHudState`、`AdminEyeVisualState`、`TacticalHudInvisibilityVisualState`、`ShieldHitVisualState`、`RemoteSwordDrawVisualState`、`KunJinKaoClientOverwriteEffects`），外加一个状态与渲染混装的 `KunJinKaoClientSwordVisuals`（8 个静态计时字段 + 粒子 + GUI 层绘制 + 物品模型谓词）。

三种时间基准混用：tick 计数（`ClientTickEvent.Post` 驱动）、`level.getGameTime()`、`System.currentTimeMillis()`（`KunJinKaoTooltipColorHandler.java:34`）。

### 7.2 客户端状态清理（本包最大结构性问题）

**全项目没有任何"退出世界/断线/切维度"的清理钩子**（grep 无 `ClientPlayerNetworkEvent.LoggingOut`、无 level unload 订阅）。现状：

| 类 | 是否重置 | 漏了会怎样 |
|---|---|---|
| `ClientHudState` | ✅ 有 `reset()`，但调用点藏在按键处理里（`KunJinKaoClientEvents.java:154-157`） | 能自愈 |
| `RemoteSwordDrawVisualState` | ✅ `level == null` 时 `clear()`（`:28-31`） | 自愈 |
| `KunJinKaoClientSwordVisuals` | ⚠️ `reset()` 在 player/level 为空时调 | 反向副作用：每次进世界重播编译动画（见 7.3） |
| `AdminEyeVisualState` | ❌ 无 | UUID 集只有增不减（有界泄漏），靠服务端登录重同步兜底 |
| `TacticalHudInvisibilityVisualState` | ❌ 无 | 同上 |
| `ShieldHitVisualState` | ❌ 无（`level==null` 时直接 return） | **确定可见的跨存档残留**：见 7.3 |
| `KunJinKaoClientOverwriteEffects` | ❌ 无显式清理 | 残留标记存绝对世界坐标 + 目标名；`PHASE_DETAIL` 无衰减规则 → 永久泄漏 |

**建议**：加一个统一的 `ClientPlayerNetworkEvent.LoggingOut` / level-unload 清理点，并给每个 State 类补一个 `reset()` 约定。

### 7.3 缺陷清单

| 级别 | 问题 | 证据 |
|---|---|---|
| 中（必然复现） | **覆写 HUD 投影 FOV 硬编码 `70.0`**，不读 `mc.options.fov()` → 任何非默认 FOV 的玩家，锚定在目标身上的文字都会偏离目标，距离越远越明显 | `KunJinKaoOverwriteHudOverlay.java:314`（受影响：`:103-109,129-143,169-176,211-224`） |
| 中 | `partialTick` 参数收了却全程不用，实体坐标取 `getX()` 不做插值 → 移动目标时文字比模型慢 1 tick、抖动（同项目 `HoneycombShieldLayer.java:85` 做法正确，不一致） | `KunJinKaoOverwriteHudOverlay.java:21,103,130,170` |
| 中 | 覆写 HUD 缺 `screen != null` 守卫 → 开着背包/暂停/死亡界面时残影仍绘制在界面之下 | `KunJinKaoOverwriteHudOverlay.java:22`（对照 `HudOverlayRenderer.java:31` 有守卫） |
| 中 | **无条件下 `setScreen`** → 玩家开着箱子/背包/加速器界面时收到服务端回包，当前界面被强行顶掉 | `TacticalHudClientPacketHandler.java:37,97,134` |
| 中 | 两个 Map 清理规则不对称：`REMAINING` 在 ≤1 时删、`PHASE_TICKS` 只在 <-100 时删 → 服务端未送 `PHASE_END`（重启/区块卸载/丢包）时，目标已消失但屏幕中央仍显示阶段三裁决约 5 秒，还会补一次终端音 | `KunJinKaoClientOverwriteEffects.java:133-137,143-147,186,167-168,264-282` |
| 中 | **跨存档串味（可见）**：用当前世界的 `gameTime` 与上个存档留下的 `expiryTick` 比较，切到 gameTime 更小的世界时条目不过期 → `alpha` 钳 1.0、`ageTicks` 为负，蜂巢护盾被定格显示，直到新世界 gameTime 越过旧值 | `ShieldHitVisualState.java:34-55`（服务端只在受击时发一次：`KunJinKaoProtectionHandler.java:221`，无重同步） |
| 低 | 早期 `return` 在 `consumeClick()` 之前 → 点击计数滞留在 `KeyMapping` 内，可能稍后被消费（开着背包按 H，关背包后 HUD 意外翻转） | `KunJinKaoClientEvents.java:154-164` |
| 低 | H 键有**两条竞争路径**：`Screen.keyPressed` 里只能关（`HudScreen.java:55-73`）vs tick 轮询里可开可关（`KunJinKaoClientEvents.java:158-161`），其中一条必然无效或重复发包（原版是否在 Screen 打开时派发 KeyMapping **待确认**） | 同上 |
| 低 | 每次进世界/切维度重播整套"取剑编译"动画 + 音效（`wasHoldingSword` 被 `reset()` 置 false） | `KunJinKaoClientSwordVisuals.java:64-79,393-402` |
| 低 | `getResidueMarkers()` 直接返回**可变内部 Map**，渲染端正在遍历它，而 `tick()` 会 `removeIf/replaceAll`（当前同线程不炸，但接口把并发风险暴露出去） | `KunJinKaoClientOverwriteEffects.java:223-225` ← `KunJinKaoOverwriteHudOverlay.java:207-208` |
| 低 | 每 tick/每帧分配：`getActiveEntityIds()` 每次 new HashSet（每 tick + 每帧各一次）；`KunJinKaoClientSwordVisuals.java:167-171` 在 48 帧内每帧 `ItemStack.copy()`+`copyTag()`+`new CustomData`+`set()`，首帧还遍历烘焙模型全部 quad 并写日志 | 同上 |
| 低 | 三处 `catch (Exception ignored)` 静默吞异常（音效、结束闪光） | `KunJinKaoClientOverwriteEffects.java:89-98,232-258` |
| 低 | Tooltip 染色改 `copy()` 出的 sibling 列表，依赖其返回 `MutableComponent`（脆弱）；`containsInfinity` 先 `getString()` 再递归，属冗余遍历 | `KunJinKaoTooltipColorHandler.java:47-70` |
| 低 | 硬编码 `modid = "kunjinkao"` 而非 `KunJinKaoEntry.MOD_ID`（改 id 时静默失效） | `KunJinKaoTooltipColorHandler.java:22` |
| 低 | **命名损坏**：`DRAW_COMPtLE_TtCKS`、`GRAB_SWORD_TtCKS`、`SCAN_WHtTE`、注释"GUt 模型"——小写 `i` 被批量替换成 `t`，只在这一个文件里出现，疑似移植期一次误伤式全量替换。能编译，但持续污染可读性与 grep | `KunJinKaoClientSwordVisuals.java:38,39,43,201` |

### 7.4 重复代码与死代码

- **同一套动画常量两份定义**：`RemoteSwordDrawVisualState.java:12-13`（48/14）与 `KunJinKaoClientSwordVisuals.java:38-39`（48/14）——本地与远端两套时钟必须手工同步
- **同一条 smoothstep 两份实现**：`KunJinKaoClientSwordVisuals.java:271-275` 与 `RemoteSwordDrawVisualState.java:45-49`（魔数 25/21 各写一遍）
- **协议时序常量三份**：`KunJinKaoClientOverwriteEffects.java:16-19`、`KunJinKaoOverwriteHudOverlay.java:15-16`、服务端 `KunJinKaoOverwriteHandler.java:62`（数值当前一致，但没有共享来源）
- 死代码：`setPhaseDetail`/`getPhaseDetail` 零调用（`KunJinKaoClientOverwriteEffects.java:64,178`，且该 Map 无衰减 → 永久泄漏）；`endFlash(int)` 单参重载零调用（`:79-81`）；`isLoadingPhaseActive`/`getLoadingTheme` 零外部调用（`:195-209`）；`getDrawCompileStage`/`noteCompileRenderObserved` 仅用于一条日志（`KunJinKaoClientSwordVisuals.java:247,252,258-263`）
- `clientbridge/ClientHooks.java:38-43` 是**静默降级**：若 `ClientModEvents` 未初始化，所有客户端包被无声丢弃，无日志无断言

### 7.5 结论

1. 客户端状态缺生命周期清理入口（结构性缺陷，已有可见的跨存档残留路径）
2. 覆写 HUD 的坐标/时序不忠实（FOV 硬编码 + 不插值 + 无界面守卫），影响面最大且必然复现
3. 战术 HUD 的界面生命周期与按键双路径互相矛盾（无条件 `setScreen` + `consumeClick` 早退）

---

## 8. 核心包 / 覆写 / 世界门专项（根包其余 5 文件 + `block` + `config` + `entity` + `recipe` + `world` + `overwrite`，2,218 行）

> 剑本体见第 4 节；加速方块见第 5 节；密码与授权见第 3 节。本节只写其余部分。

### 8.1 覆写·断未（`overwrite/KunJinKaoOverwriteHandler.java`，569 行，最大单文件）

**机制**：服务端静态 `STATES: Map<UUID, State>` + `ZONES: List<Zone>`（`:76-77`）。四个入口：`AttackEntityEvent`（`:459-482`，**取消事件即取消原版攻击**）、`LivingIncomingDamageEvent`（`:489-505`，非玩家攻击者）、`KunJinKaoSwordItem.hurtEnemy`（`:350`）、`EntityTickEvent`（`:507-519`）。
流程：`startOverwrite`（`:135-177`，建 BossBar、备份目标主手、记录护甲/抢夺/主题）→ `applyDebuffs`（`:179-217`：速度 -0.6、跳跃 -0.8、护甲 -N、清空**全部**正面效果、主手换成泥土）→ 40 tick 倒计时（每 2 tick 粒子、每 4 tick 进度包）→ `finishOverwrite`（`:276-304`）→ 写击杀标记 → `target.kill()` → 死亡点生成 3×3×2 **屏障区块**（`:306-327`），600 tick 后还原，区内玩家缓慢 II + 禁止破坏方块（`:550-568`）。

**问题（按严重度）**：

| 级别 | 问题 | 证据 |
|---|---|---|
| 高 | **"单次伤害上限"对生物目标完全失效**：`handleSwordAttack` 不检查 `getAttackDamageLimit`，而 `AttackEntityEvent` 在伤害结算前就取消了攻击 → `KunJinKaoSwordItem.java:334,344` 与 `KunJinKaoProtectionHandler.java:163` 的限伤判断**永远走不到**。把上限调成 20，近战砍生物仍是一击必杀（对玩家目标是生效的，行为不一致） | `KunJinKaoOverwriteHandler.java:399-419,479-481` |
| 高 | **屏障可能永久残留**：`setBlock(...,3)` 写下的屏障会随区块存盘，而"原方块表"只存在内存静态 `ZONES` 里；服务器在 600 tick 内关闭/崩溃或单机退档重进 → 还原逻辑永不执行，存档里留下 3×3×2 不可破坏屏障（普通玩家清不掉）。另外还原时会把玩家 30 秒内放进区内的方块一并删除 | `:313-315,320,542-544` |
| 高 | **覆写中途区块卸载会永久吃掉目标主手物品**：主手原件只备份在内存 `backupMainHand`（`:169`），而"损坏的泥土"已写进 `HandItems` 并存盘（`:214`）；区块卸载后 tick 不再触发 → 状态与备份一起泄漏，生物永久拿着泥土 | `:169,214,242-245,507-519` |
| 中 | **重复攻击 = 无限控制**：已在覆写中的目标只把 `ticksLeft` 重置为 40，无冷却、无次数上限 → 可被永久剥夺护甲/速度/武器/增益 | `:141-150` |
| 中 | BossBar 悬挂：`ServerBossEvent` 仅在 `finishOverwrite`/死亡路径移除，目标不再 tick 或攻击者掉线时永久停留（引用泄漏） | `:290-292,530-533` |
| 中 | **`sendToAttacker` 的 BlockPos 重载丢弃 phase 参数** → `:532` 传的 `PHASE_CANCEL` 永远被发成 `PHASE_END`，客户端的取消分支等于没接线 | `:439-443` vs `:532` |
| 低 | `KunJinKaoBrokenDirt` 标记写到 `copyTag()` 的**副本**上后立即丢弃（死代码 + API 误用，应 `CustomData.update` 或写回） | `:211-213` |
| 低 | 玩法与美术耦合：`freezeAi = theme == 1 \|\| theme == 4` 把两个纯美术主题硬编码成 AI 冻结，`KunJinKaoTheme` 里无任何说明 | `:105` |
| 低 | 护甲削弱取施法瞬间快照（`ADD_VALUE -armorValue`），窗口内目标换装备即失真 | `:171,196-199` |
| 低 | 每 tick 无条件清空目标**全部**正面效果（会连其他玩家/插件刚给的增益一起清掉） | `:200-208` |

### 8.2 世界门（`world/MainWorldGate.java`，144 行）

维度是**纯数据包**实现（`data/kunjinkao/dimension[_type]/main_world.json`，字段集与 1.21.1 规范一致，`MAIN_WORLD` 键与 JSON `type` 对得上 ✅），`server.getLevel()` 为 null 时会提示而非静默失败 ✅（`:68-81`）。

- ⚠️ **返回坐标跨维度错配（可稳定复现）**：`rememberReturnPosition` **不记录来源维度**（`:102-109`），返回时却恒定传送到 `server.overworld()`（`:84,93`）→ 从下界/末地进门、再在自建主世界点门，会被送到主世界的"下界坐标"（8 倍比例错位），可能卡进山体或悬空
- ⚠️ **返回点是单槽且被覆盖**：每次进入都重写同一组键、无栈；`enter` 对任何非目标维度都生效（`:61-65`），在下界/末地/自建世界之间来回走会互相覆盖返回点
- ⚠️ **落点平台推平建筑**：对固定 (0,0) 的 5×5×3 区域无条件清除非空气方块并强制 `getChunk`（主线程同步生成 → 首访卡顿）；"幂等"只在没人动过那块地时成立（`:123-142`）
- ⚠️ 返回坐标无任何安全校验（与进入时铺平台的谨慎相反），且记录在传送前就被 `remove` 消费，传送失败即丢失（`:86-93`）

### 8.3 钻石投掷物（`entity/DiamondProjectile.java`，133 行）——整套死代码

全项目 `new DiamondProjectile` **出现 0 次**，但实体类型与渲染器都已注册（`SwordRegistry.java:47-52`、`client/ClientModEvents.java:49`）→ 落雷、点火、秒杀+掉落标记全部不可达。⚠️ 一旦启用：`DiamondProjectile.java:116-132` 放的是**真实的 `Blocks.FIRE`**（纵火）。另外它未覆写 `addAdditionalSaveData/readAdditionalSaveData`，`lootingMode`/`ownerId`（`:29-30`）区块存读后会丢失；`:84` 写的 `KILLER_UUID_KEY` 全项目无人读取。

### 8.4 注册表与配方

- ⚠️ **注册风格不统一**：`AcceleratorRegistry.java:20-21` 与 `WorldGateRegistry.java:19-20` 各建一个**同 registry 同命名空间**的 `DeferredRegister<Block>`，而两个方块的**物品**却都塞进 `SwordRegistry.ITEMS`（`AcceleratorRegistry.java:28-30`、`WorldGateRegistry.java:25-27`）。`SwordRegistry` 同时承担物品 / 创造栏 / 实体类型 / 配方序列化器（`:23-26`），是明显的杂物袋。功能可行，但每加一个方块就要新建 DeferredRegister 的写法容易出错
- ⚠️ **8 个 compile 物品是空注册**：`SwordRegistry.java:33-42` 的 `KUN_JIN_KAO_COMPILE_STAGES` 全项目零引用，语言文件也没有对应键 → 8 个只能 `/give` 出来、无翻译、无用途的物品（客户端用的是同名**模型** `ClientModEvents.java:34-36,55-57`）
- ⚠️ `AdminSwordRecipe.java:48-55` 未覆写 `getResultItem`（默认 `ItemStack.EMPTY`）→ 配方书/配方提示的显示行为**待确认**
- ⚠️ `AcceleratorBlockEntity.java:106-130` 的 setter 自身不 `setChanged()`/不发方块更新，靠调用方补（`AcceleratorConfigPayload.java:39-42`）→ 其他调用方容易漏

### 8.5 主题数据表（`KunJinKaoTheme.java`，183 行）

- ⚠️ 死代码：`particle(int)`（`:174-182`）、`color(int,int)`（`:136-143`）、`tint(int)`（`:145-147`）**均无调用者**（客户端直接读 `ThemeEntry.phase1/2/3Color()`），因此 `PHASE_ONE/TWO/THREE` 常量（`:16-18`）也只被这段死代码使用

### 8.6 状态持久化与进程级静态状态

| 项 | 结论 |
|---|---|
| 剑的 11 个状态键 | ✅ 存在物品自身 `CUSTOM_DATA`，跟随物品走，不会跨存档串味 |
| `PasswordAdminSavedData` / `UltimateDeathSavedData` | ✅ 挂在主世界 `DataStorage`、`setDirty()` 到位、`load` 丢弃损坏 UUID |
| ⚠️ `KunJinKaoOverwriteHandler.STATES/ZONES` | **进程级 static，从不随服务器停止清空**：单机"退回主菜单再进另一存档"时旧 `ZONES` 持有旧 `ServerLevel` 强引用（内存泄漏，且 `zone.level == level` 恒 false → 永不到期），旧 `STATES` 持有旧实体/`ServerPlayer` 强引用 |
| ⚠️ `UltimateDeathSavedData` | 无上限、无过期、无导出/审计接口，永久保存 UUID + 玩家名（PII） |
| ⚠️ `setDisguised(false)` | 写 `CustomModelData(0)` 而非 `stack.remove(...)`（`KunJinKaoSwordItem.java:84-86`）→ 组件残留会改变 `ItemStack` 的组件比较/堆叠（两把"相同"剑不再 `isSameComponentSameItem`），并使伪装状态机多出未文档化的"0 值"状态 |

### 8.7 同一规则四套实现（回归风险来源）

"持剑攻击 → 覆写或秒杀"分别写在：`KunJinKaoOverwriteHandler.java:399-419`、`:489-505`、`KunJinKaoSwordItem.java:344-357`、`KunJinKaoProtectionHandler.java:162-185` —— 四份判断条件互不相同，8.1 第一条 bug 正源于此。相关不一致：主手/副手解析不同（`KunJinKaoOverwriteHandler.java:384-394` 查双手，`KunJinKaoProtectionHandler.java:162,193,205-207` 只看主手）；主题循环有两套（`KunJinKaoSwordItem.java:121-123` 与 `client/KunJinKaoClientEvents.java:140-144`）。
`KunJinKaoSwordItem.hurtEnemy` 里针对生物的分支（`:344-357`）在玩家攻击者路径下**疑似不可达**（`AttackEntityEvent` 已取消整个攻击）——**待确认**是否有横扫等其他调用方。

### 8.8 结论

1. **权限只守住了"合成"，没守住"能力"**：一次猜中密码、一次 `/give`、一个改包客户端，就能拿到剑的全部致命能力（清场/秒杀/覆写）并远程把加速器推到 1024×/9³
2. **覆写流程的进程级静态状态与存档脱节**，会留下不可逆后果（屏障残留、主手物品永久丢失、BossBar 悬挂、跨存档引用泄漏）
3. **同一条"持剑攻击"规则四处重复实现且互相矛盾**，已经产生可见功能 bug（伤害上限对生物失效）

---

## 9. `event` 包专项（14 文件 / 1,560 行）

> 本节最重的结论是 §3.3 的 P0 提权链，其取证主要来自本组审计（已对照 NeoForge 21.1.118 源码核实）。

### 9.1 事件挂载表（全部挂在 `NeoForge.EVENT_BUS`）

| 事件 | 订阅者（文件:行） | priority |
|---|---|---|
| `PlayerTickEvent.Post` | `AdminAdventureSwordGrantHandler:22`、`KunJinKaoProtectionHandler:46`、`SwordDrawAnimationSyncHandler:22`、`TacticalHudMagnetHandler:36`、`TacticalHudTrueInvisibilityHandler:55` | 全部 NORMAL |
| `PlayerLoggedInEvent` | `AdminEyeSyncHandler:16`、`TacticalHudTrueInvisibilityHandler:101`、`UltimateDeathHandler:59` | NORMAL |
| `PlayerLoggedOutEvent` | `AdminAdventureSwordGrantHandler:35`、`SwordDrawAnimationSyncHandler:39`、`AdminEyeSyncHandler:29` | NORMAL |
| `PlayerEvent.StartTracking` | `TacticalHudTrueInvisibilityHandler:92` | NORMAL |
| `PlayerEvent.ItemCraftedEvent` | `AdminSwordCraftingHandler:18` | NORMAL（事件不可取消） |
| `PlayerInteractEvent.LeftClickBlock` | `KunJinKaoUnbreakableBlockHandler:26` | LOWEST |
| `BlockEvent.BreakEvent` | `KunJinKaoOreDropHandler:25` | LOWEST |
| `CommandEvent` / `ServerTickEvent.Post` | `CommandProtectedSwordHandler:30` / `:45` | NORMAL |
| `AttackEntityEvent` | `KunJinKaoColdDataEffectsHandler:53` | NORMAL |
| `LivingIncomingDamageEvent` | `KunJinKaoProtectionHandler:139`、`TacticalHudTrueInvisibilityHandler:78` | NORMAL |
| `LivingDamageEvent.Pre` | `KunJinKaoProtectionHandler:189` | NORMAL |
| `LivingDeathEvent` | `KunJinKaoProtectionHandler:225`(NORMAL)、`UltimateDeathHandler:36`(**LOWEST**) | ✅ 有意为之：低优先级者检查 `isCanceled()` |
| `LivingDropsEvent` | `KunJinKaoDeathEventHandler:39` | NORMAL |
| `ItemTooltipEvent` | `KunJinKaoTooltipHandler:24` | NORMAL |

### 9.2 冲突与耦合

1. **5 个 handler 共享 `PlayerTickEvent.Post` 且全部同级** → 实际顺序=注册顺序（`KunJinKaoEntry.java:44-50`）。可观测后果：`SwordDrawAnimationSyncHandler` 注册在 `AdminAdventureSwordGrantHandler` 之前，冒险模式补发的剑要**下一 tick** 才播拔剑动画。
2. **`LivingIncomingDamageEvent` 被两类逻辑同时订阅**：`KunJinKaoProtectionHandler:139`（取消伤害）与 `TacticalHudTrueInvisibilityHandler:78`（本意"伤害生效后清仇恨"，实际监听的是伤害**前**事件，方法名 `onLivingHurt` 具误导性）。
3. **`BlockEvent.BreakEvent` 出现嵌套触发**：`KunJinKaoUnbreakableBlockHandler:45` 在 `LeftClickBlock` 内部用 `CommonHooks.fireBlockBreak(...)` 主动 post 一次作"权限探针" → 其它插件的 BreakEvent 监听器会看到一次**最终并未发生**的破坏。
4. **标记清理与 tick 频率耦合**：`KunJinKaoProtectionHandler:80-87` 只在「玩家 tick 且 health>0」时清标记 —— 保护逻辑的正确性被绑在 tick 上（见 9.3）。

### 9.3 发现

| 级别 | 问题 | 证据 |
|---|---|---|
| **P0** | **Crafter 方块绕过合成授权 → 完整提权链**（见 §3.3 P0） | `AdminSwordCraftingHandler:18-40` + 21.1.118 的 `ItemCraftedEvent` 触发点 |
| 高 | **`CommandProtectedSwordHandler` 实际是物品复制器**：任何一条命令（玩家 `/msg`、命令方块、告示牌点击、RCON）都会对**全体在线玩家**快照背包/盔甲/副手，tick 末按数量差补发。只要某人在那一刻把剑放进箱子/合成格/光标/丢在地上（`countSwords:85-103` 只数 items/armor/offhand），就会被凭空补发一把；补发按列表索引对齐（`:75-83`），可能还回不同 NBT/伪装状态的剑。同时给每条命令增加 O(玩家数 × 42 格) 开销 | `CommandProtectedSwordHandler.java:30-45,59-103` |
| 高 | **Mob 标记泄漏 → 无关死亡也触发 25/50 倍掉落**：`applyKunJinKaoMark` 把标记写在受害实体上，但清理只覆盖 Player（`KunJinKaoProtectionHandler:80-87`）；伤害事件位于 `invulnerableTime` 冷却判定**之前**（已核对 `LivingEntity.hurt`）→ 同一目标 10 tick 内被二次命中时本次 `hurt()` 返回 false、`kill()` 不执行，**Mob 永久带着标记存活**，之后因任何原因死亡都会复制掉落 | `KunJinKaoSwordItem.java:411-418`、`KunJinKaoProtectionHandler.java:80-87,185`、`KunJinKaoDeathEventHandler.java:49-103` |
| 高 | `KILL_BY_OVERWRITE` 是**1 tick 通行证，会顺带关掉整段保护**：该标记为真即跳过持剑免疫（`:154-155`），而标记在 `:169/:185` 写入、下一 tick 才清（`:80-87`）→ 同一 tick 内该玩家对摔落/虚空/岩浆/箭矢全部失去免疫 | 同上 |
| 中 | 清仇恨用错事件：注释写"伤害生效后"，实际监听伤害前事件 → 本次攻击的仇恨在事件返回后被立刻写回，真正生效的是每 tick 的兜底（应改 `LivingDamageEvent.Post`） | `TacticalHudTrueInvisibilityHandler.java:77-89` vs `:73` |
| 中 | `KunJinKaoOreDropHandler` 用「方块状态是否仍为同种方块」判断"确实被破坏" → 同种方块被放回则漏发、被换成别的则越权补发；`baseDrops` 在事件时刻计算，与其它 mod 的掉落修改不一致 | `KunJinKaoOreDropHandler.java:55-62` |
| 中 | `MagmaCube instanceof Slime` 为真 → **岩浆怪被击杀时额外掉 25/50 个粘液球** | `KunJinKaoDeathEventHandler.java:95-99` |
| 中 | 掉落逻辑**不是"翻倍"**：每个已有掉落额外生成 25/50 个，再叠加 `withLuck(25/50)` 重抽同一张战利品表；文案/注释与实现不符，javadoc 仍在描述 1.20.1 的 `getDefaultLootTable()`（移植残留） | `KunJinKaoDeathEventHandler.java:62-78,80-92,106-111` |
| 中 | `KunJinKaoProtectionHandler:189-199` 用 `getMainHandItem()` 读伤害上限 → 副手持剑、主手拿别的物品时判定的是**错误的物品** | 同上 |
| 中 | 未授权/非创造玩家即使打开"破坏不可破坏方块"开关也**打不破**屏障/命令方块（探针被 pre-cancel），且无任何提示 | `KunJinKaoUnbreakableBlockHandler.java:45-50` |
| 低 | `KunJinKaoTooltipHandler.java:54-56` 的第二个 while 是**死代码**（进入条件与第一个 while 的退出条件互斥） | 同上 |
| 低 | `KunJinKaoCraftingHandler:35` 的 `setCount(0)` 在 shift 点击路径**实际无效**（`onQuickCraft` 传入的是副本），注释描述的机制与实现不符；`:23` 的 `instanceof CraftingContainer` 恒真 | `AdminSwordCraftingHandler.java:23,35,52-62` |
| 低 | `AdminEyeSyncHandler:20-25` 登录时先逐玩家发一遍再 `sendToAll` → 新玩家收到自己的状态两次（冗余包） | 同上 |

**性能（本组是全项目最重的）**：

- `TacticalHudTrueInvisibilityHandler.java:121-126`：每个开隐身的玩家**每 tick** 做一次 257×257×257 的 `getEntitiesOfClass(Mob.class, …inflate(128))` 扫描 + 新建 List，再对每个 Mob 做两次判定 → 随开启人数线性叠加
- `KunJinKaoProtectionHandler`：每 tick 每持剑玩家 `List.copyOf(getActiveEffects())`（`:92`）；`hasSwordInInventory` 每 tick 扫 37 格（`:55`），同一 tick 内 `:142` 与 `AdminAdventureSwordGrantHandler:41` 又各扫一次，无缓存
- `KunJinKaoProtectionHandler.java:156`：**每次被取消的伤害都写一行 `LOGGER.info`** → 持剑玩家站在火/岩浆/虚空里持续刷日志
- `TacticalHudMagnetHandler.java:46-54`：每 tick 两次 50³ 盒查询 + 两次 List 分配（且会隔墙吸取）
- `KunJinKaoOreDropHandler.java:65-73`：1000 倍上限时每堆叠至少一次 `Block.popResource`

**重复代码 / 死代码**：

- **6 处"是否持有真剑"实现且判定不一致**：`KunJinKaoProtectionHandler:244-256`（items+offhand，查 disguised，**不含 armor**）、`:205-208`（只查主手）、`SwordDrawAnimationSyncHandler:43-52`、`UltimateDeathHandler:98-107`、`CommandProtectedSwordHandler:59-103`（items+armor+offhand，**不查 disguised**）、`KunJinKaoOreDropHandler:32` 与 `KunJinKaoUnbreakableBlockHandler:33`（不查 disguised）。后果：`/clear` 保护对伪装剑生效，而持剑免疫/飞行/掉落增强对伪装剑不生效；被补发进 armor 槽的剑**不提供任何保护**
- `DiamondProjectile` 全项目无生成点，因此 `KunJinKaoProtectionHandler:147-149` 的 `instanceof DiamondProjectile` 是死分支；但实体类型已注册（`/summon` 可用），而它命中即 `kill()` + 写 `KILL_BY_OVERWRITE` → 有命令权限时等于"绕过持剑免疫的秒杀器"
- `AdminAdventureSwordGrantHandler:19` 与 `SwordDrawAnimationSyncHandler:19` 各维护一份 `HashMap<UUID,Boolean>`（每 tick autoboxing）

### 9.4 结论

1. 权限边界只剩"手上有没有剑"，而这个前提本身可被绕过（Crafter 合成 + `withSword` 无授权）→ §3.3 P0
2. `CommandProtectedSwordHandler` 是**物品复制器**而非精确保护：触发条件极低（任何一条命令 + 剑不在背包里）
3. 状态下沉到实体 NBT 但清理只覆盖 Player，导致标记泄漏、25/50 倍掉落、以及"1 tick 通行证"式保护空窗

---

## 10. `client` gui / hud / render 专项（17 文件 / 2,405 行）

### 10.1 结构

- **Screen**：全部走 `Minecraft.setScreen`（无 `MenuType`/`AbstractContainerScreen`）。`AcceleratorScreen` ← `ClientBlockScreens:16` ← `ClientHooks.openAcceleratorScreen` ← `AcceleratorBlock:37-41`（有 `level.isClientSide` 守卫）；`AdminPasswordScreen` ← `KunJinKaoClientEvents:174`（关闭依赖服务端回包）；`SwordOptionsScreen` ← `KunJinKaoClientEvents:190`；HUD 三屏 ← `TacticalHudClientPacketHandler:37/97/134`
- **Overlay**：`ClientModEvents:71-87` 用 `registerAboveAll` 注册两个命名层；战术 HUD 层在 `screen != null` 时提前 return（`HudOverlayRenderer:31`），因此与 Screen 版不重复绘制 ✅
- **RenderLayer**：`ClientModEvents:90-94` 在 `AddLayers` 里对 WIDE/SLIM 分别 `addLayer` 并 null 检查 ✅ 1.21.1 正确写法；BE 渲染器走 `RegisterRenderers`
- **HUD 编辑器交互模型**：`HudScreen` 不暂停、`render` 不调 `super.render`；功能栏**不是 widget**，靠几何命中测试（`HudScreen:35` → `HudOverlayRenderer.getFunctionAt:47-58`），命中框与视觉框同源（都由 `menuX(ease(animationProgress))` 推出）→ 滑入动画期间可点区域随动画移动
- **H 键在 `HudScreen.handleToggleKeyPressed:65-73` 里手写比对是必需的**：已核实 1.21.1 `KeyboardHandler` 在 `screen != null` 时不调用 `KeyMapping.click()` → **`KunJinKaoClientEvents:158-161` 对 HUD 屏的例外判断是不可达的死条件**（这条澄清了第 7 节的待确认项）
- **`clientbridge.ClientHooks` 方向正确**（只依赖 `AcceleratorBlockEntity`/`IPayloadContext`/`FMLEnvironment`，`volatile` 字段 + null 检查，`enqueueWork` 切回游戏线程）。瑕疵：注册依附 `ClientModEvents` 静态初始化，无"已注册"校验 → 一旦该类未加载，所有 S2C 包**静默 no-op**；自定义 `PayloadHandler` 与 NeoForge 自带接口重复

### 10.2 发现

| 级别 | 问题 | 证据 |
|---|---|---|
| **高（玩家可见）** | **`RenderType.lightning()` 被用来画 UI 面片**：`LIGHTNING` 是加法混合（SRC_ALPHA,ONE）且 `OutputState = WEATHER_TARGET` → alpha 不是"半透明"而是加色发光，极佳画质下还会写入 weather framebuffer。正确类型应为 `entityTranslucent(...)` 或 `debugQuads()` | `EyeHudLayer.java:92`（对照 `RenderType.java:526-538`、`RenderStateShard.java:41-49,321-329`） |
| **高（玩家可见）** | **把 `debugFilledBox()`（TRIANGLE_STRIP）当 QUADS 用**：往同一 strip 塞 6 个独立四边形共 24 顶点 → 跨面缝合出杂散三角形（立方体是凸的所以"看起来还能用"）。一行修复：换 `RenderType.debugQuads()` | `AcceleratorBlockEntityRenderer.java:37-38,46-57`（对照 `RenderType.java:643-668`） |
| **高（玩家可见）** | **未覆写 `getRenderBoundingBox()`**：范围框最大边长 9 格，默认包围盒只有 1×1×1 → **方块本体移出视锥时整个线框/填充连带消失**（转身背对即可复现），是最容易被当成渲染 bug 上报的一条 | `AcceleratorBlockEntityRenderer.java`（应 `new AABB(blockPos).inflate(radius+1)`） |
| 中 | **三个 Screen 每帧做两遍背景**：1.21.1 `Screen.render` 第一件事就是 `renderBackground`（`Screen.java:121-122`），而三处又手动调了一次；绘制顺序导致面板/标题先画、随后被第二次 `renderMenuBackground` 压暗 | `AcceleratorScreen.java:129,149`、`AdminPasswordScreen.java:39,49`、`SwordOptionsScreen.java:106,129` |
| 中 | **`HudScreen` 未覆写 `onClose()`** → ESC 关掉界面后 `hudEnabled` 仍为 true，功能栏继续非交互绘制，"可见不可点"，只能按两次切换键恢复 | `HudScreen.java`（对照 `HudEntityScreen:155`、`HudExclusionScreen:149` 都覆写了） |
| 中 | **滑条每格一个网络包**：拖动最坏发出约 1000 个 C2S 包，且每次写客户端 `ItemStack`，无节流、无"松手才发" | `SwordOptionsScreen.java:403-410,444-451,487-495` → `:265,280,346` |
| 中 | **7 个 HUD 功能里 2 个未实现**：`SCAN`、`TARGET_LOCK` 点击只改 `activeFunction`（按钮会高亮）但不发包、无任何效果，界面上看不出未接线 | `HudScreen.java:38-48`、`HudFunction.java:4-5`、`HudFunctionManager.java:5-7` 的遗留注释 |
| 中 | 列表界面生命周期不对称：`TacticalHudClientPacketHandler:97` 每次刷新都 `setScreen(new HudEntityScreen(...))`（不复用）→ 浏览中收到刷新则选中/搜索/滚动全丢；排除列表用 `replacePlayers` 整表替换；KILL 只本地删行、TELEPORT 却把界面关回功能栏 | 同上 |
| 中 | 两个 handler 都取消 `RenderHandEvent` 且互不知情 → 「真隐身 + 编译伸手」同时成立时本地玩家仍可能看到自己的手臂 | `TrueInvisibilityRenderHandler:29-35` vs `KunJinKaoIdleDataRefreshHandler:47-53` |
| 中 | `KunJinKaoThirdPersonGrabLayer` 依赖跨事件恢复共享模型状态（render 末尾把 arm/sleeve 置 `visible=false`，靠 `RenderPlayerEvent.Post:69-79` 恢复）→ render 抛异常或中途 return 会让手臂在后续帧持续隐藏；两处守卫不对称 | `:61-64,69-79,115-116` |
| 中 | `AdminPasswordScreen` 失败路径不自清：`submit():65-70` 只发包，不清空/不关窗/不禁用按钮 → 服务端某分支不回包时**明文密码留在屏幕上**且可反复提交 | `:65-70`、`ClientPayloadHandlers:72-74` |
| 低 | 文本越界：`HudEntityScreen:93` 是唯一不做宽度裁剪的列，坐标量级大（如 `30000000.0, …` ≈ 35 字符）会画出 440px 面板之外 | 同上 |
| 低 | 搜索体验：`filterEntities:228` / `filterPlayers:207` 每次过滤把 `scrollOffset` 归零（每敲一个字符跳回顶部）；`mouseScrolled` 在两个列表界面**无条件 return true**（忽略 deltaY/deltaX、也不能真滚动） | 同上 |
| 低 | 每帧分配：`HoneycombShieldLayer:112-120` 每个六边形 `new float[6]`×2 + 12 次 cos/sin（单玩家单帧最坏约 168 个数组 + 2000 次三角函数）；`HudEntityScreen:86-90` 每行每帧 2 次 `translatable().getString()` + `String.format`；多处每帧新建 `translatable`；`KunJinKaoThirdPersonGrabLayer:169-173` 远端玩家每帧 `sword.copy()` + `copyTag()` | 同上 |

**1.21.1 迁移遗留：这 17 个文件是干净的** ✅ —— 无 `net.minecraftforge.*` 引用、无 `DistExecutor`、无 `@OnlyIn`（全包 grep 只命中相邻的 `ClientBlockScreens:10`、`ClientPayloadHandlers:25`，属装饰性旧注解）。已逐条核对 `GuiGraphics` / `ModelPart.storePose` / `ItemRenderer.renderStatic` / `LevelRenderer.renderLineBox` / `EditBox` / `mouseScrolled` 等签名**均为 1.21.1 正确用法**。真正的问题只有上面第一组的三个 `RenderType`/包围盒误用。

**重复代码**：`HudEntityScreen` 与 `HudExclusionScreen` **约 50% 逐行重复**（panel 尺寸、`trim`/`drawBorder`/`drawActionButton`、行命中测试、`mouseScrolled`、`keyPressed`、`onClose`、`filter*` 全同构，差异只有"一行画什么/过滤什么/按钮发什么"，抽 `AbstractHudListScreen<T>` 可省约 120 行）；"填 4 条 1px 边框"有 **7 份**实现；`SwordOptionsScreen` 三个滑条是同一模板三份拷贝（可省约 90 行）；三个玩家模型层的 `addToPlayerRenderers` 逻辑重复；`HudOverlayRenderer` 的命中与绘制是两套同构实现（必须手工同步）。

**死代码**：`EyeHudLayer.brace():138-145` 与 `renderCurvedSupport():113-123` 从未被调用（注释说两根白支架，实际只有 `renderWhiteSupportBars:107-111` 画两根直杆）；`KunJinKaoIdleDataRefreshHandler` 的 `APPLY_POSE_ANIMATION=false` 使整段待机动画成为默认死代码（注释已承认）；`HudFunctionButton`（6 行 record）零行为零状态；多处 javadoc 与实现矛盾（如 `EyeHudLayer:23` 说"只渲染本地玩家因而无需同步"，而 `AdminEyeSyncHandler:22-31` 会把**其他玩家**的眼部状态广播出去，`:64` 也确实按 UUID 渲染他人）。

### 10.3 结论

1. **渲染类型与包围盒误用直接产出玩家可见的画面错误**：`EyeHudLayer:92` 的 `RenderType.lightning()`、`AcceleratorBlockEntityRenderer:37` 的 `debugFilledBox()` 当 QUADS、以及缺 `getRenderBoundingBox()` 导致范围框消失——三者都不报错、玩家一眼看得出不对，其中两处一行即可修
2. **三个 Screen 每帧两遍背景**（`renderBackground` 与 `super.render` 并用）→ 重复模糊 + 全屏 blit，且面板被二次压暗。低风险高收益的统一整改点
3. **HUD 编辑器与两个列表界面是维护风险最集中处**：一半逐行重复却生命周期不一致、ESC 语义缺失（可见不可点）、7 个功能里 2 个未实现却照常显示高亮、唯一的越界文本列也在列表界面里

---

## 11. 跨包共性问题

1. **权限校验的"位置"没有统一约定**：同一个模组里同时存在三种做法——HUD/排除名单在服务端 handler 入口校验（`KunJinKaoColdDataEffectsHandler:132-275`）、合成在 `ItemCraftedEvent` 里校验（`AdminSwordCraftingHandler`）、剑能力与加速方块**完全不校验**。这种不一致本身就是 P0 提权链的成因：只要有一个入口漏了，整套密码机制就被绕开。建议统一为"**每个 `playToServer` 的 handle 第一行就校验**"，效果层再校验一次作为纵深。
2. **缺少单一事实源（Same rule, many copies）**：`4 处`"持剑攻击→覆写/秒杀"（`KunJinKaoSwordItem:344-357`、`KunJinKaoProtectionHandler:162-185`、`KunJinKaoOverwriteHandler:399-419,489-505`）、`6 处`"是否持有真剑"（且伪装/armor 判定各不相同）、`2 处`动画常量（48/14）、`3 处`覆写时序常量（40/20）、`2 套`主题循环。已经产生真实 bug：伤害上限对生物失效、`/clear` 保护与持剑免疫对伪装剑判定相反、armor 槽里的剑不提供保护。
3. **静态状态缺乏生命周期**：服务端 `KunJinKaoOverwriteHandler.STATES/ZONES` 是进程级 static（跨存档泄漏 + 屏障可能永久落盘）；客户端 6 个 State 类只有 2 个有 `reset()`，且全项目没有任何 `LoggingOut`/level-unload 钩子（已有可见的跨存档残留）。
4. **渲染热路径**：`RenderType` 类型误用 2 处 + 缺 `getRenderBoundingBox()` 1 处；跨 client 三个包的每帧/每 tick 分配（蜂巢六边形、列表文本、远端玩家剑 copy、`getActiveEntityIds()`、HUD 命中与绘制两套同构实现）。
5. **硬编码与文档漂移**：`fov = 70.0`（`KunJinKaoOverwriteHudOverlay:314`）、`modid = "kunjinkao"` 字面量（`KunJinKaoTooltipColorHandler:22`）、30+ 行中文 lore（`KunJinKaoSwordItem:242-284`）、1.20.1 遗留 javadoc（`KunJinKaoDeathEventHandler:106-111`）、规格漂移（任务书要求 UUID 白名单、实现改成密码）。
6. **日志与异常处理**：热路径 `LOGGER.info`（剑 3 处 + 覆写 7 处 + `KunJinKaoProtectionHandler:156` 每次被取消伤害一条，火/岩浆/虚空下会洪泛）、3 处 `System.out.println`、6 处 `catch (Exception)` 吞异常；而 `AdminToolConfig` 这个安全关键类**一行日志都没有**（密码失败无审计）。
7. **命名与编码残留**：`COMPtLE`/`TtCKS`/`WHtTE`/`GUt`（`KunJinKaoClientSwordVisuals.java:38,39,43,201`，小写 `i` 被批量替换成 `t`）；同一份 `CUSTOM_DATA` 里混用命名空间键（`kunjinkao:pending_admin_sword_craft`）与裸 CamelCase 键（11 个）。

### 11.1 本次审计已澄清的存疑项 ✅

| 原存疑 | 结论 | 依据 |
|---|---|---|
| Screen 打开时 `KeyMapping.click()` 是否会被调用 | **不会** → `KunJinKaoClientEvents:158-161` 对 HUD 屏的例外判断是**死条件** | 核对 1.21.1 `KeyboardHandler` |
| 合成授权能否被绕过 | **能**：`ItemCraftedEvent` 只由 `ResultSlot` 触发，Crafter 方块走 `RecipeManager.getRecipeFor` 不受 `isSpecial()` 过滤 | 核对 21.1.118 源码 |
| Mob 标记泄漏是否真实 | **真实**：`LivingIncomingDamageEvent` 位于 `invulnerableTime` 冷却判定之前 | 核对 `LivingEntity.hurt` |
| gui/hud/render 是否有 1.20.1 签名遗留 | **无**，17 个文件逐条核对签名均为 1.21.1 正确用法（`@OnlyIn` 仅剩 2 处装饰性） | 对照本地 1.21.1 补丁源码 jar |
| jar 文件名与内部声明版本是否一致 | **一致**（0.1.9 / 0.1.9），此前怀疑的"版本错配"不成立 | 解包 `META-INF/neoforge.mods.toml` |

### 11.2 仍未证实（需要实机或运行时验证）❓

- `ItemRenderer.getModel` 的缓存键：`KunJinKaoClientSwordVisuals:167-171` 每帧造临时栈是否触发模型重烘焙
- `withLuck(25/50)` 对稀有度的实际提升（取决于战利品表用的是 luck 还是附魔等级公式）
- `KunJinKaoTooltipHandler` 依赖 `Component.empty().equals(...)` 与 `attribute.modifier.` 前缀删行，在 1.21.1 是否仍有效（需实机看 tooltip）
- `DiamondProjectile` 无 owner 时命中判定的伤害路径
- `AdminSwordRecipe` 未覆写 `getResultItem` 时配方书的显示
- `ClientHooks` 静态注册时机是否由 FML 扫描保证（否则 S2C 会静默失效）
- 覆写 HUD 在极佳画质下 `RenderType.lightning()` 的具体观感

---

## 12. 修复优先级建议

### P0 — 安全 / 不可逆破坏（建议立刻处理）

| # | 问题 | 位置 | 修法要点 |
|---|---|---|---|
| 1 | **提权链**：Crafter 绕过合成授权 + `withSword` 无授权 → 任意玩家可永久封禁他人 | `AdminSwordCraftingHandler:18-40`、`KunJinKaoColdDataEffectsHandler:287-291` | 两处一起改：合成校验覆盖 Crafter（或让配方参与 `isSpecial()`），并给 10 个剑配置包补 `isAuthorized` |
| 2 | **加速方块无授权 + 74.5 万次 tick/刻** | `AcceleratorConfigPayload:30-48`、`AcceleratorShowRangePayload:30-51`、`AcceleratorBlockEntity:54-85` | 加 `isAuthorized` + 交互距离校验；给区块加速加"每刻预算/上限"或改用 `randomTick` 概率折算 |
| 3 | **覆写的内存态与存档脱节**：屏障可能永久残留、主手物品永久丢失 | `KunJinKaoOverwriteHandler:76-77,169,214,313-320` | `ZONES` 落盘（SavedData）或改为不写真实方块；主手备份随实体持久化；退出/卸载时兜底还原 |
| 4 | **密码机制偏弱**：默认密码公开 + 无限尝试 + 无法撤销 | `AdminToolConfig:25,51-67`、`PasswordAdminSavedData:42-46` | 首次启动生成随机密码或启动时告警；加失败计数/冷却；补 `revoke` + 名单查看命令 |
| 5 | **`CommandProtectedSwordHandler` 实为物品复制器** | `CommandProtectedSwordHandler:30-45,59-103` | 改为只对"物品丢失事件"生效，或按实体物品栏（含容器）精确比对；至少修掉"剑不在背包即补发" |

### P1 — 正确性 / 玩家可见

6. "单次伤害上限"对生物目标失效（`KunJinKaoOverwriteHandler:399-419` 缺限伤判断）
7. Mob 标记泄漏 → 无关死亡触发 25/50 倍掉落（`KunJinKaoProtectionHandler:80-87` 只清 Player）
8. 覆写 HUD：FOV 硬编码 70、`partialTick` 未用不插值、缺 `screen != null` 守卫
9. 三个 Screen 双背景；`HudScreen` 缺 `onClose`；`SCAN`/`TARGET_LOCK` 两个未实现按钮
10. `RenderType` 误用 2 处 + 缺 `getRenderBoundingBox()`
11. `MainWorldGate` 跨维度返回坐标错配 + 落点平台推平建筑
12. 客户端 State 补统一清理入口（`LoggingOut`/level unload）
13. 夜视以 `persistentData` 为真值（效果到期后需按两次）；实体列表编解码上限不对称（>16384 实体时客户端抛异常）

### P2 — 工程与性能

14. **36 个 payload 收敛到约 12 类 + 改用 `StreamCodec.composite`**（同时消灭编解码上限不对称这类缺陷）
15. 热路径分配优化：蜂巢六边形提表、列表文本缓存、`getActiveEntityIds()` 复用、每帧 `translatable`/`itemStack.copy()`、`hasSwordInInventory` 缓存
16. 死代码清理：`DiamondProjectile` 整套玩法（或接线）、8 个空注册 compile 物品、`KunJinKaoTheme.particle/color/tint`、`OverwriteEffectPayload.terminalText/terminalLine`、`NetworkHandler.modId()`、`EyeHudLayer.brace`、`PHASE_DETAIL`
17. 本地化：剑的 30+ 行 lore 与掠夺模式提示改 `translatable`（en_us 目前会显示中文）
18. 版本自增不再污染工作区（改为 `-PautoBump` 才回写 `gradle.properties`）
19. 文档同步：HUD 任务书里的 UUID 白名单 vs 实现的密码机制；`KunJinKaoDeathEventHandler` 的 1.20.1 javadoc
20. 卫生：`COMPtLE` 系列误伤命名、`LOGGER` 声明位置、`System.out.println` 改 logger、密码失败增加审计日志

### 最小修复集（一周内可完成、能消掉绝大部分风险）

1. 修 **P0-1**（Crafter + `withSword` 授权）—— 唯一一条"普通玩家可造成不可逆破坏"的路径
2. 修 **P0-2**（加速方块授权 + 预算）—— 唯一的"卡服/DoS"通道
3. 修 **P0-3**（覆写内存态落盘/兜底）—— 唯一的"存档被写坏且不可逆"来源
4. 给 **P0-4** 加"启动时若仍是默认密码则告警 + 密码失败限流"

只做这 4 项，就把"普通玩家能把服务器搞崩或把别人永久封号"的可能性全部关掉了。

---

## 附：本报告的可信度与边界

- 全部结论均由 5 路并行 agent 逐文件通读得出，**每条都带 `文件:行`**；对 1.21.1 API 语义的判断（事件触发点、`LivingEntity.hurt` 顺序、`RenderType` 定义、`KeyboardHandler` 行为、`ItemCraftedEvent` 触发路径）**已对照本地 NeoForge 21.1.118 源码/补丁 jar 逐条核实**，不是凭记忆
- 未经实机验证的推断均标注「待确认」，汇总见 §11.2
- 审计为**只读**：5 路 agent 与本报告均未修改任何源码文件
- 少数"看代码像 bug 但取决于运行时"的项（如 `getModel` 缓存、`withLuck` 效果）建议实机复现后再动

---

## 附录 A：逐文件清单（102 个，含行数与一句话职责）

### 根包 `dev.modmind.kunjinkao`（6 文件 / 868 行）
| 文件 | 行 | 职责 |
|---|---|---|
| `KunJinKaoEntry.java` | 62 | 唯一入口：注册网络、3 个注册表、COMMON 配置、12 实例 + 3 类事件处理器 |
| `KunJinKaoSwordItem.java` | 479 | 剑本体：11 个 NBT 状态键、伪装、50 格范围清除、秒杀/覆写入口、击杀标记 |
| `KunJinKaoTheme.java` | 183 | 5 套异象主题纯数据表（文案/配色/阶段间隔/粒子） |
| `SwordRegistry.java` | 72 | 物品 + 创造栏 + 实体类型 + 配方序列化器注册（杂物袋） |
| `AcceleratorRegistry.java` | 40 | 加速方块 + 方块实体注册（物品借 `SwordRegistry.ITEMS`） |
| `WorldGateRegistry.java` | 32 | 主世界之门方块注册（物品同上） |

### `block` / `block/entity`（3 文件 / 260 行）
| 文件 | 行 | 职责 |
|---|---|---|
| `block/AcceleratorBlock.java` | 57 | 加速器方块：客户端开 GUI、注册服务端 ticker |
| `block/WorldGateBlock.java` | 38 | 门方块：服务端右键交给 `MainWorldGate` |
| `block/entity/AcceleratorBlockEntity.java` | 165 | 每 tick 对范围内方块额外调用 ticker/randomTick（倍率-1 次） |

### `config`（3 文件 / 220 行）
| 文件 | 行 | 职责 |
|---|---|---|
| `config/AdminToolConfig.java` | 78 | 明文密码配置 + 授权校验 + 写授权 UUID（无日志） |
| `config/PasswordAdminSavedData.java` | 57 | 存档内已授权 UUID 集合（只增不减，无 revoke） |
| `config/UltimateDeathSavedData.java` | 85 | 终极死亡排除名单 UUID→名字（有序、可解除、无上限） |

### `entity` / `recipe` / `world` / `overwrite` / `clientbridge`（5 文件 / 983 行）
| 文件 | 行 | 职责 |
|---|---|---|
| `entity/DiamondProjectile.java` | 133 | 钻石投掷物：落雷/点火/秒杀（**全项目无生成点**） |
| `recipe/AdminSwordRecipe.java` | 86 | 3×3 石头+木棍自定义配方，产出带 pending 标记的剑 |
| `world/MainWorldGate.java` | 144 | 门传送：进入自建维度 / 返回记忆坐标 + 5×5 落点平台 |
| `overwrite/KunJinKaoOverwriteHandler.java` | 569 | 覆写·断未 40 tick 服务端时序（最大单文件） |
| `clientbridge/ClientHooks.java` | 51 | 共通侧访问客户端逻辑的函数式间接层 |

### `event`（14 文件 / 1,560 行）
| 文件 | 行 | 职责 |
|---|---|---|
| `AdminAdventureSwordGrantHandler.java` | 51 | 已授权玩家进冒险模式时补发剑（维护 `UUID→wasInAdventure`） |
| `AdminEyeSyncHandler.java` | 34 | 登录/登出广播"谁在管理员白名单里"（纯显示） |
| `AdminSwordCraftingHandler.java` | 77 | 合成授权：未授权者清成品退材料（**Crafter 可绕过**） |
| `CommandProtectedSwordHandler.java` | 104 | 命令前后快照/补发剑对抗 `/clear`（实为物品复制器） |
| `KunJinKaoColdDataEffectsHandler.java` | 292 | 攻击粒子特效 **+ 全部 HUD/剑配置的服务端包入口（18 个静态方法）** |
| `KunJinKaoDeathEventHandler.java` | 131 | `LivingDropsEvent`：按 25/50 级"抢夺"复制掉落、重抽战利品表 |
| `KunJinKaoOreDropHandler.java` | 75 | 破坏方块后按倍数补发矿石掉落（延迟到同 tick 的 server task） |
| `KunJinKaoProtectionHandler.java` | 257 | 「背包有剑」判定中枢：创造飞行、饱和、冒险 mayBuild、免伤、取消死亡、虚空兜底、标记清理 |
| `KunJinKaoTooltipHandler.java` | 81 | 客户端 tooltip：删除原版"主手时"属性区块 |
| `KunJinKaoUnbreakableBlockHandler.java` | 74 | 左键负硬度方块时取消交互并手动破坏 + 掉落 |
| `SwordDrawAnimationSyncHandler.java` | 53 | 检测"非持剑→持剑"并向跟踪者广播拔剑动画 |
| `TacticalHudMagnetHandler.java` | 76 | 服务端磁铁：每 tick 吸 50³ 盒内掉落物/经验球 |
| `TacticalHudTrueInvisibilityHandler.java` | 147 | 真隐身：续期隐身、清 128 格仇恨、同步视觉 |
| `UltimateDeathHandler.java` | 108 | 两个处决开关：击杀后踢人 / 写入永久排除名单并在登录时拒绝 |

### `network`（40 文件 / 1,283 行）
| 文件 | 行 | 职责 |
|---|---|---|
| `NetworkHandler.java` | 76 | 唯一注册中心（36 个包）+ 4 个发送辅助 |
| `OverwriteEffectPayload.java` | 93 | S2C：覆写特效五阶段（唯一带静态工厂，含死字段） |
| `HudEntityListPayload.java` | 62 | S2C：全维度实体列表（编码无上限 / 解码 ≤16384） |
| `ExcludedPlayersPayload.java` | 56 | S2C：排除名单全量快照 + authorized |
| `AcceleratorShowRangePayload.java` | 52 | C2S：切换加速方块范围框显示（**无鉴权**） |
| `AcceleratorConfigPayload.java` | 49 | C2S：设置加速方块倍率与半径（**无鉴权**） |
| `ToggleThemePayload.java` | 41 | C2S：设置异象主题并回显提示 |
| `ToggleDisguisePayload.java` / `ToggleOverwritePayload.java` | 36 / 36 | C2S：切换伪装 / 切换覆写开关（同模板，无鉴权） |
| `SwordDrawAnimationPayload.java` | 33 | S2C：他人拔刀动画同步 |
| `SubmitAdminPasswordPayload.java` | 31 | C2S：提交管理员密码，服务端判定后回结果 |
| `ManageHudEntityPayload.java` | 31 | C2S：管理员对实体执行击杀/传送 |
| `ManageExcludedPlayerPayload.java` | 29 | C2S：管理员解除某玩家的排除 |
| `AdminEyeStatePayload.java` / `HudTrueInvisibilityVisualPayload.java` | 28 / 28 | S2C：管理员身份 / 真隐形视觉（同模板） |
| `HudShieldHitPayload.java` | 28 | S2C：盾牌受击视觉（UUID + 偏航角 + 命中高度） |
| `HudEntityActionResultPayload.java` | 28 | S2C：实体管理操作回执 |
| `AdminPasswordResultPayload.java` | 26 | S2C：密码验证结果 |
| `TacticalHudStatePayload.java` / `HudNightVisionStatePayload.java` / `HudTrueInvisibilityStatePayload.java` / `HudMagnetStatePayload.java` | 27/26/26/26 | S2C：四类 HUD 状态回执（同模板 `(enabled, authorized)`） |
| `SetAreaClearTargetModePayload.java` / `SetSwordAttackDamageLimitPayload.java` / `SetSwordLootingModePayload.java` / `SetSwordMiningSpeedPayload.java` / `SetSwordOreDropMultiplierPayload.java` | 27 ×5 | C2S：五个剑设置（同模板 `(hand, int)`，**无鉴权**） |
| `ToggleBlueScreenAttackPayload.java` / `ToggleQuitStrikePayload.java` / `ToggleUltimateDeathPayload.java` / `ToggleUnbreakableBlockBreakingPayload.java` | 27 ×4 | C2S：四个剑开关（同模板 `(hand, boolean)`，**无鉴权**） |
| `ToggleHudMagnetPayload.java` / `ToggleHudNightVisionPayload.java` / `ToggleHudTrueInvisibilityPayload.java` / `RequestExcludedPlayersPayload.java` / `RequestHudEntityListPayload.java` | 25 ×5 | C2S：空载荷动作（同模板；前三个有鉴权） |
| `ToggleTacticalHudPayload.java` | 26 | C2S：请求开关战术 HUD（带 requestedEnabled，有鉴权） |
| `HudEntityData.java` | 8 | 数据 record：实体摘要（UUID/entityId/维度/类型/名字/坐标） |
| `ExcludedPlayerData.java` | 7 | 数据 record：排除名单一行 |
| `HudEntityAction.java` | 6 | 枚举：KILL / TELEPORT |

### `client` 顶层（15 文件 / 1,936 行）
| 文件 | 行 | 职责 |
|---|---|---|
| `KunJinKaoClientSwordVisuals.java` | 407 | 最大客户端文件：8 个静态动画计时 + 粒子 + 3D 编译模型绘制 + 模型谓词 + 攻击 HUD 文字 |
| `KunJinKaoOverwriteHudOverlay.java` | 320 | 覆写 HUD：经验条变黑、四边裂纹、三阶段文字、残留 `?`、渐隐 + 自研 `projectToScreen` |
| `KunJinKaoClientOverwriteEffects.java` | 294 | 覆写客户端状态机与音效（6 个静态 Map） |
| `KunJinKaoClientEvents.java` | 212 | GAME 总线唯一每 tick 驱动点 + 本地输入视觉 + 6 个键位轮询 |
| `TacticalHudClientPacketHandler.java` | 145 | 战术 HUD payload 应用器（含无条件 `setScreen`） |
| `ClientModEvents.java` | 95 | MOD 总线一次性注册（模型谓词/渲染器/键位/两个 GUI 层/玩家层）+ 注入 `ClientHooks` |
| `ClientPayloadHandlers.java` | 81 | 13 种 S2C 的总派发器（整体 `enqueueWork`） |
| `KunJinKaoTooltipColorHandler.java` | 74 | LOWEST 优先级 tooltip：把 `∞` 按 wall-clock 色相染彩虹 |
| `RemoteSwordDrawVisualState.java` | 72 | 远端玩家第三人称取剑动画计时（`getGameTime()`） |
| `ShieldHitVisualState.java` | 65 | 蜂巢盾受击短闪（跨存档残留风险） |
| `ClientHudState.java` | 52 | 战术 HUD 开关 + 1/8 步进淡入淡出（唯一有 `reset()`） |
| `KunJinKaoKeyBindings.java` | 50 | 6 个 KeyMapping（K/I/P/H/]/ .） |
| `TacticalHudInvisibilityVisualState.java` | 26 | 真隐形玩家 UUID 集合（渲染层据此隐藏装备） |
| `AdminEyeVisualState.java` | 25 | 管理员"眼睛"客户端副本（唯一用并发集合） |
| `ClientBlockScreens.java` | 18 | 唯一入口：`openAcceleratorScreen` → `Minecraft.setScreen` |

### `client/function` `client/gui` `client/hud` `client/render`（16 文件 / 2,354 行）
| 文件 | 行 | 职责 |
|---|---|---|
| `client/gui/SwordOptionsScreen.java` | 509 | 剑设置窗：9 个 SettingOption 轮播（同坐标叠 4 开关 + 3 滑条 + 2 循环按钮） |
| `client/gui/AcceleratorScreen.java` | 287 | 加速方块 GUI：两个自绘滑轮 + 显示范围开关 |
| `client/gui/AdminPasswordScreen.java` | 76 | 管理员密码输入窗（仅提交，校验全在服务端） |
| `client/hud/HudEntityScreen.java` | 251 | 管理员实体列表（本地搜索、选中、击杀/传送） |
| `client/hud/HudExclusionScreen.java` | 230 | 终极死亡排除名单（与上者约 50% 逐行重复） |
| `client/hud/HudOverlayRenderer.java` | 106 | 功能栏绘制 + 几何命中测试（无 widget） |
| `client/hud/HudScreen.java` | 79 | 不暂停的透明交互层，接管点击与切换键（缺 `onClose`） |
| `client/hud/HudFunctionButton.java` | 6 | HudFunction 的单字段 record 包装（零行为） |
| `client/function/HudFunctionManager.java` | 55 | 当前功能 + 夜视/隐身/磁铁三个镜像布尔 |
| `client/function/HudFunction.java` | 27 | 7 项战术功能枚举（其中 SCAN/TARGET_LOCK 未实现） |
| `client/render/KunJinKaoThirdPersonGrabLayer.java` | 187 | 第三人称编译后半段取剑层（含远端玩家剑临时模型） |
| `client/render/EyeHudLayer.java` | 162 | 挂在 `PlayerModel.head` 的第三人称眼部终端 |
| `client/render/HoneycombShieldLayer.java` | 139 | 受击蜂巢线框 + ACCESS DENIED 文字 |
| `client/render/KunJinKaoIdleDataRefreshHandler.java` | 134 | 第一人称待机动画 + 编译阶段伸手抓剑手臂（默认死代码） |
| `client/render/AcceleratorBlockEntityRenderer.java` | 70 | 加速方块范围立方体（填充 + 线框，RenderType/包围盒有问题） |
| `client/render/TrueInvisibilityRenderHandler.java` | 36 | 真隐身时取消玩家/第一人称手渲染 |

**合计：102 文件 / 9,464 行**（附录合计与 §2.1 的分包统计一致）
