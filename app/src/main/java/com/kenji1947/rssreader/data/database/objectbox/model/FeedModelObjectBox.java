package com.kenji1947.rssreader.data.database.objectbox.model;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;

/**
 * Created by kenji1947 on 09.11.2017.
 */
@Entity
public class FeedModelObjectBox {
    @Id
    long id;

    public String title;
    public String imageUrl;
    public String pageLink;
    public String description;
    public String url;

    public FeedModelObjectBox() {
    }

    public FeedModelObjectBox(String title, String imageUrl, String pageLink, String description, String url) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.pageLink = pageLink;
        this.description = description;
        this.url = url;
    }

    public FeedModelObjectBox(long id, String title, String imageUrl, String pageLink, String description, String url) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.pageLink = pageLink;
        this.description = description;
        this.url = url;
    }

    @Backlink
    ToMany<ArticleModelObjectBox> articles;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPageLink() {
        return pageLink;
    }

    public void setPageLink(String pageLink) {
        this.pageLink = pageLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ToMany<ArticleModelObjectBox> getArticles() {
        return articles;
    }

    public void setArticles(ToMany<ArticleModelObjectBox> articles) {
        this.articles = articles;
    }
}
