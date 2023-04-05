package com.kenji1947.rssreader.ui;

import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.kenji1947.rssreader.App;
import com.kenji1947.rssreader.R;
import com.kenji1947.rssreader.di.presenter.FeedListPresenterComponent;
import com.kenji1947.rssreader.domain.repository.FeedRepository;
import com.kenji1947.rssreader.presentation.MainActivity;
import com.kenji1947.rssreader.presentation.feed_list.FeedListPresenter;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.objectbox.BoxStore;
import timber.log.Timber;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Created by chamber on 17.12.2017.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class FeedListScreenLFTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<MainActivity>(MainActivity.class);
    private FeedRepository feedRepository;

    @BeforeClass
    public static void beforeClass() {
        Timber.d("beforeClass");
        BoxStore boxStore = App.INSTANCE.getAppComponent().provideBoxStore();
        FeedRepository feedRepository = App.INSTANCE.getAppComponent().provideFeedRepository();
        boxStore.diagnose();
    }
    @AfterClass
    public static void afterClass() {
        Timber.d("afterClass");
    }
    @Before
    public void before() {
        Timber.d("before");
        mainActivityActivityTestRule.getActivity();
        BoxStore boxStore = App.INSTANCE.getAppComponent().provideBoxStore();
        feedRepository = App.INSTANCE.getAppComponent().provideFeedRepository();
        boxStore.diagnose();
    }

    @After
    public void after() {
        Timber.d("after");
    }

    @Test
    public void addNewNote_ShouldAddNoteToList() throws Exception{
        Timber.d("Test 1 begininng");
        //onView(ViewMatchers.withId(R.id.fab)).check(matches(isDisplayed()));
        Timber.d("Test 1 end");
        FeedListPresenter presenter = FeedListPresenterComponent.Initializer
                .init(App.INSTANCE.getAppComponent(), 1).providePresenter();
        presenter.getAllFeeds();
//        feedRepository.getFeeds()
//                .subscribe(feeds -> {
//                    Timber.d("onComplete " + feeds.size());
//                });
    }

    @Test
    public void addNewNote_ShouldAddNoteToList2() throws Exception{
        Timber.d("Test 2 begininng");
        onView(ViewMatchers.withId(R.id.fab)).check(matches(isDisplayed()));
        Timber.d("Test 2 end");
    }
}
