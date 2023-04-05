package com.kenji1947.rssreader.util;

import android.content.Intent;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.azimolabs.conditionwatcher.ConditionWatcher;
import com.kenji1947.rssreader.R;
import com.kenji1947.rssreader.domain.entities.Article;
import com.kenji1947.rssreader.domain.entities.Feed;
import com.kenji1947.rssreader.presentation.feed_list.FeedListAdapter;

import java.util.List;

import timber.log.Timber;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by chamber on 20.12.2017.
 */

public class EspressoOperations {
    public static ActivityTestRule activityTestRule;


    public static void waitForListSize(int recyclerViewId, int size) throws Exception {
        ConditionWatcher.waitForCondition(InstructionBuilder
                .buildCheckAdapterSize(recyclerViewId, size, "", activityTestRule.getActivity()));
    }

    public static void startActivityDefaultIntent(ActivityTestRule activityTestRule) {
        activityTestRule.launchActivity(new Intent());
    }

    public static void addNewFeedThroughDialog(String url) {
        onView(withId(R.id.fab)).perform(click());
        onView(withId(R.id.textInputEditText_feed_url)).perform(typeText(url));
        onView(withText(R.string.dialog_new_feed_button_positive)).perform(click());
    }

    public static String getString(int res, ActivityTestRule activityTestRule) {
        return activityTestRule.getActivity().getString(res);
    }
    //---

    public static void checkFeedListRecyclerView(List<Feed> feedList) {
        onView(withId(R.id.recyclerView_feeds))
                .check(MyAssertions.checkFeedList(feedList).assertFeedList());
    }

    public static void checkArticleListRecyclerView(List<Article> articleList) {
        onView(withId(R.id.recyclerView_articles))
                .check(MyAssertions.checkArticleList(articleList).assertArticleList());
    }


    public static void clickOnViewInHolder(int recyclerViewId, int targetViewId, int pos) {
        onView(withId(recyclerViewId)).perform(RecyclerViewActions.scrollToPosition(pos));
        onView(MyMatcher.withRecyclerView(recyclerViewId)
                .atPositionWithId(pos, targetViewId))
                .perform(click());
    }

    //Old
    public static void checkFeedListRecyclerViewByScrollToHolder(List<Feed> feedList) throws Exception {
        waitForListSize(R.id.recyclerView_feeds, feedList.size());
        for (int i = 0; i < feedList.size(); i++) {
            onView(withId(R.id.recyclerView_feeds))
                    .perform(RecyclerViewActions.scrollToPosition(i))
                    .check(MyAssertions.checkFeedList2(feedList.get(i), i).assertFeedList());
        }
    }


    public static void checkArticleListRecyclerViewByScrollToHolder(List<Article> articleList) throws Exception {
        waitForListSize(R.id.recyclerView_articles, articleList.size());
        for (int i = 0; i < articleList.size(); i++) {
            onView(withId(R.id.recyclerView_articles))
                    .perform(RecyclerViewActions.scrollToPosition(i))
                    .check(MyAssertions.checkArticleList2(articleList.get(i), i).assertFeedList());
        }

    }
}
