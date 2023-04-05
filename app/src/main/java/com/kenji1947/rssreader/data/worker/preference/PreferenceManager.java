package com.kenji1947.rssreader.data.worker.preference;

import android.content.SharedPreferences;

public interface PreferenceManager {
    
    boolean isFeedSyncSchedulerEnabled();
    void setFeedSyncSchedulerStatus(boolean enable);

    long getFeedSyncSchedulerInterval();
    void setFeedSyncSchedulerInterval(long intervalMillis);

    void registerPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener);
    void unregisterPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener);
}
