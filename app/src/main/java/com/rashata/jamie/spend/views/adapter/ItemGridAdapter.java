package com.rashata.jamie.spend.views.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rashata.jamie.spend.util.CategoryItem;
import com.rashata.jamie.spend.R;

import java.util.ArrayList;


/**
 * Created by jjamierashata on 5/23/16 AD.
 */
public class ItemGridAdapter extends BaseAdapter {
    private Context context;
    private int layoutResourceId;
    private ArrayList<CategoryItem> data;
    private int clicked = -1;

    public ItemGridAdapter(Context context, int layoutResourceId) {
        this.context = context;
        this.layoutResourceId = layoutResourceId;
    }

    @Override
    public int getCount() {
        if (data == null) return 0;
        return data.size();
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

        CategoryItem item = data.get(position);
        if (position == clicked) {
            holder.border.setVisibility(View.VISIBLE);
        } else {
            holder.border.setVisibility(View.GONE);
        }
        if (item.getId() == -1) {
            holder.item_grid.setBackgroundColor(Color.parseColor("#80e9e9e9"));
        } else {
            holder.item_grid.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        holder.imageTitle.setText(item.getTitle());
        holder.image.setImageResource(item.getResId());
        return row;
    }

    public class ViewHolder {
        LinearLayout item_grid;
        TextView imageTitle;
        ImageView image;
        View border;

        public ViewHolder(View view) {
            item_grid = (LinearLayout) view.findViewById(R.id.item_grid);
            imageTitle = (TextView) view.findViewById(R.id.text);
            image = (ImageView) view.findViewById(R.id.image);
            border = view.findViewById(R.id.border);
        }
    }

    public int getClicked() {
        return clicked;
    }

    public void setClicked(int clicked) {
        this.clicked = clicked;
    }

    public ArrayList<CategoryItem> getData() {
        return data;
    }

    public void setData(ArrayList<CategoryItem> datas) {
        this.data = datas;
        notifyDataSetChanged();
    }


}
