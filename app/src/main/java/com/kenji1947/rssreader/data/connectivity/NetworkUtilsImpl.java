package com.kenji1947.rssreader.data.connectivity;

import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;

import io.reactivex.Single;
import timber.log.Timber;


public final class NetworkUtilsImpl implements NetworkUtils {

    private static final String EMPTY = "";
    private static final String PING_ADDRESS = "www.google.com";
    private final ConnectivityManagerWrapper connectivityManagerWrapper;

    public NetworkUtilsImpl(final ConnectivityManagerWrapper connectivityManagerWrapper) {
        this.connectivityManagerWrapper = connectivityManagerWrapper;
    }

    @Override
    public Single<Boolean> isConnectedToInternet() {
        Timber.d("isConnectedToInternet");
        //return Single.just(false);
        //return Single.fromCallable(() -> (isConnectedToNetwork() && canResolveAddress(PING_ADDRESS)));
        return Single.fromCallable(() -> isConnectedToNetwork());
    }

    @Override
    public Single<NetworkData> getActiveNetworkData() {
        Timber.d("getActiveNetworkData");
        return Single.fromCallable(connectivityManagerWrapper::getNetworkData);
    }

    private boolean canResolveAddress(final String url) {
        return pingAddress(url);
    }

    private boolean isConnectedToNetwork() {
        return connectivityManagerWrapper.isConnectedToNetwork();
    }

    private boolean pingAddress(final String url) {
        try {
            final InetAddress address = InetAddress.getByName(url);
            return address != null && !EMPTY.equals(address.getHostAddress());
        } catch (final UnknownHostException e) {
            return false;
        }
    }
}
