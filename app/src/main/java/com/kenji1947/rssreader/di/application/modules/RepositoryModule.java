package com.kenji1947.rssreader.di.application.modules;

import com.kenji1947.rssreader.data.api.fetch_feed.FetchFeedApiService;
import com.kenji1947.rssreader.data.api.search_feed.SearchFeedApiService;
import com.kenji1947.rssreader.data.database.ArticleDao;
import com.kenji1947.rssreader.data.database.FeedDao;
import com.kenji1947.rssreader.data.repository.AppSettingsRepositoryImpl;
import com.kenji1947.rssreader.data.worker.preference.PreferenceManager;
import com.kenji1947.rssreader.domain.repository.AppSettingsRepository;
import com.kenji1947.rssreader.domain.repository.ArticleRepository;
import com.kenji1947.rssreader.data.repository.ArticleRepositoryImpl;
import com.kenji1947.rssreader.domain.repository.FeedRepository;
import com.kenji1947.rssreader.data.repository.FeedRepositoryImpl;
import com.kenji1947.rssreader.domain.util.RxSchedulersProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kenji1947 on 11.11.2017.
 */

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    FeedRepository provideFeedRepository(FetchFeedApiService feedService,
                                         FeedDao feedDao,
                                         RxSchedulersProvider schedulersProvider,
                                         PreferenceManager preferenceManager,
                                         SearchFeedApiService searchFeedApiService) {
        return new FeedRepositoryImpl(feedService, feedDao, schedulersProvider, preferenceManager, searchFeedApiService);
    }

    @Provides
    @Singleton
    ArticleRepository provideArticleRepository(ArticleDao articleDao,
                                               RxSchedulersProvider schedulersProvider,
                                               FetchFeedApiService fetchFeedApiService) {
        return new ArticleRepositoryImpl(articleDao, schedulersProvider, fetchFeedApiService);
    }

    @Provides
    @Singleton
    AppSettingsRepository provideAppSettingsRepository(PreferenceManager preferenceManager,
                                                       RxSchedulersProvider rxSchedulersProvider) {
        return new AppSettingsRepositoryImpl(rxSchedulersProvider, preferenceManager);
    }


    public interface Exposes {
        FeedRepository provideFeedRepository();
        ArticleRepository provideArticleRepository();
        AppSettingsRepository provideAppSettingsRepository();
    }
}
