package com.rashata.jamie.spend.views.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.rashata.jamie.spend.Contextor;
import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.repository.RealmManager;
import com.rashata.jamie.spend.util.Constants;
import com.rashata.jamie.spend.util.RubjaiPreference;
import com.rashata.jamie.spend.util.SpinnerDropdownPictureAdapter;

import java.util.Locale;

public class FirstSettingLanguageActivity extends AppCompatActivity {
    private Spinner spin_language;
    private RubjaiPreference rubjaiPreference;
    private SpinnerDropdownPictureAdapter spinnerDropdownAdapterLang;
    private Button btn_set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_language_setting);

        rubjaiPreference = new RubjaiPreference(Contextor.getInstance().getContext());
        spin_language = (Spinner) findViewById(R.id.spin_language);
        spinnerDropdownAdapterLang = new SpinnerDropdownPictureAdapter(this, Constants.flag, Constants.language);
        spin_language.setAdapter(spinnerDropdownAdapterLang);
        if (getCurrentLocale().equals("th")) {
            spin_language.setSelection(0);
        } else if (getCurrentLocale().equals("en")) {
            spin_language.setSelection(1);
        }

        btn_set = (Button) findViewById(R.id.btn_set);
        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spin_language.getSelectedItemPosition() == 0) {
                    setLocale("th");
                } else if (spin_language.getSelectedItemPosition() == 1) {
                    setLocale("en");
                }
            }
        });
    }

    public void setLocale(String lang) {
        Resources res = this.getResources();
        Configuration conf = res.getConfiguration();
        conf.locale = new Locale(lang);
        res.updateConfiguration(conf, res.getDisplayMetrics());
        rubjaiPreference = new RubjaiPreference(this);
        rubjaiPreference.language = lang;
        rubjaiPreference.update();
        updateLanguage(getApplicationContext(), rubjaiPreference.language);
        RealmManager.getInstance().getDataRepository().initialData().subscribe();
        finish();
        Intent intent = new Intent(this, GuideActivity.class);
        startActivity(intent);
    }

    public String getCurrentLocale() {
        Locale current = getResources().getConfiguration().locale;
        return current.getLanguage();
    }


    public static void updateLanguage(Context context, String lang) {
        Configuration cfg = new Configuration();
        if (!TextUtils.isEmpty(lang))
            cfg.locale = new Locale(lang);
        else
            cfg.locale = Locale.getDefault();
        context.getResources().updateConfiguration(cfg, null);
    }

}
