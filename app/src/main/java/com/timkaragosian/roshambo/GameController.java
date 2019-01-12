package com.timkaragosian.roshambo;

import android.os.Handler;

import java.util.concurrent.TimeUnit;

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
                }
            }
        }, 1000);
    }
}
