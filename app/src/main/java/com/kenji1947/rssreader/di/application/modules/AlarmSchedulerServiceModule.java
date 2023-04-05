package com.kenji1947.rssreader.di.application.modules;

import dagger.Module;

/**
 * Created by chamber on 10.12.2017.
 */

//TODO Delete
@Module
public class AlarmSchedulerServiceModule {
//
//    @Provides
//    @Singleton
//    FeedSyncScheduler provideFeedUpdateScheduler(
//            PreferenceManager preferenceManager,
//            AlarmManager alarmManager,
//            @Named("FeedUpdateServicePendingIntent") PendingIntent servicePendingIntent) {
//        return new FeedUpdateSchedulerAlarmManager(preferenceManager, alarmManager, servicePendingIntent, 5000);
//    }
//
//    @Provides
//    @Singleton
//    AlarmManager provideAlarmManager(Context context) {
//        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//    }
//
//    @Provides
//    @Singleton
//    NotificationManagerWrapper provideNotificationManager(NotificationManagerCompat notificationManagerCompat) {
//        return new NotificationManagerWrapperImpl(notificationManagerCompat, notificationManager);
//    }
//
//    @Provides
//    @Singleton
//    NotificationManagerCompat provideNotificationManagerCompat(Context context) {
//        return NotificationManagerCompat.from(context);
//    }
//
//    //TODO Вынести в общее
//    @Provides
//    @Singleton
//    @Named("FeedUpdateNotificationPendingIntent")
//    PendingIntent provideFeedUpdateNotificationPendingIntent(Context context) {
//        final Intent targetActivityIntent = new Intent(context, MainActivity.class); //TODO Заменить
//        return PendingIntent.getActivity(context,
//                0, targetActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//    }
//
//    @Provides
//    @Singleton
//    @Named("FeedUpdateServicePendingIntent")
//    PendingIntent provideFeedUpdateServicePendingIntent(Context context) {
//        Intent intent = new Intent(context, FeedUpdateSimpleService.class);
//        return PendingIntent.getService(context, 0, intent, 0);
//    }
//
//    @Provides
//    @Singleton
//    NotificationFactory provideNotificationFactory(Context context, final Resources resources) {
//        return new NotificationFactoryImpl(context, resources);
//    }
//
////    @Provides
////    @Singleton
////    FeedUpdateSimpleService provideFeedUpdateSimpleService() {
////        return new FeedUpdateSimpleService();
////    }
//
//
//
//    public interface Exposes {
//        FeedSyncScheduler provideFeedUpdateScheduler();
//        NotificationManagerWrapper provideNotificationManager();
//        NotificationFactory provideNotificationFactory();
//    }
}
