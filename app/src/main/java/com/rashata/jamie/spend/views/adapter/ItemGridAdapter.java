package com.rashata.jamie.spend.views.adapter;

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

import com.rashata.jamie.spend.util.ImageItem;
import com.rashata.jamie.spend.R;

import java.util.ArrayList;


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
        LinearLayout item_grid;
        TextView imageTitle;
        ImageView image;

        public ViewHolder(View view) {
            item_grid = (LinearLayout) view.findViewById(R.id.item_grid);
            imageTitle = (TextView) view.findViewById(R.id.text);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }

    public int getClicked() {
        return clicked;
    }

    public void setClicked(int clicked) {
        this.clicked = clicked;
    }
}
