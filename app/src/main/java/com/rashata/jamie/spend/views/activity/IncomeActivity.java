package com.rashata.jamie.spend.views.activity;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.repository.DatabaseRealm;
import com.rashata.jamie.spend.util.ImageItem;
import com.rashata.jamie.spend.views.adapter.ItemGridAdapter;
import com.rashata.jamie.spend.manager.Data;

import java.util.ArrayList;
import java.util.Date;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import xml.RubjaiWidget;

public class IncomeActivity extends AppCompatActivity {
    private ItemGridAdapter itemGridAdapter;
    private double money;
    private int selected_catagory = -1;
    private Data data;
    private GridView gridView;
    private EditText edt_money;
    private EditText edt_note;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        setWidget();
    }

    public void setWidget() {
        gridView = (GridView) findViewById(R.id.gridView);
        edt_money = (EditText) findViewById(R.id.edt_money);
        edt_money.setRawInputType(Configuration.KEYBOARD_12KEY);
        edt_note = (EditText) findViewById(R.id.edt_note);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("รายรับ");
        itemGridAdapter = new ItemGridAdapter(this, R.layout.grid_item_layout_gray, getData());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemGridAdapter.setClicked(position);
                selected_catagory = position;
                itemGridAdapter.notifyDataSetChanged();
            }
        });
        gridView.setAdapter(itemGridAdapter);
    }

    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.id_list_income);
        String img_title[] = getResources().getStringArray(R.array.list_income);
        for (int i = 0; i < img_title.length; i++) {
            imageItems.add(new ImageItem(imgs.getResourceId(i, -1), img_title[i]));
        }
        return imageItems;
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
        DatabaseRealm.getInstance().getDataRepository().addData(money, note, selected_catagory, new Date(), Data.TYPE_INCOME)
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
