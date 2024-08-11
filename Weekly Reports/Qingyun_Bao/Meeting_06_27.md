# Was habe ich seit dem letzten Meeting gemacht (und wie lange habe ich gebraucht)?
  What did you do since the last meeting? How long did that take? 
  
- I used the publish-subscribe design pattern. DefaultEventBus is the event bus, used for setting up event registrations and managing event publications.
(cooperate with Nicolas)
- UpdateEvent: Handles player progress updates and sends them to the server. (Published by MultipleGamePlayController, subscribed by clients) 

- SelectedEvent: Handles game text selection and notifies the server. (Subscribed by clients) 

- SyncEvent: Used to display the received player information form the server on the mutiplayercontrolller interface. (Published by clients, subscribed by MultipleGamePlayController) 

- StartGameEvent: Used to synchronize information at the start of the game.

- GameFinishEvent: A game-ending event that concludes the game when a player finishes. (Published by clients, subscribed by MultipleGamePlayController) 

- Finish MultipleGamePlayController (This class mainly handles the multiplayer game interface and displays the status of different players.) (With JULIA)
- Finish the basic Rankcontroller but without Ranking 

- Moved single-player game features like text selection and customized input to the multiplayer section -> MultipleSelectOrCustomController 
GameInfo is used to store player information, such as names, current typing progress, and time.

- Added a GameState class, which stores and manages information about different players.

- Modified the server's code, setting GameState as a member variable of the server, which now holds the latest player information.
- MutipleSelectOrCustomController : The player who join the Lobby fist has right to choose text or cuntom Input. Other players just wait at the MutipleSelectorCustom-view.(Qingyun Bao)

- Advanced feature: real time update (WPM and The game progress of other players.)
- Advanced feature: custom text input
- Player join request: the player can now input the server IPV4 address to connect the server. 

# Was hat gut geklappt?
The design pattern is essentially complete, and now the controller class can interact well with the client. 
To reduce server load, a timed broadcasting feature has been implemented.

# Wo gab es Probleme?
There aren't particularly good tutorials online on how to use Git to manage code. 
Handling code conflicts is also a problem.
Regarding design patterns, using Spring Boot would make things much easier.
However, there are tutorial videos and code references available on the Chinese internet.

# Was werde ich bis zum nächsten Meeting machen?
Considering adding power-up features or options for background music on the game page. To avoid code conflicts,
i will wait until the ranking feature is completed before continuing work.

# Was hat gut geklappt im Team? Oder gab es Probleme?
Version Control issues lead to misunderstandings on the progress of the project.
One team member missed a meeting due to work, which added to the misunderstanding.