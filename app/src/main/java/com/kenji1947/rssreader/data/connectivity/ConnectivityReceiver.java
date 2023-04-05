package com.kenji1947.rssreader.data.connectivity;


import io.reactivex.Observable;
import io.reactivex.Single;

public interface ConnectivityReceiver {
    Observable<Boolean> getConnectivityStatus();
    Single<Boolean> isConnected();
}
