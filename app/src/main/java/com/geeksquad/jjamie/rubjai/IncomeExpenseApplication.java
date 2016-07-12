package com.geeksquad.jjamie.rubjai;
import android.app.Application;
import com.geeksquad.jjamie.rubjai.component.Injector;
import com.geeksquad.jjamie.rubjai.repository.DatabaseRealm;
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
