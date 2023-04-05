package com.kenji1947.rssreader.data.database.objectbox.converter;

import com.kenji1947.rssreader.data.api.fetch_feed.plain_rss_atom.model.ApiArticle;
import com.kenji1947.rssreader.data.api.fetch_feed.plain_rss_atom.model.ApiFeed;
import com.kenji1947.rssreader.data.database.objectbox.model.ArticleModelObjectBox;
import com.kenji1947.rssreader.data.database.objectbox.model.FeedModelObjectBox;
import com.kenji1947.rssreader.domain.entities.Article;
import com.kenji1947.rssreader.domain.entities.Feed;

import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chamber on 11.12.2017.
 */
@org.mapstruct.Mapper
public interface FeedObjectBoxConverter {

    Feed dbToDomain(FeedModelObjectBox feedModelObjectBox);
    @Mappings({
            @Mapping(target = "feedId", expression = "java(articleModelObjectBox.feed.getTargetId())")
    })
    Article dbToDomain(ArticleModelObjectBox articleModelObjectBox);


    @Mappings({
            @Mapping(target = "articles", ignore = true)
    })
    FeedModelObjectBox apiToDb(ApiFeed apiFeed);
    ArticleModelObjectBox apiToDb(ApiArticle apiArticle);

    @Mappings({
            @Mapping(target = "articles", ignore = true)
    })
    FeedModelObjectBox domainToDb(Feed feed);
    ArticleModelObjectBox domainToDb(Article article);


    //Because i can't create new instances of Objectbox ToOne/ToMany
    final class ConverterHelper {
        private ConverterHelper() {}

        //TODO Разобрать synchronized!
        public static List<ArticleModelObjectBox> convertApiArticleListToDb(List<ApiArticle> articles,
                                                                            FeedObjectBoxConverter converter) {
            List<ArticleModelObjectBox> articleModelObjectBoxList = new ArrayList<>();

            for (ApiArticle apiArticle : articles) {
                articleModelObjectBoxList.add(converter.apiToDb(apiArticle));
            }
            return articleModelObjectBoxList;
        }

        public static List<ArticleModelObjectBox> convertDomainArticleListToDb(List<Article> articles,
                                                                               FeedObjectBoxConverter converter) {
            List<ArticleModelObjectBox> articleModelObjectBoxList = new ArrayList<>();

            for (Article article : articles) {
                articleModelObjectBoxList.add(converter.domainToDb(article));
            }
            return articleModelObjectBoxList;
        }
    }
}
