# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.1] 19-06-2024

### Added
- v1.1. Basic single player MVC game

### Changed
- Fundamental GUI for home screen, username input, text selection, and gameplay (Julia Feckl)
- Basic game model with random text retrieval and selection (Qingyun Bao, Nicolas Malz)
- Runnable game application (Qingyun Bao)
- Controllers for Game (Nicolas Malz, Qingyun Bao)
- Use the canvas feature to display text; if an error is inputted, the character turns red,
  and further input is prohibited.（Qingyun Bao）
- UI updates are implemented by inheriting the AnimationTimer class,
  and keyboard input and reading are achieved through the TextArea component.(Qingyun Bao)
- Gradle configuration with essential plugins (Nicolas Malz)
- Dummy multiplayer button (Julia Feckl)
- Resources for gameplay, such as texts (Qingyun Bao)
- Resources for GUI, such as images/icons (Julia Feckl)

### Removed
- old source skeleton

### Fixed

## [Unreleased] 21-06-2024

### Added
- Client Server skeleton with broadcast function (Alion Nurka, Nicolas Malz)
- Slightly enhanced GUI for home screen (Julia Feckl)
- Added a customizable text input as an advanced feature,
  allowing players to copy and paste text into a customizable window for gameplay selection(Qingyun Bao)

### Changed
- Server with basic broadcast functionality (Alion Nurka)
- Simple GUI for server (Alion Nurka)
- Client with basic connectivity to server (Nicolas Malz)
- Racecar animation in home screen (Julia Feckl)
- Client and Server applications for testing (Alion Nurka)

### Removed


### Fixed
- Fixed issues with article formatting and removed special characters. (Qingyun Bao)


## [1.2] 21-06-2024

### Added
- Fundamental communication logic including message interface and types for key events, namely: Join game handshake, player ready handshake, start game handshake (Nicolas Malz)
- Handling and sending of messages on server side; ConnectionManager (Nicolas Malz)
- Handling and sending of messages on client side (Nicolas Malz)
- Serialization and Deserialization through GoogleJson (gson) (Nicolas Malz)
- Mock lobby including mock lobby view (Alion Nurka)
- Home view -> Client view (Alion Nurka)
- Client view -> Lobby view -> Game view (Nicolas Malz)
- Basic implementation of server sending text for gameplay to clients (Nicolas Malz)
- Extensive logging to track client-server communication (Nicolas Malz)
- Fundamental implementation (+ mock data) for handling game progress updates in the game to later broadcast them via the server. (Nicolas Malz)
- GameState class to manage game instances on the server (Alion Nurka)
- Add Server termination functionality and handle it from Server Control GUI. (Alion Nurka)
- Add new ServerClosedMessage message type, notify clients about server termination, handle it from the GUI of the client. (Alion Nurka)


### Changed
- Updated, significantly more modular MVC pattern for multiplayer (Alion Nurka)
- Test applications for Lobby, GameApp (Alion Nurka)
- Essential refactoring (Alion Nurka)
- Necessary changes to build/module-info (Alion Nurka)
- Extensive logging to trace JavaFX errors, stage/scene/view management (Nicolas Malz)
- Simplified Client package structure (Alion Nurka)
- Enhanced Server application by adding "Port Number" Label and making the connect button clickable only if the field contains a valid port number. (Alion Nurka)

### Removed
- Single player functionality is not available as a result of adaptations for multiplayer. This will be added back at a later point.

### Fixed


## [2.0] 27-06-2024

### Added
- v2.0 Working multiplayer implementation through publish-subscribe design pattern [Reference 1](https://en.wikipedia.org/wiki/Publish–subscribe_pattern) [Reference 2](https://www.bilibili.com/video/BV1rx4y157s2/?spm_id_from=333.880.my_history.page.click&vd_source=e6caff7e24e2b8a2727efc7ec62031fc). DefaultEventBus is the event bus used for managing event registration and publication.(Qingyun Bao)
  - **UpdateEvent**: Handles player progress updates and sends them to the server. (Published by MultipleGamePlayController, subscribed by the client) (Qingyun Bao)
  - **SelectedEvent**: Handles game text selection and notifies the server. (Subscribed by the client) (Qingyun Bao)
  - **SyncEvent**: Used to display the received player information form the server on the mutiplayercontrolller interface. (Published by the client, subscribed by MultipleGamePlayController) (Qingyun Bao)
  - **StartGameEvent**: Used to synchronize game start information (Nicolas Malz).
  - **GameFinishedEvent**: Game end event, triggered when a player finishes the game, marking the game as finished. (Published by the client, subscribed by MultipleGamePlayController) (Qingyun Bao)
  - **MutipleSelectOrCustomController** : The player who join the Lobby fist has right to choose text or cuntom Input. Other players just wait at the MutipleSelectorCustom-view.(Qingyun Bao)
  - **MultipleGamePlayController**: This class mainly handles the multiplayer game interface and displays the status of different players. (Qingyun Bao)
- v2.0 Integrated the functionality of text selection and customized input from the single-player game into the multiplayer link section -> MultipleSelectOrCustomController. (Qingyun Bao)
- v2.0 Enhanced MultipleGamePlayController by setting up two components to separate the local typing text and the status display of different players. Used AnimationTimer to update the local typing gameplay part. The status display of different players is handled using UpdateEvent and SyncEvent. (Qingyun Bao，Julia Feckl)
- v2.0 Functioning server-side communication (based on v1.2) and broadcasts and additional message handling to complete functionality (Nicolas Malz, Alion Nurka)
- v2.0 Functioning client-side communication and advanced handling (based on v1.2), e.g. TextSelectedNotification.
- v2.0 Text selection and retrieval in multiplayer and singleplayer game, **advanced feature** (Nicolas Malz)
- V2.O **advanced feature** : Custom text input. In addition to novels, users can copy and paste their own text to use in the typing game. (Qingyun Bao)
- v2.0 The user that is ready to play in lobby is now displayed to other players (v1.2 had backend implementation only) (Nicolas Malz)
- v2.0 **advanced feature:** real-time player progress/wpm updating.The client sends data to the server 30 times per second, and The server will broadcast all players' information to each player type state at a frequency of 30 times per second.  (Qingyun Bao)
- v2.0 Server-connection: The usernames of players connecting to the server must be unique.(Qingyun Bao)
- v2.0 Advanced server and Player join request: the player can now input the server IPV4 address to connect the server. the game can be played in different laptops in the same Wifi. (Qingyun Bao)



### Changed
- Compared to v1.2, gameplay controller classes have been abstracted to bundle single and multiplayer, respectively. This marks a departure from the concrete implementation before. (Qingyun Bao, Nicolas Malz)
- Single player mode has been adjusted to the new, generics based controller architecture but works exactly like before on the front end. (Qingyun Bao, Nicolas Malz)
- Mock class for RankController (final rank view feature) (Julia Feckl)
- Rank view FXML (Julia Feckl)
- Enums to organize GameModel, extensive logging, and Messages (Qingyun Bao)
- Added new Message Type ServerClosedMessage (Alion Nurka)
- Advanced playerinfo class and connectManager class (Qingyun Bao)
- Advanced ConnectmanagerClass(Qingyun Bao)


### Removed
- Old (unstable) Single player implementation without generics.

### Fixed
- Mitigated an issue where different scenes could not be loaded smoothly. Now, scenes are loaded using the root method, combined with fxmlLoader.getController().initData() method. (Qingyun Bao, Nicolas Malz)

## [2.0.1] 28-06-2024

### Added
- v2.0.1 Basic unit tests for server, client and integration (Nicolas Malz)
- v2.0.1 Basic unit tests for DefaultEventBus (Nicolas Malz)

### Changed
- Minor changes to the server class to allow more effective testing and check for maximum number of players that can join. (Nicolas Malz)

### Fixed

### Removed

## [2.1] 28-06-2024

### Added
- v2.1 **Advanced feature:** Users can now select from three different themes of text: 
  - Novel excerpts from "War and Peace" in English, 
  - "Classic German Poems", 
  - and "Famous Song Lyrics in English" (Nicolas Malz)
- Added animations (Fade-In of Text, Hover-Effect of Buttons, Rotate-animation of pictures) in HomeView. (Julia Feckl)
- v2.1 Players are capable of choosing their ready state. (Alion Nurka)
- v2.1 Player ready label on the playerScrollPane is initially "Not Ready". (Alion Nurka)
- v2.1 New way of handling the ready button and player ready event, game stage is not switched, however current stage is updated. (Alion Nurka)
- v2.1 Players are able to change their ready state even if it is already choosen. (Alion Nurka)
- v2.1 Ready Buttons are updated to "Not Ready" when player is ready. (Alion Nurka)
- v2.1 Ready Buttons are updated to "Ready" again when players change their ready state to "Not Ready". (Alion Nurka)

### Changed
- Minor changes to the GameManager class to adjust GUI display to new texts.
- Players are initially not ready.
- Pressing ready will make the player read
- Changed the colors, buttons, and fonts to match the theme and style.
- PlayerReadyNotification is handled differently.
- PlayerReadyResponse triggers new actions instead of switching scenes.

### Fixed

### Removed
- Removed nameField and playerNameLabel FXML elements. (Alion Nurka)

## [2.1.1] 29-06-2024

### Added
- v2.1.1 Tests for GameManager, Javadocs for test (Nicolas Malz)
- v2.1.1 MessageBox class that extends from HBox. (Alion Nurka)
- v2.1.1 New name implementation of ClientBox and the way it is added to other FXML elements. (Alion Nurka)

### Changed
- CI Pipeline with automatic unit tests and quality tests (Nicolas Malz)
- Added some additional ClientIntegrationTest tests (Nicolas Malz)
- Altered ClientBox implementation. (Alion Nurka)

### Fixed
- Minor adjustments to unit tests; excludes ClientTest.java from pipeline because Docker container cannot properly run the JavaFX thread, local tests work however. (Nicolas Malz)
- Spotless style applied based on Google Java Codestyle (automatic)

### Removed
- ClientTest from CI due to problems with Docker launching the JavaFX application thread, apparently this is a common issue. Tests work normally if run locally.  (Nicolas Malz)

## [2.2] 30-06-2024

### Added
- v2.2 **Advanced Feature:** Background music and general sound implementation through AudiUtil class (Nicolas Malz)
  - Home screen with sound effect at the beginning and fading audio effect. (Nicolas Malz)
  - Button in home view to toggle audio. Methods allow for implementation of music once lobby/final rank view are finished; Music during gameplay is not planned for now. (Nicolas Malz)
  - AudioUtil and its static methods allow for a flexible implementation of future soundeffects and music anywhere in the game. (Nicolas Malz)
- v2.2 **Advanced Feature:** At the end of a multiplayer game, players can view their rankings, which include the winner stages from No.1-No.4, words per minute (WPM), and their usernames displayed alongside an icon. (Julia Feckl)
- v2.2 **Advanced Feature:** Fully-functioning chat on the lobby, player is capable of sending his own message and all players are able to receive the message of the player, as well as display it with the players name in front of the message. (Alion Nurka)
- v2.2 **Advanced Feature:** Traffic Light animation that indicates the start of the typing game, as well as additional synced sound effects for enhanced playing experience. (Alion Nurka)
- Added Turn on button that starts the traffic light animation, intended for demonstration and testing purposed, to be removed in the future. (Alion Nurka)
### Changed
- Minor adjustments to HomeController and GamePlayControllers to start/stop the music. (Nicolas Malz)
- AudioUtil class to handle everything sound-related using JavaFX Media and static methods. (Nicolas Malz)
- Changed the colors, buttons, and fonts to match the theme and style (Julia Feckl).

### Fixed
- Fixed one NullPointererror Before the players allowed to start the Game. Palyer information will only be updated at the Gamecontroller (Qingyun Bao)
### Removed

## [2.3] 2-07-2024
### Added
- v2.3  **Advanced Feature:**：Added a bomb feature, where players can click a bomb button on the game interface. An explosion animation will then appear on the screens of other players. SendEmojiEvent (published by MultiplayController, subscribed by Client): When the bomb button is pressed, the name of the player who pressed the button is sent to the client. The client immediately sends it to the server, and the server broadcasts it for processing.
  EmojiEvent (published by Client, subscribed by MultiplayController): Upon receiving this event, a bomb explosion GIF is loaded.(Qingyun Bao)
- v2.3 BOMB Animation gif and display(Julia Feckl, Qingyun Bao)

## [2.4] 03-07-2024

### Added
- JAR Server and GameApp executable files under directory: "./out/artifacts". (Alion Nurka)
- Added startConfig.sh Script that runs the Server Application as well as arbitrary number of GameApp applications prompted from console. (Alion Nurka)
- New flag for lead player on the lobby. (Alion Nurka)
- v2.4 **Advanced Feature:** Adds different difficulty modes in single and multiplayer. (Nicolas Malz)
  - Depending on the difficulty chosen by the player (easy, medium, hard) the game will vary the number of sentences and their length. (Nicolas Malz)
  - Hard mode does not remove some special characters making it even harder to type (e.g. * or #). (Nicolas Malz)
- V2.4 **Advanced Feature:** Adds a little sound effect to Qingyun Bao's Bomb feature. (Nicolas Malz)
- v2.4 **Advanced Feature:**: The bomb button can only be used every 5 seconds to avoid disrupting the normal gameplay.

### Changed
- Changed build.gradle configurations, "org.beryx.jlink" was included to plugins. (Alion Nurka)
- Changed ClientBox.java implementation, it requires an isAdmin boolean to display the additional flag for the lead player. (Alion Nurka)
- Messages can be sent via Enter button. (Alion Nurka)
- Unit tests for TextUtil. (Nicolas Malz)
- Unit tests for text processing are included in CI pipeline. (Nicolas Malz)

### Fixed
- Fixed chatBox extension bug on the lobby, whenever the number of messages is reached, chatbox is cleared to make room for newer ones. (Alion Nurka)
- Nullpointer exception that occurred in some calls of the static stopMusic method. (Nicolas Malz)

### Removed

## [2.5] 05-07-2024

### Added
- v2.5 **Advanced Feature:** Players can choose their avatar that will show on the lobby and multiplayer gamestage. (Alion Nurka)
- v2.5 Added new Scene [choose_avatar-view.fxml](..%2F..%2Fsrc%2Fmain%2Fresources%2Fcom%2Ftypewrite%2Fgame%2Fchoose_avatar-view.fxml) that is used to show the available avatars for selection. (Alion Nurka)
- v2.5 Added 12 available avatars. (Alion Nurka)
- v2.5 Added 3 new Controller Classes under "controller/multiple/avatar" package (Alion Nurka)
  - ChooseAvatarView
  - DefaultAvatarBox
  - ImageBox
- v2.5 Added hovering effect to the ImageBox on ChooseAvatar Stage (Alion Nurka)
- v2.5 Added fields on ClientView that save the selected avatar on the logic of the game. (Alion Nurka)
- v2.5 New Message Class AvatarSelected. 
- v2.5 **Advanced Feature:** Re-made the game text and mode creation logic (TextUtil class) using decorator pattern (package: util.textprocessors). This allows for very flexible addition of any future game mode. (Nicolas Malz)
- v2.5 **Advanced Feature:** New game modes "Impossible" and "Math mode" in single- and multiplayer (Nicolas Malz)
  - Impossible features special characters sprinkled across sentences to make typing more challenging. (Nicolas Malz)
  - Math mode replaces certain characters with numbers (max 1 per word for readability) like A with 4, creating a different game experience. (Nicolas Malz)
- v2.5 Added unit tests for the text processor decorators (also in CI pipeline) (Nicolas Malz).

### Changed
- Network tests had to be adjusted in order for the pipeline to be passed. Will be changed again. (Alion Nurka)
- PlayerJoinRequest and PlayerJoinResponse classes were changed. So were their corresponding handler methods. (Alion Nurka)
- Unit tests for TextUtil to reflect new decorator pattern use. (Nicolas Malz)

### Fixed
- Fixed the bug when all players are ready and can not switch to the select view. (Qingyun Bao)

### Removed

## [2.6] 07-07-2024

### Added
- v2.6 Added hovering sound effects on the following window interfaces: (Alion Nurka)
- - Home View
- - SingleInputName View
- - Client View
- - Lobby View
- - MultipleSelectOrCostum View
- - ChooseAvatar View (Alion Nurka)
- v2.6 **Advanced Feature:** Added Server messages. Players can be notified about the state of the game from the lobby chat. (Alion Nurka)
- Added ChatManager utility class that provides useful methods for the lobby controller. (Alion Nurka)
- Added sound resources for the chat messages. (Alion Nurka)
- Assigned each sound effect to its corresponding chat message. (Alion Nurka)
- Simple unit tests for WPM calculation in singleplayer. (Nicolas Malz)
- Refactored TextUtil class. (Nicolas Malz)
- Added dark mode for home view (Taoyi Luo)
- Created theme manager class for all other views (Taoyi Luo)
- Added README.md first version. (Nicolas Malz)
- v2.6 **Advanced feature:** In singleplayer mode only (for practicing), players can now see key statistics of their game. (Nicolas Malz) Namely
  - min and max WPM,
  - median WPM,
  - average WPM,
  - and number of mistakes. (Nicolas Malz) 
- Key statistics are stored in PlayerPerformance class objects. Can be used for other advanced features – Singleplayer only, for now. (Nicolas Malz)
- Introduced click sound effects to various elements across multiple window stages. (Alion Nurka, Nicolas Malz)
- Enhanced the home window stage with an extra button for managing sound effects. (Alion Nurka, Nicolas Malz)
- Enriched the lobby window stage with a pair of new buttons for controlling both music and sound effects. (Alion Nurka, Nicolas Malz)

### Changed
- Refactored lobby controller class. (Alion Nurka)
- Further smoothed WPM calculations in single player for better experience in short sentence games (especially easy mode). (Nicolas Malz)
- Restructured difficulty levels for different game modes in Enum, allowing for greater modularity and cohesion. Makes it easier to change hyperparameters and add even more game modes. (Nicolas Malz)
- Adjusted textUtil tests to reflect refactoring changes (difficulty Enum). (Nicolas Malz)
- Aligned all FXMLs to the redesign, changed colors, fonts, font-sizes, screen-sizes etc. (Julia Feckl)

### Fixed
- Adds hover effect on some buttons where it was missing. (Nicolas Malz)
- Minor issue with a non-responsive button in singleplayer. (Nicolas Malz)

### Removed

## [2.7] 09-07-2024

### Added
- Added selected avatar by player on their corresponding label on Game Rank. (Alion Nurka)
- v2.7 **Advanced feature:** Live mistake tracking in singleplayer mode with GUI and character differentiation. (Nicolas Malz)
- v2.7**Advanced Feature**：A feature to kick out players has been added: 
    If a player does not type anything within 10 seconds, they will be kicked out of the game!!!(with dark bildscreen and you will see the red **kick out** on the screen)
    Other players can continue playing as usual until the game ends.
    If all players do not type anything, the game will end early, and the rankings will be displayed! 
    During the last 10 seconds, the screen will display how many seconds remain before you will be kicked out.(Qingyun Bao)


### Changed
- Circle shape was changed into ImageView on the corresponding fxml resource of RankController.java for each player. (Alion Nurka)
- Improved the way messages appear where the least current ones are removed to make place for the new messages. (Alion Nurka)
- Exchanged and Aligned avatar pictures with the racing theme and ensured consistent placement across all screens (Julia Feckl).
- Added hover animations to all screens that previously did not include them (Julia Feckl).
- Incorporated a picture into the ranking view and applied an animation to it (Julia Feckl).
- Adjusted the end screen in Single Player Mode for better visual coherence (Julia Feckl).
- Standardized colors, font family, font size, and animations across all screens for a cohesive visual experience (Julia Feckl).

### Fixed
- Fixed performance issue on the lobby where javafx application thread was being blocked by intensive operations. (Alion Nurka) 
- Fixed chatbox bug where the box got extended when more than 10 messages were added on it. (Alion Nurka)
- 
### Removed
- Circle shape from player labels on resources/com/typewrite/game/rank-view.fxml (Alion Nurka)

## [2.8] 10-07-2024

### Added
- **Advanced feature:** Added dark mode to all screens (Taoyi Luo, Julia Feckl, Nicolas Malz) 
  - Integrated a ThemeManger uses static methods and attribute as global interface for light or dark mode state. (Taoyi Luo, Nicolas Malz)
  - ThemeManager works with GameManager's public methods (loadView) to automatically load dark/light mode views upon toggling it in home view. Default is light mode. (Nicolas Malz, Julia Feckl)
  - Major refactoring binds all FXMLs to respective CSS's styleClasses instead of hitherto used inline design approach. (Julia Feckl, Nicolas Malz)
  - Refined and optimized the existing style.css for light mode to enhance performance. (Julia Feckl, Taoyi Luo)
  - Additionally, created and implemented a dedicated dark-mode.css to achieve a visually appealing and functional dark mode. (Taoyi Luo, Julia Feckl)
  - Added new JAR executable files: (Alion Nurka)
  - - Server.jar
  - - Game.jar

### Changed
- Code is now Checkstyle compliant. (Nicolas Malz)
- Longer grace period for kickout after inactivity (30 seconds up from 10). (Qingyun Bao, Nicolas Malz)
- Shorter WPM calculation warmup period in single player improves WPM display experience for very short custom inputs. (Nicolas Malz)
- Better dark mode in LobbyView. (Julia Feckl)

### Fixed
- GUI disproportions in LobbyView chat. (Julia Feckl)
- GUI disproportions in multiplayer. (Julia Feckl)


## [2.9] 12-07-2024

### Added
- Added 'Back to Home' buttons in the ClientView and SinglePlayerUsernameInputView.(Julia Feckl)
- Corresponding JARs of ServerApp and GameApp under ./JARs directory. (Alion Nurka)
- Added new task on build.gradle to generate jar files. (Alion Nurka)
- Added runOnUnix.sh and runOnWindows.cmd bash/cmd scripts that automatically locate valid JavaFX SDK in project directory and run a given number of client apps and a server app. Based on Alion Nurka's previous config bash script. (Nicolas Malz)
- demo.cmd for final presentation. (Nicolas Malz)
- README.md with detailed instructions on requirements, setting up JavaFX SDK/Java, installing the game and running it. (Nicolas Malz, Alion Nurka)

### Changed
- Empty folder for custom JavaFX SDK provision
- gitignore to not include JavaFX SDKs and images as binary to prevent any file corruption. (Nicolas Malz)

### Fixed
- Chat box extension bug (Alion Nurka)
- Fixed dark mode in SinglePlayerUsernameInputView (Julia Feckl)
- Fixed size of lobby screen (Julia Feckl)
- Implemented some spotbugs suggestions for better synchronization, performance and general code practice. (Nicolas Malz)
- Final CI pipeline version. (Nicolas Malz) 

### Removed
- Unnecessary elements of traffic light from LobbyView. (Alion Nurka)
- startConfig.sh script

## [2.10] 13-07-2024

### Added
- An alert stage has been implemented to inform the player 
when a connection to the lobby cannot be established because their chosen name is already in use by another player. (Alion Nurka)
- Added functionality to terminate the client thread when the PlayerJoinResponse message indicates an unsuccessful operation. (Alion Nurka)

### Changed
- Updated JAR files to current version of the project. (Alion Nurka)
- Tests are now refactored and checkstyle compliant (Checkstyle main + test achieve 100%). (Nicolas Malz)
- Player username input can now be confirmed by typing 'ENTER' key and not just clicking the button. (Nicolas Malz)
- Change the refresh rate in 24 times per second ,which means that now the clent send the player type information 24 times persond and the server still broadcast all the playerinfos 30 times per second. (Qingyun Bao)

## Fixed
- Refactored AudioUtil and fix of a nullpointer exception for some audio effects. (Nicolas Malz)
- Host and port information in multiplayer is more readable and has its own styleclass in CSS. (Nicolas Malz)

### Removed
- Removed Server.jar file. (Alion Nurka)
- No longer used methods due to refactoring. (Nicolas Malz, Alion Nurka)