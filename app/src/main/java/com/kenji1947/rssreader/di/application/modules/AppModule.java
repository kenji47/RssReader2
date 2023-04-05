package com.kenji1947.rssreader.di.application.modules;

import android.content.Context;
import android.content.res.Resources;

import com.kenji1947.rssreader.data.worker.error_handler.ErrorHandler;
import com.kenji1947.rssreader.data.worker.error_handler.ErrorHandlerImpl;
import com.kenji1947.rssreader.data.worker.resource_manager.ResourceManager;
import com.kenji1947.rssreader.data.worker.resource_manager.ResourceManagerImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;

/**
 * Created by kenji1947 on 11.11.2017.
 */

@Module
public class AppModule {
    private Context appContext;

    public AppModule(Context appContext) {
        this.appContext = appContext.getApplicationContext();
    }

    @Singleton
    @Provides
    public Context provideAppContext() {
        return appContext;
    }

    @Provides
    @Singleton
    Resources provideResources() {
        return appContext.getResources();
    }

    @Provides
    @Singleton
    ResourceManager provideResourceManager() {
        return new ResourceManagerImpl(appContext);
    }

    @Provides
    @Singleton
    ErrorHandler provideErrorHandler(ResourceManager resourceManager) {
        return new ErrorHandlerImpl(resourceManager);
    }

    public interface Exposes {
        Context provideContext();
        ResourceManager provideResourceManager();
        Resources resources();
        ErrorHandler provideErrorHandler();
//        Router provideRouter();
//        NavigatorHolder provideNavigatorHolder();
    }
}
