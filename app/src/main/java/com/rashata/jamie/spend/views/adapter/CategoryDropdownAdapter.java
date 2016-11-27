package com.rashata.jamie.spend.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.util.CategoryItem;

import java.util.ArrayList;


/**
 * Created by jjamierashata on 11/1/2016 AD.
 */

public class CategoryDropdownAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflter;
    private ArrayList<CategoryItem> imageItems;

    public CategoryDropdownAdapter(Context context, ArrayList<CategoryItem> imageItems) {
        this.context = context;
        this.inflter = (LayoutInflater.from(context));
        this.imageItems = imageItems;
    }


    @Override
    public int getCount() {
        return imageItems.size();
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
        view = inflter.inflate(R.layout.item_dropdown, null);
        TextView txt_name = (TextView) view.findViewById(R.id.txt_title);
        txt_name.setText(imageItems.get(i).getTitle());
        return view;
    }

    public void addTopOfItem(CategoryItem categoryItem) {
        imageItems.add(0, categoryItem);
        notifyDataSetChanged();
    }

    public ArrayList<CategoryItem> getImageItems() {
        return imageItems;
    }

    public void setImageItems(ArrayList<CategoryItem> imageItems) {
        this.imageItems = imageItems;
    }
}
