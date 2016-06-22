package com.example.jjamierashata.incomeexpense.repository;


import com.example.jjamierashata.incomeexpense.manager.Data;

import java.util.List;

/**
 * Created by jjamierashata on 6/16/16 AD.
 */
public interface DataRepository {

    rx.Observable<String> add(Data model);

    rx.Observable<List<Data>> findAll();

    rx.Observable<String> getSummary();

}
