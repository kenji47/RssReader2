package com.kenji1947.rssreader.data.api.fetch_feed.plain_rss_atom.parser;

import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;

import com.annimon.stream.Stream;
import com.einmalfel.earl.AtomEntry;
import com.einmalfel.earl.AtomFeed;
import com.einmalfel.earl.EarlParser;
import com.einmalfel.earl.Enclosure;
import com.einmalfel.earl.Feed;
import com.einmalfel.earl.Item;
import com.einmalfel.earl.RSSFeed;
import com.einmalfel.earl.RSSItem;
import com.kenji1947.rssreader.data.api.fetch_feed.plain_rss_atom.model.ApiArticle;
import com.kenji1947.rssreader.data.api.fetch_feed.plain_rss_atom.model.ApiFeed;
import com.kenji1947.rssreader.data.util.CurrentTimeProvider;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import timber.log.Timber;


public final class EarlParserWrapper implements ParserWrapper {

    private final CurrentTimeProvider currentTimeProvider;

    public EarlParserWrapper(final CurrentTimeProvider currentTimeProvider) {
        this.currentTimeProvider = currentTimeProvider;
    }

    @Override
    public ApiFeed parseOrThrow(final InputStream inputStream, final String feedUrl) throws Exception {
        return mapToApiFeed(EarlParser.parseOrThrow(inputStream, 0), feedUrl);
    }

    @Override
    public ApiFeed parse(final InputStream inputStream, final String feedUrl) {
        return mapToApiFeed(EarlParser.parse(inputStream, 0), feedUrl);
    }

    private ApiFeed mapToApiFeed(final Feed parsedFeed, final String feedUrl) {
        //Feed
        Timber.d("title " + parsedFeed.getTitle());
        Timber.d("description " + parsedFeed.getDescription());
        Timber.d("image  " + parsedFeed.getImageLink());
        Timber.d("link  " + parsedFeed.getLink());

        boolean isAtom = parsedFeed instanceof AtomFeed;

        //Article
        for (Item item : parsedFeed.getItems()) {
            Timber.d("title  " + item.getTitle());
            Timber.d("description  " + Html.fromHtml(item.getDescription()));
            Timber.d("link  " + item.getLink());
            Timber.d("pubDate  " + item.getPublicationDate());

            if (isAtom) {
                AtomEntry atomEntry = (AtomEntry) item;
                String content = null;

                if (atomEntry.content != null && !TextUtils.isEmpty(atomEntry.content.value)) {
                    try {
                        content = URLDecoder.decode(atomEntry.content.value, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        //e.printStackTrace();
                        Timber.e(e);
                    }
                }
                Timber.d("content  " + content);

            } else {
                RSSItem rssItem = (RSSItem) item;

                String imageUrl = null;
                if (rssItem.getEnclosures() != null) {
                    for (Enclosure enclosure : rssItem.getEnclosures()) {
                        String type = enclosure.getType();
                        if (type != null && type.startsWith("image/")) {
                            Timber.d("imageLink " + enclosure.getType() + " " + enclosure.getLink());
                            imageUrl = enclosure.getLink();
                            break;
                        }
                    }
                }
                //<div><img src="https://icdn.lenta.ru/images/2018/02/09/11/20180209112312841/pic_bed05acce04711650e2fd682f9ee0695.jpg" /></div>

                String content = "<div><img src=\"" + imageUrl + "\" /></div>" + " " + rssItem.getDescription();
                Timber.d("content  " + content); //TOdo Не нужно контент избавлять от тегов
            }
        }

//
//
//
//        if (parsedFeed instanceof RSSFeed) {
//            Timber.d("RSSFeed " + feedUrl);
//            Timber.d("ITEMS");
//            for (ArticleItem item : parsedFeed.getItems()) {
//                RSSItem rssItem = (RSSItem) item;
//
//                Timber.d("title  " + rssItem.getTitle());
//                Timber.d("description  " + Html.fromHtml(rssItem.getDescription())); //TODO maybe html
//
//                //Timber.d("content  " + image + description);
//
//                Timber.d("link  " + rssItem.getLink());
//                Timber.d("pubDate  " + rssItem.getPublicationDate());
//                Timber.d("getImageLink  " + rssItem.getImageLink());
//                if (rssItem.getEnclosures() != null) {
//                    for (Enclosure enclosure : rssItem.getEnclosures()) {
////                        String type = enclosure.getType();
////                        if (type != null && type.startsWith("image/")) {
////                            return enclosure.getLink();
////                        }
//                      Timber.d(enclosure.getType() + " " + enclosure.getLink());
//                    }
//                }
//            }
//        }
//        else if (parsedFeed instanceof AtomFeed) {
//            Timber.d("AtomFeed " + feedUrl);
//            Timber.d("ITEMS");
//            for (ArticleItem item : parsedFeed.getItems()) {
//                AtomEntry atomEntry = (AtomEntry) item;
//                Timber.d("title  " + atomEntry.getTitle());
//
//                //Delete all tags from html
//                Timber.d("description  " + Html.fromHtml(atomEntry.getDescription()).toString());
//
//                Timber.d("content  " + atomEntry.getDescription());
//                Timber.d("link  " + atomEntry.getLink());
//                Timber.d("pubDate  " + atomEntry.getPublicationDate());
//                Timber.d("getImageLink  " + atomEntry.getImageLink());
//
//                if (atomEntry.content != null && !TextUtils.isEmpty(atomEntry.content.value)) {
//                    String content = atomEntry.content.value;
//
//                    //Unescape html
//                    try {
//                        content = URLDecoder.decode(content, "UTF-8");
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//
//
//
//
////                if (atomEntry.getEnclosures() != null) {
////                    for (Enclosure enclosure : atomEntry.getEnclosures()) {
////                        Timber.d(enclosure.getType() + " " + enclosure.getLink());
////                    }
////                }
//
//            }
//        }

        final List<ApiArticle> apiArticles = Stream.of(parsedFeed.getItems())
                                                   .map(article -> new ApiArticle(article.getTitle(),
                                                           article.getLink(), article.getImageLink(),
                                                           getTimeForDate(article.getPublicationDate())))
                                                   .toList();
        return new ApiFeed(parsedFeed.getTitle(), parsedFeed.getImageLink(),
                parsedFeed.getLink(), parsedFeed.getDescription(), feedUrl, apiArticles);
    }

    private long getTimeForDate(@Nullable final Date date) {
        return (date != null) ? date.getTime() : currentTimeProvider.getCurrentTime();
    }
}
