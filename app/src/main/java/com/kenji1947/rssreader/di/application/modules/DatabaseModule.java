package com.kenji1947.rssreader.di.application.modules;

import android.content.Context;

import com.kenji1947.rssreader.BuildConfig;
import com.kenji1947.rssreader.data.api.fetch_feed.plain_rss_atom.model.ApiConverter;
import com.kenji1947.rssreader.data.database.ArticleDao;
import com.kenji1947.rssreader.data.database.FeedDao;
import com.kenji1947.rssreader.data.database.objectbox.ArticleDaoObjectBox;
import com.kenji1947.rssreader.data.database.objectbox.FeedDaoObjectBox;
import com.kenji1947.rssreader.data.database.objectbox.converter.FeedObjectBoxConverter;
import com.kenji1947.rssreader.data.database.objectbox.model.ArticleModelObjectBox;
import com.kenji1947.rssreader.data.database.objectbox.model.FeedModelObjectBox;
import com.kenji1947.rssreader.data.database.objectbox.model.MyObjectBox;
import com.kenji1947.rssreader.data.worker.preference.PreferenceManager;
import com.kenji1947.rssreader.data.worker.preference.PreferenceManagerImpl;

import org.mapstruct.factory.Mappers;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.android.AndroidObjectBrowser;

/**
 * Created by kenji1947 on 11.11.2017.
 */

//TODO Уйти от @Provides мелочи
@Module
public class DatabaseModule {
    @Singleton
    @Provides
    FeedDao provideFeedDao(Box<FeedModelObjectBox> box,
                           FeedObjectBoxConverter converter,
                           Box<ArticleModelObjectBox> articleBox,
                           BoxStore boxStore
                           ) {
        return new FeedDaoObjectBox(box, articleBox, converter, boxStore);
    }

    @Singleton
    @Provides
    ArticleDao provideArticleDao(Box<ArticleModelObjectBox> box, FeedObjectBoxConverter converter) {
        return new ArticleDaoObjectBox(box, converter);
    }

    //MapStruct dependencies
    @Singleton
    @Provides
    FeedObjectBoxConverter provideFeedObjectBoxConverter() {
        return Mappers.getMapper(FeedObjectBoxConverter.class);
    }

    @Singleton
    @Provides
    ApiConverter provideApiConverter() {
        return Mappers.getMapper(ApiConverter.class);
    }

    //ObjectBox dependencies
    @Singleton
    @Provides
    BoxStore provideBoxStore(Context context) {
        BoxStore boxStore = MyObjectBox.builder().androidContext(context).build();
        if (BuildConfig.DEBUG) {
            new AndroidObjectBrowser(boxStore).start(context);
        }
        return boxStore;
    }

    @Singleton
    @Provides
    Box<ArticleModelObjectBox> provideBoxArticle(BoxStore boxStore) {
        return getBox(ArticleModelObjectBox.class, boxStore);
    }

    @Singleton
    @Provides
    Box<FeedModelObjectBox> provideBoxFeed(BoxStore boxStore) {
        return getBox(FeedModelObjectBox.class, boxStore);
    }

    @Singleton
    @Provides
    PreferenceManager providePreferenceManager(Context context) {
        return new PreferenceManagerImpl(context);
    }

    //TODO Разобрать дженерик
    private <T> Box<T> getBox(Class<T> cls, BoxStore boxStore) {
        return boxStore.boxFor(cls);
    }

    public interface Exposes{
        BoxStore provideBoxStore();
        PreferenceManager providePreferenceManager();
        //Database provideDatabase();
        //FeedDao provideFeedDao();
        //ArticleDao provideArticleDao();
    }
}
