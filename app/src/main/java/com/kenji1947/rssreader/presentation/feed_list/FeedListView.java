package com.kenji1947.rssreader.presentation.feed_list;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.kenji1947.rssreader.domain.entities.Feed;
import com.kenji1947.rssreader.presentation.common.ListDataDiffHolder;

import java.util.List;

/**
 * Created by kenji1947 on 11.11.2017.
 */


public interface FeedListView extends MvpView {
    @StateStrategyType(AddToEndSingleStrategy.class)
    void showFeedSubscriptions(List<Feed> feedSubscriptions);

    @StateStrategyType(AddToEndSingleStrategy.class)
    void showFeedObservableSubscriptions(ListDataDiffHolder<Feed> dataDiffHolder);

    @StateStrategyType(AddToEndSingleStrategy.class)//TODO
    void setIsBackgroundFeedUpdateEnabled(boolean isEnabled);



    @StateStrategyType(AddToEndSingleStrategy.class)
    void showProgress(boolean progress);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showMessage(String message);

    //@StateStrategyType(SkipStrategy.class)
    @StateStrategyType(OneExecutionStateStrategy.class)
    void showNewArticlesCountMessage(Integer count);

    //error messages
    @StateStrategyType(SkipStrategy.class)
    void showErrorMessage(String message);

    @StateStrategyType(SkipStrategy.class)
    void onFeedChange(boolean b);
}
