package com.timkaragosian.roshambo.Controller;

import android.os.Handler;

import com.timkaragosian.roshambo.Model.Constants;
import com.timkaragosian.roshambo.View.PlayGameActivity;
import com.timkaragosian.roshambo.R;


public class GameController {
    //This is the class that will handle the timer and game board itself

    public final Handler countdownHandler = new Handler();

    public void startCountdownTimer(final PlayGameActivity playGameActivity, final int seconds) {
        countdownHandler.postDelayed(new Runnable() {
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
                if (!playGameActivity.mPlayerHasThrownThisRoundState) {
                    playGameActivity.computerWins();
                    playGameActivity.setPlayerThrow(Constants.ILLEGAL_MOVE_NONE_THROWN);
                }

                playGameActivity.mCanPlayerMakeLegalMove = false;
            }
        }, 1000);
    }
}
