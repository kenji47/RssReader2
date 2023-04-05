package com.kenji1947.rssreader.data.api.fetch_feed.feedly_api.feed_metadata_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by chamber on 24.03.2018.
 */

public class FeedMetadata {
    @SerializedName("coverUrl")
    @Expose
    private String coverUrl;
    @SerializedName("iconUrl")
    @Expose
    private String iconUrl;
    @SerializedName("visualUrl")
    @Expose
    private String visualUrl;
    @SerializedName("feedId")
    @Expose
    private String feedId;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("subscribers")
    @Expose
    private Long subscribers;
    @SerializedName("updated")
    @Expose
    private Long updated;
    @SerializedName("velocity")
    @Expose
    private Double velocity;
    @SerializedName("website")
    @Expose
    private String website;
    @SerializedName("topics")
    @Expose
    private List<String> topics = null;
    @SerializedName("partial")
    @Expose
    private Boolean partial;
    @SerializedName("contentType")
    @Expose
    private String contentType;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("coverColor")
    @Expose
    private String coverColor;
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("wordmark")
    @Expose
    private String wordmark;
    @SerializedName("relatedLayout")
    @Expose
    private String relatedLayout;
    @SerializedName("relatedTarget")
    @Expose
    private String relatedTarget;
    @SerializedName("accentColor")
    @Expose
    private String accentColor;

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getVisualUrl() {
        return visualUrl;
    }

    public void setVisualUrl(String visualUrl) {
        this.visualUrl = visualUrl;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

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

    public Long getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Long subscribers) {
        this.subscribers = subscribers;
    }

    public Long getUpdated() {
        return updated;
    }

    public void setUpdated(Long updated) {
        this.updated = updated;
    }

    public Double getVelocity() {
        return velocity;
    }

    public void setVelocity(Double velocity) {
        this.velocity = velocity;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<String> getTopics() {
        return topics;
    }

    public void setTopics(List<String> topics) {
        this.topics = topics;
    }

    public Boolean getPartial() {
        return partial;
    }

    public void setPartial(Boolean partial) {
        this.partial = partial;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverColor() {
        return coverColor;
    }

    public void setCoverColor(String coverColor) {
        this.coverColor = coverColor;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getWordmark() {
        return wordmark;
    }

    public void setWordmark(String wordmark) {
        this.wordmark = wordmark;
    }

    public String getRelatedLayout() {
        return relatedLayout;
    }

    public void setRelatedLayout(String relatedLayout) {
        this.relatedLayout = relatedLayout;
    }

    public String getRelatedTarget() {
        return relatedTarget;
    }

    public void setRelatedTarget(String relatedTarget) {
        this.relatedTarget = relatedTarget;
    }

    public String getAccentColor() {
        return accentColor;
    }

    public void setAccentColor(String accentColor) {
        this.accentColor = accentColor;
    }
}
