package com.kenji1947.rssreader.util;

import com.kenji1947.rssreader.domain.entities.Feed;

import java.util.List;

/**
 * Created by chamber on 21.12.2017.
 */

public interface DatabaseOperations {
    void clearAllDb();
    void addFeed(Feed feed);
    void addFeeds(List<Feed> feeds);
}
