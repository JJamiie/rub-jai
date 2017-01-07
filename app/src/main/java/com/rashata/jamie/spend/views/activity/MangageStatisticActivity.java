package com.rashata.jamie.spend.views.activity;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.manager.ExpenseCategory;
import com.rashata.jamie.spend.manager.ExpenseStatistic;
import com.rashata.jamie.spend.repository.RealmManager;
import com.rashata.jamie.spend.util.CategoryItem;
import com.rashata.jamie.spend.util.GridSpacingItemDecoration;
import com.rashata.jamie.spend.util.StatisticItem;
import com.rashata.jamie.spend.views.adapter.ManageStatisticAdapter;
import com.rashata.jamie.spend.views.adapter.ManageStatisticCategoryAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class MangageStatisticActivity extends AppCompatActivity implements ManageStatisticAdapter.OnStatisticClick {

    private Toolbar toolbar;
    private RecyclerView rec_statistic_catagory;
    private RecyclerView rec_statistic;
    private ManageStatisticAdapter manageStatisticAdapter;
    private ManageStatisticCategoryAdapter manageStatisticCategoryAdapter;
    private LinearLayoutManager llm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mangage_statistic);
        setWidget();
    }

    private void setWidget() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.setting_graph_statistic));
        rec_statistic_catagory = (RecyclerView) findViewById(R.id.rec_statistic_catagory);
        rec_statistic_catagory.setHasFixedSize(true);
        rec_statistic = (RecyclerView) findViewById(R.id.rec_statistic);
        rec_statistic.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        rec_statistic_catagory.setLayoutManager(gridLayoutManager);
        int spanCount = 4;
        int spacing = 15;
        boolean includeEdge = false;
        rec_statistic_catagory.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        llm = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rec_statistic.setLayoutManager(llm);
        manageStatisticAdapter = new ManageStatisticAdapter(this, this);
        rec_statistic.setAdapter(manageStatisticAdapter);
        manageStatisticCategoryAdapter = new ManageStatisticCategoryAdapter(this);
        rec_statistic_catagory.setAdapter(manageStatisticCategoryAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.transition_right_in, R.anim.transition_right_out);
                return true;
            case R.id.add:
                showDialogAddStatistic();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void loadData() {
        RealmManager.getInstance().getDataRepository().getAllExpenseStatistic().subscribe(new Action1<List<ExpenseStatistic>>() {
            @Override
            public void call(List<ExpenseStatistic> expenseStatistics) {
                ArrayList<StatisticItem> statisticItems = new ArrayList<StatisticItem>();
                for (ExpenseStatistic expenseStatistic : expenseStatistics) {
                    statisticItems.add(new StatisticItem(expenseStatistic.getUuid(), expenseStatistic.getTitle(), false));
                }
                if (!statisticItems.isEmpty()) {
                    manageStatisticAdapter.setStatisticItems(statisticItems);
                    loadStatisticCategory(statisticItems.get(0).getId());
                    manageStatisticCategoryAdapter.setIdExpenseStatistic(statisticItems.get(0).getId());
                    manageStatisticAdapter.setClicked(0);
                    manageStatisticCategoryAdapter.setTitleCurrentStatistic(expenseStatistics.get(0).getTitle());
                }
            }
        });
    }

    public void loadStatisticCategory(int id) {
        final ArrayList<CategoryItem> categoryItems = new ArrayList<>();
        RealmManager.getInstance().getDataRepository().getAllExpenseCategory().subscribe(new Action1<List<ExpenseCategory>>() {
            @Override
            public void call(List<ExpenseCategory> expenseCategories) {
                for (ExpenseCategory expenseCategory : expenseCategories) {
                    int resId = getResources().getIdentifier(expenseCategory.getPicture(), "drawable", "com.rashata.jamie.spend");
                    categoryItems.add(new CategoryItem(expenseCategory.getUuid(), resId, expenseCategory.getName(), false));
                }
            }
        });

        RealmManager.getInstance().getDataRepository().getStatisticCategory(id).subscribe(new Action1<List<ExpenseCategory>>() {
            @Override
            public void call(List<ExpenseCategory> expenseCategories) {
                for (ExpenseCategory expenseCategory : expenseCategories) {
                    for (CategoryItem categoryItem : categoryItems) {
                        if (categoryItem.getId() == expenseCategory.getUuid()) {
                            categoryItem.setShow(true);
                            break;
                        }
                    }
                }
            }
        });
        manageStatisticCategoryAdapter.setCategoryItems(categoryItems);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_spinner_add, menu);
        return true;
    }


    @Override
    public void onStatisticClick(int id, String title) {
        loadStatisticCategory(id);
        manageStatisticCategoryAdapter.setIdExpenseStatistic(id);
        manageStatisticCategoryAdapter.setTitleCurrentStatistic(title);

    }

    @Override
    public void onClearStatisticCategory() {
        manageStatisticCategoryAdapter.getCategoryItems().clear();
        manageStatisticCategoryAdapter.notifyDataSetChanged();
    }

    private void showDialogAddStatistic() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.add_statistic, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        final EditText edt_statistic = (EditText) promptsView.findViewById(R.id.edt_statistic);
        Button btn_cancel = (Button) promptsView.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        Button btn_add_statistic = (Button) promptsView.findViewById(R.id.btn_add_statistic);
        btn_add_statistic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String statistic = edt_statistic.getText().toString();
                if (statistic.isEmpty()) {
                    edt_statistic.setError(getResources().getString(R.string.please_fill_statistic));
                    edt_statistic.requestFocus();
                } else {
                    RealmManager.getInstance().getDataRepository().addExpenseStatistic(statistic).subscribe(new Action1<ExpenseStatistic>() {
                        @Override
                        public void call(ExpenseStatistic expenseStatistic) {
                            manageStatisticAdapter.getStatisticItems().add(new StatisticItem(expenseStatistic.getUuid(), expenseStatistic.getTitle(), false));
                            llm.scrollToPosition(manageStatisticAdapter.getItemCount() - 1);
                            alertDialog.dismiss();
                        }
                    });

                }
            }
        });
        alertDialog.show();
    }
}
