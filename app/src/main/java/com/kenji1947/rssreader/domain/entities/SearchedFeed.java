package com.kenji1947.rssreader.domain.entities;

/**
 * Created by chamber on 01.03.2018.
 */

public class SearchedFeed {
    public String title;
    public String imageUrl;
    public String pageLink;
    public String description;
    public String url;
    public boolean isSubscribed = false;

    public SearchedFeed(String title, String imageUrl, String pageLink, String description, String url) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.pageLink = pageLink;
        this.description = description;
        this.url = url;
    }
}
