package com.rashata.jamie.spend.repository;

import com.rashata.jamie.spend.Contextor;
import com.rashata.jamie.spend.repository.impl.DataRepositoryImpl;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by jjamierashata on 6/16/16 AD.
 */
public class RealmManager {
    private static RealmManager instance;
    private DataRepository dataRepository;
    private RealmConfiguration realmConfiguration;


    private RealmManager() {
        setup();
        dataRepository = new DataRepositoryImpl();
    }


    public static RealmManager getInstance() {
        if (instance == null)
            instance = new RealmManager();
        return instance;
    }

    private void setup() {
        if (realmConfiguration == null) {
            Realm.init(Contextor.getInstance().getContext());
            realmConfiguration = new RealmConfiguration.Builder()
                    .name("rubjai.realm")
                    .schemaVersion(1)
                    .migration(new DatabaseMigration())
                    .build();
            Realm.setDefaultConfiguration(realmConfiguration);
        } else {
            throw new IllegalStateException("database already configured");
        }
    }

    public DataRepository getDataRepository() {
        return dataRepository;
    }

    public void setDataRepository(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }
}
