package com.kenji1947.rssreader.presentation.feed_list;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kenji1947.rssreader.R;
import com.kenji1947.rssreader.data.worker.image_loader.ImageLoader;
import com.kenji1947.rssreader.domain.entities.Article;
import com.kenji1947.rssreader.domain.entities.Feed;
import com.kenji1947.rssreader.presentation.AdapterOverflowMenuHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import timber.log.Timber;

/**
 * Created by chamber on 08.12.2017.
 */

public final class FeedListAdapter extends RecyclerView.Adapter<FeedListAdapter.FeedViewHolder> {
    private static final long CLICK_THROTTLE_WINDOW_MILLIS = 300L; //TODO Объяснить

    private final ImageLoader imageLoader;

    private List<Feed> feeds = new ArrayList<>(0);

    private final Subject<Feed> onItemClickSubject = BehaviorSubject.create();
    private final Subject<Feed> onItemLongClickSubject = BehaviorSubject.create();
    private final Subject<AdapterOverflowMenuHolder> onOverflowMenuClickSubject = BehaviorSubject.create();

    FeedListAdapter(final ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    @Override
    public FeedListAdapter.FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_feed_c, parent, false);
        return new FeedListAdapter.FeedViewHolder(
                itemView,
                imageLoader,
                onItemClickSubject, onItemLongClickSubject, onOverflowMenuClickSubject);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        holder.setItem(feeds.get(position));
    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }

    public void onFeedsUpdate(final List<Feed> feeds) {
        //this.feeds = feeds; //TODO Объяснить чем плох этот вариант
        this.feeds.clear();
        this.feeds.addAll(feeds);
        notifyDataSetChanged();
    }

    public void updateData(List<Feed> feeds) {
        this.feeds.clear();
        this.feeds.addAll(feeds);
    }

    //TODO Отрефакторить название
    //TODO throttleFirst то что нужно для дебаунса
    public Observable<Feed> onItemClick() {
        return onItemClickSubject.throttleFirst(CLICK_THROTTLE_WINDOW_MILLIS, TimeUnit.MILLISECONDS);
    }
    public Observable<Feed> onItemLongClick() {
        return onItemLongClickSubject.throttleFirst(CLICK_THROTTLE_WINDOW_MILLIS, TimeUnit.MILLISECONDS);
    }
    public Observable<AdapterOverflowMenuHolder> onOverflowMenuClick() {
        return onOverflowMenuClickSubject.throttleFirst(CLICK_THROTTLE_WINDOW_MILLIS, TimeUnit.MILLISECONDS);
    }
    public static final class FeedViewHolder extends RecyclerView.ViewHolder {

        //@BindView(R.id.selected_indicator_view) RevealFillView selectionIndicator;
        public @BindView(R.id.imageView_feed_image) ImageView feedImage;
        public @BindView(R.id.textView_feed_title) TextView feedTitle;
        public @BindView(R.id.textView_feed_description) TextView feedDescription;
        public @BindView(R.id.imageButton_options_menu) ImageButton imageButton_options_menu;
        public @BindView(R.id.textView_article_count) TextView textView_article_count;

        private final ImageLoader imageLoader;

        private final Subject<Feed> clickSubject;
        private final Subject<Feed> longClickSubject;
        private final Subject<AdapterOverflowMenuHolder> overflowMenuClickSubject;

        private Feed feed;

        public FeedViewHolder(final View itemView,
                              final ImageLoader imageLoader,
                              final Subject<Feed> clickSubject,
                              final Subject<Feed> longClickSubject,
                              final Subject<AdapterOverflowMenuHolder> overflowMenuClickSubject) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.imageLoader = imageLoader;
            this.clickSubject = clickSubject;
            this.longClickSubject = longClickSubject;
            this.overflowMenuClickSubject = overflowMenuClickSubject;
        }

        public void setItem(final Feed feed) {
            this.feed = feed;       //TODO R.drawable.feed_icon
            Timber.d("image url " + feed.imageUrl);
            imageLoader.loadImage(
                    feed.imageUrl,
                    feedImage,
                    R.drawable.secondary_circle,
                    R.drawable.secondary_circle
            );
            feedTitle.setText(feed.title + " (" + feed.articles.size() + ")");
            feedDescription.setText(feed.description);
            //textView_article_count.setText(feed.articles.size() + "");
            Timber.d(feed.title + " articles: " + feed.articles.size());
//            if (feed.isSelected) {
//                selectionIndicator.startFillAnimation();
//            } else {
//                selectionIndicator.startHideAnimation();
//            }
        }

        private int getUnreadArticlesCount(List<Article> articles) {
            int count = 0;
            for (Article article : articles) {
                if (article.isNew)
                    count++;
            }
            return count;
        }

        //TODO Можно заменяит на ViewHolder onClick
        @OnClick(R.id.feed_content_container)
        void onFeedClick() {
            clickSubject.onNext(feed);
        }

        @OnLongClick(R.id.feed_content_container)
        boolean onFeedLongClick() {
            longClickSubject.onNext(feed);
            return true;
        }

        @OnClick(R.id.imageButton_options_menu)
        void onOverflowMenuClick() {
            overflowMenuClickSubject.onNext(
                    new AdapterOverflowMenuHolder(imageButton_options_menu, getAdapterPosition())
            );
        }
    }
}
