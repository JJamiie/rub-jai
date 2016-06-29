package com.example.jjamierashata.incomeexpense.views.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jjamierashata.incomeexpense.R;
import com.example.jjamierashata.incomeexpense.component.Injector;
import com.example.jjamierashata.incomeexpense.manager.Data;
import com.example.jjamierashata.incomeexpense.manager.Initial;
import com.example.jjamierashata.incomeexpense.repository.DataRepository;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Initial initial;
    private double initial_money;
    @Inject
    DataRepository dataRepository;
    @Bind(R.id.txt_summary)
    TextView txt_summary;
    @Bind(R.id.txt_summary_today)
    TextView txt_summary_today;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Injector.getApplicationComponent().inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    public void loadData() {
        dataRepository.getSummaryToday().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                txt_summary_today.setText(String.valueOf(s));
            }
        });

        dataRepository.getSummary().subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                txt_summary.setText(String.valueOf(s));
            }
        });
    }

    @OnClick(R.id.btn_expense)
    public void openExpenseActivity(View v) {
        Intent intent = new Intent(MainActivity.this, ExpenseActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_income)
    public void openIncomeActivity(View v) {
        Intent intent = new Intent(MainActivity.this, IncomeActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_setting)
    public void openSetting(View v) {
        showDialogSetting();
    }

    public void showDialogSetting() {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.settings_layout, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText edt_initial_money = (EditText) promptsView.findViewById(R.id.edt_initial_money);
        edt_initial_money.setRawInputType(Configuration.KEYBOARD_12KEY);

        dataRepository.getInitialMoney().subscribe(new Action1<Double>() {
            @Override
            public void call(Double aDouble) {
                if(aDouble == 0.00){
                    edt_initial_money.setText("");// set dialog message
                }else{
                    edt_initial_money.setText(aDouble.toString());// set dialog message
                }
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        String m = edt_initial_money.getText().toString();
                                        if (!m.equals("")) {
                                            initial_money = Double.parseDouble(m);
                                            dataRepository.setInitial(initial_money).subscribe();
                                            loadData();
                                        }
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });


    }

    @OnClick(R.id.btn_history)
    public void showHistory(View v) {
        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public Activity getActivity() {
        return this;
    }
}
