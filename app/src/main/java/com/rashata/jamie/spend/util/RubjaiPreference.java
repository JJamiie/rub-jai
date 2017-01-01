package com.rashata.jamie.spend.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

/**
 * Created by krerk on 5/2/16.
 */
public class RubjaiPreference {

    public String guideTour;
    public String initialData;
    public String newFeature1;
    private SharedPreferences preference;

    public RubjaiPreference(Context context) {
        preference = context.getSharedPreferences("Rubjai", Context.MODE_PRIVATE);
        guideTour = preference.getString("guideTour", "");
        initialData = preference.getString("initialData", "");
        newFeature1 = preference.getString("newFeature1", "");
    }

    public void update() {
        SharedPreferences.Editor edit = preference.edit();
        edit.putString("guideTour", guideTour);
        edit.putString("initialData", initialData);
        edit.putString("newFeature1", newFeature1);
        edit.commit();
    }


}
