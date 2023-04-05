package com.kenji1947.rssreader.domain.interactors.feed;

import com.kenji1947.rssreader.data.connectivity.ConnectivityReceiver;
import com.kenji1947.rssreader.data.database.CommonUtils;
import com.kenji1947.rssreader.domain.entities.Article;
import com.kenji1947.rssreader.domain.entities.Feed;
import com.kenji1947.rssreader.domain.exceptions.NoNetworkException;
import com.kenji1947.rssreader.domain.repository.ArticleRepository;
import com.kenji1947.rssreader.domain.repository.FeedRepository;
import com.kenji1947.rssreader.domain.util.RxSchedulersProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import timber.log.Timber;

/**
 * Created by chamber on 10.12.2017.
 */

public class FeedUpdateInteractor {
    private FeedRepository feedRepository;
    private ConnectivityReceiver connectivityReceiver;
    private ArticleRepository articleRepository;
    private RxSchedulersProvider schedulersProvider;

    @Inject
    public FeedUpdateInteractor(FeedRepository feedRepository,
                                ConnectivityReceiver connectivityReceiver,
                                ArticleRepository articleRepository,
                                RxSchedulersProvider schedulersProvider) {
        this.feedRepository = feedRepository;
        this.connectivityReceiver = connectivityReceiver;
        this.articleRepository = articleRepository;
        this.schedulersProvider = schedulersProvider;
    }


    //TODO Rename to Progress
    public Observable<Integer> updateAllFeedsAndGetNewArticlesCountObservable() {
        Timber.d("updateAllFeedsAndGetNewArticlesCountObservable");
        return connectivityReceiver.isConnected()
                .flatMap(aBoolean -> aBoolean
                        ? feedRepository.getFeeds()
                        : Single.error(new NoNetworkException()))
                .toObservable()
                .flatMap(Observable::fromIterable)
//                .flatMap(feed -> {
//                    TimeUnit.SECONDS.sleep(3);
//                    return Observable.just(feed.articles.size());
//                });
                .flatMap(feed -> fetchOnlyNewArticlesForFeed(feed).toObservable())
                .flatMap(feed -> feedRepository
                        .saveArticlesForFeed(feed.id, feed.articles)
                        .toSingleDefault(feed.articles.size())
                        .toObservable());
    }


    public Single<Integer> updateFeedAndGetNewArticlesCount(long feedId) {
        return connectivityReceiver.isConnected()
                .flatMap(aBoolean -> aBoolean
                        ? feedRepository.getFeed(feedId)
                        : Single.error(new NoNetworkException()))
                .flatMap(feed -> fetchOnlyNewArticlesForFeed(feed))
                .flatMap(feed -> feedRepository.saveArticlesForFeed(feedId, feed.articles)
                        .toSingleDefault(feed.articles.size()));
    }

    Single<Feed> fetchOnlyNewArticlesForFeed(Feed feedFromDb) {
        return articleRepository.updateArticles(feedFromDb.url)
                .map(articlesFromRemote -> {
                    List<Article> articlesNew = new ArrayList<>();
                    for (Article article : articlesFromRemote) {
                        if (!isArticleContains(feedFromDb.articles, article)) {
                            articlesNew.add(article);
                        }
                    }
                    feedFromDb.articles = articlesNew; //TODO !!!
                    Timber.d("fetchOnlyNewArticlesForFeed " + feedFromDb.id + " " + articlesNew.size());
                    return feedFromDb;
                });
    }

    private boolean isArticleContains(List<Article> fromDb, Article articleFromRemote) {
        for (Article article : fromDb) {
            if (article.link.equals(articleFromRemote.link))
                return true;
        }
        return false;
    }


//    public Single<Feed> updateFeed(long id) {
//        return connectivityReceiver.isConnected()
//                .flatMap(aBoolean -> aBoolean
//                        ? feedRepository.getFeed(id)
//                        : Single.error(new NoNetworkException()))
//                .flatMap(feed -> fetchOnlyNewArticlesForFeed(feed))
//                .flatMap(feed -> feedRepository.saveArticlesForFeed(feed.id, feed.articles).toSingleDefault(feed))
//                .flatMap(feed -> feedRepository.getFeed(id));
//    }


    public Single<List<Feed>> updateAllFeeds() {
        return connectivityReceiver.isConnected()
                .flatMap(aBoolean -> aBoolean
                        ? feedRepository.getFeeds()
                        : Single.error(new NoNetworkException()))
                .toObservable()
                .flatMap(Observable::fromIterable)
                .flatMap(feed -> fetchOnlyNewArticlesForFeed(feed).toObservable()) //catch error
                .flatMap(feed -> feedRepository
                        .saveArticlesForFeed(feed.id, feed.articles).toObservable()) //TODO Сохранять сразу весь список в транзакции
                .count()
                .flatMap(count -> feedRepository.getFeeds());
    }

    public Completable updateAllFeeds2() {
        return connectivityReceiver.isConnected()
                .flatMap(aBoolean -> aBoolean
                        ? feedRepository.getFeeds()
                        : Single.error(new NoNetworkException()))
                .toObservable()
                .flatMap(Observable::fromIterable)
                .flatMap(feed -> fetchOnlyNewArticlesForFeed(feed).toObservable()) //catch error
                .toList()
                .flatMapCompletable(feeds -> feedRepository.saveFeed(feeds));
//                .flatMap(feed -> feedRepository
//                        .saveArticlesForFeed(feed.id, feed.articles).toObservable()) //TODO Сохранять сразу весь список в транзакции
//                .count()
//                .flatMap(count -> feedRepository.getFeeds());
    }

    //Used by Update Service
    //Used in presenter
    public Single<Integer> updateAllFeedsAndGetNewArticlesCount() {
        Timber.d("updateAllFeedsAndGetUpdatedCount");
        return connectivityReceiver.isConnected()
                .flatMap(aBoolean -> aBoolean
                        ? feedRepository.getFeeds()
                        : Single.error(new NoNetworkException()))
                .toObservable()
                .flatMap(Observable::fromIterable)
                .flatMap(feed -> fetchOnlyNewArticlesForFeed(feed).toObservable())
                .flatMap(feed -> feedRepository
                        .saveArticlesForFeed(feed.id, feed.articles)
                        .toSingleDefault(feed.articles.size())
                        //.onErrorReturn(throwable -> {return 0;})
                        .toObservable())
                //.reduce((integer, integer2) -> integer + integer2).toSingle(); //TODO Not working
                .toList()
                .map(integers -> {
                    int updatedArticlesSummary = 0;
                    for (int updated : integers) {
                        updatedArticlesSummary += updated;
                    }
                    return updatedArticlesSummary;
                });
    }

    //TODO Zip example
    private Single<Integer> pullNewArticlesForFeed(Feed feed) {
        return Single.zip(
                feedRepository.getFeed(feed.id),
                feedRepository.fetchFeed(feed.url),
                (feedFromDb, feedFromRemote) -> {
                    //return zipUpdateFeed(feedFromRemote, feedFromDb);
                    List<Article> articlesNew = new ArrayList<>();

                    for (Article article : feedFromRemote.articles) {
                        if (!feedFromDb.articles.contains(article)) {
                            articlesNew.add(article);
                        }
                    }
                    feedRepository.saveArticlesForFeed(feed.id, articlesNew);
                    return articlesNew.size();
                }
        );
    }
}
