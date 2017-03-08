package com.rashata.jamie.spend.views.adapter.setting;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rashata.jamie.spend.R;

/**
 * Created by jjamierashata on 3/1/2017 AD.
 */

public class SettingMoneyStartedViewHolder extends RecyclerView.ViewHolder {
    public TextView txt_title;
    public TextView txt_money;
    public FrameLayout frm_setting;

    public SettingMoneyStartedViewHolder(View itemView) {
        super(itemView);
        txt_title = (TextView) itemView.findViewById(R.id.txt_title);
        txt_money = (TextView) itemView.findViewById(R.id.txt_money);
        frm_setting = (FrameLayout) itemView.findViewById(R.id.frm_setting);
    }
}
