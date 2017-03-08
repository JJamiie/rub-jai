package com.rashata.jamie.spend.views.adapter.setting.item;

/**
 * Created by jjamierashata on 3/1/2017 AD.
 */

public class SettingLanguageItem extends BaseSettingItem {
    private String language;
    public SettingLanguageItem() {
        super(SettingType.TYPE_LANGUAGE);
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
}
