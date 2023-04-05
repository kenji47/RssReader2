package com.kenji1947.rssreader;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.kenji1947.rssreader.di.application.AppComponent;
import com.kenji1947.rssreader.di.application.DaggerAppComponent;
import com.kenji1947.rssreader.di.application.modules.ApiModule;
import com.kenji1947.rssreader.di.application.modules.AppModule;
import com.kenji1947.rssreader.di.application.modules.DatabaseModule;
import com.kenji1947.rssreader.di.application.modules.FeedSyncWorkerModule;
import com.kenji1947.rssreader.di.application.modules.NotificationManagerModule;
import com.kenji1947.rssreader.di.application.modules.OkHttpInterceptorsModule;
import com.kenji1947.rssreader.di.application.modules.RepositoryModule;
import com.kenji1947.rssreader.di.application.modules.RouterModule;
import com.kenji1947.rssreader.di.application.modules.FeedSyncSchedulerModule;
import com.kenji1947.rssreader.di.application.modules.RxSchedulersModule;
import com.kenji1947.rssreader.di.application.modules.UtilsModule;
import com.squareup.leakcanary.LeakCanary;

import io.reactivex.plugins.RxJavaPlugins;
import timber.log.Timber;

import static android.util.Log.ERROR;

/**
 * Created by kenji1947 on 11.11.2017.
 */

public class App extends Application {
    public static App INSTANCE;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true); //TODO Do i need this?

        initAppComponent(this);
        initTimber();
        //initLeakCanary();
        initRxJava();

        Timber.d("onCreate");
    }

    private void initRxJava() {
        //Android application should set a no-op handler
        //https://github.com/ReactiveX/RxJava/wiki/What's-different-in-2.0#error-handling
        RxJavaPlugins.setErrorHandler(e -> { });
    }

    private void initLeakCanary() {
        LeakCanary.install(this);
    }

    private void initAppComponent(App app) {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(app))
                .repositoryModule(new RepositoryModule())
                .databaseModule(new DatabaseModule())
                .rxSchedulersModule(new RxSchedulersModule())
                .routerModule(new RouterModule())
                .notificationManagerModule(new NotificationManagerModule())
                .feedSyncSchedulerModule(new FeedSyncSchedulerModule())
                .feedSyncWorkerModule(new FeedSyncWorkerModule())
                .apiModule(new ApiModule("https://cloud.feedly.com"))
                .okHttpInterceptorsModule(new OkHttpInterceptorsModule())
                .utilsModule(new UtilsModule())
                .build();

    }
    private void initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new Timber.Tree() {
                @Override
                protected void log(int priority, String tag, String message, Throwable t) {
                    if (priority == ERROR)
                        Log.e("ERROR", message + " " + t);
                        //YourCrashLibrary.log(priority, tag, message);
                }
            });
        }
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}
