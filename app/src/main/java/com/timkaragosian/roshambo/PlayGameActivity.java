package com.timkaragosian.roshambo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlayGameActivity extends AppCompatActivity {

    private static final String SECONDS_REMAINING = "secondsRemaining";
    private static final String HAS_PLAYER_THROWN = "hasPlayerThrown";
    private static final String PLAYER_SCORE = "playerScore";
    private static final String COMPUTER_SCORE = "computerScore";
    private static final String IS_SELECTING_THROW = "isSelectingThrow";
    private static final String PLAYER_THROW = "playerThrow";
    private static final String COMPUTER_THROW = "computerThrow";
    private static final String HAS_PLAYER_WON = "hasPlayerWon";
    private static final String HAS_COMPUTER_WON = "hasComputerWon";

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

    String mComputerThrowChoiceState;
    String mPlayerThrowChoiceState;

    boolean mIsSelectingThrowState;
    boolean mHasPlayerWon;
    boolean mHasComputerWon;

    int mPlayerScoreState;
    int mComputerScoreState;
    int mSeconds;

    public boolean mCanPlayerMakeLegalMove = false;
    public boolean mPlayerHasThrownThisRoundState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
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
                mCountdownTextview.setText(R.string.get_ready);
                setupRound(3);
            }
        });

        mRockThrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String computerMove = new ComputerController().computerMakesMove();
                setComputerThrow(computerMove);

                new PlayerController().playerThowsForResult(PlayGameActivity.this, Constants.ROCK, computerMove, mCanPlayerMakeLegalMove);

                mStartRoundButton.setVisibility(View.VISIBLE);
                mComputerThrowImageview.setVisibility(View.VISIBLE);
            }
        });

        mPaperThrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlayerThrow(Constants.PAPER);
                mPlayerThrowResultTextview.setText(R.string.paper);

                mComputerThrowImageview.setVisibility(View.VISIBLE);
                mStartRoundButton.setVisibility(View.VISIBLE);

                setComputerThrow(Constants.PAPER);
                mComputerThrowResultTextview.setText(R.string.paper);

                roundIsDraw();
            }
        });

        mScissorsThrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlayerThrow(Constants.SCISSORS);
                mPlayerThrowResultTextview.setText(R.string.scissors);

                mComputerThrowImageview.setVisibility(View.VISIBLE);
                mStartRoundButton.setVisibility(View.VISIBLE);

                setComputerThrow(Constants.ROCK);
                mComputerThrowResultTextview.setText(R.string.rock);

                computerWins();
            }
        });
    }

    public void initSaveState(Bundle savedInstanceState) {
        mPlayerHasThrownThisRoundState = savedInstanceState.getBoolean(HAS_PLAYER_THROWN);
        mIsSelectingThrowState = savedInstanceState.getBoolean(IS_SELECTING_THROW);

        mPlayerScoreState = savedInstanceState.getInt(PLAYER_SCORE);
        mComputerScoreState = savedInstanceState.getInt(COMPUTER_SCORE);

        mPlayerScoreTextview.setText(String.valueOf(mPlayerScoreState));
        mComputerScoreTextview.setText(String.valueOf(mComputerScoreState));

        if (mPlayerHasThrownThisRoundState) {
            setPlayerThrow(savedInstanceState.getString(PLAYER_THROW));

            String computerThrow = savedInstanceState.getString(COMPUTER_THROW);
            if (!TextUtils.isEmpty(computerThrow)) {
                setComputerThrow(computerThrow);
            }

            mHasPlayerWon = savedInstanceState.getBoolean(HAS_COMPUTER_WON);
            mHasComputerWon = savedInstanceState.getBoolean(HAS_COMPUTER_WON);

            if (mHasPlayerWon) {
                playerWins();
            } else if (mHasComputerWon) {
                computerWins();
            } else {
                roundIsDraw();
            }
        } else if (mIsSelectingThrowState) {
            int secondsRemaining = savedInstanceState.getInt(SECONDS_REMAINING);
            if (secondsRemaining > 0) {
                mCountdownTextview.setText(String.valueOf(secondsRemaining));
                setupRound(secondsRemaining - 1);
            }
        }
    }

    public void setPlayerThrow(String move) {
        int resource = getResourceFromSelection(move);
        mPlayerThrowImageView.setImageResource(resource);
        mPlayerThrowResultContainer.setVisibility(View.VISIBLE);
        mPlayerThrowResultTextview.setText(move);

        mPlayerThrowChoicesContainer.setVisibility(View.GONE);
        mCountdownTextview.setVisibility(View.GONE);

        mPlayerHasThrownThisRoundState = true;
        mPlayerThrowChoiceState = move;
        mIsSelectingThrowState = false;
    }

    public void setComputerThrow(String move) {
        int resource = getResourceFromSelection(move);

        mComputerThrowImageview.setImageResource(resource);
        mComputerThrowResultContainer.setVisibility(View.VISIBLE);
        mComputerThrowResultTextview.setText(move);
        mComputerThrowChoiceState = move;
    }

    private int getResourceFromSelection(String move) {
        switch (move) {
            case Constants.ROCK:
                return R.drawable.rock_image;
            case Constants.PAPER:
                return R.drawable.paper;
            case Constants.SCISSORS:
                return R.drawable.scissors;
            case Constants.ILLEGAL_MOVE:
                return R.drawable.illegal_move;
            default:
                return 0;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(PLAYER_SCORE, mPlayerScoreState);
        outState.putInt(COMPUTER_SCORE, mComputerScoreState);

        outState.putBoolean(HAS_PLAYER_THROWN, mPlayerHasThrownThisRoundState);
        outState.putBoolean(IS_SELECTING_THROW, mIsSelectingThrowState);

        if (mPlayerHasThrownThisRoundState) {
            outState.putString(PLAYER_THROW, mPlayerThrowChoiceState);
            outState.putString(COMPUTER_THROW, mComputerThrowChoiceState);
            outState.putBoolean(HAS_PLAYER_WON, mHasPlayerWon);
            outState.putBoolean(HAS_COMPUTER_WON, mHasComputerWon);
        }

        if (mIsSelectingThrowState) {
            outState.putInt(SECONDS_REMAINING, mSeconds);
        }

        super.onSaveInstanceState(outState);
    }

    public void playerWins() {
        int score = Integer.valueOf(mPlayerScoreTextview.getText().toString());
        score++;
        mPlayerScoreTextview.setText(String.valueOf(score));
        mPlayerScoreState = score;

        mHasPlayerWon = true;
        mHasComputerWon = false;

        mPlayerWinsTextview.setText(R.string.player_wins);
        mPlayerWinsTextview.setVisibility(View.VISIBLE);
    }

    public void computerWins() {
        int score = Integer.valueOf(mComputerScoreTextview.getText().toString());
        score++;

        mHasPlayerWon = false;
        mHasComputerWon = false;

        mComputerWinsTextview.setText(R.string.computer_wins);
        mComputerScoreTextview.setText(String.valueOf(score));
        mComputerScoreState = score;
        mComputerWinsTextview.setVisibility(View.VISIBLE);
        mStartRoundButton.setVisibility(View.VISIBLE);
    }

    public void roundIsDraw() {
        mPlayerWinsTextview.setText(R.string.draw_result);
        mComputerWinsTextview.setText(R.string.draw_result);

        mPlayerWinsTextview.setVisibility(View.VISIBLE);
        mComputerWinsTextview.setVisibility(View.VISIBLE);
    }

    public void updateCountdownTimer(String updateText, int seconds) {
        mCountdownTextview.setText(updateText);
        mSeconds = seconds;
    }

    private void setupRound(int seconds) {
        mStartRoundButton.setVisibility(View.GONE);
        mPlayerThrowChoicesContainer.setVisibility(View.VISIBLE);
        mCountdownTextview.setVisibility(View.VISIBLE);

        mComputerWinsTextview.setVisibility(View.GONE);
        mPlayerWinsTextview.setVisibility(View.GONE);
        mComputerThrowResultContainer.setVisibility(View.GONE);
        mPlayerThrowResultContainer.setVisibility(View.GONE);

        new GameController().startCountdownTimer(PlayGameActivity.this, seconds);

        mPlayerHasThrownThisRoundState = false;
        mIsSelectingThrowState = true;
        mPlayerThrowChoiceState = null;
    }
}
