# Dining Hall Rota Scheduler

A Java Swing desktop project that turns tutors' weekly availability into a dining-hall supervision rota. Tutors record availability for breakfast, lunch, and supper slots. An administrator generates a schedule, reviews assignments, and exports the current week's rota as an HTML table.

## Features

- Separate tutor and administrator screens.
- Weekly availability entry and saved schedule views.
- An assignment algorithm that considers availability and assignment counts.
- Administrator editing of the generated rota.
- Local text-file storage and an HTML export.

The project uses Java's standard library, including Swing, collections, file I/O, and the date/time API.

## Run on Windows

Install a JDK and make `java` and `javac` available in your terminal, or set `JAVA_HOME` to the JDK installation directory. The source compiles with JDK 27.

From the repository folder in PowerShell:

```powershell
.\run.ps1
```

To compile without opening the application:

```powershell
.\run.ps1 -BuildOnly
```

If your PowerShell execution policy blocks local scripts, run the following equivalent commands from the repository root:

```powershell
New-Item -ItemType Directory -Force build, runtime
javac -encoding UTF-8 -d build (Get-ChildItem src -Filter '*.java').FullName
Get-ChildItem examples/accounts -File | ForEach-Object {
    $destination = Join-Path runtime $_.Name
    if (-not (Test-Path -LiteralPath $destination)) {
        Copy-Item -LiteralPath $_.FullName -Destination $destination
    }
}
Set-Location runtime
java -cp ../build Main
```

Both methods copy demo accounts only when the destination files are absent, preserving accounts from previous local runs.

## Try the demo

The included accounts use fictitious addresses reserved for examples.

| Role | Email | Demo password |
| --- | --- | --- |
| Tutor | `student@example.invalid` | `demo-student-only` |
| Administrator | `admin@example.invalid` | `demo-admin-only` |

Sign in as the tutor using the sample account above, choose a week, and save availability. Then sign in as the administrator to generate and view the current week's rota. The administrator can export that week's completed schedule to HTML through **Output schedule**.

The original school prototype stores account details in local plaintext files. Use these sample accounts for demonstrations. Scheduling uses a heuristic, so administrators should review assignments and unfilled slots.

## Project layout

- `src/` contains the Java source.
- `examples/accounts/` contains fictitious starting accounts.
- `documentation/` contains the original planning, design, development, and evaluation documents.
- `build/` contains compiled classes created locally.
- `runtime/` contains local account records and generated weekly schedules.

The last two folders are excluded from Git. Original school documentation describes the earlier submission structure. The current source and run instructions are here in the README.
