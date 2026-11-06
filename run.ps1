param([switch]$BuildOnly)

$ErrorActionPreference = 'Stop'
$projectRoot = $PSScriptRoot
$buildDirectory = Join-Path $projectRoot 'build'
$runtimeDirectory = Join-Path $projectRoot 'runtime'
$sourceDirectory = Join-Path $projectRoot 'src'

if ($env:JAVA_HOME) {
    $compiler = Join-Path $env:JAVA_HOME 'bin/javac.exe'
    $java = Join-Path $env:JAVA_HOME 'bin/java.exe'
} else {
    $compiler = (Get-Command javac -ErrorAction Stop).Source
    $java = (Get-Command java -ErrorAction Stop).Source
}

New-Item -ItemType Directory -Path $buildDirectory -Force | Out-Null
$sourceFiles = Get-ChildItem -LiteralPath $sourceDirectory -Filter '*.java'
& $compiler -encoding UTF-8 -d $buildDirectory $sourceFiles.FullName
if ($LASTEXITCODE -ne 0) {
    throw 'Compilation failed.'
}
if ($BuildOnly) {
    Write-Output 'Compilation succeeded.'
    return
}

New-Item -ItemType Directory -Path $runtimeDirectory -Force | Out-Null
Get-ChildItem -LiteralPath (Join-Path $projectRoot 'examples/accounts') -File |
    ForEach-Object {
        $destination = Join-Path $runtimeDirectory $_.Name
        if (-not (Test-Path -LiteralPath $destination)) {
            Copy-Item -LiteralPath $_.FullName -Destination $destination
        }
    }

Push-Location $runtimeDirectory
try {
    & $java -cp $buildDirectory Main
    if ($LASTEXITCODE -ne 0) {
        throw 'The application exited with an error.'
    }
} finally {
    Pop-Location
}
