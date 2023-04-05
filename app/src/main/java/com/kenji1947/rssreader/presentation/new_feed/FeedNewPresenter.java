package com.kenji1947.rssreader.presentation.new_feed;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.kenji1947.rssreader.data.worker.error_handler.ErrorHandler;
import com.kenji1947.rssreader.domain.entities.Feed;
import com.kenji1947.rssreader.domain.entities.SearchedFeed;
import com.kenji1947.rssreader.domain.interactors.feed.SubscribeToFeedInteractor;
import com.kenji1947.rssreader.domain.interactors.feed.FeedCrudInteractor;
import com.kenji1947.rssreader.domain.interactors.feed.SearchFeedsInteractor;
import com.kenji1947.rssreader.domain.util.RxSchedulersProvider;
import com.kenji1947.rssreader.presentation.Screens;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import ru.terrakok.cicerone.Router;
import timber.log.Timber;

/**
 * Created by chamber on 13.12.2017.
 */

@InjectViewState
public class FeedNewPresenter extends MvpPresenter<FeedNewView> {

    private boolean isLoading;
    private SearchFeedsInteractor searchFeedsInteractor;
    private FeedCrudInteractor feedCrudInteractor;
    private RxSchedulersProvider schedulersProvider;
    private SubscribeToFeedInteractor createFeedInteractor;
    private ErrorHandler errorHandler;
    private Router router;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private List<SearchedFeed> searchedFeeds;
    private List<Feed> subscribedFeeds;

    @Inject
    public FeedNewPresenter(SubscribeToFeedInteractor createFeedInteractor,
                            SearchFeedsInteractor searchFeedsInteractor,
                            FeedCrudInteractor feedCrudInteractor,
                            RxSchedulersProvider schedulersProvider,
                            ErrorHandler errorHandler,
                            Router router) {
        this.searchFeedsInteractor = searchFeedsInteractor;
        this.feedCrudInteractor = feedCrudInteractor;
        this.schedulersProvider = schedulersProvider;
        this.createFeedInteractor = createFeedInteractor;
        this.errorHandler = errorHandler;
        this.router = router;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        //show keyboard on start up
        getViewState().showKeyboard(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    //TODO Ошибки:
    // урл не валиден
    // нет интернета
    // фид уже есть в бд
    // нет интернета во время запроса
    // ресурса с данным урл не существует


    //---
    public void searchFeeds(String feedName) {
        getViewState().showKeyboard(false);
        compositeDisposable.add(
                searchFeedsInteractor.searchFeed(feedName)
                        .observeOn(schedulersProvider.getMain())
                        .doOnSubscribe(disposable -> {
                            getViewState().showSearchFeedLoadingDialog();
                            isLoading = true;
                        })
                        .doAfterTerminate(() -> {
                            getViewState().hideLoadingDialog();
                            isLoading = false;
                        })
                        .subscribe(this::onSearchFeedsSuccess, this::onSearchFeedsError)
        );
    }

    private void onSearchFeedsSuccess(List<SearchedFeed> searchedFeeds) {
        Timber.e("onSearchFeedsSuccess " + searchedFeeds.size());
        this.searchedFeeds = searchedFeeds;
        //searchedFeeds.get(1).isSubscribed = true;
        getViewState().setSearchedFeeds(searchedFeeds);
    }

    private void onSearchFeedsError(Throwable throwable) {
        Timber.e("onSearchFeedsError " + throwable);
        errorHandler.handleErrorScreenNewFeed(throwable, s -> getViewState().showErrorMessage(s));
    }

    public void subscribeToFeed(int pos) {
        Timber.d("subscribeToFeed pos: " + pos + " title: " + searchedFeeds.get(pos).title);

        if (searchedFeeds.get(pos).isSubscribed)
            getViewState().showMessage("You already subscribed to this feed");
        else {
            //compositeDisposable.add(createFeedInteractor.createNewFeed2(searchedFeeds.get(pos))
            compositeDisposable.add(createFeedInteractor.subscribeToFeed(searchedFeeds.get(pos).url)
                    .observeOn(schedulersProvider.getMain())
                    .doOnSubscribe(disposable -> getViewState()
                            .showSubscribeFeedLoadingDialog(searchedFeeds.get(pos).title))
                    .doAfterTerminate(() -> getViewState().hideLoadingDialog())
                    .subscribe(() -> {
                                Timber.d("subscribeToFeed complete");
                                router.exitWithResult(Screens.RESULT_NEW_FEED_SCREEN_UPDATE,
                                        searchedFeeds.get(pos).title);
                                //return to feed list
                                //show added toast
                            },
                            throwable -> {
                                Timber.e("subscribeToFeed error " + throwable);
                                throwable.printStackTrace();
                                errorHandler.handleErrorScreenNewFeed(throwable,
                                        s -> getViewState().showErrorMessage(s));
                            }));
        }
    }

    public void back() {
        if (isLoading) {
            compositeDisposable.clear();
            getViewState().hideLoadingDialog();
            isLoading = false;
        } else {
            router.backTo(Screens.FEED_LIST_SCREEN);
        }

    }
}
