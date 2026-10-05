# tools/art —— 剑的美术生成器

这把剑的模型和贴图**不是手绘的，是代码生成的**。想调颜色、调比例、调造型，
改脚本重跑即可，不用重新画。

## 四个脚本

| 脚本 | 作用 | 产出 |
|---|---|---|
| `build_atlas.ps1` | 画 64x64 贴图图集（8 个 16x16 区块） | `textures/item/kun_jin_kao_atlas.png` + `docs/art/atlas_x8.png` |
| `export_model.ps1` | 从几何参数导出主模型（**会覆盖 Blockbench 的改动**） | `models/item/kun_jin_kao_3d.json` |
| `make_compile_stages.ps1` | 从主模型切出八级编译模型 | `models/item/kun_jin_kao_compile_0..7.json` |
| `render_preview.ps1` | 自己写的小渲染器：把模型按等轴测/正交投影出来，带贴图，输出透明背景图标 | `docs/art/icon_*.png`、`docs/art/view_*.png` |

四个都是纯 PowerShell + System.Drawing，不依赖任何第三方库。

## 主模型谁说了算：Blockbench

`kun_jin_kao_3d.json` **以 Blockbench 导出的那份为准**。`export_model.ps1` 是当初
"模型还没进 Blockbench"时留下的脚手架，跑它会覆盖前者。

平时改造型走 Blockbench；改完重跑 `make_compile_stages.ps1` 把八级对齐即可 ——
它从主模型取元素前缀（1 / 2 / 3 / 9 / 11 / 12 / 13 / 17 个），
所以分级与成品永远是同一把剑。Blockbench 会丢掉元素的 name，但**保留顺序**，
这个脚本正是靠顺序工作的；若你在主模型里增删了部件，要同步改它顶部的 `$stageCounts`
（个数对不上它会直接报错，不会默默切错）。

只有想从 `$raw` 重造主模型时才跑 `export_model.ps1`，跑完也要接着跑
`make_compile_stages.ps1`。

## 常用调整

- **整体大小**：`export_model.ps1` 里的 `$K`（当前 0.92）。它是以 (8,0,8) 为中心等比缩放，
  所以改它不会让剑在手里跑偏。注意这只对"从 $raw 重造主模型"这条路有效 ——
  现在主模型以 Blockbench 为准，在那边缩放更直接。
- **某个部件的形状**：改 `$raw` 里那一条的 `from`/`to`。坐标约定是刀身朝 +Y、Z 居中 8。
- **配色**：改 `build_atlas.ps1` 里对应区块的颜色。主色是 `#57CFFF`（刀脊），
  和 `.` 菜单的描边同源。
- **图集区块的分工**：见 `build_atlas.ps1` 顶部注释，两个脚本里的区块序号必须一致。

## 为什么要有 render_preview.ps1

因为模型是代码生成的，改了参数得能**看见**。等轴测下每个面正好是平行四边形，
用三点仿射就能把对应的图集区块精确贴上去 —— 所以那张预览图就是它带贴图之后真正的样子，
不是示意图。图标也是同一个渲染器出的。

## 注意

- `kun_jin_kao.json`（物品模型）的 `parent` 指向 `kun_jin_kao_3d`，
  它自己还带着 `draw_compile` 的 overrides，把编译分级模型挂上去 —— 改模型时别动那个文件。
- 屏幕中央那段编译动画用的还是**扁平的** `kun_jin_kao_stage_0..7.png`（32x32），
  和这里生成的 3D 分级模型是两套东西。手上切的是 3D 分级，中央放的是扁平揭示。