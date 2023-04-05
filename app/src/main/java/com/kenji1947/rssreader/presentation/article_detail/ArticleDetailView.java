package com.kenji1947.rssreader.presentation.article_detail;

import com.arellomobile.mvp.MvpView;
import com.kenji1947.rssreader.domain.entities.Article;

/**
 * Created by chamber on 15.12.2017.
 */

public interface ArticleDetailView extends MvpView {
    void setArticle(Article article);
}
