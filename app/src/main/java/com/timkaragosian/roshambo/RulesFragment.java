package com.timkaragosian.roshambo;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class RulesFragment extends Fragment {

    public RulesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_rules, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);

        if (getView() != null) {
            Button returnToMainMenuButton = getView().findViewById(R.id.rules_return_to_main_menu_button);

            returnToMainMenuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
                        ((MainActivity) getActivity()).showFragment(new MainMenuFragment());
                    }
                }
            });

            TextView moreInformationTextview = getView().findViewById(R.id.rules_more_information_text);
            moreInformationTextview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://wikipedia.org/wiki/Rock–paper–scissors"));
                    startActivity(browserIntent);
                }
            });
        }
    }
}
