package com.rashata.jamie.spend.repository.impl;

import android.util.Log;

import com.rashata.jamie.spend.Contextor;
import com.rashata.jamie.spend.manager.Data;
import com.rashata.jamie.spend.manager.ExpenseCategory;
import com.rashata.jamie.spend.manager.ExpenseStatistic;
import com.rashata.jamie.spend.manager.IncomeCategory;
import com.rashata.jamie.spend.manager.Initial;
import com.rashata.jamie.spend.repository.DataRepository;
import com.rashata.jamie.spend.util.CategoryItem;
import com.rashata.jamie.spend.util.Constants;
import com.rashata.jamie.spend.util.RubjaiPreference;

import java.util.ArrayList;
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
    public Observable<Float> getStatistic(final int idExpenseStatistic) {
        return Observable.create(new Observable.OnSubscribe<Float>() {
            @Override
            public void call(Subscriber<? super Float> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    List<ExpenseCategory> expenseCategories = realm.where(ExpenseCategory.class).equalTo("idExpenseStatistic", idExpenseStatistic).findAll();
                    float summary = 0;
                    for (ExpenseCategory expenseCategory : expenseCategories) {
                        List<Data> datas = realm.where(Data.class).equalTo("catagory", expenseCategory.getUuid())
                                .equalTo("type", Data.TYPE_EXPENSE).findAll();
                        for (Data data : datas) {
                            summary += data.getMoney();
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
    public Observable<List<ExpenseCategory>> getStatisticCategory(final int idExpenseStatistic) {
        return Observable.create(new Observable.OnSubscribe<List<ExpenseCategory>>() {
            @Override
            public void call(Subscriber<? super List<ExpenseCategory>> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    List<ExpenseCategory> expenseCategories = realm.where(ExpenseCategory.class).equalTo("idExpenseStatistic", idExpenseStatistic).findAll();
                    subscriber.onNext(expenseCategories);
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    subscriber.onError(e);

                }
            }
        });
    }


    @Override
    public Observable<Float> getStatistic(final int idExpenseStatistic, final int month) {
        return Observable.create(new Observable.OnSubscribe<Float>() {
            @Override
            public void call(Subscriber<? super Float> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    List<ExpenseCategory> expenseCategories = realm.where(ExpenseCategory.class)
                            .equalTo("idExpenseStatistic", idExpenseStatistic).findAll();
                    float summary = 0;
                    if (month != -1) {
                        Calendar calendar_before = Calendar.getInstance();
                        calendar_before.set(Calendar.MONTH, month);
                        calendar_before.set(Calendar.DAY_OF_MONTH, 1);
                        Date date_before = calendar_before.getTime();
                        Calendar calendar_after = Calendar.getInstance();
                        calendar_after.set(Calendar.MONTH, month + 1);
                        calendar_after.set(Calendar.DAY_OF_MONTH, 1);
                        Date date_after = calendar_after.getTime();
                        for (ExpenseCategory expenseCategory : expenseCategories) {
                            List<Data> datas = realm.where(Data.class).equalTo("catagory", expenseCategory.getUuid())
                                    .greaterThanOrEqualTo("date", date_before)
                                    .lessThan("date", date_after)
                                    .equalTo("type", Data.TYPE_EXPENSE).findAll();
                            for (Data data : datas) {
                                summary += data.getMoney();
                            }
                        }
                    } else {
                        for (ExpenseCategory expenseCategory : expenseCategories) {
                            List<Data> datas = realm.where(Data.class).equalTo("catagory", expenseCategory.getUuid())
                                    .equalTo("type", Data.TYPE_EXPENSE).findAll();
                            for (Data data : datas) {
                                summary += data.getMoney();
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
    public Observable<List<ExpenseStatistic>> getAllExpenseStatistic() {
        return Observable.create(new Observable.OnSubscribe<List<ExpenseStatistic>>() {
            @Override
            public void call(Subscriber<? super List<ExpenseStatistic>> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    List<ExpenseStatistic> expenseStatistics = realm.where(ExpenseStatistic.class).findAll();
                    subscriber.onNext(expenseStatistics);
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

    @Override
    public Observable getExpenseCategory() {
        return Observable.create(new Observable.OnSubscribe<List<ExpenseCategory>>() {
            @Override
            public void call(Subscriber<? super List<ExpenseCategory>> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    List<ExpenseCategory> datas = realm.where(ExpenseCategory.class).equalTo("show", true).findAllSorted("position");
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
    public Observable addExpenseCategory(final String name, final int image) {
        return Observable.create(new Observable.OnSubscribe<ExpenseCategory>() {
            @Override
            public void call(Subscriber<? super ExpenseCategory> subscriber) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                RealmResults<ExpenseCategory> datas = realm.where(ExpenseCategory.class).findAll().sort("uuid", Sort.DESCENDING);
                ExpenseCategory expensecategory;
                if (datas.isEmpty()) {
                    expensecategory = realm.createObject(ExpenseCategory.class, 0);
                } else {
                    expensecategory = realm.createObject(ExpenseCategory.class, datas.get(0).getUuid() + 1);
                }
                expensecategory.setName(name);
                expensecategory.setPicture(Contextor.getInstance().getContext().getResources().getResourceEntryName(image));
                expensecategory.setShow(true);
                expensecategory.setPosition(datas.size());
                expensecategory.setIdExpenseStatistic(-1);
                realm.commitTransaction();
                subscriber.onNext(expensecategory);
                subscriber.onCompleted();
                realm.close();
            }
        });
    }

    @Override
    public Observable editExpenseCategory(final int uuid, final String name, final int image) {
        return Observable.create(new Observable.OnSubscribe<ExpenseCategory>() {
            @Override
            public void call(Subscriber<? super ExpenseCategory> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    ExpenseCategory expenseCategory = realm.where(ExpenseCategory.class).equalTo("uuid", uuid).findFirst();
                    expenseCategory.setName(name);
                    expenseCategory.setPicture(Contextor.getInstance().getContext().getResources().getResourceEntryName(image));
                    realm.commitTransaction();
                    subscriber.onNext(expenseCategory);
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable addIncomeCategory(final String name, final int image) {
        return Observable.create(new Observable.OnSubscribe<IncomeCategory>() {
            @Override
            public void call(Subscriber<? super IncomeCategory> subscriber) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                RealmResults<IncomeCategory> datas = realm.where(IncomeCategory.class).findAll().sort("uuid", Sort.DESCENDING);
                IncomeCategory incomeCategory = realm.createObject(IncomeCategory.class, datas.get(0).getUuid() + 1);
                incomeCategory.setName(name);
                incomeCategory.setPicture(Contextor.getInstance().getContext().getResources().getResourceEntryName(image));
                incomeCategory.setShow(true);
                incomeCategory.setPosition(datas.size());
                realm.commitTransaction();
                subscriber.onNext(incomeCategory);
                subscriber.onCompleted();
                realm.close();
            }
        });
    }

    @Override
    public Observable editIncomeCategory(final int uuid, final String name, final int image) {
        return Observable.create(new Observable.OnSubscribe<IncomeCategory>() {
            @Override
            public void call(Subscriber<? super IncomeCategory> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    IncomeCategory incomeCategory = realm.where(IncomeCategory.class).equalTo("uuid", uuid).findFirst();
                    incomeCategory.setName(name);
                    incomeCategory.setPicture(Contextor.getInstance().getContext().getResources().getResourceEntryName(image));
                    realm.commitTransaction();
                    subscriber.onNext(incomeCategory);
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable<List<ExpenseCategory>> getAllExpenseCategory() {
        return Observable.create(new Observable.OnSubscribe<List<ExpenseCategory>>() {
            @Override
            public void call(Subscriber<? super List<ExpenseCategory>> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    List<ExpenseCategory> datas = realm.where(ExpenseCategory.class).findAllSorted("position");
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
    public Observable addExpenseCategory(final int idExpenseStatistic, final int idExpenseCategory) {
        return Observable.create(new Observable.OnSubscribe() {
            @Override
            public void call(Object o) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                ExpenseCategory expenseCategory = realm.where(ExpenseCategory.class).equalTo("uuid", idExpenseCategory).findFirst();
                expenseCategory.setIdExpenseStatistic(idExpenseStatistic);
                realm.commitTransaction();
                realm.close();
            }
        });
    }

    @Override
    public Observable deleteExpenseCategory(final int idExpenseCategory) {
        return Observable.create(new Observable.OnSubscribe() {
            @Override
            public void call(Object o) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                ExpenseCategory expenseCategory = realm.where(ExpenseCategory.class).equalTo("uuid", idExpenseCategory).findFirst();
                expenseCategory.setIdExpenseStatistic(-1);
                realm.commitTransaction();
                realm.close();
            }
        });
    }

    @Override
    public Observable getExpenseCategoryWithId(final int id) {
        return Observable.create(new Observable.OnSubscribe<ExpenseCategory>() {
            @Override
            public void call(Subscriber<? super ExpenseCategory> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    ExpenseCategory expenseCategory = realm.where(ExpenseCategory.class).equalTo("uuid", id).findFirst();
                    subscriber.onNext(expenseCategory);
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable getIncomeCategory() {
        return Observable.create(new Observable.OnSubscribe<List<IncomeCategory>>() {
            @Override
            public void call(Subscriber<? super List<IncomeCategory>> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    List<IncomeCategory> datas = realm.where(IncomeCategory.class).equalTo("show", true).findAllSorted("position");
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
    public Observable getAllIncomeCategory() {
        return Observable.create(new Observable.OnSubscribe<List<IncomeCategory>>() {
            @Override
            public void call(Subscriber<? super List<IncomeCategory>> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    List<IncomeCategory> datas = realm.where(IncomeCategory.class).findAllSorted("position");
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
    public Observable getIncomeCategoryWithId(final int id) {
        return Observable.create(new Observable.OnSubscribe<IncomeCategory>() {
            @Override
            public void call(Subscriber<? super IncomeCategory> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    IncomeCategory incomeCategory = realm.where(IncomeCategory.class).equalTo("uuid", id).findFirst();
                    subscriber.onNext(incomeCategory);
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    @Override
    public Observable initialData() {
        return Observable.create(new Observable.OnSubscribe() {
                                     @Override
                                     public void call(Object o) {
                                         Realm realm = Realm.getDefaultInstance();
                                         realm.beginTransaction();
                                         Log.d(TAG, "add initial data");

                                         //Add expense statistic
                                         for (int i = 0; i < Constants.titleInitialStatistic.length; i++) {
                                             ExpenseStatistic expenseStatistic = realm.createObject(ExpenseStatistic.class, i);
                                             expenseStatistic.setTitle(Constants.titleInitialStatistic[i]);
                                         }

                                         // Add Income
                                         for (int i = 0; i < Constants.incomeInitial.length; i++) {
                                             IncomeCategory incomeCategory = realm.createObject(IncomeCategory.class, i);
                                             incomeCategory.setName(Constants.incomeInitial[i]);
                                             incomeCategory.setPicture(Contextor.getInstance().getContext().getResources().getResourceEntryName(Constants.incomePicInitial[i]));
                                             if (i == Constants.incomeInitial.length - 1) incomeCategory.setShow(false);
                                             else incomeCategory.setShow(true);
                                             incomeCategory.setPosition(i);
                                         }

                                         // Add Expense
                                         for (int i = 0; i < Constants.expenseInitial.length; i++) {
                                             ExpenseCategory expensecategory = realm.createObject(ExpenseCategory.class, i);
                                             expensecategory.setName(Constants.expenseInitial[i]);
                                             expensecategory.setPicture(Contextor.getInstance().getContext().getResources().getResourceEntryName(Constants.expensePicInitial[i]));
                                             expensecategory.setIdExpenseStatistic(-1);
                                             if (i == Constants.expenseInitial.length - 1) expensecategory.setShow(false);
                                             else expensecategory.setShow(true);
                                             expensecategory.setPosition(i);
                                             for (int item : Constants.factor4) {
                                                 if (i == item) {
                                                     expensecategory.setIdExpenseStatistic(0);
                                                     break;
                                                 }
                                             }
                                             for (int item : Constants.relax) {
                                                 if (i == item) {
                                                     expensecategory.setIdExpenseStatistic(1);
                                                     break;
                                                 }
                                             }
                                             for (int item : Constants.travel) {
                                                 if (i == item) {
                                                     expensecategory.setIdExpenseStatistic(2);
                                                     break;
                                                 }
                                             }
                                             for (int item : Constants.payment) {
                                                 if (i == item) {
                                                     expensecategory.setIdExpenseStatistic(3);
                                                     break;
                                                 }
                                             }
                                         }

                                         RubjaiPreference rubjaiPreference = new RubjaiPreference(Contextor.getInstance().getContext());
                                         rubjaiPreference.initialData = "done";
                                         rubjaiPreference.update();
                                         realm.commitTransaction();
                                         realm.close();
                                     }
                                 }

        );
    }

    @Override
    public Observable updateCategoryExpense(final ArrayList<CategoryItem> categoryItems) {
        return Observable.create(new Observable.OnSubscribe() {
            @Override
            public void call(Object o) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                for (int i = 0; i < categoryItems.size(); i++) {
                    ExpenseCategory expenseCategory = realm.where(ExpenseCategory.class).equalTo("uuid", categoryItems.get(i).getId()).findFirst();
                    expenseCategory.setPosition(i);
                    expenseCategory.setShow(categoryItems.get(i).isShow());
                }
                realm.commitTransaction();
                realm.close();
            }
        });
    }

    @Override
    public Observable updateCategoryIncome(final ArrayList<CategoryItem> categoryItems) {
        return Observable.create(new Observable.OnSubscribe() {
            @Override
            public void call(Object o) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                for (int i = 0; i < categoryItems.size(); i++) {
                    IncomeCategory incomeCategory = realm.where(IncomeCategory.class).equalTo("uuid", categoryItems.get(i).getId()).findFirst();
                    incomeCategory.setPosition(i);
                    incomeCategory.setShow(categoryItems.get(i).isShow());
                }
                realm.commitTransaction();
                realm.close();
            }
        });
    }

    @Override
    public Observable deleteCategoryExpense(final int uuid) {
        return Observable.create(new Observable.OnSubscribe() {
            @Override
            public void call(Object o) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                List<Data> datas = realm.where(Data.class).equalTo("catagory", uuid).equalTo("type", Data.TYPE_EXPENSE).findAll();
                for (Data data : datas) {
                    data.setCatagory(Constants.TYPE_OTHER_EXPENSE);
                }
                ExpenseCategory expenseCategory = realm.where(ExpenseCategory.class).equalTo("uuid", uuid).findFirst();
                expenseCategory.deleteFromRealm();
                realm.commitTransaction();
                realm.close();
            }
        });
    }

    @Override
    public Observable deleteCategoryIncome(final int uuid) {
        return Observable.create(new Observable.OnSubscribe() {
            @Override
            public void call(Object o) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                List<Data> datas = realm.where(Data.class).equalTo("catagory", uuid).equalTo("type", Data.TYPE_INCOME).findAll();
                for (Data data : datas) {
                    data.setCatagory(Constants.TYPE_OTHER_INCOME);
                }
                IncomeCategory incomeCategory = realm.where(IncomeCategory.class).equalTo("uuid", uuid).findFirst();
                incomeCategory.deleteFromRealm();
                realm.commitTransaction();
                realm.close();
            }
        });
    }

    @Override
    public Observable<ExpenseStatistic> addExpenseStatistic(final String title) {
        return Observable.create(new Observable.OnSubscribe<ExpenseStatistic>() {
            @Override
            public void call(Subscriber<? super ExpenseStatistic> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    RealmResults<ExpenseStatistic> datas = realm.where(ExpenseStatistic.class).findAll().sort("uuid", Sort.DESCENDING);
                    ExpenseStatistic expenseStatistic = null;
                    if (datas.isEmpty()) {
                        expenseStatistic = realm.createObject(ExpenseStatistic.class, 0);
                    } else {
                        expenseStatistic = realm.createObject(ExpenseStatistic.class, datas.get(0).getUuid() + 1);
                    }
                    expenseStatistic.setTitle(title);
                    realm.commitTransaction();
                    subscriber.onNext(expenseStatistic);
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    subscriber.onError(e);

                }
            }
        });
    }

    @Override
    public Observable<ExpenseStatistic> editExpenseStatistic(final int uuid, final String title) {
        return Observable.create(new Observable.OnSubscribe<ExpenseStatistic>() {
            @Override
            public void call(Subscriber<? super ExpenseStatistic> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    realm.beginTransaction();
                    ExpenseStatistic expenseStatistic = realm.where(ExpenseStatistic.class).equalTo("uuid", uuid).findFirst();
                    expenseStatistic.setTitle(title);
                    realm.commitTransaction();
                    subscriber.onNext(expenseStatistic);
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    subscriber.onError(e);

                }
            }
        });
    }

    @Override
    public Observable deleteExpenseStatistic(final int uuid) {
        return Observable.create(new Observable.OnSubscribe() {

            @Override
            public void call(Object o) {
                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                List<ExpenseCategory> expenseCategories = realm.where(ExpenseCategory.class).equalTo("idExpenseStatistic", uuid).findAll();
                for (ExpenseCategory expenseCategory : expenseCategories) {
                    expenseCategory.setIdExpenseStatistic(-1);
                }
                ExpenseStatistic expenseStatistic = realm.where(ExpenseStatistic.class).equalTo("uuid", uuid).findFirst();
                expenseStatistic.deleteFromRealm();
                realm.commitTransaction();
                realm.close();
            }
        });


    }

    @Override
    public Observable<String> getTitleStatistic(final int uuid) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                try {
                    Realm realm = Realm.getDefaultInstance();
                    ExpenseCategory expenseCategory = realm.where(ExpenseCategory.class).equalTo("uuid", uuid).findFirst();
                    if (expenseCategory.getIdExpenseStatistic() == -1) {
                        subscriber.onNext("");
                    } else {
                        ExpenseStatistic expenseStatistic = realm.where(ExpenseStatistic.class).equalTo("uuid", expenseCategory
                                .getIdExpenseStatistic()).findFirst();
                        subscriber.onNext(expenseStatistic.getTitle());
                    }
                    subscriber.onCompleted();
                    realm.close();
                } catch (Exception e) {
                    subscriber.onError(e);

                }
            }
        });
    }
}
