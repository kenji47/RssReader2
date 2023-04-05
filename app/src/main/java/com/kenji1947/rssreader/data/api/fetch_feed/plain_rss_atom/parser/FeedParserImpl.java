package com.kenji1947.rssreader.data.api.fetch_feed.plain_rss_atom.parser;

import com.kenji1947.rssreader.data.api.fetch_feed.plain_rss_atom.model.ApiFeed;

import java.io.InputStream;

import io.reactivex.Single;


public final class FeedParserImpl implements FeedParser {

    private final ParserWrapper externalParserWrapper;

    public FeedParserImpl(final ParserWrapper externalParserWrapper) {
        this.externalParserWrapper = externalParserWrapper;
    }

    @Override
    public Single<ApiFeed> parseFeed(final InputStream inputStream, final String feedUrl) {
        return Single.defer(() -> Single.just(externalParserWrapper.parseOrThrow(inputStream, feedUrl)));
    }
}
