package com.kenji1947.rssreader.data;

import com.kenji1947.rssreader.R;
import com.kenji1947.rssreader.data.worker.error_handler.ErrorHandler;
import com.kenji1947.rssreader.data.worker.error_handler.ErrorHandlerImpl;
import com.kenji1947.rssreader.data.worker.resource_manager.ResourceManager;

import org.mockito.InOrder;

import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

/**
 * Created by chamber on 19.12.2017.
 */

public class TestUtils {
    public static String MSG_NO_NETWORK = "no_network";
    public static String MSG_FEED_ALREADY_SUBSCRIBED = "You are already subscribed to this feed";
    public static String MSG_UNKNOWN_ERROR = "unknown_error";
    public static String MSG_INCORRECT_URL = "incorrect_url";

    public static ErrorHandler initErrorHandler(ResourceManager resourceManager) {
        when(resourceManager.getString(R.string.message_no_network)).thenReturn(MSG_NO_NETWORK);
        when(resourceManager.getString(R.string.message_incorrect_feed_url)).thenReturn(MSG_INCORRECT_URL);
        when(resourceManager.getString(R.string.message_feed_already_subscribed)).thenReturn(MSG_FEED_ALREADY_SUBSCRIBED);
        when(resourceManager.getString(R.string.message_unknown)).thenReturn(MSG_UNKNOWN_ERROR);

        return new ErrorHandlerImpl(resourceManager);
    }


}
