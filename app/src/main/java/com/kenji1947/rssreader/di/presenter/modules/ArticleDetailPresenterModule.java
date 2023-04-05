package com.kenji1947.rssreader.di.presenter.modules;

import com.kenji1947.rssreader.di.presenter.PresenterScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chamber on 01.03.2018.
 */

@Module
public class ArticleDetailPresenterModule {
    private long articleId;

    public ArticleDetailPresenterModule(long articleId) {
        this.articleId = articleId;
    }

    @Provides
    @PresenterScope
    long provideFeedId() {
        return articleId;
    }
}
