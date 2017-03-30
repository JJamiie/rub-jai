package com.rashata.jamie.spend.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rashata.jamie.spend.R;

import java.util.ArrayList;


/**
 * Created by jjamierashata on 11/1/2016 AD.
 */

public class SpinnerDropdownAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<String> titles;

    public SpinnerDropdownAdapter(Context context, ArrayList<String> titles) {
        this.inflater = (LayoutInflater.from(context));
        this.titles = titles;
    }

    @Override
    public int getCount() {
        if (titles == null) return 0;
        return titles.size();
    }

    @Override
    public String getItem(int i) {
        return titles.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_dropdown, null);
        TextView txt_name = (TextView) view.findViewById(R.id.txt_title);
        txt_name.setText(titles.get(i));
        return view;
    }

}
