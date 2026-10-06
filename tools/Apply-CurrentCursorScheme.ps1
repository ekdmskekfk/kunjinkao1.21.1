<#
.SYNOPSIS
    把当前光标方案热应用到当前会话（不必注销，也不必重启 explorer）。

.DESCRIPTION
    Windows 改完光标方案要注销或重启 explorer 才生效。本脚本直接读
    HKCU\Control Panel\Cursors，用 SetSystemCursor 立刻把光标换过来。

    相比只换 Arrow / Hand 的简化版，这里补齐了几件非做不可的事：

    1) 覆盖全部 16 个 OCR_* 角色。
       只换箭头和手型的话，其余（I 形、等待、缩放箭头、禁止…）还是旧方案的，
       新旧混在一起比不换更难看。

    2) 成功之后【绝不】DestroyCursor。
       SetSystemCursor 的文档备注：The system destroys hcur by calling the
       DestroyCursor function. 成功后句柄归系统所有，再释放一次就是双重释放 ——
       句柄可能已被回收并分配给别的对象，释放它等于踩别人。
       只有 SetSystemCursor 失败时句柄仍归自己，才由自己释放。

    3) .ico 不是光标格式。
       LoadCursorFromFile 支持 .cur 与 .ani。遇到 .ico 会失败，而且
       GetLastWin32Error 返回 0 —— 看起来像权限问题，实际是类型不对。
       这里按扩展名先行判断并给出明确原因。

    4) 注册表里的值可能不是干净的路径。
       可能被引号包裹，也可能带 ",索引" 后缀。这里按
       "原样存在就用原样，否则再去掉后缀试一次"的顺序处理，
       不无条件截断，免得把路径里合法的逗号切掉。

    5) 半途失败时交代清楚。
       某个角色失败不会回滚已经换掉的，所以脚本会列清楚
       "本次已经改过哪几个"，并给出恢复办法。

.PARAMETER Restore
    让系统按注册表重新加载全部光标（SystemParametersInfo 的 SPI_SETCURSORS）。

    【请看清它的语义】它做的是让"当前会话"与"注册表里的方案"重新对齐，
    不是回到上一个方案的视觉效果 —— 注册表里存的一直是当前方案，
    脚本改的只是会话状态。用途是：某次只应用了一部分、或别的程序把光标弄乱了，
    用它把系统状态摆正。要真正换一套外观，去
    "设置 -> 鼠标 -> 其他鼠标设置 -> 指针"里选方案。

.PARAMETER Roles
    只处理指定角色，例如 -Roles Arrow,Hand。默认全部 16 个。

.PARAMETER ListOnly
    只读注册表并打印打算做什么，不调用任何会修改系统的接口。

.EXAMPLE
    .\Apply-CurrentCursorScheme.ps1 -ListOnly
    先看清要换哪些、路径对不对，再决定动不动手。

.EXAMPLE
    .\Apply-CurrentCursorScheme.ps1
    全部应用。

.EXAMPLE
    .\Apply-CurrentCursorScheme.ps1 -Restore
    让系统按注册表重新加载光标。
#>
[CmdletBinding(SupportsShouldProcess = $true, ConfirmImpact = 'Medium')]
param(
    [switch]$Restore,
    [string[]]$Roles,
    [switch]$ListOnly
)

$ErrorActionPreference = 'Stop'
Set-StrictMode -Version Latest

# ============================ 角色表 ============================
# 值名是 HKCU\Control Panel\Cursors 下的注册表值名；
# ID 是 SetSystemCursor 要求的 OCR_* 常量（注意不是 IDC_*）。
$script:RoleTable = [ordered]@{
    Arrow       = [uint32]32512   # OCR_NORMAL
    IBeam       = [uint32]32513   # OCR_IBEAM
    Wait        = [uint32]32514   # OCR_WAIT
    Crosshair   = [uint32]32515   # OCR_CROSS
    UpArrow     = [uint32]32516   # OCR_UP
    SizeNWSE    = [uint32]32642   # OCR_SIZENWSE
    SizeNESW    = [uint32]32643   # OCR_SIZENESW
    SizeWE      = [uint32]32644   # OCR_SIZEWE
    SizeNS      = [uint32]32645   # OCR_SIZENS
    SizeAll     = [uint32]32646   # OCR_SIZEALL
    No          = [uint32]32648   # OCR_NO
    Hand        = [uint32]32649   # OCR_HAND
    AppStarting = [uint32]32650   # OCR_APPSTARTING
    Help        = [uint32]32651   # OCR_HELP
    Pin         = [uint32]32671   # OCR_PIN
    Person      = [uint32]32672   # OCR_PERSON
}

$script:SPI_SETCURSORS = [uint32]0x0057
$script:SPIF_UPDATEINIFILE = [uint32]0x0001
$script:SPIF_SENDCHANGE = [uint32]0x0002
$script:CursorKey = 'Registry::HKEY_CURRENT_USER\Control Panel\Cursors'
$script:CursorExtensions = @('.cur', '.ani')

# ============================ 原生互操作 ============================
# 这里刻意用字符串数组拼接，而不是 Add-Type 的 here-string：
# 本文件将来若被别的 here-string 包裹（例如自动化脚本生成它），
# 内层 here-string 的结束标记会提前把外层截断。
if (-not ('CursorSchemeNative' -as [type])) {
    $nativeSource = @(
        'using System;'
        'using System.Runtime.InteropServices;'
        ''
        'public static class CursorSchemeNative'
        '{'
        '    [DllImport("user32.dll", CharSet = CharSet.Unicode, SetLastError = true)]'
        '    public static extern IntPtr LoadCursorFromFile(string fileName);'
        ''
        '    [DllImport("user32.dll", SetLastError = true)]'
        '    public static extern bool SetSystemCursor(IntPtr cursor, uint cursorId);'
        ''
        '    [DllImport("user32.dll", SetLastError = true)]'
        '    public static extern bool DestroyCursor(IntPtr cursor);'
        ''
        '    [DllImport("user32.dll", SetLastError = true)]'
        '    public static extern bool SystemParametersInfo(uint uiAction, uint uiParam, IntPtr pvParam, uint fWinIni);'
        '}'
    ) -join [Environment]::NewLine
    Add-Type -TypeDefinition $nativeSource
}

# ============================ 辅助 ============================
function Get-RegistryCursorValue {
    param([Parameter(Mandatory)][string]$Role)

    $item = Get-ItemProperty -LiteralPath $script:CursorKey
    # 不用 $item.$Role：Set-StrictMode -Version Latest 下访问不存在的属性会直接抛
    # "属性不存在"，把本意是"这个角色没配路径"的情况报成一句莫名其妙的错。
    if ($item.PSObject.Properties.Name -notcontains $Role) {
        return $null
    }
    return $item.$Role
}

function Resolve-CursorPath {
    <#
        把注册表里的原始字符串变成可用路径。
        处理两种脏数据：被引号包裹、带 ",索引" 后缀。
        索引后缀【只在原样不存在时才尝试去掉】，避免切掉路径里合法的逗号。
    #>
    param([Parameter(Mandatory)][AllowEmptyString()][string]$Raw)

    $value = $Raw.Trim()
    if ($value.Length -ge 2 -and $value.StartsWith('"') -and $value.EndsWith('"')) {
        $value = $value.Substring(1, $value.Length - 2)
    }
    $value = [Environment]::ExpandEnvironmentVariables($value).Trim()

    if (Test-Path -LiteralPath $value -PathType Leaf) {
        return $value
    }
    if ($value -match '^(?<path>.+),\s*-?\d+\s*$') {
        $trimmed = $matches['path'].Trim()
        if (Test-Path -LiteralPath $trimmed -PathType Leaf) {
            return $trimmed
        }
    }
    return $value
}

# ============================ 恢复 ============================
if ($Restore) {
    if ($PSCmdlet.ShouldProcess('系统光标', '按注册表重新加载（SPI_SETCURSORS）')) {
        $flags = $script:SPIF_UPDATEINIFILE -bor $script:SPIF_SENDCHANGE
        $ok = [CursorSchemeNative]::SystemParametersInfo(
            $script:SPI_SETCURSORS, [uint32]0, [IntPtr]::Zero, $flags)
        if (-not $ok) {
            $code = [Runtime.InteropServices.Marshal]::GetLastWin32Error()
            throw "SystemParametersInfo(SPI_SETCURSORS) 失败。Win32 错误码: $code"
        }
        Write-Host '已让系统按注册表重新加载全部光标。'
    }
    return
}

# ============================ 选定角色 ============================
if ($Roles) {
    $unknown = @($Roles | Where-Object { $script:RoleTable.Keys -notcontains $_ })
    if ($unknown.Count -gt 0) {
        throw "未知角色: $($unknown -join ', ')。可用: $($script:RoleTable.Keys -join ', ')"
    }
    $selected = @($Roles)
} else {
    $selected = @($script:RoleTable.Keys)
}

$applied = New-Object System.Collections.Generic.List[string]
$skipped = New-Object System.Collections.Generic.List[string]
$failed  = New-Object System.Collections.Generic.List[string]
$abortRole = $null

# ============================ 逐个应用 ============================
foreach ($role in $selected) {
    $cursorId = $script:RoleTable[$role]

    $raw = Get-RegistryCursorValue -Role $role
    if ([string]::IsNullOrWhiteSpace($raw)) {
        $skipped.Add("$role —— 注册表里没有配置路径")
        continue
    }

    $cursorPath = Resolve-CursorPath -Raw $raw
    if (-not (Test-Path -LiteralPath $cursorPath -PathType Leaf)) {
        $skipped.Add("$role —— 文件不存在: $cursorPath")
        continue
    }

    $extension = [IO.Path]::GetExtension($cursorPath).ToLowerInvariant()
    $wrongFormat = $script:CursorExtensions -notcontains $extension
    if ($wrongFormat) {
        Write-Warning "$role 指向的不是光标格式（$extension）: $cursorPath"
        Write-Warning '  LoadCursorFromFile 只支持 .cur 与 .ani；若下一步报 Win32 错误码 0，原因就是文件类型。'
    }

    if ($ListOnly -or -not $PSCmdlet.ShouldProcess("$role (OCR $cursorId)", "应用 $cursorPath")) {
        $applied.Add("$role (OCR $cursorId) <- $cursorPath")
        continue
    }

    $handle = [CursorSchemeNative]::LoadCursorFromFile($cursorPath)
    if ($handle -eq [IntPtr]::Zero) {
        $code = [Runtime.InteropServices.Marshal]::GetLastWin32Error()
        $hint = if ($wrongFormat) { '（扩展名不是 .cur/.ani，很可能就是原因）' } else { '' }
        $failed.Add("$role —— LoadCursorFromFile 失败，Win32 错误码 $code$hint : $cursorPath")
        $abortRole = $role
        break
    }

    if (-not [CursorSchemeNative]::SetSystemCursor($handle, $cursorId)) {
        $code = [Runtime.InteropServices.Marshal]::GetLastWin32Error()
        # 走到这里系统没有接管句柄，仍然归自己，必须释放。
        [void][CursorSchemeNative]::DestroyCursor($handle)
        $failed.Add("$role —— SetSystemCursor 失败，Win32 错误码 $code")
        $abortRole = $role
        break
    }

    # 【成功路径上绝不要 DestroyCursor】—— 句柄已归系统所有，再释放就是双重释放。
    $applied.Add("$role (OCR $cursorId) <- $cursorPath")
}

# ============================ 汇报 ============================
Write-Host ''
Write-Host '================ 结果 ================'

if ($applied.Count -gt 0) {
    if ($ListOnly) { Write-Host '将要应用:' } else { Write-Host '已应用:' }
    foreach ($line in $applied) { Write-Host "  $line" }
}
if ($skipped.Count -gt 0) {
    Write-Host '已跳过:'
    foreach ($line in $skipped) { Write-Host "  $line" }
}
if ($failed.Count -gt 0) {
    Write-Host '失败:'
    foreach ($line in $failed) { Write-Host "  $line" -ForegroundColor Red }
}

if ($abortRole) {
    Write-Host ''
    Write-Warning "中途停止：$abortRole 处理失败。"
    if ($applied.Count -gt 0 -and -not $ListOnly) {
        Write-Warning '本次已经改过的角色【不会被回滚】：'
        foreach ($line in $applied) { Write-Warning "  $line" }
    }
    $rest = @($selected | Where-Object { $_ -ne $abortRole })
    Write-Warning '恢复办法（任选其一）:'
    Write-Warning '  1) 修正失败的那一项后重跑本脚本；'
    if ($rest.Count -gt 0) {
        Write-Warning "  2) 排除它重跑: .\Apply-CurrentCursorScheme.ps1 -Roles $($rest -join ',')"
    }
    Write-Warning '  3) 让系统按注册表重新加载: .\Apply-CurrentCursorScheme.ps1 -Restore'
    exit 1
}

Write-Host ''
if ($ListOnly) {
    Write-Host '（-ListOnly：没有做任何修改。去掉该参数即执行。）'
} else {
    Write-Host '当前会话的光标已按方案更新。'
}