package com.example.jjamierashata.incomeexpense.component;

import android.content.Context;

import com.example.jjamierashata.incomeexpense.IncomeExpenseApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jjamierashata on 6/16/16 AD.
 */
@Module
public class ApplicationContextModule {

    private final IncomeExpenseApplication incomeExpenseApplication;

    public ApplicationContextModule(IncomeExpenseApplication incomeExpenseApplication) {
        this.incomeExpenseApplication = incomeExpenseApplication;
    }

    @Provides
    @Singleton
    public IncomeExpenseApplication application() {
        return this.incomeExpenseApplication;
    }

    @Provides
    @Singleton
    public Context applicationContext() {
        return this.incomeExpenseApplication.getApplicationContext();
    }


}
