$ErrorActionPreference = "Stop"
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $scriptDir

$localJdkRoot = Join-Path $scriptDir ".tools\jdk-25"
$localJdk = Get-ChildItem -Path $localJdkRoot -Directory -Filter "jdk-*" -ErrorAction SilentlyContinue |
Sort-Object Name -Descending |
Select-Object -First 1
if ($localJdk) {
    $env:JAVA_HOME = $localJdk.FullName
    $env:Path = "$($localJdk.FullName)\bin;$env:Path"
}

Write-Host "===================================================" -ForegroundColor Cyan
Write-Host "  Starting Hall Booking Spring Boot Backend...     " -ForegroundColor Cyan
Write-Host "===================================================" -ForegroundColor Cyan

if (Get-Command mvn -ErrorAction SilentlyContinue) {
    Write-Host "Using system Maven..." -ForegroundColor Green
    mvn spring-boot:run
    exit $LASTEXITCODE
}

$localMvn = Join-Path $scriptDir ".tools\apache-maven-3.9.9\bin\mvn.cmd"
if (Test-Path $localMvn) {
    Write-Host "Using bundled Maven ($localMvn)..." -ForegroundColor Green
    & $localMvn spring-boot:run
    exit $LASTEXITCODE
}

Write-Host "Maven not found. Running setup-maven.ps1..." -ForegroundColor Yellow
& (Join-Path $scriptDir "setup-maven.ps1")

if (Test-Path $localMvn) {
    & $localMvn spring-boot:run
}
else {
    Write-Error "Failed to locate Maven."
}
