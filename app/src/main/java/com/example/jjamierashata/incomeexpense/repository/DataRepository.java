package com.example.jjamierashata.incomeexpense.repository;


import com.example.jjamierashata.incomeexpense.manager.Data;
import com.example.jjamierashata.incomeexpense.manager.Initial;

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

    rx.Observable<String> getSummaryToday();

    rx.Observable<Double> getInitialMoney();

    rx.Observable<Initial> setInitial(double money);

    rx.Observable deleteData(int uuid);


}
