package com.kenji1947.rssreader.presentation;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by chamber on 16.12.2017.
 */

public class Screens {
    public static final String FEED_LIST_SCREEN = "feed_list_screen";
    public static final String NEW_FEED_SCREEN = "new_feed_screen";
    public static final String ARTICLE_LIST_SCREEN = "article_list_screen";
    public static final String ARTICLE_FAV_LIST_SCREEN = "article_fav_list_screen";
    public static final String ARTICLE_DETAIL_SCREEN = "article_detail_screen";
    public static final String SETTINGS_SCREEN = "settings_screen";
    public static final String NEW_FEED_DIALOG = "new_feed_dialog";

    public static final int RESULT_NEW_FEED_SCREEN_UPDATE = 1;

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
