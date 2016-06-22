package com.example.jjamierashata.incomeexpense.views.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.jjamierashata.incomeexpense.R;
import com.example.jjamierashata.incomeexpense.views.adapter.HistoryAdapter;
import com.example.jjamierashata.incomeexpense.util.DatasHistory;
import com.example.jjamierashata.incomeexpense.util.History;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private ArrayList<DatasHistory> datasHistories;
    private RecyclerView rcc_history;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("ประวัติการใช้");
        bindWidget();
    }

    public void bindWidget() {
        initialTemplate();
        rcc_history = (RecyclerView) findViewById(R.id.rcc_history);
        rcc_history.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rcc_history.setLayoutManager(llm);
        HistoryAdapter historyAdapter = new HistoryAdapter(this, datasHistories);
        rcc_history.setAdapter(historyAdapter);
    }

    public void initialTemplate() {
        datasHistories = new ArrayList<>();
        History history = new History();
        history.setImg(R.drawable.item_family);
        history.setMoney(100.0);
        history.setNote("นั่งรถจนเบื่อ");
        history.setTitle("เดินทาง");
        ArrayList<History> histories = new ArrayList<>();
        histories.add(history);
        histories.add(history);
        histories.add(history);
        DatasHistory datasHistory = new DatasHistory(histories, "Mon , May 29, 2016", 275.0);
        datasHistories.add(datasHistory);
        datasHistories.add(datasHistory);
        datasHistories.add(datasHistory);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
