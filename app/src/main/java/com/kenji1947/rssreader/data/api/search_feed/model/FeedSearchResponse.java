package com.kenji1947.rssreader.data.api.search_feed.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FeedSearchResponse {
    @SerializedName("results")
    @Expose
    private List<FeedSearchItem> results;

    public List<FeedSearchItem> getResults() {
        return results;
    }

    public void setResults(List<FeedSearchItem> results) {
        this.results = results;
    }
}
