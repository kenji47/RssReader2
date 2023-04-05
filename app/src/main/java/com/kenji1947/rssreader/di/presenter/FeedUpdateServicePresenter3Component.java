package com.kenji1947.rssreader.di.presenter;

import com.kenji1947.rssreader.data.worker.feed_sync_scheduler.FeedUpdateServicePresenter;
import com.kenji1947.rssreader.di.application.AppComponent;

import dagger.Component;

/**
 * Created by chamber on 20.12.2017.
 */
@PresenterScope
@Component(dependencies = AppComponent.class)
public interface FeedUpdateServicePresenter3Component {
    FeedUpdateServicePresenter providePresenter();

    final class Initializer {

        public static FeedUpdateServicePresenter3Component init(AppComponent appComponent) {
            return DaggerFeedUpdateServicePresenter3Component.builder()
                    .appComponent(appComponent)
                    .build();
        }

        private Initializer() {
        }
    }
}
