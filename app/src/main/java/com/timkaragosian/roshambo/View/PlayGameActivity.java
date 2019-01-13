package com.timkaragosian.roshambo.View;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.timkaragosian.roshambo.Model.RpsGame;
import com.timkaragosian.roshambo.Presenter.RpsGamePresenter;
import com.timkaragosian.roshambo.R;

/**
 * Activity that handles all the views for the game
 * <p>
 * Presentation layer feeds this a state and the game state will be shown
 */
public class PlayGameActivity extends AppCompatActivity {

    private static final String IS_GAME_PHASE_COUNTDOWN_SAVE_STATE = "isGamePhaseCountdown";
    private static final String IS_GAME_PHASE_ROUND_COMPLETE_SAVE_STATE = "isRoundComplete";
    private static final String HAS_PLAYER_WON_SAVE_STATE = "hasPlayerWon";
    private static final String HAS_COMPUTER_WON_SAVE_STATE = "hasComputerWon";
    private static final String CAN_PLAYER_MAKE_LEGAL_MOVE_SAVE_STATE = "hasComputerWon";
    private static final String HAS_PLAYER_THROWN_SAVE_STATE = "hasPlayerThrown";
    private static final String PLAYER_SCORE_SAVE_STATE = "playerScore";
    private static final String COMPUTER_SCORE_SAVE_STATE = "computerScore";
    private static final String SECONDS_REMAINING_SAVE_STATE = "secondsRemaining";
    private static final String PLAYER_THROW_VALUE_SAVE_STATE = "playerThrow";
    private static final String COMPUTER_THROW_VALUE_SAVE_STATE = "computerThrow";
    private static final String COMPUTER_THROW_IMAGE_SAVE_STATE = "computerThrowImage";
    private static final String PLAYER_THROW_IMAGE_SAVE_STATE = "playerThrowImage";
    private static final String COUNTDOWN_DESCRIPTION_VALUE_SAVE_STATE = "countdownDescription";
    private static final String PLAYER_THROW_DESCRIPTION_VALUE_SAVE_STATE = "playerThrowDescription";
    private static final String COMPUTER_THROW_DESCRIPTION_VALUE_SAVE_STATE = "computerThrowDescription";

    Button mStartRoundButton;

    ImageButton mRockThrow;
    ImageButton mPaperThrow;
    ImageButton mScissorsThrow;

    TextView mCountdownTextview;
    TextView mPlayerScoreTextview;
    TextView mComputerScoreTextview;
    TextView mPlayerWinsTextview;
    TextView mComputerWinsTextview;
    TextView mComputerThrowResultTextview;
    TextView mPlayerThrowResultTextview;

    ImageView mPlayerThrowImageView;
    ImageView mComputerThrowImageview;

    LinearLayout mPlayerThrowChoicesContainer;
    LinearLayout mPlayerThrowResultContainer;
    LinearLayout mComputerThrowResultContainer;

    RpsGamePresenter mRpsGamePresenter;

    //updates the view with whatever state it is given from the presenter
    RpsGamePresenter.OnGameStateChangedListener mOnGameStateChangedListener = new RpsGamePresenter.OnGameStateChangedListener() {
        @Override
        public void onGameStateChanged(RpsGame rpsGame) {
            if (rpsGame.getIsGamePhaseCountDown()) {
                mStartRoundButton.setVisibility(View.GONE);
                mPlayerThrowChoicesContainer.setVisibility(View.VISIBLE);
                mCountdownTextview.setVisibility(View.VISIBLE);

                mComputerWinsTextview.setVisibility(View.GONE);
                mPlayerWinsTextview.setVisibility(View.GONE);
                mComputerThrowResultContainer.setVisibility(View.GONE);
                mPlayerThrowResultContainer.setVisibility(View.GONE);

                mCountdownTextview.setText(rpsGame.getCountDownDisplayValue());

                if (!rpsGame.getIsCountdownRunning()) {
                    mRpsGamePresenter.startCountdownHandler(rpsGame.getCountDownSeconds());
                }
            } else if (rpsGame.getIsGamePhaseRoundComplete()) {
                //setup basic view visibility
                mPlayerThrowChoicesContainer.setVisibility(View.GONE);
                mCountdownTextview.setVisibility(View.GONE);
                mPlayerThrowResultContainer.setVisibility(View.VISIBLE);
                mPlayerThrowResultTextview.setText(rpsGame.getPlayerMoveDescription());

                //display moves where applicable
                if (rpsGame.getPlayerThrowImage() != 0) {
                    mPlayerThrowImageView.setImageDrawable(getResources().getDrawable(rpsGame.getPlayerThrowImage()));
                }

                if (!(rpsGame.getPlayerThrowValue() == RpsGamePresenter.ILLEGAL_THROW_TOO_EARLY_VALUE || rpsGame.getPlayerThrowValue() == RpsGamePresenter.ILLEGAL_THROW_TOO_LATE_VALUE)) {
                    mComputerThrowResultTextview.setVisibility(View.VISIBLE);
                    mComputerThrowResultTextview.setText(rpsGame.getComputerMoveDescription());
                    mComputerThrowResultContainer.setVisibility(View.VISIBLE);
                    mComputerThrowImageview.setImageDrawable(getResources().getDrawable(rpsGame.getComputerThrowImage()));
                }

                //display scores
                mPlayerScoreTextview.setText(String.valueOf(rpsGame.getPlayerScore()));
                mComputerScoreTextview.setText(String.valueOf(rpsGame.getComputerScore()));

                //display winner indicator
                if (rpsGame.getHasPlayerWon()) {
                    mPlayerScoreTextview.setText(String.valueOf(rpsGame.getPlayerScore()));
                    mPlayerWinsTextview.setText(R.string.player_wins);
                    mPlayerWinsTextview.setVisibility(View.VISIBLE);
                } else if (rpsGame.getHasComputerWon()) {
                    mComputerWinsTextview.setText(R.string.computer_wins);
                    mComputerWinsTextview.setVisibility(View.VISIBLE);
                    mStartRoundButton.setVisibility(View.VISIBLE);
                } else {
                    mPlayerWinsTextview.setText(R.string.draw_result);
                    mComputerWinsTextview.setText(R.string.draw_result);
                    mPlayerWinsTextview.setVisibility(View.VISIBLE);
                    mComputerWinsTextview.setVisibility(View.VISIBLE);
                }

                mStartRoundButton.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        mRpsGamePresenter = new RpsGamePresenter(mOnGameStateChangedListener);

        initViews();
        initListeners();

        if (savedInstanceState != null) {
            initSaveState(savedInstanceState);
        }
    }

    private void initViews() {
        mStartRoundButton = findViewById(R.id.start_round_button);

        mRockThrow = findViewById(R.id.rock_throw_choice_imagebutton);
        mPaperThrow = findViewById(R.id.paper_throw_choice_imagebutton);
        mScissorsThrow = findViewById(R.id.scissors_throw_choice_imagebutton);

        mCountdownTextview = findViewById(R.id.countdown_textview);
        mPlayerScoreTextview = findViewById(R.id.player_score);
        mComputerScoreTextview = findViewById(R.id.computer_score);
        mComputerThrowResultTextview = findViewById(R.id.computer_throw_textview);
        mPlayerThrowResultTextview = findViewById(R.id.player_throw_textview);
        mPlayerWinsTextview = findViewById(R.id.player_wins_textview);
        mComputerWinsTextview = findViewById(R.id.computer_wins_textview);

        mComputerThrowImageview = findViewById(R.id.computer_throw_imageview);
        mPlayerThrowImageView = findViewById(R.id.player_throw_imageview);

        mPlayerThrowChoicesContainer = findViewById(R.id.player_throw_choices_container);
        mPlayerThrowResultContainer = findViewById(R.id.player_throw_result_container);
        mComputerThrowResultContainer = findViewById(R.id.computer_throw_result_container);
    }

    private void initListeners() {
        mStartRoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRpsGamePresenter.handleRoundCountdown(3);
            }
        });

        mRockThrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRpsGamePresenter.setPlayerThrow(RpsGamePresenter.ROCK_THROW_VALUE);
            }
        });

        mPaperThrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRpsGamePresenter.setPlayerThrow(RpsGamePresenter.PAPER_THROW_VALUE);
            }
        });

        mScissorsThrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRpsGamePresenter.setPlayerThrow(RpsGamePresenter.SCISSORS_THROW_VALUE);
            }
        });
    }

    //pass values into presenter to recreate from save state
    public void initSaveState(Bundle savedInstanceState) {
        RpsGame rpsGame = new RpsGame();

        rpsGame.setIsGamePhaseCountDown(savedInstanceState.getBoolean(IS_GAME_PHASE_COUNTDOWN_SAVE_STATE));
        rpsGame.setIsGamePhaseRoundComplete(savedInstanceState.getBoolean(IS_GAME_PHASE_ROUND_COMPLETE_SAVE_STATE));
        rpsGame.setHasPlayerWon(savedInstanceState.getBoolean(HAS_PLAYER_WON_SAVE_STATE));
        rpsGame.setHasComputerWon(savedInstanceState.getBoolean(HAS_COMPUTER_WON_SAVE_STATE));
        rpsGame.setCanPlayerMakeLegalMove(savedInstanceState.getBoolean(CAN_PLAYER_MAKE_LEGAL_MOVE_SAVE_STATE));
        rpsGame.setHasPlayerThrownThisRound(savedInstanceState.getBoolean(HAS_PLAYER_THROWN_SAVE_STATE));

        rpsGame.setPlayerScore(savedInstanceState.getInt(PLAYER_SCORE_SAVE_STATE));
        rpsGame.setComputerScore(savedInstanceState.getInt(COMPUTER_SCORE_SAVE_STATE));
        rpsGame.setCountDownSeconds(savedInstanceState.getInt(SECONDS_REMAINING_SAVE_STATE));
        rpsGame.setPlayerThrowValue(savedInstanceState.getInt(PLAYER_THROW_VALUE_SAVE_STATE));
        rpsGame.setComputerThrowValue(savedInstanceState.getInt(COMPUTER_THROW_VALUE_SAVE_STATE));
        rpsGame.setPlayerThrowImage(savedInstanceState.getInt(PLAYER_THROW_IMAGE_SAVE_STATE));
        rpsGame.setComputerThrowImage(savedInstanceState.getInt(COMPUTER_THROW_IMAGE_SAVE_STATE));

        rpsGame.setCountDownDisplayValue(savedInstanceState.getString(COUNTDOWN_DESCRIPTION_VALUE_SAVE_STATE));
        rpsGame.setPlayerMoveDescription(savedInstanceState.getString(PLAYER_THROW_DESCRIPTION_VALUE_SAVE_STATE));
        rpsGame.setComputerMoveDescription(savedInstanceState.getString(COMPUTER_THROW_DESCRIPTION_VALUE_SAVE_STATE));

        mRpsGamePresenter.setGameState(rpsGame);
    }

    //save everything in rpsGame obj on presenter to be restored
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        RpsGame rpsGame = mRpsGamePresenter.getGameState();

        outState.putBoolean(IS_GAME_PHASE_COUNTDOWN_SAVE_STATE, rpsGame.getIsGamePhaseCountDown());
        outState.putBoolean(IS_GAME_PHASE_ROUND_COMPLETE_SAVE_STATE, rpsGame.getIsGamePhaseRoundComplete());
        outState.putBoolean(HAS_PLAYER_WON_SAVE_STATE, rpsGame.getHasPlayerWon());
        outState.putBoolean(HAS_COMPUTER_WON_SAVE_STATE, rpsGame.getHasComputerWon());
        outState.putBoolean(CAN_PLAYER_MAKE_LEGAL_MOVE_SAVE_STATE, rpsGame.getCanPlayerMakeLegalMove());
        outState.putBoolean(HAS_PLAYER_THROWN_SAVE_STATE, rpsGame.getHasPlayerHasThrownThisRound());

        outState.putInt(PLAYER_SCORE_SAVE_STATE, rpsGame.getPlayerScore());
        outState.putInt(COMPUTER_SCORE_SAVE_STATE, rpsGame.getComputerScore());
        outState.putInt(SECONDS_REMAINING_SAVE_STATE, rpsGame.getCountDownSeconds());
        outState.putInt(PLAYER_THROW_VALUE_SAVE_STATE, rpsGame.getPlayerThrowValue());
        outState.putInt(COMPUTER_THROW_VALUE_SAVE_STATE, rpsGame.getComputerThrowValue());
        outState.putInt(PLAYER_THROW_IMAGE_SAVE_STATE, rpsGame.getPlayerThrowImage());
        outState.putInt(COMPUTER_THROW_IMAGE_SAVE_STATE, rpsGame.getComputerThrowImage());

        outState.putString(COUNTDOWN_DESCRIPTION_VALUE_SAVE_STATE, rpsGame.getCountDownDisplayValue());
        outState.putString(PLAYER_THROW_DESCRIPTION_VALUE_SAVE_STATE, rpsGame.getPlayerMoveDescription());
        outState.putString(COMPUTER_THROW_DESCRIPTION_VALUE_SAVE_STATE, rpsGame.getComputerMoveDescription());

        super.onSaveInstanceState(outState);
    }
}
