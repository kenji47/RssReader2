package com.kenji1947.rssreader.di.application.modules;

import android.content.Context;

import com.kenji1947.rssreader.data.util.CurrentTimeProvider;
import com.kenji1947.rssreader.data.util.CurrentTimeProviderImpl;
import com.kenji1947.rssreader.data.worker.image_loader.ImageLoader;
import com.kenji1947.rssreader.data.worker.image_loader.ImageLoaderGlide;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chamber on 11.12.2017.
 */

@Module
public class UtilsModule {
    @Provides
    @Singleton
    ImageLoader provideImageLoader(Context context) {
        return new ImageLoaderGlide(context);
    }

    @Provides
    @Singleton
    CurrentTimeProvider provideCurrentTimeProvider() {
        return new CurrentTimeProviderImpl();
    }

    public interface Exposes {
        ImageLoader provideImageLoader();
        CurrentTimeProvider currentTimeProvider();
    }
}
