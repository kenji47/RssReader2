package com.kenji1947.rssreader.presentation.article_list;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.kenji1947.rssreader.domain.entities.Article;
import com.kenji1947.rssreader.domain.interactors.article.ArticleFavouriteInteractor;
import com.kenji1947.rssreader.domain.interactors.article.ArticleUnreadInteractor;
import com.kenji1947.rssreader.domain.interactors.article.ArticleUpdateInteractor;
import com.kenji1947.rssreader.domain.interactors.article.ArticlesCrudInteractor;
import com.kenji1947.rssreader.domain.interactors.article.ObserveArticlesModificationInteractor;
import com.kenji1947.rssreader.domain.interactors.feed.FeedUpdateInteractor;
import com.kenji1947.rssreader.domain.util.RxSchedulersProvider;
import com.kenji1947.rssreader.presentation.Screens;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import ru.terrakok.cicerone.Router;
import timber.log.Timber;

/**
 * Created by chamber on 14.12.2017.
 */

@InjectViewState
public class ArticleListPresenter extends MvpPresenter<ArticleListView> {

    private long feedId;
    private boolean isFavModeOn;
    private List<Article> articleList;

    private FeedUpdateInteractor feedUpdateInteractor;
    private ArticlesCrudInteractor articlesCrudInteractor;
    private ArticleFavouriteInteractor articleFavouriteInteractor;
    private ArticleUnreadInteractor articleUnreadInteractor;
    private ArticleUpdateInteractor articleUpdateInteractor;
    private ObserveArticlesModificationInteractor observeArticleUpdatesInteractor;
    private RxSchedulersProvider schedulersProvider;
    private Router router;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Inject
    public ArticleListPresenter(long feedId, boolean isFavModeOn,
                                FeedUpdateInteractor feedUpdateInteractor,
                                ArticlesCrudInteractor articlesCrudInteractor,
                                ArticleFavouriteInteractor articleFavouriteInteractor,
                                ArticleUnreadInteractor articleUnreadInteractor,
                                ArticleUpdateInteractor articleUpdateInteractor,
                                ObserveArticlesModificationInteractor observeArticleUpdatesInteractor,
                                RxSchedulersProvider schedulersProvider,
                                Router router) {
        this.feedId = feedId;
        this.isFavModeOn = isFavModeOn;
        this.feedUpdateInteractor = feedUpdateInteractor;
        this.articlesCrudInteractor = articlesCrudInteractor;
        this.articleFavouriteInteractor = articleFavouriteInteractor;
        this.articleUnreadInteractor = articleUnreadInteractor;
        this.articleUpdateInteractor = articleUpdateInteractor;
        this.observeArticleUpdatesInteractor = observeArticleUpdatesInteractor;
        this.schedulersProvider = schedulersProvider;
        this.router = router;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        //observeArticleUpdates();

        if (isFavModeOn) {
            getFavArticles();
        } else {
            getArticlesForFeedObserve(feedId);
            //getArticlesForFeed(feedId);
        }
    }

    private void observeArticleUpdates() {
        Timber.d("observeArticlesModification");
        compositeDisposable.add(observeArticleUpdatesInteractor.observeArticlesModification()
                .observeOn(schedulersProvider.getMain())
                .subscribe(this::onObserveArticleUpdatesSuccess)
        );
    }

    private void onObserveArticleUpdatesSuccess(boolean status) {
        Timber.d("onObserveArticleUpdatesSuccess");
        if (!isFavModeOn) {
            getArticlesForFeed(feedId);
        }
    }

    void getFavArticles() {
        articleFavouriteInteractor.getFavouriteArticles()
//                .doOnSubscribe(disposable -> {getViewState().showProgress(true);})
//                .doAfterTerminate(() -> {getViewState().showProgress(false);})
                .observeOn(schedulersProvider.getMain())
                .subscribe(articles -> {
                    articleList = articles;
                    getViewState().showArticles(articles);
                });
    }

    void getArticlesForFeed(long feedId) {
        this.feedId = feedId;
        articlesCrudInteractor.getArticles(feedId)
//                .doOnSubscribe(disposable -> {getViewState().showProgress(true);})
//                .doAfterTerminate(() -> {getViewState().showProgress(false);})
                .observeOn(schedulersProvider.getMain())
                .subscribe(articles -> {
                    articleList = articles;
                    getViewState().showArticles(articles);
                });
    }

    void updateArticles() {
        //TODO check if
        compositeDisposable.add(feedUpdateInteractor.updateFeedAndGetNewArticlesCount(feedId)
                .doOnSubscribe(disposable -> {getViewState().showProgress(true);})
                .doAfterTerminate(() -> {getViewState().showProgress(false);})
                .observeOn(schedulersProvider.getMain())
               .subscribe(
                       newArticlesCount -> {Timber.d("updateArticles Success " + newArticlesCount);
                       getViewState().showNewArticlesCountMessage(newArticlesCount);},
                       throwable -> {}));
    }

    void getArticlesForFeedObserve(long feedId) {
        this.feedId = feedId;
        articlesCrudInteractor.getArticlesAndObserve(feedId)
//                .doOnSubscribe(disposable -> {getViewState().showProgress(true);})
//                .doAfterTerminate(() -> {getViewState().showProgress(false);})
                .observeOn(schedulersProvider.getMain())
                .subscribe(articles -> {
                    Timber.d("getArticlesForFeedObserve " + articles.size());
                    articleList = articles;
                    getViewState().showArticles(articles);
                });
    }

    //TODO Optimistic call
    void markArticleAsRead(long articleId) {
        articleUnreadInteractor.markArticleAsRead(articleId)
                .doOnComplete(() -> observeArticleUpdatesInteractor.notifyArticleModified())
                .observeOn(schedulersProvider.getMain())
                .subscribe(() -> {
                    Timber.d("onComplete markArticleAsRead");
                });
    }

    //TODO Optimistic call
    void toggleArticleFavourite (int pos) {
        (articleList.get(pos).isFavourite ?
        articleFavouriteInteractor.favouriteArticle(articleList.get(pos).id)
        : articleFavouriteInteractor.unFavouriteArticle(articleList.get(pos).id))
                .subscribe(() -> {Timber.d("onComplete toggleArticleFavourite");});
    }

    void showArticle(int pos) {
        //router.navigateTo(Screens.ARTICLE_DETAIL_SCREEN, articleList.get(pos).link);
        router.navigateTo(Screens.ARTICLE_DETAIL_SCREEN, articleList.get(pos).id);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Timber.d("onDestroy");
        compositeDisposable.clear();
    }
}
