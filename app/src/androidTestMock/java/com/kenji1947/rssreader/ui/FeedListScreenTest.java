package com.kenji1947.rssreader.ui;

import android.icu.util.TimeUnit;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.MenuItem;

import com.azimolabs.conditionwatcher.ConditionWatcher;
import com.kenji1947.rssreader.*;
import com.kenji1947.rssreader.domain.entities.Feed;
import com.kenji1947.rssreader.fakes.AppSetup;
import com.kenji1947.rssreader.fakes.DataLab;
import com.kenji1947.rssreader.presentation.MainActivity;
import com.kenji1947.rssreader.util.DatabaseOperationsImpl;
import com.kenji1947.rssreader.util.InstructionBuilder;
import com.kenji1947.rssreader.util.MyMatcher;
import com.kenji1947.rssreader.util.EspressoOperations;


import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;

/**
 * Created by chamber on 20.12.2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FeedListScreenTest {
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
    public void firstOpen_ShouldShowEmptyPic_WhenDbEmpty() throws Exception{
        EspressoOperations.startActivityDefaultIntent(mainActivityActivityTestRule);

        onView(withId(R.id.linearlayout_empty_list))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }

    @Test
    public void firstOpen_ShouldShowContent_WhenDbHaveRecords() throws Exception{
        databaseOperations.addFeeds(DataLab.generateFeeds(15, 2));

        EspressoOperations.startActivityDefaultIntent(mainActivityActivityTestRule);

        onView(withId(R.id.linearlayout_empty_list))
                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));

        EspressoOperations.checkFeedListRecyclerViewByScrollToHolder(DataLab.getFeeds());
    }

    //@Test
    public void clickToggleNotification() throws Exception{
        EspressoOperations.startActivityDefaultIntent(mainActivityActivityTestRule);
        //очистить префы

        //TODO Нужно узнать какой ресурс назначен кнопке

//        onView(withId(R.id.toggle_notifications))
//                .perform(click());
//
//        onView(withId(R.id.toggle_notifications)).check((view, noViewFoundException) -> {
//            if (view instanceof MenuItem) {
//                MenuItem menuItem = (MenuItem) view;
//                menuItem.getIcon();
//            }
//        });
//
//        onView(withId(R.id.toggle_notifications))
//                .perform(click());

    }

    @Test
    public void deleteFeed() throws Exception{
        List<Feed> feeds = DataLab.generateFeeds(30, 10);
        databaseOperations.addFeeds(feeds);

        EspressoOperations.startActivityDefaultIntent(mainActivityActivityTestRule);

        EspressoOperations.waitForListSize(R.id.recyclerView_feeds, feeds.size());

        //click on feed with pos:0 options menu
        EspressoOperations.clickOnViewInHolder(R.id.recyclerView_feeds, R.id.imageButton_options_menu, 0);

        //click delete in context menu
        onView(withText(R.string.menu_popup_feed_list_delete)).perform(click());
        //click ok in dialog
        onView(withText(android.R.string.ok)).perform(click());
        feeds.remove(0);

        //check FeedList. Should delete feed at pos:0
        EspressoOperations.checkFeedListRecyclerViewByScrollToHolder(feeds);

        Thread.sleep(100);
        //click on feed with pos:0 options menu
        EspressoOperations.clickOnViewInHolder(R.id.recyclerView_feeds, R.id.imageButton_options_menu, 0);

        //click delete in context menu
        onView(withText(R.string.menu_popup_feed_list_delete)).perform(click());
        //click ok in dialog
        onView(withText(android.R.string.ok)).perform(click());;
        feeds.remove(0);

        //check FeedList. List should be empty
        EspressoOperations.checkFeedListRecyclerViewByScrollToHolder(feeds);

        //check empty pic
//        onView(withId(R.id.linearlayout_empty_list))
//                .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }
}
