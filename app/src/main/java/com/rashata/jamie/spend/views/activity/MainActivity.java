package com.rashata.jamie.spend.views.activity;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.rashata.jamie.spend.Contextor;
import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.manager.Initial;
import com.rashata.jamie.spend.repository.RealmManager;
import com.rashata.jamie.spend.util.RubjaiPreference;
import com.rashata.jamie.spend.util.SpinnerLanguageDropdownAdapter;

import java.util.Locale;

import rx.functions.Action1;
import xml.RubjaiWidget;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private Initial initial;
    private double initial_money;
    private TextView txt_summary;
    private TextView txt_summary_today;
    private Button btn_expense;
    private Button btn_income;
    private Button btn_setting;
    private Button btn_history;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setWidget();
    }

    private void setWidget() {
        txt_summary = (TextView) findViewById(R.id.txt_summary);
        txt_summary_today = (TextView) findViewById(R.id.txt_summary_today);
        btn_expense = (Button) findViewById(R.id.btn_expense);
        btn_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ExpenseActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.transition_left_in, R.anim.transition_left_out);
            }
        });
        btn_income = (Button) findViewById(R.id.btn_income);
        btn_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IncomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.transition_left_in, R.anim.transition_left_out);
            }
        });
        btn_setting = (Button) findViewById(R.id.btn_setting);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogSetting();
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

        final TextView btn_clear_data = (TextView) promptsView.findViewById(R.id.btn_clear);
        final LinearLayout lin_passcode = (LinearLayout) promptsView.findViewById(R.id.lin_passcode);
        final SwitchCompat switch_pass = (SwitchCompat) promptsView.findViewById(R.id.switch_pass);
        final LinearLayout lin_change_passcode = (LinearLayout) promptsView.findViewById(R.id.lin_change_passcode);
        final Spinner dropdown_language = (Spinner) promptsView.findViewById(R.id.dropdown_language);
        final SpinnerLanguageDropdownAdapter spinnerDropdownAdapter = new SpinnerLanguageDropdownAdapter(getActivity());
        dropdown_language.setAdapter(spinnerDropdownAdapter);
        if (getCurrentLocale().equals("th")) {
            dropdown_language.setSelection(0);
        } else if (getCurrentLocale().equals("en")) {
            dropdown_language.setSelection(1);
        }
        dropdown_language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        RealmManager.getInstance().getDataRepository().getInitialMoney().subscribe(new Action1<Double>() {
            @Override
            public void call(Double aDouble) {
                if (aDouble == 0.00) {
                    edt_initial_money.setText("");// set dialog message
                } else {
                    edt_initial_money.setText(String.format("%.2f", aDouble));// set dialog message
                }
                alertDialogBuilder
                        .setPositiveButton(getResources().getString(R.string.done),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        String m = edt_initial_money.getText().toString();
                                        if (!m.equals("")) {
                                            initial_money = Double.parseDouble(m);
                                            RealmManager.getInstance().getDataRepository().setInitial(initial_money).subscribe();
                                            loadData();
                                        }
                                        int posLanguage = dropdown_language.getSelectedItemPosition();
                                        if (posLanguage == 0) {
                                            setLocale("th");
                                        } else if (posLanguage == 1) {
                                            setLocale("en");
                                        }
                                    }
                                });

                // create alert dialog
                final AlertDialog alertDialog = alertDialogBuilder.create();
                btn_clear_data.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                        showDialogConfirmClear();
                    }
                });
                final RubjaiPreference rubjaiPreference = new RubjaiPreference(Contextor.getInstance().getContext());
                if (rubjaiPreference.passcode_en) {
                    switch_pass.setChecked(true);
                    lin_passcode.setVisibility(View.VISIBLE);
                    lin_change_passcode.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), PasscodeActivity.class);
                            startActivity(intent);
                            alertDialog.dismiss();
                        }
                    });
                } else {
                    switch_pass.setChecked(false);
                    lin_passcode.setVisibility(View.GONE);
                }
                switch_pass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            lin_passcode.setVisibility(View.VISIBLE);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(getActivity(), PasscodeActivity.class);
                                    startActivity(intent);
                                    alertDialog.dismiss();
                                }
                            }, 500);
                        } else {
                            lin_passcode.setVisibility(View.GONE);
                            rubjaiPreference.passcode_en = false;
                        }
                        rubjaiPreference.update();
                    }
                });

                // show it
                alertDialog.show();

            }
        });
    }


    public void showDialogConfirmClear() {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.clear_layout, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                RealmManager.getInstance().getDataRepository().clearDB().subscribe();
                                loadData();
                            }
                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                            }
                        });
        ;

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


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


    public String getCurrentLocale() {
        Locale current = getResources().getConfiguration().locale;
        return current.getLanguage();
    }

    public void setLocale(String lang) {
        Log.d(TAG, getCurrentLocale());
        if (lang.equals(getCurrentLocale())) return;
        Resources res = this.getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(lang);
        res.updateConfiguration(conf, dm);
        // Refresh
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
