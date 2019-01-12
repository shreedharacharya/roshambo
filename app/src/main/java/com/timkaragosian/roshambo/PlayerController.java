package com.timkaragosian.roshambo;

import java.util.ArrayList;

public class PlayerController {
    //takes in user input handles accordingly

    public void playerThowsForResult(PlayGameActivity playGameActivity, String playerMove, String computerMove, Boolean canPlayerMakeLegalMove) {
        //Responses will be a string indicating what should happen
        if (canPlayerMakeLegalMove) {
            playGameActivity.setPlayerThrow(playerMove);
            playGameActivity.setComputerThrow(computerMove);

            if (playerMove.equals(computerMove)) {
                playGameActivity.roundIsDraw();
            } else if (playerMove.equals(Constants.ROCK) && computerMove.equals(Constants.SCISSORS) ||
                    playerMove.equals(Constants.PAPER) && computerMove.equals(Constants.ROCK) ||
                    playerMove.equals(Constants.SCISSORS) && computerMove.equals(Constants.PAPER)) {
                playGameActivity.playerWins();
            } else {
                playGameActivity.computerWins();
            }
        } else {
            playGameActivity.computerWins();
            playGameActivity.setPlayerThrow(Constants.ILLEGAL_MOVE_TOO_EARLY);
        }
    }
}
