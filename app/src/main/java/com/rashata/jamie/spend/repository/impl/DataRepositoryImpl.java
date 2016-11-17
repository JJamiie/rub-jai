package com.rashata.jamie.spend.repository.impl;

import android.util.Log;

import com.rashata.jamie.spend.manager.Data;
import com.rashata.jamie.spend.manager.Initial;
import com.rashata.jamie.spend.repository.DataRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Subscriber;
import rx.Observable;


public class DataRepositoryImpl implements DataRepository {

    private static final String TAG = "DataRepositoryImpl";

    @Override
    public Observable<Integer> addData(final double money, final String note, final int catagory, final Date date, final int type) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    RealmResults<Data> datas = realm.where(Data.class).findAll().sort("uuid", Sort.DESCENDING);
                    Data data = null;
                    if (datas.isEmpty()) {
                        data = realm.createObject(Data.class, 0);
                    } else {
                        data = realm.createObject(Data.class, datas.get(0).getUuid() + 1);
                    }
                    data.setMoney(money);
                    data.setNote(note);
                    data.setCatagory(catagory);
                    data.setDate(date);
                    data.setType(type);
                    realm.commitTransaction();
                    subscriber.onNext(data.getUuid());
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Integer> editData(final int uuid, final double money, final String note, final int catagory, final Date date, final int type) {
        return Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    final Data data = realm.where(Data.class).equalTo("uuid", uuid).findFirst();
                    realm.beginTransaction();
                    data.setMoney(money);
                    data.setNote(note);
                    data.setCatagory(catagory);
                    data.setDate(date);
                    data.setType(type);
                    realm.commitTransaction();
                    subscriber.onNext(data.getUuid());
                    subscriber.onCompleted();
                    realm.close();
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
                    Realm realm = Realm.getDefaultInstance();
                    List<Data> datas = realm.where(Data.class).findAll().sort("uuid", Sort.DESCENDING);
                    subscriber.onNext(datas);
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<Data>> findAllData(final int type) {
        return Observable.create(new Observable.OnSubscribe<List<Data>>() {
            @Override
            public void call(Subscriber<? super List<Data>> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    Date date = new Date();
                    date.setHours(0);
                    date.setMinutes(0);
                    date.setSeconds(0);
                    List<Data> datas = null;
                    switch (type) {
                        case 0:
                            datas = realm.where(Data.class).findAll().sort("uuid", Sort.DESCENDING);
                            break;
                        case 1:
                            datas = realm.where(Data.class).greaterThanOrEqualTo("date", new Date(date.getTime())).findAll().sort("uuid", Sort.DESCENDING);
                            break;
                        case 2:
                            datas = realm.where(Data.class).greaterThanOrEqualTo("date", new Date(date.getTime() - 172800000L)).findAll().sort("uuid", Sort.DESCENDING);
                            break;
                        case 3:
                            datas = realm.where(Data.class).greaterThanOrEqualTo("date", new Date(date.getTime() - 518400000L)).findAll().sort("uuid", Sort.DESCENDING);
                            break;
                        case 4:
                            datas = realm.where(Data.class).greaterThanOrEqualTo("date", new Date(date.getTime() - 2505600000L)).findAll().sort("uuid", Sort.DESCENDING);
                            break;
                        default:
                            datas = realm.where(Data.class).findAll().sort("uuid", Sort.DESCENDING);
                            break;
                    }
                    subscriber.onNext(datas);
                    subscriber.onCompleted();
                    realm.close();
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
                try {
                    Realm realm = Realm.getDefaultInstance();
                    List<Initial> initials = realm.where(Initial.class).findAll();
                    subscriber.onNext(initials);
                    subscriber.onCompleted();
                    realm.close();
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
                    Realm realm = Realm.getDefaultInstance();
                    List<Data> datas = realm.where(Data.class).findAll();
                    double summary = 0.0;
                    for (Data data : datas) {
                        if (data.getType() == Data.TYPE_INCOME) {
                            summary += data.getMoney();
                        } else {
                            summary -= data.getMoney();
                        }
                    }
                    Initial initial = realm.where(Initial.class).findFirst();
                    if (initial != null) {
                        summary += initial.getMoney();
                    }
                    if (summary > 0) {
                        subscriber.onNext("+" + String.format("%.2f", summary));
                    } else {
                        subscriber.onNext(String.format("%.2f", summary));

                    }
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    subscriber.onError(e);

                }
            }
        });
    }

    @Override
    public Observable<String> getSummary(final int type, final int month) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();

                    List<Data> datas;
                    if (month != -1) {
                        Calendar calendar_before = Calendar.getInstance();
                        calendar_before.set(Calendar.MONTH, month);
                        calendar_before.set(Calendar.DAY_OF_MONTH, 1);
                        Date date_before = calendar_before.getTime();
                        Calendar calendar_after = Calendar.getInstance();
                        calendar_after.set(Calendar.MONTH, month + 1);
                        calendar_after.set(Calendar.DAY_OF_MONTH, 1);
                        Date date_after = calendar_after.getTime();
                        datas = realm.where(Data.class)
                                .greaterThanOrEqualTo("date", date_before)
                                .lessThan("date", date_after)
                                .equalTo("type", type)
                                .findAll();
                    } else {
                        datas = realm.where(Data.class).equalTo("type", type).findAll();
                    }

                    double summary = 0.0;

                    for (Data data : datas) {
                        summary += data.getMoney();
                    }
                    subscriber.onNext(String.format("%.2f", summary));
                    subscriber.onCompleted();
                    realm.close();
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
                    Realm realm = Realm.getDefaultInstance();
                    Date date = new Date();
                    date.setHours(0);
                    date.setMinutes(0);
                    date.setSeconds(0);
                    List<Data> datas = realm.where(Data.class).greaterThanOrEqualTo("date", date).findAll();
                    double summary = 0.0;
                    for (Data data : datas) {
                        if (data.getType() == Data.TYPE_INCOME) {
                            summary += data.getMoney();
                        } else {
                            summary -= data.getMoney();
                        }
                    }
                    if (summary > 0) {
                        subscriber.onNext("+" + String.format("%.2f", summary));
                    } else {
                        subscriber.onNext(String.format("%.2f", summary));
                    }
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    subscriber.onError(e);

                }
            }
        });
    }

    @Override
    public Observable<String> getSummary(final int type) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    List<Data> datas = realm.where(Data.class).findAll();
                    double summary = 0.0;
                    for (Data data : datas) {
                        if (data.getType() == type) {
                            summary += data.getMoney();
                        }
                    }
                    subscriber.onNext(String.format("%.2f", summary));
                    subscriber.onCompleted();
                    realm.close();
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
                try {
                    Realm realm = Realm.getDefaultInstance();
                    Initial initial = realm.where(Initial.class).findFirst();
                    if (initial != null) {
                        subscriber.onNext(initial.getMoney());
                    } else {
                        subscriber.onNext(0.00);
                    }
                    subscriber.onCompleted();
                    realm.close();

                } catch (Exception e) {
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
                try {
                    Realm realm = Realm.getDefaultInstance();
                    Initial initial = realm.where(Initial.class).findFirst();
                    realm.beginTransaction();
                    if (initial == null) {
                        initial = realm.createObject(Initial.class, 0);
                        initial.setMoney(money);
                    } else {
                        initial.setMoney(money);
                    }
                    realm.commitTransaction();
                    subscriber.onNext(initial);
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<Float> getStatistic(final int[] type) {
        return Observable.create(new Observable.OnSubscribe<Float>() {
            @Override
            public void call(Subscriber<? super Float> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    List<Data> datas = realm.where(Data.class).findAll();
                    float summary = 0;
                    for (Data data : datas) {
                        Log.d(TAG, "id catagory: " + data.getCatagory());
                        for (int i = 0; i < type.length; i++) {
                            if (data.getCatagory() == type[i]) {
                                summary += data.getMoney();
                                break;
                            }
                        }
                    }
                    subscriber.onNext(summary);
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    subscriber.onError(e);

                }
            }
        });
    }

    @Override
    public Observable<Float> getStatistic(final int[] type, final int month) {
        return Observable.create(new Observable.OnSubscribe<Float>() {
            @Override
            public void call(Subscriber<? super Float> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    List<Data> datas;
                    if (month != -1) {
                        Calendar calendar_before = Calendar.getInstance();
                        calendar_before.set(Calendar.MONTH, month);
                        calendar_before.set(Calendar.DAY_OF_MONTH, 1);
                        Date date_before = calendar_before.getTime();
                        Calendar calendar_after = Calendar.getInstance();
                        calendar_after.set(Calendar.MONTH, month + 1);
                        calendar_after.set(Calendar.DAY_OF_MONTH, 1);
                        Date date_after = calendar_after.getTime();
                        datas = realm.where(Data.class)
                                .greaterThanOrEqualTo("date", date_before)
                                .lessThan("date", date_after)
                                .equalTo("type", Data.TYPE_EXPENSE)
                                .findAll();
                    } else {
                        datas = realm.where(Data.class).equalTo("type", Data.TYPE_EXPENSE).findAll();
                    }

                    float summary = 0;
                    for (Data data : datas) {
                        Log.d(TAG, "id catagory: " + data.getCatagory());
                        for (int i = 0; i < type.length; i++) {
                            if (data.getCatagory() == type[i]) {
                                summary += data.getMoney();
                                break;
                            }
                        }
                    }
                    subscriber.onNext(summary);
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    subscriber.onError(e);

                }
            }
        });
    }


    @Override
    public Observable deleteData(final int uuid) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    final Data data = realm.where(Data.class).equalTo("uuid", uuid).findFirst();
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            data.deleteFromRealm();
                        }
                    });
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    subscriber.onError(e);
                }


            }
        });
    }

    @Override
    public Observable clearDB() {
        return Observable.create(new Observable.OnSubscribe() {
            @Override
            public void call(Object o) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.delete(Data.class);
                realm.delete(Initial.class);
                realm.commitTransaction();
                realm.close();
            }
        });
    }


}
