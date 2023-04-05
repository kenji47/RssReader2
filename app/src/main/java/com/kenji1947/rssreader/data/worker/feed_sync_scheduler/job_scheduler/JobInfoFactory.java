package com.kenji1947.rssreader.data.worker.feed_sync_scheduler.job_scheduler;

import android.app.job.JobInfo;
import android.content.ComponentName;

import timber.log.Timber;

/**
 * Created by chamber on 07.12.2017.
 */

public class JobInfoFactory {
    private JobInfoFactory() {
    }

    public static JobInfo createJobInfo(int jobId, long intervalMin, ComponentName jobService) {
        Timber.d("createJobInfo jobId:" + jobId + " intervalMin:" + intervalMin);
        JobInfo.Builder builder = new JobInfo.Builder(jobId, jobService);
        //long intervalMillis = TimeUnit.MINUTES.toMillis(intervalMin);
        long intervalMillis = intervalMin;
        builder
                .setPersisted(true)
                //.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY) //TODO
                .setPeriodic(intervalMillis);
        return builder.build();
    }
}
