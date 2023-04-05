package com.kenji1947.rssreader.presentation.article_list;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kenji1947.rssreader.R;
import com.kenji1947.rssreader.data.worker.image_loader.ImageLoader;
import com.kenji1947.rssreader.domain.entities.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;
import timber.log.Timber;

/**
 * Created by chamber on 14.12.2017.
 */

public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ArticleHolder> {
    private static final long CLICK_THROTTLE_WINDOW_MILLIS = 300L;

    private List<Article> articleList = new ArrayList<>();
    private Subject<Integer> onItemClickSubject = BehaviorSubject.create();
    private Subject<Integer> onItemFavouriteClickSubject = BehaviorSubject.create();

    private final ImageLoader imageLoader;

    ArticleListAdapter(final ImageLoader imageLoader) {
        this.imageLoader = imageLoader;
    }

    @Override
    public ArticleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_article, parent, false);
        return new ArticleHolder(itemView, imageLoader, onItemClickSubject, onItemFavouriteClickSubject);
    }

    @Override
    public void onBindViewHolder(ArticleHolder holder, int position) {
        holder.setItem(articleList.get(position));
    }

    public void updateArticles(List<Article> articleList) {
        this.articleList.clear();
        this.articleList.addAll(articleList);
        notifyDataSetChanged();
    }

    public Observable<Integer> onItemClick() {
        return onItemClickSubject.throttleFirst(CLICK_THROTTLE_WINDOW_MILLIS, TimeUnit.MILLISECONDS)
                .doOnNext(this::markArticleAsRead);
    }

    private void markArticleAsRead(Integer integer) {
        Article article = articleList.get(integer);
        article.isNew = false;
        notifyItemChanged(integer);
    }

    public Observable<Integer> onFavouriteArticleClick() {
        return onItemFavouriteClickSubject.throttleFirst(CLICK_THROTTLE_WINDOW_MILLIS, TimeUnit.MILLISECONDS)
                .doOnNext(this::toggleArticleFavouriteStatus);
    }

    //TODO Optimistic action
    private void toggleArticleFavouriteStatus(Integer integer) {
        Article article = articleList.get(integer);
        article.isFavourite = !article.isFavourite;
        notifyItemChanged(integer);
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    public static class ArticleHolder extends RecyclerView.ViewHolder {
        public @BindView(R.id.imageView_article_icon) ImageView imageView_article_icon;
        public @BindView(R.id.textView_article_title) TextView textView_article_title;
        public @BindView(R.id.textView_article_date) TextView textView_article_date;
        public @BindView(R.id.textView_article_new_indicator) TextView textView_article_new_indicator;
        public @BindView(R.id.imageView_article_favourite_indicator) ImageView imageView_article_favourite_indicator;

        private final ImageLoader imageLoader;

        private Article article;

        private Subject<Integer> onItemClickSubject;
        private Subject<Integer> onItemFavouriteClickSubject;

        public ArticleHolder(View itemView,
                             final ImageLoader imageLoader,
                             Subject<Integer> onItemClickSubject,
                             Subject<Integer> onItemFavouriteClickSubject) {
            super(itemView);
            this.onItemClickSubject = onItemClickSubject;
            this.onItemFavouriteClickSubject = onItemFavouriteClickSubject;
            this.imageLoader = imageLoader;
            ButterKnife.bind(this, itemView);
        }

        public void setItem(Article article) {
            this.article = article;
            textView_article_title.setText(article.title);
            textView_article_date.setText(article.publicationDate + "");

            imageView_article_favourite_indicator.setImageResource(article.isFavourite ?
                    R.drawable.ic_favorite : R.drawable.ic_not_favorite);

            imageView_article_favourite_indicator.setTag(article.isFavourite ?
                    R.drawable.ic_favorite : R.drawable.ic_not_favorite);

            textView_article_new_indicator.setVisibility(article.isNew ? View.VISIBLE : View.GONE);

            imageLoader.loadImage(
                    article.imageLink,
                    imageView_article_icon,
                    R.drawable.secondary_circle,
                    R.drawable.secondary_circle
            );
            Timber.d("imageLink: " + article.imageLink);
        }

        @OnClick(R.id.article_content_container)
        void onArticleClick() {
            onItemClickSubject.onNext(getAdapterPosition());
        }

        @OnClick(R.id.imageView_article_favourite_indicator)
        void onFeedFavouriteArticleClick() {
            onItemFavouriteClickSubject.onNext(getAdapterPosition());
        }
    }

}
