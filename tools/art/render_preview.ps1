Add-Type -AssemblyName System.Drawing
$dir = Join-Path $PSScriptRoot '..\..\docs\art'
$atlasPath = Join-Path $PSScriptRoot '..\..\src\main\resources\assets\kunjinkao\textures\item\kun_jin_kao_atlas.png'
New-Item -ItemType Directory -Path $dir -Force | Out-Null
$atlas = [System.Drawing.Image]::FromFile($atlasPath)

# ---- 整体缩放：以 (8,0,8) 为中心等比缩小 ----
$K = 0.92
$CX = 8.0; $CY = 0.0; $CZ = 8.0
function S($v, $c) { return $c + ($v - $c) * $K }

$partsRaw = @(
  @{ f=@(6.9,-0.4,6.9);  t=@(9.1,0.8,9.1);   tex='trim'   },
  @{ f=@(7.1, 0.8,7.1);  t=@(8.9,4.6,8.9);   tex='shell'  },
  @{ f=@(4.6, 4.6,6.4);  t=@(11.4,6.8,9.6);  tex='panel'  },
  @{ f=@(3.6, 5.0,7.0);  t=@(4.6,6.4,9.0);   tex='trim'   },
  @{ f=@(11.4,5.0,7.0);  t=@(12.4,6.4,9.0);  tex='trim'   },
  @{ f=@(5.4, 4.9,6.15); t=@(10.6,6.5,6.40); tex='dark'   },
  @{ f=@(6.6, 5.3,5.95); t=@(9.4,5.95,6.15); tex='cursor' },
  @{ f=@(5.4, 4.9,9.60); t=@(10.6,6.5,9.85); tex='dark'   },
  @{ f=@(6.6, 5.3,9.85); t=@(9.4,5.95,10.05);tex='cursor' },
  @{ f=@(6.9, 6.8,6.9);  t=@(9.1,7.3,9.1);   tex='trim'   },
  @{ f=@(7.2, 7.3,7.60); t=@(8.8,11.4,8.40); tex='spine'  },
  @{ f=@(7.3,11.4,7.65); t=@(8.7,15.0,8.35); tex='spine'  },
  @{ f=@(7.4,15.0,7.70); t=@(8.6,17.6,8.30); tex='spine'  },
  @{ f=@(7.0,17.6,7.50); t=@(9.0,19.2,8.50); tex='cursor' },
  @{ f=@(4.6, 9.2,7.7);  t=@(6.4,9.8,8.3);   tex='dash'   },
  @{ f=@(4.2,12.2,7.7);  t=@(6.0,12.8,8.3);  tex='dash'   },
  @{ f=@(4.8,15.0,7.7);  t=@(6.6,15.6,8.3);  tex='dash'   }
)
$parts = @()
foreach ($p in $partsRaw) {
  $parts += @{ f=@((S $p.f[0] $CX),(S $p.f[1] $CY),(S $p.f[2] $CZ))
               t=@((S $p.t[0] $CX),(S $p.t[1] $CY),(S $p.t[2] $CZ))
               tex=$p.tex }
}
$tileIdx = @{ shell=0; panel=1; metal=2; spine=3; cursor=4; dash=5; trim=6; dark=7 }
function TileRect($name) {
  $i=$tileIdx[$name]; return New-Object System.Drawing.RectangleF((($i%4)*16),([int][Math]::Floor($i/4)*16),16,16)
}
function BuildFaces($mode,$scale,$offX,$offY) {
  $list = New-Object 'System.Collections.Generic.List[object]'
  foreach ($p in $parts) {
    $x0=$p.f[0];$y0=$p.f[1];$z0=$p.f[2]; $x1=$p.t[0];$y1=$p.t[1];$z1=$p.t[2]
    $r = TileRect $p.tex
    $defs = @(
      @{ pts=@(@($x0,$y1,$z0),@($x1,$y1,$z0),@($x1,$y1,$z1),@($x0,$y1,$z1)) },
      @{ pts=@(@($x0,$y0,$z0),@($x1,$y0,$z0),@($x1,$y0,$z1),@($x0,$y0,$z1)) },
      @{ pts=@(@($x0,$y0,$z0),@($x1,$y0,$z0),@($x1,$y1,$z0),@($x0,$y1,$z0)) },
      @{ pts=@(@($x0,$y0,$z1),@($x1,$y0,$z1),@($x1,$y1,$z1),@($x0,$y1,$z1)) },
      @{ pts=@(@($x0,$y0,$z0),@($x0,$y0,$z1),@($x0,$y1,$z1),@($x0,$y1,$z0)) },
      @{ pts=@(@($x1,$y0,$z0),@($x1,$y0,$z1),@($x1,$y1,$z1),@($x1,$y1,$z0)) } )
    foreach ($d in $defs) {
      $pp=@(); $sum=0.0
      foreach ($pt in $d.pts) {
        $sum += ($pt[0]+$pt[1]+$pt[2])
        if ($mode -eq 'iso') {
          $px = (($pt[0]-$pt[2])*0.8660254)*$scale + $offX
          $py = ((($pt[0]+$pt[2])*0.5 - $pt[1]))*$scale + $offY
        } else {
          $px = $pt[0]*$scale + $offX; $py = -$pt[1]*$scale + $offY
        }
        $pp += ,@($px,$py)
      }
      $list.Add([pscustomobject]@{ depth=($sum/4.0); pts=$pp; rect=$r })
    }
  }
  return $list
}
function DrawFaces($gr, $faces) {
  foreach ($f in ($faces | Sort-Object depth)) {
    $pts=$f.pts; $ul=-1;$rIdx=-1;$dIdx=-1
    for ($i=0;$i -lt 4;$i++){
      $a=($i+1)%4; $b=($i+3)%4
      if (($pts[$a][0] -gt $pts[$i][0]+0.01) -and ($pts[$b][1] -gt $pts[$i][1]+0.01)) { $ul=$i;$rIdx=$a;$dIdx=$b; break }
      if (($pts[$b][0] -gt $pts[$i][0]+0.01) -and ($pts[$a][1] -gt $pts[$i][1]+0.01)) { $ul=$i;$rIdx=$b;$dIdx=$a; break }
    }
    if ($ul -lt 0) { continue }
    $dest = New-Object 'System.Drawing.PointF[]' 3
    $dest[0]=New-Object System.Drawing.PointF([single]$pts[$ul][0],[single]$pts[$ul][1])
    $dest[1]=New-Object System.Drawing.PointF([single]$pts[$rIdx][0],[single]$pts[$rIdx][1])
    $dest[2]=New-Object System.Drawing.PointF([single]$pts[$dIdx][0],[single]$pts[$dIdx][1])
    $gr.DrawImage($atlas,$dest,$f.rect,[System.Drawing.GraphicsUnit]::Pixel)
  }
}
# ---- 透明背景的图标：自动裁到内容并缩到指定尺寸 ----
function RenderIcon($mode, $size, $file) {
  $f0 = BuildFaces $mode 1.0 0 0
  $xs=@(); $ys=@()
  foreach ($f in $f0) { foreach ($pt in $f.pts) { $xs += $pt[0]; $ys += $pt[1] } }
  $xMin=($xs|Measure-Object -Minimum).Minimum; $xMax=($xs|Measure-Object -Maximum).Maximum
  $yMin=($ys|Measure-Object -Minimum).Minimum; $yMax=($ys|Measure-Object -Maximum).Maximum
  $w = $xMax-$xMin; $h = $yMax-$yMin
  $sc = ($size * 0.95) / [Math]::Max($w,$h)
  $offX = (-$xMin*$sc) + ($size - $w*$sc)/2
  $offY = (-$yMin*$sc) + ($size - $h*$sc)/2
  $faces = BuildFaces $mode $sc $offX $offY
  $bmp = New-Object System.Drawing.Bitmap($size,$size,[System.Drawing.Imaging.PixelFormat]::Format32bppArgb)
  $gr = [System.Drawing.Graphics]::FromImage($bmp)
  $gr.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::NearestNeighbor
  $gr.PixelOffsetMode = [System.Drawing.Drawing2D.PixelOffsetMode]::Half
  $gr.Clear([System.Drawing.Color]::Transparent)
  DrawFaces $gr $faces
  $gr.Dispose(); $bmp.Save($file,[System.Drawing.Imaging.ImageFormat]::Png); $bmp.Dispose()
  "  $mode ${size}x${size}  内容占比 $([math]::Round(($sc*[Math]::Max($w,$h))/$size*100,0))%  ->  $(Split-Path $file -Leaf)"
}
'图标:'
RenderIcon 'iso'   256 (Join-Path $dir 'icon_iso_256.png')
RenderIcon 'iso'   128 (Join-Path $dir 'icon_iso_128.png')
RenderIcon 'iso'   64  (Join-Path $dir 'icon_iso_64.png')
RenderIcon 'front' 128 (Join-Path $dir 'icon_front_128.png')
''
'=== 证明图（透明背景，尺寸不变）==='
function RenderProof($mode,$scale,$file) {
  $f0 = BuildFaces $mode $scale 0 0
  $xs=@(); $ys=@()
  foreach ($f in $f0) { foreach ($pt in $f.pts) { $xs += $pt[0]; $ys += $pt[1] } }
  $xMin=($xs|Measure-Object -Minimum).Minimum; $xMax=($xs|Measure-Object -Maximum).Maximum
  $yMin=($ys|Measure-Object -Minimum).Minimum; $yMax=($ys|Measure-Object -Maximum).Maximum
  $m=24; $W=[int]($xMax-$xMin)+$m*2; $H=[int]($yMax-$yMin)+$m*2
  $faces = BuildFaces $mode $scale (-$xMin+$m) (-$yMin+$m)
  $bmp = New-Object System.Drawing.Bitmap($W,$H,[System.Drawing.Imaging.PixelFormat]::Format32bppArgb)
  $gr = [System.Drawing.Graphics]::FromImage($bmp)
  $gr.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::NearestNeighbor
  $gr.PixelOffsetMode = [System.Drawing.Drawing2D.PixelOffsetMode]::Half
  $gr.Clear([System.Drawing.Color]::Transparent)
  DrawFaces $gr $faces
  $gr.Dispose(); $bmp.Save($file,[System.Drawing.Imaging.ImageFormat]::Png); $bmp.Dispose()
  "  $mode -> ${W}x${H}"
}
RenderProof 'iso'   42 (Join-Path $dir 'view_iso.png')
RenderProof 'front' 42 (Join-Path $dir 'view_front.png')
$atlas.Dispose()