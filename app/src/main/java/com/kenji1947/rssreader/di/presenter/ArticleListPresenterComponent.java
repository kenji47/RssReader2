package com.kenji1947.rssreader.di.presenter;

import com.kenji1947.rssreader.di.application.AppComponent;
import com.kenji1947.rssreader.di.presenter.modules.ArticleListPresenterModule;
import com.kenji1947.rssreader.presentation.article_list.ArticleListPresenter;

import dagger.Component;

/**
 * Created by chamber on 14.12.2017.
 */

@PresenterScope
@Component(dependencies = AppComponent.class, modules = ArticleListPresenterModule.class)
public interface ArticleListPresenterComponent {
    ArticleListPresenter providePresenter();

    final class Initializer {

        public static ArticleListPresenterComponent init(AppComponent appComponent, long id, boolean isFavModeOn) {
            return DaggerArticleListPresenterComponent.builder()
                    .appComponent(appComponent)
                    .articleListPresenterModule(new ArticleListPresenterModule(id, isFavModeOn))
                    .build();
        }

        private Initializer() {
        }
    }
}
