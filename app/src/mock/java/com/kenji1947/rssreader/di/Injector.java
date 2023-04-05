package com.kenji1947.rssreader.di;

import com.kenji1947.rssreader.data.api.fetch_feed.FetchFeedApiService;

import com.kenji1947.rssreader.data.api.fetch_feed.plain_rss_atom.FetchFeedApiServicePlain;
import com.kenji1947.rssreader.data.api.fetch_feed.plain_rss_atom.model.ApiConverter;
import com.kenji1947.rssreader.data.api.fetch_feed.plain_rss_atom.parser.FeedParser;
import com.kenji1947.rssreader.data.connectivity.ConnectivityManagerWrapper;
import com.kenji1947.rssreader.data.connectivity.NetworkUtils;
import com.kenji1947.rssreader.data.connectivity.NetworkUtilsImpl;
import com.kenji1947.rssreader.data.util.AppSchedulers;
import com.kenji1947.rssreader.domain.util.RxSchedulersProvider;

/**
 * Created by chamber on 19.12.2017.
 */

public class Injector {

    public static RxSchedulersProvider provideSchedulers() {
       return new AppSchedulers();
        //return new SchedulersTrampoline();
    }

    public static NetworkUtils provideNetworkUtils(ConnectivityManagerWrapper wrapper) {
        //return new NetworkUtilsFake();
        return new NetworkUtilsImpl(wrapper);
    }
    public static FetchFeedApiService provideFeedService(FeedParser feedParser, ApiConverter apiConverter) {
        return new FetchFeedApiServicePlain(feedParser, apiConverter);
        //return new FeedServiceFake();
    }
}
