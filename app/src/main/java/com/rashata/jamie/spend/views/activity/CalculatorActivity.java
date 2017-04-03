package com.rashata.jamie.spend.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.rashata.jamie.spend.R;

public class CalculatorActivity extends AppCompatActivity {

    public static final int TYPE_CLEAR = -1;
    public static final int TYPE_PLUS = 0;
    public static final int TYPE_MINUS = 1;
    public static final int TYPE_MULTIPLE = 2;
    public static final int TYPE_DIVIDE = 3;
    public static final int TYPE_EQUAL = 4;

    public static final int MODE_CLEAR = 5;
    public static final int MODE_OPERATION = 6;
    public static final int MODE_NUMBER = 7;

    private static final String TAG = "CalculatorActivity";
    private Button btn_clear;
    private Button btn_divide;
    private Button btn_multiply;
    private Button btn_minus;
    private Button btn_plus;
    private Button btn_equal;
    private Button btn_9;
    private Button btn_8;
    private Button btn_7;
    private Button btn_6;
    private Button btn_5;
    private Button btn_4;
    private Button btn_3;
    private Button btn_2;
    private Button btn_1;
    private Button btn_0;

    private double result;
    private double current;
    private TextView txt_result;
    private Button btn_dot;
    private int mode = MODE_CLEAR;
    private int current_type = TYPE_CLEAR;

    private boolean isDot = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle(getString(R.string.calculator));
        initInstances();
    }

    private void initInstances() {
        current = 0;
        result = 0;
        btn_clear = (Button) findViewById(R.id.btn_clear);
        btn_clear.setOnClickListener(new ClearOnClickListener());
        btn_plus = (Button) findViewById(R.id.btn_plus);
        btn_plus.setOnClickListener(new EquaOnclickListener(TYPE_PLUS));
        btn_minus = (Button) findViewById(R.id.btn_minus);
        btn_minus.setOnClickListener(new EquaOnclickListener(TYPE_MINUS));
        btn_multiply = (Button) findViewById(R.id.btn_multiply);
        btn_multiply.setOnClickListener(new EquaOnclickListener(TYPE_MULTIPLE));
        btn_divide = (Button) findViewById(R.id.btn_divide);
        btn_divide.setOnClickListener(new EquaOnclickListener(TYPE_DIVIDE));
        btn_equal = (Button) findViewById(R.id.btn_equal);
        btn_equal.setOnClickListener(new EquaOnclickListener(TYPE_EQUAL));
        btn_9 = (Button) findViewById(R.id.btn_9);
        btn_9.setOnClickListener(new NumberOnclickListener(9));
        btn_8 = (Button) findViewById(R.id.btn_8);
        btn_8.setOnClickListener(new NumberOnclickListener(8));
        btn_7 = (Button) findViewById(R.id.btn_7);
        btn_7.setOnClickListener(new NumberOnclickListener(7));
        btn_6 = (Button) findViewById(R.id.btn_6);
        btn_6.setOnClickListener(new NumberOnclickListener(6));
        btn_5 = (Button) findViewById(R.id.btn_5);
        btn_5.setOnClickListener(new NumberOnclickListener(5));
        btn_4 = (Button) findViewById(R.id.btn_4);
        btn_4.setOnClickListener(new NumberOnclickListener(4));
        btn_3 = (Button) findViewById(R.id.btn_3);
        btn_3.setOnClickListener(new NumberOnclickListener(3));
        btn_2 = (Button) findViewById(R.id.btn_2);
        btn_2.setOnClickListener(new NumberOnclickListener(2));
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_1.setOnClickListener(new NumberOnclickListener(1));
        btn_0 = (Button) findViewById(R.id.btn_0);
        btn_0.setOnClickListener(new NumberOnclickListener(0));
        btn_dot = (Button) findViewById(R.id.btn_dot);
        btn_dot.setOnClickListener(new NumberOnclickListener(-1));
        txt_result = (TextView) findViewById(R.id.txt_result);
    }

    public class ClearOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            isDot = false;
            if (btn_clear.getText().equals("AC")) {
                result = 0;
                mode = MODE_CLEAR;
                current_type = TYPE_CLEAR;
            }
            current = 0;
            txt_result.setText("0");
            btn_clear.setText("AC");
            clearSelected();
        }
    }

    public class NumberOnclickListener implements View.OnClickListener {
        int number;

        public NumberOnclickListener(int number) {
            this.number = number;
        }

        @Override
        public void onClick(View v) {
            btn_clear.setText("C");
            clearSelected();
            mode = MODE_NUMBER;

            if (number == -1) {
                if (!isDot) {
                    isDot = true;
                    txt_result.setText(String.valueOf((int) current) + '.');
                }
            } else {
                if (isDot) {
                    if (txt_result.getText().length() > 7) return;
                    String currentStr = txt_result.getText().toString() + number;
                    current = Double.parseDouble(currentStr);
                    txt_result.setText(currentStr);
                } else {
                    if (current == 0) {
                        current = number;
                    } else {
                        if (txt_result.getText().length() > 7) return;
                        current = (current * 10) + number;
                    }
                    txt_result.setText(String.valueOf((int) current));
                }

            }
        }
    }


    public class EquaOnclickListener implements View.OnClickListener {

        public int type;

        public EquaOnclickListener(int type) {
            this.type = type;
        }

        @Override
        public void onClick(View v) {
            isDot = false;
            if (type == TYPE_PLUS) {
                btn_plus.setSelected(true);
                btn_minus.setSelected(false);
                btn_multiply.setSelected(false);
                btn_divide.setSelected(false);
            } else if (type == TYPE_MINUS) {
                btn_plus.setSelected(false);
                btn_minus.setSelected(true);
                btn_multiply.setSelected(false);
                btn_divide.setSelected(false);
            } else if (type == TYPE_MULTIPLE) {
                btn_plus.setSelected(false);
                btn_minus.setSelected(false);
                btn_multiply.setSelected(true);
                btn_divide.setSelected(false);
            } else if (type == TYPE_DIVIDE) {
                btn_plus.setSelected(false);
                btn_minus.setSelected(false);
                btn_multiply.setSelected(false);
                btn_divide.setSelected(true);
            } else if (type == TYPE_EQUAL) {
                clearSelected();
            }

            if (mode == MODE_CLEAR) {
                result = current;
                current = 0;
            } else if (mode == MODE_NUMBER) {
                Log.d(TAG, "result: " + result + "current: " + current);

                calculate();
                setNumber();
                mode = MODE_OPERATION;
                current_type = type;
                current = 0;
            } else if (mode == MODE_OPERATION) {
                current_type = type;
            }
            Log.d(TAG, "result: " + result);
        }
    }

    public void calculate() {
        if (current_type == TYPE_CLEAR) {
            result = current;
        } else if (current_type == TYPE_PLUS) {
            result = result + current;
        } else if (current_type == TYPE_MINUS) {
            result = result - current;
        } else if (current_type == TYPE_MULTIPLE) {
            result = result * current;
        } else if (current_type == TYPE_DIVIDE) {
            result = result / current;
        }
    }


    public void setNumber() {
        if (String.valueOf((int) result).length() > 8) {
            txt_result.setText("Error");
        } else {
            if (result == Math.floor(result)) {
                txt_result.setText(String.valueOf((int) result));
            } else {
                String resultStr = String.valueOf(result);
                int len = resultStr.length() > 8 ? 8 : resultStr.length();
                try {
                    result = Double.parseDouble(String.valueOf(result).substring(0, len));
                } catch (NumberFormatException e) {
                    txt_result.setText("Error");
                }
                txt_result.setText(String.valueOf(result));
            }
        }
    }

    public void clearSelected() {
        btn_plus.setSelected(false);
        btn_minus.setSelected(false);
        btn_multiply.setSelected(false);
        btn_divide.setSelected(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            overridePendingTransition(R.anim.transition_right_in, R.anim.transition_right_out);
            finish();
        } else if (item.getItemId() == R.id.action_use) {
            calculate();
            Intent intent = new Intent();
            String resultStr = result == (int) result ? String.valueOf((int) result) : String.valueOf(result);
            intent.putExtra("result", resultStr);
            setResult(RESULT_OK, intent);
            finish();
        }
        return true;
    }
}
