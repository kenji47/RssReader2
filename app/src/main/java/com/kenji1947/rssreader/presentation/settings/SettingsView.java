package com.kenji1947.rssreader.presentation.settings;

import com.arellomobile.mvp.MvpView;
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.kenji1947.rssreader.domain.entities.AppSettings;

/**
 * Created by chamber on 27.12.2017.
 */

public interface SettingsView extends MvpView {

    //TODO Разобрать стратегии
    @StateStrategyType(AddToEndSingleStrategy.class)
    void setAppSettings(AppSettings appSettings);
}
