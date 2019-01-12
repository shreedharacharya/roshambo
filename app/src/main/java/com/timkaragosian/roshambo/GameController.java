package com.timkaragosian.roshambo;

import android.os.Handler;


public class GameController {
    //This is the class that will handle the timer and game board itself

    public void startCountdownTimer(final PlayGameActivity playGameActivity, final int seconds) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playGameActivity.updateCountdownTimer(String.valueOf(seconds), seconds);

                if (seconds > 0) {
                    startCountdownTimer(playGameActivity, seconds - 1);
                } else {
                    playGameActivity.updateCountdownTimer(playGameActivity.getString(R.string.throw_now), seconds);
                    startThrowNowTimer(playGameActivity);
                }
            }
        }, 1000);
    }

    public void startThrowNowTimer(final PlayGameActivity playGameActivity) {
        playGameActivity.mCanPlayerMakeLegalMove = true;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                playGameActivity.mCanPlayerMakeLegalMove = false;

                if (!playGameActivity.mPlayerHasThrownThisRoundState) {
                    playGameActivity.computerWins();
                    playGameActivity.setPlayerThrow("Illegal Move");
                }
            }
        }, 1000);
    }
}
