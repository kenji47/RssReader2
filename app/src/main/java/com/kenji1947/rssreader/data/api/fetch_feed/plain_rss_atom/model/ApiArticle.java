package com.kenji1947.rssreader.data.api.fetch_feed.plain_rss_atom.model;

public final class ApiArticle {

    public final String title;
    public final String link;
    public final String imageLink;
    public final long publicationDate;

    public ApiArticle(final String title, final String link, String imageLink, final long publicationDate) {
        this.title = title;
        this.link = link;
        this.imageLink = imageLink;
        this.publicationDate = publicationDate;
    }
}
