package com.kenji1947.rssreader.domain.interactors.article;

import com.kenji1947.rssreader.domain.entities.Article;
import com.kenji1947.rssreader.domain.repository.ArticleRepository;
import com.kenji1947.rssreader.domain.util.RxSchedulersProvider;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by chamber on 08.12.2017.
 */

public class ArticlesCrudInteractor {
    private ArticleRepository articleRepository;
    private RxSchedulersProvider schedulersProvider;

    @Inject
    public ArticlesCrudInteractor(ArticleRepository articleRepository, RxSchedulersProvider schedulersProvider) {
        this.articleRepository = articleRepository;
        this.schedulersProvider = schedulersProvider;
    }

    public Single<List<Article>> getArticles(long feedId) {
        return articleRepository.getArticles(feedId);
    }

    public Single<Article> getArticle(long articleId) {
        return articleRepository.getArticle(articleId);
    }

    public Observable<List<Article>> getArticlesAndObserve(long feedId) {
        return articleRepository.getArticlesAndObserve(feedId);
    }

}
