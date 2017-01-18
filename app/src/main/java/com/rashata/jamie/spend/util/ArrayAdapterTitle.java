package com.rashata.jamie.spend.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rashata.jamie.spend.Contextor;
import com.rashata.jamie.spend.R;

import java.util.Arrays;
import java.util.List;


/**
 * Created by jjamierashata on 10/12/2016 AD.
 */

public class ArrayAdapterTitle extends ArrayAdapter<String> {
    private List<String> items;
    private ViewHolder holder;


    public ArrayAdapterTitle(Context context, List<String> items) {
        super(context, android.R.layout.select_dialog_item, items);
    }

    public ArrayAdapterTitle(Context context, String[] items) {
        super(context, android.R.layout.select_dialog_item, items);
        this.items = Arrays.asList(items);
    }


    class ViewHolder {
        TextView title;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LayoutInflater inflater = (LayoutInflater) Contextor.getInstance().getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, null);

            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(items.get(position));
        return convertView;
    }

}