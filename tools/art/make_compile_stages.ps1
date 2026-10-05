<#
  从【主模型】切出八个编译分级模型。

  为什么要有这个脚本：分级模型以前是 export_model.ps1 按它自己那份 $raw 几何生成的。
  一旦主模型改用 Blockbench 编辑，两边就对不上了 —— 手里那八级拼起来的形状
  和最终成品不是同一把剑。

  现在改成从 kun_jin_kao_3d.json 直接取元素前缀：
  主模型是唯一真源，分级只是"少放几个部件"的它。以后在 Blockbench 里改完主模型，
  重跑这个脚本即可，不用回头同步任何几何。

  前提：Blockbench 导出时保留了元素顺序（它会丢掉 name，但顺序不变）。
  下面的 $stageCounts 就是"第 N 级用到前几个元素"，与主模型的元素顺序一一对应：
      0 pommel | 1 grip | 2 guardBody | 3-4 fins | 5-8 screens+prompts
      9 collar | 10-12 blade1..3 | 13 cursorTip | 14-16 dashes
  在主模型里增删部件时，这里要跟着改。
#>
param(
  [string]$Model = "$PSScriptRoot\..\..\src\main\resources\assets\kunjinkao\models\item\kun_jin_kao_3d.json",
  [string]$OutDir = "$PSScriptRoot\..\..\src\main\resources\assets\kunjinkao\models\item"
)

$utf8NoBom = [Text.UTF8Encoding]::new($false)

# 每一级用到的元素个数（前缀长度）。
$stageCounts = @(1, 2, 3, 9, 11, 12, 13, 17)

if (-not (Test-Path $Model)) { throw "主模型不存在：$Model" }
$main = Get-Content $Model -Raw -Encoding UTF8 | ConvertFrom-Json
$total = $main.elements.Count

if ($stageCounts[-1] -ne $total) {
  throw ("主模型有 $total 个元素，而分级表最后一级要 $($stageCounts[-1]) 个 —— " +
         "你在主模型里增删了部件，请同步更新本脚本的 stageCounts。")
}

for ($s = 0; $s -lt $stageCounts.Count; $s++) {
  $n = $stageCounts[$s]
  # 只换 elements；parent / display / textures 全部照抄主模型，
  # 这样分级和成品的手势、贴图不会各走各的。
  $stage = [ordered]@{
    parent           = $main.parent
    ambientocclusion = $main.ambientocclusion
    display          = $main.display
    textures         = $main.textures
    elements         = @($main.elements[0..($n - 1)])
  }
  $path = Join-Path $OutDir "kun_jin_kao_compile_$s.json"
  [IO.File]::WriteAllText($path, ($stage | ConvertTo-Json -Depth 12), $utf8NoBom)
  "  compile_$s -> 前 $n 个元素"
}

"从 $([IO.Path]::GetFileName($Model))（$total 个元素）切出 8 级"