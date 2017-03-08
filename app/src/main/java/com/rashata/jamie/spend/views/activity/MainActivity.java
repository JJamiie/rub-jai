package com.rashata.jamie.spend.views.activity;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.manager.Data;
import com.rashata.jamie.spend.repository.RealmManager;

import rx.functions.Action1;
import xml.RubjaiWidget;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView txt_summary;
    private TextView txt_summary_today;
    private Button btn_expense;
    private Button btn_income;
    private Button btn_setting;
    private Button btn_history;
    private TextView txt_today;
    private TextView txt_total;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    private void initInstances() {
        txt_today = (TextView) findViewById(R.id.txt_today);
        txt_today.setText(getString(R.string.today));
        txt_total = (TextView) findViewById(R.id.txt_total);
        txt_total.setText(getString(R.string.total));
        txt_summary = (TextView) findViewById(R.id.txt_summary);
        txt_summary_today = (TextView) findViewById(R.id.txt_summary_today);
        btn_expense = (Button) findViewById(R.id.btn_expense);
        btn_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExpenseIncomeActivity.class);
                intent.putExtra("type", Data.TYPE_EXPENSE);
                startActivity(intent);
                overridePendingTransition(R.anim.transition_left_in, R.anim.transition_left_out);
            }
        });
        btn_expense.setText(getString(R.string.expense));
        btn_income = (Button) findViewById(R.id.btn_income);
        btn_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExpenseIncomeActivity.class);
                intent.putExtra("type", Data.TYPE_INCOME);
                startActivity(intent);
                overridePendingTransition(R.anim.transition_left_in, R.anim.transition_left_out);
//                Intent intent = new Intent(MainActivity.this, IncomeActivity.class);
//                startActivity(intent);
//                overridePendingTransition(R.anim.transition_left_in, R.anim.transition_left_out);
            }
        });
        btn_income.setText(getString(R.string.income));
        btn_setting = (Button) findViewById(R.id.btn_setting);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.transition_left_in, R.anim.transition_left_out);
            }
        });
        btn_history = (Button) findViewById(R.id.btn_history);
        btn_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.transition_left_in, R.anim.transition_left_out);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initInstances();
        loadData();
    }

    public void loadData() {
        RealmManager.getInstance().getDataRepository().getSummaryToday().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                txt_summary_today.setText(s);
            }
        });

        RealmManager.getInstance().getDataRepository().getSummary().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                txt_summary.setText(s);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateWidget();
    }

    public Activity getActivity() {
        return this;
    }

    public void updateWidget() {
        Intent intent = new Intent(this, RubjaiWidget.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        int ids[] = AppWidgetManager.getInstance(getApplication()).getAppWidgetIds(new ComponentName(getApplication(), RubjaiWidget.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        sendBroadcast(intent);
    }


}
