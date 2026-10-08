@echo off
setlocal
cd /d "%~dp0"
for /d %%D in (".tools\jdk-25\jdk-*") do (
    if exist "%%~fD\bin\java.exe" (
        set "JAVA_HOME=%%~fD"
        set "PATH=%%~fD\bin;%PATH%"
    )
)
echo ===================================================
echo   Starting Hall Booking Spring Boot Backend...
echo ===================================================

where mvn >nul 2>nul
if %ERRORLEVEL% EQU 0 (
    echo Using system Maven...
    mvn spring-boot:run
    goto :end
)

if exist ".tools\apache-maven-3.9.9\bin\mvn.cmd" (
    echo Using bundled Maven...
    call .tools\apache-maven-3.9.9\bin\mvn.cmd spring-boot:run
    goto :end
)

echo Maven not found. Initializing portable Maven...
powershell -ExecutionPolicy Bypass -File .\setup-maven.ps1
if exist ".tools\apache-maven-3.9.9\bin\mvn.cmd" (
    call .tools\apache-maven-3.9.9\bin\mvn.cmd spring-boot:run
) else (
    echo Failed to initialize Maven.
    pause
)

:end
endlocal
