package com.example.jjamierashata.incomeexpense.repository.impl;
import com.example.jjamierashata.incomeexpense.component.Injector;
import com.example.jjamierashata.incomeexpense.manager.Data;
import com.example.jjamierashata.incomeexpense.manager.Initial;
import com.example.jjamierashata.incomeexpense.repository.DataRepository;
import com.example.jjamierashata.incomeexpense.repository.DatabaseRealm;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import io.realm.Realm;
import io.realm.Sort;
import rx.Subscriber;
import rx.Observable;


public class DataRepositoryImpl implements DataRepository {

    private static final String TAG = "DataRepositoryImpl";
    @Inject
    DatabaseRealm databaseRealm;

    public DataRepositoryImpl() {
        Injector.getApplicationComponent().inject(this);
    }


    @Override
    public Observable<Integer> addData(final double money, final String note, final int catagory,final String title, final Date date, final int type) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    Realm realm = databaseRealm.getRealmInstance();
                    realm.beginTransaction();
                    Data data = realm.createObject(Data.class);
                    data.setUuid(databaseRealm.getRealmInstance().where(Data.class).findAll().size());
                    data.setMoney(money);
                    data.setNote(note);
                    data.setCatagory(catagory);
                    data.setTitle(title);
                    data.setDate(date);
                    data.setType(type);
                    realm.commitTransaction();
                    subscriber.onNext(data.getUuid());
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<Data>> findAllData() {
        return Observable.create(new Observable.OnSubscribe<List<Data>>() {
            @Override
            public void call(Subscriber<? super List<Data>> subscriber) {
                try {
                    List<Data> datas = databaseRealm.getRealmInstance().where(Data.class).findAll().sort("uuid", Sort.DESCENDING);
                    subscriber.onNext(datas);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<Initial>> findAllInitial() {
        return Observable.create(new Observable.OnSubscribe<List<Initial>>() {
            @Override
            public void call(Subscriber<? super List<Initial>> subscriber) {
                List<Initial> initials = databaseRealm.getRealmInstance().where(Initial.class).findAll();
                subscriber.onNext(initials);
                subscriber.onCompleted();
            }
        });
    }

    @Override
    public Observable<String> getSummary() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    List<Data> datas = databaseRealm.getRealmInstance().where(Data.class).findAll();
                    double summary = 0.0;
                    for (Data data : datas) {
                        if(data.getType() == Data.TYPE_INCOME){
                            summary += data.getMoney();
                        }else{
                            summary -= data.getMoney();
                        }
                    }
                    Initial initial = databaseRealm.getRealmInstance().where(Initial.class).findFirst();
                    if(initial != null ){
                        summary += initial.getMoney();
                    }
                    subscriber.onNext(String.format("%.2f", summary));
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);

                }
            }
        });
    }

    @Override
    public Observable<String> getSummaryToday() {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    Date date = new Date();
                    date.setHours(0);
                    date.setMinutes(0);
                    date.setSeconds(0);
                    List<Data> datas =  databaseRealm.getRealmInstance().where(Data.class).greaterThanOrEqualTo("date", date).findAll();
                    double summary = 0.0;
                    for (Data data : datas) {
                        if(data.getType() == Data.TYPE_INCOME){
                            summary += data.getMoney();
                        }else{
                            summary -= data.getMoney();
                        }
                    }
                    subscriber.onNext(String.format("%.2f", summary));
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onError(e);

                }
            }
        });
    }

    @Override
    public Observable<Double> getInitialMoney() {
        return Observable.create(new Observable.OnSubscribe<Double>() {
            @Override
            public void call(Subscriber<? super Double> subscriber) {
                try{
                    Initial initial = databaseRealm.getRealmInstance().where(Initial.class).findFirst();
                    if(initial != null){
                        subscriber.onNext(initial.getMoney());
                    }else{
                        subscriber.onNext(0.00);
                    }
                    subscriber.onCompleted();
                }catch (Exception e){
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Initial> setInitial(final double money) {
        return Observable.create(new Observable.OnSubscribe<Initial>() {
            @Override
            public void call(Subscriber<? super Initial> subscriber) {
                Initial initial = databaseRealm.getRealmInstance().where(Initial.class).findFirst();
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                if(initial == null) {
                    initial = realm.createObject(Initial.class);
                    initial.setMoney(money);
                }else{
                    initial.setMoney(money);
                }
                realm.commitTransaction();
            }
        });
    }


}
