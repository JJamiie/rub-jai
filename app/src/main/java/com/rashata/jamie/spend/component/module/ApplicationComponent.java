package com.rashata.jamie.spend.component.module;

import com.rashata.jamie.spend.IncomeExpenseApplication;
import com.rashata.jamie.spend.component.ApplicationContextModule;
import com.rashata.jamie.spend.repository.DatabaseRealm;
import com.rashata.jamie.spend.repository.impl.DataRepositoryImpl;
import com.rashata.jamie.spend.views.activity.ExpenseActivity;
import com.rashata.jamie.spend.views.activity.HistoryActivity;
import com.rashata.jamie.spend.views.activity.IncomeActivity;
import com.rashata.jamie.spend.views.activity.MainActivity;
import com.rashata.jamie.spend.views.activity.StatisticActivity;

import javax.inject.Singleton;

import dagger.Component;
import xml.RubjaiWidget;

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

    void inject(RubjaiWidget rubjaiWidget);

    void inject(StatisticActivity statisticActivity);


}
