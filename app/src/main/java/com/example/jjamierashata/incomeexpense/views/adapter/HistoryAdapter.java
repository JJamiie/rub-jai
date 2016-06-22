package com.example.jjamierashata.incomeexpense.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jjamierashata.incomeexpense.R;
import com.example.jjamierashata.incomeexpense.util.DatasHistory;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by jjamierashata on 5/30/16 AD.
 */
public class HistoryAdapter extends SectionedRecyclerViewAdapter<HistoryHeaderViewHolder, HistoryItemViewHolder, HistoryFooterViewHolder> {
    private Context context;
    private ArrayList<DatasHistory> datasHistories;


    public HistoryAdapter(Context context, ArrayList<DatasHistory> datasHistories) {
        this.context = context;
        this.datasHistories = datasHistories;
    }


    @Override
    protected int getSectionCount() {
        return datasHistories.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        return datasHistories.get(section).getHistory().size();
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
        holder.txt_date.setText(datasHistories.get(section).getDate());
        holder.txt_total_money.setText(datasHistories.get(section).getTotal_money().toString());
    }

    @Override
    protected void onBindSectionFooterViewHolder(HistoryFooterViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(HistoryItemViewHolder holder, int section, int position) {
        holder.txt_history_money.setText(datasHistories.get(section).getHistory().get(position).getMoney() + "");
        holder.txt_history_note.setText(datasHistories.get(section).getHistory().get(position).getNote());
        holder.txt_history_title.setText(datasHistories.get(section).getHistory().get(position).getTitle());
        holder.img_history.setImageResource(datasHistories.get(section).getHistory().get(position).getImg());
    }

    protected LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(context);
    }
}

class HistoryItemViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.txt_history_title) TextView txt_history_title;
    @Bind(R.id.txt_history_note) TextView txt_history_note;
    @Bind(R.id.txt_history_money)TextView txt_history_money;
    @Bind(R.id.img_history)ImageView img_history;

    public HistoryItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);

    }
}

class HistoryHeaderViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.txt_date) TextView txt_date;
    @Bind(R.id.txt_total_money) TextView txt_total_money;

    public HistoryHeaderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }
}


class HistoryFooterViewHolder extends RecyclerView.ViewHolder {


    public HistoryFooterViewHolder(View itemView) {
        super(itemView);


    }
}
