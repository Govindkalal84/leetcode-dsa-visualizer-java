@echo off
echo ===================================================
echo   Compiling LeetCode DSA Visualizer (Java Engine)
echo ===================================================

if not exist "bin" mkdir bin

dir /s /b src\*.java > sources.txt
javac -d bin -encoding UTF-8 @sources.txt
del sources.txt

if %ERRORLEVEL% equ 0 (
    echo [SUCCESS] Compilation finished successfully! Class files written to bin\
) else (
    echo [ERROR] Compilation failed with error code %ERRORLEVEL%
    exit /b %ERRORLEVEL%
)
