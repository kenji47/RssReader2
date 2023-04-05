package com.kenji1947.rssreader.presentation.article_list;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.kenji1947.rssreader.App;
import com.kenji1947.rssreader.R;
import com.kenji1947.rssreader.data.worker.image_loader.ImageLoader;
import com.kenji1947.rssreader.di.presenter.ArticleListPresenterComponent;
import com.kenji1947.rssreader.domain.entities.Article;
import com.kenji1947.rssreader.domain.entities.Feed;
import com.kenji1947.rssreader.presentation.article_detail.ArticleDetailFragment;
import com.kenji1947.rssreader.presentation.common.ThemedSnackbar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * Created by chamber on 14.12.2017.
 */

public class ArticleListFragment extends MvpAppCompatFragment implements ArticleListView {
    public static final String TAG = ArticleListFragment.class.getSimpleName();

    public final class ArgumentsHolder {
        public long id;
        public String title;

        public ArgumentsHolder(long id, String title) {
            this.id = id;
            this.title = title;
        }
    }

    private static final String ARG_FEED_ID = "feed_id";
    private static final String ARG_FEED_TITLE = "title";
    private static final String ARG_FAVOURITE_ARTICLES_MODE = "favourite_articles_mode";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.swipeRefreshLayout_refresh_articles) SwipeRefreshLayout swipeRefreshLayout_refresh_articles;
    @BindView(R.id.recyclerView_articles) RecyclerView recyclerView_articles;
    @BindView(R.id.coordinator_top) CoordinatorLayout coordinator_top;

    private Unbinder unbinder;
    private boolean isFavMode;
    private List<Article> articles;
    private ArticleListAdapter adapter;

    @Inject
    ImageLoader imageLoader;

    @InjectPresenter
    ArticleListPresenter presenter;

    @ProvidePresenter
    ArticleListPresenter providePresenter() {
        //TODO id
        return ArticleListPresenterComponent.Initializer.init(
                App.INSTANCE.getAppComponent(), getIdFromArguments(), getFavModeStatusFromArguments()
        ).providePresenter();
    }

    public static ArticleListFragment newInstance(long feedId, String title) {
        Bundle args = new Bundle();
        args.putLong(ARG_FEED_ID, feedId);
        args.putString(ARG_FEED_TITLE, title);
        ArticleListFragment fragment = new ArticleListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ArticleListFragment newInstanceFavMode() {
        Bundle args = new Bundle();
        args.putBoolean(ARG_FAVOURITE_ARTICLES_MODE, true);
        ArticleListFragment fragment = new ArticleListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private long getIdFromArguments() {
        return getArguments().getLong(ARG_FEED_ID);
    }

    private boolean getFavModeStatusFromArguments() {
        return getArguments().getBoolean(ARG_FAVOURITE_ARTICLES_MODE, false);
    }

    private String getFeedTitleFromArguments() {
        return getArguments().getString(ARG_FEED_TITLE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        isFavMode = getFavModeStatusFromArguments();
        App.INSTANCE.getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_article_list, container, false);
        unbinder = ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList();
        if (isFavMode) {
            //presenter.getFavArticles();
            initToolbar(getString(R.string.toolbar_title_favourite_articles));
        } else {
            //presenter.getArticlesForFeed(getIdFromArguments());
            initToolbar(getFeedTitleFromArguments());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initList() {
        adapter = new ArticleListAdapter(imageLoader);

        adapter.onItemClick().subscribe(this::onItemClick);
        adapter.onFavouriteArticleClick().subscribe(this::onItemFav);

        recyclerView_articles.setAdapter(adapter);

        recyclerView_articles.setLayoutManager(new LinearLayoutManager(getActivity()));
        //TODO Disable refresh isFavMode
        swipeRefreshLayout_refresh_articles.setOnRefreshListener(() -> {
            if (isFavMode)
                presenter.getFavArticles();
            else
                presenter.updateArticles();
                //presenter.getArticlesForFeed(getIdFromArguments());
        });
    }

    private void onItemClick(int pos) {
        Timber.d("onItemClick" + pos);
        presenter.markArticleAsRead(articles.get(pos).id);
        presenter.showArticle(pos);

//        FragmentManager fragmentManager = getFragmentManager();
//        ArticleDetailFragment articleDetailFragment = ArticleDetailFragment.newInstance(articles.get(pos).link);
//
//        fragmentManager.beginTransaction()
//                .add(R.id.frameLayout_container, articleDetailFragment, ArticleListFragment.TAG)
//                .hide(this)
//                .addToBackStack("dsa") //TODO tag?
//                .commit();
    }

    private void onItemFav(int pos) {
        Timber.d("onItemFav" + pos);
        presenter.toggleArticleFavourite(pos);
    }

    private void initToolbar(String title) {
        toolbar.setTitle(title);
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); //TODO
        //toolbar.setNavigationOnClickListener(view -> presenter.onBackPressed());
        //setHasOptionsMenu(true);
    }

    //TODO NavigationUp

    //---
    @Override
    public void showProgress(boolean progress) {
        swipeRefreshLayout_refresh_articles.post(() -> swipeRefreshLayout_refresh_articles.setRefreshing(progress));
    }

    @Override
    public void showArticles(List<Article> articles) {
        this.articles = articles;
        adapter.updateArticles(articles);
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public void showNewArticlesCountMessage(Integer count) {
        ThemedSnackbar
                .makeNewArticlesSnackbar(coordinator_top, getString(R.string.snackbar_text_updated_articles, count))
                .show();
    }

    @Override
    public void showErrorMessage(String message) {

    }
}
