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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rashata.jamie.spend.Contextor;
import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.repository.database.Data;
import com.rashata.jamie.spend.util.CategoryItem;

import java.util.ArrayList;

/**
 * Created by jjamierashata on 1/1/2017 AD.
 */

public class  ItemCategoryAdapter extends RecyclerView.Adapter<ItemCategoryAdapter.ItemViewHolder> {
    private ArrayList<CategoryItem> data;
    private int clicked = -1;
    private ActivityListener activityListener;
    private int type;


    public ItemCategoryAdapter(ActivityListener activityListener, int type) {
        this.activityListener = activityListener;
        this.type = type;
    }

    public interface ActivityListener {
        void onItemClicked(int position);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = type == Data.TYPE_INCOME ? R.layout.grid_item_layout_gray : R.layout.grid_item_layout;
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        ItemViewHolder ivh = new ItemViewHolder(view);
        return ivh;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        CategoryItem item = data.get(position);
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

        if (item.getId() == -1) {
            holder.item_grid.setBackgroundColor(Color.parseColor("#80e9e9e9"));
        } else {
            holder.item_grid.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        holder.imageTitle.setText(item.getTitle());
        holder.image.setImageResource(item.getResId());
        holder.frm_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityListener.onItemClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (data == null) return 0;
        return data.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item_grid;
        TextView imageTitle;
        ImageView image;
        FrameLayout frm_item;

        public ItemViewHolder(View itemView) {
            super(itemView);
            item_grid = (LinearLayout) itemView.findViewById(R.id.item_grid);
            imageTitle = (TextView) itemView.findViewById(R.id.text);
            image = (ImageView) itemView.findViewById(R.id.image);
            frm_item = (FrameLayout) itemView.findViewById(R.id.frm_item);
        }
    }

    public ArrayList<CategoryItem> getData() {
        return data;
    }

    public void setData(ArrayList<CategoryItem> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public int getClicked() {
        return clicked;
    }

    public void setClicked(int clicked) {
        this.clicked = clicked;
        notifyDataSetChanged();
    }
}
