package com.rashata.jamie.spend.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.repository.database.Data;
import com.rashata.jamie.spend.repository.RealmManager;
import com.rashata.jamie.spend.util.DatasHistory;
import com.rashata.jamie.spend.util.History;
import com.rashata.jamie.spend.views.adapter.HistoryAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.functions.Action1;

public class HistoryActivity extends AppCompatActivity implements HistoryAdapter.ActivityListener {
    private HistoryAdapter historyAdapter;
    private RecyclerView rcc_history;
    private LinearLayout frm_nodata;
    private int current_type = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initInstances();
        loadData(0);
    }

    public void initInstances() {
        rcc_history = (RecyclerView) findViewById(R.id.rcc_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.history));
        rcc_history.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rcc_history.setLayoutManager(llm);
        frm_nodata = (LinearLayout) findViewById(R.id.frm_nodata);
        historyAdapter = new HistoryAdapter(this, this);
        rcc_history.setAdapter(historyAdapter);
    }

    public void loadData(int type) {
        final ArrayList<DatasHistory> datasHistories = new ArrayList<>();
        RealmManager.getInstance().getDataRepository().findAllData(type).subscribe(new Action1<List<Data>>() {
            @Override
            public void call(List<Data> datas) {
                ArrayList<History> histories = new ArrayList<History>();
                if (!datas.isEmpty()) {
                    Date date = datas.get(0).getDate();
                    double total = 0;
                    for (int i = 0; i < datas.size(); i++) {
                        Data data = datas.get(i);
                        History history = new History(data.getUuid(), data.getMoney(), data.getNote(), data.getCatagory(), data.getDate(), data.getType());
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
                    historyAdapter.setDatasHistories(datasHistories);
                }

                if (histories.isEmpty()) {
                    frm_nodata.setVisibility(View.VISIBLE);
                } else {
                    frm_nodata.setVisibility(View.GONE);
                }
                historyAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.transition_right_in, R.anim.transition_right_out);
                return true;
            case R.id.statistic:
                Intent intent = new Intent(this, StatisticActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.transition_left_in, R.anim.transition_left_out);
                return true;
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


    @Override
    public void onLoadData() {
        loadData(current_type);
    }
}
