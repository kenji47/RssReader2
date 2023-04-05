package com.kenji1947.rssreader.di.presenter;

import com.kenji1947.rssreader.di.application.AppComponent;
import com.kenji1947.rssreader.di.presenter.modules.FeedListPresenterModule;
import com.kenji1947.rssreader.presentation.feed_list.FeedListPresenter;

import dagger.Component;

/**
 * Created by kenji1947 on 13.11.2017.
 */

@PresenterScope
@Component(dependencies = AppComponent.class, modules = FeedListPresenterModule.class)
public interface FeedListPresenterComponent {
    FeedListPresenter providePresenter();

    //TODO Разобрать такой способ
    final class Initializer {

        public static FeedListPresenterComponent init(AppComponent appComponent, long id) {
            return DaggerFeedListPresenterComponent.builder()
                    .appComponent(appComponent)
                    .feedListPresenterModule(new FeedListPresenterModule(id))
                    .build();
        }

        private Initializer() {
        }
    }
}
