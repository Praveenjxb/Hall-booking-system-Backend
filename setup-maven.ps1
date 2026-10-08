$ErrorActionPreference = "Stop"
$scriptDir = Split-Path -Parent $MyInvocation.MyCommand.Path
$toolsDir = Join-Path $scriptDir ".tools"
$mavenDir = Join-Path $toolsDir "apache-maven-3.9.9"
$mvnCmd = Join-Path $mavenDir "bin\mvn.cmd"

if (Test-Path $mvnCmd) {
    Write-Host "Portable Maven is already available at: $mvnCmd"
    & $mvnCmd -version
    exit 0
}

Write-Host "Setting up portable Maven..."
if (!(Test-Path $toolsDir)) {
    New-Item -ItemType Directory -Path $toolsDir | Out-Null
}

$zipPath = Join-Path $toolsDir "maven.zip"
$downloadUrl = "https://archive.apache.org/dist/maven/maven-3/3.9.9/binaries/apache-maven-3.9.9-bin.zip"

Write-Host "Downloading Maven from $downloadUrl ..."
[Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12
Invoke-WebRequest -Uri $downloadUrl -OutFile $zipPath -UseBasicParsing

Write-Host "Extracting Maven archive..."
Expand-Archive -Path $zipPath -DestinationPath $toolsDir -Force

if (Test-Path $zipPath) {
    Remove-Item -Path $zipPath -Force
}

if (Test-Path $mvnCmd) {
    Write-Host "Maven installed successfully!"
    & $mvnCmd -version
} else {
    Write-Error "Could not locate $mvnCmd after extraction."
}
