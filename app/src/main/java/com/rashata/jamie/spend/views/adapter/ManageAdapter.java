package com.rashata.jamie.spend.views.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.manager.Data;
import com.rashata.jamie.spend.util.CategoryItem;
import com.rashata.jamie.spend.util.ItemTouchHelperAdapter;
import com.rashata.jamie.spend.util.ItemTouchHelperViewHolder;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jjamierashata on 11/11/2016 AD.
 */

public class ManageAdapter extends RecyclerView.Adapter<ManageAdapter.ManageViewHolder> implements ItemTouchHelperAdapter {
    private int type;
    private ArrayList<CategoryItem> imageItems;
    private Activity activity;
    private OnDragStartListener mDragStartListener;


    public ManageAdapter(Activity activity, OnDragStartListener mDragStartListener, int type) {
        this.mDragStartListener = mDragStartListener;
        this.activity = activity;
        this.imageItems = new ArrayList<>();
        this.type = type;
    }

    @Override
    public ManageViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_item_layout_edit, viewGroup, false);
        ManageViewHolder qvh = new ManageViewHolder(v);
        return qvh;
    }

    @Override
    public void onBindViewHolder(final ManageViewHolder holder, final int position) {
        holder.cus_move.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEventCompat.getActionMasked(motionEvent) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onDragStarted(holder);
                }
                return false;
            }
        });
        holder.image.setImageResource(imageItems.get(position).getResId());
        holder.text.setText(imageItems.get(position).getTitle());
        if (type == Data.TYPE_EXPENSE) {
            holder.text.setTextColor(Color.parseColor("#89C4CA"));
        } else {
            holder.text.setTextColor(Color.parseColor("#989a9c"));
        }
        holder.btn_is_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if (imageItems.get(position).isShow()) holder.btn_is_show.setPressed(true);
        else holder.btn_is_show.setPressed(false);

        holder.btn_is_show.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                // show interest in events resulting from ACTION_DOWN
                if (event.getAction() == MotionEvent.ACTION_DOWN) return true;

                // don't handle event unless its ACTION_UP so "doSomething()" only runs once.
                if (event.getAction() != MotionEvent.ACTION_UP) return false;
                if (holder.btn_is_show.isPressed()) {
                    holder.btn_is_show.setPressed(false);
                    imageItems.get(position).setShow(false);
                } else {
                    holder.btn_is_show.setPressed(true);
                    imageItems.get(position).setShow(true);
                }
                return true;
            }
        });
    }

    public interface OnDragStartListener {
        void onDragStarted(RecyclerView.ViewHolder viewHolder);
    }

    @Override
    public int getItemCount() {
        return imageItems.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        Collections.swap(imageItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    public class ManageViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        private ImageView image;
        private TextView text;
        private Button btn_is_show;
        private ImageView cus_move;

        public ManageViewHolder(View itemView) {
            super(itemView);
            cus_move = (ImageView) itemView.findViewById(R.id.cus_move);
            image = (ImageView) itemView.findViewById(R.id.image);
            text = (TextView) itemView.findViewById(R.id.text);
            btn_is_show = (Button) itemView.findViewById(R.id.btn_is_show);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(Color.WHITE);
        }
    }

    public ArrayList<CategoryItem> getImageItems() {
        return imageItems;
    }

    public void setImageItems(ArrayList<CategoryItem> imageItems) {
        this.imageItems = imageItems;
        notifyDataSetChanged();
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void addCategory(CategoryItem categoryItem) {
        this.imageItems.add(categoryItem);
    }

}

