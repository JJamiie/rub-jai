package com.rashata.jamie.spend.repository;

import android.content.Context;
import com.rashata.jamie.spend.component.Injector;
import javax.inject.Inject;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by jjamierashata on 6/16/16 AD.
 */
public class DatabaseRealm {

    private static final String TAG = "DatabaseRealm";
    @Inject
    Context mContext;

    RealmConfiguration realmConfiguration;

    public DatabaseRealm() {
        Injector.getApplicationComponent().inject(this);
    }

    public void setup() {
        if (realmConfiguration == null) {
            realmConfiguration = new RealmConfiguration.Builder(mContext).deleteRealmIfMigrationNeeded().build();
            Realm.setDefaultConfiguration(realmConfiguration);
        } else {
            throw new IllegalStateException("database already configured");
        }
    }

    public Realm getRealmInstance() {
        return Realm.getDefaultInstance();
    }

}
