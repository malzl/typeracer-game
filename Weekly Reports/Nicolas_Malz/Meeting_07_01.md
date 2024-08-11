# Was habe ich seit dem letzten Meeting gemacht (und wie lange habe ich gebraucht)?

Week 4:
*General remark: Time between last meeting and this one was only one weekend so less progress was made.*
- **Expanded my advanced feature**, which is being able to select custom text of which random sentences are drawn, by adding three different theme texts namely excerpts from War and Peace, a range of famous German poems in public domain as well as famous song lyrics.
- Created basic unit tests for Client, Server, publish subscribe pattern, GameManager and their integration using JUnit/Mockito/TestFX. Classes are all in the Test Sources Root directory.
- Based on the tests, some adjustments were made to Client/Server classes, like improved exception handling in Client.
- Set up CI pipeline for assembly, unit tests, and quality checks (spotBugs, Javadoc, checkstyle, spotless)
- **New advanced feature** background music through AudioUtil class.

Time invested so far in this iteration: 3 sessions à 3-6 hours as coming up with tests and struggling with testing GUI/JavaFX components took a long time and a lot of lost attempts.

# Was hat gut geklappt?
- GitLab issues and branching problems were solved, base version of game is now ready on main (v2.0) so everyone can add features to the stable release
- Basic unit tests based on our stable release and general suggestions (from lecture "Formale Spezifikation und Verifikation") work and pointed out some minor issues we still had.
- CI pipeline is in place and can help others while developing their advanced features
- Implementing background music was fun

# Wo gab es Probleme?
- Unit testing with JavaFX has proven very hard. Testing with TestFX did not work in Docker container due to a problem they seem to have with initializing the JavaFX application thread.
- We opted for Singletons in most classes which has its own consequences for Testing and limits the use of mockups. Maybe we can still change this in some classes?
- We still need to make the windows of the GUI resizable

# Was werde ich bis zum nächsten Meeting machen?
- Help out with advanced features where needed
- Add more documentation and if useful new tests
- ...

# Was hat gut geklappt im Team? Oder gab es Probleme?
- We talked about our misunderstanding last week and, in my view, resolved it well and will separate issues better from now on.
- Current release allows for team members to work more independently which was very tricky in the multiplayer integration phase.
- Team mates are not afraid to ask for help when they struggle with something.