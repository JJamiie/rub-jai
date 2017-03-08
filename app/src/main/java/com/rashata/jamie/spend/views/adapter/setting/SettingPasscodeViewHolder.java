package com.rashata.jamie.spend.views.adapter.setting;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rashata.jamie.spend.R;

/**
 * Created by jjamierashata on 3/1/2017 AD.
 */

public class SettingPasscodeViewHolder extends RecyclerView.ViewHolder {
    public FrameLayout frm_setting;
    public TextView txt_title;
    public TextView txt_change_passcode;
    public TextView txt_forget_desc;
    public SwitchCompat switch_on_off;
    public FrameLayout frm_change_passcode;


    public SettingPasscodeViewHolder(View itemView) {
        super(itemView);
        txt_title = (TextView) itemView.findViewById(R.id.txt_title);
        txt_change_passcode = (TextView) itemView.findViewById(R.id.txt_change_passcode);
        txt_forget_desc = (TextView) itemView.findViewById(R.id.txt_forget_desc);
        frm_setting = (FrameLayout) itemView.findViewById(R.id.frm_setting);
        switch_on_off = (SwitchCompat) itemView.findViewById(R.id.switch_on_off);
        frm_change_passcode = (FrameLayout) itemView.findViewById(R.id.frm_change_passcode);
    }
}
