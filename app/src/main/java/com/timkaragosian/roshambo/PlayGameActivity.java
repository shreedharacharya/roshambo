package com.timkaragosian.roshambo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PlayGameActivity extends AppCompatActivity {

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

    int mComputerThrowChoiceState;
    int mPlayerThrowChoiceState;

    boolean mIsSelectingThrowState;
    boolean mPlayerHasThrownThisRoundState;
    boolean mHasPlayerWon;
    boolean mHasComputerWon;

    int mPlayerScoreState;
    int mComputerScoreState;
    int mSeconds;

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
                setPlayerThrow(R.drawable.rock_image);
                mPlayerThrowResultTextview.setText(R.string.rock);

                mComputerThrowImageview.setVisibility(View.VISIBLE);
                mStartRoundButton.setVisibility(View.VISIBLE);

                setComputerThrow(R.drawable.scissors);
                mComputerThrowResultTextview.setText(R.string.scissors);

                playerWins();
            }
        });

        mPaperThrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlayerThrow(R.drawable.paper);
                mPlayerThrowResultTextview.setText(R.string.paper);

                mComputerThrowImageview.setVisibility(View.VISIBLE);
                mStartRoundButton.setVisibility(View.VISIBLE);

                setComputerThrow(R.drawable.paper);
                mComputerThrowResultTextview.setText(R.string.paper);

                roundIsDraw();
            }
        });

        mScissorsThrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlayerThrow(R.drawable.scissors);
                mPlayerThrowResultTextview.setText(R.string.scissors);

                mComputerThrowImageview.setVisibility(View.VISIBLE);
                mStartRoundButton.setVisibility(View.VISIBLE);

                setComputerThrow(R.drawable.rock_image);
                mComputerThrowResultTextview.setText(R.string.rock);

                computerWins();
            }
        });
    }

    public void initSaveState(Bundle savedInstanceState) {
        mPlayerHasThrownThisRoundState = savedInstanceState.getBoolean("hasPlayerThrown");
        mIsSelectingThrowState = savedInstanceState.getBoolean("isSelectingThrow");

        mPlayerScoreState = savedInstanceState.getInt("playerScore");
        mComputerScoreState = savedInstanceState.getInt("computerScore");

        mPlayerScoreTextview.setText(String.valueOf(mPlayerScoreState));
        mComputerScoreTextview.setText(String.valueOf(mComputerScoreState));

        if (mPlayerHasThrownThisRoundState) {
            setPlayerThrow(savedInstanceState.getInt("playerThrow"));
            setComputerThrow(savedInstanceState.getInt("computerThrow"));
            mHasPlayerWon = savedInstanceState.getBoolean("hasPlayerWon");
            mHasComputerWon = savedInstanceState.getBoolean("hasComputerWon");

            if (mHasPlayerWon) {
                playerWins();
            } else if (mHasComputerWon) {
                computerWins();
            } else {
                roundIsDraw();
            }
        } else if (mIsSelectingThrowState) {
            int secondsRemaining = savedInstanceState.getInt("secondsRemaining");
            if (secondsRemaining > 0) {
                mCountdownTextview.setText(String.valueOf(secondsRemaining));
                setupRound(secondsRemaining - 1);
            }
        }
    }

    public void setPlayerThrow(int resource) {
        mPlayerThrowImageView.setImageResource(resource);
        mPlayerThrowResultContainer.setVisibility(View.VISIBLE);

        mPlayerThrowChoicesContainer.setVisibility(View.GONE);
        mCountdownTextview.setVisibility(View.GONE);

        mPlayerHasThrownThisRoundState = true;
        mPlayerThrowChoiceState = resource;
        mIsSelectingThrowState = false;
    }

    public void setComputerThrow(int resource) {
        mComputerThrowImageview.setImageResource(resource);
        mComputerThrowResultContainer.setVisibility(View.VISIBLE);

        mComputerThrowChoiceState = resource;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("playerScore", mPlayerScoreState);
        outState.putInt("computerScore", mComputerScoreState);

        outState.putBoolean("hasPlayerThrown", mPlayerHasThrownThisRoundState);
        outState.putBoolean("isSelectingThrow", mIsSelectingThrowState);

        if (mPlayerHasThrownThisRoundState) {
            outState.putInt("playerThrow", mPlayerThrowChoiceState);
            outState.putInt("computerThrow", mComputerThrowChoiceState);
            outState.putBoolean("hasPlayerWon", mHasPlayerWon);
            outState.putBoolean("hasComputerWon", mHasComputerWon);
        }

        if (mIsSelectingThrowState) {
            outState.putInt("secondsRemaining", mSeconds);
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
        mPlayerThrowChoiceState = 0;
    }
}
