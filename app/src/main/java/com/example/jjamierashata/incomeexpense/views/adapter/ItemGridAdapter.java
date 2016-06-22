package com.example.jjamierashata.incomeexpense.views.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jjamierashata.incomeexpense.util.ImageItem;
import com.example.jjamierashata.incomeexpense.R;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jjamierashata on 5/23/16 AD.
 */
public class ItemGridAdapter extends ArrayAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList<ImageItem> data = new ArrayList<>();
    private int clicked = -1;

    public ItemGridAdapter(Context context, int layoutResourceId, ArrayList<ImageItem> data) {
        super(context, layoutResourceId, data);
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
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

        ImageItem item = data.get(position);
        if (position == clicked) {
            holder.item_grid.setBackgroundColor(Color.parseColor("#80e9e9e9"));
        } else {
            holder.item_grid.setBackgroundColor(Color.parseColor("#00000000"));
        }

        holder.imageTitle.setText(item.getTitle());
        holder.image.setImageResource(item.getResId());
        return row;
    }

    static class ViewHolder {
        @Bind(R.id.item_grid) LinearLayout item_grid;
        @Bind(R.id.text) TextView imageTitle;
        @Bind(R.id.image) ImageView image;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public int getClicked() {
        return clicked;
    }

    public void setClicked(int clicked) {
        this.clicked = clicked;
    }
}
