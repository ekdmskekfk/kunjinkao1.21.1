# 修复汇总（对应 `docs/structure-report.md` 的结论）

> 本文记录"按报告结论做的修复"与**仍未处理**的项，便于逐条核对。
> 每个提交都通过本地 Gradle 编译（`gradle build --offline`，Gradle 8.14.3 + JDK 21）；
> 提交为本地提交，**尚未推送**。

## 一、P0（安全 / 不可逆后果）

| 报告结论 | 状态 | 提交 | 要点 |
|---|---|---|---|
| 提权链：Crafter 绕过合成授权 + `withSword` 无授权 → 任意玩家可永久封禁他人 | ✅ 已修 | `a4939d8` `ee1d029` `2eb035b` | ① `withSword` 加 `isAuthorized` 闸门（覆盖经它分发的剑设置包）② 三个独立入口（ToggleOverwrite/ToggleTheme/ToggleDisguise）单独补闸门 ③ pending 标记升级为**能力闸门**：未验证成品在 `use/hurtEnemy/getDestroySpeed/isCorrectToolForDrops` 一律退化为普通剑，两个 `findSword` 也接入 |
| 加速方块：配置无授权、无归属、无距离校验，1024 倍 × 9³ ≈ 74.5 万次 tick/刻 | ✅ 已修 | `a4939d8` | `AcceleratorBlockEntity.mayEdit`（授权 + 8 格距离，两个包共用）+ 每刻 4096 次额外 tick 硬预算；去掉重复发包与全服跨维度广播 |
| 覆写内存态与存档脱节：屏障永久残留、主手物品永久丢失、BossBar 悬挂 | ✅ 已修 | `22e6862` `64aa4e5` | 新增 `UndefinedZoneSavedData` 落盘 + 启动兜底还原（只还原仍是自己放下的 BARRIER）；主手备份写进实体 persistent data + `EntityJoinLevelEvent` 兜底；BossBar/静态集合补掉线、停服清理；复核发现的"恢复时把主手清空"风险已修 |
| 密码机制：默认密码公开、无限尝试、授权不可撤销 | ✅ 已修 | `a4939d8` `8cc155d` | 限流（2 秒间隔 / 5 分钟窗口 5 次失败）+ 成功失败审计日志 + 默认密码告警；新增 `revoke/snapshot` 与 `/kunjinkao-admin list\|grant\|revoke`（OP 4 级），撤销时一并收回剑 |
| `CommandProtectedSwordHandler` 实为物品复制器 | ✅ 已修 | `8cc155d` `ee1d029` | 只对 `clear\|item\|data`（含 `namespace:` 写法）快照；结算把容器槽/光标/8 格内掉落物/潜影盒内部都算作"仍然拥有"；按物品身份抵消而非按下标补发 |

## 二、P1（正确性 / 玩家可见）

| 报告结论 | 状态 | 提交 |
|---|---|---|
| 伤害上限对生物目标失效 | ✅ | `f43c078`（配合 `2eb035b` 的能力闸门） |
| 击杀标记泄漏 → 无关死亡触发 25/50 倍掉落 | ✅ 标记加 40 tick 时效 | `f43c078` |
| 夜视开关真值源错误 | ✅ 改读实际效果 | `f43c078` |
| 实体列表编解码上限不对称 / `readEnum` 越界 | ✅ | `f43c078` |
| 覆写 HUD：FOV 写死 70、`partialTick` 未用、缺界面守卫 | ✅ | `c5d7cca` |
| 三个 Screen 每帧两遍背景 | ✅ | `cbdd7fc` |
| `RenderType.lightning()` / `debugFilledBox()` 误用、缺 `getRenderBoundingBox` | ✅ | `cbdd7fc` |
| 世界门返回维度错配、落点推平建筑 | ✅ | `0996093` |
| 客户端 State 无生命周期清理（跨存档串味） | ✅ 6 个 State 补 reset + `LoggingOut` 统一清理 | `bb3e065` `1e3eb11` |
| HUD 功能开关不在清理路径 | ✅ | `b8adea7` |
| 列表界面：坐标列越界、过滤归零滚动、滚轮吞事件、刷新重建界面 | ✅ | `bb3e065` |

## 三、P2（工程 / 性能 / 死代码 / 本地化）

| 报告结论 | 状态 | 提交 |
|---|---|---|
| payload 样板膨胀（36 个里 22 个是克隆） | 🟡 **只合并形状完全相同的三组（36 → 21）**：剑设置 9→1、HUD 空载荷 5→1、HUD 状态回执 4→1；其余形状各异的包收益小、协议风险相同，未动 | 见 Batch 6 |
| 热路径日志刷屏 / `System.out.println` | ✅ 命中与攻击路径降 debug，3 处 `System.out` 全部改 log4j | `d9f0bb4` `2eb035b` |
| 死代码：钻石投射物整套 | ✅ 删除（实体类 + 注册 + 渲染器 + 死分支） | `24cba65` |
| 死代码：8 个 compile 空物品、Theme 三个方法、`HudFunctionButton`、`EyeHudLayer.brace`、`NetworkHandler.modId()`、`OverwriteEffectPayload.terminalText/terminalLine` | ✅ | `350e8fa` `64aa4e5` |
| 本地化：剑的 30+ 行硬编码 lore、掠夺模式提示、区块破坏提示 | ✅ 改 `translatable`，zh/en 各 106 键完全对齐 | `350e8fa` `2ed4d27` `d02a914` |
| 版本自增污染工作区 | ✅ 改为 `-PautoBump` 才回写（实测普通 build 字节不变） | `2ed4d27` |
| 文档漂移（HUD 任务书写 UUID 白名单，实现是密码） | ✅ 加「实现现状」小节 | `2ed4d27` |

## 四、仍未处理（含理由）

1. **`/give`、创造模式物品栏、`/summon` 得到的剑不带 pending 标记**：这需要 OP/创造权限，属"权限持有者的正常能力"，不在"未授权玩家"的攻击面内。若要彻底堵住，需要把剑绑定授权者 UUID（改变"持剑即权限"的既定设计），**需要你决定**。
2. **数据包函数内的命令不触发 `CommandEvent`**：`#function` 里的 `/clear` 既不快照也不补发（旧实现的"每条命令"同样覆盖不到），属既有边界。
3. **限流与"密码错误"在客户端不可区分**：两者都回 `authorized=false`，管理员可能误判。改法要给 `AdminPasswordResultPayload` 加原因码（协议改动）。
4. **加速方块面板的服务端拒绝不会回滚客户端本地状态**（超距/未授权时客户端显示已改、服务端未改，重开 GUI 才对齐）。
5. **`ThemeEntry.tint` 访问器零调用**：删它会改变 record 形状，收益极低，保留。
6. **`AdminPasswordScreen.onResult()` 是死方法**：回包路径由 `ClientPayloadHandlers` 直接关窗；只有"服务端完全不回包"时按钮会一直禁用（可按 ESC 关窗）。

## 五、验证方式

- 每批改动均跑 `gradle build --offline`（含 `processResources`/`jar`），编译失败不提交。
- 两次**对抗性复核**（逐条对照 `neoforge-21.1.118-sources.jar` 与字节码）：覆写持久化、Batch 1 安全修复 —— 后者的三处证伪（三个剑开关包无闸门、命令保护正则漏命名空间、`revoke` 不收回剑）已在本汇总的提交里修掉。
- 发现并修正过一次"提交树编译不过"（并发代理的连带改动未跟上），此后以 HEAD 树为准复核。

## 六、建议实机验证的项（静态审计无法覆盖）

1. 客户端状态清理是否真的生效（`LoggingOut` 注册路径已改为显式注册，仍未实机验证）。
2. 覆写流程在"中途区块卸载 / 服务器崩溃后重进"下的屏障与主手恢复。
3. `debugQuads()` 替换后加速范围框在极端视角是否有 z-fighting。
4. 合并后的 payload 在真实客户端-服务端之间的收发（协议版本已 20 → 21）。
5. 限流参数（2 秒 / 5 次每 5 分钟）在多人服务器上的手感。