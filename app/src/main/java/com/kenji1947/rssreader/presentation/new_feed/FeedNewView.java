package com.kenji1947.rssreader.presentation.new_feed;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.kenji1947.rssreader.domain.entities.Feed;
import com.kenji1947.rssreader.domain.entities.SearchedFeed;

import java.util.List;

/**
 * Created by chamber on 13.12.2017.
 */

public interface FeedNewView extends MvpView {

    //delete
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showProgress(boolean progress);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showSearchFeedLoadingDialog();

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showSubscribeFeedLoadingDialog(String feedTitle);

    //TODO Прохладное решение с несколькими методами!
    @StateStrategyType(AddToEndSingleStrategy.class)
    void hideLoadingDialog();

    //---
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showMessage(String message);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showErrorMessage(String message);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void setSearchedFeeds(List<SearchedFeed> feeds);

    //
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showKeyboard(boolean isShowing);

}
