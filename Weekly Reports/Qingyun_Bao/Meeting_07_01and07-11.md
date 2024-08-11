# Was habe ich seit dem letzten Meeting gemacht (und wie lange habe ich gebraucht)?

Week 4:
- **Advanced Feature:**：Added a bomb feature, where players can click a bomb button on the game interface. An explosion animation will then appear on the screens of other players. SendEmojiEvent (published by MultiplayController, subscribed by Client): When the bomb button is pressed, the name of the player who pressed the button is sent to the client. The client immediately sends it to the server, and the server broadcasts it for processing.
EmojiEvent (published by Client, subscribed by MultiplayController): Upon receiving this event, a bomb explosion GIF is loaded.

- Fix one nullpointer error when the players reload the game.

Time invested so far in this iteration: 3 sessions à 4 hours as coming up with BOMB ISSUE and explore animation

Week 5:
- **Advanced Feature**：A feature to kick out players has been added: If a player does not type anything within 10 seconds, they will be kicked out of the game!!! 
Other players can continue playing as usual until the game ends. If all players do not type anything, the game will end early, and the rankings will be displayed!
- Completed the initial content of the README document with teammates!! Fixed some code conflicts.
- An executable JAR file that allows the game to run directly!
- remove duplicate methods
- run checkstyle adn fix any issues that arise
- finish the final Readme
# Was hat gut geklappt?
- GitLab issues and branching problems were solved, base version of game is now ready on main (v2.0) so everyone can add features to the stable release


# Wo gab es Probleme?


# Was werde ich bis zum nächsten Meeting machen?



# Was hat gut geklappt im Team? Oder gab es Probleme?
- Current release allows for team members to work more independently which was very tricky in the multiplayer integration phase.
