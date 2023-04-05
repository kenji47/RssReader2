package com.kenji1947.rssreader.util;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;

import org.hamcrest.Matcher;

/**
 * Created by chamber on 21.12.2017.
 */

public class MyActions {

    public static void rotateScreen(Activity activity) {
        System.out.println("ROTATING");
        int orientation = activity.getResources().getConfiguration().orientation;
        activity.setRequestedOrientation(
                (orientation == Configuration.ORIENTATION_PORTRAIT) ?
                        ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE :
                        ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }


}
