package com.kenji1947.rssreader.data.worker.feed_sync_scheduler.job_scheduler;

/**
 * Created by chamber on 24.12.2017.
 */

public interface FeedUpdateServiceView {
    void finishJob();
    void showNewArticlesNotification(int newArticlesCount, int unreadArticles);
}
