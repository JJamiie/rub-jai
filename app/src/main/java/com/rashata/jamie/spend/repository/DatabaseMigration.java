package com.rashata.jamie.spend.repository;


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

    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        RealmSchema schema = realm.getSchema();
        if (oldVersion == 0) {

            RealmObjectSchema dataSchema = schema.get("Data");
            dataSchema.addField("uuid", int.class, FieldAttribute.PRIMARY_KEY)
                    .addField("money", double.class)
                    .addField("note", String.class)
                    .addField("catagory", int.class)
                    .addField("date", Date.class)
                    .addField("type", int.class);

            RealmObjectSchema initialSchema = schema.get("Initial");
            initialSchema.addField("uuid", int.class, FieldAttribute.PRIMARY_KEY)
                    .addField("money", double.class);

            oldVersion++;
        }
    }
}
