package com.kenji1947.rssreader.data.api.fetch_feed.feedly_api.feed_articles_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by chamber on 01.03.2018.
 */

public class Webfeeds {
    @SerializedName("logo")
    @Expose
    private String logo;
    @SerializedName("relatedLayout")
    @Expose
    private String relatedLayout;
    @SerializedName("wordmark")
    @Expose
    private String wordmark;
    @SerializedName("relatedTarget")
    @Expose
    private String relatedTarget;
    @SerializedName("accentColor")
    @Expose
    private String accentColor;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getRelatedLayout() {
        return relatedLayout;
    }

    public void setRelatedLayout(String relatedLayout) {
        this.relatedLayout = relatedLayout;
    }

    public String getWordmark() {
        return wordmark;
    }

    public void setWordmark(String wordmark) {
        this.wordmark = wordmark;
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
