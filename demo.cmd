@echo off
setlocal

rem Prompt for the number of clients
set /p Clients=Enter the number of clients (min 1 and max 4):

rem Validate input
if "%Clients%" lss "1" (
    echo Minimum number of clients is 1.
    goto end
)
if "%Clients%" gtr "4" (
    echo Maximum number of clients is 4.
    goto end
)

rem Launch the specified number of clients asynchronously
for /l %%i in (1,1,%Clients%) do (
    start "Client %%i" cmd /c java --module-path C:\Users\julia\IdeaProjects\a5-g16_final\javafx-sdk\javafx-sdk-17.0.11\lib --add-modules javafx.controls,javafx.fxml --add-exports javafx.graphics/com.sun.glass.utils=ALL-UNNAMED --add-exports javafx.base/com.sun.javafx=ALL-UNNAMED --add-exports javafx.graphics/com.sun.javafx.tk=ALL-UNNAMED -jar C:\Users\julia\IdeaProjects\a5-g16_final\JARs\GameApp\typewrite-game-1.0-SNAPSHOT-all.jar
)

rem Delay for a moment before launching the server (optional)
timeout /t 3 /nobreak >nul

rem Run the server if the number of clients is greater than 1
if "%Clients%" gtr "1" (
    start "Server" cmd /c java --module-path C:\Users\julia\IdeaProjects\a5-g16_final\javafx-sdk\javafx-sdk-17.0.11\lib --add-modules javafx.controls,javafx.fxml --add-exports javafx.graphics/com.sun.glass.utils=ALL-UNNAMED --add-exports javafx.base/com.sun.javafx=ALL-UNNAMED --add-exports javafx.graphics/com.sun.javafx.tk=ALL-UNNAMED -jar C:\Users\julia\IdeaProjects\a5-g16_final\JARs\ServerApp\typewrite-game-1.0-SNAPSHOT-server.jar
)

:end
endlocal
