package com.example.jjamierashata.incomeexpense.repository;

import android.content.Context;
import com.example.jjamierashata.incomeexpense.component.Injector;
import java.util.List;
import javax.inject.Inject;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;

/**
 * Created by jjamierashata on 6/16/16 AD.
 */
public class DatabaseRealm {

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

    public <T extends RealmObject> T add(T model) {
        Realm realm = getRealmInstance();
        realm.beginTransaction();
        realm.copyToRealm(model);
        realm.commitTransaction();
        return model;
    }

    public <T extends RealmObject> List<T> findAll(Class<T> clazz) {
        return getRealmInstance().where(clazz).findAll();
    }


}
