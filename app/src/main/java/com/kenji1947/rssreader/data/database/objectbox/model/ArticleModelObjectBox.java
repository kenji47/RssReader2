package com.kenji1947.rssreader.data.database.objectbox.model;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

/**
 * Created by kenji1947 on 09.11.2017.
 */

@Entity
public class ArticleModelObjectBox {

    @Id
    long id;

    public String title;
    public String link;
    public String content;
    public long publicationDate;
    public String imageLink;

    public boolean isNew = true;
    public boolean isFavourite = false;

    public ArticleModelObjectBox() {
    }

    public ArticleModelObjectBox(String title, String link, String imageLink, String content, long publicationDate) {
        this.title = title;
        this.link = link;
        this.imageLink = imageLink;
        this.content = content;
        this.publicationDate = publicationDate;
    }

    public ArticleModelObjectBox(String title, String link, String imageLink, String content, long publicationDate, boolean isNew, boolean isFavourite) {
        this.title = title;
        this.link = link;
        this.imageLink = imageLink;
        this.content = content;
        this.publicationDate = publicationDate;
        this.isNew = isNew;
        this.isFavourite = isFavourite;
    }

    public ArticleModelObjectBox(long id, String title, String link, String imageLink, String content, long publicationDate, boolean isNew, boolean isFavourite) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.imageLink = imageLink;
        this.content = content;
        this.publicationDate = publicationDate;
        this.isNew = isNew;
        this.isFavourite = isFavourite;
    }

    @Backlink
    public ToOne<FeedModelObjectBox> feed;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public long getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(long publicationDate) {
        this.publicationDate = publicationDate;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public ToOne<FeedModelObjectBox> getFeed() {
        return feed;
    }

    public void setFeed(ToOne<FeedModelObjectBox> feed) {
        this.feed = feed;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
