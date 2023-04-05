package com.kenji1947.rssreader.di.application.modules;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.NotificationManagerCompat;

import com.kenji1947.rssreader.data.worker.notifications.NotificationFactory;
import com.kenji1947.rssreader.data.worker.notifications.NotificationFactoryImpl;
import com.kenji1947.rssreader.data.worker.notifications.NotificationManagerWrapper;
import com.kenji1947.rssreader.data.worker.notifications.NotificationManagerWrapperImpl;
import com.kenji1947.rssreader.presentation.MainActivity;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by chamber on 17.03.2018.
 */

//TODO Rename module to NotificationsModule
@Module
public class NotificationManagerModule {
    private static final String CHANNEL_ID_FEED_SYNC = "CHANNEL_ID_FEED_SYNC";

    @Provides
    @Singleton
    @Named("FEED_SYNC_NOTIFICATION_ID")
    int provideFeedSyncNotificationId() {
        return 194747;
    }


    @Provides
    @Singleton
    NotificationFactory provideNotificationFactory(Context context, final Resources resources) {
        return new NotificationFactoryImpl(context, resources, CHANNEL_ID_FEED_SYNC);
    }


    @Provides
    @Singleton
    NotificationManagerWrapper provideNotificationManagerWrapper(NotificationManagerCompat notificationManagerCompat,
                                                          NotificationManager notificationManager,
                                                          Resources resources) {
        return new NotificationManagerWrapperImpl(notificationManagerCompat, notificationManager,
                resources, CHANNEL_ID_FEED_SYNC);
    }

    @Provides
    @Singleton
    NotificationManagerCompat provideNotificationManagerCompat(Context context) {
        return NotificationManagerCompat.from(context);
    }

    @Provides
    @Singleton
    NotificationManager provideNotificationManager(Context context) {
        return (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
    }


    @Provides
    @Singleton
    @Named("FeedUpdateNotificationPendingIntent")
    PendingIntent provideFeedUpdateNotificationPendingIntent(Context context) {
        final Intent targetActivityIntent = new Intent(context, MainActivity.class); //TODO Заменить
        return PendingIntent.getActivity(context,
                0, targetActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public interface Exposes {
        NotificationManagerWrapper provideNotificationManagerWrapper();
        NotificationFactory provideNotificationFactory();
        @Named("FEED_SYNC_NOTIFICATION_ID") int provideFeedSyncNotificationId(); //TODO Так можно делать?
    }
}
