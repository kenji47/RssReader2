package com.kenji1947.rssreader.data.api.search_feed.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by chamber on 26.02.2018.
 */

public class FeedSearchItem {
    @SerializedName("feedId")
    @Expose
    private String feedId;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("website")
    @Expose
    private String website;

    @SerializedName("iconUrl")
    @Expose
    private String iconUrl;

    @SerializedName("visualUrl")
    @Expose
    private String visualUrl;

    @SerializedName("deliciousTags")
    @Expose
    private List<String> deliciousTags;

    //---------------

    @SerializedName("subscribers")
    @Expose
    private Integer subscribers;

    @SerializedName("velocity")
    @Expose
    private Double velocity;

    @SerializedName("lastUpdated")
    @Expose
    private long lastUpdated;



    @SerializedName("coverage")
    @Expose
    private Double coverage;

    @SerializedName("coverageScore")
    @Expose
    private Double coverageScore;

    @SerializedName("estimatedEngagement")
    @Expose
    private Integer estimatedEngagement;

    @SerializedName("scheme")
    @Expose
    private String scheme;

    @SerializedName("language")
    @Expose
    private String language;

    @SerializedName("contentType")
    @Expose
    private String contentType;

    @SerializedName("partial")
    @Expose
    private Boolean partial;


    @SerializedName("art")
    @Expose
    private Double art;

    @SerializedName("coverUrl")
    @Expose
    private String coverUrl;

    @SerializedName("coverColor")
    @Expose
    private String coverColor;

    public List<String> getDeliciousTags() {
        return deliciousTags;
    }

    public void setDeliciousTags(List<String> deliciousTags) {
        this.deliciousTags = deliciousTags;
    }

    public String getFeedId() {
        return feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Integer subscribers) {
        this.subscribers = subscribers;
    }

    public Double getVelocity() {
        return velocity;
    }

    public void setVelocity(Double velocity) {
        this.velocity = velocity;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Integer lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Double getCoverage() {
        return coverage;
    }

    public void setCoverage(Double coverage) {
        this.coverage = coverage;
    }

    public Double getCoverageScore() {
        return coverageScore;
    }

    public void setCoverageScore(Double coverageScore) {
        this.coverageScore = coverageScore;
    }

    public Integer getEstimatedEngagement() {
        return estimatedEngagement;
    }

    public void setEstimatedEngagement(Integer estimatedEngagement) {
        this.estimatedEngagement = estimatedEngagement;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Boolean getPartial() {
        return partial;
    }

    public void setPartial(Boolean partial) {
        this.partial = partial;
    }

    public String getVisualUrl() {
        return visualUrl;
    }

    public void setVisualUrl(String visualUrl) {
        this.visualUrl = visualUrl;
    }

    public Double getArt() {
        return art;
    }

    public void setArt(Double art) {
        this.art = art;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getCoverColor() {
        return coverColor;
    }

    public void setCoverColor(String coverColor) {
        this.coverColor = coverColor;
    }

}
