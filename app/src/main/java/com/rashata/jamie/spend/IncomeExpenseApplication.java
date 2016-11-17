package com.rashata.jamie.spend;

import android.app.Application;
import android.content.Context;

import com.rashata.jamie.spend.repository.DatabaseRealm;


public class IncomeExpenseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Initialize thing(s) here
        Contextor.getInstance().init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
