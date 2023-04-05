package com.kenji1947.rssreader.di.presenter;

import com.kenji1947.rssreader.di.application.AppComponent;
import com.kenji1947.rssreader.presentation.new_feed.FeedNewPresenter;

import dagger.Component;

/**
 * Created by chamber on 14.12.2017.
 */

@PresenterScope
@Component(dependencies = AppComponent.class)
public interface FeedNewPresenterComponent {

    FeedNewPresenter provideFeedNewPresenter();

    final class Initializer {

        public static FeedNewPresenterComponent init(AppComponent appComponent) {
            return DaggerFeedNewPresenterComponent.builder()
                    .appComponent(appComponent)
                    .build();
        }

        private Initializer() {
        }
    }
}
