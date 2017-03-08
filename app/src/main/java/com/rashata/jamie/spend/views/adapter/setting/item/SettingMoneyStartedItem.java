package com.rashata.jamie.spend.views.adapter.setting.item;

/**
 * Created by jjamierashata on 3/1/2017 AD.
 */

public class SettingMoneyStartedItem extends BaseSettingItem {
    private String money;

    public SettingMoneyStartedItem( ) {
        super(SettingType.TYPE_MONEY_STARTED);
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
