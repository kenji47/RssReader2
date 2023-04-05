package com.kenji1947.rssreader.domain.entities;

/**
 * Created by chamber on 27.12.2017.
 */

public class AppSettings {
    private boolean isFeedSyncSchedulerEnabled;
    private long feedSyncSchedulerInterval;

    public AppSettings(boolean isFeedSyncSchedulerEnabled, long feedSyncSchedulerInterval) {
        this.isFeedSyncSchedulerEnabled = isFeedSyncSchedulerEnabled;
        this.feedSyncSchedulerInterval = feedSyncSchedulerInterval;
    }

    public boolean isFeedSyncSchedulerEnabled() {
        return isFeedSyncSchedulerEnabled;
    }

    public long getFeedSyncSchedulerInterval() {
        return feedSyncSchedulerInterval;
    }
}
