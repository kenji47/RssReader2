package com.kenji1947.rssreader.di.application.modules;

import android.support.annotation.NonNull;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kenji1947.rssreader.data.api.ApiRetrofit;
import com.kenji1947.rssreader.data.api.fetch_feed.FetchFeedApiService;
import com.kenji1947.rssreader.data.api.fetch_feed.feedly_api.FetchFeedApiServiceFeedly;
import com.kenji1947.rssreader.data.api.fetch_feed.plain_rss_atom.model.ApiConverter;
import com.kenji1947.rssreader.data.api.fetch_feed.plain_rss_atom.parser.EarlParserWrapper;
import com.kenji1947.rssreader.data.api.fetch_feed.plain_rss_atom.parser.ParserWrapper;
import com.kenji1947.rssreader.data.api.fetch_feed.plain_rss_atom.parser.FeedParser;
import com.kenji1947.rssreader.data.api.fetch_feed.plain_rss_atom.parser.FeedParserImpl;
import com.kenji1947.rssreader.data.api.search_feed.SearchFeedApiService;
import com.kenji1947.rssreader.data.api.search_feed.SearchFeedApiServiceImpl;
import com.kenji1947.rssreader.data.util.CurrentTimeProvider;
import com.kenji1947.rssreader.di.application.qualifiers.OkHttpInterceptors;
import com.kenji1947.rssreader.di.application.qualifiers.OkHttpNetworkInterceptors;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chamber on 10.12.2017.
 */

@Module
public class ApiModule {
    private final String baseUrl;

    public ApiModule(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    //fetch feed
    @Provides
    @Singleton
    FetchFeedApiService provideFeedService(FeedParser feedParser, ApiConverter apiConverter, ApiRetrofit searchFeedApi) {
        return new FetchFeedApiServiceFeedly(searchFeedApi);
        //return Injector.provideFeedService(feedParser, apiConverter);
    }


    @Provides
    @Singleton
    FeedParser provideFeedParser(final CurrentTimeProvider currentTimeProvider, final ParserWrapper externalParserWrapper) {
        return new FeedParserImpl(externalParserWrapper);
    }

    @Provides
    @Singleton
    ParserWrapper provideExternalParserWrapper(final CurrentTimeProvider currentTimeProvider) {
        return new EarlParserWrapper(currentTimeProvider);
    }

    //search feed
    @Provides
    @Singleton
    SearchFeedApiService searchFeedApiService(ApiRetrofit searchFeedApi) {
        return new SearchFeedApiServiceImpl(searchFeedApi);
    }

    //todo общее
    @Provides
    @Singleton
    ApiRetrofit searchFeedApi(Retrofit retrofit) {
        return retrofit.create(ApiRetrofit.class);
    }

    //retrofit
    @Provides
    @NonNull
    @Singleton
    public OkHttpClient provideOkHttpClient(@OkHttpInterceptors @NonNull List<Interceptor> interceptors,
                                            @OkHttpNetworkInterceptors @NonNull List<Interceptor> networkInterceptors) {
        final OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();

        //TODO Bubbble инжектит в ретрофит проверку интернета. Изучить

//        okHttpBuilder.addInterceptor(new NetworkCheckInterceptor(networkChecker));
//        okHttpBuilder.addInterceptor(new DribbbleTokenInterceptor(BuildConfig.DRIBBBLE_CLIENT_ACCESS_TOKEN));

        for (Interceptor interceptor : interceptors) {
            okHttpBuilder.addInterceptor(interceptor);
        }

        for (Interceptor networkInterceptor : networkInterceptors) {
            okHttpBuilder.addNetworkInterceptor(networkInterceptor);
        }

        return okHttpBuilder.build();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
        return new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }


    public interface Exposes {
        SearchFeedApiService searchFeedApiService();
        FeedParser feedParser();
        FetchFeedApiService feedService();
    }
}
