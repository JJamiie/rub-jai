package com.rashata.jamie.spend.util;

import com.rashata.jamie.spend.Contextor;
import com.rashata.jamie.spend.R;



public class Constants {
    public static final int[] expensePic = {R.drawable.item_bus, R.drawable.item_food, R.drawable.item_shirt, R.drawable.item_movie
            , R.drawable.item_health, R.drawable.item_lover, R.drawable.item_party, R.drawable.item_shopping, R.drawable.item_gift
            , R.drawable.item_electric, R.drawable.item_water, R.drawable.item_phone, R.drawable.item_pet, R.drawable.item_run
            , R.drawable.item_family, R.drawable.item_bed, R.drawable.item_others, R.drawable.expense_ic_1, R.drawable.expense_ic_1
            , R.drawable.expense_ic_2, R.drawable.expense_ic_3, R.drawable.expense_ic_4, R.drawable.expense_ic_5, R.drawable.expense_ic_6
            , R.drawable.expense_ic_7, R.drawable.expense_ic_8, R.drawable.expense_ic_9, R.drawable.expense_ic_10, R.drawable.expense_ic_11
            , R.drawable.expense_ic_12, R.drawable.expense_ic_13, R.drawable.expense_ic_14, R.drawable.expense_ic_15, R.drawable.expense_ic_16
            , R.drawable.expense_ic_17, R.drawable.expense_ic_18, R.drawable.expense_ic_19, R.drawable.expense_ic_20, R.drawable.expense_ic_21
            , R.drawable.expense_ic_22, R.drawable.expense_ic_23, R.drawable.expense_ic_24, R.drawable.expense_ic_25, R.drawable.expense_ic_26
            , R.drawable.expense_ic_27, R.drawable.expense_ic_28, R.drawable.expense_ic_29, R.drawable.expense_ic_30, R.drawable.expense_ic_31
            , R.drawable.expense_ic_32};

    public static final int[] incomePic = {R.drawable.item_parent, R.drawable.item_salary, R.drawable.item_gift_gray, R.drawable.item_loan
            , R.drawable.item_sell, R.drawable.item_others_gray, R.drawable.income_ic_1, R.drawable.income_ic_2, R.drawable.income_ic_3
            , R.drawable.income_ic_4, R.drawable.income_ic_5, R.drawable.income_ic_6, R.drawable.income_ic_7, R.drawable.income_ic_8
            , R.drawable.income_ic_9, R.drawable.income_ic_10, R.drawable.income_ic_11, R.drawable.income_ic_12};

    public static final int TYPE_OTHER_INCOME = 5;
    public static final int TYPE_OTHER_EXPENSE = 16;

    public static String[] incomeInitial = {Contextor.getInstance().getContext().getString(R.string.parent),
            Contextor.getInstance().getContext().getString(R.string.salary), Contextor.getInstance().getContext().getString(R.string.gift),
            Contextor.getInstance().getContext().getString(R.string.borrow), Contextor.getInstance().getContext().getString(R.string.sale),
            Contextor.getInstance().getContext().getString(R.string.others)};
    public static int[] incomePicInitial = {R.drawable.item_parent, R.drawable.item_salary, R.drawable.item_gift_gray, R.drawable.item_loan
            , R.drawable.item_sell, R.drawable.item_others_gray};
    public static String[] expenseInitial = {Contextor.getInstance().getContext().getString(R.string.travel),
            Contextor.getInstance().getContext().getString(R.string.food), Contextor.getInstance().getContext().getString(R.string.cloth),
            Contextor.getInstance().getContext().getString(R.string.movie), Contextor.getInstance().getContext().getString(R.string.health),
            Contextor.getInstance().getContext().getString(R.string.lover), Contextor.getInstance().getContext().getString(R.string.party),
            Contextor.getInstance().getContext().getString(R.string.shopping), Contextor.getInstance().getContext().getString(R.string.gift),
            Contextor.getInstance().getContext().getString(R.string.electricity_bill), Contextor.getInstance().getContext().getString(R.string.water_bill),
            Contextor.getInstance().getContext().getString(R.string.telephone), Contextor.getInstance().getContext().getString(R.string.pet),
            Contextor.getInstance().getContext().getString(R.string.sport), Contextor.getInstance().getContext().getString(R.string.family),
            Contextor.getInstance().getContext().getString(R.string.reservation), Contextor.getInstance().getContext().getString(R.string.others)};
    public static int[] expensePicInitial = {R.drawable.item_bus, R.drawable.item_food, R.drawable.item_shirt, R.drawable.item_movie
            , R.drawable.item_health, R.drawable.item_lover, R.drawable.item_party, R.drawable.item_shopping, R.drawable.item_gift
            , R.drawable.item_electric, R.drawable.item_water, R.drawable.item_phone, R.drawable.item_pet, R.drawable.item_run
            , R.drawable.item_family, R.drawable.item_bed, R.drawable.item_others};

    public static String[] titleInitialStatistic = {Contextor.getInstance().getContext().getString(R.string.basic_needs),
            Contextor.getInstance().getContext().getString(R.string.relax), Contextor.getInstance().getContext().getString(R.string.travel)
            , Contextor.getInstance().getContext().getString(R.string.pay)};
    public static int[] factor4 = {1, 4, 15};
    public static int[] relax = {2, 3, 5, 6, 7, 8, 13, 14};
    public static int[] travel = {0};
    public static int[] payment = {9, 10, 11, 12};


    public static String date[] = {Contextor.getInstance().getContext().getString(R.string.sunday)
            , Contextor.getInstance().getContext().getString(R.string.monday)
            , Contextor.getInstance().getContext().getString(R.string.tuesday)
            , Contextor.getInstance().getContext().getString(R.string.wednesday)
            , Contextor.getInstance().getContext().getString(R.string.thursday)
            , Contextor.getInstance().getContext().getString(R.string.friday)
            , Contextor.getInstance().getContext().getString(R.string.saturday)
    };
    public static String month[] = {Contextor.getInstance().getContext().getString(R.string.january)
            , Contextor.getInstance().getContext().getString(R.string.february)
            , Contextor.getInstance().getContext().getString(R.string.march)
            , Contextor.getInstance().getContext().getString(R.string.april)
            , Contextor.getInstance().getContext().getString(R.string.may)
            , Contextor.getInstance().getContext().getString(R.string.june)
            , Contextor.getInstance().getContext().getString(R.string.july)
            , Contextor.getInstance().getContext().getString(R.string.august)
            , Contextor.getInstance().getContext().getString(R.string.september)
            , Contextor.getInstance().getContext().getString(R.string.october)
            , Contextor.getInstance().getContext().getString(R.string.november)
            , Contextor.getInstance().getContext().getString(R.string.december)
    };


    public static int[] flag = new int[]{R.drawable.flag_thai, R.drawable.flag_eng};
    public static String[] language = new String[]{Contextor.getInstance().getContext().getString(R.string.thai)
            , Contextor.getInstance().getContext().getString(R.string.english)};

}
