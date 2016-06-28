package com.example.jjamierashata.incomeexpense.util;

import com.example.jjamierashata.incomeexpense.manager.Data;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by jjamierashata on 5/31/16 AD.
 */
public class DatasHistory {
    private ArrayList<Data> datas;
    private Date date;
    private double total_money;

    public DatasHistory(ArrayList<Data> datas, Date date, Double total_money) {
        this.datas = datas;
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

    public void setTotal_money(Double total_money) {
        this.total_money = total_money;
    }

    public ArrayList<Data> getDatas() {
        return datas;
    }

    public void setDatas(ArrayList<Data> datas) {
        this.datas = datas;
    }
}
