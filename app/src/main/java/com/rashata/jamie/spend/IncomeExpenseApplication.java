package com.rashata.jamie.spend;

import android.app.Application;
import com.rashata.jamie.spend.repository.RealmManager;
import com.rashata.jamie.spend.util.RubjaiPreference;


public class IncomeExpenseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Initialize thing(s) here
        Contextor.getInstance().init(getApplicationContext());
        RubjaiPreference rubjaiPreference = new RubjaiPreference(this);
        if (!rubjaiPreference.initialData.equals("done")){
            RealmManager.getInstance().getDataRepository().initialData().subscribe();
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
