package com.rashata.jamie.spend.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rashata.jamie.spend.repository.RealmManager;
import com.rashata.jamie.spend.util.RubjaiPreference;

public class SplashActivity extends AppCompatActivity {

    private RubjaiPreference rubjaiPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rubjaiPreference = new RubjaiPreference(this);
        Intent intent = null;
        if (rubjaiPreference.guideTour.equals("done") && rubjaiPreference.newFeature1.equals("done")) {
            initialData();
            if (rubjaiPreference.passcode_en) {
                intent = new Intent(this, PasscodeActivity.class);
                intent.putExtra("isStart", true);
            } else {
                intent = new Intent(this, MainActivity.class);
            }
        } else if (rubjaiPreference.guideTour.equals("done") && !rubjaiPreference.newFeature1.equals("done")) {
            initialData();
            intent = new Intent(this, GuideActivity.class);
            intent.putExtra("feature", 1);
        } else {
            intent = new Intent(this, FirstSettingLanguageActivity.class);
        }
        startActivity(intent);
        finish();
    }

    public void initialData(){
        if (!rubjaiPreference.initialData.equals("done")) {
            RealmManager.getInstance().getDataRepository().initialData().subscribe();
        }
    }
}
