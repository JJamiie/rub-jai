package com.rashata.jamie.spend.component.module;

import com.rashata.jamie.spend.repository.DatabaseRealm;
import com.rashata.jamie.spend.repository.DataRepository;
import com.rashata.jamie.spend.repository.impl.DataRepositoryImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jjamierashata on 6/16/16 AD.
 */
@Module
public class RepositoryModule {

    @Provides
    @Singleton
    public DataRepository provideDataRepository() {
        return new DataRepositoryImpl();
    }

    @Provides
    @Singleton
    public DatabaseRealm provideDatabaseRealm() {
        return new DatabaseRealm();
    }
}