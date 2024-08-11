# Was habe ich seit dem letzten Meeting gemacht (und wie lange habe ich gebraucht)?

- Worked on finding a solution on how to update the gui on runtime outside its FXML controller.
- **Advanced feature** Lobby chat
    - Implemented chat on the lobby. Arbitrary number of players are able to communicate with one another with following utility classes:
        -  MessageBox
- Implemented new Message Class for the chat.
- Added new chat message handling logic in our network.
- Added logging for debugging purposes on the lobby controller.
- Removed the nameField for the player, because it is not worth the time to add the optionality of name changing.
- Players are capable of choosing their ready state.
- Player ready state is continuously updated on the players pane via the provided class:
    - ClientBox
- Added a semaphore which should be toggled when all players are ready and game is about to start.
- Included music for the semaphore.
- Communicated with team members to let them know of the current state of the lobby, as well as discussed with them the future of the semaphore.
- Did bug checking on the chat.

**Time spent: 12 hours,**
- 7 hours invested on individual coding sessions,
- 2 on pair programming,
- 1 on bug checking,
- 1 on the communication with team members about planning and feature discussion,
- 1 on unofficial team meeting.

# Was hat gut geklappt?
- There are no merge conflicts, the member are more confident in using git.
- The communication between team members has improved.
- The new advanced features worked as expected.

# Wo gab es Probleme?
- No problems occurred this time.

# Was werde ich bis zum nächsten Meeting machen?
- Fix the chatBox bug.
- Start the semaphore after every player is ready and then switch scenes.
- Decide when to play the semaphore, probably after the text has been decided.

# Was hat gut geklappt im Team? Oder gab es Probleme?
- No problems.
- Commiting, pushing and merging went smoothly.
- Communication has improved, members have shown demos of their work to let ever member know.
- All members were present in the informal meeting.