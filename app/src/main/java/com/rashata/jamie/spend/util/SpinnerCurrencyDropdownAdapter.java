package com.rashata.jamie.spend.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rashata.jamie.spend.R;

import java.util.Map;


/**
 * Created by jjamierashata on 11/1/2016 AD.
 */

public class SpinnerCurrencyDropdownAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    public Map<String, Double> data;
    private String[] mKeys;


    public SpinnerCurrencyDropdownAdapter(Context context, Map<String, Double> data) {
        this.inflater = (LayoutInflater.from(context));
        this.data = data;
        this.mKeys = data.keySet().toArray(new String[data.size()]);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_dropdown_currency, null);
        TextView txt_name = (TextView) view.findViewById(R.id.txt_title);
        txt_name.setText(mKeys[i]);
        return view;
    }

    public Map<String, Double> getData() {
        return data;
    }

    public void setData(Map<String, Double> data) {
        this.data = data;
    }

    public String[] getmKeys() {
        return mKeys;
    }

    public void setmKeys(String[] mKeys) {
        this.mKeys = mKeys;
    }
}
