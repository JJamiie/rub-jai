package com.rashata.jamie.spend.views.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rashata.jamie.spend.Contextor;
import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.repository.RealmManager;
import com.rashata.jamie.spend.util.CategoryItem;

import java.util.ArrayList;

import rx.functions.Action1;

/**
 * Created by jjamierashata on 11/11/2016 AD.
 */

public class ManageStatisticCategoryAdapter extends RecyclerView.Adapter<ManageStatisticCategoryAdapter.ManageViewHolder> {
    private static final String TAG = "ManageStatisticCategoryAdapter";
    private ArrayList<CategoryItem> categoryItems;
    private Activity activity;
    private int idExpenseStatistic;
    private String titleCurrentStatistic;


    public ManageStatisticCategoryAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public ManageViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_statistic_category, viewGroup, false);
        ManageViewHolder qvh = new ManageViewHolder(v);
        return qvh;
    }

    @Override
    public void onBindViewHolder(final ManageViewHolder holder, final int position) {
        holder.image.setImageResource(categoryItems.get(position).getResId());
        holder.text.setText(categoryItems.get(position).getTitle());
        holder.txt_is_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (categoryItems.get(position).isShow()) {
                    categoryItems.get(position).setShow(false);
                    RealmManager.getInstance().getDataRepository().deleteExpenseCategory(categoryItems.get(position).getId()).subscribe();
                } else {
                    RealmManager.getInstance().getDataRepository().getTitleStatistic(categoryItems.get(position).getId()).subscribe(new Action1<String>() {
                        @Override
                        public void call(String title) {
                            if (title.isEmpty()) {
                                setIdExpenseStatisticInExpenseCategory(idExpenseStatistic, categoryItems.get(position).getId(), position);
                            } else {
                                showDialogConfirmChangeStatistic(idExpenseStatistic, categoryItems.get(position).getId(), position, title, categoryItems.get(position).getTitle());
                            }
                        }
                    });

                }
                notifyDataSetChanged();
            }
        });
        if (categoryItems.get(position).isShow()) {
            holder.txt_is_show.setTextColor(Color.WHITE);
            holder.txt_is_show.setBackgroundColor(Color.parseColor("#EFB7B6"));
        } else {
            holder.txt_is_show.setBackgroundColor(Color.parseColor("#fffafa"));
            holder.txt_is_show.setTextColor(Color.parseColor("#EFB7B6"));
        }

    }

    private void setIdExpenseStatisticInExpenseCategory(int idExpenseStatistic, int idCategory, int position) {
        categoryItems.get(position).setShow(true);
        RealmManager.getInstance().getDataRepository().addExpenseCategory(idExpenseStatistic, idCategory).subscribe();
    }

    @Override
    public int getItemCount() {
        if (categoryItems == null) return 0;
        return categoryItems.size();
    }

    public class ManageViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView text;
        private TextView txt_is_show;

        public ManageViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            text = (TextView) itemView.findViewById(R.id.text);
            txt_is_show = (TextView) itemView.findViewById(R.id.txt_is_show);

        }

    }

    private void showToast(String text) {
        Toast.makeText(Contextor.getInstance().getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public ArrayList<CategoryItem> getCategoryItems() {
        return categoryItems;
    }

    public void setCategoryItems(ArrayList<CategoryItem> categoryItems) {
        this.categoryItems = categoryItems;
        notifyDataSetChanged();
    }

    public Activity getActivity() {
        return activity;
    }

    public int getIdExpenseStatistic() {
        return idExpenseStatistic;
    }

    public void setIdExpenseStatistic(int idExpenseStatistic) {
        this.idExpenseStatistic = idExpenseStatistic;
    }

    public void showDialogConfirmChangeStatistic(final int idExpenseStatistic, final int idCategory, final int position, final String titleStatistic, final String titleCategory) {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.confirm_change, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        TextView txt_confirm = (TextView) promptsView.findViewById(R.id.txt_confirm);
        TextView txt_confirm2 = (TextView) promptsView.findViewById(R.id.txt_confirm2);
        txt_confirm.setText("คุณต้องการจะย้ายหมวด" + titleCategory);
        txt_confirm2.setText("จากสถิติ" + titleStatistic + "มาสู่สถิติ" + titleCurrentStatistic + "ใช่หรือไม่?");
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("ใช่",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                setIdExpenseStatisticInExpenseCategory(idExpenseStatistic, idCategory, position);
                                notifyDataSetChanged();
                            }
                        }).setNegativeButton("ไม่",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }
                });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public String getTitleCurrentStatistic() {
        return titleCurrentStatistic;
    }

    public void setTitleCurrentStatistic(String titleCurrentStatistic) {
        this.titleCurrentStatistic = titleCurrentStatistic;
    }
}

