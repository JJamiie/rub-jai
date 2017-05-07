package com.rashata.jamie.spend.views.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rashata.jamie.spend.Contextor;
import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.repository.database.Data;
import com.rashata.jamie.spend.repository.database.ExpenseCategory;
import com.rashata.jamie.spend.repository.database.IncomeCategory;
import com.rashata.jamie.spend.repository.RealmManager;
import com.rashata.jamie.spend.util.Constants;
import com.rashata.jamie.spend.util.GridSpacingItemDecoration;
import com.rashata.jamie.spend.util.CategoryItem;
import com.rashata.jamie.spend.util.ItemTouchHelperCallback;
import com.rashata.jamie.spend.views.adapter.ManageAdapter;
import com.rashata.jamie.spend.views.adapter.NewItemCategoryAdapter;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class ManageActivity extends AppCompatActivity implements ManageAdapter.OnDragStartListener, NewItemCategoryAdapter.ActivityListener {

    private static final String TAG = "ManageActivity";
    private Toolbar toolbar;
    private RecyclerView rec_item;
    private ItemTouchHelper mItemTouchHelper;
    private ManageAdapter manageAdapter;
    private int type;
    private FloatingActionButton btn_add;
    private int selected_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
        initInstances();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void initInstances() {
        Intent intent = getIntent();
        type = intent.getIntExtra("type", -1);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.edit_category));

        rec_item = (RecyclerView) findViewById(R.id.rec_item);
        rec_item.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        rec_item.setLayoutManager(gridLayoutManager);
        int spanCount = 4;
        int spacing = 15;
        boolean includeEdge = false;
        rec_item.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        manageAdapter = new ManageAdapter(this, this, type);
        rec_item.setAdapter(manageAdapter);
        final ItemTouchHelperCallback callback = new ItemTouchHelperCallback(manageAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(rec_item);
        btn_add = (FloatingActionButton) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogToAddCategory();
            }
        });
    }

    private void showDialogToAddCategory() {
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.add_category, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        RecyclerView rec_item = (RecyclerView) promptsView.findViewById(R.id.rec_item);
        rec_item.setHasFixedSize(true);
        rec_item.setLayoutManager(new GridLayoutManager(this, 4));
        final NewItemCategoryAdapter newItemCategoryAdapter = new NewItemCategoryAdapter(type, this);
        rec_item.setAdapter(newItemCategoryAdapter);
        selected_item = -1;
        if (type == Data.TYPE_EXPENSE) {
            newItemCategoryAdapter.setData(Constants.expensePic);
        } else {
            newItemCategoryAdapter.setData(Constants.incomePic);
        }
        final EditText edt_category = (EditText) promptsView.findViewById(R.id.edt_category);
        edt_category.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newItemCategoryAdapter.setNameCategory(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        Button btn_add_category = (Button) promptsView.findViewById(R.id.btn_add_category);
        btn_add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edt_category.getText().toString();
                if (name.isEmpty()) {
                    edt_category.setError(getResources().getString(R.string.please_fill_category_name));
                    edt_category.requestFocus();
                    return;
                } else if (selected_item == -1) {
                    showToast(getResources().getString(R.string.please_choose_category_name));
                    return;
                } else {
                    if (type == Data.TYPE_EXPENSE) {
                        RealmManager.getInstance().getDataRepository()
                                .addExpenseCategory(name, newItemCategoryAdapter.getData()[selected_item])
                                .subscribe(new Action1<ExpenseCategory>() {
                                    @Override
                                    public void call(ExpenseCategory expenseCategory) {
                                        int resId = getResources().getIdentifier(expenseCategory.getPicture(), "drawable", "com.rashata.jamie.spend");
                                        manageAdapter.addCategory(new CategoryItem(expenseCategory.getUuid(), resId, expenseCategory.getName(), expenseCategory.isShow()));
                                    }
                                });
                    } else {
                        RealmManager.getInstance().getDataRepository()
                                .addIncomeCategory(name, newItemCategoryAdapter.getData()[selected_item])
                                .subscribe(new Action1<IncomeCategory>() {
                                    @Override
                                    public void call(IncomeCategory incomeCategory) {
                                        int resId = getResources().getIdentifier(incomeCategory.getPicture(), "drawable", "com.rashata.jamie.spend");
                                        manageAdapter.addCategory(new CategoryItem(incomeCategory.getUuid(), resId, incomeCategory.getName(), incomeCategory.isShow()));
                                    }
                                });
                    }
                    updateCategory();
                }
                alertDialog.dismiss();
                manageAdapter.notifyDataSetChanged();
            }
        });
        Button btn_cancel = (Button) promptsView.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                manageAdapter.notifyDataSetChanged();
            }
        });
        alertDialog.show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.transition_right_in, R.anim.transition_right_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadData() {
        if (type == Data.TYPE_EXPENSE) {
            loadDataExpense();
        } else {
            loadDataIncome();
        }
    }

    private void loadDataExpense() {
        final ArrayList<CategoryItem> imageItems = new ArrayList<>();
        RealmManager.getInstance().getDataRepository().getAllExpenseCategory().subscribe(new Action1<List<ExpenseCategory>>() {
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
        manageAdapter.setImageItems(imageItems);
    }

    private void loadDataIncome() {
        final ArrayList<CategoryItem> imageItems = new ArrayList<>();
        RealmManager.getInstance().getDataRepository().getAllIncomeCategory().subscribe(new Action1<List<IncomeCategory>>() {
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
        manageAdapter.setImageItems(imageItems);
    }


    @Override
    public void onDragStarted(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onUpdate() {
        updateCategory();
    }


    public void updateCategory() {
        if (type == Data.TYPE_EXPENSE) {
            RealmManager.getInstance().getDataRepository().updateCategoryExpense(manageAdapter.getImageItems()).subscribe();
        } else {
            RealmManager.getInstance().getDataRepository().updateCategoryIncome(manageAdapter.getImageItems()).subscribe();
        }
    }

    public Activity getActivity() {
        return this;
    }

    public void showToast(String text) {
        Toast.makeText(Contextor.getInstance().getContext(), text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClicked(int position) {
        selected_item = position;
    }
}
