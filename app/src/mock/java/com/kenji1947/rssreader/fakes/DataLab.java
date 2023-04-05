package com.kenji1947.rssreader.fakes;

import com.kenji1947.rssreader.domain.entities.Article;
import com.kenji1947.rssreader.domain.entities.Feed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by chamber on 26.12.2017.
 */

public class DataLab {
    private static Map<String, Feed> feedMap = new LinkedHashMap<>();

    public static int DEFAULT_ARTICLES_NUMBER = 15;

    static long time = System.currentTimeMillis();

    public final static String URL_XXX = "Xxx.com";
    public final static String URL_YYY = "Yyy.com";
    public final static String URL_ZZZ = "Zzz.com";

    //Feed
    private static String feed_title = "feed_title";
    private static String feed_image_url = "feed_image_url";
    private static String feed_page_link = "feed_page_link";
    private static String feed_description = "feed_description";
    private static String feed_url = "feed_url";

    //Article
    private static String article_title = "article_title";
    private static String article_link = "article_link";


    public static List<Feed> getFeeds() {
        return new ArrayList<>(feedMap.values());
    }

    public static void clearAllData() {
        feedMap.clear();
        DEFAULT_ARTICLES_NUMBER = 15;
    }

    public static List<Feed> generateFeeds(int feedsCount, int articlesCount) {
        DEFAULT_ARTICLES_NUMBER = articlesCount;
        for (int i = 0; i < feedsCount; i++) {
            generateFeed(String.valueOf(i));
        }
        return getFeeds();
    }

    public static Feed generateFeed(String url) {
        Feed feed = feedMap.get(url);
        if (feed != null)
            return feed;

        feed = new Feed(
                feed_title + " " + url,
                feed_image_url + " " + url,
                feed_page_link + " " + url,
                feed_description + " " + url,
                 url
        );
        feed.articles = generateArticlesForFeed(DEFAULT_ARTICLES_NUMBER, feed.url);
        feedMap.put(feed.url, feed);
        return feed;
    }

    private static List<Article> generateArticlesForFeed(int articlesCount, String feedUrl) {
        List<Article> articles = new ArrayList<>();
        for (int i = 0; i < articlesCount; i++) {
            Article article = new Article(
                    article_title + i + " " + feedUrl,
                    article_link + i + " " + feedUrl,
                    "",
                    "",
                     time++,
                    true,
                    false
            );
            articles.add(article);
        }
        sortArticlesDesc(articles);
        return articles;
    }

    public static Feed generateFeedWithNewArticles(String url, int newArticlesCount) {
        Feed feed = feedMap.get(url);
        if (feed == null)
            feed = generateFeed(url);

        feed.articles.addAll(generateNewArticles(url, newArticlesCount));
        sortArticlesDesc(feed.articles);
        return feed;
    }

    private static List<Article> generateNewArticles(String url, int newArticlesCount) {
        List<Article> articles = new ArrayList<>();
        //long time = System.currentTimeMillis();
        for (int i = 0; i < newArticlesCount; i++) {
            String uuid = UUID.randomUUID().toString().subSequence(0, 4).toString();

            Article article = new Article(
                    article_title + "_" + uuid + " " + url,
                    article_link + "_" + uuid + " " + url,
                     "",
                     "",
                     time++,
                    true,
                    false
            );
            articles.add(article);
        }
        return articles;
    }

    private static void sortArticlesDesc(List<Article> list) {
        Collections.sort(list, new Comparator<Article>() {
            @Override
            public int compare(Article a1, Article a2) {
                return (int) (a2.publicationDate - a1.publicationDate);
            }
        });
    }
}
