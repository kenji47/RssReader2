package com.kenji1947.rssreader.domain.interactors.feed;

import com.kenji1947.rssreader.data.SchedulersTrampoline;
import com.kenji1947.rssreader.data.connectivity.ConnectivityReceiver;
import com.kenji1947.rssreader.domain.entities.Article;
import com.kenji1947.rssreader.domain.entities.Feed;
import com.kenji1947.rssreader.domain.repository.FeedRepository;
import com.kenji1947.rssreader.domain.util.RxSchedulersProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by chamber on 25.12.2017.
 */
public class UpdateAllFeedsInteractorTest {
    @Mock
    private FeedRepository feedRepository;
    @Mock private ConnectivityReceiver connectivityReceiver;
    private RxSchedulersProvider schedulersProvider = new SchedulersTrampoline();

    int feedListSize = 5;
    List<Feed> feedList;

    private TestObserver<List<Feed>> testObserverListFeed;
    private TestObserver<Feed> testObserverFeed;
    private TestObserver<Integer> testObserverInteger;
    private FeedUpdateInteractor interactor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        feedList = new ArrayList<>();
        for (int i = 0; i < feedListSize; i++) {
            feedList.add(new Feed());
        }

        testObserverListFeed = new TestObserver<>();
        testObserverFeed = new TestObserver<>();
        testObserverInteger = new TestObserver<>();
        interactor = new FeedUpdateInteractor(feedRepository, connectivityReceiver, schedulersProvider);
    }

    //TODO Определить  equals




    @Test
    public void fetchOnlyNewArticlesForFeed_ShouldReturnEmpty_WhenNoNewArticles() throws Exception {
        Feed feedFromDb = UiTestData.generateFeeds(1, 5).get(0);
        Feed feedFromRemote = UiTestData.generateFeeds(1, 5).get(0);

        when(feedRepository.fetchFeed(feedFromDb.url)).thenReturn(Single.just(feedFromRemote));

        interactor.fetchOnlyNewArticlesForFeed(feedFromDb).subscribe(testObserverFeed);

        testObserverFeed.assertComplete();
        testObserverFeed.assertNoErrors();
        Feed feedOutput = testObserverFeed.values().get(0);
        assertThat(feedOutput.articles.size(), is(0));
    }

    @Test
    public void fetchOnlyNewArticlesForFeed_ShouldReturnEmpty_WhenArticlesEmpty() throws Exception {
        Feed feedFromDb = UiTestData.generateFeeds(1, 5).get(0);
        Feed feedFromRemote = UiTestData.generateFeeds(1, 0).get(0);

        when(feedRepository.fetchFeed(feedFromDb.url)).thenReturn(Single.just(feedFromRemote));

        interactor.fetchOnlyNewArticlesForFeed(feedFromDb).subscribe(testObserverFeed);

        testObserverFeed.assertComplete();
        testObserverFeed.assertNoErrors();
        Feed feedOutput = testObserverFeed.values().get(0);
        assertThat(feedOutput.articles.size(), is(0));
    }

    @Test
    public void fetchOnlyNewArticlesForFeed_ShouldReturnNewArticles_WhenHaveNewArticles() throws Exception {
        String newArticleLink1 = "new1";
        String newArticleLink2 = "new2";
        Feed feedFromRemote = UiTestData.generateFeeds(1, 5).get(0);
        feedFromRemote.articles.get(0).link = newArticleLink1;
        feedFromRemote.articles.get(1).link = newArticleLink2;

        Feed feedFromDb = UiTestData.generateFeeds(1, 5).get(0);

        when(feedRepository.fetchFeed(feedFromDb.url)).thenReturn(Single.just(feedFromRemote));

        interactor.fetchOnlyNewArticlesForFeed(feedFromDb).subscribe(testObserverFeed);

        testObserverFeed.assertComplete();
        testObserverFeed.assertNoErrors();
        Feed feedOutput = testObserverFeed.values().get(0);
        assertThat(feedOutput.articles.size(), is(2));
        assertThat(feedOutput.articles.get(0).link, is(newArticleLink1));
        assertThat(feedOutput.articles.get(1).link, is(newArticleLink2));
    }

    @Test
    public void fetchOnlyNewArticlesForFeed_ShouldReturnError_WhenError() throws Exception {
        String newArticleLink1 = "new1";
        String newArticleLink2 = "new2";
        Feed feedFromRemote = UiTestData.generateFeeds(1, 5).get(0);
        feedFromRemote.articles.get(0).link = newArticleLink1;
        feedFromRemote.articles.get(1).link = newArticleLink2;

        Feed feedFromDb = UiTestData.generateFeeds(1, 5).get(0);

        when(feedRepository.fetchFeed(feedFromDb.url)).thenReturn(Single.error(new RuntimeException()));

        interactor.fetchOnlyNewArticlesForFeed(feedFromDb).subscribe(testObserverFeed);

        testObserverFeed.assertError(RuntimeException.class);
        testObserverFeed.assertNotComplete();
    }

    //---

    @Test
    public void updateAllFeedsAndGetNewCount_ShouldReturn0_WhenNoNewArticles() throws Exception {
        List<Feed> feedsFromDb = UiTestData.generateFeeds(2, 20);

        when(connectivityReceiver.isConnected()).thenReturn(Single.just(true));

        when(feedRepository.getFeeds()).thenReturn(Single.just(feedsFromDb));
        when(feedRepository.saveArticlesForFeed(anyLong(), anyList())).thenReturn(Completable.complete());

        //Return Feeds with no Articles from Api
        when(feedRepository.fetchFeed(feedsFromDb.get(0).url)).thenReturn(Single.just(feedsFromDb.get(0)));
        when(feedRepository.fetchFeed(feedsFromDb.get(1).url)).thenReturn(Single.just(feedsFromDb.get(1)));

        interactor.updateAllFeedsAndGetNewArticlesCount().subscribe(testObserverInteger);

        verify(feedRepository).saveArticlesForFeed(feedsFromDb.get(0).id, new ArrayList<>());
        verify(feedRepository).saveArticlesForFeed(feedsFromDb.get(1).id, new ArrayList<>());

        testObserverInteger.assertNoErrors();
        testObserverInteger.assertComplete();

        int articlesExpected = 0;
        int articlesActual = testObserverInteger.values().get(0);
        assertThat(articlesExpected, is(articlesActual));
    }

    @Test
    public void updateAllFeedsAndGetNewCount_ShouldReturnNewArticles() throws Exception {
        List<Feed> feedsFromDb = UiTestData.generateFeeds(2, 20);

        Feed feedFromRemote = UiTestData.generateFeeds(1, 0).get(0);
        List<Article> articles = new ArrayList<>();
        articles.add(new Article("e", "new", 324324, true, false));
        articles.add(new Article("e", "new1", 324324, true, false));
        feedFromRemote.articles = articles;

        when(connectivityReceiver.isConnected()).thenReturn(Single.just(true));

        when(feedRepository.getFeeds()).thenReturn(Single.just(feedsFromDb));
        when(feedRepository.saveArticlesForFeed(anyLong(), anyList())).thenReturn(Completable.complete());

        when(feedRepository.fetchFeed(feedsFromDb.get(0).url)).thenReturn(Single.just(feedFromRemote));
        //when(feedRepository.fetchFeed(feedsFromDb.get(0).url)).thenReturn(Single.error(new RuntimeException()));
        when(feedRepository.fetchFeed(feedsFromDb.get(1).url)).thenReturn(Single.just(feedsFromDb.get(1)));

        interactor.updateAllFeedsAndGetNewArticlesCount().subscribe(testObserverInteger);

        verify(feedRepository).saveArticlesForFeed(feedsFromDb.get(0).id, feedFromRemote.articles);
        verify(feedRepository).saveArticlesForFeed(feedsFromDb.get(1).id, new ArrayList<>());

        testObserverInteger.assertNoErrors();
        testObserverInteger.assertComplete();

        int articlesExpected = 2;
        int articlesActual = testObserverInteger.values().get(0);
        assertThat(articlesExpected, is(articlesActual));
    }

    @Test
    public void updateAllFeedsAndGetNewCount_Error() throws Exception {
        List<Feed> feedsFromDb = UiTestData.generateFeeds(2, 20);

        Feed feedFromRemote = UiTestData.generateFeeds(1, 5).get(0);
        List<Article> articles = new ArrayList<>();
        articles.add(new Article("e", "new", 324324, true, false));
        articles.add(new Article("e", "new1", 324324, true, false));
        feedFromRemote.articles = articles;

        when(connectivityReceiver.isConnected()).thenReturn(Single.just(true));

        when(feedRepository.getFeeds()).thenReturn(Single.just(feedsFromDb));
        when(feedRepository.saveArticlesForFeed(anyLong(), anyList())).thenReturn(Completable.complete());

        when(feedRepository.fetchFeed(feedsFromDb.get(0).url)).thenReturn(Single.just(feedsFromDb.get(0)));
        when(feedRepository.fetchFeed(feedsFromDb.get(1).url)).thenReturn(Single.just(feedsFromDb.get(1)));

        interactor.updateAllFeedsAndGetNewArticlesCount().subscribe(testObserverInteger);

        verify(feedRepository).saveArticlesForFeed(feedsFromDb.get(0).id, feedFromRemote.articles);
        verify(feedRepository).saveArticlesForFeed(feedsFromDb.get(1).id, new ArrayList<>());

        testObserverInteger.assertNoErrors();
        testObserverInteger.assertComplete();

        int articlesExpected = 2;
        int articlesActual = testObserverInteger.values().get(0);
        assertThat(articlesExpected, is(articlesActual));
    }
}