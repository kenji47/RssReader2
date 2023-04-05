package com.kenji1947.rssreader.fakes;



/**
 * Created by chamber on 21.12.2017.
 */

public class AppSetup {

    public enum NETWORK_STATE {
        ONLINE,
        OFFLINE
    }
    public enum FEED_SERVICE_STATE {
        RETURN_SAME_FEED, //create new feed and get updates from api without new articles
        RETURN_NEW_ARTICLES, //create new feed and get updates from api with new articles
        ERROR,
        EMPTY
    }
    public static NETWORK_STATE networkState = NETWORK_STATE.ONLINE;
    public static FEED_SERVICE_STATE feedApiServiceState = FEED_SERVICE_STATE.RETURN_NEW_ARTICLES;
}
