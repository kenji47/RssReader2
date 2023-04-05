package com.kenji1947.rssreader.data.worker.feed_sync_scheduler.job_scheduler;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;

/**
 * Created by chamber on 07.12.2017.
 */

public class JobSchedulerWrapperImpl implements JobSchedulerWrapper {
    private JobScheduler jobScheduler;

    public JobSchedulerWrapperImpl(JobScheduler jobScheduler) {
        this.jobScheduler = jobScheduler;
    }

    @Override
    public int schedule(JobInfo jobInfo) {
        return jobScheduler.schedule(jobInfo);
    }

    @Override
    public void cancel(int jobId) {
        jobScheduler.cancel(jobId);
    }
}
