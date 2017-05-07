package com.rashata.jamie.spend.repository.database;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jjamierashata on 11/26/2016 AD.
 */

public class ExpenseStatistic extends RealmObject {
    @PrimaryKey
    private int uuid;
    private String title;

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
