package com.geeksquad.jjamie.rubjai.component.module;

import com.geeksquad.jjamie.rubjai.IncomeExpenseApplication;
import com.geeksquad.jjamie.rubjai.component.ApplicationContextModule;
import com.geeksquad.jjamie.rubjai.repository.DatabaseRealm;
import com.geeksquad.jjamie.rubjai.repository.impl.DataRepositoryImpl;
import com.geeksquad.jjamie.rubjai.views.activity.ExpenseActivity;
import com.geeksquad.jjamie.rubjai.views.activity.HistoryActivity;
import com.geeksquad.jjamie.rubjai.views.activity.IncomeActivity;
import com.geeksquad.jjamie.rubjai.views.activity.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by jjamierashata on 6/16/16 AD.
 */
@Singleton
@Component(modules = {ApplicationContextModule.class, RepositoryModule.class})
public interface ApplicationComponent {

    void inject(IncomeExpenseApplication application);

    void inject(DatabaseRealm databaseRealm);

    void inject(DataRepositoryImpl messageRepository);

    void inject(MainActivity mainActivity);

    void inject(ExpenseActivity expenseActivity);

    void inject(IncomeActivity incomeActivity);

    void inject(HistoryActivity historyActivity);


}
