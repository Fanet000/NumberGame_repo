@echo off
echo ====================================
echo    Guess The Number Challenge Mode
echo ====================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo Error: Java is not installed or not found in PATH
    echo Please install Java Runtime Environment (JRE) to run this game
    echo You can download Java from: https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)

REM Run the game
echo Starting the game...
echo.
java -jar GuessTheNumberGame.jar

REM If the game exits with an error
if errorlevel 1 (
    echo.
    echo Error: The game encountered an error while running
    pause
    exit /b 1
)

pause 