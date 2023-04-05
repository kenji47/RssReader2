package com.kenji1947.rssreader.di.presenter;

import com.kenji1947.rssreader.data.worker.feed_sync_worker.FeedSyncServicePresenter;
import com.kenji1947.rssreader.di.application.AppComponent;

import dagger.Component;

/**
 * Created by chamber on 18.03.2018.
 */

@PresenterScope
@Component(dependencies = AppComponent.class)
public interface FeedSyncServicePresenterComponent {
    FeedSyncServicePresenter providePresenter();

    final class Initializer {

        public static FeedSyncServicePresenterComponent init(AppComponent appComponent) {
            return DaggerFeedSyncServicePresenterComponent.builder()
                    .appComponent(appComponent)
                    .build();
        }
        private Initializer() {
        }
    }
}
