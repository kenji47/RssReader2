package com.kenji1947.rssreader.data.api.fetch_feed.feedly_api.feed_articles_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by chamber on 01.03.2018.
 */

public class ArticleItem {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("originId")
    @Expose
    private String originId;
    @SerializedName("fingerprint")
    @Expose
    private String fingerprint;

    @SerializedName("content")
    @Expose
    private Content content;

    @SerializedName("summary")
    @Expose
    private Content summary;

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("updated")
    @Expose
    private long updated;
    @SerializedName("published")
    @Expose
    private long published;
    @SerializedName("crawled")
    @Expose
    private long crawled;
    @SerializedName("alternate")
    @Expose
    private List<AlternateItem> alternate = null;
    @SerializedName("origin")
    @Expose
    private Origin origin;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("visual")
    @Expose
    private Visual visual;
    @SerializedName("canonicalUrl")
    @Expose
    private String canonicalUrl;
    @SerializedName("ampUrl")
    @Expose
    private String ampUrl;
    @SerializedName("cdnAmpUrl")
    @Expose
    private String cdnAmpUrl;
    @SerializedName("unread")
    @Expose
    private Boolean unread;
    @SerializedName("engagement")
    @Expose
    private Integer engagement;
    @SerializedName("engagementRate")
    @Expose
    private Double engagementRate;

    @SerializedName("webfeeds")
    @Expose
    private Webfeeds webfeeds;

    @SerializedName("memes")
    @Expose
    private List<Meme> memes = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginId() {
        return originId;
    }

    public void setOriginId(String originId) {
        this.originId = originId;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Content getSummary() {
        return summary;
    }

    public void setSummary(Content summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getUpdated() {
        return updated;
    }

    public void setUpdated(long updated) {
        this.updated = updated;
    }

    public long getPublished() {
        return published;
    }

    public void setPublished(long published) {
        this.published = published;
    }

    public long getCrawled() {
        return crawled;
    }

    public void setCrawled(long crawled) {
        this.crawled = crawled;
    }

    public List<AlternateItem> getAlternate() {
        return alternate;
    }

    public void setAlternate(List<AlternateItem> alternate) {
        this.alternate = alternate;
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Visual getVisual() {
        return visual;
    }

    public void setVisual(Visual visual) {
        this.visual = visual;
    }

    public String getCanonicalUrl() {
        return canonicalUrl;
    }

    public void setCanonicalUrl(String canonicalUrl) {
        this.canonicalUrl = canonicalUrl;
    }

    public String getAmpUrl() {
        return ampUrl;
    }

    public void setAmpUrl(String ampUrl) {
        this.ampUrl = ampUrl;
    }

    public String getCdnAmpUrl() {
        return cdnAmpUrl;
    }

    public void setCdnAmpUrl(String cdnAmpUrl) {
        this.cdnAmpUrl = cdnAmpUrl;
    }

    public Boolean getUnread() {
        return unread;
    }

    public void setUnread(Boolean unread) {
        this.unread = unread;
    }

    public Integer getEngagement() {
        return engagement;
    }

    public void setEngagement(Integer engagement) {
        this.engagement = engagement;
    }

    public Double getEngagementRate() {
        return engagementRate;
    }

    public void setEngagementRate(Double engagementRate) {
        this.engagementRate = engagementRate;
    }

    public Webfeeds getWebfeeds() {
        return webfeeds;
    }

    public void setWebfeeds(Webfeeds webfeeds) {
        this.webfeeds = webfeeds;
    }

    public List<Meme> getMemes() {
        return memes;
    }

    public void setMemes(List<Meme> memes) {
        this.memes = memes;
    }
}
