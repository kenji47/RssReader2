package com.kenji1947.rssreader.fakes;

import com.kenji1947.rssreader.data.connectivity.NetworkData;
import com.kenji1947.rssreader.data.connectivity.NetworkUtils;

import io.reactivex.Single;
import timber.log.Timber;

/**
 * Created by chamber on 07.12.2017.
 */

public class NetworkUtilsFake implements NetworkUtils {

    @Override
    public Single<Boolean> isConnectedToInternet() {
        switch (AppSetup.networkState) {
            case ONLINE: return Single.just(true);
            case OFFLINE: return Single.just(false);
        }
        return Single.just(true);
    }

    @Override
    public Single<NetworkData> getActiveNetworkData() {
        Timber.d("getActiveNetworkData");
        return null;
    }
}
