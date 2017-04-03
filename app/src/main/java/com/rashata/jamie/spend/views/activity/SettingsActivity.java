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
import android.widget.EditText;
import android.widget.Spinner;

import com.rashata.jamie.spend.Contextor;
import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.repository.RealmManager;
import com.rashata.jamie.spend.util.Constants;
import com.rashata.jamie.spend.util.RubjaiPreference;
import com.rashata.jamie.spend.util.SpinnerDropdownAdapter;
import com.rashata.jamie.spend.util.SpinnerDropdownPictureAdapter;
import com.rashata.jamie.spend.views.adapter.SettingsAdapter;
import com.rashata.jamie.spend.views.adapter.setting.item.BaseSettingItem;
import com.rashata.jamie.spend.views.adapter.setting.item.SettingClearDataItem;
import com.rashata.jamie.spend.views.adapter.setting.item.SettingLanguageItem;
import com.rashata.jamie.spend.views.adapter.setting.item.SettingMoneyStartedItem;
import com.rashata.jamie.spend.views.adapter.setting.item.SettingPasscodeItem;
import com.rashata.jamie.spend.views.adapter.setting.item.SettingType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import rx.functions.Action1;


public class SettingsActivity extends AppCompatActivity implements SettingsAdapter.SettingsAdapterListener {

    private static final String TAG = "SettingsActivity";
    private static final int RESULT_PASSCODE = 0;

    private RecyclerView rec_settings;
    private SettingsAdapter settingsAdapter;
    private RubjaiPreference rubjaiPreference;
    private double initial_money;
    private double moneyStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initInstances();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        loadData();
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
                settingMoneyStartedItem.setCurrency(String.valueOf(rubjaiPreference.currency));
                baseSettingItems.add(settingMoneyStartedItem); //0
                SettingPasscodeItem settingPasscodeItem = new SettingPasscodeItem();
                settingPasscodeItem.setChecked(rubjaiPreference.passcode_en);
                baseSettingItems.add(settingPasscodeItem);// 1
                SettingLanguageItem settingLanguageItem = new SettingLanguageItem();
                settingLanguageItem.setLanguage(getCurrentLocale());
                baseSettingItems.add(settingLanguageItem); // 2
                baseSettingItems.add(new SettingClearDataItem());
                settingsAdapter.setBaseSettingItems(baseSettingItems);// 3
            }
        });
        getSupportActionBar().setTitle(getResources().getString(R.string.settings));

    }


    /************** dialog*****************/

    public void showDialogMoneyStarted() {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.money_started_layout, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        final Spinner spin_currency = (Spinner) promptsView.findViewById(R.id.spin_currency);

        String[] currencies = getResources().getStringArray(R.array.spinner_list_currency);
        ArrayList<String> listCurrecy = new ArrayList<String>(Arrays.asList(currencies));
        int currentPosition = 0;
        for (int i = 0; i < listCurrecy.size(); i++) {
            if (rubjaiPreference.currency.equals(listCurrecy.get(i))) {
                currentPosition = i;
                break;
            }
        }
        final SpinnerDropdownAdapter spinnerDropdownAdapter = new SpinnerDropdownAdapter(this, listCurrecy);
        spin_currency.setAdapter(spinnerDropdownAdapter);
        spin_currency.setSelection(currentPosition);
        final EditText edt_initial_money = (EditText) promptsView.findViewById(R.id.edt_initial_money);
        edt_initial_money.setRawInputType(Configuration.KEYBOARD_12KEY);
        if (moneyStarted == 0.00) {
            edt_initial_money.setText("");
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
                                String currency = spinnerDropdownAdapter.getItem(spin_currency.getSelectedItemPosition());
                                rubjaiPreference.currency = currency;
                                rubjaiPreference.update();
                                ((SettingMoneyStartedItem) settingsAdapter.getBaseSettingItems().get(0)).setCurrency(currency);
                                settingsAdapter.notifyDataSetChanged();
                            }
                        });

        // create alert dialog
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
                                loadData();
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
        } else if (type == SettingType.TYPE_CLEAR_DATA) {
            showDialogConfirmClear();
        } else if (type == SettingType.TYPE_ENABLED_PASSCODE) {
            startPasscodeActivity();
        } else if (type == SettingType.TYPE_DISABLED_PASSCODE) {
            rubjaiPreference.passcode_en = false;
            rubjaiPreference.update();
            updatePasscode();
        }
    }

    public void updatePasscode() {
        rubjaiPreference = new RubjaiPreference(Contextor.getInstance().getContext());
        SettingPasscodeItem settingPasscodeItem = (SettingPasscodeItem) settingsAdapter.getBaseSettingItems().get(1);
        settingPasscodeItem.setChecked(rubjaiPreference.passcode_en);
        settingsAdapter.notifyDataSetChanged();
    }


    public void startPasscodeActivity() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getActivity(), PasscodeActivity.class);
                startActivityForResult(intent,RESULT_PASSCODE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RESULT_PASSCODE){
            updatePasscode();
        }
    }
}
