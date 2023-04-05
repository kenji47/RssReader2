package com.kenji1947.rssreader.presentation.activity_test;

import android.support.v7.view.menu.MenuView;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Created by chamber on 15.12.2017.
 */

public interface TestActivityView extends MvpView {
    @StateStrategyType(SkipStrategy.class)
    void next(TestActivity source);
}
