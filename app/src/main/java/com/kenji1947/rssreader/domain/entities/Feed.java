package com.kenji1947.rssreader.domain.entities;

import java.util.List;

public  class Feed {
    public Feed() {
    }

    public long id;
    public String title;
    public String imageUrl;
    public String pageLink;
    public String description;
    public String url;

    public List<Article> articles;

    public Feed(String title, String imageUrl, String pageLink, String description, String url) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.pageLink = pageLink;
        this.description = description;
        this.url = url;
    }

    public Feed(final long id, final String title, final String imageUrl, final String pageLink, final String description, final String url) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.pageLink = pageLink;
        this.description = description;
        this.url = url;
    }

    @Override
    public String toString() {
        return "Feed{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", pageLink='" + pageLink + '\'' +
                ", description='" + description + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

}
