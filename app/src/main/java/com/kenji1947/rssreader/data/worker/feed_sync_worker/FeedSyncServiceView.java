package com.kenji1947.rssreader.data.worker.feed_sync_worker;

/**
 * Created by chamber on 17.03.2018.
 */

public interface FeedSyncServiceView {

    void showFeedSyncNotificationProgress(int feedsTotal, int feedProgress);
    void showFeedSyncNotificationComplete(int newArticlesCount);

    void finishJob();
}
