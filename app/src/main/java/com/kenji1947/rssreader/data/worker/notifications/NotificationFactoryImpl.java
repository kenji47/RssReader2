package com.kenji1947.rssreader.data.worker.notifications;

import android.app.*;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;

import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.res.ResourcesCompat;


import com.kenji1947.rssreader.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by chamber on 07.12.2017.
 */

public class NotificationFactoryImpl implements NotificationFactory {

    private final Context context;
    private final Resources resources;
    private String channelIdFeedSync;

    public NotificationFactoryImpl(final Context context, final Resources resources, String channelIdFeedSync ) {
        this.context = context;
        this.resources = resources;
        this.channelIdFeedSync = channelIdFeedSync;
    }



    //TODO Используется в service feed sync. Удалить
    @Override
    public Notification createNewArticlesNotificationNew(String title, String content, PendingIntent contentIntent) {
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        return notificationBuilder.setAutoCancel(true)
                .setColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, null))
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(contentIntent)
                .build();
    }

    @Override
    public Notification createFeedSyncNotificationComplete(String title, String content,
                                                           PendingIntent contentIntent) {
        final NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelIdFeedSync);

        return notificationBuilder
                .setAutoCancel(true)
                .setColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, null))
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(contentIntent)
                .build();
    }

    @Override
    public Notification createFeedSyncNotificationProgress(String title, String content,
                                                           int total, int progress,
                                                           PendingIntent contentIntent) {
        final NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelIdFeedSync);

        return notificationBuilder
                .setAutoCancel(true)
                .setColor(ResourcesCompat.getColor(resources, R.color.colorPrimary, null))
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(content)
                .setContentIntent(contentIntent)
                .setProgress(total, progress, false)
                .build();
    }
}
