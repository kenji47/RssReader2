package com.kenji1947.rssreader.data.api.fetch_feed.plain_rss_atom;

import com.kenji1947.rssreader.data.api.fetch_feed.FetchFeedApiService;
import com.kenji1947.rssreader.data.api.fetch_feed.plain_rss_atom.model.ApiConverter;
import com.kenji1947.rssreader.data.api.fetch_feed.plain_rss_atom.parser.FeedParser;
import com.kenji1947.rssreader.domain.entities.Article;
import com.kenji1947.rssreader.domain.entities.Feed;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import io.reactivex.Single;
import timber.log.Timber;


public final class FetchFeedApiServicePlain implements FetchFeedApiService {

    private final FeedParser feedParser;
    private ApiConverter apiConverter;

    public FetchFeedApiServicePlain(final FeedParser feedParser, ApiConverter apiConverter) {
        this.feedParser = feedParser;
        this.apiConverter = apiConverter;
    }


    @Override
    public Single<Feed> fetchFeed(final String feedUrl) {
        Timber.d("fetchFeed " + feedUrl);

        //TODO
        return Single.using(
                () -> new URL(feedUrl).openConnection().getInputStream(),
                inputStream -> parse(inputStream, feedUrl),
                InputStream::close
        );

//        final HttpURLConnection connection = (HttpURLConnection) new URL(feedUrl).openConnection();
//
//        return Single.fromCallable(() -> {
//            //HttpURLConnection connection = (HttpURLConnection) new URL(feedUrl).openConnection();
//            connection.setConnectTimeout(5000);
//            connection.setReadTimeout(5000);
//            connection.connect();
//            Timber.d("after conncect");
//            return connection.getInputStream();
//            //return new URL(feedUrl).openConnection().setConnectTimeout(3).getInputStream();
//        })
//                //.doFinally(() -> {Timber.d("doFinally disc 1"); connection.disconnect(); })
//                .doOnDispose(() -> {Timber.d("doOnDispose"); connection.disconnect();})
//                .flatMap(inputStream -> {
//                    Timber.d("flatMap 1");
//                    return parse(connection, inputStream, feedUrl);}
//                    );




//        try {
//            final InputStream inputStream = new URL(feedUrl).openConnection().getInputStream();
//            Timber.d("after getInputStream");
//
//
//
//            return feedParser.parseFeed(inputStream, feedUrl)
//                    .doOnSuccess(feed -> closeStream(inputStream)) //TODO Перенести в finally?
//                    .doOnError(throwable -> closeStream(inputStream))
//                    .map(apiFeed -> apiConverter.apiToDomain(apiFeed));
//        } catch (final IOException e) {
//            Timber.e(e);
//            return Single.error(e);
//        }
    }

    @Override
    public Single<List<Article>> fetchArticles(String feedUrl) {
        return null;
    }

    private Single<Feed> parse(InputStream inputStream, String feedUrl) {
        Timber.d("parse");
        return feedParser.parseFeed(inputStream, feedUrl)
//                .doOnSuccess(feed -> closeStream(inputStream)) //TODO Перенести в finally?
//                .doOnError(throwable -> closeStream(inputStream))
//                .doFinally(() -> {Timber.d("doFinally disc"); inputStream.close();
                .map(apiFeed -> apiConverter.apiToDomain(apiFeed));
    }

    private void closeStream(final InputStream inputStream) {
        try {
            inputStream.close();
        } catch (final IOException e) {
            Timber.e(e);
        }
    }
}
