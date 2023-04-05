package com.kenji1947.rssreader.ui;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.kenji1947.rssreader.App;
import com.kenji1947.rssreader.R;
import com.kenji1947.rssreader.domain.entities.Feed;
import com.kenji1947.rssreader.fakes.AppSetup;
import com.kenji1947.rssreader.fakes.DataLab;
import com.kenji1947.rssreader.presentation.MainActivity;
import com.kenji1947.rssreader.util.DatabaseOperationsImpl;
import com.kenji1947.rssreader.util.MyAssertions;
import com.kenji1947.rssreader.util.MyMatcher;
import com.kenji1947.rssreader.util.EspressoOperations;


import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.assertThat;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;

/**
 * Created by chamber on 21.12.2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ArticleListScreen {
    private static DatabaseOperationsImpl databaseOperations;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<MainActivity>(MainActivity.class, false, false);

    @BeforeClass
    public static void beforeClass() {
        AppSetup.networkState = AppSetup.NETWORK_STATE.ONLINE;
        AppSetup.feedApiServiceState = AppSetup.FEED_SERVICE_STATE.RETURN_SAME_FEED;

        databaseOperations = new DatabaseOperationsImpl(App.INSTANCE);
        databaseOperations.clearAllDb();
    }

    @Before
    public void setUp() throws Exception {
        EspressoOperations.activityTestRule = mainActivityActivityTestRule;
    }

    @After
    public void after() {
        DataLab.clearAllData();
        databaseOperations.clearAllDb();
    }

    //--
    @Test
    public void favouriteArticle() throws Exception{
        List<Feed> feedList = DataLab.generateFeeds(2, 2);
        databaseOperations.addFeeds(feedList);
        EspressoOperations.startActivityDefaultIntent(mainActivityActivityTestRule);


        //click on Feed at pos:0
        onView(withId(R.id.recyclerView_feeds))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //check ArticleList screen title
        onView(withId(R.id.toolbar)).check(MyAssertions
                .checkToolbarTitle(feedList.get(0).title));

        //check ArticleList
        EspressoOperations.checkArticleListRecyclerView(feedList.get(0).articles);

        //Favourite Article at pos:0
        onView(MyMatcher.atPositionWithId(
                R.id.recyclerView_articles,
                0,
                R.id.imageView_article_favourite_indicator)).perform(click());

        Espresso.pressBack();

        //click on Feed at pos:0
        onView(withId(R.id.recyclerView_feeds))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        feedList.get(0).articles.get(0).isFavourite = true;

        //check ArticleList
        EspressoOperations.checkArticleListRecyclerView(feedList.get(0).articles);

        //Unfavourite Article at pos:0
        onView(MyMatcher.atPositionWithId(R.id.recyclerView_articles, 0,
                R.id.imageView_article_favourite_indicator)).perform(click());

        Espresso.pressBack();

        //click on Feed at pos:0
        onView(withId(R.id.recyclerView_feeds))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        feedList.get(0).articles.get(0).isFavourite = false;

        //check ArticleList
        EspressoOperations.checkArticleListRecyclerView(feedList.get(0).articles);

        //TODO Аналог клика на фав, только поиск холдера по тексту
//        onView(MyMatcher.withRecyclerView(R.id.recyclerView_articles)
//                .withTextWithId(feedList.get(0).articles.get(1).title, R.id.textView_article_title,
//                        R.id.imageView_article_favourite_indicator)).perform(click());

    }
    @Test
    public void showFavouriteArticles() throws Exception{
        List<Feed> feedList = DataLab.generateFeeds(2, 2);
        databaseOperations.addFeeds(feedList);
        EspressoOperations.startActivityDefaultIntent(mainActivityActivityTestRule);

        //show Favourite screen
        onView(withId(R.id.show_favourites)).perform(click());

        //check Favourite screen title. Should be feed.title
        onView(withId(R.id.toolbar)).check(MyAssertions
                .checkToolbarTitle(EspressoOperations
                        .getString(R.string.toolbar_title_favourite_articles, mainActivityActivityTestRule)));


        //check ArticleList. Should be empty
        EspressoOperations.checkArticleListRecyclerView(new ArrayList<>());
        Espresso.pressBack();

        //click on Feed at pos:0
        onView(withId(R.id.recyclerView_feeds))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //Favourite Article at pos:0
        onView(MyMatcher.atPositionWithId(R.id.recyclerView_articles, 0,
                R.id.imageView_article_favourite_indicator)).perform(click());
        //Favourite Article at pos:1
        onView(MyMatcher.atPositionWithId(R.id.recyclerView_articles, 1,
                R.id.imageView_article_favourite_indicator)).perform(click());

        feedList.get(0).articles.get(0).isFavourite = true;
        feedList.get(0).articles.get(1).isFavourite = true;

        Espresso.pressBack();

        //Show Favourite screen
        onView(withId(R.id.show_favourites)).perform(click());

        //check ArticleList. Should be favourite 2 articles
        EspressoOperations.checkArticleListRecyclerView(feedList.get(0).articles);

    }

    @Test
    public void markArticleAsRead() throws Exception{
        List<Feed> feedList = DataLab.generateFeeds(2, 2);
        databaseOperations.addFeeds(feedList);
        EspressoOperations.startActivityDefaultIntent(mainActivityActivityTestRule);

        //click on Feed at pos:0
        onView(withId(R.id.recyclerView_feeds))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //check ArticleList. All articles should be new
        EspressoOperations.checkArticleListRecyclerView(feedList.get(0).articles);

        //mark article as read at pos:0
        onView(withId(R.id.recyclerView_articles))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        feedList.get(0).articles.get(0).isNew = false;

        Espresso.pressBack();

        //check ArticleList. First article should be readed
        EspressoOperations.checkArticleListRecyclerView(feedList.get(0).articles);

        //mark article as read at pos:1
        onView(withId(R.id.recyclerView_articles))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));
        feedList.get(0).articles.get(1).isNew = false;

        Espresso.pressBack();

        //check ArticleList. All article should be readed
        EspressoOperations.checkArticleListRecyclerView(feedList.get(0).articles);
    }

}
