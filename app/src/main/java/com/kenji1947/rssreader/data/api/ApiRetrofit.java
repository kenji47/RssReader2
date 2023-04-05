package com.kenji1947.rssreader.data.api;

import com.kenji1947.rssreader.data.api.fetch_feed.feedly_api.feed_articles_model.FeedArticlesResponse;
import com.kenji1947.rssreader.data.api.fetch_feed.feedly_api.feed_metadata_model.FeedMetadata;
import com.kenji1947.rssreader.data.api.search_feed.model.FeedSearchResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by chamber on 26.02.2018.
 */

public interface ApiRetrofit {


    //TODO Увеличить количество выдачи
    //https://developer.feedly.com/v3/search/
    @GET("/v3/search/feeds")
    Single<FeedSearchResponse> searchFeed(@Query("query") String name);


    //TODO Добавить пагинацию
    //https://developer.feedly.com/v3/streams/
    @GET("/v3/streams/contents")
    Single<FeedArticlesResponse> getFeedArticles(@Query("streamId") String streamId);


    @GET("v3/feeds/{feedId}")
    Single<FeedMetadata> geFeedMetadata(@Path("feedId") String feedId);
}
