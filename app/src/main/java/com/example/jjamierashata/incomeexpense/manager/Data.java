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
    public static final int TYPE_EXPENSE = 0;
    public static final int TYPE_INCOME = 0;

    @PrimaryKey private String uuid;
    private double money;
    private String note;
    private int catagory;
    private Date date;
    private int type;


    public String getUuid() {
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

    public Data(Builder builder) {
        this.uuid = builder.uuid;
    }

    public static Builder newBuilder(){
        return new Builder();
    }


    public static class Builder {
        private String uuid;
        private double money;
        private String note;
        private int catagory;
        private Date date;
        private int type;

        public Builder() {
            this.uuid = UUID.randomUUID().toString();
        }

        public Builder money(double money) {
            this.money = money;
            return this;
        }

        public Builder note(String note) {
            this.note = note;
            return this;
        }

        public Builder catagory(int catagory) {
            this.catagory = catagory;
            return this;
        }

        public Builder date(Date date) {
            this.date = date;
            return this;
        }

        public Builder type(int type) {
            this.type = type;
            return this;
        }


        public Data build() {
            return new Data(this);
        }
    }
}
