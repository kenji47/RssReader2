package com.kenji1947.rssreader.data.util;

public class CurrentTimeProviderImpl implements CurrentTimeProvider {

    @Override
    public long getCurrentTime() {
        return System.currentTimeMillis();
    }
}
