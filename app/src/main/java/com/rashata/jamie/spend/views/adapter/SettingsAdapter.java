package com.rashata.jamie.spend.views.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.rashata.jamie.spend.Contextor;
import com.rashata.jamie.spend.R;
import com.rashata.jamie.spend.views.adapter.setting.SettingClearDataViewHolder;
import com.rashata.jamie.spend.views.adapter.setting.SettingCurrencyViewHolder;
import com.rashata.jamie.spend.views.adapter.setting.SettingLanguageViewHolder;
import com.rashata.jamie.spend.views.adapter.setting.SettingMoneyStartedViewHolder;
import com.rashata.jamie.spend.views.adapter.setting.SettingPasscodeViewHolder;
import com.rashata.jamie.spend.views.adapter.setting.item.BaseSettingItem;
import com.rashata.jamie.spend.views.adapter.setting.item.SettingClearDataItem;
import com.rashata.jamie.spend.views.adapter.setting.item.SettingCurrencyItem;
import com.rashata.jamie.spend.views.adapter.setting.item.SettingLanguageItem;
import com.rashata.jamie.spend.views.adapter.setting.item.SettingMoneyStartedItem;
import com.rashata.jamie.spend.views.adapter.setting.item.SettingPasscodeItem;
import com.rashata.jamie.spend.views.adapter.setting.item.SettingType;

import java.util.ArrayList;


public class SettingsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "SettingsAdapter";
    private ArrayList<BaseSettingItem> baseSettingItems;
    private SettingsAdapterListener settingsAdapterListener;


    public SettingsAdapter(SettingsAdapterListener settingsAdapterListener) {
        this.settingsAdapterListener = settingsAdapterListener;
    }

    public interface SettingsAdapterListener {
        void onSelectSettingItem(int type);
    }

    @Override
    public int getItemViewType(int position) {
        return baseSettingItems.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SettingType.TYPE_MONEY_STARTED) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting_started, parent, false);
            SettingMoneyStartedViewHolder settingMoneyStartedViewHolder = new SettingMoneyStartedViewHolder(view);
            return settingMoneyStartedViewHolder;
        } else if (viewType == SettingType.TYPE_PASSCODE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_passcode, parent, false);
            SettingPasscodeViewHolder settingPasscodeViewHolder = new SettingPasscodeViewHolder(view);
            return settingPasscodeViewHolder;
        } else if (viewType == SettingType.TYPE_LANGUAGE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_language, parent, false);
            SettingLanguageViewHolder settingLanguageViewHolder = new SettingLanguageViewHolder(view);
            return settingLanguageViewHolder;
        } else if (viewType == SettingType.TYPE_CLEAR_DATA) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_clear_data, parent, false);
            SettingClearDataViewHolder settingClearDataViewHolder = new SettingClearDataViewHolder(view);
            return settingClearDataViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BaseSettingItem baseSettingItem = baseSettingItems.get(position);
        if (holder instanceof SettingMoneyStartedViewHolder) {
            SettingMoneyStartedViewHolder settingViewHolder = (SettingMoneyStartedViewHolder) holder;
            SettingMoneyStartedItem settingMoneyStartedItem = (SettingMoneyStartedItem) baseSettingItem;
            setupSettingMoney(settingViewHolder, settingMoneyStartedItem);
        } else if (holder instanceof SettingPasscodeViewHolder) {
            SettingPasscodeViewHolder settingPasscodeViewHolder = (SettingPasscodeViewHolder) holder;
            SettingPasscodeItem settingPasscodeItem = (SettingPasscodeItem) baseSettingItem;
            setupSettingPasscode(settingPasscodeViewHolder, settingPasscodeItem);
        } else if (holder instanceof SettingLanguageViewHolder) {
            SettingLanguageViewHolder settingLanguageViewHolder = (SettingLanguageViewHolder) holder;
            SettingLanguageItem settingLanguageItem = (SettingLanguageItem) baseSettingItem;
            setttingLanguage(settingLanguageViewHolder, settingLanguageItem);
        } else if (holder instanceof SettingCurrencyViewHolder) {
            SettingCurrencyViewHolder settingCurrencyViewHolder = (SettingCurrencyViewHolder) holder;
            SettingCurrencyItem settingCurrencyItem = (SettingCurrencyItem) baseSettingItem;
            settingCurrency(settingCurrencyViewHolder, settingCurrencyItem);
        } else if (holder instanceof SettingClearDataViewHolder) {
            SettingClearDataViewHolder settingClearDataViewHolder = (SettingClearDataViewHolder) holder;
            SettingClearDataItem settingClearDataItem = (SettingClearDataItem) baseSettingItem;
            settingClearData(settingClearDataViewHolder, settingClearDataItem);
        }
    }

    private void settingClearData(SettingClearDataViewHolder settingClearDataViewHolder, SettingClearDataItem settingClearDataItem) {
        settingClearDataViewHolder.txt_title.setText(Contextor.getInstance().getContext().getString(R.string.clear_data));
        settingClearDataViewHolder.frm_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsAdapterListener.onSelectSettingItem(SettingType.TYPE_CLEAR_DATA);
            }
        });
    }

    private void settingCurrency(SettingCurrencyViewHolder settingCurrencyViewHolder, SettingCurrencyItem settingCurrencyItem) {
        settingCurrencyViewHolder.txt_title.setText(Contextor.getInstance().getContext().getString(R.string.currency));
        settingCurrencyViewHolder.frm_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsAdapterListener.onSelectSettingItem(SettingType.TYPE_CURRENCY);
            }
        });
    }

    private void setttingLanguage(SettingLanguageViewHolder settingLanguageViewHolder, SettingLanguageItem settingLanguageItem) {
        settingLanguageViewHolder.txt_title.setText(Contextor.getInstance().getContext().getString(R.string.language));
        if (settingLanguageItem.getLanguage().equals("th")) {
            settingLanguageViewHolder.txt_country.setText(R.string.thai);
            settingLanguageViewHolder.img_flag.setImageResource(R.drawable.flag_thai);
        } else if (settingLanguageItem.getLanguage().equals("en")) {
            settingLanguageViewHolder.txt_country.setText(R.string.english);
            settingLanguageViewHolder.img_flag.setImageResource(R.drawable.flag_us);
        }
        settingLanguageViewHolder.frm_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsAdapterListener.onSelectSettingItem(SettingType.TYPE_LANGUAGE);
            }
        });
    }

    private void setupSettingPasscode(SettingPasscodeViewHolder settingPasscodeViewHolder, SettingPasscodeItem settingPasscodeItem) {
        settingPasscodeViewHolder.txt_title.setText(Contextor.getInstance().getContext().getString(R.string.passcode_lock));
        settingPasscodeViewHolder.txt_change_passcode.setText(Contextor.getInstance().getContext().getString(R.string.change_passcode));
        settingPasscodeViewHolder.txt_forget_desc.setText(Contextor.getInstance().getContext().getString(R.string.forget_passcode));
        if (settingPasscodeItem.isChecked()) {
            settingPasscodeViewHolder.frm_change_passcode.setVisibility(View.VISIBLE);
        } else {
            settingPasscodeViewHolder.frm_change_passcode.setVisibility(View.GONE);
        }
        settingPasscodeViewHolder.frm_change_passcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsAdapterListener.onSelectSettingItem(SettingType.TYPE_ENABLED_PASSCODE);
            }
        });
        settingPasscodeViewHolder.switch_on_off.setOnCheckedChangeListener(null);
        settingPasscodeViewHolder.switch_on_off.setChecked(settingPasscodeItem.isChecked());
        settingPasscodeViewHolder.switch_on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    settingsAdapterListener.onSelectSettingItem(SettingType.TYPE_ENABLED_PASSCODE);
                } else {
                    settingsAdapterListener.onSelectSettingItem(SettingType.TYPE_DISABLED_PASSCODE);
                }
            }
        });
    }

    private void setupSettingMoney(SettingMoneyStartedViewHolder settingMoneyStartedViewHolder, SettingMoneyStartedItem settingItem) {
        settingMoneyStartedViewHolder.txt_title.setText(Contextor.getInstance().getContext().getString(R.string.money_started));
        settingMoneyStartedViewHolder.frm_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsAdapterListener.onSelectSettingItem(SettingType.TYPE_MONEY_STARTED);
            }
        });
        settingMoneyStartedViewHolder.txt_currency.setText(settingItem.getCurrency());
        settingMoneyStartedViewHolder.txt_money.setText(settingItem.getMoney());
    }

    @Override
    public int getItemCount() {
        if (baseSettingItems == null) return 0;
        return baseSettingItems.size();
    }

    public ArrayList<BaseSettingItem> getBaseSettingItems() {
        return baseSettingItems;
    }

    public void setBaseSettingItems(ArrayList<BaseSettingItem> baseSettingItems) {
        this.baseSettingItems = baseSettingItems;
        notifyDataSetChanged();
    }


}
