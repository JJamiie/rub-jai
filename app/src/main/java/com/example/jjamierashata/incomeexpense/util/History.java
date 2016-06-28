package com.example.jjamierashata.incomeexpense.util;

import java.lang.reflect.Type;

/**
 * Created by jjamierashata on 5/31/16 AD.
 */
public class History {
    private String title;
    private String note;
    private int img;
    private double money;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getTitle() {
        return title;
    }

    public String getNote() {
        return note;
    }

    public int getImg() {
        return img;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setImg(int img) {
        this.img = img;
    }

}
