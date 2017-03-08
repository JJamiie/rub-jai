package com.rashata.jamie.spend.views.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rashata.jamie.spend.Contextor;
import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.manager.http.HttpManager;
import com.rashata.jamie.spend.manager.http.dao.ListCurrencyDao;
import com.rashata.jamie.spend.repository.RealmManager;
import com.rashata.jamie.spend.util.Constants;
import com.rashata.jamie.spend.util.RubjaiPreference;
import com.rashata.jamie.spend.util.SpinnerCurrencyDropdownAdapter;
import com.rashata.jamie.spend.util.SpinnerDropdownAdapter;
import com.rashata.jamie.spend.util.SpinnerDropdownPictureAdapter;
import com.rashata.jamie.spend.views.adapter.SettingsAdapter;
import com.rashata.jamie.spend.views.adapter.setting.item.BaseSettingItem;
import com.rashata.jamie.spend.views.adapter.setting.item.SettingClearDataItem;
import com.rashata.jamie.spend.views.adapter.setting.item.SettingLanguageItem;
import com.rashata.jamie.spend.views.adapter.setting.item.SettingMoneyStartedItem;
import com.rashata.jamie.spend.views.adapter.setting.item.SettingPasscodeItem;
import com.rashata.jamie.spend.views.adapter.setting.item.SettingType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.functions.Action1;


public class SettingsActivity extends AppCompatActivity implements SettingsAdapter.SettingsAdapterListener {

    private static final String TAG = "SettingsActivity";

    private RecyclerView rec_settings;
    private SettingsAdapter settingsAdapter;
    private RubjaiPreference rubjaiPreference;
    private double initial_money;
    private double moneyStarted;
    private Map<String, Double> rates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initInstances();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        listCurrency();
    }

    private void initInstances() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.settings));
        rec_settings = (RecyclerView) findViewById(R.id.rec_settings);
        rec_settings.setHasFixedSize(true);
        rec_settings.setLayoutManager(new LinearLayoutManager(this));
        settingsAdapter = new SettingsAdapter(this);
        rec_settings.setAdapter(settingsAdapter);
    }


    public void loadData() {
        rubjaiPreference = new RubjaiPreference(Contextor.getInstance().getContext());
        RealmManager.getInstance().getDataRepository().getInitialMoney().subscribe(new Action1<Double>() {
            @Override
            public void call(Double aDouble) {
                moneyStarted = aDouble;
                ArrayList<BaseSettingItem> baseSettingItems = new ArrayList<>();
                SettingMoneyStartedItem settingMoneyStartedItem = new SettingMoneyStartedItem();
                settingMoneyStartedItem.setMoney(String.valueOf(moneyStarted));
                baseSettingItems.add(settingMoneyStartedItem);
                SettingPasscodeItem settingPasscodeItem = new SettingPasscodeItem();
                settingPasscodeItem.setChecked(rubjaiPreference.passcode_en);
                baseSettingItems.add(settingPasscodeItem);
                SettingLanguageItem settingLanguageItem = new SettingLanguageItem();
                settingLanguageItem.setLanguage(getCurrentLocale());
                baseSettingItems.add(settingLanguageItem);
//                baseSettingItems.add(new SettingCurrencyItem());
                baseSettingItems.add(new SettingClearDataItem());
                settingsAdapter.setBaseSettingItems(baseSettingItems);
            }
        });
    }


    /************** dialog*****************/

    public void showDialogMoneyStarted() {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.money_started_layout, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        Spinner spin_currency = (Spinner) promptsView.findViewById(R.id.spin_currency);
        SpinnerDropdownAdapter spinnerDropdownAdapter = new SpinnerDropdownAdapter(this, rates);
        spin_currency.setAdapter(spinnerDropdownAdapter);
        final EditText edt_initial_money = (EditText) promptsView.findViewById(R.id.edt_initial_money);
        edt_initial_money.setRawInputType(Configuration.KEYBOARD_12KEY);
        if (moneyStarted == 0.00) {
            edt_initial_money.setText("");// set dialog message
        } else {
            edt_initial_money.setText(String.format("%.2f", moneyStarted));// set dialog message
        }
        alertDialogBuilder
                .setPositiveButton(getResources().getString(R.string.done),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String m = edt_initial_money.getText().toString();
                                if (!m.equals("")) {
                                    initial_money = Double.parseDouble(m);
                                    RealmManager.getInstance().getDataRepository().setInitial(initial_money).subscribe();
                                }
                                SettingMoneyStartedItem settingMoneyStartedItem = (SettingMoneyStartedItem) settingsAdapter.getBaseSettingItems().get(0);
                                settingMoneyStartedItem.setMoney(String.valueOf(initial_money));
                                settingsAdapter.notifyDataSetChanged();
                            }
                        });

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        if (rates == null) {
            showToast(getString(R.string.pleasetryagain));
        }else{
            alertDialog.show();
        }
    }



    public void showDialogChangeLanguage() {
        View promptsView = LayoutInflater.from(this).inflate(R.layout.change_language_layout, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        final Spinner spin_language = (Spinner) promptsView.findViewById(R.id.spin_language);
        final SpinnerDropdownPictureAdapter spinnerDropdownAdapter = new SpinnerDropdownPictureAdapter(this, Constants.flag, Constants.language);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (spin_language.getSelectedItemPosition() == 0) {
                                    setLocale("th");
                                } else if (spin_language.getSelectedItemPosition() == 1) {
                                    setLocale("en");
                                }
                            }
                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        spin_language.setAdapter(spinnerDropdownAdapter);
        if (getCurrentLocale().equals("th")) {
            spin_language.setSelection(0);
        } else if (getCurrentLocale().equals("en")) {
            spin_language.setSelection(1);
        }
        // show it
        alertDialog.show();
    }

    public void showDialogConfirmClear() {
        View promptsView = LayoutInflater.from(this).inflate(R.layout.clear_layout, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                RealmManager.getInstance().getDataRepository().clearDB().subscribe();
                            }
                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }


    public void showDialogCurrency() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.dialog_change_currrency, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        TextView txt_current_currency = (TextView) promptsView.findViewById(R.id.txt_current_currency);
        final EditText edt_select_currency = (EditText) promptsView.findViewById(R.id.edt_select_currency);
        txt_current_currency.setText(rubjaiPreference.currency);
        final Spinner spin_currency = (Spinner) promptsView.findViewById(R.id.spin_currency);

        final SpinnerCurrencyDropdownAdapter spinnerCurrencyDropdownAdapter = new SpinnerCurrencyDropdownAdapter(getActivity(), rates);
        spin_currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                edt_select_currency.setText(String.valueOf(spinnerCurrencyDropdownAdapter.getData().get(spinnerCurrencyDropdownAdapter.getmKeys()[position])));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_currency.setAdapter(spinnerCurrencyDropdownAdapter);
        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                .setNegativeButton(getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    /***********************************/


    public String getCurrentLocale() {
        Locale current = getResources().getConfiguration().locale;
        return current.getLanguage();
    }

    public void setLocale(String lang) {
        Log.d(TAG, getCurrentLocale());
        if (lang.equals(getCurrentLocale())) return;
        Resources res = this.getResources();
        // Change locale settings in the app.
        Configuration conf = res.getConfiguration();
        conf.locale = new Locale(lang);
        res.updateConfiguration(conf, res.getDisplayMetrics());
        RubjaiPreference rubjaiPreference = new RubjaiPreference(this);
        rubjaiPreference.language = lang;
        rubjaiPreference.update();
        // Refresh
        loadData();
    }

    public Activity getActivity() {
        return this;
    }


    @Override
    public void onSelectSettingItem(int type) {
        if (type == SettingType.TYPE_MONEY_STARTED) {
            showDialogMoneyStarted();
        } else if (type == SettingType.TYPE_LANGUAGE) {
            showDialogChangeLanguage();
        } else if (type == SettingType.TYPE_CURRENCY) {
            showDialogCurrency();
        } else if (type == SettingType.TYPE_CLEAR_DATA) {
            showDialogConfirmClear();
        } else if (type == SettingType.TYPE_ENABLED_PASSCODE) {
            startPasscodeActivity();
        } else if (type == SettingType.TYPE_DISABLED_PASSCODE) {
            rubjaiPreference.passcode_en = false;
            rubjaiPreference.update();
            loadData();
        }
    }

    public void startPasscodeActivity() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getActivity(), PasscodeActivity.class);
                startActivity(intent);
            }
        }, 500);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.transition_right_in, R.anim.transition_right_out);
                break;
        }
        return true;
    }

    /**************Connect Server*****************/

    public void listCurrency() {
        Call<ListCurrencyDao> call = HttpManager.getInstance().getApiService().listCurrency(rubjaiPreference.currency);
        call.enqueue(new Callback<ListCurrencyDao>() {
            @Override
            public void onResponse(Call<ListCurrencyDao> call, Response<ListCurrencyDao> response) {
                if (response.isSuccessful()) {
                    ListCurrencyDao listCurrencyDao = response.body();
                    Log.d(TAG, new Gson().toJson(listCurrencyDao));
                    rates = listCurrencyDao.getRates();
                } else {
                    try {
                        Log.d(TAG, response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ListCurrencyDao> call, Throwable t) {
                Log.d(TAG, t.toString());
            }
        });
    }

    private void showToast(String text) {
        Toast.makeText(Contextor.getInstance().getContext(), text, Toast.LENGTH_SHORT).show();
    }
}
