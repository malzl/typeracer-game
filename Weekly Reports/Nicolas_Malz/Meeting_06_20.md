# Was habe ich seit dem letzten Meeting gemacht (und wie lange habe ich gebraucht)?
*Summarizes work from first two weeks*

Week 1 (Basic single player MVC)
- Set up basic project with gradle, checkstyle, spotbugs, spotless style, module-info
- Set up a Miro Board (online whiteboard) for everyone to put their todo-lists, plan, class diagrams, sequence diagrams, mockups etc. Board is kept up to date by all of us depending on responsibilities weekly.
- Built a first working version of the game in single player with working random text retrieval and game logic using MVC architecture. Pair Programing mostly with Qingyun Bao and GUI from Julia Feckl. (Week 1) Total time: Three sessions à 3-5 hours with pair programing. Note that the very first prototype was done locally mostly, then pushed as v1.1
  - Primarily involved in GamePlayView and GameManager classes (MVC)
  - Some involvement in textUtil for randomized text-retrieval

Week 2 (Basic Client Server; advanced communication)
- Based on Alion Nurka's foundations (Client, ClientView, ServerView, Server, basic applications, lobby, etc.) I added Json serialization classes (JsonConverter, MessageTypeAdapter) for messages
- Added and specified different message types (Message interface, PlayerJoinRequest, etc.) and integrated them with serialization
- Added and integrated client handling (Client) and sending of these messages*
- Added and integrated server handling and sending of these messages (Server, Connection Manager, GameState)*
- Extensive logging to trace issues.
- Calls/Meetup with the team (virtually) to discuss future implementation of multiplayer and prepare weekly tutor meeting
- Connected overall communication with controllers (ClientView, HomeView, GameView, etc.) such that a full game flow could be achieved and a basic text sent out to multiple clients
- Start game on all players ready in the lobby functionality (basic in backend only, to be extended and better integrated with view) through PlayerReady notifications/requests-
- Total time: 4 sessions à 3-5 hours including communication and version control overhead

*partly in cooperation/coordination with Alion Nurka in pair programing

# Was hat gut geklappt?
First version of the game worked quite well, game flow is at a point where there is a clear 'Absprungpunkt' to the multiplayer game.
This starting point can be used next week to build the actual multiplayer implementation, although some changes to the controller structure will be needed.

# Wo gab es Probleme?
Getting acquainted with GitLab took a while and we had trouble knowing 'where to start' so we did most things locally and together before pushing the first version. We then got more comfortable with the issues, branching, and merging and are using that since the 2nd week all the time.
Communication in a team of 5 is not always easy and working in parallel has proven very difficult for the foundations which is why we will work more sequentially and with larger commits until a first working version is done.
Learning: Small changes at the beginning have big consequences.

# Was werde ich bis zum nächsten Meeting machen?
Help finish multiplayer together with a new, and better architecture as we now have the problem that we use completely separate controllers for single and multiplayer. This could be abstracted.
Think about tests other than manual testing and line coverage.

# Was hat gut geklappt im Team? Oder gab es Probleme?
Cooperation with Qingyun Bao and Alion Nurka in particular has worked very well for pair programing.
Great GUI ideas and implementation from Taoyi Luo and Julia Feckl which we could immediately work with.
Teamwork and communication have worked well so far. Our virtual meet ups are complemented by in person meetups prior to the tutor meeting to prepare.