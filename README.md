# ROSHAMBO
A simple Roshambo Android App of Rock, Paper, Scissors where user is playing against the computer. This uses a clean MVP architecture so that utilizing the game state throughout and unit testing is simple and straightforward. This was created for educational purposes and should be used as a guide for others understanding the core concepts. There is no AI backing the computer's decisions at the time of writing this. 

Getting Started
======================
To use this application, clone this repsitory to a local directory and open and build using Android Studio.

How the Game Works
======================
The game operates with the standard rules of rock, paper, scissors (more information https://en.wikipedia.org/wiki/Rock%E2%80%93paper%E2%80%93scissors). The game begins with a countdown from 3 seconds where a player can choose to throw one of the three moves (rock, paper, or scissor) and the computer will throw a move. If the player thows too early or too late, it is an illegal throw, the computer does not throw a move, and the player loses.

The View in this case, are the activities and fragments in the code. It triggers the Presenter to handle the setting of the data in the Model which is our game state (RpsGame). This data is then passed into the View from the Presenter to show the current state of the game at any given time. This is particularly useful when the device switches to the landscape layout and the Activity is destroyed and recreated with the instance state saved and easily recreated.

License
======================
Copyright 2019 Tim Karagosian
