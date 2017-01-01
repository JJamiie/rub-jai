package com.rashata.jamie.spend.views.activity;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.manager.IncomeCategory;
import com.rashata.jamie.spend.repository.RealmManager;
import com.rashata.jamie.spend.util.CategoryItem;
import com.rashata.jamie.spend.views.adapter.ItemCategoryAdapter;
import com.rashata.jamie.spend.manager.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import xml.RubjaiWidget;

public class IncomeActivity extends AppCompatActivity implements ItemCategoryAdapter.ActivityListener {
    private double money;
    private int selected_catagory = -1;
    private EditText edt_money;
    private EditText edt_note;
    private Toolbar toolbar;
    private RecyclerView rec_item;
    private ItemCategoryAdapter itemCategoryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        setWidget();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    public void setWidget() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("รายรับ");
        edt_money = (EditText) findViewById(R.id.edt_money);
        edt_money.setRawInputType(Configuration.KEYBOARD_12KEY);
        edt_note = (EditText) findViewById(R.id.edt_note);
        rec_item = (RecyclerView) findViewById(R.id.rec_item);
        rec_item.setHasFixedSize(true);
        rec_item.setLayoutManager(new GridLayoutManager(this, 4));
        itemCategoryAdapter = new ItemCategoryAdapter(this);
        rec_item.setAdapter(itemCategoryAdapter);
        addAds();
    }

    public void addAds() {
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3754673556433244/6327931413");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void getData() {
        final ArrayList<CategoryItem> imageItems = new ArrayList<>();
        RealmManager.getInstance().getDataRepository().getIncomeCategory().subscribe(new Action1<List<IncomeCategory>>() {
            @Override
            public void call(List<IncomeCategory> datas) {
                if (!datas.isEmpty()) {
                    for (IncomeCategory incomeCategory : datas) {
                        int resId = getResources().getIdentifier(incomeCategory.getPicture(), "drawable", "com.rashata.jamie.spend");
                        imageItems.add(new CategoryItem(incomeCategory.getUuid(), resId, incomeCategory.getName(), incomeCategory.isShow()));
                    }
                }
            }
        });
        imageItems.add(new CategoryItem(-1, R.drawable.item_ic_setting_income, "แก้ไข", false));
        itemCategoryAdapter.setData(imageItems);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.transition_right_in, R.anim.transition_right_out);
                break;
            case R.id.correct:
                addToDatabase();
                break;
        }
        updateWidget();
        return true;
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
        RealmManager.getInstance().getDataRepository().addData(money, note, selected_catagory, new Date(), Data.TYPE_INCOME)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        finish();
                        overridePendingTransition(R.anim.transition_right_in, R.anim.transition_right_out);

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

    @Override
    public void onItemClicked(int position) {
        int idItem = itemCategoryAdapter.getData().get(position).getId();
        if (idItem == -1) {
            Intent intent = new Intent(getActivity(), ManageActivity.class);
            intent.putExtra("type", Data.TYPE_EXPENSE);
            startActivity(intent);
            overridePendingTransition(R.anim.transition_left_in, R.anim.transition_left_out);
        } else {
            selected_catagory = idItem;
            itemCategoryAdapter.setClicked(position);
        }
    }
}
