package com.kenji1947.rssreader.data.database.objectbox;

import com.kenji1947.rssreader.data.database.CommonUtils;
import com.kenji1947.rssreader.data.database.FeedDao;
import com.kenji1947.rssreader.data.database.objectbox.converter.FeedObjectBoxConverter;
import com.kenji1947.rssreader.data.database.objectbox.model.ArticleModelObjectBox;
import com.kenji1947.rssreader.data.database.objectbox.model.FeedModelObjectBox;
import com.kenji1947.rssreader.data.database.objectbox.model.FeedModelObjectBox_;
import com.kenji1947.rssreader.domain.entities.Article;
import com.kenji1947.rssreader.domain.entities.Feed;
import com.kenji1947.rssreader.domain.exceptions.NoNetworkException;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;
import io.objectbox.rx.RxQuery;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import timber.log.Timber;

/**
 * Created by kenji1947 on 11.11.2017.
 */

public class FeedDaoObjectBox implements FeedDao{
    private Box<FeedModelObjectBox> feedBox;
    private FeedObjectBoxConverter converter;
    private Box<ArticleModelObjectBox> articleBox;
    private BoxStore boxStore;

    private Query<FeedModelObjectBox> feedQuery;

    public FeedDaoObjectBox(Box<FeedModelObjectBox> feedBox,
                            Box<ArticleModelObjectBox> articleBox,
                            FeedObjectBoxConverter converter,
                            BoxStore boxStore) {
        this.feedBox = feedBox;
        this.articleBox = articleBox;
        this.boxStore = boxStore;
        this.converter = converter;

        this.feedQuery = feedBox.query().build();
    }


    @Override
    public Single<List<Feed>> getFeeds() throws NoNetworkException {
        //throw new NoNetworkException("asd");
        return Single.fromCallable(() -> { //TODO БУдет ли передано исключение?
            CommonUtils.longOperation();
            return feedBox.getAll();
        })
                .toFlowable() //TODO Observable vs Flowable
                .flatMap(Flowable::fromIterable)
                .map(feedModel -> converter.dbToDomain(feedModel))
                .toList();
    }

    @Override
    public Observable<List<Feed>> getFeedsAndObserve() {
        Query<Feed> query;
        return RxQuery.observable(feedQuery)
                .map(feedModelObjectBoxes -> {
                    List<Feed> feedList = new ArrayList<>();
                    for (FeedModelObjectBox dbFeed : feedModelObjectBoxes) {
                       feedList.add(converter.dbToDomain(dbFeed));
                    }
                    return feedList;
                });
//                .flatMap(Observable::fromIterable) //TODO Это бесконечный источник
//                .map(feedModel -> converter.dbToDomain(feedModel))
//                .toList().toObservable();
    }

    @Override
    public Single<Feed> getFeed(long id) {
        return Single.fromCallable(() -> feedBox.get(id))
                .map(feedModel -> converter.dbToDomain(feedModel));
    }

    //--
    @Override
    public Single<Boolean> doesFeedExist(final String feedUrl) {
        return Single.defer(() -> Single.just(innerDoesFeedExist(feedUrl)));
    }

    private boolean innerDoesFeedExist(final String feedUrl) {
        return !feedBox.query()
                .equal(FeedModelObjectBox_.url, feedUrl)
                .build()
                .find()
                .isEmpty();
    }

    //---

    @Override
    public Completable insertFeed(Feed feed) {
        Timber.d("insertFeed " + feed.url);
        return Completable.fromAction(() -> innerInsertFeed(feed));
    }

    private void innerInsertFeed(final Feed feed) {
        FeedModelObjectBox feedModelObjectBox = converter.domainToDb(feed);

        feedModelObjectBox.getArticles().addAll(FeedObjectBoxConverter.ConverterHelper
                .convertDomainArticleListToDb(feed.articles, converter));
        feedBox.put(feedModelObjectBox);
    }


//    @Override
//    public Completable insertFeed(ApiFeed apiFeed) {
//        Timber.d("insertFeed " + apiFeed.url);
//        return Completable.fromAction(() -> innerInsertFeed(apiFeed));
//    }
//
//    private void innerInsertFeed(final ApiFeed apiFeed) {
//        FeedModelObjectBox feedModelObjectBox = converter.apiToDb(apiFeed);
//
//        //convert Articles from api to db model
////        FeedObjectBoxConverter.ConverterHelper.convertApiArticleListToDb(apiFeed.articles, converter);
////        List<ArticleModelObjectBox> articleModelObjectBoxList = new ArrayList<>();
////        for (ApiArticle apiArticle : apiFeed.articles) {
////            articleModelObjectBoxList.add(converter.apiToDb(apiArticle));
////        }
//
//        feedModelObjectBox.getArticles().addAll(FeedObjectBoxConverter.ConverterHelper
//                                .convertApiArticleListToDb(apiFeed.articles, converter));
//        feedBox.put(feedModelObjectBox);
//    }
    //---
//    @Override
//    public Completable updateFeed(final long feedId, List<ApiArticle> apiArticles) {
//        return Completable.fromAction(() -> innerUpdateFeed(feedId, apiArticles));
//    }
//    private void innerUpdateFeed(final long feedId, List<ApiArticle> apiArticles) {
//        FeedModelObjectBox feed = feedBox.get(feedId);
//        //TODO If articles.size == 0 -> skip?
//        for (ApiArticle apiArticle: apiArticles) {
//            ArticleModelObjectBox articleDb = converter.apiToDb(apiArticle);
//            feed.getArticles().add(articleDb);
//        }
//        feedBox.put(feed);
//    }

    @Override
    public Completable updateFeed(final long feedId, List<Article> articles) {
        Timber.d("updateFeedDomain feedId " + feedId);
        return Completable.fromAction(() -> innerUpdateFeedDomain(feedId, articles));
    }

    @Override
    public Completable updateFeeds(List<Feed> feeds) {
        Timber.d("updateFeeds");
        return Completable.fromAction(() -> {
            boxStore.runInTx(() -> {
                for (Feed feed : feeds) {
                    innerUpdateFeedDomain(feed.id, feed.articles);
                }
            });
        });
    }

    private void innerUpdateFeedDomain(final long feedId, List<Article> articles) {
        FeedModelObjectBox feed = feedBox.get(feedId);

        for (Article article: articles) {
            ArticleModelObjectBox articleDb = converter.domainToDb(article);
            feed.getArticles().add(articleDb);
        }
        feedBox.put(feed);
    }
    //---
    @Override
    public Completable createNewFeed(String feedUrl) {
        return Completable.fromAction(() -> {
            CommonUtils.longOperation();
            FeedModelObjectBox feedModel = new FeedModelObjectBox();
            feedModel.setTitle(feedUrl);
            feedModel.setUrl("url");
            feedBox.put(feedModel);
        });
    }

    @Override
    public Completable deleteFeed(long feedId) {
        return Completable.fromAction(() -> {
            boxStore.runInTx(() -> {
                FeedModelObjectBox feed = feedBox.get(feedId);
                for (ArticleModelObjectBox article : feed.getArticles()) {
                    articleBox.remove(article.getId());
                }
                feedBox.remove(feedId);
            });
        });
    }

    @Override
    public Completable pullArticlesForFeedFromOrigin(Feed feed) {
        return null;
    }

    @Override
    public Single<Boolean> shouldUpdateFeedsInBackground() {
        return null;
    }

    @Override
    public Completable setShouldUpdateFeedsInBackground(boolean shouldUpdate) {
        return null;
    }
}
