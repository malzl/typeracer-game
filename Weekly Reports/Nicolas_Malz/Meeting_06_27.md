# Was habe ich seit dem letzten Meeting gemacht (und wie lange habe ich gebraucht)?

Week 3:
- Assisted Bao Qingyun with the new architecture for the controllers concept
  - Contributed to those parts that interact with communication/network, such as StartGameEvent and interlinking with overall communication logic from previous work ('message')
  - Server broadcasting function/message (classes in 'server' directory)
  - Client handling broadcasts and messages (Client class)
  - Merged and prepared old single player (v1.1) and unstable multiplayer (v1.2) for the presentation on 24/06 in cooperation with Julia Feckl. 
  - Improved, generics based select book function ( _SelectOrCustom classes) connected to our previously used TextUtil class (enhanced) and Resource; View is based on our single player select or custom view
    - **My Advanced feature**: Select one of multiple books (as of now three novels freely available) of which random sentences are drawn, parsed and deployed in the game
  - Lobby now displays ready users to all users upon clicking 'ready'. Feature is also functional: Game only starts when 1 to maximum 4 players are ready.
  - Total: 4 sessions à 2-5 hours including calls/pair programing with Qingyun Bao, Julia Feckl + coordination/version control issues 3 h

# Was hat gut geklappt?
We have a working multiplayer game with some advanced features already implemented like selecting texts, custom text, real-time updates, animations, etc.

# Wo gab es Probleme?
Big problems with GitLab and version control when trying to merge the new working code to our work branch (intended for assembly of unstable versions of multiplayer).
Lead to a misunderstanding in the team and chaos with some branches/issues. We ended up assembling locally and pushing to a new branch. Some minor changes got lost in the way by Alion Nurka, however, compatibility with them is mostly ensured as they are about the lobby and lobby code has been isolated specifically for him because it is his advanced feature.

# Was werde ich bis zum nächsten Meeting machen?
Tasked with merging our newest version with main branch, updating logs, and new class diagram on Miro.
Write unit tests so we can add advanced features and test automatically rather than through manual testing, testing applications, and line coverage tests.
Add a different language text to text selection.
Think about other advanced features and if deemed feasible create issues and start implementing them.

# Was hat gut geklappt im Team? Oder gab es Probleme?
Version Control issues lead to misunderstandings on the progress of the project. One team member missed a meeting due to work, which added to the misunderstanding.
Otherwise, the project proceeds ahead of our schedule and we were very proud of our presentation on 24/06.