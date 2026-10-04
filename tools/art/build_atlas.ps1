<#
  生成剑的贴图图集（64x64，8 个 16x16 区块），并输出一份放大 8 倍的查看图。

  区块顺序（与 export_model.ps1 里的 $tileIdx 必须一致）：
    0 shell  握把斜纹缠绕     4 cursor 光标亮块
    1 panel  护手面板         5 dash   日志段
    2 metal  拉丝金属         6 trim   倒角
    3 spine  青色刀脊         7 dark   屏幕底
#>
param(
  [string]$Out     = "$PSScriptRoot\..\..\src\main\resources\assets\kunjinkao\textures\item\kun_jin_kao_atlas.png",
  [string]$Preview = "$PSScriptRoot\..\..\docs\art\atlas_x8.png"
)
Add-Type -AssemblyName System.Drawing
$atlas = New-Object System.Drawing.Bitmap(64,64)
function Px($b,$x,$y,$r,$g,$bl){ $b.SetPixel($x,$y,[System.Drawing.Color]::FromArgb(255,$r,$g,$bl)) }
function Fill($b,$ox,$oy,$r,$g,$bl){ for($y=0;$y -lt 16;$y++){for($x=0;$x -lt 16;$x++){Px $b ($ox+$x) ($oy+$y) $r $g $bl}} }

# T0 shell —— 握把斜纹缠绕
Fill $atlas 0 0 30 36 46
for($y=0;$y -lt 16;$y++){ for($x=0;$x -lt 16;$x++){ if((($x+$y) % 4) -lt 2){ Px $atlas $x $y 20 24 32 } } }
for($i=0;$i -lt 16;$i++){ Px $atlas $i 0 44 52 64; Px $atlas $i 15 18 22 30; Px $atlas 0 $i 44 52 64; Px $atlas 15 $i 18 22 30 }

# T1 panel —— 护手面板，带明显边框
Fill $atlas 16 0 46 56 70
for($i=0;$i -lt 16;$i++){
  Px $atlas (16+$i) 0 66 78 94; Px $atlas (16+$i) 1 60 72 88
  Px $atlas (16+$i) 14 28 34 44; Px $atlas (16+$i) 15 24 30 40
  Px $atlas 16 $i 30 37 47; Px $atlas 17 $i 34 42 53
  Px $atlas 30 $i 28 34 44; Px $atlas 31 $i 24 30 40 }
for($x=4;$x -lt 12;$x++){ Px $atlas (16+$x) 8 34 42 54 }

# T2 metal —— 拉丝
Fill $atlas 32 0 128 140 152
for($y=0;$y -lt 16;$y++){ for($x=0;$x -lt 16;$x++){ if(($y % 2) -eq 0){ Px $atlas (32+$x) $y 116 128 140 } } }
for($x=0;$x -lt 16;$x++){ Px $atlas (32+$x) 2 176 186 196 }
for($y=0;$y -lt 16;$y++){ Px $atlas 32 $y 100 110 122; Px $atlas 47 $y 100 110 122 }

# T3 spine —— 亮芯收窄到 2px
Fill $atlas 48 0 87 207 255
for($y=0;$y -lt 16;$y++){
  for($x=0;$x -lt 16;$x++){
    if($x -le 2 -or $x -ge 13){ Px $atlas (48+$x) $y 52 150 205 }
    elseif($x -eq 6 -or $x -eq 9){ Px $atlas (48+$x) $y 122 222 250 }
    elseif($x -eq 7 -or $x -eq 8){ Px $atlas (48+$x) $y 176 242 255 } } }
for($x=3;$x -lt 13;$x++){ Px $atlas (48+$x) 5 58 158 208; Px $atlas (48+$x) 12 58 158 208 }

# T4 cursor
Fill $atlas 0 16 190 245 255
for($i=0;$i -lt 16;$i++){ Px $atlas $i 16 118 198 228; Px $atlas $i 31 118 198 228; Px $atlas 0 (16+$i) 118 198 228; Px $atlas 15 (16+$i) 118 198 228 }
for($y=3;$y -le 12;$y++){ for($x=3;$x -le 12;$x++){ Px $atlas $x (16+$y) 228 252 255 } }

# T5 dash
Fill $atlas 16 16 48 124 156
for($i=0;$i -lt 16;$i++){ Px $atlas (16+$i) 16 32 88 116; Px $atlas (16+$i) 31 32 88 116; Px $atlas 16 (16+$i) 32 88 116; Px $atlas 31 (16+$i) 32 88 116 }
for($x=2;$x -lt 14;$x++){ Px $atlas (16+$x) 23 78 168 198; Px $atlas (16+$x) 24 78 168 198 }

# T6 trim
Fill $atlas 32 16 92 104 116
for($i=0;$i -lt 16;$i++){
  Px $atlas (32+$i) 16 138 150 164; Px $atlas (32+$i) 17 126 138 152
  Px $atlas 32 (16+$i) 138 150 164; Px $atlas 33 (16+$i) 126 138 152
  Px $atlas (32+$i) 31 52 60 72;   Px $atlas (32+$i) 30 60 68 80
  Px $atlas 47 (16+$i) 52 60 72;   Px $atlas 46 (16+$i) 60 68 80 }

# T7 dark —— 屏幕底 + 淡青扫描线
Fill $atlas 48 16 14 17 22
for($i=0;$i -lt 16;$i++){ Px $atlas (48+$i) 16 36 44 56; Px $atlas (48+$i) 31 36 44 56; Px $atlas 48 (16+$i) 36 44 56; Px $atlas 63 (16+$i) 36 44 56 }
for($x=2;$x -lt 14;$x++){ Px $atlas (48+$x) 18 30 74 94 }

New-Item -ItemType Directory -Path (Split-Path $Out -Parent) -Force | Out-Null
$atlas.Save($Out,[System.Drawing.Imaging.ImageFormat]::Png)
New-Item -ItemType Directory -Path (Split-Path $Preview -Parent) -Force | Out-Null
$big = New-Object System.Drawing.Bitmap(512,512)
$g2 = [System.Drawing.Graphics]::FromImage($big)
$g2.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::NearestNeighbor
$g2.PixelOffsetMode = [System.Drawing.Drawing2D.PixelOffsetMode]::Half
$g2.DrawImage($atlas,0,0,512,512); $g2.Dispose()
$big.Save($Preview,[System.Drawing.Imaging.ImageFormat]::Png); $big.Dispose()
$atlas.Dispose()
"图集已生成: $Out"
"放大预览  : $Preview"