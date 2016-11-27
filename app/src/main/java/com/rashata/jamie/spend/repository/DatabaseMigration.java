package com.rashata.jamie.spend.repository;


import android.util.Log;

import java.util.Date;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmObjectSchema;
import io.realm.RealmSchema;
import io.realm.annotations.PrimaryKey;

/**
 * Created by jjamierashata on 11/15/2016 AD.
 */

public class DatabaseMigration implements RealmMigration {

    private String TAG = "DatabaseMigration";

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();

        if (oldVersion == 0) {
            Log.d(TAG, "version: " + oldVersion);
            RealmObjectSchema expenseCategorySchema = schema.create("ExpenseCategory");
            expenseCategorySchema.addField("uuid", int.class, FieldAttribute.PRIMARY_KEY)
                    .addField("name", String.class)
                    .addField("picture", int.class)
                    .addField("show", boolean.class)
                    .addField("position", int.class);
            RealmObjectSchema incomeCategorySchema = schema.create("IncomeCategory");
            incomeCategorySchema.addField("uuid", int.class, FieldAttribute.PRIMARY_KEY)
                    .addField("name", String.class)
                    .addField("picture", int.class)
                    .addField("show", boolean.class)
                    .addField("position", int.class);
            oldVersion++;
        }
    }
}
