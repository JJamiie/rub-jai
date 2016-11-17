package com.rashata.jamie.spend.views.adapter;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.manager.Data;
import com.rashata.jamie.spend.repository.DataRepository;
import com.rashata.jamie.spend.util.DatasHistory;
import com.rashata.jamie.spend.util.History;
import com.rashata.jamie.spend.views.activity.HistoryActivity;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import xml.RubjaiWidget;

/**
 * Created by jjamierashata on 5/30/16 AD.
 */
public class HistoryAdapter extends SectionedRecyclerViewAdapter<HistoryHeaderViewHolder, HistoryItemViewHolder, HistoryFooterViewHolder> {
    DataRepository dataRepository;
    private static final String TAG = "HistoryAdapter";
    private ArrayList<DatasHistory> datasHistories;
    private HistoryActivity historyActivity;
    public String date_thai[] = {"วันอาทิตย์ที่", "วันจันทร์ที่", "วันอังคารที่", "วันพุธที่", "วันพฤหัสบดีที่", "วันศุกร์่", "วันเสาร์ที่"};
    public String month_thai[] = {"มกราคม", "กุมภาพันธ์", "มีนาคม", "เมษายน", "พฤษภาคม", "มิถุนายน", "กรกฎาคม", "สิงหาคม", "กันยายน", "ตุลาคม", "พฤศจิกายน", "ธันวาคม"};


    public HistoryAdapter(ArrayList<DatasHistory> datasHistories, DataRepository dataRepository, HistoryActivity historyActivity) {
        this.historyActivity = historyActivity;
        this.datasHistories = datasHistories;
        this.dataRepository = dataRepository;
    }


    @Override
    protected int getSectionCount() {
        return datasHistories.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        return datasHistories.get(section).getHistories().size();
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected HistoryHeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.item_history_header, parent, false);
        return new HistoryHeaderViewHolder(view);
    }

    @Override
    protected HistoryFooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected HistoryItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getLayoutInflater().inflate(R.layout.item_history, parent, false);
        return new HistoryItemViewHolder(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(HistoryHeaderViewHolder holder, int section) {
        Date date = datasHistories.get(section).getDate();
        holder.txt_date.setText(date_thai[date.getDay()] + " " + date.getDate() + " " + month_thai[date.getMonth()] + " " + (date.getYear() + 1900 + 543));
        if (datasHistories.get(section).getTotal_money() >= 0) {
            holder.txt_total_money.setTextColor(Color.parseColor("#888b8f"));
            holder.txt_total_money.setText("+" + String.format("%.2f", datasHistories.get(section).getTotal_money()));
        } else {
            holder.txt_total_money.setTextColor(Color.parseColor("#EF5350"));
            holder.txt_total_money.setText(String.format("%.2f", datasHistories.get(section).getTotal_money()));
        }

    }

    @Override
    protected void onBindSectionFooterViewHolder(HistoryFooterViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(HistoryItemViewHolder holder, final int section, final int position) {
        int catagory = datasHistories.get(section).getHistories().get(position).getCatagory();
        TypedArray imgs;
        if (datasHistories.get(section).getHistories().get(position).getType() == Data.TYPE_EXPENSE) {
            holder.txt_history_money.setTextColor(Color.parseColor("#89C4CA"));
            holder.txt_history_money.setText(String.format("%.2f", datasHistories.get(section).getHistories().get(position).getMoney()));
            holder.txt_history_title.setText(getContext().getResources().getTextArray(R.array.list_expense)[catagory]);
            imgs = getContext().getResources().obtainTypedArray(R.array.id_list_expense);
        } else {
            holder.txt_history_money.setTextColor(Color.parseColor("#9e9e9e"));
            holder.txt_history_money.setText("+" + String.format("%.2f", datasHistories.get(section).getHistories().get(position).getMoney()));
            holder.txt_history_title.setText(getContext().getResources().getTextArray(R.array.list_income)[catagory]);
            imgs = getContext().getResources().obtainTypedArray(R.array.id_list_income);
        }
        holder.img_history.setImageResource(imgs.getResourceId(catagory, -1));
        holder.txt_history_note.setText(datasHistories.get(section).getHistories().get(position).getNote());
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogDelete(datasHistories.get(section).getHistories().get(position).getUuid());
            }
        });
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogEdit(datasHistories.get(section).getHistories().get(position));
            }
        });
    }

    protected LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(getContext());
    }

    public void showDialogDelete(final int uuid) {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.delete_layout, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("ใช่",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dataRepository.deleteData(uuid).subscribe();
                                historyActivity.loadData(historyActivity.getCurrent_type());
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

    public void showDialogEdit(final History data) {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(getContext());
        View promptsView = li.inflate(R.layout.edit_layout, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        final EditText edt_money = (EditText) promptsView.findViewById(R.id.edt_money);
        final EditText edt_note = (EditText) promptsView.findViewById(R.id.edt_note);
        final Spinner spinner = (Spinner) promptsView.findViewById(R.id.spin_type);

        edt_money.setText((String.format("%.2f", data.getMoney())));
        edt_note.setText(data.getNote());

        int id_list;
        if (data.getType() == Data.TYPE_EXPENSE) {
            id_list = R.array.list_expense;
        } else {
            id_list = R.array.list_income;
        }
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                id_list, R.layout.drop_spinner_title_edit);
        adapter.setDropDownViewResource(R.layout.drop_spinner_item_edit);
        spinner.setAdapter(adapter);
        spinner.setSelection(data.getCatagory());
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("แก้ไข",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                int position = spinner.getSelectedItemPosition();
                                if (data.getType() == Data.TYPE_EXPENSE) {
                                    dataRepository.editData(data.getUuid(), Double.parseDouble(edt_money.getText().toString()), edt_note.getText().toString(),
                                            position, data.getDate(), data.getType()).subscribe();
                                } else {
                                    dataRepository.editData(data.getUuid(), Double.parseDouble(edt_money.getText().toString()), edt_note.getText().toString(),
                                            position, data.getDate(), data.getType()).subscribe();
                                }
                                historyActivity.loadData(historyActivity.getCurrent_type());
                            }
                        })
                .setNegativeButton("ยกเลิก",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }

    public Context getContext() {
        return historyActivity;
    }
}


class HistoryItemViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.txt_history_title)
    TextView txt_history_title;
    @Bind(R.id.txt_history_note)
    TextView txt_history_note;
    @Bind(R.id.txt_history_money)
    TextView txt_history_money;
    @Bind(R.id.img_history)
    ImageView img_history;
    @Bind(R.id.btn_delete)
    ImageButton btn_delete;
    @Bind(R.id.btn_edit)
    ImageButton btn_edit;
    @Bind(R.id.v_front)
    View v_front;

    public HistoryItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

    }
}

class HistoryHeaderViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.txt_date)
    TextView txt_date;
    @Bind(R.id.txt_total_money)
    TextView txt_total_money;


    public HistoryHeaderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}


class HistoryFooterViewHolder extends RecyclerView.ViewHolder {

    public HistoryFooterViewHolder(View itemView) {
        super(itemView);


    }
}




