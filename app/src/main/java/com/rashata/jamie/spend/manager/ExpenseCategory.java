package com.rashata.jamie.spend.manager;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jjamierashata on 11/26/2016 AD.
 */

public class ExpenseCategory extends RealmObject {
    @PrimaryKey
    private int uuid;
    private String name;
    private String picture;
    private boolean show;
    private int position;
    private int idExpenseStatistic;

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public boolean isShow() {
        return show;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getIdExpenseStatistic() {
        return idExpenseStatistic;
    }

    public void setIdExpenseStatistic(int idExpenseStatistic) {
        this.idExpenseStatistic = idExpenseStatistic;
    }

}
