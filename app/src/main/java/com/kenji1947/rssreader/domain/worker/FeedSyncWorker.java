package com.kenji1947.rssreader.domain.worker;

/**
 * Created by chamber on 17.03.2018.
 */

public interface FeedSyncWorker {
    void startFeedSync();
    void cancelFeedSync();
}
