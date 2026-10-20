# 双仓库协同：1.21.1 NeoForge ↔ 1.20.1 Forge

> 更新：2026-10-19。移植**已完工**，本文档描述现在的维护方式。
>
> 历史内容（"本仓库装了两个 mod"、294 类范围、60–105 小时工作量估算、
> 抽 `common` source set 的方案）**全部作废**，原因见 §5。仅 §3 §4 的结论仍然有效。

## 1. 两个独立仓库

同一款 mod（锟斤拷烫烫烫 / `kunjinkao`），两条加载器线，**两个独立 git 仓库**，不共源码树。

| | 1.21.1 线 | 1.20.1 线 |
|---|---|---|
| 路径 | `F:\mcmodli\tang\neoforge1.21.1` | `F:\mcmodli\tang\forge1.20.1` |
| 远程 | `origin` → `ekdmskekfk/kunjinkao1.21.1` | `origin` → `ekdmskekfk/kunjinkao1.20.1` |
| 分支 | `main` | `main` |
| 加载器 | NeoForge 21.1.118 | Forge 47.4.0 |
| MC | 1.21.1（`pack_format 34`） | 1.20.1（`pack_format 15`） |
| 编译 JDK | 21 | **17** |
| 包根 | `dev.modmind.kunjinkao` | 同 |
| 规模 | 179 java | 177 java / 241 class / 64 资源 |

> 1.20.1 是本地 F: 盘路径，git 会报 `dubious ownership`。
> 命令前面加 `-c safe.directory=F:/mcmodli/tang/forge1.20.1` 即可，**不要**为此改全局配置。

## 2. 版本号策略：同功能同号

两个仓库**共用同一个 `mod_version`**：同一批功能在两个加载器上必须号相同，玩家一眼能对上。

- 改功能 → 两边同时改 `gradle.properties` 里的 `mod_version`，不允许只改一边。
- 加载器靠 `archives_base_name` 区分产物（`kunjinkao_neoforge_1.21.1` / `kunjinkao_forge_1.20.1`），
  **不靠版本号后缀**（历史上试过 `-star` 尾缀，已废弃）。
- 1.21.1 侧有 `-PautoBump`：只在 `build` 任务时把 `mod_version` +1 并写回，`compileJava` 不会触发。

## 3. 不移植清单（4 项，均有明确理由）

| 项 | 原因 |
|---|---|
| `cyou.rimuru.shootingstardemo.mc1211.*`（115 个类 + 约 298 个资源） | 用户决定**删除**，两个仓库都不再包含 |
| `compat/WrenchAbilities` | 依赖 NeoForge 独有的 `ItemAbility`，Forge 无对应机制 |
| `mixin/ECOLargeIntegratedWorkingStationOutputRouterMixin` | 目标代码在 1.20.1 的 neoecoae 20.4.2 里**根本不存在**（那套"大型集成工作站产物投递"是 1.21 线才有的功能），补丁无对象可补；20.4.2 已提供 `ECOExternalCpuOutputRoutes` 注册式 API，不需要织字节码 |
| `data/` 中依赖 1.21 独有格式的部分 | 已按 1.20.1 格式改造，`pack_format` 由 34 → 15 |

## 4. 日常纪律

1. **源头优先**：新功能/bug 先在 1.21.1 写，验证通过后立刻同步到 1.20.1；不要在 1.20.1 上单独发明功能。
2. **只编译验证**：两个工程都只跑 `compileJava`（各自 JDK 17/21），**不要跑 `build`**（会跑 `reobfJar` + 部署）。
3. **不允许 `neoforged` 残留**：1.20.1 工程里出现 `net.neoforged.*` 即为移植漏改，验收标准是残留数 0。
4. **资源各存一份**：`assets/` `data/` 两边各自持有（结构不同），改动时两边都要动。
5. **提交粒度**：一个功能一条提交，两边用**同样的提交信息**，便于对账。

## 5. 为什么旧的边界分析作废

旧版本文档做的是"抽 `common` 共享层"的可行性分析，前提是**两个版本共处一个仓库**。
实际执行时改成了**两个独立仓库、独立源码树、手工同步**的方案：

- 没有隐藏的耦合，跨版本差异直接体现在两份代码里，调试时不用在 source set 之间跳。
- 省掉了 Gradle 多子工程/source set 的复杂度（项目里已有 mixin、reobf 等构建细节）。

代价是同一处改动要敲两遍——由 `PORTING.md` 里的转换规律表 + 手工对账来消化。

## 6. 权威参考

- **1.20.1 移植手册**（转换规律、踩坑、API 差异）：
  `F:\mcmodli\tang\forge1.20.1\PORTING.md`（19 节，1184 行）
- **1.20.1 原生 API 查询**：直接 `javap` 反编译好的 forge jar，别猜签名：

  ```powershell
  $jar = "$env:USERPROFILE\.gradle\caches\forge_gradle\minecraft_user_repo\net\minecraftforge\forge\1.20.1-47.4.0_mapped_official_1.20.1\forge-1.20.1-47.4.0_mapped_official_1.20.1.jar"
  & "C:\Program Files\Microsoft\jdk-17.0.10.7-hotspot\bin\javap.exe" -classpath $jar 'net.minecraft.world.entity.EntityType$Builder'
  ```
