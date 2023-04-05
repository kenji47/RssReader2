package com.kenji1947.rssreader.data.worker.notifications;

import android.app.Notification;

/**
 * Created by chamber on 07.12.2017.
 */

public interface NotificationManagerWrapper {
    void showNotification(int notificationId, Notification notification);
    void updateNotification(int notificationId, Notification notification);
    void hideNotification(int notificationId);
    void hideNotifications();
}
