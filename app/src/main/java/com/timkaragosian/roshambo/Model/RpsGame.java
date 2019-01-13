package com.timkaragosian.roshambo.Model;

public class RpsGame {

    OnGameStateChangedListener mOnGameStateChangedListener;

    boolean mIsSelectingThrowState;
    boolean mHasPlayerWon;
    boolean mHasComputerWon;

    int mPlayerScoreState;
    int mComputerScoreState;
    int mSeconds;
    int mUserThrowValue;

    public boolean mCanPlayerMakeLegalMove = false;
    public boolean mPlayerHasThrownThisRoundState;

    public void setUserThrow(int throwValue){
        this.mUserThrowValue = throwValue;
        notifyObservers();
    }

    private void notifyObservers() {
        if (this.mOnGameStateChangedListener != null){
            mOnGameStateChangedListener.onGameStateChanged(this);
        }
    }

    public void setmOnGameStateChangedListener(OnGameStateChangedListener l) {
        this.mOnGameStateChangedListener = l;
    }

    public interface OnGameStateChangedListener {
        void onGameStateChanged(RpsGame rpsGame);
    }
}
