package com.geeksquad.jjamie.rubjai.manager;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jjamierashata on 6/3/16 AD.
 */
public class Initial extends RealmObject {


    @PrimaryKey
    private int uuid;

    private double money;

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }
}
