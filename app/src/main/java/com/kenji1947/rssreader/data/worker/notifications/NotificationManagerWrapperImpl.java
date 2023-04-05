package com.kenji1947.rssreader.data.worker.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.v4.app.NotificationManagerCompat;

import com.bumptech.glide.load.engine.Resource;
import com.kenji1947.rssreader.R;
import com.kenji1947.rssreader.data.worker.resource_manager.ResourceManager;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by chamber on 07.12.2017.
 */

public class NotificationManagerWrapperImpl implements NotificationManagerWrapper {

    private final NotificationManagerCompat notificationManagerCompat;
    private android.app.NotificationManager notificationManager;
    private Resources resources;
    private String channelIdFeedSync;


    public NotificationManagerWrapperImpl(NotificationManagerCompat notificationManagerCompat,
                                          android.app.NotificationManager notificationManager,
                                          Resources resources,
                                          String channelIdFeedSync) {
        this.notificationManagerCompat = notificationManagerCompat;
        this.notificationManager = notificationManager;
        this.resources = resources;
        this.channelIdFeedSync = channelIdFeedSync;

        registerChannels();
    }

    private void registerChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //TODO Создать новый канал со звуком, для завершения загрузки
            NotificationChannel notificationChannel = new NotificationChannel(
                            channelIdFeedSync,
                            resources.getString(R.string.notification_channel_feed_sync_name),
                            NotificationManager.IMPORTANCE_LOW
                    );
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    @Override
    public void showNotification(final int notificationId, final Notification notification) {
        notificationManagerCompat.notify(notificationId, notification);
    }

    @Override
    public void updateNotification(final int notificationId, final Notification notification) {
        notificationManagerCompat.notify(notificationId, notification);
    }

    @Override
    public void hideNotification(final int notificationId) {
        notificationManagerCompat.cancel(notificationId);
    }

    @Override
    public void hideNotifications() {
        notificationManagerCompat.cancelAll();
    }
}
