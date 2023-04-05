package com.kenji1947.rssreader.data.api.fetch_feed.plain_rss_atom.model;

import com.kenji1947.rssreader.domain.entities.Article;
import com.kenji1947.rssreader.domain.entities.Feed;

/**
 * Created by chamber on 21.12.2017.
 */
@org.mapstruct.Mapper
public interface ApiConverter {
    Feed apiToDomain(ApiFeed apiFeed);
    Article apiToDomain(ApiArticle article);
}
