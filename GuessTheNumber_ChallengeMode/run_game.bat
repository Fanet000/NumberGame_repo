@echo off
echo Starting Guess The Number Challenge Mode...
echo.

REM Compile the Java files
javac -source 1.8 -target 1.8 GuessTheNumberChallenge.java
if errorlevel 1 (
    echo Compilation failed!
    pause
    exit /b 1
)

REM Run the game
java GuessTheNumberChallenge
if errorlevel 1 (
    echo Game execution failed!
    pause
    exit /b 1
)

echo.
echo Press any key to exit...
pause > nul 