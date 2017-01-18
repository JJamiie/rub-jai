package com.rashata.jamie.spend.views.adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.rashata.jamie.spend.Contextor;
import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.manager.Data;
import com.rashata.jamie.spend.manager.ExpenseCategory;
import com.rashata.jamie.spend.manager.IncomeCategory;
import com.rashata.jamie.spend.repository.RealmManager;
import com.rashata.jamie.spend.util.ArrayAdapterTitle;
import com.rashata.jamie.spend.util.CategoryItem;
import com.rashata.jamie.spend.util.Constants;
import com.rashata.jamie.spend.util.ItemTouchHelperAdapter;
import com.rashata.jamie.spend.util.ItemTouchHelperViewHolder;

import java.util.ArrayList;

import rx.functions.Action1;

/**
 * Created by jjamierashata on 11/11/2016 AD.
 */

public class ManageAdapter extends RecyclerView.Adapter<ManageAdapter.ManageViewHolder> implements ItemTouchHelperAdapter, NewItemCategoryAdapter.ActivityListener {
    private static final String TAG = "ManageAdapter";
    private int type;
    private ArrayList<CategoryItem> imageItems;
    private Activity activity;
    private OnDragStartListener mDragStartListener;
    private int selected_item;


    public interface OnDragStartListener {
        void onDragStarted(RecyclerView.ViewHolder viewHolder);

        void onUpdate();
    }

    public ManageAdapter(Activity activity, OnDragStartListener mDragStartListener, int type) {
        this.mDragStartListener = mDragStartListener;
        this.activity = activity;
        this.imageItems = new ArrayList<>();
        this.type = type;
    }

    @Override
    public ManageViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_item_layout_edit, viewGroup, false);
        ManageViewHolder qvh = new ManageViewHolder(v);
        return qvh;
    }

    @Override
    public void onBindViewHolder(final ManageViewHolder holder, final int position) {
        holder.cus_move.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (MotionEventCompat.getActionMasked(motionEvent) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onDragStarted(holder);
                }
                return false;
            }
        });
        holder.image.setImageResource(imageItems.get(position).getResId());
        holder.text.setText(imageItems.get(position).getTitle());
        if (type == Data.TYPE_EXPENSE) {
            holder.text.setTextColor(Color.parseColor("#89C4CA"));
        } else {
            holder.text.setTextColor(Color.parseColor("#989a9c"));
        }
        holder.txt_is_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageItems.get(position).isShow()) {
                    imageItems.get(position).setShow(false);
                } else {
                    imageItems.get(position).setShow(true);
                }
                mDragStartListener.onUpdate();
                notifyDataSetChanged();
            }
        });
        if (imageItems.get(position).isShow()) {
            holder.txt_is_show.setTextColor(Color.WHITE);
            holder.txt_is_show.setBackgroundColor(Color.parseColor("#EFB7B6"));
        } else {
            holder.txt_is_show.setBackgroundColor(Color.parseColor("#fffafa"));
            holder.txt_is_show.setTextColor(Color.parseColor("#EFB7B6"));
        }


        holder.crd_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((type == Data.TYPE_EXPENSE && imageItems.get(position).getId() == Constants.TYPE_OTHER_EXPENSE) ||
                        (type == Data.TYPE_INCOME && imageItems.get(position).getId() == Constants.TYPE_OTHER_INCOME)) {
                    showToast(Contextor.getInstance().getContext().getString(R.string.cant_edit_others_category));
                } else {
                    showDialogOption(imageItems.get(position).getId(), imageItems.get(position).getResId()
                            , imageItems.get(position).getTitle(), position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageItems.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        imageItems.add(toPosition, imageItems.remove(fromPosition));
//        Collections.swap(imageItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }


    public class ManageViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        private ImageView image;
        private TextView text;
        private TextView txt_is_show;
        private ImageView cus_move;
        private FrameLayout crd_item;

        public ManageViewHolder(View itemView) {
            super(itemView);
            cus_move = (ImageView) itemView.findViewById(R.id.cus_move);
            image = (ImageView) itemView.findViewById(R.id.image);
            text = (TextView) itemView.findViewById(R.id.text);
            txt_is_show = (TextView) itemView.findViewById(R.id.txt_is_show);
            crd_item = (FrameLayout) itemView.findViewById(R.id.crd_item);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(Color.WHITE);
        }
    }

    public ArrayList<CategoryItem> getImageItems() {
        return imageItems;
    }

    public void setImageItems(ArrayList<CategoryItem> imageItems) {
        this.imageItems = imageItems;
        notifyDataSetChanged();
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void addCategory(CategoryItem categoryItem) {
        this.imageItems.add(categoryItem);
    }


    public void showDialogOption(final int uuid, final int resId, final String title, final int position) {
        final String[] items = new String[]{Contextor.getInstance().getContext().getString(R.string.edit_category),
                Contextor.getInstance().getContext().getString(R.string.delete_category)};
        ListAdapter adapter = new ArrayAdapterTitle(getActivity(), items);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        showDialogToEditCategory(uuid, resId, title, position);
                        break;
                    case 1:
                        showDialogDeleteCategory(uuid, position);
                        break;
                }
            }
        });
        // set prompts.xml to alertdialog builder
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    public void showDialogDeleteCategory(final int uuid, final int position) {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.delete_category_layout, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton(Contextor.getInstance().getContext().getString(R.string.yes),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (type == Data.TYPE_EXPENSE) {
                                    RealmManager.getInstance().getDataRepository().deleteCategoryExpense(uuid).subscribe();
                                } else {
                                    RealmManager.getInstance().getDataRepository().deleteCategoryIncome(uuid).subscribe();
                                }
                                imageItems.remove(position);
                                mDragStartListener.onUpdate();
                                notifyDataSetChanged();
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

    private void showDialogToEditCategory(final int uuid, int resId, String title, final int position) {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View promptsView = li.inflate(R.layout.edit_category, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        RecyclerView rec_item = (RecyclerView) promptsView.findViewById(R.id.rec_item);
        rec_item.setHasFixedSize(true);
        rec_item.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        final NewItemCategoryAdapter newItemCategoryAdapter = new NewItemCategoryAdapter(type, this);
        selected_item = -1;
        rec_item.setAdapter(newItemCategoryAdapter);
        if (type == Data.TYPE_EXPENSE) {
            newItemCategoryAdapter.setData(Constants.expensePic);
        } else {
            newItemCategoryAdapter.setData(Constants.incomePic);
        }
        for (int i = 0; i < newItemCategoryAdapter.getData().length; i++) {
            if (newItemCategoryAdapter.getData()[i] == resId) {
                newItemCategoryAdapter.setClicked(i);
                selected_item = i;
                break;
            }
        }
        final EditText edt_category = (EditText) promptsView.findViewById(R.id.edt_category);
        edt_category.setText(title);
        newItemCategoryAdapter.setNameCategory(title);
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
                    edt_category.setError(Contextor.getInstance().getContext().getString(R.string.please_fill_category_name));
                    edt_category.requestFocus();
                    return;
                } else if (selected_item == -1) {
                    showToast(Contextor.getInstance().getContext().getString(R.string.please_choose_category_name));
                    return;
                } else {
                    if (type == Data.TYPE_EXPENSE) {
                        RealmManager.getInstance().getDataRepository()
                                .editExpenseCategory(uuid, name, newItemCategoryAdapter.getData()[selected_item])
                                .subscribe(new Action1<ExpenseCategory>() {
                                    @Override
                                    public void call(ExpenseCategory expenseCategory) {
                                        int resId = getActivity().getResources().getIdentifier(expenseCategory.getPicture(), "drawable", "com.rashata.jamie.spend");
                                        imageItems.get(position).setResId(resId);
                                        imageItems.get(position).setTitle(expenseCategory.getName());
                                        notifyDataSetChanged();
                                    }
                                });
                    } else {
                        RealmManager.getInstance().getDataRepository()
                                .editIncomeCategory(uuid, name, newItemCategoryAdapter.getData()[selected_item])
                                .subscribe(new Action1<IncomeCategory>() {
                                    @Override
                                    public void call(IncomeCategory incomeCategory) {
                                        int resId = getActivity().getResources().getIdentifier(incomeCategory.getPicture(), "drawable", "com.rashata.jamie.spend");
                                        imageItems.get(position).setResId(resId);
                                        imageItems.get(position).setTitle(incomeCategory.getName());
                                        notifyDataSetChanged();
                                    }
                                });
                    }
                }
                mDragStartListener.onUpdate();
                alertDialog.dismiss();
                notifyDataSetChanged();
            }
        });
        Button btn_cancel = (Button) promptsView.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                notifyDataSetChanged();
            }
        });
        alertDialog.show();
    }

    private void showToast(String text) {
        Toast.makeText(Contextor.getInstance().getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public OnDragStartListener getmDragStartListener() {
        return mDragStartListener;
    }

    @Override
    public void onItemClicked(int position) {
        selected_item = position;

    }
}

