package com.timkaragosian.roshambo.Presenter;

import android.os.Handler;

import com.timkaragosian.roshambo.Model.RpsGame;
import com.timkaragosian.roshambo.R;

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

    private static final int ROCK_THROW_IMAGE_RESOURCE = R.drawable.rock_image;
    private static final int PAPER_THROW_IMAGE_RESOURCE = R.drawable.paper;
    private static final int SCISSORS_THROW_IMAGE_RESOURCE = R.drawable.scissors;
    private static final int ILLEGAL_THROW_IMAGE_RESOURCE = R.drawable.illegal_move;

    RpsGame mRpsGame = new RpsGame();
    OnGameStateChangedListener mOnGameStateChangedListener;

    private final Handler countdownHandler = new Handler();

    public RpsGamePresenter(OnGameStateChangedListener listener) {
        this.setOnGameStateChangedListener(listener);
    }

    public void startNewRound() {
        //start timer
        mRpsGame.setCountDownDisplayValue(GET_READY_MGS);
        mRpsGame.setPlayerThrowValue(EMPTY_THROW_VALUE);
        mRpsGame.setComputerThrowValue(EMPTY_THROW_VALUE);
        mRpsGame.setIsGamePhaseCountDown(true);
        notifyObservers();

        startCountdownHandler(3);
    }

    public void setPlayerThrow(int throwValue) {
        mRpsGame.setPlayerThrowValue(throwValue);
        notifyObservers();
    }

    private void notifyObservers() {
        if (this.mOnGameStateChangedListener != null) {
            mOnGameStateChangedListener.onGameStateChanged(mRpsGame);
        }
    }

    public void setOnGameStateChangedListener(OnGameStateChangedListener l) {
        this.mOnGameStateChangedListener = l;
    }

    public interface OnGameStateChangedListener {
        void onGameStateChanged(RpsGame rpsGame);
    }

    //Starts timer to display a countdown
    public void startCountdownHandler(final int seconds) {
        mRpsGame.setIsGamePhaseRoundComplete(false);
        mRpsGame.setHasPlayerWon(false);
        mRpsGame.setHasComputerWon(false);
        mRpsGame.setCanPlayerMakeLegalMove(false);

        countdownHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mRpsGame.setCountDownSeconds(seconds);
                mRpsGame.setCountDownDisplayValue(String.valueOf(seconds));

                if (seconds > 0) {
                    startCountdownHandler(seconds - 1);
                } else {
                    startThrowNowHandler();
                }

                notifyObservers();
            }
        }, 1000);
    }

    //Sets up game to enable user to throw a move for one second and delivers message
    // if no throw is chosen during this time, the player loses with an illegal move of throwing too late
    public void startThrowNowHandler() {
        mRpsGame.setCanPlayerMakeLegalMove(true);
        mRpsGame.setCountDownDisplayValue(THROW_NOW_MSG);
        notifyObservers();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!mRpsGame.getHasPlayerHasThrownThisRound()) {
                    mRpsGame.setHasComputerWon(true);
                    mRpsGame.setHasPlayerWon(false);
                    mRpsGame.setComputerScore(mRpsGame.getComputerScore() + 1);
                    mRpsGame.setIsGamePhaseRoundComplete(true);
                    mRpsGame.setIsGamePhaseCountDown(false);
                    mRpsGame.setCanPlayerMakeLegalMove(false);
                    mRpsGame.setmComputerThrowImage(0);
                    mRpsGame.setmPlayerThrowImage(ILLEGAL_THROW_IMAGE_RESOURCE);
                    mRpsGame.setPlayerMoveDescription(ILLEGAL_MOVE_NONE_THROWN_DISPLAY_DESCRIPTION);
                    mRpsGame.setPlayerThrowValue(4); //set to whatever needs to be illegal move thrown too late
                }

                mRpsGame.setCanPlayerMakeLegalMove(false);
                notifyObservers();
            }
        }, 1000);
    }
}
