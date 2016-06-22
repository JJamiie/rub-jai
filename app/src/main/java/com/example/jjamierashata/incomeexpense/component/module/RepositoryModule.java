package com.example.jjamierashata.incomeexpense.component.module;

import com.example.jjamierashata.incomeexpense.repository.DatabaseRealm;
import com.example.jjamierashata.incomeexpense.repository.DataRepository;
import com.example.jjamierashata.incomeexpense.repository.impl.DataRepositoryImpl;

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