package com.kenji1947.rssreader.presentation.activity_test;

import android.support.v4.app.FragmentManager;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.kenji1947.rssreader.R;
import com.kenji1947.rssreader.presentation.feed_list.FeedListFragment;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Created by chamber on 15.12.2017.
 */

@InjectViewState
public class TestActivityPresenter extends MvpPresenter<TestActivityView>{
    List<TestActivity> activityList = new ArrayList<>();

   void setActivity(TestActivity activity) {
       activityList.add(activity);
    };

   void checkActivityEquality() {
       Timber.d("" + activityList.get(0).equals(activityList.get(1)));
       Timber.d("Number of activities " + activityList.size());
       for (TestActivity activity : activityList) {
           Timber.d(activity.toString());
       }
   }

   void checkFragmentManagerEquality() {
       Timber.d("checkFragmentManagerEquality");
       for (TestActivity activity : activityList) {
           Timber.d(activity.getSupportFragmentManager().toString());
       }
   }

    public void next() {
       getViewState().next(activityList.get(0));
    }

    public void nextFragment() {
        FeedListFragment feedListFragment = FeedListFragment.newInstance();
        FragmentManager fragmentManager = activityList.get(0).getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.frameLayout_container, feedListFragment, "tag")
                .addToBackStack("")
                .commit();
    }

    public void finishActivity() {
       Timber.d("finishActivity " + activityList.get(1).toString());
       activityList.get(1).finish();
    }

}
