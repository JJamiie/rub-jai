package com.example.jjamierashata.incomeexpense.views.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.jjamierashata.incomeexpense.R;
import com.example.jjamierashata.incomeexpense.component.Injector;
import com.example.jjamierashata.incomeexpense.repository.DataRepository;
import com.example.jjamierashata.incomeexpense.util.ImageItem;
import com.example.jjamierashata.incomeexpense.views.adapter.ItemGridAdapter;
import com.example.jjamierashata.incomeexpense.manager.Data;

import java.util.ArrayList;
import java.util.Date;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ExpenseActivity extends AppCompatActivity {

    private static final String TAG = "ExpenseActivity";
    private ItemGridAdapter itemGridAdapter;
    private int item[] = {R.drawable.item_bus, R.drawable.item_food, R.drawable.item_shirt, R.drawable.item_movie,
            R.drawable.item_health, R.drawable.item_lover, R.drawable.item_party, R.drawable.item_shopping,
            R.drawable.item_gift, R.drawable.item_electric, R.drawable.item_water, R.drawable.item_phone,
            R.drawable.item_pet, R.drawable.item_run, R.drawable.item_family, R.drawable.item_bed, R.drawable.item_others};
    public static String itemTitle[] = {"เดินทาง", "อาหาร", "เสื้อผ้า", "ดูหนัง",
                                        "รักษาตัว", "คนรัก", "ปาร์ตี้", "ช๊อปปิ้ง",
                                         "ของขวัญ", "ค่าไฟ", "ค่าน้ำ", "โทรศัพท์",
                                        "สัตว์เลี้ยง", "กีฬา", "ครอบครัว", "ที่พัก", "อื่นๆ"};
    private int selected_catagory = -1;
    @Bind(R.id.gridView) GridView gridView;
    @Bind(R.id.edt_money) EditText edt_money;
    @Bind(R.id.edt_note) EditText edt_note;
    @Bind(R.id.toolbar) Toolbar toolbar;

    @Inject
    DataRepository dataRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        ButterKnife.bind(this);
        Injector.getApplicationComponent().inject(this);
        edt_money.setRawInputType(Configuration.KEYBOARD_12KEY);
        setWidget();
    }

    public void setWidget() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("รายจ่าย");
        itemGridAdapter = new ItemGridAdapter(this, R.layout.grid_item_layout, getData());
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
        for (int i = 0; i < item.length; i++) {
            imageItems.add(new ImageItem(item[i], itemTitle[i]));
        }
        return imageItems;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
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
        dataRepository.addData(money,note,item[selected_catagory],itemTitle[selected_catagory],new Date(),Data.TYPE_EXPENSE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
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
}
