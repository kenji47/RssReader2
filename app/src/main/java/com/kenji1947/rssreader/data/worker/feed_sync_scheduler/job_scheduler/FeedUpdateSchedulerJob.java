package com.kenji1947.rssreader.data.worker.feed_sync_scheduler.job_scheduler;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;

import com.kenji1947.rssreader.data.worker.preference.PreferenceManager;
import com.kenji1947.rssreader.domain.worker.FeedSyncScheduler;

import timber.log.Timber;

/**
 * Created by chamber on 07.12.2017.
 */

public class FeedUpdateSchedulerJob implements FeedSyncScheduler {

    //TODO Вынести в Даггер?
    private static final int FEEDS_UPDATE_JOB_ID = 8791;
    private static final int FEEDS_UPDATE_INTERVAL_MILLIS = 4000;


    private ComponentName feedsUpdateJobService;
    private PreferenceManager preferenceManager;
    private JobSchedulerWrapper jobSchedulerWrapper;
    private JobInfo jobInfo;

    public FeedUpdateSchedulerJob(
            ComponentName feedsUpdateJobService,
            PreferenceManager preferenceManager,
            JobSchedulerWrapper jobSchedulerWrapper,
            JobInfo jobInfo
    ) {
        this.feedsUpdateJobService = feedsUpdateJobService;
        this.preferenceManager = preferenceManager;
        this.jobSchedulerWrapper = jobSchedulerWrapper;
        this.jobInfo = jobInfo;
    }

    @Override
    public void enableFeedSyncScheduler(long intervalMillis) {

    }

    //TODO
    @Override
    public void enableFeedSyncScheduler() {
        Timber.d("enableFeedSyncScheduler");
        int result = jobSchedulerWrapper.schedule(buildJobInfo());
        checkScheduleResult(result);
    }

    private JobInfo buildJobInfo() {
        return JobInfoFactory.createJobInfo(
                FEEDS_UPDATE_JOB_ID,
                preferenceManager.getFeedSyncSchedulerInterval(),
                feedsUpdateJobService
        );
    }

    @Override
    public void cancelFeedSyncScheduler() {
        Timber.d("cancelFeedSyncScheduler");
        jobSchedulerWrapper.cancel(jobInfo.getId());
    }


    private void checkScheduleResult(final int scheduleResult) {
        if (scheduleResult != JobScheduler.RESULT_SUCCESS) {
            Timber.e("Failed to schedule background feeds update");
        }
    }
}
