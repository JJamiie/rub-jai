package com.rashata.jamie.spend.views.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.rashata.jamie.spend.Contextor;
import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.repository.RealmManager;
import com.rashata.jamie.spend.util.RubjaiPreference;
import com.rashata.jamie.spend.util.SpinnerDropdownAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class FirstSettingMoneyStartedActivity extends AppCompatActivity {
    private EditText edt_initial_money;
    private Spinner spin_currency;
    private RubjaiPreference rubjaiPreference;
    private SpinnerDropdownAdapter spinnerDropdownAdapter;
    private Button btn_set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_setting_money_started);
        rubjaiPreference = new RubjaiPreference(Contextor.getInstance().getContext());
        edt_initial_money = (EditText) findViewById(R.id.edt_initial_money);
        spin_currency = (Spinner) findViewById(R.id.spin_currency);

        String[] currencies = getResources().getStringArray(R.array.spinner_list_currency);
        ArrayList<String> listCurrecy = new ArrayList<String>(Arrays.asList(currencies));
        int currentPosition = 0;
        for (int i = 0; i < listCurrecy.size(); i++) {
            if (rubjaiPreference.currency.equals(listCurrecy.get(i))) {
                currentPosition = i;
                break;
            }
        }

        spinnerDropdownAdapter = new SpinnerDropdownAdapter(this, listCurrecy);
        spin_currency.setAdapter(spinnerDropdownAdapter);
        spin_currency.setSelection(currentPosition);
        edt_initial_money.setRawInputType(Configuration.KEYBOARD_12KEY);

        btn_set = (Button) findViewById(R.id.btn_set);
        btn_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSet();
            }
        });
    }

    public void onSet() {
        String m = edt_initial_money.getText().toString();
        double initial_money;
        if (!m.equals("")) {
            initial_money = Double.parseDouble(m);
            RealmManager.getInstance().getDataRepository().setInitial(initial_money).subscribe();
        }
        String currency = spinnerDropdownAdapter.getItem(spin_currency.getSelectedItemPosition());
        rubjaiPreference.currency = currency;
        rubjaiPreference.update();
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


}
