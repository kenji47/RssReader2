package com.kenji1947.rssreader.util;

import com.kenji1947.rssreader.App;
import com.kenji1947.rssreader.data.database.objectbox.converter.FeedObjectBoxConverter;
import com.kenji1947.rssreader.data.database.objectbox.converter.FeedObjectBoxConverterImpl;
import com.kenji1947.rssreader.data.database.objectbox.model.ArticleModelObjectBox;
import com.kenji1947.rssreader.data.database.objectbox.model.FeedModelObjectBox;
import com.kenji1947.rssreader.domain.entities.Feed;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * Created by chamber on 20.12.2017.
 */

public class DatabaseOperationsImpl implements DatabaseOperations {
    private BoxStore boxStore;
    private Box<FeedModelObjectBox> feedBox;
    private Box<ArticleModelObjectBox> articleBox;
    private FeedObjectBoxConverter converter;

    public DatabaseOperationsImpl(App app) {
        this.boxStore = app.getAppComponent().provideBoxStore();
        this.feedBox = boxStore.boxFor(FeedModelObjectBox.class);
        this.articleBox = boxStore.boxFor(ArticleModelObjectBox.class);
        this.converter = new FeedObjectBoxConverterImpl();
    }

    @Override
    public void clearAllDb() {
        feedBox.removeAll();
        articleBox.removeAll();
    }

    @Override
    public void addFeed(Feed feed) {
        FeedModelObjectBox feedDb = converter.domainToDb(feed);
        feedDb.getArticles().addAll(FeedObjectBoxConverter.ConverterHelper
                .convertDomainArticleListToDb(feed.articles, converter));
        feedBox.put(feedDb);
    }

    @Override
    public void addFeeds(List<Feed> feeds) {
        List<FeedModelObjectBox> feedModelObjectBoxes = new ArrayList<>();
        for (Feed feed : feeds) {
            FeedModelObjectBox feedDb = converter.domainToDb(feed);
            feedDb.getArticles().addAll(FeedObjectBoxConverter.ConverterHelper
                    .convertDomainArticleListToDb(feed.articles, converter));
            feedModelObjectBoxes.add(feedDb);
        }
        feedBox.put(feedModelObjectBoxes);
    }
}
