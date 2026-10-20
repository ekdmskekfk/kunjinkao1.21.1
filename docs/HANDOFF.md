# 交接文档 —— kunjinkao（锟斤拷烫烫烫）

> 给下一个会话看的。开场直接把这份贴进去，不用重新解释背景。

## 一句话

NeoForge 1.21.1 的管理员剑模组，主题是"编码损坏 / 数据乱码"。
带一套管理员工具（区域场、覆写、时间加速、建筑/破坏手杖）、五主题 HUD、
八级编译动画、3D 模型与动画贴图、自发光、粒子与音效。

## 当前状态（2026-10-10）

| 项 | 值 |
|---|---|
| 版本 | **0.10.0** —— 15 项功能开发计划的 M1/M2/M3 **全部落地** |
| 远端 | `github.com/ekdmskekfk/kunjinkao1.21.1` |
| 最新提交 | `9c055a0`（本地提交，**不 push**） |
| CI | 未跑（不 push 就不会触发） |
| 测试实例 | `F:\mcc\mc\.minecraft\versions\1.21.1mod测试` — 已装 0.10.0 |
| 用户主实例 | `F:\mcc\mc\.minecraft\versions\infinity3.3.3` — 停在 0.9.5 |

**已做的事**：`docs/kunjinkao_15项功能开发计划.md` 里的 15 项功能全部实现。
提交序列：`f22563a` `518c04e`（M1, 0.9.8）→ `92f0cce`（M2, 0.9.9）→ `9e3ce3f`（M3 部分, 0.9.10）→ `9c055a0`（M3 完成, 0.10.0）。

**最重要的一条**：**15 项功能一个都没在游戏里验收过。**
构建通过只代表编译与打包没问题，不代表功能真的能用。下一轮最该做的就是逐项实测。

工作区：`F:\mcmodli\tang\neoforge1.21.1`
分发包：`F:\mcmodli\tang\neoforge1.21.1mod`

## 构建与部署

```powershell
$env:JAVA_HOME = 'C:\Program Files\Java\jdk-21.0.11'
$gradle = 'C:\Users\www\.gradle\wrapper\dists\gradle-8.14.3-bin\cv11ve7ro1n3o1j4so8xd9n66\gradle-8.14.3\bin\gradle.bat'
cmd /c "`"$gradle`" -p F:\mcmodli\tang\neoforge1.21.1 build --console=plain > build\full.log 2>&1"
```

- `build` 通过 `finalizedBy deployToTestInstance` 自动部署到测试实例
- **游戏开着时部署必定失败**：任务在删不掉旧 jar 时会 `throw GradleException`，
  这是故意的 —— 否则 mods 里会同时存在两个同 ID 模组，下次启动直接崩
- 修改版本号：`gradle.properties` 的 `mod_version`，它决定 jar 名
- CI 会自动拉 `libs/` 里那两个 jar（见下）

## 代码结构与关键文件

```
src/main/java/dev/modmind/kunjinkao/
  KunJinKaoSwordItem.java          剑本体；tryPlaceAreaField 是区域场的唯一真源
  KunJinKaoPalette.java            语义色（青/琥珀/品红/红）+ 乱码符号 U+FFFD
  KunJinKaoTheme.java              覆写"五主题异象"数据表
  sound/KunJinKaoSounds.java       剑自己的 5 个音效
  mixin/LevelPauseMixin.java       暂停场停方块实体（用 @Inject，不是 @Redirect）
  mixin/ECOLargeIntegratedWorkingStationOutputRouterMixin.java   neoecoae 输出路由修复
  world/SwordAreaFields.java       暂停场 + 范围加速的门面
  world/SwordPlacementRegistry.java 放置记录 SavedData
  network/*Payload.java            全部网络包（SwordSettingPayload 的 id 表在文件里）
  client/KunJinKaoClientSwordVisuals.java   编译动画、中央展示、命中 HUD
  client/render/SwordAmbientParticles.java  待机粒子 + 待机嗡鸣
  client/render/SwordWorldEffects.java      区域场扫描环 + 掉地剑光柱
  client/render/SwordSwingTrail.java        挥砍拖尾带
  client/render/SwordHitFeedback.java       命中冲击波
  client/render/KunJinKaoGlowModels.java    自发光（多渲染通道）
tools/art/build_atlas.ps1          生成动画图集 + .mcmeta
tools/sound/                       音效合成（如已存在）
```

## 已知坑（**这部分最值钱，都是踩过的**）

### 工具链

1. **`write` / `edit` 工具会报 `EISDIR`** —— 工作区文件权限问题。
   绕道：用 pwsh 的 `[IO.File]::WriteAllText($p, $s, [Text.UTF8Encoding]::new($false))`
2. **`.ps1` 必须带 BOM**（中文会乱码），**`.java` 绝对不能带 BOM**（javac 报错）
3. **pwsh here-string 里出现 `'@` 会提前截断外层** ——
   写 Java 文件时若内含 C# heredoc，改用字符串数组拼接
4. **`-replace "a","b"` 直接当方法参数会被拆成多参数** —— 先赋给变量再传
5. **Gradle / javac 日志是 UTF-8，不是 GBK**（2026-10-10 修正本条：原文写的是 GBK）。
   按 `[Text.Encoding]::GetEncoding('GB18030')` 读会得到 `閿欒` 这种乱码。
   直接 `Get-Content -Encoding UTF8` 或 `[IO.File]::ReadAllLines($p, [Text.Encoding]::UTF8)` 就对。
6. **日志别放 `$env:TEMP`** —— DSH 的临时目录每轮会变，下一轮就找不到。放 `build/` 下
7. **本机访问不了 `cdn.modrinth.com`，但 GitHub Actions 可以** —— 所以下载依赖放 CI 做

### Minecraft / NeoForge

8. **MC 只认 Ogg Vorbis**（`JOrbisAudioStream`），全 jar 没有 WAV 支持。
   合成出来的 PCM 必须转 `.ogg`。本机 ffmpeg 在
   `F:\mcc\mc\.minecraft\ffmpeg\bin\ffmpeg.exe`（启动器自带，本机没装 ffmpeg）
9. **两个 `@Redirect` 打在同一个调用点会冲突并崩溃**（`observable` 就撞过）。
   能改 `@Inject` 就改
10. **`RegisterAdditional.register()` 只接受 `standalone` 变体**，
    传 `inventory` 会让整个资源重载失败
11. **`RenderLevelStageEvent` 没有 `getMultiBufferSource()`**（那是 `RenderHighlightEvent` 才有），
    要从 `renderBuffers().bufferSource()` 取，**画完必须 `endBatch()`**
12. **MC 1.21.1 顶点 API 是新版**：`addVertex` / `setColor`，
    而且**没有 `endVertex()`** —— 写旧版直接编译不过
13. **item model 的 `overrides` 必须升序**（MC 取"最后一个匹配的"）。
    写成降序会导致所有值都解析到第一条 —— 这个 bug 让八级编译动画长期失效
14. **动画贴图的 UV 只映射到第一帧** —— 所以图集改成帧条后，模型 UV 一个字节都不用动
15. **`dataTag(stack)` 返回的是副本**，每个 setter 都必须 `writeDataTag(stack, tag)`
16. **物品模型父链一旦碰到 `builtin/generated`，元素会被 ItemModelGenerator 丢光** ——
    要用 `minecraft:block/block`
17. **`GuiGraphics.renderItem` 内部会 `translate(x+8, y+8, 150)`**，
    而 pose 是先平移后缩放，所以那个 `+8` 会被 scale 放大 —— 传 `(-8,-8)` 抵消

### 构建

18. **`libs/` 被 `.gitignore` 排除**（AE2 与 neoecoae 的 jar，不再分发），
    但 mixin 需要它们在编译类路径上。
    **CI 已加下载步骤**（锁定 URL + sha1 校验），本地需自行放入。
    `build.gradle` 里已加缺 jar 的明确报错
19. **部署任务删不掉旧 jar 时会故意失败** —— 通常是游戏开着
20. **工作区策略是 `workspace-write` 时 Gradle 起不来**：它要写
    `C:\Users\www\.gradle\native` 才能加载原生库，报
    `Could not initialize native services` / `Failed to load native library 'native-platform.dll'`。
    这不是代码问题 —— 提权跑一次（`sandbox_permissions: danger-full-access`）即可。

## QoL 功能面板（15 项功能计划 M1 引入）

- **打开方式**：按键 **G**（`key.kunjinkao.open_qol_panel`），或剑设置界面标题栏左侧的入口按钮。
  计划里建议默认 `J`，但 `J` 已经是伪装，故改用 `G`。
- **`NetworkHandler.PROTOCOL_VERSION` 已从 `"22"` 提到 `"25"`**（M1 加两条包到 23；M3 的 127 加两条到 24；
  0.10.2 框选改木棍后删掉 `ZoneSelectPayload`，注册表又变了 → 25）。
  **以后只要动注册表就必须再 +1**，否则新旧端会握手成功然后解码错位。
- **新增网络 id 表**（只能往后追加，不能改已有取值）：
  - `QolSettingPayload`（C2S，`settingId` + `value`）的 `settingId` 就是 `qol/QolSetting` 枚举里的 `id()`：
    `NIGHT_VISION=0`、`FLY_MINING=1`、`WATER_BREATH=2`、`VILLAGE_HERO=3`、`REACH=4`
  - `QolStatePayload`（S2C）：`settingId, enabled, value, min, max, serverScope, authorized`，一行一条
  - `SimpleActionPayload` 新增 `REQUEST_QOL_STATE = 6`（打开面板时请求全量）
- **新增配置**：`AdminToolConfig` 的 `[qol]` 分节
  - `villageHeroMaxLevel`（默认 5，0~255）—— 万民敬仰个人等级的服务器上限
  - `reachMaxBlocks`（默认 8，0~64）—— 长臂善舞距离加成的服务器上限
- **个人级存档**：`config/QolStateSavedData`，挂 overworld 单例，`UUID -> int[QolSetting.COUNT]`，
  下标即 `id()`。**服务器级值不在这里**（那些属于 toml）。
- **面板界面状态**：`client/qol/QolPanelConfig`，存 `config/kunjinkao-qol-panel.json`
  （`selectedCategory` / `scrollY` / `guiScaleOffset` / `adaptive` / `toggleBinds`）。
  只存"看起来怎么样"，不存"功能开没开" —— 后者是服务端权威。
- **快捷键绑定**：只做**开关键**（开关类功能一行一个键，绑定存进上面的 `toggleBinds`）。
  **数值键与执行键刻意不做**：M1 的两个数值项不是高频操作，用面板滚轮就够；
  执行键要等 M3 的 127 选区挖掘才有意义。以后要补时按同一套边沿检测写。
- **M1 状态**：已提交（`f22563a`、`518c04e`，版本 0.9.8），已部署测试实例。
- **M2 状态**：已提交（`92f0cce`，版本 0.9.9），已部署测试实例。
- **M3 状态**：已完成，已提交（`9e3ce3f` 0.9.10 与 `9c055a0` 0.10.0）。87/96/119/127 全部落地。
- **15 项功能全部实现，但一项都没在游戏里验收。**

### 新增按键（截至 0.10.0）

| 键 | 作用 |
|---|---|
| `G` | 打开功能控制台面板 |
| `K` | 撤销（先试选区作业，没有再走放置撤销） |

选区作业（127）的框选与执行都**不在固定按键上**：
- 框选：手持**木棍右键**方块设角点（第一次 A、第二次 B、第三次重开）
- 执行：在 G 面板里给「选区作业·破坏」和「选区作业·放置」**两行各绑一个执行键**
  （面板行尾第二个格子）。按住 Shift 按执行键 = 取消。
- **破坏与放置是两个独立的键，没有"当前模式"这个状态。** 这一步不可逆，
  共用一个键时"到底是挖还是填"取决于上次滚轮停在哪，而滚轮很容易被误碰。

### 面板里有什么、配置里有什么

- **G 面板只放个人级项**（`QolSetting.panelVisible()` == `scope == PERSONAL`）。
- **服务器级项只活在 `config/kunjinkao-admin.toml` 的 `[qol]` 分节**，面板里一行都不画。
  理由：两种东西混在一张列表里看着像同一种 —— 面板上的行点一下就改，
  而这些行改了是写进服务器配置、影响所有人的。
- 枚举里仍然保留服务器级项：它们同时是「配置键的定义」，
  `AdminToolConfig` 的 `[qol]` 分节是照它们自动生成的（`configKey()`）。
- 服务端的 SERVER 作用域校验分支保留：那是防伪造包，不是面板入口。

### 127 选区挖掘的注意点

- **作业按 `zoneTickBudgetMs` 分批**，这是它不掉 TPS 的唯一保证。不要为了"挖快点"把预算调大。
- **选区刻意不持久化**（`ZoneSelection` 是内存里的）：存进存档会出现"框了一半下线、
  几天后上线一按执行就没了 64×64"。重启即清空是安全特性，不是漏做。
- **掉落骰子按 BlockState 缓存，但带方块实体的方块不走缓存** ——
  按状态缓存会把第一个箱子的内容复制给后面所有同状态箱子，那是刷物品。
- **撤销会把当时塞进背包的掉落物收回来**（`granted` 清单）。不收的话撤销就是刷物品。
  收不回来（已被用掉/丢掉）时只还原地形并如实提示。
- **黑名单是方块标签** `kunjinkao:zone_excavation_blacklist`（在 `data/kunjinkao/tags/block/`），
  服主可以往数据包同名标签里追加。

### 客户端新增处理器（M3）

| 类 | 作用 | 注意 |
|---|---|---|
| `client/QolFogHandler` | 87 水下/岩浆推开雾 | 走 `ViewportEvent.RenderFog` / `ComputeFogColor` 事件，**不写 mixin**；`RenderFog` 必须 `setCanceled(true)` 才生效；与光影大概率冲突 |
| `client/qol/QolTreasureScanner` | 96 扫描 + 缓存 | **不要按方块遍历**（129³ 会卡死），按区块只遍历方块实体，每次最多 24 个区块分帧摊开；三条件同时满足才扫（总闸 + 已授权 + 个人开关） |
| `client/render/QolTreasureRenderer` | 96 画穿透线框 | `AFTER_BLOCK_ENTITIES` + `renderBuffers().bufferSource()` + `disableDepthTest`，画完 `endBatch()` |

### mixin 清单（截至 M2）

| 类 | 目标 | 注入点 | 备注 |
|---|---|---|---|
| `LevelPauseMixin` | 原有 | — | 暂停场停方块实体 |
| `ECOLargeIntegratedWorkingStationOutputRouterMixin` | 原有 | — | neoecoae 输出路由修复 |
| `ItemStackDurabilityMixin` | `ItemStack#hurtAndBreak(int, ServerLevel, LivingEntity, Consumer)` | `@ModifyVariable` HEAD, argsOnly, index 1 | 功能 70；`require = 0` |
| `EntityFireImmuneMixin` | `Entity#fireImmune()` | `@Inject` HEAD, cancellable | 功能 84；`require = 0`；两侧都判 |

- 两条新 mixin 都登记在 `src/main/resources/kunjinkao.mixins.json`。
- **对 `ItemStack` / `Entity` 这类外部目标必须显式写 `require = 0`**：
  别的模组改过方法体就让注入失败，那应当退化成原版行为，而不是启动崩溃。
- `EntityFireImmuneMixin` 客户端分支走 `ClientHooks.isQolEnabledOnClient` ——
  **mixin 是 common 的，绝不能直接引用 `client` 包里的类**，否则专用服务器一加载就炸。
  要新增这类查询照 `ClientHooks.registerQolStateQuery` 的老办法加。
- 打包后 jar 里**没有 refmap 是正常的**：NeoForge 1.21.1 用官方映射，运行时不需要重映射。

### `[qol]` 配置项全表

| 键 | 默认 | 范围 | 说明 |
|---|---|---|---|
| `villageHeroMaxLevel` | 5 | 0~255 | 88 万民敬仰个人等级的服务器上限 |
| `reachMaxBlocks` | 8 | 0~64 | 89 长臂善舞距离加成的服务器上限 |
| `xpBoostMultiplier` | 1 | 1~101 | 72 经验飞涨倍率（服务器级，OP 可改） |
| `anvilUnbreakableEnabled` | 0 | 0~1 | 77 不朽铭文总开关（服务器级） |
| `anvilXpCost` | 10 | 1~100 | 77 的经验成本（服务器级） |
| `machineLootMultiplier` | 1 | 1~100 | 119 机器共鸣：假玩家击杀的掉落倍率（服务器级） |
| `treasureHunterEnabled` | 0 | 0~1 | 96 寻宝大师服务器总闸（关掉则个人开关也失效） |
| `treasureHunterRadius` | 64 | 8~64 | 96 的扫描半径（格） |
| `zoneExcavateEnabled` | 0 | 0~1 | 127 选区挖掘服务器总闸（默认关） |
| `zoneMaxSide` | 64 | 1~64 | 127 单边上限（格） |
| `zoneDropPolicy` | 0 | 0~2 | 127 掉落策略：0 进背包 / 1 生成掉落物 / 2 销毁 |
| `zoneTickBudgetMs` | 45 | 10~48 | 127 每 tick 时间预算（毫秒） |
| `zoneBatchSide` | 256 | 32~512 | 127 单批处理方块数 |
| `zoneUndoMaxBlocks` | 20000 | 0~100000 | 127 撤销快照上限；0 = 禁用撤销 |

服务器级项的读写统一走 `AdminToolConfig.qolServerValue` / `setQolServerValue`
（`EnumMap` + `SPEC.save()`），键名与范围都从 `QolSetting` 取，加一项不用再写字段。

## 待办

- [x] ~~把 0.9.5 装到 `infinity3.3.3`~~（0.9.5 已装；0.9.6 新功能还没装过去）
- [ ] 剑的新功能：**数据污染值 → 蓝屏**（用剑累积损坏度，HUD 逐渐乱码，满了触发蓝屏后清零）【别主动提】
- [ ] 把 **`�`（U+FFFD）做成粒子或刻到刀身**（现在只在 HUD 上）
- [ ] `build.gradle` 里缺 jar 检查那段**缩进是 0**，夹在 `dependencies {}` 里看着像顶层（纯外观）【别主动提】
- [ ] `kun_jin_kao.png` 这张 1254×1254 旧平面图（约 6 MB 显存）还在，只产生 mip 警告，未清理【别主动提】
- [ ] 探针粒子位置是近似的（用手的位置代替刀刃）

## 用户偏好（重要）

- **中文回复** —— 不只是最终答复，**工具调用之间的那句短说明也要中文**。
  之前踩过：正文是中文，但"Now the GUI:"、"Validation then build:" 这类过程说明写成了英文，
  用户 2026-10-10 专门提了一次。全程中文，别中英混着。
- **绝对不要用 ✓ ✗ 或 ✅** —— 原话"看的我心烦"
- **简洁，少写长报告**（吐槽过很多次）
- **行动优先于提问**
- **用户在游戏里自己测试**，不用我跑游戏（曾被理解反过：是"用户自己测"，不是"我自己测"）
- **要我诚实交代自己的错误**，不要替他找借口
- **每次构建完就 commit，但不要 push**（2026-10-07 原话定的规矩）
- **待办里标了【别主动提】的那几条，不要再拿去问他"要不要做"** —— 2026-10-10 原话："这三项以后不要提"。
  等他自己开口；他提了就正常做。

## 已知的未解决问题

**我的输出经常落进"思考"块而不是正式回答。** 表现为用户看到思考但看不到答复。
间歇性，与长短格式无关（试过短文本、纯文本，仍会复现）。
这一条不是内容问题、内容都在思考块里可读，但要从根上解决得在 DSH 层面查。