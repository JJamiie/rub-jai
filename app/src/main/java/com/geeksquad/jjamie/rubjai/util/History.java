package com.geeksquad.jjamie.rubjai.util;

import java.util.Date;

/**
 * Created by jjamierashata on 5/31/16 AD.
 */
public class History {
    private int uuid;
    private double money;
    private String note;
    private int catagory;
    private Date date;
    private int type;

    public History(int uuid, double money, String note, int catagory, Date date,int type){
        this.uuid = uuid;
        this.money = money;
        this.note = note;
        this.catagory  =catagory;
        this.date = date;
        this.type = type;
    }

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
