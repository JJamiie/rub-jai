package com.rashata.jamie.spend.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rashata.jamie.spend.Contextor;
import com.rashata.jamie.spend.R;


/**
 * Created by jjamierashata on 11/1/2016 AD.
 */

public class SpinnerLanguageDropdownAdapter extends BaseAdapter {
    private LayoutInflater inflter;
    private int[] flag = new int[]{R.drawable.flag_thai, R.drawable.flag_eng};
    private String[] language = new String[]{Contextor.getInstance().getContext().getString(R.string.thai)
            , Contextor.getInstance().getContext().getString(R.string.english)};

    public SpinnerLanguageDropdownAdapter(Context context) {
        this.inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return language.length;
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
        view = inflter.inflate(R.layout.item_lang_dropdown, null);
        TextView txt_name = (TextView) view.findViewById(R.id.txt_title);
        ImageView img_flag = (ImageView) view.findViewById(R.id.img_flag);
        txt_name.setText(language[i]);
        img_flag.setImageResource(flag[i]);
        return view;
    }

}
