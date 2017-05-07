package com.rashata.jamie.spend.repository;


import com.rashata.jamie.spend.manager.Data;
import com.rashata.jamie.spend.manager.ExpenseCategory;
import com.rashata.jamie.spend.manager.ExpenseStatistic;
import com.rashata.jamie.spend.manager.Initial;
import com.rashata.jamie.spend.util.CategoryItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by jjamierashata on 6/16/16 AD.
 */
public interface DataRepository {

    rx.Observable<Integer> addData(double money, String note, int catagory, Date date, int type);

    rx.Observable<Integer> editData(int uuid, double money, String note, int catagory, Date date, int type);

    rx.Observable<List<Data>> findAllData();

    rx.Observable<List<Data>> findAllData(int type);

    rx.Observable<List<Initial>> findAllInitial();

    rx.Observable<String> getSummary();

    rx.Observable<String> getSummary(int type, int month);

    rx.Observable<String> getSummaryToday();

    rx.Observable<String> getSummary(int type);

    rx.Observable<Double> getInitialMoney();

    rx.Observable<Initial> setInitial(double money);

    rx.Observable<Float> getStatistic(int idExpenseStatistic);

    rx.Observable<List<ExpenseCategory>> getStatisticCategory(int idExpenseStatistic);

    rx.Observable<Float> getStatistic(final int idExpenseStatistic, final int month);

    rx.Observable<List<ExpenseStatistic>> getAllExpenseStatistic();

    rx.Observable deleteData(int uuid);

    rx.Observable clearDB();

    rx.Observable getExpenseCategory();

    rx.Observable addExpenseCategory(String name, int image);

    rx.Observable editExpenseCategory(int uuid, String name, int image);

    rx.Observable addIncomeCategory(String name, int image);

    rx.Observable editIncomeCategory(int uuid, String name, int image);

    rx.Observable<List<ExpenseCategory>> getAllExpenseCategory();

    rx.Observable addExpenseCategory(int idExpenseStatistic, int idExpenseCategory);

    rx.Observable deleteExpenseCategory(int idExpenseCategory);

    rx.Observable getExpenseCategoryWithId(int id);

    rx.Observable getIncomeCategory();

    rx.Observable getAllIncomeCategory();

    rx.Observable getIncomeCategoryWithId(int id);

    rx.Observable initialData();

    rx.Observable updateCategoryExpense(ArrayList<CategoryItem> categoryItems);

    rx.Observable updateCategoryIncome(ArrayList<CategoryItem> categoryItems);

    rx.Observable deleteCategoryExpense(int uuid);

    rx.Observable deleteCategoryIncome(int uuid);

    rx.Observable<ExpenseStatistic> addExpenseStatistic(String title);

    rx.Observable<ExpenseStatistic> editExpenseStatistic(int uuid, String title);

    rx.Observable deleteExpenseStatistic(int uuid);

    rx.Observable<String> getTitleStatistic(int uuid);

    rx.Observable changeLanguage(String from);
}
