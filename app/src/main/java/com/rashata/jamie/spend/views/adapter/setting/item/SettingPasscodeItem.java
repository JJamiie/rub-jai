package com.rashata.jamie.spend.views.adapter.setting.item;

/**
 * Created by jjamierashata on 3/1/2017 AD.
 */

public class SettingPasscodeItem extends BaseSettingItem {
    private boolean isChecked;
    public SettingPasscodeItem() {
        super(SettingType.TYPE_PASSCODE);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
