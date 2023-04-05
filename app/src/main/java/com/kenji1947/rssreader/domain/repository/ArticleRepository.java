package com.kenji1947.rssreader.domain.repository;


import com.kenji1947.rssreader.domain.entities.Article;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by kenji1947 on 09.11.2017.
 */

public interface ArticleRepository {
    Single<List<Article>> updateArticles(String feedUrl);

    void notifyArticleModified();

    Observable<Boolean> observeArticlesModification();

    Single<List<Article>> getArticles(long feedId);

    Single<Article> getArticle(long articleId);

    Observable<List<Article>> getArticlesAndObserve(long feedId);

    Single<List<Article>> getFavouriteArticles();
    Completable markArticleAsRead(long articleId);
    Completable favouriteArticle(long articleId);
    Completable unFavouriteArticle(long articleId);
    Single<Long> getUnreadArticlesCount();

}
