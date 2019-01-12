package com.timkaragosian.roshambo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainMenuFragment mainMenuFragment = new MainMenuFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_layout, mainMenuFragment, mainMenuFragment.getTag())
                .commit();

        initViews();
        initListeners();
    }

    public void initViews(){

    }

    public void initListeners(){

    }
}
