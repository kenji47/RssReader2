package com.kenji1947.rssreader.di.presenter.modules;

import com.kenji1947.rssreader.di.presenter.PresenterScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chamber on 14.12.2017.
 */

@Module
public class ArticleListPresenterModule {
    private long feedId;
    private boolean isFavModeOn;

    public ArticleListPresenterModule(long feedId, boolean isFavModeOn) {
        this.feedId = feedId;
        this.isFavModeOn = isFavModeOn;
    }

    @Provides
    @PresenterScope
    long provideFeedId() {
        return feedId;
    }

    @Provides
    @PresenterScope
    boolean provideFavModeStatus() {
        return isFavModeOn;
    }
}
