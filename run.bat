@echo off
if not exist "bin\com\leetcode\Main.class" (
    echo Binary classes not found. Running build first...
    call build.bat
    if %ERRORLEVEL% neq 0 exit /b %ERRORLEVEL%
)

java -cp bin com.leetcode.Main %*
