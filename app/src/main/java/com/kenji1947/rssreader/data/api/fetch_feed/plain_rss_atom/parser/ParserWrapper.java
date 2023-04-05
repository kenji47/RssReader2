package com.kenji1947.rssreader.data.api.fetch_feed.plain_rss_atom.parser;

import com.kenji1947.rssreader.data.api.fetch_feed.plain_rss_atom.model.ApiFeed;

import java.io.InputStream;


public interface ParserWrapper {

    ApiFeed parseOrThrow(final InputStream inputStream, final String feedUrl) throws Exception;

    ApiFeed parse(final InputStream inputStream, final String feedUrl);
}
