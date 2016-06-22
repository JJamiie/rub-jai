package com.example.jjamierashata.incomeexpense.util;

import java.util.ArrayList;

/**
 * Created by jjamierashata on 5/31/16 AD.
 */
public class DatasHistory {
    private ArrayList<History> history;
    private String date;
    private Double total_money;

    public DatasHistory(ArrayList<History> history, String date, Double total_money) {
        this.history = history;
        this.date = date;
        this.total_money = total_money;
    }

    public String getDate() {
        return date;
    }

    public Double getTotal_money() {
        return total_money;
    }

    public ArrayList<History> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<History> history) {
        this.history = history;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTotal_money(Double total_money) {
        this.total_money = total_money;
    }
}
