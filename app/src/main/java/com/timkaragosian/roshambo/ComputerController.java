package com.timkaragosian.roshambo;

import java.util.ArrayList;
import java.util.Random;

public class ComputerController {
    private ArrayList<String> playerMoves = new ArrayList<>(); //this will hold the player's moves to analyize

    //takes input from view and returns the move that the computer will do

    //give it some AI to handle user interactions, evaluates up to the last 5 inputs to make a guess, if less than 2 inputs, pick opposite last opponent's pick, if first pick, random

    public String computerMakesMove() {
        Random random = new Random();
        int selection = random.nextInt(2);

        switch (selection) {
            case 0:
                return Constants.ROCK;
            case 1:
                return Constants.PAPER;
            case 2:
                return Constants.SCISSORS;
            default:
                return null;
        }
    }
}
