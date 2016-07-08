package com.example.jjamierashata.incomeexpense.manager;

import com.example.jjamierashata.incomeexpense.repository.DatabaseRealm;

import java.util.Date;
import java.util.UUID;

import javax.inject.Inject;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jjamierashata on 6/3/16 AD.
 */
public class Data extends RealmObject {
    public static final int TYPE_INCOME = 0;
    public static final int TYPE_EXPENSE = 1;
    @PrimaryKey private int uuid;
    private double money;
    private String note;
    private int catagory;
    private Date date;
    private int type;

    public int getUuid() {
        return uuid;
    }

    public double getMoney() {
        return money;
    }

    public String getNote() {
        return note;
    }

    public int getCatagory() {
        return catagory;
    }

    public Date getDate() {
        return date;
    }

    public int getType() {
        return type;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setCatagory(int catagory) {
        this.catagory = catagory;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setType(int type) {
        this.type = type;
    }


}
