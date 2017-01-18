package com.rashata.jamie.spend.views.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.util.RubjaiPreference;
import com.rashata.jamie.spend.views.adapter.PasscodeAdapter;


public class PasscodeActivity extends AppCompatActivity implements PasscodeAdapter.ActivityListener {
    private RecyclerView rec_passcode;
    private LinearLayoutManager llm;
    private Button btn_1;
    private Button btn_2;
    private Button btn_3;
    private Button btn_4;
    private Button btn_5;
    private Button btn_6;
    private Button btn_7;
    private Button btn_8;
    private Button btn_9;
    private Button btn_0;
    private ImageButton btn_delete;
    private Button btn_back;
    private TextView txt_title_passcode;
    private TextView txt_desc_title;

    private PasscodeAdapter passcodeAdapter;
    private boolean isStart;
    private String firstPasscode = "";
    private RubjaiPreference rubjaiPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);
        initInstances();
    }

    private void initInstances() {
        rubjaiPreference = new RubjaiPreference(this);
        Intent intent = getIntent();
        isStart = intent.getBooleanExtra("isStart", false);
        rec_passcode = (RecyclerView) findViewById(R.id.rec_passcode);
        rec_passcode.setHasFixedSize(true);
        llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rec_passcode.setLayoutManager(llm);
        passcodeAdapter = new PasscodeAdapter(this);
        rec_passcode.setAdapter(passcodeAdapter);

        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_1.setOnClickListener(new ButtonNumberOnclick(1));

        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_2.setOnClickListener(new ButtonNumberOnclick(2));

        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_3.setOnClickListener(new ButtonNumberOnclick(3));

        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_4.setOnClickListener(new ButtonNumberOnclick(4));

        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_5.setOnClickListener(new ButtonNumberOnclick(5));

        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_6.setOnClickListener(new ButtonNumberOnclick(6));

        btn_7 = (Button) findViewById(R.id.btn_7);
        btn_7.setOnClickListener(new ButtonNumberOnclick(7));

        btn_8 = (Button) findViewById(R.id.btn_8);
        btn_8.setOnClickListener(new ButtonNumberOnclick(8));

        btn_9 = (Button) findViewById(R.id.btn_9);
        btn_9.setOnClickListener(new ButtonNumberOnclick(9));

        btn_0 = (Button) findViewById(R.id.btn_0);
        btn_0.setOnClickListener(new ButtonNumberOnclick(0));

        btn_delete = (ImageButton) findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passcodeAdapter.delete();
            }
        });

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txt_title_passcode = (TextView) findViewById(R.id.txt_title_passcode);
        txt_desc_title = (TextView) findViewById(R.id.txt_desc_title);

        if (!isStart) {
            txt_title_passcode.setText("Set Passcode");
            txt_desc_title.setText("Please enter a passcode");
        } else {
            txt_title_passcode.setVisibility(View.GONE);
        }
    }

    @Override
    public void onFillPasscodeFinish(String passcode) {
        boolean clearPasscode = false;
        boolean isCorrect = false;
        if (isStart) {
            if (rubjaiPreference.passcode.equals(passcode)) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                clearPasscode = true;
                isCorrect = true;
            }
        } else {
            if (firstPasscode.isEmpty()) {
                txt_title_passcode.setText("Confirm Passcode");
                txt_desc_title.setText("Please re-enter a passcode.");
                firstPasscode = passcode;
                clearPasscode = true;
            } else if (firstPasscode.equals(passcode)) {
                rubjaiPreference.passcode = passcode;
                rubjaiPreference.passcode_en = true;
                rubjaiPreference.update();
                finish();
            } else {
                firstPasscode = "";
                txt_title_passcode.setText("Set Passcode");
                clearPasscode = true;
                isCorrect = true;
            }
        }
        if (clearPasscode) {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    passcodeAdapter.clear();
                }
            }, 200);
        }
        if (isCorrect) {
            txt_desc_title.setText("Passcode does not match. Please try again.");
            Vibrator v = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
        }
    }

    public class ButtonNumberOnclick implements View.OnClickListener {
        private int number;

        public ButtonNumberOnclick(int number) {
            this.number = number;
        }

        @Override
        public void onClick(View v) {
            passcodeAdapter.clicked(number);
        }
    }
}
