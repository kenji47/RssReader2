package com.kenji1947.rssreader.data.worker.error_handler;

import com.kenji1947.rssreader.R;
import com.kenji1947.rssreader.data.worker.resource_manager.ResourceManager;
import com.kenji1947.rssreader.domain.exceptions.FeedAlreadySubscribedException;
import com.kenji1947.rssreader.domain.exceptions.NoNetworkException;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * Created by chamber on 19.12.2017.
 */
//TODO Проблема данного решения
    //разные экраны могут показывать разные сообщения для одного типа ошибок
public class ErrorHandlerImpl implements ErrorHandler{
    private String errorOnHandling = "Error handling error";
    private ResourceManager resourceManager;

    public ErrorHandlerImpl(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    //Create feed screen
    @Override
    public void handleError(Throwable throwable, Consumer<String> consumer) {
        try {
            if (throwable instanceof NoNetworkException)
                consumer.accept(resourceManager.getString(R.string.message_no_network));
            else if (throwable instanceof FeedAlreadySubscribedException)
                consumer.accept(resourceManager.getString(R.string.message_feed_already_subscribed));
            else
                consumer.accept(resourceManager.getString(R.string.message_incorrect_feed_url)); //TODO Не нужно показывать по дефолту
        } catch (Exception e) {
            Timber.e(errorOnHandling + " " + throwable);
        }
        Timber.d("handleError " + throwable.getMessage());
    }
    @Override
    public void handleErrorScreenFeedList(Throwable throwable, Consumer<String> consumer) {
        try {
            if (throwable instanceof NoNetworkException)
                consumer.accept(resourceManager.getString(R.string.message_no_network));
            else
                consumer.accept(resourceManager.getString(R.string.message_unknown));
        } catch (Exception e) {
            Timber.e(errorOnHandling + " " + throwable);
        }
    }

    @Override
    public void handleErrorScreenNewFeed(Throwable throwable, Consumer<String> consumer) {
        try {
            if (throwable instanceof NoNetworkException)
                consumer.accept(resourceManager.getString(R.string.message_no_network));
            else if (throwable instanceof FeedAlreadySubscribedException)
                consumer.accept(resourceManager.getString(R.string.message_feed_already_subscribed));
            else
                consumer.accept(resourceManager.getString(R.string.message_incorrect_feed_url));
        } catch (Exception e) {
            Timber.e(errorOnHandling + " " + throwable);
        }
        Timber.d("handleError " + throwable);
    }
}
