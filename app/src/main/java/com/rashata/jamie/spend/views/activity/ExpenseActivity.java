package com.rashata.jamie.spend.views.activity;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.manager.ExpenseCategory;
import com.rashata.jamie.spend.repository.RealmManager;
import com.rashata.jamie.spend.util.CategoryItem;
import com.rashata.jamie.spend.views.adapter.ItemGridAdapter;
import com.rashata.jamie.spend.manager.Data;
import com.rashata.jamie.spend.views.adapter.ManageAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import xml.RubjaiWidget;

public class ExpenseActivity extends AppCompatActivity {

    private static final String TAG = "ExpenseActivity";
    private ItemGridAdapter itemGridAdapter;
    private int selected_catagory = -1;
    private GridView gridView;
    private EditText edt_money;
    private EditText edt_note;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        setWidget();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    public void setWidget() {
        gridView = (GridView) findViewById(R.id.gridView);
        edt_money = (EditText) findViewById(R.id.edt_money);
        edt_note = (EditText) findViewById(R.id.edt_note);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        edt_money.setRawInputType(Configuration.KEYBOARD_12KEY);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("รายจ่าย");
        itemGridAdapter = new ItemGridAdapter(this, R.layout.grid_item_layout);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int idItem = itemGridAdapter.getData().get(position).getId();
                if (idItem == -1) {
                    Intent intent = new Intent(getActivity(), ManageActivity.class);
                    intent.putExtra("type", Data.TYPE_EXPENSE);
                    startActivity(intent);
                } else {
                    selected_catagory = idItem;
                    itemGridAdapter.setClicked(position);
                    itemGridAdapter.notifyDataSetChanged();
                }

            }
        });
        gridView.setAdapter(itemGridAdapter);
    }

    private void getData() {
        final ArrayList<CategoryItem> imageItems = new ArrayList<>();
        RealmManager.getInstance().getDataRepository().getExpenseCategory().subscribe(new Action1<List<ExpenseCategory>>() {
            @Override
            public void call(List<ExpenseCategory> datas) {
                if (!datas.isEmpty()) {
                    for (ExpenseCategory expenseCategory : datas) {
                        int resId = getResources().getIdentifier(expenseCategory.getPicture(), "drawable", "com.rashata.jamie.spend");
                        imageItems.add(new CategoryItem(expenseCategory.getUuid(), resId, expenseCategory.getName(), expenseCategory.isShow()));
                    }
                }
            }
        });
        imageItems.add(new CategoryItem(-1, R.drawable.item_ic_setting_expense, "แก้ไข", false));
        itemGridAdapter.setData(imageItems);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                updateWidget();
                finish();
                return true;
            case R.id.correct:
                addToDatabase();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_expense_income, menu);
        return true;
    }

    private double money;

    public void addToDatabase() {
        String m = edt_money.getText().toString();
        if (!m.equals("")) {
            money = Double.parseDouble(m);
        } else {
            Toast.makeText(this, "กรุณาระบุจำนวนเงิน", Toast.LENGTH_LONG).show();
            return;
        }

        if (selected_catagory == -1) {
            Toast.makeText(this, "กรุณาเลือกชนิดหมวด", Toast.LENGTH_LONG).show();
            return;
        }
        final String note = edt_note.getText().toString();
        RealmManager.getInstance().getDataRepository().addData(money, note, selected_catagory, new Date(), Data.TYPE_EXPENSE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        updateWidget();
                        finish();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
