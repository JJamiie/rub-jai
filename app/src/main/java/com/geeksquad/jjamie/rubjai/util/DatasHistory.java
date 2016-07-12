package com.geeksquad.jjamie.rubjai.util;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jjamierashata on 5/31/16 AD.
 */
public class DatasHistory {
    private ArrayList<History> histories;
    private Date date;
    private double total_money;

    public DatasHistory(ArrayList<History> histories, Date date, double total_money) {
        this.histories = histories;
        this.date = date;
        this.total_money = total_money;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getTotal_money() {
        return total_money;
    }

    public void setTotal_money(double total_money) {
        this.total_money = total_money;
    }

    public ArrayList<History> getHistories() {
        return histories;
    }

    public void setHistories(ArrayList<History> histories) {
        this.histories = histories;
    }
}
