package com.kenji1947.rssreader.ui.converter;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.kenji1947.rssreader.App;
import com.kenji1947.rssreader.data.api.fetch_feed.plain_rss_atom.model.ApiArticle;
import com.kenji1947.rssreader.data.api.fetch_feed.plain_rss_atom.model.ApiFeed;
import com.kenji1947.rssreader.data.database.objectbox.converter.FeedObjectBoxConverter;
import com.kenji1947.rssreader.data.database.objectbox.model.ArticleModelObjectBox;
import com.kenji1947.rssreader.data.database.objectbox.model.FeedModelObjectBox;
import com.kenji1947.rssreader.domain.entities.Article;
import com.kenji1947.rssreader.domain.entities.Feed;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

/**
 * Created by chamber on 19.12.2017.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
public class FeedConverterTest {
    private static BoxStore boxStore;
    private static Box<FeedModelObjectBox> feedBox;
    private static Box<ArticleModelObjectBox> articleBox;
    private FeedObjectBoxConverter converter;

    private static long feedId;

    @BeforeClass
    public static void beforeClass() {
        boxStore = App.INSTANCE.getAppComponent().provideBoxStore();
        feedBox = boxStore.boxFor(FeedModelObjectBox.class);
        articleBox = boxStore.boxFor(ArticleModelObjectBox.class);

        FeedModelObjectBox feedDbModel = new FeedModelObjectBox(
                ConverterTestData.TEST_STRING_TITLE_1,
                ConverterTestData.TEST_IMAGE_URL,
                ConverterTestData.TEST_BASIC_URL_STRING,
                ConverterTestData.TEST_DESCRIPTION_STRING,
                ConverterTestData.TEST_COMPLEX_URL_STRING_1
        );

        List<ArticleModelObjectBox> articlesList = new ArrayList<>();
        for (int i  = 0; i < 2; i++) {
            ArticleModelObjectBox article = new ArticleModelObjectBox(
                    ConverterTestData.TEST_STRING_TITLE_1,
                    ConverterTestData.TEST_COMPLEX_URL_STRING_1,
                    ConverterTestData.TEST_LONG,
                    false,
                    true);
            articlesList.add(article);
        }
        feedDbModel.getArticles().addAll(articlesList);
        feedId = feedBox.put(feedDbModel);
    }

    @AfterClass
    public static void afterClass() {
        //delete articles
        articleBox.remove(feedBox.get(feedId).getArticles());
        feedBox.remove(feedId);
    }

    @Before
    public void setUp() throws Exception {
        converter = Mappers.getMapper(FeedObjectBoxConverter.class);
    }


    @Test
    public void shouldMapArticleDbModelToDomain() throws Exception {
        ArticleModelObjectBox articleDb = feedBox.get(feedId).getArticles().get(0);

        Article articleDomain = converter.dbToDomain(articleDb);

        assertArticleDbToDomain(articleDb, articleDomain);
    }

    private void assertArticleDbToDomain(ArticleModelObjectBox articleDb, Article articleDomain) {
        Assert.assertEquals(articleDb.getFeed().getTargetId(), articleDomain.feedId);
        Assert.assertEquals(articleDb.getId(), articleDomain.id);
        Assert.assertEquals(articleDb.getTitle(), articleDomain.title);
        Assert.assertEquals(articleDb.getLink(), articleDomain.link);
        Assert.assertEquals(articleDb.getPublicationDate(), articleDomain.publicationDate);
        Assert.assertEquals(articleDb.isNew(), articleDomain.isNew);
        Assert.assertEquals(articleDb.isFavourite(), articleDomain.isFavourite);
    }

    @Test
    public void shouldMapFeedDbModelToDomain() throws Exception {
        FeedModelObjectBox feedDb = feedBox.get(feedId);

        Feed feedDomain = converter.dbToDomain(feedDb);

        Assert.assertEquals(feedDb.getId(), feedDomain.id);
        Assert.assertEquals(feedDb.getDescription(), feedDomain.description);
        Assert.assertEquals(feedDb.getImageUrl(), feedDomain.imageUrl);
        Assert.assertEquals(feedDb.getPageLink(), feedDomain.pageLink);
        Assert.assertEquals(feedDb.getTitle(), feedDomain.title);
        Assert.assertEquals(feedDb.getUrl(), feedDomain.url);

        for (ArticleModelObjectBox articleDb : feedDb.getArticles()) {
            assertArticleDbToDomain(articleDb, converter.dbToDomain(articleDb));
        }
    }

    @Test
    public void shouldMapApiArticleToDb() throws Exception {
        ApiArticle apiArticle = new ApiArticle("New title", "link/", imageLink, 2343245L);
        assertApiArticleToDb(apiArticle, converter.apiToDb(apiArticle));
    }

    private void assertApiArticleToDb(ApiArticle apiArticle, ArticleModelObjectBox articleDb) {
        Assert.assertEquals(apiArticle.publicationDate, articleDb.getPublicationDate());
        Assert.assertEquals(apiArticle.link, articleDb.getLink());
        Assert.assertEquals(apiArticle.title, articleDb.getTitle());
        Assert.assertEquals(false, articleDb.isFavourite());
        Assert.assertEquals(true, articleDb.isNew());
    }

    @Test
    public void shouldMapFeedApiToDb() throws Exception {
        int srticleListSize = 3;

        //create ApiFeed with ApiArticle list
        List<ApiArticle> apiArticles = new ArrayList<>();
        for (int i = 0; i < srticleListSize; i++) {
            apiArticles.add(new ApiArticle("New title" + i, "link/" + i, imageLink, 2343245L + i));
        }
        ApiFeed apiFeed = new ApiFeed(
                ConverterTestData.TEST_STRING_TITLE_1,
                ConverterTestData.TEST_IMAGE_URL,
                ConverterTestData.TEST_COMPLEX_URL_STRING_1,
                ConverterTestData.TEST_DESCRIPTION_STRING,
                ConverterTestData.TEST_BASIC_URL_STRING,
                apiArticles
        );

        //convert to FeedModelObjectBox with ArticleModelObjectBox list
        FeedModelObjectBox feedDbModel = converter.apiToDb(apiFeed);
        for (ApiArticle apiArticle: apiFeed.articles) {
            ArticleModelObjectBox articleDb = converter.apiToDb(apiArticle);
            feedDbModel.getArticles().add(articleDb);
        }

        //assert Feed convert
        Assert.assertEquals(apiFeed.title, feedDbModel.getTitle());
        Assert.assertEquals(apiFeed.description, feedDbModel.getDescription());
        Assert.assertEquals(apiFeed.imageUrl, feedDbModel.getImageUrl());
        Assert.assertEquals(apiFeed.pageLink, feedDbModel.getPageLink());
        Assert.assertEquals(apiFeed.url, feedDbModel.getUrl());

        //assert Article convert
        for (int i = 0; i < srticleListSize; i++) {
            assertApiArticleToDb(apiFeed.articles.get(i), feedDbModel.getArticles().get(i));
        }
    }
}
