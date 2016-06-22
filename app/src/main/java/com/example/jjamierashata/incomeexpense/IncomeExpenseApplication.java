package com.example.jjamierashata.incomeexpense;
import android.app.Application;
import com.example.jjamierashata.incomeexpense.component.Injector;
import com.example.jjamierashata.incomeexpense.repository.DatabaseRealm;
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
