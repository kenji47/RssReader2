package com.kenji1947.rssreader.util;

import com.kenji1947.rssreader.domain.entities.Article;
import com.kenji1947.rssreader.domain.entities.Feed;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chamber on 20.12.2017.
 */

//TODO delete
public class UiTestData {

    public static String feed_title = "feed_title";
    public static String feed_image_url = "feed_image_url";
    public static String feed_page_link = "feed_page_link";
    public static String feed_description = "feed_description";
    public static String feed_url = "feed_url";

    public static String article_title = "article_title";
    public static String article_link = "article_link";
    public static long publicationDate = 1000L;

    //---

    public static List<Feed> generateFeeds(int feedsCount, int articlesCount) {
        List<Feed> feeds = new ArrayList<>();
        for (int i = 0; i < feedsCount; i++) {
            Feed feed = new Feed(
                    feed_title + i,
                    feed_image_url + i,
                    feed_page_link + i,
                    feed_description + i,
                    feed_url + i
            );
            feed.articles = generateArticles(articlesCount, feed.url);
            feeds.add(feed);
        }
        return feeds;
    }

    public static List<Article> generateArticles(int articlesCount, String feedUrl) {
        List<Article> articles = new ArrayList<>();
        for (int i = 0; i < articlesCount; i++) {
            Article article = new Article(
                    article_title + i + " " + feedUrl,
                    article_link + i + " " + feedUrl,
                    publicationDate + i,
                    true,
                    false
            );
            articles.add(article);
        }
        return articles;
    }

//    public static List<FeedModelObjectBox> generateObjectBoxFeeds(int feedsCount, int articlesCount) {
//        List<FeedModelObjectBox> feeds = new ArrayList<>();
//
//        for (int i = 0; i < feedsCount; i++) {
//            FeedModelObjectBox feed = new FeedModelObjectBox(
//                    feed_title + i,
//                    feed_image_url + i,
//                    feed_page_link + i,
//                    feed_description + i,
//                    feed_url + i
//            );
//            feed.getArticles().addAll(generateObjectBoxArticles(articlesCount, feed.url));
//            feeds.add(feed);
//        }
//        return feeds;
//    }
//
//    public static FeedModelObjectBox generateObjectBoxFeedWithArticles(int articlesCount) {
//        FeedModelObjectBox feed = new FeedModelObjectBox(
//                feed_title,
//                feed_image_url,
//                feed_page_link,
//                feed_description,
//                feed_url
//        );
//        feed.getArticles().addAll(generateObjectBoxArticles(articlesCount, feed.url));
//
//        return feed;
//    }
//
//    public static List<ArticleModelObjectBox> generateObjectBoxArticles(int articlesCount, String feedUrl) {
//        List<ArticleModelObjectBox> articles = new ArrayList<>();
//        for (int i = 0; i < articlesCount; i++) {
//            ArticleModelObjectBox article = new ArticleModelObjectBox(
//                    article_title + i + " " + feedUrl,
//                    article_link + i + " " + feedUrl,
//                    publicationDate + i
//            );
//            articles.add(article);
//        }
//        return articles;
//    }
}
