package com.kenji1947.rssreader.domain.worker;

/**
 * Created by chamber on 07.12.2017.
 */

public interface FeedSyncScheduler {
    void enableFeedSyncScheduler(long intervalMillis);

    void enableFeedSyncScheduler(); //TODO Добавить интервал обновления
    void cancelFeedSyncScheduler();
}
