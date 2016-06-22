package com.example.jjamierashata.incomeexpense.repository.impl;

import com.example.jjamierashata.incomeexpense.component.Injector;
import com.example.jjamierashata.incomeexpense.manager.Data;
import com.example.jjamierashata.incomeexpense.repository.DataRepository;
import com.example.jjamierashata.incomeexpense.repository.DatabaseRealm;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Observable;


public class DataRepositoryImpl implements DataRepository {

    @Inject
    DatabaseRealm databaseRealm;

    public DataRepositoryImpl() {
        Injector.getApplicationComponent().inject(this);
    }

    @Override
    public Observable<String> add(final Data model) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    databaseRealm.add(model);
                    subscriber.onNext(model.getUuid());
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<Data>> findAll() {
        return Observable.create(new Observable.OnSubscribe<List<Data>>() {
            @Override
            public void call(Subscriber<? super List<Data>> subscriber) {
                try {
                    List<Data> datas = databaseRealm.findAll(Data.class);
                    subscriber.onNext(datas);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<String> getSummary() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    List<Data> datas = databaseRealm.findAll(Data.class);
                    double summary = 0.0;
                    for (Data data : datas) {
                        summary += data.getMoney();

                    }
                    subscriber.onNext(String.format("%.2f", summary));
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);

                }
            }
        });
    }


}
