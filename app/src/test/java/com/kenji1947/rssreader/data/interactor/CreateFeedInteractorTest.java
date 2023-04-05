package com.kenji1947.rssreader.data.interactor;

import com.kenji1947.rssreader.data.SchedulersTrampoline;
import com.kenji1947.rssreader.data.connectivity.ConnectivityReceiver;
import com.kenji1947.rssreader.domain.exceptions.FeedAlreadySubscribedException;
import com.kenji1947.rssreader.domain.exceptions.NoNetworkException;
import com.kenji1947.rssreader.domain.interactors.feed.SubscribeToFeedInteractor;
import com.kenji1947.rssreader.domain.repository.FeedRepository;
import com.kenji1947.rssreader.domain.util.RxSchedulersProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.when;

/**
 * Created by chamber on 19.12.2017.
 */

public class CreateFeedInteractorTest {
    @Mock
    private FeedRepository feedRepository;
    @Mock
    private ConnectivityReceiver connectivityReceiver;

    private RxSchedulersProvider schedulersProvider = new SchedulersTrampoline();

    private SubscribeToFeedInteractor interactor;

    private TestObserver testObserver;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        interactor = new SubscribeToFeedInteractor(feedRepository, connectivityReceiver, schedulersProvider);
        testObserver = new TestObserver();
    }

    @Test
    public void shouldAddNewFeedWhenInternetIsAvailableAndFeedNotExistInDb() throws Exception {
        String url = "url";

        when(feedRepository.createNewFeed(Mockito.any())).thenReturn(Completable.complete());
        when(feedRepository.feedExists(url)).thenReturn(Single.just(false));
        when(connectivityReceiver.isConnected()).thenReturn(Single.just(true));

        interactor.createNewFeed(url).subscribe(testObserver);

        //TODO Тавтологические тесты?
        //Тесты должны проверять, что метод СДЕЛАЛ, а не КАК он это сделал.
        Mockito.verify(feedRepository, Mockito.times(1)).createNewFeed(url);
        Mockito.verify(feedRepository, Mockito.times(1)).feedExists(url);
        Mockito.verify(connectivityReceiver, Mockito.times(1)).isConnected();

        Mockito.verifyNoMoreInteractions(feedRepository);

        testObserver.assertComplete();
    }

    @Test
    public void shouldReturnNoNetworkExceptionWhenInternetIsUnavailable() throws Exception {
        String url = "url";

        when(feedRepository.createNewFeed(Mockito.any())).thenReturn(Completable.complete());
        when(feedRepository.feedExists(url)).thenReturn(Single.just(false));
        when(connectivityReceiver.isConnected()).thenReturn(Single.just(false));

        interactor.createNewFeed(url).subscribe(testObserver);

        Mockito.verify(connectivityReceiver, Mockito.times(1)).isConnected();
        Mockito.verifyNoMoreInteractions(feedRepository);

        testObserver.assertError(NoNetworkException.class);
    }

    @Test
    public void shouldReturnFeedAlreadySubscribedExceptionWhenFeedExistInDb() throws Exception {
        String url = "url";

        when(feedRepository.createNewFeed(Mockito.any())).thenReturn(Completable.complete());
        when(feedRepository.feedExists(url)).thenReturn(Single.just(true));
        when(connectivityReceiver.isConnected()).thenReturn(Single.just(true));

        interactor.createNewFeed(url).subscribe(testObserver);

        Mockito.verify(connectivityReceiver, Mockito.times(1)).isConnected();
        Mockito.verify(feedRepository, Mockito.times(1)).feedExists(url);
        Mockito.verifyNoMoreInteractions(feedRepository);

        testObserver.assertError(FeedAlreadySubscribedException.class);
    }
}
