package com.rashata.jamie.spend.component;

import android.annotation.TargetApi;
import android.os.Build;
import com.rashata.jamie.spend.IncomeExpenseApplication;
import com.rashata.jamie.spend.component.module.ApplicationComponent;
import com.rashata.jamie.spend.component.module.DaggerApplicationComponent;
import com.rashata.jamie.spend.component.module.RepositoryModule;

import java.util.Objects;

public class Injector {
    private static ApplicationComponent applicationComponent;

    private Injector() {}

    public static void initializeApplicationComponent(IncomeExpenseApplication incomeExpenseApplication) {
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationContextModule(new ApplicationContextModule(incomeExpenseApplication))
                .repositoryModule(new RepositoryModule())
                .build();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static ApplicationComponent getApplicationComponent() {
            Objects.requireNonNull(applicationComponent, "applicationComponent is null");
        return applicationComponent;
    }

}
