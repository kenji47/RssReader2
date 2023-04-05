package com.kenji1947.rssreader.di.presenter;

import com.kenji1947.rssreader.di.application.AppComponent;
import com.kenji1947.rssreader.presentation.settings.SettingsPresenter;

import dagger.Component;

/**
 * Created by chamber on 27.12.2017.
 */
@PresenterScope
@Component(dependencies = AppComponent.class)
public interface SettingsPresenterComponent {
    SettingsPresenter provideSettingsPresenter();

    final class Initializer {

        public static SettingsPresenterComponent init(AppComponent appComponent) {
            return DaggerSettingsPresenterComponent.builder()
                    .appComponent(appComponent)
                    .build();
        }

        private Initializer() {
        }
    }
}
