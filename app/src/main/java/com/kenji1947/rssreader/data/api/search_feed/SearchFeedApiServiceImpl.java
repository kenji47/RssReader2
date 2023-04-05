package com.kenji1947.rssreader.data.api.search_feed;

import com.kenji1947.rssreader.data.api.ApiRetrofit;
import com.kenji1947.rssreader.data.api.search_feed.model.FeedSearchItem;
import com.kenji1947.rssreader.domain.entities.Feed;
import com.kenji1947.rssreader.domain.entities.SearchedFeed;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;

/**
 * Created by chamber on 26.02.2018.
 */

public class SearchFeedApiServiceImpl implements SearchFeedApiService {
    private ApiRetrofit searchFeedApi;

    public SearchFeedApiServiceImpl(ApiRetrofit searchFeedApi) {
        this.searchFeedApi = searchFeedApi;
    }

    @Override
    public Single<List<SearchedFeed>> searchFeed(String feedName) {
        return searchFeedApi.searchFeed(feedName)
                .map(feedSearchResponse -> {
                    List<SearchedFeed> searchedFeeds = new ArrayList<>();
                    for (FeedSearchItem item : feedSearchResponse.getResults()) {
                        searchedFeeds.add(new SearchedFeed(
                                item.getTitle(),
                                item.getIconUrl(),
                                item.getWebsite(),
                                item.getDescription(),
                                item.getFeedId()));
                    }
                    return searchedFeeds;
                });
    }
}
