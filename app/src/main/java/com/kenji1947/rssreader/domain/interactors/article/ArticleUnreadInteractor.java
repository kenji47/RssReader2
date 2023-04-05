package com.kenji1947.rssreader.domain.interactors.article;

import com.kenji1947.rssreader.domain.repository.ArticleRepository;
import com.kenji1947.rssreader.domain.util.RxSchedulersProvider;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by chamber on 08.12.2017.
 */

public class ArticleUnreadInteractor {

    private ArticleRepository articleRepository;
    private RxSchedulersProvider schedulersProvider;


    @Inject
    public ArticleUnreadInteractor(ArticleRepository articleRepository, RxSchedulersProvider schedulersProvider) {
        this.articleRepository = articleRepository;
        this.schedulersProvider = schedulersProvider;
    }

    public Completable markArticleAsRead(long articleId) {
        return articleRepository.markArticleAsRead(articleId);
    }

    public Single<Long> getUnreadArticlesCount() {
        return articleRepository.getUnreadArticlesCount();
    }
}
