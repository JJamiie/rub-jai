package com.geeksquad.jjamie.rubjai.views.activity;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.geeksquad.jjamie.rubjai.R;
import com.geeksquad.jjamie.rubjai.component.Injector;
import com.geeksquad.jjamie.rubjai.manager.Data;
import com.geeksquad.jjamie.rubjai.repository.DataRepository;
import com.geeksquad.jjamie.rubjai.util.History;
import com.geeksquad.jjamie.rubjai.views.adapter.HistoryAdapter;
import com.geeksquad.jjamie.rubjai.util.DatasHistory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class HistoryActivity extends AppCompatActivity {
    private static final String TAG = "HistoryActivity";
    private ArrayList<DatasHistory> datasHistories;
    private HistoryAdapter historyAdapter;
    @Bind(R.id.rcc_history) RecyclerView rcc_history;
    @Inject DataRepository dataRepository;
    @Bind(R.id.toolbar) Toolbar toolbar;
    private int current_type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);
        Injector.getApplicationComponent().inject(this);
        datasHistories = new ArrayList<>();
        loadData(0);
        setWidget();
    }

    public void setWidget() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("ประวัติการใช้");
        rcc_history.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rcc_history.setLayoutManager(llm);
        historyAdapter = new HistoryAdapter(datasHistories, dataRepository, this);
        rcc_history.setAdapter(historyAdapter);
    }

    public void loadData(int type) {
        datasHistories.clear();
        dataRepository.findAllData(type).subscribe(new Action1<List<Data>>() {
            @Override
            public void call(List<Data> datas) {
                ArrayList<History> histories = new ArrayList<History>();
                if (!datas.isEmpty()) {
                    Date date = datas.get(0).getDate();
                    double total = 0;
                    for (int i = 0; i < datas.size(); i++) {
                        Data data = datas.get(i);
                        History history = new History(data.getUuid(),data.getMoney(),data.getNote(),data.getCatagory(),data.getDate(),data.getType());
                        if (!compareDate(date, datas.get(i).getDate())) {
                            DatasHistory datasHistory = new DatasHistory(histories, date, total);
                            datasHistories.add(datasHistory);
                            date = datas.get(i).getDate();
                            total = 0;
                            histories = new ArrayList<History>();
                        }
                        if (datas.get(i).getType() == Data.TYPE_INCOME) {
                            total += datas.get(i).getMoney();
                        } else {
                            total -= datas.get(i).getMoney();
                        }
                        histories.add(history);
                    }
                    DatasHistory datasHistory = new DatasHistory(histories, date, total);
                    datasHistories.add(datasHistory);
                }

                if (historyAdapter != null) {
                    historyAdapter.notifyDataSetChanged();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_spinner, menu);
        MenuItem menuItem = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(menuItem);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_list_item_array, R.layout.drop_spinner_title);
        adapter.setDropDownViewResource(R.layout.drop_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        loadData(0);
                        setCurrent_type(0);
                        break;
                    case 1:
                        loadData(1);
                        setCurrent_type(1);
                        break;
                    case 2:
                        loadData(2);
                        setCurrent_type(2);
                        break;
                    case 3:
                        loadData(3);
                        setCurrent_type(3);
                        break;
                    case 4:
                        loadData(4);
                        setCurrent_type(4);
                        break;
                    default:
                        loadData(0);
                        setCurrent_type(0);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        return true;
    }

    public int getCurrent_type() {
        return current_type;
    }

    public void setCurrent_type(int current_type) {
        this.current_type = current_type;
    }
}
