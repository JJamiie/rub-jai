package com.rashata.jamie.spend;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.text.TextUtils;

import com.rashata.jamie.spend.util.RubjaiPreference;

import java.util.Locale;


public class IncomeExpenseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Initialize thing(s) here
        Contextor.getInstance().init(getApplicationContext());
        RubjaiPreference rubjaiPreference = new RubjaiPreference(this);
        updateLanguage(getApplicationContext(), rubjaiPreference.language);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
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
