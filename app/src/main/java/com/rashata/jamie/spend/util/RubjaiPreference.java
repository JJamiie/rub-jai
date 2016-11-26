package com.rashata.jamie.spend.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

/**
 * Created by krerk on 5/2/16.
 */
public class RubjaiPreference {

    public String guideTour;
    private SharedPreferences preference;

    public RubjaiPreference(Context context) {
        preference = context.getSharedPreferences("Rubjai", Context.MODE_PRIVATE);
        guideTour = preference.getString("guideTour", "");
    }

    public void update() {
        SharedPreferences.Editor edit = preference.edit();
        edit.putString("guideTour", guideTour);
        edit.commit();
    }

}
