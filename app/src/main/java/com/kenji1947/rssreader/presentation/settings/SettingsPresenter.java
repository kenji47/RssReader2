package com.kenji1947.rssreader.presentation.settings;

import android.content.SharedPreferences;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.kenji1947.rssreader.data.worker.error_handler.ErrorHandler;
import com.kenji1947.rssreader.data.worker.preference.PreferenceManager;
import com.kenji1947.rssreader.domain.interactors.feed.FeedSyncInteractor;
import com.kenji1947.rssreader.domain.interactors.feed.FeedSyncSchedulerInteractor;
import com.kenji1947.rssreader.domain.interactors.settings.AppSettingsInteractor;
import com.kenji1947.rssreader.domain.util.RxSchedulersProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import ru.terrakok.cicerone.Router;
import timber.log.Timber;

/**
 * Created by chamber on 27.12.2017.
 */

@InjectViewState
public class SettingsPresenter extends MvpPresenter<SettingsView> {

    private RxSchedulersProvider rxSchedulersProvider;
    private FeedSyncSchedulerInteractor feedSyncSchedulerInteractor;
    private AppSettingsInteractor appSettingsInteractor;
    private Router router;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private ErrorHandler errorHandler;


    SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
            Timber.d("onSharedPreferenceChanged " + s);
//            switch (s) {
//                case KEY_SHOULD_UPDATE_FEEDS_IN_BACKGROUND:
//                    Timber.d("KEY_SHOULD_UPDATE_FEEDS_IN_BACKGROUND " +
//                            sharedPreferences.getBoolean(KEY_SHOULD_UPDATE_FEEDS_IN_BACKGROUND, false));
//                    break;
//                case KEY_FEED_UPDATE_SERVICE_PERIOD:
//                    Timber.d("KEY_FEED_UPDATE_SERVICE_PERIOD " +
//                            sharedPreferences.getLong(KEY_FEED_UPDATE_SERVICE_PERIOD, DEFAULT_FEED_UPDATE_SERVICE_PERIOD));
//                    break;
//            }
        }
    };

    @Inject
    public SettingsPresenter(RxSchedulersProvider schedulersProvider,
                             FeedSyncInteractor backgroundFeedUpdateInteractor,
                             FeedSyncSchedulerInteractor feedSyncSchedulerInteractor,
                             AppSettingsInteractor appSettingsInteractor,
                             PreferenceManager preferenceManager,
                             ErrorHandler errorHandler,
                             Router router) {
        this.rxSchedulersProvider = schedulersProvider;
        this.feedSyncSchedulerInteractor = feedSyncSchedulerInteractor;
        this.appSettingsInteractor = appSettingsInteractor;
        this.errorHandler = errorHandler;
        this.router = router;
    }

    @Override
    protected void onFirstViewAttach() {
        super.onFirstViewAttach();
        Timber.d("onFirstViewAttach");
        //preferenceManager.registerPreferenceChangeListener(preferenceChangeListener);
        loadSettings();
    }

    private void loadSettings() {
        appSettingsInteractor.getAppSettings()
                .observeOn(rxSchedulersProvider.getMain())
                .subscribe(preferences -> {
                    getViewState().setAppSettings(preferences);
                });
    }


    public void setFeedSyncSchedulerStatus(boolean enabled) {
        (enabled ? feedSyncSchedulerInteractor.enableFeedSyncScheduler()
                : feedSyncSchedulerInteractor.disableFeedSyncScheduler())
                .observeOn(rxSchedulersProvider.getMain())
                .subscribe(() -> {Timber.d("setFeedSyncSchedulerStatus Complete " + enabled);});
    }

    public void setFeedSyncSchedulerInterval(long intervalMillis) {
        appSettingsInteractor.setFeedSyncSchedulerInterval(intervalMillis)
                .observeOn(rxSchedulersProvider.getMain())
                .subscribe(() -> {Timber.d("setFeedSyncSchedulerInterval Complete " + intervalMillis);});
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
        //preferenceManager.unregisterPreferenceChangeListener(preferenceChangeListener);
    }

    public void onBackPressed() {
        router.exit();
    }
}
