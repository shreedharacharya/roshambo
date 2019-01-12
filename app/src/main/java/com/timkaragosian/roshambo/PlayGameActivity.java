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

    ImageView mPlayerThrowImageView;
    ImageView mComputerThrowImageview;

    LinearLayout mPlayerThrowChoicesContainer;

    int mComputerThrowChoiceState;
    int mPlayerThrowChoiceState;

    boolean mIsSelectingThrowState;
    boolean mPlayerHasThrownThisRoundState;

    int mPlayerScoreState;
    int mComputerScoreState;

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

    public void initViews() {
        mStartRoundButton = findViewById(R.id.start_round_button);

        mRockThrow = findViewById(R.id.rock_throw_choice_imagebutton);
        mPaperThrow = findViewById(R.id.paper_throw_choice_imagebutton);
        mScissorsThrow = findViewById(R.id.scissors_throw_choice_imagebutton);

        mCountdownTextview = findViewById(R.id.countdown_textview);
        mPlayerScoreTextview = findViewById(R.id.player_score);
        mComputerScoreTextview = findViewById(R.id.computer_score);

        mComputerThrowImageview = findViewById(R.id.computer_throw_imageview);
        mPlayerThrowImageView = findViewById(R.id.player_throw_imageview);

        mPlayerThrowChoicesContainer = findViewById(R.id.player_throw_choices_container);
    }

    private void initListeners() {
        mStartRoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStartRoundButton.setVisibility(View.GONE);
                mPlayerThrowChoicesContainer.setVisibility(View.VISIBLE);

                mPlayerThrowImageView.setVisibility(View.GONE);
                mComputerThrowImageview.setVisibility(View.GONE);

                mCountdownTextview.setText("3... 2... 1... Throw!");
                mPlayerHasThrownThisRoundState = false;
                mIsSelectingThrowState = true;
                mPlayerThrowChoiceState = 0;
            }
        });

        mRockThrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlayerThrow(R.drawable.rock_image);
                mComputerThrowImageview.setVisibility(View.VISIBLE);
                mStartRoundButton.setVisibility(View.VISIBLE);
                setComputerThrow(R.drawable.scissors);
                playerWins();
            }
        });

        mPaperThrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlayerThrow(R.drawable.paper);
                mComputerThrowImageview.setVisibility(View.VISIBLE);
                mStartRoundButton.setVisibility(View.VISIBLE);
                setComputerThrow(R.drawable.scissors);
                computerWins();
            }
        });

        mScissorsThrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlayerThrow(R.drawable.scissors);
                mComputerThrowImageview.setVisibility(View.VISIBLE);
                mStartRoundButton.setVisibility(View.VISIBLE);
                setComputerThrow(R.drawable.rock_image);
                computerWins();
            }
        });
    }

    public void initSaveState(Bundle savedInstanceState) {
        mPlayerHasThrownThisRoundState = savedInstanceState.getBoolean("hasPlayerThrown");
        mIsSelectingThrowState = savedInstanceState.getBoolean("isSelectingThrow");

        mPlayerScoreTextview.setText(savedInstanceState.getInt("playerScore"));
        mComputerScoreTextview.setText(savedInstanceState.getInt("computerScore"));

        if (mPlayerHasThrownThisRoundState) {
            setPlayerThrow(savedInstanceState.getInt("playerThrow"));
            setComputerThrow(savedInstanceState.getInt("computerThrow"));
        } else if (mIsSelectingThrowState) {
            mRockThrow.performClick();
        }
    }

    public void setPlayerThrow(int resource) {
        mPlayerThrowImageView.setImageResource(resource);
        mPlayerThrowImageView.setVisibility(View.VISIBLE);
        mPlayerThrowChoicesContainer.setVisibility(View.GONE);

        mPlayerHasThrownThisRoundState = true;
        mPlayerThrowChoiceState = resource;
        mIsSelectingThrowState = false;
    }

    public void setComputerThrow(int resource) {
        mComputerThrowImageview.setImageResource(resource);
        mComputerThrowImageview.setVisibility(View.VISIBLE);
        mComputerThrowChoiceState = resource;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("playerScore", mPlayerScoreState);
        outState.putInt("computerScore", mComputerScoreState);

        outState.putBoolean("hasPlayerThrown", mPlayerHasThrownThisRoundState);
        outState.putBoolean("isSelectingThrow", mIsSelectingThrowState);

        if (mPlayerHasThrownThisRoundState) {
            outState.putInt("playerThrow", mPlayerThrowChoiceState);
            outState.putInt("computerThrow", mComputerThrowChoiceState);
        }
    }

    public void playerWins() {
        int score = Integer.valueOf(mPlayerScoreTextview.getText().toString());
        score++;
        mPlayerScoreTextview.setText(String.valueOf(score));
        mPlayerScoreState = score;
    }

    public void computerWins() {
        int score = Integer.valueOf(mComputerScoreTextview.getText().toString());
        score++;

        mComputerScoreTextview.setText(String.valueOf(score));
        mComputerScoreState = score;
    }
}
