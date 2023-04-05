package com.kenji1947.rssreader.presentation.article_detail;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.kenji1947.rssreader.domain.interactors.article.ArticlesCrudInteractor;
import com.kenji1947.rssreader.domain.util.RxSchedulersProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

/**
 * Created by chamber on 15.12.2017.
 */

@InjectViewState
public class ArticleDetailPresenter extends MvpPresenter<ArticleDetailView> {
    private long articleId;
    private ArticlesCrudInteractor articlesCrudInteractor;
    private RxSchedulersProvider schedulersProvider;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public ArticleDetailPresenter(long articleId, ArticlesCrudInteractor articlesCrudInteractor, RxSchedulersProvider schedulersProvider) {
        this.articleId = articleId;
        this.articlesCrudInteractor = articlesCrudInteractor;
        this.schedulersProvider = schedulersProvider;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        loadArticle(articleId);
        //start load feed
    }

    private void loadArticle(long articleId) {
        compositeDisposable.add(articlesCrudInteractor.getArticle(articleId)
                .observeOn(schedulersProvider.getMain())
                .subscribe(article -> {
                    getViewState().setArticle(article);
                    Timber.d("loadFeed success " + article.title);
                }, throwable -> {
                    Timber.e("loadFeed error " + throwable);
                })
        );
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();

    }
}
