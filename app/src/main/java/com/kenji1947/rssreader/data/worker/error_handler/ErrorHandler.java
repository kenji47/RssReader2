package com.kenji1947.rssreader.data.worker.error_handler;

import io.reactivex.functions.Consumer;

/**
 * Created by chamber on 19.12.2017.
 */

public interface ErrorHandler {
    public void handleError(Throwable throwable, Consumer<String> consumer);

    void handleErrorScreenFeedList(Throwable throwable, Consumer<String> consumer);

    void handleErrorScreenNewFeed(Throwable throwable, Consumer<String> consumer);
}
