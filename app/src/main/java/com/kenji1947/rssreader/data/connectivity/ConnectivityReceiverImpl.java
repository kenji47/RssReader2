package com.kenji1947.rssreader.data.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.kenji1947.rssreader.domain.util.RxSchedulersProvider;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.subjects.PublishSubject;

//TODO Разобрать BroadcastReceiver
public final class ConnectivityReceiverImpl extends BroadcastReceiver implements ConnectivityReceiver {

    private static final String TAG = ConnectivityReceiverImpl.class.getSimpleName();

    private static final String ACTION_CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";

    private final NetworkUtils networkUtils;
    private final RxSchedulersProvider schedulersProvider;
    private final PublishSubject<Boolean> subject;

    private boolean isConnected;

    public ConnectivityReceiverImpl(final Context context, final NetworkUtils networkUtils,
                                    final RxSchedulersProvider schedulersProvider) {
        this.networkUtils = networkUtils;
        this.schedulersProvider = schedulersProvider;
        final IntentFilter intentFilter = new IntentFilter(ACTION_CONNECTIVITY_CHANGE);//TODO
        context.registerReceiver(this, intentFilter);//TODO
        this.subject = PublishSubject.create();
    }

    @Override
    public Observable<Boolean> getConnectivityStatus() {
        return subject.subscribeOn(schedulersProvider.getIo())
                      .observeOn(schedulersProvider.getIo());
    }

    @Override
    public Single<Boolean> isConnected() {
        return networkUtils.isConnectedToInternet()
                .subscribeOn(schedulersProvider.getIo());
    }

    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (subject == null) {
            return;
        }

        checkConnectionStatus();
    }

    private void checkConnectionStatus() {
        networkUtils.isConnectedToInternet()
                    .subscribeOn(schedulersProvider.getIo())
                    .observeOn(schedulersProvider.getIo())
                    .subscribe(this::onNetworkStatus, this::onNetworkStatusError);
    }

    private void onNetworkStatus(final Boolean isConnected) {
        if (this.isConnected != isConnected) {
            this.isConnected = isConnected;
            subject.onNext(isConnected);
        }
    }

    private void onNetworkStatusError(final Throwable throwable) {
        // No-op
    }
}