package com.timkaragosian.roshambo;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MainMenuFragment extends Fragment {

    Button mPlayGame;
    Button mRules;
    Button mQuit;

    public MainMenuFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        initListeners();
    }

    public void initViews() {
        if (getView() != null) {
            mPlayGame = getView().findViewById(R.id.play_game_button);
            mRules = getView().findViewById(R.id.rules_button);
            mQuit = getView().findViewById(R.id.quit_button);
        }
    }

    public void initListeners() {
        mPlayGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
                    ((MainActivity)getActivity()).showFragment(new PlayGameFragment());
                }
            }
        });

        mRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
                    ((MainActivity)getActivity()).showFragment(new RulesFragment());
                }
            }
        });

        mQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
    }
}
