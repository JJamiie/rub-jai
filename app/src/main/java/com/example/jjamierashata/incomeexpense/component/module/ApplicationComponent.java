package com.example.jjamierashata.incomeexpense.component.module;

import com.example.jjamierashata.incomeexpense.IncomeExpenseApplication;
import com.example.jjamierashata.incomeexpense.component.ApplicationContextModule;
import com.example.jjamierashata.incomeexpense.repository.DatabaseRealm;
import com.example.jjamierashata.incomeexpense.repository.impl.DataRepositoryImpl;
import com.example.jjamierashata.incomeexpense.util.History;
import com.example.jjamierashata.incomeexpense.views.activity.ExpenseActivity;
import com.example.jjamierashata.incomeexpense.views.activity.HistoryActivity;
import com.example.jjamierashata.incomeexpense.views.activity.IncomeActivity;
import com.example.jjamierashata.incomeexpense.views.activity.MainActivity;
import com.example.jjamierashata.incomeexpense.views.adapter.HistoryAdapter;

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
