package com.kenji1947.rssreader.di.application.modules;

import android.content.Context;

import com.kenji1947.rssreader.di.Injector;
import com.kenji1947.rssreader.data.connectivity.ConnectivityManagerWrapper;
import com.kenji1947.rssreader.data.connectivity.ConnectivityManagerWrapperImpl;
import com.kenji1947.rssreader.data.connectivity.ConnectivityReceiver;
import com.kenji1947.rssreader.data.connectivity.ConnectivityReceiverImpl;
import com.kenji1947.rssreader.data.connectivity.NetworkUtils;
import com.kenji1947.rssreader.domain.util.RxSchedulersProvider;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by chamber on 10.12.2017.
 */

@Module
public class ConnectivityModule {

    @Provides
    @Singleton
    ConnectivityReceiver provideConnectivityReceiver(Context context,
                                                     NetworkUtils networkUtils,
                                                     RxSchedulersProvider schedulersProvider) {
        return new ConnectivityReceiverImpl(context, networkUtils, schedulersProvider);
    }

    @Provides
    @Singleton
    NetworkUtils provideNetworkUtils(ConnectivityManagerWrapper wrapper) {
        return Injector.provideNetworkUtils(wrapper);
    }

//    @Provides
//    @Singleton
//    NetworkUtils provideNetworkUtils(ConnectivityManagerWrapper wrapper) {
//        return new NetworkUtilsImpl(wrapper);
//    }

    @Provides
    @Singleton
    ConnectivityManagerWrapper provideConnectivityManagerWrapper(Context context) {
        return new ConnectivityManagerWrapperImpl(context);
    }

    public interface Exposes {
        ConnectivityReceiver provideConnectivityReceiver();
    }
}
