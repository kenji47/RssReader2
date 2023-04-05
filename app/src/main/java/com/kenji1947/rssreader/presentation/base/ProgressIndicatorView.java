package com.kenji1947.rssreader.presentation.base;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;

/**
 * Created by chamber on 19.12.2017.
 */

public interface ProgressIndicatorView extends MvpView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showProgress(boolean progress);
}
