package com.rashata.jamie.spend;
import android.app.Application;
import com.rashata.jamie.spend.component.Injector;
import com.rashata.jamie.spend.repository.DatabaseRealm;
import javax.inject.Inject;

public class IncomeExpenseApplication extends Application {

    @Inject DatabaseRealm databaseRealm;

    @Override
    public void onCreate() {
        super.onCreate();
        //Initialize thing(s) here
        Injector.initializeApplicationComponent(this);
        Injector.getApplicationComponent().inject(this);
        databaseRealm.setup();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

}
