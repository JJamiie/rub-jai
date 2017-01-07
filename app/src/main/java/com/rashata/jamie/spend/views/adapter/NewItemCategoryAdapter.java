package com.rashata.jamie.spend.views.adapter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.rashata.jamie.spend.Contextor;
import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.manager.Data;

/**
 * Created by jjamierashata on 1/1/2017 AD.
 */

public class NewItemCategoryAdapter extends RecyclerView.Adapter<NewItemCategoryAdapter.ItemViewHolder> {
    private final ActivityListener activityListener;
    private int[] data;
    private String nameCategory;
    private int clicked = -1;
    private int type;

    public interface ActivityListener {
        void onItemClicked(int position);
    }

    public NewItemCategoryAdapter(int type, ActivityListener activityListener) {
        this.activityListener = activityListener;
        this.type = type;
    }


    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = type == Data.TYPE_EXPENSE ? R.layout.grid_item_layout : R.layout.grid_item_layout_gray;
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ItemViewHolder ivh = new ItemViewHolder(view);
        return ivh;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        if (position == clicked) {
            int border = type == Data.TYPE_INCOME ? R.drawable.border_gray : R.drawable.border_sky;
            Drawable bg = ContextCompat.getDrawable(Contextor.getInstance().getContext(), border);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                holder.frm_item.setBackground(bg);
            } else {
                holder.frm_item.setBackgroundDrawable(bg);
            }
        } else {
            holder.frm_item.setBackgroundColor(Color.TRANSPARENT);
        }
        holder.image.setImageResource(data[position]);
        holder.text.setText(nameCategory);
        holder.frm_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked = position;
                activityListener.onItemClicked(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.length;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView text;
        FrameLayout frm_item;

        public ItemViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
            text = (TextView) view.findViewById(R.id.text);
            frm_item = (FrameLayout) view.findViewById(R.id.frm_item);
        }
    }

    public void setData(int[] data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
        notifyDataSetChanged();
    }

    public int[] getData() {
        return data;
    }

    public int getClicked() {
        return clicked;
    }

    public void setClicked(int clicked) {
        this.clicked = clicked;
        notifyDataSetChanged();
    }
}
