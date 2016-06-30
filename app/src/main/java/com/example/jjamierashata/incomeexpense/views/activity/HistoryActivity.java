package com.example.jjamierashata.incomeexpense.views.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.example.jjamierashata.incomeexpense.R;
import com.example.jjamierashata.incomeexpense.component.Injector;
import com.example.jjamierashata.incomeexpense.manager.Data;
import com.example.jjamierashata.incomeexpense.repository.DataRepository;
import com.example.jjamierashata.incomeexpense.views.adapter.HistoryAdapter;
import com.example.jjamierashata.incomeexpense.util.DatasHistory;
import com.example.jjamierashata.incomeexpense.util.History;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class HistoryActivity extends AppCompatActivity {
    private static final String TAG = "HistoryActivity";
    private ArrayList<DatasHistory> datasHistories;

    @Bind(R.id.rcc_history)
    RecyclerView rcc_history;
    @Inject
    DataRepository dataRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        Injector.getApplicationComponent().inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("ประวัติการใช้");
        loadData();
        setWidget();
    }

    public void setWidget() {
        rcc_history.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rcc_history.setLayoutManager(llm);
        HistoryAdapter historyAdapter = new HistoryAdapter(this, datasHistories);
        rcc_history.setAdapter(historyAdapter);
    }

    public void loadData() {
        datasHistories = new ArrayList<>();
        dataRepository.findAllData().subscribe(new Action1<List<Data>>() {
            @Override
            public void call(List<Data> datas) {
                ArrayList<Data> d = new ArrayList<Data>();
                if (!datas.isEmpty()) {
                    Date date = datas.get(0).getDate();
                    double total = 0;
                    for (int i = 0; i < datas.size(); i++) {
                        if (datas.get(i).getType() == Data.TYPE_INCOME) {
                            total += datas.get(i).getMoney();
                        } else {
                            total -= datas.get(i).getMoney();
                        }
                        d.add(datas.get(i));
                        if (!compareDate(date, datas.get(i).getDate()) || i == datas.size() - 1) {
                            DatasHistory datasHistory = new DatasHistory(d, date, total);
                            date = datas.get(i).getDate();
                            datasHistories.add(datasHistory);
                            total = 0;
                            d = new ArrayList<Data>();
                        }

                    }
                }

            }
        });
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

    public boolean compareDate(Date date, Date date2) {
        if (date.getDate() == date2.getDate() && date.getMonth() == date2.getMonth() && date.getYear() == date2.getYear()) {
            return true;
        }
        return false;

    }




}
