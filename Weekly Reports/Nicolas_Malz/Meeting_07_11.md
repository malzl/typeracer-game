# Was habe ich seit dem letzten Meeting gemacht (und wie lange habe ich gebraucht)?

Week 4/5 + final days before submission and after last tutor meeting:
- **Advanced feature:** Players can pick different difficulty modes in select or custom view which alter the sentence length, number of sentences and occurrence of special characters.
- Improved WPM calculation to remove 'spike' upon last character, especially in easy mode and single player.
- Added unit tests for text processing (TextUtil)
- Added them to CI
- Refactored controller classes (moved common methods to abstract for difficulty etc.)
- Added a little explosion sound effect to bomb to distract other player.
- **Advanced feature:** I revisited the TextUtil and now created a decorator pattern to do the different textprocessing steps allowing for flexible and modular composition of different text processing behaviors.
- **Advanced feature:** Added "Impossible" mode with special characters mixed into the words and "Math mode" with letters getting replaced by numbers (e.g., "4" instead of "A"). Works both in single and multiplayer.
- Unit tests for refined TextUtil and decorators, also added to CI
- Difficulty/Game modes are now organized in enum which now serves as 'config' for game modes and for better separation of concerns. TextUtil class was refactored for better overiew.
- Extensive documentation added to many classes where missing.
- Smoothed out WPM calculations for very short games. Wrote unit tests for those methods.
- Adjusted game hyperparameters for better experience.
- Added hover effects where missing.
- Added first version README.md
- **Advanced feature:** Player statistics in Singleplayer mode
  - Storing key statistics like mistakes and WPM flow (min, max, median, average) and number of mistakes in PlayerPerformance class instances.
  - Uses new PlayerPerformance class to track statistics in single player for a better practicing experience.
  - Player statistics are displayed upon finishing a game
  - Live mistake tracking in singleplayer
- Pair programing session with Alion Nurka on 07/07 to integrate further sound features/effects to lobby, buttons and messages.
- Already worked on checkStyle suggestions (from 150+ down to <50) to have less work down the road.
- Live mistake tracking in singleplayer (for practice)
- helped integrate new design/merging
- **Advanced feature:** Light and dark modes (Result of a very long pair programing session with Julia Feckl/Taoyi Luo):
  - New ThemeManager class as utility to globally manage dark mode for client/server app GUI
  - Integration of ThemeManager with GameManager
  - Integration of dark mode in runtime FXMLs e.g. in ChooseAvatarView
  - Refactored FXMLs to bind them to CSS styleClasses, creating a separate dark-mode.css and remade style.css completely
  - Extensive manual testing of GUI
- Implement checkstyleMain suggestions and highest priority/feasible spotbugs suggestions
- Small fixes to GUI, hyperparameters such as timeout duration, algorithm warmup period
- Removed 'javadocs' test from pipeline because checkstyle already checks for sufficient javadoc documentation in all public/protected methods, etc.
- Added working shell scripts for windows and unix systems to run clients/server as desired (runOnUnix.sh, runOnWindows.cmd). Cross tested compatibility on Windows VM and my own Mac.
- Detailed README.md instructions
- custom demo.cmd for final presentation on 15/07

Time invested so far in this iteration: 8 sessions à 4-6 hours plus cooperation and discussion of new ideas and how to implement them with Alion Nurka and Qingyun Bao. Pair programing with Alion Nurka on advanced sound effects (7/8th of July) (3+ h). Coordination of GUI with Julia Feckl/Taoyi Luo.

# Was hat gut geklappt?
- New features work and design now has a theme.
- CI and tests provided valuable feedback to improve some methods.
- Pair programing

# Wo gab es Probleme?
- I had to reverse a merge to main because I had not taken into consideration an edge case in the difficulty feature integration.
- Some spotbugs suggestions are impractical or do not make sense semantically such as setting certain attributes static.
- Final bugs took a long time and high amount of diligence to fix.

# Was werde ich bis zum nächsten Meeting machen?
- Alion prepared the JAR scripts, we need to integrate this into the README.md install guide once it's done
- Add intelliJ launch of applications to README
- See what spotbugs/checkstyle implementations could be useful; remove it from final pipeline.
- Statistics in custom text
- Prepare/help with final hand in of project (documentation, jars, style, pipeline)

# Was hat gut geklappt im Team? Oder gab es Probleme?
- Excellent collaboration with Alion/Julia while working together on these features which really required we communicate.
- Qingyun Bao quickly found bugs and fixed them, assisting when needed.
- Communication has reached an excellent level in the team throughout the last weeks.
- Great team effort in the final stages to thoroughly test the game, adding final touches/documentation and refactoring the code.
