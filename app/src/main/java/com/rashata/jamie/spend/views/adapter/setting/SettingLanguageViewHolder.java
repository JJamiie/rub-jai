package com.rashata.jamie.spend.views.adapter.setting;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.rashata.jamie.spend.R;

/**
 * Created by jjamierashata on 3/1/2017 AD.
 */

public class SettingLanguageViewHolder extends RecyclerView.ViewHolder {
    public FrameLayout frm_setting;
    public TextView txt_country;
    public TextView txt_title;
    public ImageView img_flag;

    public SettingLanguageViewHolder(View itemView) {
        super(itemView);
        frm_setting = (FrameLayout) itemView.findViewById(R.id.frm_setting);
        txt_country = (TextView) itemView.findViewById(R.id.txt_country);
        txt_title = (TextView) itemView.findViewById(R.id.txt_title);
        img_flag = (ImageView) itemView.findViewById(R.id.img_flag);
    }

}
