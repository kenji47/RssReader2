package com.kenji1947.rssreader.di.presenter.modules;

import com.kenji1947.rssreader.di.presenter.PresenterScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chamber on 05.12.2017.
 */

@Module
public class FeedListPresenterModule {
    private long id;

    public FeedListPresenterModule(long id) {
        this.id = id;
    }

    @PresenterScope
    @Provides
    long provideId() {return id;}
}
