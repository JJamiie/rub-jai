package com.rashata.jamie.spend.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rashata.jamie.spend.util.RubjaiPreference;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RubjaiPreference rubjaiPreference = new RubjaiPreference(this);
        if (rubjaiPreference.guideTour.equals("done")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, GuideActivity.class);
            startActivity(intent);

        }
        finish();
    }
}
