package com.kenji1947.rssreader.data.worker.feed_sync_scheduler;

import com.kenji1947.rssreader.data.worker.feed_sync_scheduler.job_scheduler.FeedUpdateServiceView;
import com.kenji1947.rssreader.data.worker.notifications.NotificationFactory;
import com.kenji1947.rssreader.data.worker.notifications.NotificationManagerWrapper;
import com.kenji1947.rssreader.data.worker.resource_manager.ResourceManager;
import com.kenji1947.rssreader.domain.interactors.article.ObserveArticlesModificationInteractor;
import com.kenji1947.rssreader.domain.interactors.feed.FeedUpdateInteractor;
import com.kenji1947.rssreader.domain.util.RxSchedulersProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;

/**
 * Created by chamber on 24.12.2017.
 */

//TODO Delete
public class FeedUpdateServicePresenter {
    private FeedUpdateInteractor updateAllFeedsInteractor;
    private NotificationManagerWrapper notificationManager;
    private NotificationFactory notificationFactory;
    private ResourceManager resourceManager;
    private RxSchedulersProvider schedulersProvider;
    private ObserveArticlesModificationInteractor observeArticleUpdatesInteractor;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private FeedUpdateServiceView serviceView;

    @Inject
    public FeedUpdateServicePresenter(
            FeedUpdateInteractor updateAllFeedsInteractor,
            NotificationManagerWrapper notificationManager,
            NotificationFactory notificationFactory,
            ObserveArticlesModificationInteractor observeArticleUpdatesInteractor,
            ResourceManager resourceManager,
            RxSchedulersProvider schedulersProvider) {

        this.updateAllFeedsInteractor = updateAllFeedsInteractor;
        this.notificationManager = notificationManager;
        this.notificationFactory = notificationFactory;
        this.observeArticleUpdatesInteractor = observeArticleUpdatesInteractor;
        this.resourceManager = resourceManager;
        this.schedulersProvider = schedulersProvider;

    }


    public void attachView(FeedUpdateServiceView view) {
        Timber.d("attachView");
        serviceView = view;
    }


    public void onDestroy() {
        Timber.d("onDestroy");
        compositeDisposable.clear();
        serviceView = null;
    }

    public void updateAllFeeds() {
        Timber.d("updateAllFeeds");
        compositeDisposable.add(updateAllFeedsInteractor.updateAllFeedsAndGetNewArticlesCount()
                //.observeOn(schedulersProvider.getIo()) //TODO
                .subscribe(this::onUpdateAllFeedsSuccess, this::onUpdateAllFeedsError));
    }

    private void onUpdateAllFeedsSuccess(int newArticlesCount) {
        Timber.d("onUpdateAllFeedsSuccess newArticlesCount: " + newArticlesCount);
        if (serviceView != null) {
            if (newArticlesCount > 0) {
                //TODO Зачем закоменчено
                //observeArticleUpdatesInteractor.notifyArticleModified();
                serviceView.showNewArticlesNotification(newArticlesCount, 5);
            }
            serviceView.finishJob();
        }
    }

//    private String getContentTitle(int newArticlesCount, int unreadArticles) {
//        return resourceManager.getString(R.string.new_articles);
//}
//    private String getContentText(int newArticlesCount, int unreadArticles) {
//        return resourceManager.getStringFormat(
//                R.string.there_have_been_new_articles,
//                newArticlesCount + "", unreadArticles + ""
//        );
//    }

    private void onUpdateAllFeedsError(Throwable throwable) {
        Timber.d("onUpdateAllFeedsError " + throwable);
        throwable.printStackTrace();
        if (serviceView != null) {
            serviceView.finishJob();
        }
    }
}
