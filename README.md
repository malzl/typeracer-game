# Typeracer Game

A fast-paced typing game that challenges players to improve their typing speed and accuracy.

## Table of Contents

- [Installation](#installation)
- [Usage](#usage)
- [Features](#features)
- [Contributing](#contributing)
- [License](#license)
- [Contact](#contact)
- [Acknowledgements](#acknowledgements)

# Installation

1. Ensure Java 17 or higher is installed on your machine. Best performance is guaranteed with any stable Java 17 or 21 version.
2. Clone the repository:
   ```
   git clone https://gitlab2.cip.ifi.lmu.de/sosy-lab/sep-ss-2024/a5-g16.git
   ```
3. Navigate to the project directory:
   ```
   cd a5-g16
   ```
4. Ensure you're on the `main` branch:
   ```
   git checkout main
   ```
5. Ensure everything is up-to-date:
    ```
   git pull
   ```
6. Ensure latest stable **JavaFX SDK Version 17** is installed on your machine. Other JavaFX versions **will not work**. Make sure you **know the exact path** to the JavaFX SDK /lib in your system**. If yes, proceed to [Usage](#usage), **else** visit instructions on [setting up JavaFX SDK](#adding-a-suitable-javafx-sdk-directly-to-the-project)  before proceeding to Usage.

#### Adding a suitable JavaFX SDK directly to the project

For your convenience, if you do not have JavaFX SDK installed yet, add a compatible JavaFX SDK directly to this directory.


We suggest you download it from [here](https://gluonhq.com/products/javafx/).
- Please select the correct JavaFX 17.0.11 for your
    - operating system
    - and CPU architecture.
- Download, extract, and paste into the ```javafx-sdk/``` directory in this project.
- The integrated ```grep``` in ```runOnUnix.sh``` and ```runOnWindows.cmd```, respectively, will find your installation within the directory and take care of the rest!

_Note: These instructions are repeated in the ```javafx-sdk/ADD-JAVAFX-SDK-HERE.md``` directory file._

## Usage

### Using JARs on UNIX systems (Mac, Ubuntu, ...)

To start the Typeracer Game:

1. In your terminal, navigate to the project directory.
    ```cd path/to/the/project/directory```
2. Run the following command to ensure the bash script has the rights to be executed:
   ```
   chmod +x runOnUnix.sh
   ``` 
3. Run the script:
    ```./runOnUnix.sh```
4. The script will now search the project root directory for the JavaFX SDK. If not found, you will be prompted to **input your own JavaFX SDK /lib path**. Example: ```/Users/USERNAME/Downloads/javafx-sdk-17.0.11/lib```
5. Upon providing a valid filepath for the JavaFX SDK, the script will ask you to specify the number of clients (between 1 and 4). This is the number of instances that will be started simultaneously.
6. A server application instance is always started along side the client(s).
7. Multiplayer only: Specify a valid server port. The address for joining the server when hosted on local device will be ```localhost```.
8. Follow on-screen instructions to play the game!

### Using JARs on Windows systems

To start the Typeracer Game:

1. In your terminal, navigate to the project directory.
   ```%cd% path\to\the\project\directory```
2. Ensure the CMD script has the rights to be executed (file properties)
3. Run the script:
   ```runOnWindows.cmd```
4. Upon providing a valid filepath for the JavaFX SDK, the script will ask you to specify the number of clients (between 1 and 4). This is the number of instances that will be started simultaneously.
5. A server application instance is always started along side the client(s). 
6. Multiplayer only: Specify a valid server port. The address for joining the server when hosted on local device will be ```localhost```.If another laptop wants to connect to the server, please enter the server's ```IPv4 address``` in the join game interface.
7. Follow on-screen instructions to play the game!

### Alternatively using _Gradle_ with _IntelliJ_/Terminal on any platform:
1. Optional if permission denied: Run the following command to ensure the gradle wrapper script has the rights to be executed:
   ```
   chmod +x gradlew
   ``` 
2. Run GameApp.java
    1. within IDE, e.g. IntelliJ IDEA, the game launches and GUI provides all necessary guidance.
    2. or using ```./gradlew run``` (Singleplayer only)

   Note that Multiple instances of GameApp.java can be run at the same time through additional (default) run configurations.

3. If multiplayer mode is desired, in addition to GameApp.java, run ServerApp.java in IntelliJ
4. Enter your desired port number (4 digit number up to 65535) and start the server.If hosted on your local machine the address will be ```localhost```.If another laptop wants to connect to the server, please enter the server's ``` IPv4 address``` in the join game interface.
5. Start playing following the GUI instructions.

## Features

- Single player mode:
    - Practice your skills in a _Typeracer_ experience that closely mirrors the multiplayer.
    - Get insights into your typing skills through key statistics like mistakes committed or median WPM.
    - You select everything, from text and genre to difficulty.
- Multiplayer mode:
    - Connect to your own game server with custom port，please notice that the player should has different name,or it will be refused to join the server.
    - Select one out of 12 custom avatars.
    - Game lobby showing the present players and their status ('ready').
    - Chat with other players in the lobby.
    - Chat displays important server messages, e.g. players joining or game being about to start.
    - First player to join is the admin and can pick game text and difficulty for all players.
    - When you're ready, click the 'ready' button!
    - Real-time multiplayer typing races with other players (enabled by the server).
    - Clicking the bomb button will cause an explosion effect to appear on the other players' screens, with a 5-second cooldown for the bomb.
    - Kick-out：If a player does not type anything for 30 seconds, they will be kicked out of the game. The kicked-out player's game interface will be destroyed, and the other players' screens will also display the player's status as "kick out". If all players stop typing, the game will end.
    - Compare their words per minute (WPM) and game progress to yours in real time.
    - Final leaderboard with average words per minute (WPM) in multiplayer.
- In all game modes:
    - Real time, highly responsive typeracer gameplay experience and time tracking.
    - Navigate through menus/views intuitively.
    - Custom username.
    - Always see your current WPM count in real-time (24 FPS refresh rate, to be exact).
    - Background music and sound effects for buttons, bomb effect and many more. These can be turned off for your convenience. Music automatically fades away upon start of the game so as not to distract players. 
    - Select one of three text themes: Tolstoy's War and Peace (English), classic poems in German from Rilke to Goethe, and famous song lyrics in English from The Beatles, Oasis, and many more!
    - Alternatively write or paste your own text in the custom text box and play with that.
    - Select one of six classic/specialty difficulty modes:
      - Play longer games with longer, more complex sentences as you go from Easy to Medium and Hard modes.
      - Alternatively, opt for the adventurous 'impossible' mode with extra special characters, practice _leetspeak_ in the number-heavy math mode, or try yourself at reversed words in _reverse_ mode!
    - Colorful, uniform design theme with normal (light) and dark mode.
    - Lively animations that are synchronized with sound for an appealing audio-visual experience.
    - Shell scripts to auto-launch a set number of clients and server.

## Contributing

We welcome contributions to the Typeracer Game! Here's how you can help:

1. Fork the repository
2. Create a new branch (`git checkout -b feature/amazing-feature`)
3. Make your changes
4. Commit your changes (`git commit -m 'Add some amazing feature'`)
5. Push to the branch (`git push origin feature/amazing-feature`)
6. Open a pull request

## License

This project is licensed under the MIT License. 

The MIT License is a permissive license, you have the...
- **Freedom to use**: The software can be used for any purpose, including commercial applications.
- **Freedom to modify**: You are free to modify the source code in any way.
- **Freedom to redistribute**: Copies can be sold, given away, or included as part of a larger project that has a different license.
- **Freedom to distribute modified versions**: You can make changes to the software or incorporate it into your own projects and distribute the modified version.


## Contact

Qingyun Bao - bao@cip.ifi.lmu.de
Julia Feckl - feckl@cip.ifi.lmu.de
Taoyi Luo - luot@cip.ifi.lmu.de
Nicolas Malz - malz@cip.ifi.lmu.de
Alion Nurka - nurka@cip.ifi.lmu.de

Project Link: https://gitlab2.cip.ifi.lmu.de/sosy-lab/sep-ss-2024/a5-g16

_Order of contributors is alphabetic and does not imply amount of contribution._

## Acknowledgements

- All contributors who have helped this project grow
- Our tutor Michael for his valuable advice and guidance throughout the project
- SoSy Lab team for their teaching and organizing this software practical
