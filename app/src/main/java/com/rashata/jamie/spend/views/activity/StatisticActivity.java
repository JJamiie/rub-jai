package com.rashata.jamie.spend.views.activity;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.manager.Data;
import com.rashata.jamie.spend.repository.RealmManager;
import java.util.ArrayList;
import rx.functions.Action1;

public class StatisticActivity extends AppCompatActivity implements OnChartValueSelectedListener {
    private static final String TAG = "StatisticActivity";
    final int[] factor4 = {1, 4, 13, 15};
    final int[] relax = {2, 3, 5, 6, 7, 8, 14};
    final int[] travel = {0};
    final int[] payment = {9, 10, 11, 12};
    private Toolbar toolbar;
    private TextView txt_total_income;
    private TextView txt_total_expense;
    private PieChart mChart;
    private ArrayList<PieEntry> entries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        setWidget();
    }

    public void setWidget() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txt_total_income = (TextView) findViewById(R.id.txt_total_income);
        txt_total_expense = (TextView) findViewById(R.id.txt_total_expense);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("สถิติการใช้เงิน");
        setChart();
        loadData(-1);
    }

    private void setChart() {
        mChart = (PieChart) findViewById(R.id.pieChart);
        mChart.setUsePercentValues(true);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(0.95f);
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(ContextCompat.getColor(this, R.color.colorPrimary));

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);
        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDescription("");

        mChart.getLegend().setEnabled(false);
        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTextSize(14f);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
    }

    private void setData() {
        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        colors.add(ContextCompat.getColor(this, R.color.gray));
        colors.add(ContextCompat.getColor(this, R.color.pink));
        colors.add(ContextCompat.getColor(this, R.color.sky));
        colors.add(ContextCompat.getColor(this, R.color.green));

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);
        mChart.highlightValues(null);
        mChart.invalidate();
    }


    public void loadData(int month) {
        RealmManager.getInstance().getDataRepository().getSummary(Data.TYPE_EXPENSE, month).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                txt_total_expense.setText(s);
            }
        });
        RealmManager.getInstance().getDataRepository().getSummary(Data.TYPE_INCOME, month).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                txt_total_income.setText(s);
            }
        });
        entries = new ArrayList<PieEntry>();

        RealmManager.getInstance().getDataRepository().getStatistic(factor4, month).subscribe(new Action1<Float>() {
            @Override
            public void call(Float aFloat) {
                if (aFloat != 0.0)
                    entries.add(new PieEntry(aFloat, "ปัจจัย 4"));

            }
        });
        RealmManager.getInstance().getDataRepository().getStatistic(relax, month).subscribe(new Action1<Float>() {
            @Override
            public void call(Float aFloat) {
                if (aFloat != 0.0)
                    entries.add(new PieEntry(aFloat, "ผ่อนคลาย"));
            }
        });
        RealmManager.getInstance().getDataRepository().getStatistic(travel, month).subscribe(new Action1<Float>() {
            @Override
            public void call(Float aFloat) {
                if (aFloat != 0.0)
                    entries.add(new PieEntry(aFloat, "เดินทาง"));
            }
        });
        RealmManager.getInstance().getDataRepository().getStatistic(payment, month).subscribe(new Action1<Float>() {
            @Override
            public void call(Float aFloat) {
                if (aFloat != 0.0)
                    entries.add(new PieEntry(aFloat, "ชำระเงิน"));
            }
        });
        setData();

    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_spinner_stat, menu);
        MenuItem menuItem = menu.findItem(R.id.spinner);
        Spinner spinner = (Spinner) MenuItemCompat.getActionView(menuItem);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_list_item_month, R.layout.drop_spinner_title);
        adapter.setDropDownViewResource(R.layout.drop_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        loadData(-1);
                        break;
                    case 1:
                        loadData(0);
                        break;
                    case 2:
                        loadData(1);
                        break;
                    case 3:
                        loadData(2);
                        break;
                    case 4:
                        loadData(3);
                        break;
                    case 5:
                        loadData(4);
                        break;
                    case 6:
                        loadData(5);
                        break;
                    case 7:
                        loadData(6);
                        break;
                    case 8:
                        loadData(7);
                        break;
                    case 9:
                        loadData(8);
                        break;
                    case 10:
                        loadData(9);
                        break;
                    case 11:
                        loadData(10);
                        break;
                    case 12:
                        loadData(11);
                        break;
                    default:
                        loadData(-1);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });
        return true;
    }
}
