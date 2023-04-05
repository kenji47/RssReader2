package com.kenji1947.rssreader.di.activity;

import com.kenji1947.rssreader.di.application.AppComponent;

import dagger.Component;

/**
 * Created by chamber on 15.12.2017.
 */

@Component(dependencies = AppComponent.class)
@ActivityScope
public interface ActivityComponent {
}
