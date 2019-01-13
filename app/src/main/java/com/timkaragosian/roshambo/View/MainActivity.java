package com.timkaragosian.roshambo.View;

import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.timkaragosian.roshambo.R;

public class MainActivity extends AppCompatActivity {

    Fragment oldFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (oldFragment != null) {
            showFragment(oldFragment);
        } else {
            showFragment(new MainMenuFragment());
        }
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_activity_layout);
        MainMenuFragment mainMenuFragment = new MainMenuFragment();

        if (currentFragment.getClass().equals(mainMenuFragment.getClass())) {
            super.onBackPressed();
        } else {
            showFragment(mainMenuFragment);
        }
    }

    public void showFragment(Fragment fragment) {
        oldFragment = fragment;

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_layout, fragment, fragment.getTag())
                .commit();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        MainMenuFragment mainMenuFragment = new MainMenuFragment();

        if (newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE && oldFragment.getClass() == mainMenuFragment.getClass()){
                        
        }
    }
}
