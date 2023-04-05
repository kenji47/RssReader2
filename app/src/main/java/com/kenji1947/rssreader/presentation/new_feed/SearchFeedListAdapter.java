package com.kenji1947.rssreader.presentation.new_feed;

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
import com.kenji1947.rssreader.domain.entities.SearchedFeed;
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

public final class SearchFeedListAdapter extends RecyclerView.Adapter<SearchFeedListAdapter.FeedViewHolder> {
    private static final long CLICK_THROTTLE_WINDOW_MILLIS = 300L; //TODO Объяснить

    private final ImageLoader imageLoader;

    private List<SearchedFeed> feeds = new ArrayList<>(0);

    private final Subject<Integer> onItemClickSubject = BehaviorSubject.create();
    private final Subject<Integer> onItemLongClickSubject = BehaviorSubject.create();

    SearchFeedListAdapter(final ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    @Override
    public SearchFeedListAdapter.FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_search_feed, parent, false);
        return new SearchFeedListAdapter.FeedViewHolder(itemView, imageLoader, onItemClickSubject,
                onItemLongClickSubject);
    }

    @Override
    public void onBindViewHolder(FeedViewHolder holder, int position) {
        holder.setItem(feeds.get(position));
    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }

    public void onFeedsUpdate(final List<SearchedFeed> feeds) {
        //this.feeds = feeds; //TODO Объяснить чем плох этот вариант
        this.feeds.clear();
        this.feeds.addAll(feeds);
        notifyDataSetChanged();
    }

    public void updateData(List<SearchedFeed> feeds) {
        this.feeds.clear();
        this.feeds.addAll(feeds);
    }

    //TODO Отрефакторить название
    //TODO throttleFirst то что нужно для дебаунса
    public Observable<Integer> onItemClick() {
        return onItemClickSubject.throttleFirst(CLICK_THROTTLE_WINDOW_MILLIS, TimeUnit.MILLISECONDS);
    }
    public Observable<Integer> onItemLongClick() {
        return onItemLongClickSubject.throttleFirst(CLICK_THROTTLE_WINDOW_MILLIS, TimeUnit.MILLISECONDS);
    }

    public static final class FeedViewHolder extends RecyclerView.ViewHolder {

        public @BindView(R.id.imageView_feed_image) ImageView feedImage;
        public @BindView(R.id.textView_feed_title) TextView feedTitle;
        public @BindView(R.id.textView_feed_description) TextView feedDescription;

        private final ImageLoader imageLoader;

        private final Subject<Integer> clickSubject;
        private final Subject<Integer> longClickSubject;

        private SearchedFeed feed;

        public FeedViewHolder(final View itemView,
                              final ImageLoader imageLoader,
                              final Subject<Integer> clickSubject,
                              final Subject<Integer> longClickSubject) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.imageLoader = imageLoader;
            this.clickSubject = clickSubject;
            this.longClickSubject = longClickSubject;
        }

        public void setItem(final SearchedFeed feed) {
            this.feed = feed;       //TODO R.drawable.feed_icon
            Timber.d("image url " + feed.imageUrl);
            imageLoader.loadImage(
                    feed.imageUrl,
                    feedImage,
                    R.drawable.secondary_circle,
                    R.drawable.secondary_circle
            );
            feedTitle.setText(feed.title);
            feedDescription.setText(feed.description);
            if (feed.isSubscribed) {
                Timber.d("isSubscribed " + feed.title);
                itemView.setSelected(true);
            }
        }

        //TODO Можно заменяит на ViewHolder onClick
        @OnClick(R.id.feed_content_container)
        void onFeedClick() {
            clickSubject.onNext(getAdapterPosition());
        }

        @OnLongClick(R.id.feed_content_container)
        boolean onFeedLongClick() {
            longClickSubject.onNext(getAdapterPosition());
            return true;
        }

    }
}
