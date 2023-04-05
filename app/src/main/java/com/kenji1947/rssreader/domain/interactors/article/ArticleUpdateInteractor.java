package com.kenji1947.rssreader.domain.interactors.article;

import com.kenji1947.rssreader.data.connectivity.ConnectivityReceiver;
import com.kenji1947.rssreader.domain.entities.Article;
import com.kenji1947.rssreader.domain.entities.Feed;
import com.kenji1947.rssreader.domain.exceptions.NoNetworkException;
import com.kenji1947.rssreader.domain.interactors.feed.FeedCrudInteractor;
import com.kenji1947.rssreader.domain.repository.ArticleRepository;
import com.kenji1947.rssreader.domain.repository.FeedRepository;
import com.kenji1947.rssreader.domain.util.RxSchedulersProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;
import timber.log.Timber;

/**
 * Created by chamber on 25.03.2018.
 */

public class ArticleUpdateInteractor {
    private ConnectivityReceiver connectivityReceiver;
    private RxSchedulersProvider schedulersProvider;
    private ArticleRepository articleRepository;
    private FeedRepository feedRepository;
    private FeedCrudInteractor feedCrudInteractor;

    @Inject
    public ArticleUpdateInteractor(ConnectivityReceiver connectivityReceiver,
                                   RxSchedulersProvider schedulersProvider,
                                   ArticleRepository articleRepository,
                                   FeedRepository feedRepository,
                                   FeedCrudInteractor feedCrudInteractor) {
        this.connectivityReceiver = connectivityReceiver;
        this.schedulersProvider = schedulersProvider;
        this.articleRepository = articleRepository;
        this.feedRepository = feedRepository;
        this.feedCrudInteractor = feedCrudInteractor;
    }


    public Completable updateArticles(long feedId) {
        return connectivityReceiver.isConnected()
                .flatMap(aBoolean -> aBoolean
                        ? feedRepository.getFeed(feedId)
                        : Single.error(new NoNetworkException()))
                .flatMap(feed -> fetchOnlyNewArticles(feed))
                .flatMapCompletable(feed -> feedRepository.saveArticlesForFeed(feedId, feed.articles));
    }

    Single<Feed> fetchOnlyNewArticles(Feed feedFromDb) {
        return articleRepository.updateArticles(feedFromDb.url)
                .map(articlesFromRemote -> {
                    List<Article> articlesNew = new ArrayList<>();
                    for (Article article : articlesFromRemote) {
                        if (!isArticleContains(feedFromDb.articles, article)) {
                            articlesNew.add(article);
                        }
                    }
                    feedFromDb.articles = articlesNew; //TODO !!!
                    Timber.d("fetchOnlyNewArticles " + feedFromDb.id + " " + articlesNew.size());
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
}
