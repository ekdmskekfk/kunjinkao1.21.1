<#
  从几何参数导出模型 JSON：
    - models/item/kun_jin_kao_3d.json        主模型（17 个部件）
    - models/item/kun_jin_kao_compile_0..7.json  编译分级模型（按部件递进，手里会看着它自己装起来）

  几何坐标沿用原模型约定：刀身 +Y、Z 居中 8。
  坐标先按 $K 以 ($CX,$CY,$CZ) 为中心等比缩放，这样"整体多大"是一个数字的事。

  想调造型就改 $raw 里的 from/to；想调整体大小就改 $K。
#>
param(
  [string]$Res = "$PSScriptRoot\..\..\src\main\resources\assets\kunjinkao"
)

# ---- 整体缩放 ----
$K = 0.92
$CX = 8.0; $CY = 0.0; $CZ = 8.0
function S($v,$c){ [math]::Round($c + ($v-$c)*$K, 3) }

# ---- 几何：n=部件名（编译分级按它挑选），tex=用哪块图集 ----
$raw = @(
  @{ n='pommel';    f=@(6.9,-0.4,6.9);  t=@(9.1,0.8,9.1);   tex='trim'   },
  @{ n='grip';      f=@(7.1, 0.8,7.1);  t=@(8.9,4.6,8.9);   tex='shell'  },
  @{ n='guardBody'; f=@(4.6, 4.6,6.4);  t=@(11.4,6.8,9.6);  tex='panel'  },
  @{ n='finL';      f=@(3.6, 5.0,7.0);  t=@(4.6,6.4,9.0);   tex='trim'   },
  @{ n='finR';      f=@(11.4,5.0,7.0);  t=@(12.4,6.4,9.0);  tex='trim'   },
  @{ n='screenF';   f=@(5.4, 4.9,6.15); t=@(10.6,6.5,6.40); tex='dark'   },
  @{ n='promptF';   f=@(6.6, 5.3,5.95); t=@(9.4,5.95,6.15); tex='cursor' },
  @{ n='screenB';   f=@(5.4, 4.9,9.60); t=@(10.6,6.5,9.85); tex='dark'   },
  @{ n='promptB';   f=@(6.6, 5.3,9.85); t=@(9.4,5.95,10.05);tex='cursor' },
  @{ n='collar';    f=@(6.9, 6.8,6.9);  t=@(9.1,7.3,9.1);   tex='trim'   },
  @{ n='blade1';    f=@(7.2, 7.3,7.60); t=@(8.8,11.4,8.40); tex='spine'  },
  @{ n='blade2';    f=@(7.3,11.4,7.65); t=@(8.7,15.0,8.35); tex='spine'  },
  @{ n='blade3';    f=@(7.4,15.0,7.70); t=@(8.6,17.6,8.30); tex='spine'  },
  @{ n='cursorTip'; f=@(7.0,17.6,7.50); t=@(9.0,19.2,8.50); tex='cursor' },
  @{ n='dash1';     f=@(4.6, 9.2,7.7);  t=@(6.4,9.8,8.3);   tex='dash'   },
  @{ n='dash2';     f=@(4.2,12.2,7.7);  t=@(6.0,12.8,8.3);  tex='dash'   },
  @{ n='dash3';     f=@(4.8,15.0,7.7);  t=@(6.6,15.6,8.3);  tex='dash'   }
)
$parts = @()
foreach ($p in $raw) {
  $parts += @{ n=$p.n; tex=$p.tex
    f=@((S $p.f[0] $CX),(S $p.f[1] $CY),(S $p.f[2] $CZ))
    t=@((S $p.t[0] $CX),(S $p.t[1] $CY),(S $p.t[2] $CZ)) }
}

# ---- 图集区块：名字 -> 序号（与 build_atlas.ps1 一致）----
$tileIdx = @{ shell=0; panel=1; metal=2; spine=3; cursor=4; dash=5; trim=6; dark=7 }
$dirs = @('north','south','east','west','up','down')

function Build-Elements($list) {
  $els = @()
  foreach ($p in $list) {
    $i = $tileIdx[$p.tex]
    $u1 = ($i % 4) * 4; $v1 = [int][Math]::Floor($i/4) * 4
    $uv = @($u1, $v1, ($u1+4), ($v1+4))
    $faces = [ordered]@{}
    foreach ($d in $dirs) { $faces[$d] = [ordered]@{ uv = $uv; texture = '#0' } }
    $els += [ordered]@{ from = $p.f; to = $p.t; faces = $faces }
  }
  return ,$els
}
$display = [ordered]@{
  thirdperson_righthand = [ordered]@{ rotation=@(0,-90,55);  translation=@(0,4.0,0.5);    scale=@(1.25,1.25,1.25) }
  thirdperson_lefthand  = [ordered]@{ rotation=@(0,90,-55);  translation=@(0,4.0,0.5);    scale=@(1.25,1.25,1.25) }
  firstperson_righthand = [ordered]@{ rotation=@(0,-90,25);  translation=@(1.13,3.2,1.13); scale=@(0.85,0.85,0.85) }
  firstperson_lefthand  = [ordered]@{ rotation=@(0,90,-25);  translation=@(1.13,3.2,1.13); scale=@(0.85,0.85,0.85) }
  gui                   = [ordered]@{ rotation=@(30,225,0);  translation=@(0,0,0);        scale=@(1.00,1.00,1.00) }
  ground                = [ordered]@{ rotation=@(0,0,0);     translation=@(0,2.0,0);      scale=@(0.75,0.75,0.75) }
  fixed                 = [ordered]@{ rotation=@(0,180,0);   translation=@(0,0,0);        scale=@(0.75,0.75,0.75) }
}
$textures = [ordered]@{ '0' = 'kunjinkao:item/kun_jin_kao_atlas'; particle = 'kunjinkao:item/kun_jin_kao_atlas' }

$modelDir = Join-Path $Res 'models\item'
New-Item -ItemType Directory -Path $modelDir -Force | Out-Null

# ---- 主模型 ----
# ⚠ parent 必须是 minecraft:block/block，不能是 minecraft:item/handheld：
#   后者的父链根是 builtin/generated，MC 会运行 ItemModelGenerator，
#   拿 layer0 逐像素生成四边形并把模型自带的 elements 整个丢弃 ——
#   表现为"整张图集被铺成一块平面"或"手里的剑直接消失"。这段坑本仓库踩过两次。
$model = [ordered]@{
  parent = 'minecraft:block/block'; ambientocclusion = $false
  display = $display; textures = $textures; elements = (Build-Elements $parts)
}
$out3d = Join-Path $modelDir 'kun_jin_kao_3d.json'
[IO.File]::WriteAllText($out3d, ($model | ConvertTo-Json -Depth 12), [Text.UTF8Encoding]::new($false))
"主模型 -> kun_jin_kao_3d.json （$($parts.Count) 个部件）"

# ---- 8 级编译模型：按部件递进，手里会看着剑自己装起来 ----
$stages = @(
  @('pommel'),
  @('pommel','grip'),
  @('pommel','grip','guardBody'),
  @('pommel','grip','guardBody','finL','finR','screenF','promptF','screenB','promptB'),
  @('pommel','grip','guardBody','finL','finR','screenF','promptF','screenB','promptB','collar','blade1'),
  @('pommel','grip','guardBody','finL','finR','screenF','promptF','screenB','promptB','collar','blade1','blade2'),
  @('pommel','grip','guardBody','finL','finR','screenF','promptF','screenB','promptB','collar','blade1','blade2','blade3'),
  @('pommel','grip','guardBody','finL','finR','screenF','promptF','screenB','promptB','collar','blade1','blade2','blade3','cursorTip','dash1','dash2','dash3')
)
for ($s=0; $s -lt 8; $s++) {
  $subset = @($parts | Where-Object { $stages[$s] -contains $_.n })
  $m = [ordered]@{
    parent = 'minecraft:block/block'; ambientocclusion = $false
    display = $display; textures = $textures; elements = (Build-Elements $subset)
  }
  [IO.File]::WriteAllText((Join-Path $modelDir "kun_jin_kao_compile_$s.json"),
    ($m | ConvertTo-Json -Depth 12), [Text.UTF8Encoding]::new($false))
  "  compile_$s -> $($subset.Count) 个部件"
}