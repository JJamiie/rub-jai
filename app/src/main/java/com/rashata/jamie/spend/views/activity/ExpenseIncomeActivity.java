package com.rashata.jamie.spend.views.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.manager.Data;
import com.rashata.jamie.spend.manager.ExpenseCategory;
import com.rashata.jamie.spend.manager.IncomeCategory;
import com.rashata.jamie.spend.repository.RealmManager;
import com.rashata.jamie.spend.util.CategoryItem;
import com.rashata.jamie.spend.util.RubjaiPreference;
import com.rashata.jamie.spend.views.adapter.ItemCategoryAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import xml.RubjaiWidget;

import static com.rashata.jamie.spend.manager.Data.TYPE_EXPENSE;
import static com.rashata.jamie.spend.manager.Data.TYPE_INCOME;


public class ExpenseIncomeActivity extends AppCompatActivity implements ItemCategoryAdapter.ActivityListener {
    private static final String TAG = "ExpenseIncomeActivity";
    private static final int TYPE_CALCULATOR = 11;
    private int selected_catagory = -1;
    private EditText edt_money;
    private EditText edt_note;
    private TextView txt_currency;
    private RecyclerView rec_item;
    private ItemCategoryAdapter itemCategoryAdapter;
    private ImageButton btn_calendar;
    private Calendar calendar;
    private int type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInstances();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    public void initInstances() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        Intent intent = getIntent();
        type = intent.getIntExtra("type", -1);
        if (type == Data.TYPE_EXPENSE) {
            setContentView(R.layout.activity_expense);
            getSupportActionBar().setTitle(getResources().getString(R.string.expense));
            itemCategoryAdapter = new ItemCategoryAdapter(this, Data.TYPE_EXPENSE);
        } else if (type == TYPE_INCOME) {
            setContentView(R.layout.activity_income);
            getSupportActionBar().setTitle(getResources().getString(R.string.income));
            itemCategoryAdapter = new ItemCategoryAdapter(this, Data.TYPE_INCOME);
        }
        txt_currency = (TextView) findViewById(R.id.txt_currency);
        txt_currency.setText(new RubjaiPreference(this).currency);
        rec_item = (RecyclerView) findViewById(R.id.rec_item);
        rec_item.setHasFixedSize(true);
        rec_item.setLayoutManager(new GridLayoutManager(this, 4));
        rec_item.setAdapter(itemCategoryAdapter);
        edt_money = (EditText) findViewById(R.id.edt_money);
        edt_note = (EditText) findViewById(R.id.edt_note);
        edt_money.setRawInputType(Configuration.KEYBOARD_12KEY);
        calendar = Calendar.getInstance();
        btn_calendar = (ImageButton) findViewById(R.id.btn_calendar);
        btn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChooseDate();
            }
        });
        addAds();
    }

    private void getData() {
        final ArrayList<CategoryItem> imageItems = new ArrayList<>();
        if (type == Data.TYPE_EXPENSE) {
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
            imageItems.add(new CategoryItem(-1, R.drawable.item_ic_setting_expense, getString(R.string.edit), false));
        } else if (type == Data.TYPE_INCOME) {
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
            imageItems.add(new CategoryItem(-1, R.drawable.item_ic_setting_income, getString(R.string.edit), false));
        }
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
            case R.id.calculator:
                Intent intent = new Intent(getActivity(), CalculatorActivity.class);
                overridePendingTransition(R.anim.transition_right_in, R.anim.transition_right_out);
                startActivityForResult(intent, TYPE_CALCULATOR);
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

    private double money;

    public void addToDatabase() {
        String m = edt_money.getText().toString();
        if (!m.equals("")) {
            money = Double.parseDouble(m);
        } else {
            edt_money.setError(getString(R.string.please_fill_money));
            edt_money.requestFocus();
            return;
        }

        if (selected_catagory == -1) {
            Toast.makeText(this, getString(R.string.please_choose_category_name), Toast.LENGTH_LONG).show();
            return;
        }
        final String note = edt_note.getText().toString();
        if (type == Data.TYPE_EXPENSE) {
            RealmManager.getInstance().getDataRepository().addData(money, note, selected_catagory, calendar.getTime(), TYPE_EXPENSE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Integer>() {
                        @Override
                        public void call(Integer integer) {
                            finish();
                            overridePendingTransition(R.anim.transition_right_in, R.anim.transition_right_out);
                        }
                    });
        } else if (type == Data.TYPE_INCOME) {
            RealmManager.getInstance().getDataRepository().addData(money, note, selected_catagory, calendar.getTime(), TYPE_INCOME)
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

    private void showDialogChooseDate() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.cus_dialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

            }
        });
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                int loadedOrientation = getActivity().getResources().getConfiguration().orientation;
                int requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
                if (loadedOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                } else if (loadedOrientation == Configuration.ORIENTATION_PORTRAIT) {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                }
                getActivity().setRequestedOrientation(requestedOrientation);
            }
        });
        datePickerDialog.show();
    }


    @Override
    public void onItemClicked(int position) {
        int idItem = itemCategoryAdapter.getData().get(position).getId();
        if (idItem == -1) { // setting
            Intent intent = new Intent(getActivity(), ManageActivity.class);
            if (type == Data.TYPE_EXPENSE) {
                intent.putExtra("type", Data.TYPE_EXPENSE);
            } else if (type == Data.TYPE_INCOME) {
                intent.putExtra("type", Data.TYPE_INCOME);
            }
            startActivity(intent);
            overridePendingTransition(R.anim.transition_left_in, R.anim.transition_left_out);
        } else {
            selected_catagory = idItem;
            itemCategoryAdapter.setClicked(position);
        }
    }

    public void addAds() {
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3754673556433244/6327931413");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TYPE_CALCULATOR && resultCode == RESULT_OK) {
            String result = data.getStringExtra("result");
            edt_money.setText(result);
        }
    }
}
