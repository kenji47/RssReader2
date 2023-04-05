package com.kenji1947.rssreader.data.api.fetch_feed;



import com.kenji1947.rssreader.domain.entities.Article;
import com.kenji1947.rssreader.domain.entities.Feed;

import java.io.IOException;
import java.util.List;

import io.reactivex.Single;

public interface FetchFeedApiService {

    //TODO Delete.
    //TODO Перевести plain_api на fetchArticles
    //Используется как для создания фида, так и для обновления статей
    Single<Feed> fetchFeed(String feedUrl) throws IOException;

    //Uses for updating articles in ArticleList
    Single<List<Article>> fetchArticles(String feedUrl);
}
