package com.kenji1947.rssreader.data.worker.resource_manager;

import android.content.Context;

/**
 * Created by chamber on 19.12.2017.
 */

public class ResourceManagerImpl implements ResourceManager {
    private Context appContext;

    public ResourceManagerImpl(Context context) {
        this.appContext = context.getApplicationContext();
    }

    @Override
    public String getString(int resource) {
        return appContext.getString(resource);
    }

    @Override
    public String getStringFormat(int resource, Object... formatArgs) {
        return appContext.getString(resource, formatArgs);
    }
}
