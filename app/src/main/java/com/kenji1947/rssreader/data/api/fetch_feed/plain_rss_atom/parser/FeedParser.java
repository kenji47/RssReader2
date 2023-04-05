package com.kenji1947.rssreader.data.api.fetch_feed.plain_rss_atom.parser;

import com.kenji1947.rssreader.data.api.fetch_feed.plain_rss_atom.model.ApiFeed;

import java.io.InputStream;

import io.reactivex.Single;


public interface FeedParser {

    Single<ApiFeed> parseFeed(InputStream inputStream, String feedUrl);
}
