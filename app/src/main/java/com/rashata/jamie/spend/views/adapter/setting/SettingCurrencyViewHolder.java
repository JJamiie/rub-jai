package com.rashata.jamie.spend.views.adapter.setting;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rashata.jamie.spend.R;

/**
 * Created by jjamierashata on 3/1/2017 AD.
 */

public class SettingCurrencyViewHolder extends RecyclerView.ViewHolder {
    public TextView txt_title;
    public FrameLayout frm_setting;

    public SettingCurrencyViewHolder(View itemView) {
        super(itemView);
        txt_title = (TextView) itemView.findViewById(R.id.txt_title);
        frm_setting = (FrameLayout) itemView.findViewById(R.id.frm_setting);
    }
}
