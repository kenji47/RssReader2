package com.kenji1947.rssreader.domain.exceptions;

/**
 * Created by chamber on 18.12.2017.
 */

public class FeedAlreadySubscribedException extends RuntimeException {
    public FeedAlreadySubscribedException() {
        super("You already subscribed on this feed");
    }
}
