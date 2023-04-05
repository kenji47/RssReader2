package com.kenji1947.rssreader.fakes;

import com.kenji1947.rssreader.data.api.fetch_feed.FetchFeedApiService;
import com.kenji1947.rssreader.domain.entities.Article;
import com.kenji1947.rssreader.domain.entities.Feed;

import java.util.List;

import io.reactivex.Single;
import timber.log.Timber;


/**
 * Created by chamber on 06.12.2017.
 */

public class FeedServiceFake implements FetchFeedApiService {

    @Override
    public Single<Feed> fetchFeed(String feedUrl) {
        Timber.d("fetchFeed " + feedUrl);

        switch (AppSetup.feedApiServiceState) {

            case RETURN_NEW_ARTICLES: {
                Timber.d("RETURN_NEW_ARTICLES");
                return Single.just(DataLab.generateFeedWithNewArticles(feedUrl, 3));
            }

            case RETURN_SAME_FEED: return Single.just(DataLab.generateFeed(feedUrl));

            case ERROR: return Single.error(new RuntimeException());

            default: return Single.just(DataLab.generateFeed(feedUrl));
        }
    }

    @Override
    public Single<List<Article>> fetchArticles(String feedUrl) {
        return null;
    }
}
