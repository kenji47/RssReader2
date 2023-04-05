package com.kenji1947.rssreader.data.worker.resource_manager;

/**
 * Created by chamber on 19.12.2017.
 */

public interface ResourceManager {
    String getString(int resource);
    String getStringFormat(int resource, Object... formatArgs);
}
