# AI Development Todo

> Updated: 2026/8/10 23:38:43

## Goal

Automatic build repair round 1/3. Fix every reported build error. Preserve the requested mod behavior and make the smallest complete source changes needed.

BUILD FAILURE
Error invoking remote method 'minecraft:buildProject': Error: Gradle 构建失败（退出代码 1）
F:\mcmodli\tang\neoforge_1.21.1\src\main\java\dev\modmind\kunjinkao\KunJinKaoSwordItem.java:125: 错误: 找不到符号
    public void appendHoverText(ItemStack stack, net.minecraft.world.item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
                                                                         ^
F:\mcmodli\tang\neoforge_1.21.1\src\main\java\dev\modmind\kunjinkao\client\KunJinKaoItemRenderer.java:30: 错误: 方法不会覆盖或实现超类型的方法
            @Override
            ^
F:\mcmodli\tang\neoforge_1.21.1\src\main\java\dev\modmind\kunjinkao\client\KunJinKaoEntityRenderer.java:22: 错误: 找不到符号
        this.minecraft = pContext.getMinecraft();
                                 ^
F:\mcmodli\tang\neoforge_1.21.1\src\main\java\dev\modmind\kunjinkao\event\KunJinKaoDeathEventHandler.java:66: 错误: 找不到符号
        LootTable lootTable = serverLevel.getServer().getLootData().getLootTable(lootTableKey);
                                                     ^
完整日志：F:\mcmodli\tang\neoforge_1.21.1\.modmind\builds\minecraft-test-build.log

## Plan Summary

对 1.21.1 官方映射：appendHoverText 的 TooltipContext 是 Item 嵌套类；BlockEntityWithoutLevelRenderer.renderByItem 需要 ItemDisplayContext 参数；EntityRendererProvider.Context 没有 getMinecraft()；战利品表 API 需按 1.21.1 校正。仅改动这四处，保留全部玩法逻辑。

## Tasks

- [ ] T1 修复 KunJinKaoSwordItem.appendHoverText 的 TooltipContext 类型 (in progress)
- [ ] T2 修复 KunJinKaoItemRenderer.renderByItem 覆盖签名 (in progress)
- [ ] T3 修复 KunJinKaoEntityRenderer 的 Minecraft 获取方式 (in progress)
- [ ] T4 修复 KunJinKaoDeathEventHandler 战利品表获取 API (in progress)
- [ ] T5 构建验证 (in progress)

## Acceptance Criteria

- [ ] Gradle 构建成功（退出码 0）
- [ ] 四处编译错误全部消除
- [ ] 修改保持最小且不改变已实现的覆写/断未玩法行为

## Risks And Constraints

- 战利品表 API 可能还有其它 1.21.1 差异（如 LootParams 构造），需在构建后确认无新错误
- EntityRenderer 渲染路径可能不再注册，但本轮只做编译修复，不扩展功能

## Verification

- 尚未完成独立验证。
