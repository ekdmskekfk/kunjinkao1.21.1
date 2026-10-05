<#
  生成剑的贴图图集（64x64 一个区块图，共 8 个 16x16 区块），并做成【动画贴图】。

  ## 为什么是动画

  Minecraft 支持贴图动画：把 N 帧竖着堆成一张图，再配一个同名 .png.mcmeta，
  游戏就会自动循环播放。关键在于 —— 【动画贴图的 UV 只映射到第一帧】。

  所以模型里那些 `uv: [8,4,12,8]` 之类的子矩形【一个字节都不用改】：
  它们指向的仍然是第一帧里的那块 16x16 区域，只是那块区域的像素每几刻换一帧。
  于是刀身能流动、光标能呼吸、屏幕能扫描，而模型 JSON 完全不动。

  ## 各区块的动画

  - T3 spine  青色刀脊：一条能量带沿刀身向上流动（8 帧走完 16px，无缝循环）
  - T4 cursor 光标亮块：呼吸明暗
  - T5 dash   日志段：亮条在 12px 宽度内来回扫
  - T7 dark   屏幕底：扫描线向下移动
  - T0/T1/T2/T6 外壳/面板/金属/倒角：静态，每帧复制同一份

  ## 区块顺序（与 export_model.ps1 里的 $tileIdx 必须一致）
    0 shell  握把斜纹缠绕     4 cursor 光标亮块
    1 panel  护手面板         5 dash   日志段
    2 metal  拉丝金属         6 trim   倒角
    3 spine  青色刀脊         7 dark   屏幕底
#>
param(
  [string]$Out     = "$PSScriptRoot\..\..\src\main\resources\assets\kunjinkao\textures\item\kun_jin_kao_atlas.png",
  [string]$Preview = "$PSScriptRoot\..\..\docs\art\atlas_x8.png",
  [int]$Frames     = 8,
  [int]$Frametime  = 3
)
Add-Type -AssemblyName System.Drawing

function Clamp([int]$v) { [Math]::Max(0, [Math]::Min(255, $v)) }

# 一张 64 x (64*Frames) 的帧条：每 64 行是一帧完整的图集
$atlas = New-Object System.Drawing.Bitmap(64, (64 * $Frames))
function Px($b,$x,$y,$r,$g,$bl){ $b.SetPixel($x, $y, [System.Drawing.Color]::FromArgb(255, (Clamp $r), (Clamp $g), (Clamp $bl))) }
function Fill($b,$ox,$oy,$r,$g,$bl){ for($y=0;$y -lt 16;$y++){ for($x=0;$x -lt 16;$x++){ Px $b ($ox+$x) ($oy+$y) $r $g $bl } } }

for ($f = 0; $f -lt $Frames; $f++) {
  $oy = $f * 64          # 本帧在帧条里的行偏移

  # ================= T0 shell —— 握把斜纹缠绕（静态） =================
  Fill $atlas 0 $oy 30 36 46
  for($y=0;$y -lt 16;$y++){ for($x=0;$x -lt 16;$x++){ if((($x+$y) % 4) -lt 2){ Px $atlas $x ($oy+$y) 20 24 32 } } }
  for($i=0;$i -lt 16;$i++){ Px $atlas $i $oy 44 52 64; Px $atlas $i ($oy+15) 18 22 30; Px $atlas 0 ($oy+$i) 44 52 64; Px $atlas 15 ($oy+$i) 18 22 30 }

  # ================= T1 panel —— 护手面板（静态） =================
  Fill $atlas 16 $oy 46 56 70
  for($i=0;$i -lt 16;$i++){
    Px $atlas (16+$i) $oy 66 78 94;          Px $atlas (16+$i) ($oy+1)  60 72 88
    Px $atlas (16+$i) ($oy+14) 28 34 44;     Px $atlas (16+$i) ($oy+15) 24 30 40
    Px $atlas 16 ($oy+$i) 30 37 47;          Px $atlas 17 ($oy+$i) 34 42 53
    Px $atlas 30 ($oy+$i) 28 34 44;          Px $atlas 31 ($oy+$i) 24 30 40 }
  for($x=4;$x -lt 12;$x++){ Px $atlas (16+$x) ($oy+8) 34 42 54 }

  # ================= T2 metal —— 拉丝（静态） =================
  Fill $atlas 32 $oy 128 140 152
  for($y=0;$y -lt 16;$y++){ for($x=0;$x -lt 16;$x++){ if(($y % 2) -eq 0){ Px $atlas (32+$x) ($oy+$y) 116 128 140 } } }
  for($x=0;$x -lt 16;$x++){ Px $atlas (32+$x) ($oy+2) 176 186 196 }
  for($y=0;$y -lt 16;$y++){ Px $atlas 32 ($oy+$y) 100 110 122; Px $atlas 47 ($oy+$y) 100 110 122 }

  # ================= T3 spine —— 青色刀脊：能量带向上流动 =================
  # 每帧位移 2px，8 帧正好走完 16px 一轮，首尾无缝。
  $shift = $f * (16 / $Frames)
  for($y=0;$y -lt 16;$y++){
    # 一条 3px 的亮带 + 3px 的过渡，位置随帧号上移
    $band = ((($y - $shift) % 16) + 16) % 16
    $glow = if ($band -lt 3) { 1.0 } elseif ($band -lt 6) { 0.4 } else { 0.0 }
    for($x=0;$x -lt 16;$x++){
      if($x -le 2 -or $x -ge 13){ $r=52; $g=150; $b=205 }
      elseif($x -eq 6 -or $x -eq 9){ $r=122; $g=222; $b=250 }
      elseif($x -eq 7 -or $x -eq 8){ $r=176; $g=242; $b=255 }
      else { $r=87; $g=207; $b=255 }
      if ($glow -gt 0) { $r = $r + 62*$glow; $g = $g + 13*$glow; $b = $b + 0*$glow }
      Px $atlas (48+$x) ($oy+$y) $r $g $b
    }
  }
  # 两道静态的横向刻痕，给流动一个参照物
  for($x=3;$x -lt 13;$x++){ Px $atlas (48+$x) ($oy+5) 58 158 208; Px $atlas (48+$x) ($oy+12) 58 158 208 }

  # ================= T4 cursor —— 光标亮块：呼吸 =================
  $pulse = @(1.00, 1.00, 0.95, 0.88, 0.80, 0.88, 0.95, 1.00)[$f % 8]
  Fill $atlas 0 (16+$oy) 190 245 255
  for($i=0;$i -lt 16;$i++){
    Px $atlas $i (16+$oy) 118 198 228; Px $atlas $i (16+$oy+15) 118 198 228
    Px $atlas 0 (16+$oy+$i) 118 198 228; Px $atlas 15 (16+$oy+$i) 118 198 228 }
  for($y=3;$y -le 12;$y++){ for($x=3;$x -le 12;$x++){ Px $atlas $x (16+$oy+$y) (228*$pulse) (252*$pulse) (255*$pulse) } }

  # ================= T5 dash —— 日志段：亮条来回扫 =================
  Fill $atlas 16 (16+$oy) 48 124 156
  for($i=0;$i -lt 16;$i++){
    Px $atlas (16+$i) (16+$oy) 32 88 116; Px $atlas (16+$i) (16+$oy+15) 32 88 116
    Px $atlas 16 (16+$oy+$i) 32 88 116;   Px $atlas 31 (16+$oy+$i) 32 88 116 }
  # 亮条在 x=2..13 这 12px 内往复，8 帧走一个来回
  $span = 12
  $pos = ($f * 2) % ($span * 2)
  if ($pos -ge $span) { $pos = $span * 2 - $pos }
  $barX = 2 + $pos
  for($x=2;$x -lt 14;$x++){
    if ($x -ge $barX -and $x -lt ($barX + 4)) { Px $atlas (16+$x) (16+$oy+7) 108 200 228; Px $atlas (16+$x) (16+$oy+8) 108 200 228 }
    else { Px $atlas (16+$x) (16+$oy+7) 78 168 198; Px $atlas (16+$x) (16+$oy+8) 78 168 198 }
  }

  # ================= T6 trim —— 倒角（静态） =================
  Fill $atlas 32 (16+$oy) 92 104 116
  for($i=0;$i -lt 16;$i++){
    Px $atlas (32+$i) (16+$oy) 138 150 164;  Px $atlas (32+$i) (16+$oy+1) 126 138 152
    Px $atlas 32 (16+$oy+$i) 138 150 164;    Px $atlas 33 (16+$oy+$i) 126 138 152
    Px $atlas (32+$i) (16+$oy+15) 52 60 72;  Px $atlas (32+$i) (16+$oy+14) 60 68 80
    Px $atlas 47 (16+$oy+$i) 52 60 72;       Px $atlas 46 (16+$oy+$i) 60 68 80 }

  # ================= T7 dark —— 屏幕底：扫描线向下移动 =================
  Fill $atlas 48 (16+$oy) 14 17 22
  for($i=0;$i -lt 16;$i++){
    Px $atlas (48+$i) (16+$oy) 36 44 56; Px $atlas (48+$i) (16+$oy+15) 36 44 56
    Px $atlas 48 (16+$oy+$i) 36 44 56;   Px $atlas 63 (16+$oy+$i) 36 44 56 }
  # 扫描线在 0..14 之间每帧走 2px，8 帧走完一轮
  $scanY = ($f * 2) % 16
  if ($scanY -gt 14) { $scanY = 14 }
  for($x=2;$x -lt 14;$x++){ Px $atlas (48+$x) (16+$oy+$scanY) 30 74 94 }
}

New-Item -ItemType Directory -Path (Split-Path $Out -Parent) -Force | Out-Null
$atlas.Save($Out, [System.Drawing.Imaging.ImageFormat]::Png)

# 动画描述文件必须与贴图同名、加 .mcmeta 后缀
$meta = '{' + "`n" + '  "animation": {' + "`n" + '    "frametime": ' + $Frametime + "`n" + '  }' + "`n" + '}' + "`n"
[IO.File]::WriteAllText(($Out + '.mcmeta'), $meta, [Text.UTF8Encoding]::new($false))

# 放大预览：第一帧 x8，以及整条帧条 x2
New-Item -ItemType Directory -Path (Split-Path $Preview -Parent) -Force | Out-Null
$frame0 = New-Object System.Drawing.Bitmap(64,64)
$g0 = [System.Drawing.Graphics]::FromImage($frame0)
$g0.DrawImage($atlas, (New-Object System.Drawing.Rectangle(0,0,64,64)), (New-Object System.Drawing.Rectangle(0,0,64,64)), [System.Drawing.GraphicsUnit]::Pixel)
$g0.Dispose()
$big = New-Object System.Drawing.Bitmap(512,512)
$g2 = [System.Drawing.Graphics]::FromImage($big)
$g2.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::NearestNeighbor
$g2.PixelOffsetMode = [System.Drawing.Drawing2D.PixelOffsetMode]::Half
$g2.DrawImage($frame0, 0, 0, 512, 512); $g2.Dispose()
$big.Save($Preview, [System.Drawing.Imaging.ImageFormat]::Png); $big.Dispose(); $frame0.Dispose()

$strip = Join-Path (Split-Path $Preview -Parent) 'atlas_strip_x2.png'
$sb = New-Object System.Drawing.Bitmap((64*2), (64*$Frames*2))
$g3 = [System.Drawing.Graphics]::FromImage($sb)
$g3.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::NearestNeighbor
$g3.PixelOffsetMode = [System.Drawing.Drawing2D.PixelOffsetMode]::Half
$g3.DrawImage($atlas, 0, 0, (64*2), (64*$Frames*2)); $g3.Dispose()
$sb.Save($strip, [System.Drawing.Imaging.ImageFormat]::Png); $sb.Dispose()

$atlas.Dispose()
"图集已生成: $Out  (64 x $(64*$Frames)，$Frames 帧)"
"动画描述  : $Out.mcmeta  (frametime=$Frametime)"
"放大预览  : $Preview"
"帧条预览  : $strip"
