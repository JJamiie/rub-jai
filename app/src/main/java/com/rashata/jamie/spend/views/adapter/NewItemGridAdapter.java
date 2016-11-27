package com.rashata.jamie.spend.views.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.manager.Data;
import com.rashata.jamie.spend.util.CategoryItem;

import java.util.ArrayList;


/**
 * Created by jjamierashata on 5/23/16 AD.
 */
public class NewItemGridAdapter extends BaseAdapter {
    private Context context;
    private int layoutResourceId;
    private int[] data;
    private int clicked = -1;
    private String nameCategory;

    public NewItemGridAdapter(Context context, int layoutResourceId) {
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.nameCategory = "";
    }

    @Override
    public int getCount() {
        if (data == null) return 0;
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;
        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        if (position == clicked) {
            holder.border.setVisibility(View.VISIBLE);
        } else {
            holder.border.setVisibility(View.GONE);
        }
        holder.image.setImageResource(data[position]);
        holder.text.setText(nameCategory);
        return row;
    }

    public class ViewHolder {
        ImageView image;
        View border;
        TextView text;

        public ViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.image);
            border = view.findViewById(R.id.border);
            text = (TextView) view.findViewById(R.id.text);
        }
    }

    public int getClicked() {
        return clicked;
    }

    public void setClicked(int clicked) {
        this.clicked = clicked;
        notifyDataSetChanged();
    }


    public void setData(int[] datas) {
        this.data = datas;
        notifyDataSetChanged();
    }

    public int[] getData() {
        return data;
    }

    public String getText() {
        return nameCategory;
    }

    public void setText(String text) {
        this.nameCategory = text;
        notifyDataSetChanged();
    }
}
