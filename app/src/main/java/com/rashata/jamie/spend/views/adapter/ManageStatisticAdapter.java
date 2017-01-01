package com.rashata.jamie.spend.views.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.rashata.jamie.spend.Contextor;
import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.manager.Data;
import com.rashata.jamie.spend.manager.ExpenseCategory;
import com.rashata.jamie.spend.manager.ExpenseStatistic;
import com.rashata.jamie.spend.manager.IncomeCategory;
import com.rashata.jamie.spend.repository.RealmManager;
import com.rashata.jamie.spend.util.ArrayAdapterWithIcon;
import com.rashata.jamie.spend.util.CategoryItem;
import com.rashata.jamie.spend.util.Constants;
import com.rashata.jamie.spend.util.ItemTouchHelperAdapter;
import com.rashata.jamie.spend.util.ItemTouchHelperViewHolder;
import com.rashata.jamie.spend.util.StatisticItem;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;

import rx.functions.Action1;

/**
 * Created by jjamierashata on 11/11/2016 AD.
 */

public class ManageStatisticAdapter extends RecyclerView.Adapter<ManageStatisticAdapter.ManageViewHolder> {
    private static final String TAG = "ManageStatisticAdapter";
    private ArrayList<StatisticItem> statisticItems;
    private Activity activity;
    private OnStatisticClick onStatisticClick;


    public interface OnStatisticClick {
        void onStatisticClick(int id, String title);

        void onClearStatisticCategory();
    }

    public ManageStatisticAdapter(Activity activity, OnStatisticClick onStatisticClick) {
        this.activity = activity;
        this.onStatisticClick = onStatisticClick;
    }

    @Override
    public ManageViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_statistic, viewGroup, false);
        ManageViewHolder qvh = new ManageViewHolder(v);
        return qvh;
    }

    @Override
    public void onBindViewHolder(final ManageViewHolder holder, final int position) {
        holder.txt_title.setText(statisticItems.get(position).getTitle());
        holder.frm_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClicked(position);
                onStatisticClick.onStatisticClick(statisticItems.get(position).getId(), statisticItems.get(position).getTitle());
            }
        });
        holder.frm_back.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setClicked(position);
                onStatisticClick.onStatisticClick(statisticItems.get(position).getId(), statisticItems.get(position).getTitle());
                showDialogOption(statisticItems.get(position).getId(), statisticItems.get(position).getTitle(), position);
                return true;
            }
        });
        if (statisticItems.get(position).getClicked())
            holder.img_back.setImageResource(R.drawable.border_curve_pink);
        else
            holder.img_back.setImageResource(R.drawable.border_curve_blue);
    }

    @Override
    public int getItemCount() {
        if (statisticItems == null) return 0;
        return statisticItems.size();
    }

    public class ManageViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_title;
        private ImageView img_back;
        private FrameLayout frm_back;

        public ManageViewHolder(View itemView) {
            super(itemView);
            txt_title = (TextView) itemView.findViewById(R.id.txt_title);
            img_back = (ImageView) itemView.findViewById(R.id.img_back);
            frm_back = (FrameLayout) itemView.findViewById(R.id.frm_back);
        }
    }

    private void showToast(String text) {
        Toast.makeText(Contextor.getInstance().getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public ArrayList<StatisticItem> getStatisticItems() {
        return statisticItems;
    }

    public void setStatisticItems(ArrayList<StatisticItem> statisticItems) {
        this.statisticItems = statisticItems;
        notifyDataSetChanged();
    }

    public void setClicked(int position) {
        for (int i = 0; i < statisticItems.size(); i++) {
            if (position == i) statisticItems.get(i).setClicked(true);
            else statisticItems.get(i).setClicked(false);
        }
        notifyDataSetChanged();
    }

    private void showDialogEditStatistic(final int uuid, String title, final int position) {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.edit_statistic, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        final EditText edt_statistic = (EditText) promptsView.findViewById(R.id.edt_statistic);
        edt_statistic.setText(title);
        Button btn_cancel = (Button) promptsView.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        Button btn_add_statistic = (Button) promptsView.findViewById(R.id.btn_add_statistic);
        btn_add_statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String statistic = edt_statistic.getText().toString();
                if (statistic.isEmpty()) {
                    edt_statistic.setError("กรุณากรอกชื่อสถิติ");
                    edt_statistic.requestFocus();
                } else {
                    RealmManager.getInstance().getDataRepository().editExpenseStatistic(uuid, statistic).subscribe(new Action1<ExpenseStatistic>() {
                        @Override
                        public void call(ExpenseStatistic expenseStatistic) {
                            getStatisticItems().set(position, new StatisticItem(expenseStatistic.getUuid(), expenseStatistic.getTitle(), true));
                            notifyDataSetChanged();
                            alertDialog.dismiss();
                        }
                    });
                }
            }
        });
        alertDialog.show();
    }

    public void showDialogOption(final int uuid, final String title, final int position) {
        final String[] items = new String[]{"แก้ไขสถิติ", "ลบสถิติ"};
        ListAdapter adapter = new ArrayAdapterWithIcon(getActivity(), items);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        showDialogEditStatistic(uuid, title, position);
                        break;
                    case 1:
                        showDialogDeleteCategory(uuid, position);
                        break;
                }
            }
        });
        // set prompts.xml to alertdialog builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void showDialogDeleteCategory(final int uuid, final int position) {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.delete_statistic_layout, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("ใช่",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                RealmManager.getInstance().getDataRepository().deleteExpenseStatistic(uuid).subscribe();
                                statisticItems.remove(position);
                                if (position > 0) {
                                    setClicked(position - 1);
                                    onStatisticClick.onStatisticClick(statisticItems.get(position - 1).getId(), statisticItems.get(position - 1).getTitle());
                                } else {
                                    onStatisticClick.onClearStatisticCategory();
                                }
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

    public Activity getActivity() {
        return activity;
    }
}

