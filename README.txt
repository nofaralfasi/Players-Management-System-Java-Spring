Notes:
 - I attached a jar file (json-20211205.jar) which I have used im my project for JSON processing.
 - In the csv file, I only included the name of the player's team (I wasn't sure if all fields are required, so I assumed that the team's name is enough).
 - I considered creating classes for the player and the team, and using the Gson library to process the data, but since there were not much of a use for this kind of implementation, I have decided to use another way.
 - The websocket sends its message to http://localhost:port.

Improvements:
 - Consider using the Java 8 Streams API.
 - Adding unit tests.
 - Code refactor.