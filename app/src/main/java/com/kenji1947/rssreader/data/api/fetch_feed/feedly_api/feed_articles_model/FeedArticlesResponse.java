package com.kenji1947.rssreader.data.api.fetch_feed.feedly_api.feed_articles_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by chamber on 01.03.2018.
 */

public class FeedArticlesResponse {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("direction")
    @Expose
    private String direction;
    @SerializedName("updated")
    @Expose
    private long updated;
    @SerializedName("alternate")
    @Expose
    private List<AlternateFeed> alternate = null;
    @SerializedName("continuation")
    @Expose
    private String continuation;
    @SerializedName("items")
    @Expose
    private List<ArticleItem> items = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public List<AlternateFeed> getAlternate() {
        return alternate;
    }

    public void setAlternate(List<AlternateFeed> alternate) {
        this.alternate = alternate;
    }

    public String getContinuation() {
        return continuation;
    }

    public void setContinuation(String continuation) {
        this.continuation = continuation;
    }

    public List<ArticleItem> getItems() {
        return items;
    }

    public void setItems(List<ArticleItem> items) {
        this.items = items;
    }
}
