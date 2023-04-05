package com.kenji1947.rssreader.data.api.search_feed;

import com.kenji1947.rssreader.domain.entities.Feed;
import com.kenji1947.rssreader.domain.entities.SearchedFeed;

import java.io.IOException;
import java.util.List;

import io.reactivex.Single;

/**
 * Created by chamber on 26.02.2018.
 */

public interface SearchFeedApiService {
    Single<List<SearchedFeed>> searchFeed(String feed);
}
