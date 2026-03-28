@echo off
chcp 65001 >nul
set "MAVEN_URL=https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.zip"
set "MAVEN_ZIP=%TEMP%\maven_temp.zip"
set "MAVEN_DIR=%TEMP%\apache-maven-3.9.6"

echo =======================================================
echo Skill Swap Platform - Startup Script
echo =======================================================

if not exist "%MAVEN_DIR%\bin\mvn.cmd" (
    echo.
    echo [1/2] Missing tools detected. Downloading Maven...
    echo       Please wait, this will only happen once...
    powershell -NoProfile -Command "Invoke-WebRequest -Uri '%MAVEN_URL%' -OutFile '%MAVEN_ZIP%' -UseBasicParsing"
    echo       Extracting tools...
    powershell -NoProfile -Command "Expand-Archive -Path '%MAVEN_ZIP%' -DestinationPath '%TEMP%' -Force"
)

echo.
echo [2/2] Starting the Server!
echo       Make sure your MySQL database is turned ON.
echo       Ignore any warnings, just wait for "Started PlatformApplication"
echo.

REM We must use 'call' or else the batch script terminates early!
call "%MAVEN_DIR%\bin\mvn.cmd" spring-boot:run

echo.
echo =======================================================
echo Oops, the server stopped! Please check the text above for errors.
echo Take a screenshot of the RED or WHITE error message.
echo =======================================================
pause
