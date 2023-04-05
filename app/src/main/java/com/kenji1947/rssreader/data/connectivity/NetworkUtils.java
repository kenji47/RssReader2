package com.kenji1947.rssreader.data.connectivity;


import io.reactivex.Single;

public interface NetworkUtils {

    Single<Boolean> isConnectedToInternet();

    Single<NetworkData> getActiveNetworkData();
}