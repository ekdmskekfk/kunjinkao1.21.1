@echo off
setlocal EnableExtensions
rem ===========================================================================
rem  kunjinkao (NeoForge 1.21.1) build script
rem
rem  Usage:
rem    build.cmd                       gradle build (default)
rem    build.cmd clean build           clean, then build
rem    build.cmd build --offline       use the local cache only
rem    build.cmd --offline             args starting with "-" are appended to build
rem    build.cmd -PmodJarDir=D:/out    override the jar output folder
rem
rem  Why not gradlew: the local gradlew/gradlew.bat point at a <project>/lib Gradle
rem  distribution that is not part of the repository, so Gradle is looked up as:
rem    PATH -> cached ~/.gradle/wrapper/dists Gradle 8.x -> .\gradlew.bat
rem  JDK: JAVA_HOME when it is 21, otherwise the usual jdk-21* install locations.
rem  After the build, build.gradle's copyModJar task copies the mod jar to
rem  F:\mcmodli\tang\neoforge1.21.1mod (override with -PmodJarDir=...).
rem ===========================================================================

set "PROJECT_DIR=%~dp0"
if "%PROJECT_DIR:~-1%"=="\" set "PROJECT_DIR=%PROJECT_DIR:~0,-1%"
set "MODJAR_DIR=F:\mcmodli\tang\neoforge1.21.1mod"
set "RC=1"

rem ---- 1) JDK 21 -----------------------------------------------------------
set "JDK21="
if defined JAVA_HOME if exist "%JAVA_HOME%\bin\java.exe" (
  "%JAVA_HOME%\bin\java.exe" -version 2>&1 | findstr /r /c:"version \"21\." >nul && set "JDK21=%JAVA_HOME%"
)
if not defined JDK21 for /d %%d in ("C:\Program Files\Java\jdk-21*") do if not defined JDK21 if exist "%%~d\bin\java.exe" set "JDK21=%%~d"
if not defined JDK21 for /d %%d in ("C:\Program Files\Eclipse Adoptium\jdk-21*") do if not defined JDK21 if exist "%%~d\bin\java.exe" set "JDK21=%%~d"
if not defined JDK21 for /d %%d in ("C:\Program Files\Microsoft\jdk-21*") do if not defined JDK21 if exist "%%~d\bin\java.exe" set "JDK21=%%~d"
if not defined JDK21 for /d %%d in ("C:\Program Files\Zulu\zulu-21*") do if not defined JDK21 if exist "%%~d\bin\java.exe" set "JDK21=%%~d"
if defined JDK21 (
  set "JAVA_HOME=%JDK21%"
  set "PATH=%JDK21%\bin;%PATH%"
  echo [JDK]    %JDK21%
) else (
  echo [JDK]    JDK 21 not found locally - Gradle will provision a toolchain
)

rem ---- 2) Gradle -----------------------------------------------------------
set "GRADLE="
where gradle.bat >nul 2>nul && set "GRADLE=gradle.bat"
if not defined GRADLE where gradle >nul 2>nul && set "GRADLE=gradle"
if not defined GRADLE for /d %%d in ("%USERPROFILE%\.gradle\wrapper\dists\gradle-8.14.3-bin\*") do if not defined GRADLE if exist "%%~d\gradle-8.14.3\bin\gradle.bat" set "GRADLE=%%~d\gradle-8.14.3\bin\gradle.bat"
if not defined GRADLE for /d %%d in ("%USERPROFILE%\.gradle\wrapper\dists\gradle-8*-bin\*") do if not defined GRADLE for /d %%e in ("%%~d\gradle-8*") do if not defined GRADLE if exist "%%~e\bin\gradle.bat" set "GRADLE=%%~e\bin\gradle.bat"
if not defined GRADLE if exist "%PROJECT_DIR%\gradlew.bat" set "GRADLE=%PROJECT_DIR%\gradlew.bat"
if not defined GRADLE (
  echo [ERROR] No usable Gradle found.
  echo         Install Gradle, or restore gradle\wrapper\gradle-wrapper.jar.
  goto :done
)
echo [Gradle] %GRADLE%

rem ---- 3) tasks ------------------------------------------------------------
set "TASKS=build"
if not "%~1"=="" set "TASKS=%*"
if not "%~1"=="" echo %~1 | findstr /b /c:"-" >nul && set "TASKS=build %*"
echo [Tasks]  %TASKS%
echo.

rem ---- 4) run --------------------------------------------------------------
pushd "%PROJECT_DIR%"
call "%GRADLE%" %TASKS%
set "RC=%ERRORLEVEL%"
popd

echo.
if "%RC%"=="0" (
  echo [OK] BUILD SUCCESSFUL
  echo [Jar folder] %MODJAR_DIR%
  if exist "%MODJAR_DIR%\*.jar" dir /b /o-d "%MODJAR_DIR%\*.jar"
) else (
  echo [FAIL] Gradle exited with code %RC%
)

:done
if "%~1"=="" echo %CMDCMDLINE% | find /i "%~nx0" >nul && pause
endlocal & exit /b %RC%