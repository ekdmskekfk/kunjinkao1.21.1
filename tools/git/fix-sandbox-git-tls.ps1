# 让 git 在 DSH 沙箱里也能走 HTTPS。
#
# 背景（本机实测）：
#   1) 沙箱里 schannel 拿不到凭证（SEC_E_NO_CREDENTIALS），所以必须改用 openssl 后端；
#   2) 本机装了 Watt Toolkit（Steam++）在做 HTTPS 中间人，github.com 实际给的是
#      "CN=SteamTools Certificate" 签的证书，而 Mozilla 根表里没有它 —— 于是校验失败；
#   3) 沙箱里 MSYS2 的程序（sh.exe / openssl.exe）起不来：它们启动时要建命名管道，被沙箱禁止。
#      这不影响 http.sslBackend=openssl（那是 libcurl 内部用 OpenSSL，不另起进程）。
#
# 这个脚本做两件事（都只影响 git，不碰系统证书库）：
#   把 Mozilla 根表 + 系统证书库里 SteamTools 的根合成一份，放到 %USERPROFILE%\.git-ca\combined-ca.pem
#   给 git 设全局的 http.sslBackend=openssl 与 http.sslCAInfo 指向它
#
# 请在**你自己的终端**里跑（不要经沙箱），因为它要写用户目录与全局 .gitconfig。
# 加速器换根/升级后重跑一次即可。

$ErrorActionPreference = 'Stop'

$caDir  = Join-Path $env:USERPROFILE '.git-ca'
$bundle = Join-Path $caDir 'combined-ca.pem'
New-Item -ItemType Directory -Force -Path $caDir | Out-Null

$mozilla = 'C:\Program Files\Git\mingw64\etc\ssl\certs\ca-bundle.crt'
if (-not (Test-Path $mozilla)) { throw "找不到 Mozilla 根表: $mozilla" }

$sb = [Text.StringBuilder]::new()
[void]$sb.Append((Get-Content $mozilla -Raw))

$roots = Get-ChildItem Cert:\LocalMachine\Root, Cert:\CurrentUser\Root -ErrorAction SilentlyContinue |
    Where-Object { $_.Subject -match 'SteamTools|BeyondDimension' } |
    Sort-Object Thumbprint -Unique
foreach ($root in $roots) {
    $b64 = [Convert]::ToBase64String(
        $root.Export([System.Security.Cryptography.X509Certificates.X509ContentType]::Cert),
        'InsertLineBreaks')
    [void]$sb.Append("`n-----BEGIN CERTIFICATE-----`n$b64`n-----END CERTIFICATE-----`n")
}
[IO.File]::WriteAllText($bundle, $sb.ToString(), [Text.UTF8Encoding]::new($false))
"合成根表: $bundle"
"Mozilla 根 + SteamTools 根 $($roots.Count) 张"

# git 的配置文件里用正斜杠更稳
$bundleForGit = $bundle -replace '\\', '/'
git config --global http.sslBackend openssl
git config --global http.sslCAInfo $bundleForGit

"已写入全局配置:"
"  http.sslBackend = $(git config --global --get http.sslBackend)"
"  http.sslCAInfo  = $(git config --global --get http.sslCAInfo)"

"验证（受限沙箱里现在应该能列引用）:"
git -C (Split-Path -Parent (Split-Path -Parent $PSScriptRoot)) ls-remote --heads origin | Select-Object -First 2