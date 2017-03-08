package com.rashata.jamie.spend.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rashata.jamie.spend.R;


/**
 * Created by jjamierashata on 11/1/2016 AD.
 */

public class SpinnerDropdownPictureAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    public int[] pictures;
    public String[] titles;

    public SpinnerDropdownPictureAdapter(Context context) {
        this.inflater = (LayoutInflater.from(context));
    }

    public SpinnerDropdownPictureAdapter(Context context, int[] pictures, String[] titles) {
        this.inflater = (LayoutInflater.from(context));
        this.pictures = pictures;
        this.titles = titles;
    }

    @Override
    public int getCount() {
        return pictures.length;
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
        view = inflater.inflate(R.layout.item_lang_dropdown, null);
        TextView txt_name = (TextView) view.findViewById(R.id.txt_title);
        ImageView img_flag = (ImageView) view.findViewById(R.id.img_flag);
        txt_name.setText(titles[i]);
        img_flag.setImageResource(pictures[i]);
        return view;
    }

    public int[] getPictures() {
        return pictures;
    }

    public void setPictures(int[] pictures) {
        this.pictures = pictures;
    }

    public String[] getTitles() {
        return titles;
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
    }
}
