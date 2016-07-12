package com.geeksquad.jjamie.rubjai.component.module;

import com.geeksquad.jjamie.rubjai.repository.DatabaseRealm;
import com.geeksquad.jjamie.rubjai.repository.DataRepository;
import com.geeksquad.jjamie.rubjai.repository.impl.DataRepositoryImpl;

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