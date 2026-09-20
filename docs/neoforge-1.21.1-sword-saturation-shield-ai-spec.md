# NeoForge 1.21.1 管理员剑：持续饱和与命中蜂巢护盾 AI 编码任务书

> **历史文档提示**：本文是当时的实现任务书。文中提到的「管理员 UUID 白名单」授权方式**已被密码验证取代** ——
> 现在由 `config/kunjinkao-admin.toml` 中的密码授权，见 `docs/kunjinkao-admin.toml.example`。
> 另外文中的 `AdminSwordItem` 在最终实现里名为 `KunJinKaoSwordItem`。文中其余功能描述仍然有效。

## 目标与范围

为 Minecraft 1.21.1 / NeoForge 21.1.x / Java 21 模组实现两项管理员剑功能：

1. 玩家背包或副手中有管理员剑时，持续获得无粒子、无状态图标的一级饱和效果。
2. 持有管理员剑的玩家受到生物攻击时，在受击方向对应的玩家模型外侧显示淡蓝色透明蜂巢护盾；多次受击可同时存在多块，并在约 1 秒内渐隐。

本任务只实现上述功能及必要的网络同步、客户端渲染和测试。不实现剑风、投射物、额外伤害、配方、H 键界面或实体管理。

管理员 UUID 白名单、OP 权限、H 键战术 HUD 都不是这两项效果的前置条件：只要管理员剑在主背包或副手中，效果就应生效。

## 版本与技术边界

- 必须基于实际 NeoForge 1.21.1 MDK 编译，使用 Java 21。
- 禁止使用 Forge 1.20.1 的 net.minecraftforge 包、SimpleChannel、NetworkEvent.Context 及旧事件包名。
- 按 NeoForge 1.21.1 的 RegisterPayloadHandlersEvent、CustomPacketPayload、StreamCodec 注册网络包。官方网络文档以 RegisterPayloadHandlersEvent 作为 payload 注册入口。
- 游戏规则判断、饱和施加和受击触发都在逻辑服务端完成；客户端只保存视觉状态和渲染。公共代码不得导入 net.minecraft.client 包，确保专用服务器不加载客户端类。

## 建议结构

    common/SwordPresence.java                 管理员剑背包检测
    common/SwordSaturationHandler.java        服务端持续饱和
    common/ShieldHitHandler.java              服务端受击判定与同步
    network/payload/ShieldHitPayload.java     服务端到客户端护盾视觉数据
    client/ShieldHitVisualState.java          每个玩家的多块护盾生命周期
    client/render/HoneycombShieldLayer.java   PlayerRenderer 的 RenderLayer
    client/ClientRendererRegistration.java    仅物理客户端注册渲染层

现有管理员剑物品类应是唯一判定来源，例如 stack.getItem() instanceof AdminSwordItem；不能通过显示名、注册表字符串或 NBT 名称判断。

## 一、背包管理员剑的持续饱和

### 判定

实现公共只读方法，例如 hasAdminSwordInInventory(Player player)。

- 扫描 player.getInventory().items 的全部主背包和快捷栏。
- 同时扫描副手槽；剑只放在副手也必须有效。
- 跳过空物品栈。
- 不要求剑位于主手，也不要求玩家在管理员 UUID 白名单中。
- 若项目有“伪装管理员剑”机制，仅真实管理员剑可触发。

### 服务端刷新策略

在玩家 tick 的服务端 Post 阶段执行：

    如果背包或副手有管理员剑：
      如果没有饱和，或剩余时间不超过 20 tick：
        添加 MobEffects.SATURATION，持续 300 tick，等级 0，
        ambient=false，showParticles=false，showIcon=false。
    如果没有管理员剑：
      停止刷新该效果。

- 只在 !level.isClientSide 的逻辑服务端执行；客户端不得自行加效果。
- 不要每 tick 重新 addEffect，否则会频繁重置效果并产生多余同步。
- 移走管理员剑时，不得粗暴清除全部饱和效果。仅停止本模组刷新，让其自然结束，避免删除食物、药水或其他模组提供的饱和。
- 目标是持续饱和，不是把食物值一次性改成极高数值。
- 推荐跳过旁观者；创造模式是否施加须保持实现一致。

### 饱和验收

1. 剑在快捷栏、主背包或副手时，服务端玩家持续有一级饱和。
2. 效果没有药水粒子和状态图标。
3. 把剑移走后，效果不续期，并在剩余时间结束后消失。
4. 单人、局域网和专用服务器均以服务端为准。

## 二、生物攻击时的蜂巢护盾

### 服务端触发和命中位置

在实际 NeoForge 1.21.1 MDK 提供的服务端生命实体受击或伤害事件中处理。事件类、是否可取消及伤害阶段必须以当前 MDK 为准；禁止照搬 Forge 1.20.1 LivingAttackEvent 的包名或签名。

仅当全部条件成立时触发：

- 目标是 ServerPlayer；
- 玩家背包或副手有管理员剑；
- 伤害来源的攻击者是 Mob；
- 事件仍能取得正确攻击来源。

若管理员剑已有免受生物伤害逻辑，必须先计算并发送 ShieldHitPayload，再取消伤害、减伤或执行其他保护。这样即使命中最终取消，视觉护盾仍会出现。客户端或 C2S 数据包不得伪造触发。

受击方向必须由攻击来源决定，而不是固定在正面：

    impactSource = 有直接伤害实体（如投射物）时取其位置，
                   否则取攻击生物位置。
    direction = impactSource.position - player.position。
    水平方向接近零时，使用玩家当前朝向作为安全回退。

    impactYaw = atan2(direction.z, direction.x) 的角度。
    playerForwardYaw = 90 - player.getYRot()。
    relativeImpactYaw = wrapDegrees(impactYaw - playerForwardYaw)。
    impactHeight = clamp(
      attackSourceY + attackSourceBbHeight / 2 - playerY,
      0.20,
      1.35
    )。

relativeImpactYaw 与 impactHeight 是护盾视觉协议字段。不能用“固定正前方”标记，否则侧面和背面攻击会错误显示。

### 服务端到客户端 payload

定义仅 S2C 的 payload，例如含 playerUuid、relativeImpactYaw、impactHeight 三个字段的 ShieldHitPayload。

- TYPE 使用模组 ID 加 shield_hit 路径。
- StreamCodec 的字段顺序必须固定为 UUID、float、float。
- 在 RegisterPayloadHandlersEvent 注册客户端方向 payload；物理客户端 handler 调用 ShieldHitVisualState.add。
- 用实际 NeoForge 1.21.1 MDK 的追踪玩家分发 API，把包发给受击玩家及正在追踪该玩家的客户端；不得只让攻击者或本地客户端看见。
- 网络 handler 不做伤害、背包或权限判断；这三项都属于服务端受击事件。

### 客户端视觉状态

维护每个玩家的护盾列表，而不是单块状态：

    Map<UUID, List<ShieldHit>>
    ShieldHit = { relativeImpactYaw, impactHeight, createdClientTick }

- 每块持续 20 tick，alpha 从 1.0 线性降至 0.0。
- 收到新包先清除过期项，再新增一块。
- 每个玩家最多保留 12 块；达到上限时淘汰最旧项。
- 每帧跳过 alpha 小于等于 0 的项并清理过期项。
- 新命中不得覆盖旧命中；连续受击时应看到处在不同方向和高度的多块护盾。

## 三、蜂巢护盾渲染

### 注册和坐标

只在物理客户端，通过 EntityRenderersEvent.AddLayers（以实际 1.21.1 MDK 名称为准）向 default 和 slim 两种 PlayerRenderer 加入 HoneycombShieldLayer。

渲染每一块护盾时：

1. 以 PlayerModel.body 为局部原点，并应用身体部位变换。
2. 每块护盾独立 pushPose / popPose，避免变换串到下一块。
3. 以当前身体和视角的插值朝向，把 relativeImpactYaw 转换成模型空间角度。
4. 根据 impactHeight 移动到受击高度，再沿模型表面法线向外偏移。护盾必须在模型外，而非体内或固定在正面。

玩家模型局部方向与世界 yaw 的关系可能与直觉相反。以实际模型验证符号，推荐从下式开始进行四向实机校验：

    viewRelativeToBody = playerViewYaw - playerBodyYaw
    modelRotation = -relativeImpactYaw - viewRelativeToBody

玩家转向后，已有护盾应仍处在正确受击方位；不能漂到模型内部、镜像到另一侧或固定在世界坐标。

### 外观和顶点安全

- 渲染小型蜂巢补丁：中心六边形加六个环绕六边形，共 7 格；不要制作覆盖全屏或全身的大平面。
- 单元半径建议 0.10f 模型单位，整体明显小于玩家躯干宽度。
- 使用淡蓝色或青蓝色透明六边形轮廓，alpha 乘本块渐隐系数；可选低强度半透明填充。
- 首选与顶点属性完全匹配的 RenderType.lines。每个顶点必须写入该 RenderType 所需的位置、颜色、法线和其余属性。
- 禁止用只写 position/color 的顶点喂给要求更多属性的 RenderType，否则可能触发 Not filled all elements of the vertex 崩溃。
- 不得照搬 Forge 1.20.1 渲染调用签名；以 NeoForge 1.21.1 Mojang 映射和当前 MDK 的 VertexConsumer 方法为准。

建议初始常量：

    MAX_SHIELDS_PER_PLAYER = 12
    SHIELD_LIFETIME_TICKS = 20
    CELL_RADIUS = 0.10f
    MODEL_SURFACE_OFFSET = -0.62f

MODEL_SURFACE_OFFSET 只是起始调参值。最终必须在前、后、左、右受击时都使护盾位于模型外且玩家可见。

## 禁止事项

- 禁止用仅客户端 tick、客户端背包扫描或客户端配置决定饱和、伤害或护盾。
- 禁止每 tick 重加效果，或在移剑时删除其他来源提供的全部饱和。
- 禁止把护盾定义成 Map<UUID, ShieldHit> 并覆盖旧项。
- 禁止把护盾固定在玩家正前方、固定世界坐标或玩家模型内部。
- 禁止把 C2S 请求作为护盾真实触发来源。
- 禁止在公共或专用服务器可加载类中导入客户端渲染、Minecraft.getInstance 或 PlayerRenderer。
- 禁止引入 Forge 1.20.1 的网络、事件或渲染 API。

## 验收清单

1. 管理员剑在主背包、快捷栏或副手时，玩家持续有无粒子、无图标的一级饱和；移走后不再续期。
2. 僵尸等 Mob 攻击该玩家时，每一次受击新增一个护盾补丁；如伤害被管理员剑保护取消，护盾仍显示。
3. 从前、后、左、右及不同高度攻击，护盾均出现在玩家模型外侧的对应方位和高度。
4. 玩家转向后，护盾不进入模型、不镜像，也不固定在错误世界方向。
5. 快速连续命中至少五次时，多块护盾同时存在；每块约一秒缓慢淡出；第 13 块到来时淘汰最旧项。
6. 两名客户端均能看到被其追踪玩家的护盾；专用服务器不会因客户端类加载失败。
7. 不出现 Not filled all elements of the vertex 或类似顶点格式崩溃。
8. 使用项目 wrapper 执行 gradlew build 成功，运行环境为 Java 21 与 NeoForge 1.21.1。
