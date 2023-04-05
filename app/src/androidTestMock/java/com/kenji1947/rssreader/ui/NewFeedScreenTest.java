package com.kenji1947.rssreader.ui;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.kenji1947.rssreader.App;
import com.kenji1947.rssreader.R;
import com.kenji1947.rssreader.fakes.AppSetup;
import com.kenji1947.rssreader.fakes.DataLab;
import com.kenji1947.rssreader.presentation.MainActivity;
import com.kenji1947.rssreader.util.DatabaseOperationsImpl;
import com.kenji1947.rssreader.util.EspressoOperations;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by chamber on 20.12.2017.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class NewFeedScreenTest {
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

    @Test
    public void addFeed_Success() throws Exception{
        AppSetup.feedApiServiceState = AppSetup.FEED_SERVICE_STATE.RETURN_SAME_FEED;

        EspressoOperations.startActivityDefaultIntent(mainActivityActivityTestRule);

        //check empty pic
        onView(withId(R.id.linearlayout_empty_list))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        //add new feed xxx
        EspressoOperations.addNewFeedThroughDialog(DataLab.URL_XXX);

        //check FeedList. Should be feed xxx
        EspressoOperations.checkFeedListRecyclerViewByScrollToHolder(DataLab.getFeeds());

        //check empty pic
        onView(withId(R.id.linearlayout_empty_list))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        //add new feed yyy
        EspressoOperations.addNewFeedThroughDialog(DataLab.URL_YYY);

        //check FeedList. Should be 2 feeds
        EspressoOperations.checkFeedListRecyclerViewByScrollToHolder(DataLab.getFeeds());

        //click on xxx feed
        onView(withId(R.id.recyclerView_feeds))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //check ArticleList
        EspressoOperations.checkArticleListRecyclerViewByScrollToHolder(DataLab.generateFeed(DataLab.URL_XXX).articles);
        Espresso.pressBack();

        //click on yyy feed
        onView(withId(R.id.recyclerView_feeds))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        //check ArticleList
        EspressoOperations.checkArticleListRecyclerViewByScrollToHolder(DataLab.generateFeed(DataLab.URL_YYY).articles);
        Espresso.pressBack();
    }

    @Test
    public void addFeed_Error_AlreadySubscribed() throws Exception{

        EspressoOperations.startActivityDefaultIntent(mainActivityActivityTestRule);

        //check empty
        onView(withId(R.id.linearlayout_empty_list))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        //try to add same feed twice. Should be error message
        EspressoOperations.addNewFeedThroughDialog(DataLab.URL_XXX);
        EspressoOperations.addNewFeedThroughDialog(DataLab.URL_XXX);

        //check error message
        onView(withId(R.id.textView_error_message))
                .check(matches(withText(
                        EspressoOperations.getString(
                                R.string.message_feed_already_subscribed,
                                mainActivityActivityTestRule)
                )));

        Espresso.closeSoftKeyboard();
        Espresso.pressBack();

        //check FeedList. Should be feed xxx
        DataLab.generateFeed(DataLab.URL_XXX);
        EspressoOperations.checkFeedListRecyclerViewByScrollToHolder(DataLab.getFeeds());
    }

    @Test
    public void addFeed_Error_NoNetwork() throws Exception{
        AppSetup.networkState = AppSetup.NETWORK_STATE.OFFLINE;
        EspressoOperations.startActivityDefaultIntent(mainActivityActivityTestRule);

        //try to add feed without network connection. Should be error message
        EspressoOperations.addNewFeedThroughDialog(DataLab.URL_XXX);

        onView(withId(R.id.textView_error_message))
                .check(matches(withText(
                        EspressoOperations.getString(
                                R.string.message_no_network,
                                mainActivityActivityTestRule)
                )));
        Espresso.closeSoftKeyboard();
        Espresso.pressBack();

        //check empty pic
        onView(withId(R.id.linearlayout_empty_list))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));

        //check FeedList. Should be empty
        EspressoOperations.checkFeedListRecyclerViewByScrollToHolder(new ArrayList<>());
    }

    //@Test
    public void addFeed_Error_UrlNotValid() throws Exception{
        //add feed

        //add feed

        //check error

        //check list
    }
    //@Test
    public void addFeed_Error_Other() throws Exception{
        //add feed

        //add feed

        //check error

        //check list
    }


}
