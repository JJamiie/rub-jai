package com.rashata.jamie.spend.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by krerk on 5/2/16.
 */
public class RubjaiPreference {

    public String guideTour;
    public String initialData;
    public String newFeature1;
    private SharedPreferences preference;
    public boolean passcode_en;
    public String passcode;
    public String language;
    public String currency;

    public RubjaiPreference(Context context) {
        preference = context.getSharedPreferences("Rubjai", Context.MODE_PRIVATE);
        guideTour = preference.getString("guideTour", "");
        initialData = preference.getString("initialData", "");
        newFeature1 = preference.getString("newFeature1", "");
        passcode_en = preference.getBoolean("passcode_en", false);
        passcode = preference.getString("passcode", "");
        language = preference.getString("language", "th");
        currency = preference.getString("currency", "THB");
    }

    public void update() {
        SharedPreferences.Editor edit = preference.edit();
        edit.putString("guideTour", guideTour);
        edit.putString("initialData", initialData);
        edit.putString("newFeature1", newFeature1);
        edit.putBoolean("passcode_en", passcode_en);
        edit.putString("passcode", passcode);
        edit.putString("language", language);
        edit.putString("currency", currency);
        edit.commit();
    }


}
