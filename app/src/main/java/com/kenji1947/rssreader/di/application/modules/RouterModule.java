package com.kenji1947.rssreader.di.application.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;

/**
 * Created by kenji1947 on 12.11.2017.
 */

@Module
public class RouterModule {
    private Cicerone<Router> cicerone;

    public RouterModule() {
        cicerone = Cicerone.create();
    }

    @Provides
    @Singleton
    Router provideRouter() {
        return cicerone.getRouter();
    }

    @Provides
    @Singleton
    NavigatorHolder provideNavigationHolder() {
        return cicerone.getNavigatorHolder();
    }

    public interface Exposes {
        Router provideRouter();
        NavigatorHolder provideNavigationHolder();
    }
}
