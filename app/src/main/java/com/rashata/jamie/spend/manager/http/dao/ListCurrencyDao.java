package com.rashata.jamie.spend.manager.http.dao;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by jjamierashata on 2/26/2017 AD.
 */


public class ListCurrencyDao {

    @Expose
    @SerializedName("base")
    private String base;
    @Expose
    @SerializedName("date")
    private String date;
    @Expose
    @SerializedName("rates")
    private Map<String,Double> rates;

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }
}
