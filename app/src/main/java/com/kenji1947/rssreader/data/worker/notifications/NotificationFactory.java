package com.kenji1947.rssreader.data.worker.notifications;

import android.app.Notification;
import android.app.PendingIntent;

/**
 * Created by chamber on 07.12.2017.
 */

public interface NotificationFactory {
    //TODO Разные сервисы используют разные методы
    Notification createNewArticlesNotificationNew(String title, String content, PendingIntent contentIntent);

    Notification createFeedSyncNotificationComplete(String title, String content, PendingIntent contentIntent);

    Notification createFeedSyncNotificationProgress(String title, String content, int total, int progress, PendingIntent contentIntent);
}
