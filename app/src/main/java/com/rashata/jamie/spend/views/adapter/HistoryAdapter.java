package com.rashata.jamie.spend.views.adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.rashata.jamie.spend.Contextor;
import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.manager.Data;
import com.rashata.jamie.spend.manager.ExpenseCategory;
import com.rashata.jamie.spend.manager.IncomeCategory;
import com.rashata.jamie.spend.repository.RealmManager;
import com.rashata.jamie.spend.util.ArrayAdapterTitle;
import com.rashata.jamie.spend.util.Constants;
import com.rashata.jamie.spend.util.DatasHistory;
import com.rashata.jamie.spend.util.History;
import com.rashata.jamie.spend.util.CategoryItem;
import com.truizlop.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by jjamierashata on 5/30/16 AD.
 */
public class HistoryAdapter extends SectionedRecyclerViewAdapter<HistoryHeaderViewHolder, HistoryItemViewHolder, HistoryFooterViewHolder> {
    private static final String TAG = "HistoryAdapter";
    private ArrayList<DatasHistory> datasHistories;
    private Activity activity;
    private ActivityListener activityListener;


    public HistoryAdapter(ArrayList<DatasHistory> datasHistories, Activity activity, ActivityListener activityListener) {
        this.activity = activity;
        this.datasHistories = datasHistories;
        this.activityListener = activityListener;
    }


    public interface ActivityListener {
        void onLoadData();
    }

    @Override
    protected int getSectionCount() {
        return datasHistories.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        return datasHistories.get(section).getHistories().size();
    }

    @Override
    protected boolean hasFooterInSection(int section) {
        return false;
    }

    @Override
    protected HistoryHeaderViewHolder onCreateSectionHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_header, parent, false);
        return new HistoryHeaderViewHolder(view);
    }

    @Override
    protected HistoryFooterViewHolder onCreateSectionFooterViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    protected HistoryItemViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryItemViewHolder(view);
    }

    @Override
    protected void onBindSectionHeaderViewHolder(HistoryHeaderViewHolder holder, int section) {
        Date date = datasHistories.get(section).getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Log.d(TAG, (Calendar.DAY_OF_WEEK - 1) + " " + calendar.get(Calendar.MONTH));
        holder.txt_date.setText(Constants.date[calendar.get(Calendar.DAY_OF_WEEK) - 1] + " " + calendar.get(Calendar.DAY_OF_MONTH) + " "
                + Constants.month[calendar.get(Calendar.MONTH)] + " " + (calendar.get(Calendar.YEAR) + 543));
        if (datasHistories.get(section).getTotal_money() >= 0) {
            holder.txt_total_money.setTextColor(Color.parseColor("#888b8f"));
            holder.txt_total_money.setText("+" + String.format("%.2f", datasHistories.get(section).getTotal_money()));
        } else {
            holder.txt_total_money.setTextColor(Color.parseColor("#EF5350"));
            holder.txt_total_money.setText(String.format("%.2f", datasHistories.get(section).getTotal_money()));
        }
    }

    @Override
    protected void onBindSectionFooterViewHolder(HistoryFooterViewHolder holder, int section) {

    }

    @Override
    protected void onBindItemViewHolder(final HistoryItemViewHolder holder, final int section, final int position) {
        final int catagory = datasHistories.get(section).getHistories().get(position).getCatagory();
        if (datasHistories.get(section).getHistories().get(position).getType() == Data.TYPE_EXPENSE) {
            holder.txt_history_money.setTextColor(Color.parseColor("#89C4CA"));
            holder.txt_history_money.setText(String.format("%.2f", datasHistories.get(section).getHistories().get(position).getMoney()));
            RealmManager.getInstance().getDataRepository().getExpenseCategoryWithId(catagory).subscribe(new Action1<ExpenseCategory>() {
                @Override
                public void call(ExpenseCategory data) {
                    holder.txt_history_title.setText(data.getName());
                    int resId = Contextor.getInstance().getContext().getResources().getIdentifier(data.getPicture(), "drawable", "com.rashata.jamie.spend");
                    holder.img_history.setImageResource(resId);
                }
            });

        } else {
            holder.txt_history_money.setTextColor(Color.parseColor("#9e9e9e"));
            holder.txt_history_money.setText("+" + String.format("%.2f", datasHistories.get(section).getHistories().get(position).getMoney()));
            RealmManager.getInstance().getDataRepository().getIncomeCategoryWithId(catagory).subscribe(new Action1<IncomeCategory>() {
                @Override
                public void call(IncomeCategory data) {
                    holder.txt_history_title.setText(data.getName());
                    int resId = Contextor.getInstance().getContext().getResources().getIdentifier(data.getPicture(), "drawable", "com.rashata.jamie.spend");
                    holder.img_history.setImageResource(resId);
                }
            });
        }
        holder.txt_history_note.setText(datasHistories.get(section).getHistories().get(position).getNote());
        holder.frm_history.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDialogOption(section, position);
                return true;
            }
        });

    }


    public void showDialogDelete(final int uuid) {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.delete_layout, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(Contextor.getInstance().getContext().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                RealmManager.getInstance().getDataRepository().deleteData(uuid).subscribe();
                                activityListener.onLoadData();
                            }
                        }).setNegativeButton(Contextor.getInstance().getContext().getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }
                });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void showDialogEdit(final History data) {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.edit_layout, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        final EditText edt_money = (EditText) promptsView.findViewById(R.id.edt_money);
        final EditText edt_note = (EditText) promptsView.findViewById(R.id.edt_note);
        final Spinner spinner = (Spinner) promptsView.findViewById(R.id.spin_type);
        final LinearLayout lin_date = (LinearLayout) promptsView.findViewById(R.id.lin_date);
        final TextView txt_date = (TextView) promptsView.findViewById(R.id.txt_date);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(data.getDate());
        String date = calendar.get(Calendar.DAY_OF_MONTH) + " "
                + Constants.month[calendar.get(Calendar.MONTH)] + " " + (calendar.get(Calendar.YEAR) + 543);
        lin_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChooseDate(calendar, txt_date);
            }
        });
        txt_date.setText(date);
        edt_money.setText((String.format("%.2f", data.getMoney())));
        edt_note.setText(data.getNote());

        final ArrayList<CategoryItem> imageItems = new ArrayList<>();
        if (data.getType() == Data.TYPE_EXPENSE) {
            RealmManager.getInstance().getDataRepository().getExpenseCategory().subscribe(new Action1<List<ExpenseCategory>>() {
                @Override
                public void call(List<ExpenseCategory> datas) {
                    if (!datas.isEmpty()) {
                        for (ExpenseCategory expenseCategory : datas) {
                            imageItems.add(new CategoryItem(expenseCategory.getUuid(), expenseCategory.getPosition(), expenseCategory.getName(), expenseCategory.isShow()));
                        }
                    }
                }
            });
        } else {
            RealmManager.getInstance().getDataRepository().getIncomeCategory().subscribe(new Action1<List<IncomeCategory>>() {
                @Override
                public void call(List<IncomeCategory> datas) {
                    if (!datas.isEmpty()) {
                        for (IncomeCategory incomeCategory : datas) {
                            imageItems.add(new CategoryItem(incomeCategory.getUuid(), incomeCategory.getPosition(), incomeCategory.getName(), incomeCategory.isShow()));
                        }
                    }
                }
            });
        }
        final CategoryDropdownAdapter categoryDropdownAdapter = new CategoryDropdownAdapter(getActivity(), imageItems);
        spinner.setAdapter(categoryDropdownAdapter);
        int selection = -1;
        for (int i = 0; i < imageItems.size(); i++) {
            if (data.getCatagory() == imageItems.get(i).getId()) {
                selection = i;
                break;
            }
        }
        if (selection == -1) {
            if (data.getType() == Data.TYPE_EXPENSE) {
                RealmManager.getInstance().getDataRepository().getExpenseCategoryWithId(data.getCatagory()).subscribe(new Action1<ExpenseCategory>() {
                    @Override
                    public void call(ExpenseCategory expenseCategory) {
                        categoryDropdownAdapter.addTopOfItem(new CategoryItem(expenseCategory.getUuid(), expenseCategory.getPosition(), expenseCategory.getName(), expenseCategory.isShow()));
                    }
                });
            } else {
                RealmManager.getInstance().getDataRepository().getIncomeCategoryWithId(data.getCatagory()).subscribe(new Action1<IncomeCategory>() {
                    @Override
                    public void call(IncomeCategory incomeCategory) {
                        categoryDropdownAdapter.addTopOfItem(new CategoryItem(incomeCategory.getUuid(), incomeCategory.getPosition(), incomeCategory.getName(), incomeCategory.isShow()));
                    }
                });
            }
        } else {
            spinner.setSelection(selection);
        }
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(Contextor.getInstance().getContext().getString(R.string.edit),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                int position = spinner.getSelectedItemPosition();
                                if (data.getType() == Data.TYPE_EXPENSE) {
                                    RealmManager.getInstance().getDataRepository().editData(data.getUuid(), Double.parseDouble(edt_money.getText().toString()), edt_note.getText().toString(),
                                            imageItems.get(position).getId(), calendar.getTime(), data.getType()).subscribe();
                                } else {
                                    RealmManager.getInstance().getDataRepository().editData(data.getUuid(), Double.parseDouble(edt_money.getText().toString()), edt_note.getText().toString(),
                                            imageItems.get(position).getId(), calendar.getTime(), data.getType()).subscribe();
                                }
                                activityListener.onLoadData();
                            }
                        })
                .setNegativeButton(Contextor.getInstance().getContext().getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void showDialogChooseDate(final Calendar calendar, final TextView txt_date) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.cus_dialogTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                String date = calendar.get(Calendar.DAY_OF_MONTH) + " "
                        + Constants.month[calendar.get(Calendar.MONTH)] + " " + (calendar.get(Calendar.YEAR) + 543);
                txt_date.setText(date);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

            }
        });
        datePickerDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                int loadedOrientation = getActivity().getResources().getConfiguration().orientation;
                int requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
                if (loadedOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                } else if (loadedOrientation == Configuration.ORIENTATION_PORTRAIT) {
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                }
                getActivity().setRequestedOrientation(requestedOrientation);
            }
        });
        datePickerDialog.show();
    }

    public void showDialogOption(final int section, final int position) {
        final String[] items = new String[]{Contextor.getInstance().getContext().getString(R.string.edit_data),
                Contextor.getInstance().getContext().getString(R.string.delete_data)};
        ListAdapter adapter = new ArrayAdapterTitle(getActivity(), items);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        showDialogEdit(datasHistories.get(section).getHistories().get(position));
                        break;
                    case 1:
                        showDialogDelete(datasHistories.get(section).getHistories().get(position).getUuid());
                        break;
                }
            }
        });
        // set prompts.xml to alertdialog builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}


class HistoryItemViewHolder extends RecyclerView.ViewHolder {
    CardView frm_history;
    TextView txt_history_title;
    TextView txt_history_note;
    TextView txt_history_money;
    ImageView img_history;
    View v_front;

    public HistoryItemViewHolder(View itemView) {
        super(itemView);
        txt_history_title = (TextView) itemView.findViewById(R.id.txt_history_title);
        txt_history_note = (TextView) itemView.findViewById(R.id.txt_history_note);
        txt_history_money = (TextView) itemView.findViewById(R.id.txt_history_money);
        img_history = (ImageView) itemView.findViewById(R.id.img_history);
        v_front = itemView.findViewById(R.id.v_front);
        frm_history = (CardView) itemView.findViewById(R.id.frm_history);

    }
}

class HistoryHeaderViewHolder extends RecyclerView.ViewHolder {
    TextView txt_date;
    TextView txt_total_money;


    public HistoryHeaderViewHolder(View itemView) {
        super(itemView);
        txt_date = (TextView) itemView.findViewById(R.id.txt_date);
        txt_total_money = (TextView) itemView.findViewById(R.id.txt_total_money);
    }
}


class HistoryFooterViewHolder extends RecyclerView.ViewHolder {

    public HistoryFooterViewHolder(View itemView) {
        super(itemView);


    }
}




