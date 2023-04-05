package com.kenji1947.rssreader.data.api.fetch_feed.feedly_api;

import com.kenji1947.rssreader.data.api.ApiRetrofit;
import com.kenji1947.rssreader.data.api.fetch_feed.FetchFeedApiService;
import com.kenji1947.rssreader.data.api.fetch_feed.feedly_api.feed_articles_model.ArticleItem;
import com.kenji1947.rssreader.data.api.fetch_feed.feedly_api.feed_articles_model.FeedArticlesResponse;
import com.kenji1947.rssreader.data.api.fetch_feed.feedly_api.feed_metadata_model.FeedMetadata;
import com.kenji1947.rssreader.domain.entities.Article;
import com.kenji1947.rssreader.domain.entities.Feed;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.reactivex.Single;
import retrofit2.http.HTTP;
import timber.log.Timber;

/**
 * Created by chamber on 24.02.2018.
 */

public class FetchFeedApiServiceFeedly implements FetchFeedApiService {
    private ApiRetrofit apiRetrofit;

    public FetchFeedApiServiceFeedly(ApiRetrofit apiRetrofit) {
        this.apiRetrofit = apiRetrofit;
    }


    @Override
    public Single<Feed> fetchFeed(String feedUrl) throws IOException {
        Timber.d("fetchFeed " +feedUrl + "  " + URLEncoder.encode(feedUrl, "UTF-8"));
        return apiRetrofit.geFeedMetadata(feedUrl)
                .map(feedMetadata -> parseFeedMetadataToDomain(feedMetadata))
                .flatMap(feed -> apiRetrofit.getFeedArticles(feedUrl)
                        .map(feedArticlesResponse -> {
                            feed.articles = parseApiArticlesToDomain(feedArticlesResponse);
                            return feed;
                        }));
    }

    private Feed parseFeedMetadataToDomain(FeedMetadata feedMetadata) {
        //TODO Check fucking nulls
        return new Feed(
                feedMetadata.getTitle(),
                feedMetadata.getIconUrl(), //NULL!
                feedMetadata.getWebsite(),
                feedMetadata.getDescription(),
                feedMetadata.getFeedId()
        );
    }

    private List<Article> parseApiArticlesToDomain(FeedArticlesResponse feedArticlesResponse) {
        List<Article> articles = new ArrayList<>();

        for (ArticleItem articleItem : feedArticlesResponse.getItems()) {
            articles.add(new Article(
                    articleItem.getTitle(),
                    getUrl(articleItem),
                    getContent(articleItem),
                    getImageLink(articleItem),
                    articleItem.getPublished()
            ));
        }
        return articles;
    }

//    @Override
//    public Single<Feed> fetchFeed(String feedUrl) throws IOException {
//        return apiRetrofit.getFeedArticles(feedUrl)
//                .map(feedArticlesResponse -> {
//                    Feed feed = new Feed();
//
//                    List<Article> articles = new ArrayList<>();
//
//                    for (ArticleItem articleItem : feedArticlesResponse.getItems()) {
//                        articles.add(new Article(
//                                articleItem.getTitle(),
//                                articleItem.getCanonicalUrl(),
//                                "",
//                                articleItem.getVisual().getUrl(),
//                                articleItem.getPublished()
//                        ));
//                    }
//
//                    feed.articles = articles;
//                    return feed;
//                });
//    }

    @Override
    public Single<List<Article>> fetchArticles(String feedUrl) {
//        List<Article> articles = new ArrayList<>();
//        String uuid = UUID.randomUUID().toString();
//        articles.add(new Article("title" + uuid, "link" + uuid, "content" + uuid, "imageLink" + uuid, 911));
//        return Single.just(articles);

        return apiRetrofit.getFeedArticles(feedUrl)
                .map(feedArticlesResponse -> parseApiArticlesToDomain(feedArticlesResponse));
    }

    //TODO Вынести в парсер
    private String getContent(ArticleItem articleItem) {
        Timber.d("getContent " + articleItem.getTitle() + " " + articleItem.getId());
        if (articleItem.getContent() != null)
            return articleItem.getContent().getContent();

        if (articleItem.getSummary() != null) {
            String content = articleItem.getSummary().getContent();
            if (getImageLink(articleItem) != null) {
                String imageUrl = getImageLink(articleItem);
                content =  "<div><img src=\"" + imageUrl + "\" /></div>" + " " + content;
            }
            return content;
        }
        return "";
    }

    private String getImageLink(ArticleItem articleItem) {
        if (articleItem.getVisual() != null) {
            return articleItem.getVisual().getUrl();
        }
        return null;
    }

    //TODO Доработать. xkcd дает null
    private String getUrl(ArticleItem articleItem) {
        if (articleItem.getCanonicalUrl() != null)
            return articleItem.getCanonicalUrl();
        if (articleItem.getAlternate() != null && !articleItem.getAlternate().isEmpty()) {
            return articleItem.getAlternate().get(0).getHref();
        }
        Timber.e("Article page link == null. Article " + articleItem.getTitle() );
        return null;
    }
}
