package com.example.jjamierashata.incomeexpense.views.activity;

import android.app.Activity;
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

import com.example.jjamierashata.incomeexpense.R;
import com.example.jjamierashata.incomeexpense.util.ImageItem;
import com.example.jjamierashata.incomeexpense.views.adapter.ItemGridAdapter;
import com.example.jjamierashata.incomeexpense.manager.Data;

import java.util.ArrayList;
import butterknife.Bind;
import butterknife.ButterKnife;

public class IncomeActivity extends AppCompatActivity {
    private ItemGridAdapter itemGridAdapter;
    private int item[] = {R.drawable.item_parent, R.drawable.item_salary, R.drawable.item_gift_gray, R.drawable.item_loan, R.drawable.item_sell, R.drawable.item_others_gray};
    private String itemTitle[] = {"พ่อแม่", "เงินเดือน", "ของขวัญ", "ยืม", "ขายของ", "อื่นๆ"};
    private double money;
    private int selected_catagory = -1;
    private Data data;
    @Bind(R.id.gridView) GridView gridView;
    @Bind(R.id.edt_money) EditText edt_money;
    @Bind(R.id.edt_note) EditText edt_note;
    @Bind(R.id.toolbar) Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        ButterKnife.bind(this);
        setWidget();
    }

    public void setWidget() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("รายรับ");
        itemGridAdapter = new ItemGridAdapter(this, R.layout.grid_item_layout_gray, getData());
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemGridAdapter.setClicked(position);
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
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                data = realm.createObject(Data.class);
//                data.setMoney(money);
//                data.setNote(note);
//                data.setCatagory(selected_catagory);
//                data.setDate(new Date());
//                data.setType(Data.TYPE_INCOME);
//            }
//        });
        finish();
    }

    public Activity getActivity() {
        return this;
    }
}
