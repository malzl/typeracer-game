# Was habe ich seit dem letzten Meeting gemacht (und wie lange habe ich gebraucht)?

- JAR Server and GameApp executable files under directory: "./out/artifacts".
- Added startConfig.sh Script that runs the Server Application as well as arbitrary number of GameApp applications prompted from console.
- Changed build.gradle configurations, "org.beryx.jlink" was included to plugins.
- New flag for lead player on the lobby.
- Changed ClientBox.java implementation, it requires an isAdmin boolean to display the additional flag for the lead player.
- Made it possible for chat messages to be sent with Enter button.
- Fixed chatBox extension bug on the lobby, whenever the number of messages is reached, chatbox is cleared to make room for newer ones.

- **Advanced Feature** Avatar Selection
- Added new Scene [choose_avatar-view.fxml](..%2F..%2Fsrc%2Fmain%2Fresources%2Fcom%2Ftypewrite%2Fgame%2Fchoose_avatar-view.fxml) that is used to show the available avatars for selection.
- Added 3 new Controller Classes under "controller/multiple/avatar" package
    - ChooseAvatarView
    - DefaultAvatarBox
    - ImageBox
- Added hovering effect to the ImageBox on ChooseAvatar Stage
- Handled avatar logic on the network.
- Included avatar field on the List of PlayerInfo on the server.
- Changed the JoinRequestMessage, the selected avatar was included.
- Added fields on ClientView that save the selected avatar on the logic of the game.

- v2.6 Added hovering sound effects on the following window interfaces:
- - Home View
- - SingleInputName View
- - Client View
- - Lobby View
- - MultipleSelectOrCostum View
- - ChooseAvatar View

- v2.6 **Advanced Feature:** Added Server messages. Players can be notified about the state of the game from the lobby chat.
- Added ChatManager utility class.
- Handled server notifications with local cache of players.
- Pair programing session with Nicolas Malz on 07/07 to integrate further sound features/effects to lobby, buttons and messages.
- Pair programming: Added click sound effects to elements across different window stages.
- Pair programming: Added one additional button on home window stage to toggle sound effects.
- Pair programming: Added two additional buttons on lobby window stage to toggle music and sound effects.


**Time invested: 24 hours**
- 20 hours on individual coding sessions,
- 3 hours on pair programming with Nicolas Malz,
- 1 hour on the communication and collaboration with team members.

### 13.07.2024

- Added alert stage to inform the player when a connection to the lobby cannot be established because their chosen name is already in use by another player. 
- Added functionality to terminate the client thread when the PlayerJoinResponse message indicates an unsuccessful operation.

# Was hat gut geklappt?
- Consistent aesthetic across all scenes.
- Amount of advanced features enhance the overall experience of the player.

# Wo gab es Probleme?
- Decided to remove the semaphore animation due to my inability to fully and accurately implement it without issues within the group’s agreed deadline.
- Chat bugs still appear, the most plausible explanation could be an incorrect merge into the main branch.

# Was werde ich bis zum nächsten Meeting machen?
- Fix the chat bugs
- Add Jar Files for the ServerApp and GameApp
- Help Julia with the final presentation

# Was hat gut geklappt im Team? Oder gab es Probleme?
- Went well
- - We are well satisfied with the yielded result of the time and effort we invested on the project, both on the pleasing aesthetics and the advanced features it provides.

- Problems
- - There has been confusion over issue/task allocation.
