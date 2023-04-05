package com.kenji1947.rssreader.data.database.objectbox;

import com.kenji1947.rssreader.data.database.ArticleDao;
import com.kenji1947.rssreader.data.database.objectbox.converter.FeedObjectBoxConverter;
import com.kenji1947.rssreader.data.database.objectbox.model.ArticleModelObjectBox;
import com.kenji1947.rssreader.data.database.objectbox.model.ArticleModelObjectBox_;
import com.kenji1947.rssreader.domain.entities.Article;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.query.Query;
import io.objectbox.rx.RxQuery;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by kenji1947 on 11.11.2017.
 */

public class ArticleDaoObjectBox implements ArticleDao{
    private Box<ArticleModelObjectBox> articleBox;
    private FeedObjectBoxConverter converter;

    public ArticleDaoObjectBox(Box<ArticleModelObjectBox> articleBox, FeedObjectBoxConverter converter) {
        this.articleBox = articleBox;
        this.converter = converter;
    }

    @Override
    public Observable<List<Article>> getArticlesAndObserve(long feedId) {
        Query<ArticleModelObjectBox> queryArticleByFeedId = articleBox.query()
                .equal(ArticleModelObjectBox_.feedId, feedId)
                .orderDesc(ArticleModelObjectBox_.publicationDate)
                .build();
        return RxQuery.observable(queryArticleByFeedId)
                .map(articleModelObjectBoxes -> {
                    List<Article> articles = new ArrayList<>();
                    for (ArticleModelObjectBox a : articleModelObjectBoxes) {
                        articles.add(converter.dbToDomain(a));
                    }
                    return articles;
                });
                //.flatMap(Observable::fromIterable)
                //.map(articleModel -> converter.dbToDomain(articleModel))
                //.toList();
    }

    @Override
    public Single<List<Article>> getArticles(long feedId) {
        Query<ArticleModelObjectBox> queryArticleByFeedId = articleBox.query()
                .equal(ArticleModelObjectBox_.feedId, feedId)
                .orderDesc(ArticleModelObjectBox_.publicationDate)
                .build();

//        Single.defer(() -> {
//            CommonUtils.longOperation();
//            return Single.just(queryArticleByFeedId.find());
//        });
        return Single
                .fromCallable(queryArticleByFeedId::find)
                .toObservable()
                .flatMap(Observable::fromIterable)
                .map(articleModel -> converter.dbToDomain(articleModel))
                .toList();
    }

    @Override
    public Single<Article> getArticle(long articleId) {
        return Single
                .fromCallable(() -> articleBox.get(articleId))
                .map(articleModelObjectBox -> converter.dbToDomain(articleModelObjectBox));
    }

    @Override
    public Single<List<Article>> getFavouriteArticles() {
        Query<ArticleModelObjectBox> queryArticleFav = articleBox.query()
                .equal(ArticleModelObjectBox_.isFavourite, true)
                .orderDesc(ArticleModelObjectBox_.publicationDate)
                .build();

        return Single
                .fromCallable(queryArticleFav::find)
                .toObservable()
                .flatMap(Observable::fromIterable)
                .map(articleModel -> converter.dbToDomain(articleModel))
                .toList();
    }

    @Override
    public Completable markArticleAsRead(long articleId) {
        return Completable.fromAction(() -> {
            ArticleModelObjectBox article = articleBox.get(articleId);
            article.setNew(false);
            articleBox.put(article);
        });
    }

    @Override
    public Completable favouriteArticle(long articleId) {
        return Completable.fromAction(() -> {
            ArticleModelObjectBox article = articleBox.get(articleId);
            article.setFavourite(true);
            articleBox.put(article);
        });
    }

    @Override
    public Completable unFavouriteArticle(long articleId) {
        return Completable.fromAction(() -> {
            ArticleModelObjectBox article = articleBox.get(articleId);
            article.setFavourite(false);
            articleBox.put(article);
        });
    }

    @Override
    public Single<Long> getUnreadArticlesCount() {
        return Single.fromCallable(() ->
                articleBox.query()
                .equal(ArticleModelObjectBox_.isNew, true)
                .build()
                .count()
        );
    }
}
