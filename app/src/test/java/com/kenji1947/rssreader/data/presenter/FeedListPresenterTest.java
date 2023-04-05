package com.kenji1947.rssreader.data.presenter;

import com.kenji1947.rssreader.data.DataTestData;
import com.kenji1947.rssreader.data.SchedulersTrampoline;
import com.kenji1947.rssreader.data.TestUtils;
import com.kenji1947.rssreader.data.worker.error_handler.ErrorHandler;
import com.kenji1947.rssreader.data.worker.feed_sync_scheduler.job_scheduler.FeedUpdateServiceView;
import com.kenji1947.rssreader.data.worker.resource_manager.ResourceManager;
import com.kenji1947.rssreader.domain.entities.Feed;
import com.kenji1947.rssreader.domain.exceptions.NoNetworkException;
import com.kenji1947.rssreader.domain.interactors.feed.FeedSyncInteractor;
import com.kenji1947.rssreader.domain.interactors.feed.FeedCrudInteractor;
import com.kenji1947.rssreader.domain.interactors.feed.FeedUpdateInteractor;
import com.kenji1947.rssreader.presentation.Screens;
import com.kenji1947.rssreader.presentation.article_list.ArticleListArgumentHolder;
import com.kenji1947.rssreader.presentation.feed_list.FeedListPresenter;
import com.kenji1947.rssreader.presentation.feed_list.FeedListView$$State;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import ru.terrakok.cicerone.Router;
import ru.terrakok.cicerone.result.ResultListener;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by chamber on 19.12.2017.
 */
public class FeedListPresenterTest {

    @Mock private Router router;
    @Mock private FeedCrudInteractor feedCrudInteractor;
    @Mock private FeedUpdateInteractor updateAllFeedsInteractor;
    @Mock private FeedSyncInteractor backgroundUpdateInteractor;
    @Mock private ResourceManager resourceManager;
    private ErrorHandler errorHandler;
    @Mock private FeedListView$$State viewState;

    private FeedListPresenter presenter;

    ArgumentCaptor<ResultListener> captorResultListener = ArgumentCaptor.forClass(ResultListener.class);
    ArgumentCaptor<ArticleListArgumentHolder> captorArticleListArgumentHolder =
            ArgumentCaptor.forClass(ArticleListArgumentHolder.class);


    private List<Feed> feedList;
    private long id;

    @Mock FeedUpdateServiceView feedUpdateServiceView;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        errorHandler = TestUtils.initErrorHandler(resourceManager);

        presenter = new FeedListPresenter(
                feedCrudInteractor,
                updateAllFeedsInteractor,
                backgroundUpdateInteractor,
                new SchedulersTrampoline(),
                errorHandler,
                router
        );
        presenter.setViewState(viewState);

        id = 1;
        feedList = new ArrayList<>();
        when(feedCrudInteractor.getFeeds()).thenReturn(Single.just(feedList)); //default behavior
    }


    @After
    public void tearDown() throws Exception {
    }


    //view.showFeedSubscriptions
    //view.setIsBackgroundFeedUpdateEnabled
    @Test
    public void onFirstViewAttach_ShouldShowFeedUpdateStatusAndShowFeeds() throws Exception {
        when(backgroundUpdateInteractor.shouldUpdateFeedsInBackground()).thenReturn(Single.just(true));
        when(updateAllFeedsInteractor.updateAllFeeds()).thenReturn(Single.just(feedList));

        presenter.onFirstViewAttach();

        verify(viewState, times(1)).setIsBackgroundFeedUpdateEnabled(true);
        verify(viewState, times(1)).showFeedSubscriptions(feedList);
        verify(updateAllFeedsInteractor, times(1)).updateAllFeeds();
    }

    //view.showFeedSubscriptions
    @Test
    public void updateAllFeeds_Success() throws Exception {
        when(updateAllFeedsInteractor.updateAllFeeds()).thenReturn(Single.just(feedList));

        presenter.updateAllFeeds();

        verifyViewShowProgress();
        //verifyViewShowProgress();
        verify(viewState, times(1)).showFeedSubscriptions(feedList);
        verifyNoMoreInteractions(viewState);
    }


    //view.showFeedSubscriptions
    //view.showErrorMessage
    @Test
    public void updateAllFeeds_Error_NoNetwork() throws Exception {
        when(updateAllFeedsInteractor.updateAllFeeds()).thenReturn(Single.error(new NoNetworkException()));

        presenter.updateAllFeeds();

        //TODO Проблема с прогресс
        //verifyViewShowProgress();

        InOrder orderShowProgress = inOrder(viewState);
        orderShowProgress.verify(viewState, times(1)).showProgress(true);
        orderShowProgress.verify(viewState, times(1)).showProgress(false);
        orderShowProgress.verify(viewState, times(1)).showProgress(true);
        orderShowProgress.verify(viewState, times(1)).showProgress(false);

        verify(viewState, times(1)).showErrorMessage(TestUtils.MSG_NO_NETWORK);
        verify(viewState, times(1)).showFeedSubscriptions(feedList);
        //verifyNoMoreInteractions(viewState);
    }

    //view.showFeedSubscriptions
    //view.showErrorMessage
    @Test
    public void updateAllFeeds_Error_Other() throws Exception {
        when(updateAllFeedsInteractor.updateAllFeeds()).thenReturn(Single.error(new RuntimeException()));

        presenter.updateAllFeeds();

        //verifyViewShowProgress(); //TODO Ошибка в индикаторе уже здесь!!!
        verify(viewState, times(1)).showErrorMessage(TestUtils.MSG_UNKNOWN_ERROR);
        verify(viewState, times(1)).showFeedSubscriptions(feedList);
        //verifyNoMoreInteractions(viewState);
    }

    @Test
    public void getAllFeeds_Success() throws Exception {
        presenter.getAllFeeds();
        verify(viewState, times(1)).showFeedSubscriptions(feedList);
    }

    @Test
    public void getAllFeeds_Error_Other() throws Exception {
        when(feedCrudInteractor.getFeeds()).thenReturn(Single.error(new RuntimeException()));
        presenter.getAllFeeds();
        verify(viewState, times(1)).showErrorMessage(TestUtils.MSG_UNKNOWN_ERROR);
    }

    //TODO Проблема с каптором
    @Test
    public void showAddNewFeed() throws Exception {
        presenter.showAddNewFeed();

        verify(router, times(1)).navigateTo(Screens.NEW_FEED_SCREEN);

        verify(router).setResultListener(
                eq(Screens.RESULT_NEW_FEED_SCREEN_UPDATE),
                captorResultListener.capture()
        );
//        captorResultListener.getValue().onResult(true);
//        verify(router, times(1)).removeResultListener(Screens.RESULT_NEW_FEED_SCREEN_UPDATE);
//        verify(viewState, times(1)).showFeedSubscriptions(feedList);
    }

    @Test
    public void unSubscribeFromFeed_Success() throws Exception {
        when(feedCrudInteractor.deleteFeed(id)).thenReturn(Completable.complete());

        presenter.unSubscribeFromFeed(id);

        verify(feedCrudInteractor, times(1)).deleteFeed(id);
        verify(viewState, times(1)).showFeedSubscriptions(feedList);
    }

    @Test
    public void unSubscribeFromFeed_Error() throws Exception {
        when(feedCrudInteractor.deleteFeed(id)).thenReturn(Completable.error(new RuntimeException()));

        presenter.unSubscribeFromFeed(id);

        verify(feedCrudInteractor, times(1)).deleteFeed(id);
        verify(viewState, never()).showFeedSubscriptions(feedList);
    }

    @Test
    public void showArticles() throws Exception {
        Feed feed = new Feed(
                DataTestData.TEST_LONG_ID_1,
                DataTestData.TEST_STRING_TITLE_1,
                DataTestData.TEST_IMAGE_URL,
                DataTestData.TEST_COMPLEX_URL_STRING_1,
                DataTestData.TEST_DESCRIPTION_STRING,
                DataTestData.TEST_BASIC_URL_STRING
                );
        presenter.showArticles(feed);
        //TODO Каптор не работает
//        verify(router, times(1)).navigateTo(
//                Screens.ARTICLE_LIST_SCREEN,
//                captorArticleListArgumentHolder.capture()
//                );
    }

    @Test
    public void showFavouriteArticles() throws Exception {
        presenter.showFavouriteArticles();
        verify(router, times(1)).navigateTo(Screens.ARTICLE_FAV_LIST_SCREEN);
    }

    @Test
    public void enableBackgroundFeedUpdates() throws Exception {
        when(backgroundUpdateInteractor.enableBackgroundFeedUpdate()).thenReturn(Completable.complete());
        presenter.enableBackgroundFeedUpdates();
        verify(viewState, times(1)).setIsBackgroundFeedUpdateEnabled(true);
    }

    @Test
    public void disableBackgroundFeedUpdates() throws Exception {
        when(backgroundUpdateInteractor.disableBackgroundFeedUpdate()).thenReturn(Completable.complete());
        presenter.disableBackgroundFeedUpdates();
        verify(viewState, times(1)).setIsBackgroundFeedUpdateEnabled(false);
    }
    private void verifyViewShowProgress() {
        InOrder orderShowProgress = inOrder(viewState);
        orderShowProgress.verify(viewState, times(1)).showProgress(true);
        orderShowProgress.verify(viewState, times(1)).showProgress(false);
    }
}