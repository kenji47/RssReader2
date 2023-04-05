package com.kenji1947.rssreader.data.presenter;

import com.kenji1947.rssreader.data.SchedulersTrampoline;
import com.kenji1947.rssreader.data.TestUtils;
import com.kenji1947.rssreader.data.worker.error_handler.ErrorHandler;
import com.kenji1947.rssreader.data.worker.resource_manager.ResourceManager;
import com.kenji1947.rssreader.domain.exceptions.FeedAlreadySubscribedException;
import com.kenji1947.rssreader.domain.exceptions.NoNetworkException;
import com.kenji1947.rssreader.domain.interactors.feed.SubscribeToFeedInteractor;
import com.kenji1947.rssreader.presentation.Screens;
import com.kenji1947.rssreader.presentation.new_feed.FeedNewPresenter;
import com.kenji1947.rssreader.presentation.new_feed.FeedNewView$$State;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.reactivex.Completable;
import ru.terrakok.cicerone.Router;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by chamber on 19.12.2017.
 */


public class FeedNewPresenterTest {
    private FeedNewPresenter presenter;
    @Mock private ResourceManager resourceManager;
    private ErrorHandler errorHandler;

    @Mock
    Router router;

    @Mock
    SubscribeToFeedInteractor createFeedInteractor;

    @Mock
    FeedNewView$$State viewState;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        errorHandler = TestUtils.initErrorHandler(resourceManager);
        presenter = new FeedNewPresenter(createFeedInteractor, new SchedulersTrampoline(), errorHandler, router);
        presenter.setViewState(viewState);
    }

    @Test
    public void shouldAddNewFeedIfInternetIsAvailable() {
        String url = "url";
        when(createFeedInteractor.createNewFeed(url)).thenReturn(Completable.complete());
        presenter.addNewFeed(url);
        verify(router, times(1))
                .exitWithResult(Screens.RESULT_NEW_FEED_SCREEN_UPDATE, true);

        verifyViewShowProgress();
        Mockito.verifyNoMoreInteractions(viewState);
    }

    @Test
    public void shouldShowNoNetworkIfInternetIsUnavailable() {
        String url = "url";
        when(createFeedInteractor.createNewFeed(url)).thenReturn(Completable.error(new NoNetworkException()));
        presenter.addNewFeed(url);
        verifyViewShowProgress();
        verify(viewState, times(1)).showMessage("No network");
        Mockito.verifyNoMoreInteractions(viewState);
    }

    @Test
    public void shouldShowFeedAlreadyExistIfFeedInDb() {
        String url = "url";
        when(createFeedInteractor.createNewFeed(url)).thenReturn(Completable.error(new FeedAlreadySubscribedException()));
        presenter.addNewFeed(url);
        verifyViewShowProgress();
        verify(viewState, times(1)).showMessage("Feed already subscribed");
        Mockito.verifyNoMoreInteractions(viewState);
    }

    @Test
    public void shouldShowShowErrorIfUnknownErrorHappen() {
        String url = "url";
        when(createFeedInteractor.createNewFeed(url)).thenReturn(Completable.error(new RuntimeException()));
        presenter.addNewFeed(url);
        verifyViewShowProgress();
        verify(viewState, times(1)).showMessage("incorrect feed url");
        Mockito.verifyNoMoreInteractions(viewState);
    }

    private void verifyViewShowProgress() {
        InOrder orderShowProgress = inOrder(viewState);
        orderShowProgress.verify(viewState, times(1)).showProgress(true);
        orderShowProgress.verify(viewState, times(1)).showProgress(false);
    }
}
