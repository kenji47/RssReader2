package com.kenji1947.rssreader.ui;

import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiCollection;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject;
import android.support.test.uiautomator.UiObjectNotFoundException;
import android.support.test.uiautomator.UiSelector;
import android.test.suitebuilder.annotation.LargeTest;

import com.kenji1947.rssreader.App;
import com.kenji1947.rssreader.R;
import com.kenji1947.rssreader.data.worker.preference.PreferenceManager;
import com.kenji1947.rssreader.domain.entities.Feed;
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

import java.util.concurrent.TimeUnit;

import timber.log.Timber;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.is;

/**
 * Created by chamber on 26.12.2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FeedBackgroundUpdatesTest {
    private UiDevice device;
    private static DatabaseOperationsImpl databaseOperations;

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule
            = new ActivityTestRule<MainActivity>(MainActivity.class, false, false);

    @BeforeClass
    public static void beforeClass() {
        AppSetup.networkState = AppSetup.NETWORK_STATE.ONLINE;


        databaseOperations = new DatabaseOperationsImpl(App.INSTANCE);
        databaseOperations.clearAllDb();
    }

    @After
    public void after() {
        DataLab.clearAllData();
        databaseOperations.clearAllDb();
    }

    @Before
    public void before() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @Test
    public void backgroundFeedUpdateWithNewArticles_ShouldShowNotification() throws Exception {

        int newArticlesCount = 100;
        int unreadArticlesCount = 5;

        AppSetup.feedApiServiceState = AppSetup.FEED_SERVICE_STATE.RETURN_NEW_ARTICLES;

        //Disable background updates in preference. Set update interval to 2 seconds
        PreferenceManager preferenceManager = App.INSTANCE.getAppComponent().providePreferenceManager();
        preferenceManager.setFeedSyncSchedulerStatus(false);
        preferenceManager.setFeedSyncSchedulerInterval(2000);

        Feed feed = DataLab.generateFeed(DataLab.URL_XXX);
        databaseOperations.addFeed(feed);

        EspressoOperations.startActivityDefaultIntent(mainActivityActivityTestRule);

        onView(withId(R.id.toggle_notifications)).perform(click());

        TimeUnit.SECONDS.sleep(5);

        onView(withId(R.id.toggle_notifications)).perform(click());

        //click on Feed at pos:0
        onView(withId(R.id.recyclerView_feeds))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //check ArticleList. Should present 6 articles: 3 new and 3 old
        EspressoOperations.checkArticleListRecyclerView(feed.articles);
        Espresso.pressBack();

        device.openNotification();
        UiObject notificationTitle = device
                .findObject(new UiSelector().text(
                        EspressoOperations.getString(R.string.notification_feed_updated_title, mainActivityActivityTestRule)));

        String text = mainActivityActivityTestRule.getActivity()
                .getString(R.string.notification_feed_updated_text, newArticlesCount, unreadArticlesCount);
        UiObject notificationText = device
                .findObject(new UiSelector().text(text));

        notificationText.getText();
        notificationTitle.clickAndWaitForNewWindow();

        device.pressHome();
    }

    @Test
    public void backgroundFeedUpdateWithoutNewArticles() throws Exception {

        AppSetup.feedApiServiceState = AppSetup.FEED_SERVICE_STATE.RETURN_SAME_FEED;

        //Disable background updates in preference. Set update interval to 2 seconds
        PreferenceManager preferenceManager = App.INSTANCE.getAppComponent().providePreferenceManager();
        preferenceManager.setFeedSyncSchedulerStatus(false);
        preferenceManager.setFeedSyncSchedulerInterval(2000);

        Feed feed = DataLab.generateFeed(DataLab.URL_XXX);
        databaseOperations.addFeed(feed);

        EspressoOperations.startActivityDefaultIntent(mainActivityActivityTestRule);

        onView(withId(R.id.toggle_notifications)).perform(click());

        TimeUnit.SECONDS.sleep(5);

        onView(withId(R.id.toggle_notifications)).perform(click());

        //click on Feed at pos:0
        onView(withId(R.id.recyclerView_feeds))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //check ArticleList. Should present 3 old articles
        EspressoOperations.checkArticleListRecyclerView(feed.articles);
        Espresso.pressBack();

        device.openNotification();
        UiObject notificationTitle = device
                .findObject(new UiSelector().text(
                        EspressoOperations.getString(R.string.notification_feed_updated_title, mainActivityActivityTestRule)));

        device.pressHome();
    }

    private void findObj() throws UiObjectNotFoundException {
        UiObject notificationTitle = device
                .findObject(new UiSelector().text(
                        EspressoOperations.getString(R.string.notification_feed_updated_title, mainActivityActivityTestRule)));

        String text = mainActivityActivityTestRule.getActivity().getString(R.string.notification_feed_updated_text, 3, 5);
        UiObject notificationText = device
                .findObject(new UiSelector().text(text));

        notificationText.getText();
        notificationTitle.clickAndWaitForNewWindow();

    }

    private void findChild() throws UiObjectNotFoundException {
        UiCollection notifications = new UiCollection(new UiSelector().className("android.widget.FrameLayout"));
        UiObject notificationTitle =  notifications.getChildByText(new UiSelector().className("android.widget.TextView"), "New articles!");
        UiObject notificationText =  notifications.getChildByText(new UiSelector().className("android.widget.TextView"), "You have 3 new articles. 5 articles unread");
        notificationTitle.clickAndWaitForNewWindow();
    }

    private void old() {
        UiCollection notifications = new UiCollection(new UiSelector().className("android.widget.FrameLayout"));

        UiObject myNotification;
        try {
            myNotification = notifications
                    .getChildByText(new UiSelector().className("android.Widget.TextView"), "ObjectBox Browser");
            boolean b = myNotification.click();
        } catch (UiObjectNotFoundException e) {
            Timber.e(e);
        }
    }
}
