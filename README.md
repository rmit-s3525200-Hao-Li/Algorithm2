## Background
Guess Who is a classic two player game. The game consists of a set of characters/persons1 with
various attributes, e.g., hair or eye colour. Each player initially choose a person from this set. The
aim of the game is each player takes turns to guess their opponent’s chosen person. Each turn,
a player can ask a question, such as, “does your person have green eyes?”, which the opponent
answers yes/no. From the answer, a player can eliminate the possible persons that their opponent
have. The game ends when a player make a correct guess of the opponent’s chosen person. See
https://en.wikipedia.org/wiki/Guess_Who%3F for more details.
Traditionally, Guess Who is played between human players. In this assignment, your group will
develop algorithms to automatically play Guess Who.

- RandomGuessPlayer.java 
Class implementing the random guessing player.

- BinSearchGuessPlayer.java Class 
implementing the binary search based guessing player.

## Guess structure
There are two types of guesses, one for asking if opponent’s chosen player has a certain attribute-value
pair, the other for asking if the chosen player is a certain person.
In the Guess class, there are three attributes, mType, mAttribute and mValue. When asking about
attribute-value pair, set mType to Attribute and mAttribute and mValue to the asked attribute
and value respectively. When asking about if the chosen player is someone, set mType to Person,
mAttribute to “” (empty string) and mValue to the person’s name. See the Guess class for more
details.
For a player, when the answer(Guess guess) method is called, they should return true if the
question/guess is true, and false otherwise. For the receiveAnswer(Guess guess, boolean answer)
method, the player that was asking the question is updated with the answer, and should also return
true if the game has finished (i.e., they guessed a person and the answer was true). For all other types
of guesses and questions, that player should return false. See Player interface and Guess Who class
for more details.

## Compiling and Executing
To compile the files, run the following command from the root directory (the directory that GuessWho.java
is in):
```
javac -cp .:jopt-simple-5.0.2.jar *.java
```
Note that for Windows machine, remember to replace ’:’ with ’;’ in the classpath.

To run the Guess Who framework:
```
java -cp .:jopt-simple-5.0.2.jar GuessWho [-l <game log file>] <game configuration
file> <chosen person file> <player 1 type> <player 2 type>
```

- game log file: name of the file to write the log of the game
- game configuration file: name of the file that contains the attributes, values and persons in the
Guess Who game.
- chosen person file: name of the file that specifies which person that each player have chosen.
- player 1 type: specifies which type of player to use for first player, one of [random — binary
— custom]. random is the random guessing player, binary is the binary-search based guessing
player, and custom is the customised guessing player.
- player 2 type: specifies which type of player to use for second player, one of [random — binary
— custom].
The jar file contains a library for reading command line options.

This specifies the following game:
- Three attributes, hairLength, glasses and eyeColour.
- attribute hairLength can take values {short, medium, long}.
- attribute glasses can take values {round, square, none}.
- attribute eyeColor can take values {black, brown, blue, green}.
- Three persons in this game:
	– Person “P1” has hairLength = long, glasses = round and eyeColour = brown.
	– Person “P2” has hairLength = short, glasses = round and eyeColour = black.
	– Person “P3” has hairLength = medium, glasses = none and eyeColour = blue.

## Chosen person file	
The names are separated by a space.
An example chosen person file is as follows:
```
P1 P3
```
This specifies that player 1 has chosen person P1, and player 2 has chosen person P3



