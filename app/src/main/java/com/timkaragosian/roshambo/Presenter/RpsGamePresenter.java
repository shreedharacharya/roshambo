package com.timkaragosian.roshambo.Presenter;

import android.os.Handler;

import com.timkaragosian.roshambo.Model.RpsGame;
import com.timkaragosian.roshambo.R;

import java.util.Random;

/**
 * Class that prepares all the data to be displayed in the view from user interaction in the View (PlayGameActivity)
 * <p>
 * Uses RpsGame as the basis for all data management (the Model)
 * <p>
 * All game logic is contained here
 */
public class RpsGamePresenter {
    private static final String GET_READY_MGS = "GET READY!";
    private static final String THROW_NOW_MSG = "THROW NOW!";

    public static final int EMPTY_THROW_VALUE = 0;
    public static final int ROCK_THROW_VALUE = 1;
    public static final int PAPER_THROW_VALUE = 2;
    public static final int SCISSORS_THROW_VALUE = 3;
    public static final int ILLEGAL_THROW_TOO_EARLY_VALUE = 4;
    public static final int ILLEGAL_THROW_TOO_LATE_VALUE = 5;

    public final static String ROCK_THROW_DISPLAY_DESCRIPTION = "Rock";
    public final static String PAPER_THROW_DISPLAY_DESCRIPTION = "Paper";
    public final static String SCISSORS_THROW_DISPLAY_DESCRIPTION = "Scissors";
    public final static String ILLEGAL_MOVE_NONE_THROWN_DISPLAY_DESCRIPTION = "Illegal Move! No Move Thrown!";
    public final static String ILLEGAL_MOVE_TOO_EARLY_DISPLAY_DESCRIPTION = "Illegal Move! Thrown Too Early!";

    private static final int ROCK_THROW_IMAGE_RESOURCE_ID = R.drawable.rock_image;
    private static final int PAPER_THROW_IMAGE_RESOURCE_ID = R.drawable.paper;
    private static final int SCISSORS_THROW_IMAGE_RESOURCE_ID = R.drawable.scissors;
    private static final int ILLEGAL_THROW_IMAGE_RESOURCE_ID = R.drawable.illegal_move;

    RpsGame mRpsGame = new RpsGame();
    OnGameStateChangedListener mOnGameStateChangedListener;

    public Handler mCountdownHandler = new Handler();
    public Handler mThrowNowHandler = new Handler();

    public RpsGamePresenter(OnGameStateChangedListener listener) {
        this.setOnGameStateChangedListener(listener);
    }

    //sets object from save state and updates view
    public void setGameState(RpsGame rpsGame) {
        this.mRpsGame = rpsGame;
        notifyObservers();
    }

    public RpsGame getGameState() {
        return mRpsGame;
    }

    //prepares the game on the start of a new round and starts timer
    public void handleRoundCountdown(int seconds) {
        //start timer
        mRpsGame.setCountDownDisplayValue(GET_READY_MGS);
        mRpsGame.setPlayerThrowValue(EMPTY_THROW_VALUE);
        mRpsGame.setComputerThrowValue(EMPTY_THROW_VALUE);
        mRpsGame.setIsGamePhaseCountDown(true);
        mRpsGame.setHasPlayerThrownThisRound(false);
        mRpsGame.setCountDownSeconds(seconds);
        mRpsGame.setCanPlayerMakeLegalMove(false);

        startCountdownHandler(seconds);

        notifyObservers();
    }

    //sets the player throw, descriptions, gets computer move, and determines winner before updating the view
    public void setPlayerThrow(int throwValue) {
        if (!mRpsGame.getCanPlayerMakeLegalMove()) {
            throwValue = ILLEGAL_THROW_TOO_EARLY_VALUE;
        } else {
            computerThrowsMove();
        }

        mCountdownHandler.removeCallbacksAndMessages(null);

        mRpsGame.setPlayerThrowImage(getImageResourceIdFromThrowValue(throwValue));
        mRpsGame.setPlayerMoveDescription(getThrowDisplayDescriptionFromThrowValue(throwValue));
        mRpsGame.setPlayerThrowValue(throwValue);

        mRpsGame.setHasPlayerThrownThisRound(true);
        mRpsGame.setIsGamePhaseRoundComplete(true);
        mRpsGame.setIsGamePhaseCountDown(false);
        mRpsGame.setIsCountdownRunning(false);
        mRpsGame.setCanPlayerMakeLegalMove(false);

        determineWinner();

        notifyObservers();
    }

    //compares throw values and determines winner of round
    private void determineWinner() {
        if (mRpsGame.getPlayerThrowValue() == mRpsGame.getComputerThrowValue()) {
            mRpsGame.setHasPlayerWon(false);
            mRpsGame.setHasComputerWon(false);
        } else if ((mRpsGame.getPlayerThrowValue() == ROCK_THROW_VALUE && mRpsGame.getComputerThrowValue() == SCISSORS_THROW_VALUE) ||
                (mRpsGame.getPlayerThrowValue() == PAPER_THROW_VALUE && mRpsGame.getComputerThrowValue() == ROCK_THROW_VALUE) ||
                (mRpsGame.getPlayerThrowValue() == SCISSORS_THROW_VALUE && mRpsGame.getComputerThrowValue() == PAPER_THROW_VALUE)) {
            mRpsGame.setHasPlayerWon(true);
            mRpsGame.setHasComputerWon(false);
            mRpsGame.setPlayerScore(mRpsGame.getPlayerScore() + 1);
        } else {
            mRpsGame.setHasPlayerWon(false);
            mRpsGame.setHasComputerWon(true);
            mRpsGame.setComputerScore(mRpsGame.getComputerScore() + 1);
        }
    }

    //determines what move computer will make and sets to object
    public void computerThrowsMove() {
        Random random = new Random();
        int selection = random.nextInt(3);

        switch (selection) {
            case 0:
                mRpsGame.setComputerThrowValue(ROCK_THROW_VALUE);
                break;
            case 1:
                mRpsGame.setComputerThrowValue(PAPER_THROW_VALUE);
                break;
            case 2:
                mRpsGame.setComputerThrowValue(SCISSORS_THROW_VALUE);
                break;
            default:
                mRpsGame.setComputerThrowValue(ROCK_THROW_VALUE);
        }

        mRpsGame.setComputerThrowImage(getImageResourceIdFromThrowValue(mRpsGame.getComputerThrowValue()));
        mRpsGame.setComputerMoveDescription(getThrowDisplayDescriptionFromThrowValue(mRpsGame.getComputerThrowValue()));
    }

    //determines what image resource id to use from throw value
    private int getImageResourceIdFromThrowValue(int throwValue) {
        switch (throwValue) {
            case ROCK_THROW_VALUE:
                return ROCK_THROW_IMAGE_RESOURCE_ID;
            case PAPER_THROW_VALUE:
                return PAPER_THROW_IMAGE_RESOURCE_ID;
            case SCISSORS_THROW_VALUE:
                return SCISSORS_THROW_IMAGE_RESOURCE_ID;
            case ILLEGAL_THROW_TOO_EARLY_VALUE:
            case ILLEGAL_THROW_TOO_LATE_VALUE:
            default:
                return ILLEGAL_THROW_IMAGE_RESOURCE_ID;
        }
    }

    //determines what string description to show from throw value
    private String getThrowDisplayDescriptionFromThrowValue(int throwValue) {
        switch (throwValue) {
            case ROCK_THROW_VALUE:
                return ROCK_THROW_DISPLAY_DESCRIPTION;
            case PAPER_THROW_VALUE:
                return PAPER_THROW_DISPLAY_DESCRIPTION;
            case SCISSORS_THROW_VALUE:
                return SCISSORS_THROW_DISPLAY_DESCRIPTION;
            case ILLEGAL_THROW_TOO_EARLY_VALUE:
                return ILLEGAL_MOVE_TOO_EARLY_DISPLAY_DESCRIPTION;
            case ILLEGAL_THROW_TOO_LATE_VALUE:
            default:
                return ILLEGAL_MOVE_NONE_THROWN_DISPLAY_DESCRIPTION;
        }
    }

    //way of updating view as long as listener is set
    private void notifyObservers() {
        if (this.mOnGameStateChangedListener != null) {
            mOnGameStateChangedListener.onGameStateChanged(mRpsGame);
        }
    }

    public void setOnGameStateChangedListener(OnGameStateChangedListener listener) {
        this.mOnGameStateChangedListener = listener;
    }

    //setup interface to handle when game state changes
    public interface OnGameStateChangedListener {
        void onGameStateChanged(RpsGame rpsGame);
    }

    //Starts timer to display a countdown
    public void startCountdownHandler(final int seconds) {
        setupCountdownHandler(seconds);

        mCountdownHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRpsGame.setCountDownDisplayValue(String.valueOf(seconds));

                if (seconds > 0) {
                    mRpsGame.setCountDownSeconds(seconds - 1);
                    startCountdownHandler(mRpsGame.getCountDownSeconds());
                } else {
                    startThrowNowHandler();
                }

                notifyObservers();
            }
        }, 1000);
    }

    public void setupCountdownHandler(int seconds) {
        mRpsGame.setIsCountdownRunning(true);
        mRpsGame.setCountDownSeconds(seconds);
        mRpsGame.setIsGamePhaseRoundComplete(false);
        mRpsGame.setHasPlayerWon(false);
        mRpsGame.setHasComputerWon(false);
    }

    //Sets up game to enable user to throw a move for one second and delivers message
    // if no throw is chosen during this time, the player loses with an illegal move of throwing too late
    private void startThrowNowHandler() {
        setupThrowNowHandler();

        mThrowNowHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (!mRpsGame.getHasPlayerHasThrownThisRound()) {
                    playerIllegalMoveTooLate();
                }

                notifyObservers();
            }
        }, 1000);
    }

    public void setupThrowNowHandler() {
        mCountdownHandler.removeCallbacksAndMessages(null);
        mRpsGame.setCanPlayerMakeLegalMove(true);
        mRpsGame.setCountDownDisplayValue(THROW_NOW_MSG);
        notifyObservers();
    }

    public void playerIllegalMoveTooLate() {
        mRpsGame.setIsCountdownRunning(false);
        mRpsGame.setHasComputerWon(true);
        mRpsGame.setHasPlayerWon(false);
        mRpsGame.setComputerScore(mRpsGame.getComputerScore() + 1);
        mRpsGame.setIsGamePhaseRoundComplete(true);
        mRpsGame.setIsGamePhaseCountDown(false);
        mRpsGame.setCanPlayerMakeLegalMove(false);
        mRpsGame.setComputerThrowImage(0);
        mRpsGame.setPlayerThrowImage(ILLEGAL_THROW_IMAGE_RESOURCE_ID);
        mRpsGame.setPlayerMoveDescription(ILLEGAL_MOVE_NONE_THROWN_DISPLAY_DESCRIPTION);
        mRpsGame.setPlayerThrowValue(4);
    }
}
