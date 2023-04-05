package com.kenji1947.rssreader.domain.interactors.article;

import com.kenji1947.rssreader.domain.entities.Article;
import com.kenji1947.rssreader.domain.repository.ArticleRepository;
import com.kenji1947.rssreader.domain.util.RxSchedulersProvider;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * Created by chamber on 08.12.2017.
 */

public class ArticleFavouriteInteractor {
    private ArticleRepository articleRepository;
    private RxSchedulersProvider schedulersProvider;

    @Inject
    public ArticleFavouriteInteractor(ArticleRepository articleRepository, RxSchedulersProvider schedulersProvider) {
        this.articleRepository = articleRepository;
        this.schedulersProvider = schedulersProvider;
    }

    public Single<List<Article>> getFavouriteArticles() {
        return articleRepository.getFavouriteArticles();
    }

    public Completable favouriteArticle(long articleId) {
        return articleRepository.favouriteArticle(articleId);
    }

    public Completable unFavouriteArticle(long articleId) {
        return articleRepository.unFavouriteArticle(articleId);
    }
}
