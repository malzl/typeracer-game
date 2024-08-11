@echo off
setlocal

REM Define the root directory to search in
set "PROJECT_DIR=%cd%"

REM Attempt to locate the JavaFX SDK lib directory automatically within the project directory
echo Searching for JavaFX SDK directory within the project...
for /f "delims=" %%a in ('dir /b /s "%PROJECT_DIR%\javafx.base.jar"') do (
    set "JAVA_FX_SDK_PATH=%%~dpa"
    goto checkpath
)

:checkpath
REM Check if a possible SDK path was found
if not "%JAVA_FX_SDK_PATH%"=="" (
    echo Possible JavaFX SDK found at "%JAVA_FX_SDK_PATH%"
    set /p "response=Is this the correct JavaFX SDK path? (yes/no): "
    if /i not "%response%"=="yes" (
        set "JAVA_FX_SDK_PATH="
    )
)

REM If no path was found or the user rejected the found path, ask for it
if "%JAVA_FX_SDK_PATH%"=="" (
    set /p "JAVA_FX_SDK_PATH=Please enter the path to the JavaFX SDK lib directory: "
)

REM Check if the entered path is valid
if not exist "%JAVA_FX_SDK_PATH%" (
    echo The path provided is not a valid directory. Exiting.
    exit /b 1
)

REM Prompt the user to specify the number of instances of game.jar to run (between 1 and 4 as this is the game maximum)
set /p "NUM_INSTANCES=Please enter the number of game.jar instances to run (1-4): "

REM Check if the number of instances is valid
if "%NUM_INSTANCES%" LSS "1" (
    echo Invalid number of instances. Please enter a number between 1 and 4. Exiting.
    exit /b 1
)
if "%NUM_INSTANCES%" GTR "4" (
    echo Invalid number of instances. Please enter a number between 1 and 4. Exiting.
    exit /b 1
)

REM Create a log directory
mkdir logs 2>nul

REM Run the specified number of instances of the GameApp JAR
for /L %%i in (1,1,%NUM_INSTANCES%) do (
    start "GameApp Instance %%i" cmd /c java --module-path "%JAVA_FX_SDK_PATH%" --add-modules javafx.controls,javafx.fxml --add-exports javafx.graphics/com.sun.glass.utils=ALL-UNNAMED --add-exports javafx.base/com.sun.javafx=ALL-UNNAMED --add-exports javafx.graphics/com.sun.javafx.tk=ALL-UNNAMED -jar JARs/GameApp/typewrite-game-1.0-SNAPSHOT-all.jar > logs/gameapp_%%i.log 2>&1
    echo Started instance %%i of game.jar
)

REM Run the Server JAR
start "ServerApp" cmd /c java --module-path "%JAVA_FX_SDK_PATH%" --add-modules javafx.controls,javafx.fxml --add-exports javafx.graphics/com.sun.glass.utils=ALL-UNNAMED --add-exports javafx.base/com.sun.javafx=ALL-UNNAMED --add-exports javafx.graphics/com.sun.javafx.tk=ALL-UNNAMED -jar JARs/ServerApp/typewrite-game-1.0-SNAPSHOT-server.jar > logs/serverapp.log 2>&1
echo Started server.jar

endlocal
pause
