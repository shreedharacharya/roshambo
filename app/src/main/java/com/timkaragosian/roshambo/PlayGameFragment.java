package com.timkaragosian.roshambo;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayGameFragment extends Fragment {


    public PlayGameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);
        if (getView() != null) {
            Button returnToMainMenuButton = getView().findViewById(R.id.play_game_return_to_main_menu_button);

            returnToMainMenuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
                        ((MainActivity)getActivity()).showFragment(new MainMenuFragment());
                    }
                }
            });
        }
    }
}
