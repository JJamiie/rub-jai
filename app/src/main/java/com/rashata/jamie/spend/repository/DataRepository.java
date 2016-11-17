package com.rashata.jamie.spend.repository;


import com.rashata.jamie.spend.manager.Data;
import com.rashata.jamie.spend.manager.Initial;

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

    rx.Observable<Float> getStatistic(int[] type);

    rx.Observable<Float> getStatistic(int[] type, int month);

    rx.Observable deleteData(int uuid);

    rx.Observable clearDB();


}
