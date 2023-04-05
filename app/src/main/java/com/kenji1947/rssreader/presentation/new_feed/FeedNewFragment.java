package com.kenji1947.rssreader.presentation.new_feed;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.kenji1947.rssreader.App;
import com.kenji1947.rssreader.R;
import com.kenji1947.rssreader.data.worker.image_loader.ImageLoader;
import com.kenji1947.rssreader.di.presenter.FeedNewPresenterComponent;
import com.kenji1947.rssreader.domain.entities.SearchedFeed;
import com.kenji1947.rssreader.presentation.common.BackButtonListener;
import com.kenji1947.rssreader.presentation.common.ThemedSnackbar;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * Created by chamber on 13.12.2017.
 */

//TODO Not used
public class FeedNewFragment extends MvpAppCompatFragment implements FeedNewView, BackButtonListener {
    private static final long THROTTLE_MILLIS = 300L; //TODO Объяснить

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.editText_search_feed) EditText editText_search_feed;
    @BindView(R.id.button_search_feed) Button button_search_feed;
    @BindView(R.id.loadProgressBar) ProgressBar progressBar_loading_indicator;
    @BindView(R.id.loading_layout) ViewGroup loading_layout;
    @BindView(R.id.textView_loading_message) TextView textView_loading_message;
    @BindView(R.id.recyclerView_feeds) RecyclerView recyclerView_feeds;
    @BindView(R.id.coordinator_top) CoordinatorLayout coordinator_top;

    @BindView(R.id.linearlayout_feed_search_add_new_feed) LinearLayout linearlayout_feed_search_add_new_feed;
    @BindView(R.id.linearlayout_feed_search_nothing_found) LinearLayout linearlayout_feed_search_nothing_found;

    private Unbinder unbinder;

    private SearchFeedListAdapter adapter;
    private List<SearchedFeed> feeds;

    public static FeedNewFragment newInstance() {
        Bundle args = new Bundle();

        FeedNewFragment fragment = new FeedNewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Inject
    ImageLoader imageLoader;

    @InjectPresenter
    FeedNewPresenter presenter;
    @ProvidePresenter
    FeedNewPresenter providePresenter() {
        return FeedNewPresenterComponent.Initializer.init(App.INSTANCE.getAppComponent()).provideFeedNewPresenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.INSTANCE.getAppComponent().inject(this);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_new_feed_search, container, false);
        unbinder = ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initList();
        initViews();
    }


    private void initViews() {
        editText_search_feed.requestFocus();
    }

    private void initToolbar() {
        toolbar.setTitle("Search feed");
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_delete);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Timber.d("home");
                onBackPressed();
                return true;
        }
        return false;
    }

    private void initList() {
        adapter = new SearchFeedListAdapter(imageLoader);
        recyclerView_feeds.setAdapter(adapter);
        recyclerView_feeds.setLayoutManager(new LinearLayoutManager(getContext()));

        //TODO Почему не положили подписку в Composite?
        adapter.onItemClick().subscribe(this::onFeedClicked);
        //adapter.onItemLongClick().subscribe(this::onFeedLongClick);
    }

    private void onFeedClicked(int pos) {
        Timber.d("onFeedClicked " + pos);
        presenter.subscribeToFeed(pos);
    }

    //TODO Валидация текста
    @OnClick(R.id.button_search_feed)
    void onButtonSearchFeeds() {presenter.searchFeeds(editText_search_feed.getText().toString());}

    private void back() {
        presenter.back();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //---
    @Override
    public void showSearchFeedLoadingDialog() {
        textView_loading_message.setText("Searching feeds...");
        loading_layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSubscribeFeedLoadingDialog(String feedTitle) {
        textView_loading_message.setText("Subscribing to " + feedTitle);
        loading_layout.setVisibility(View.VISIBLE);
    }

    public void hideLoadingDialog() {
        loading_layout.setVisibility(View.GONE);
    }

    @Override
    public void showProgress(boolean progress) {
        progressBar_loading_indicator.setVisibility(progress ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showMessage(String message) {
        Snackbar snackbar = Snackbar
                .make(coordinator_top, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void showErrorMessage(String message) {
        ThemedSnackbar.makeErrorSnackbar(coordinator_top, message).show();
    }

    @Override
    public void setSearchedFeeds(List<SearchedFeed> feeds) {
        this.feeds = feeds;
        adapter.onFeedsUpdate(feeds);
        adjustEmptyState(feeds.isEmpty());
    }

    private void adjustEmptyState(boolean isViewEmpty) {
        linearlayout_feed_search_add_new_feed.setVisibility(View.GONE);
        linearlayout_feed_search_nothing_found.setVisibility(isViewEmpty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showKeyboard(boolean show) {
        if (show) {
            editText_search_feed.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText_search_feed, InputMethodManager.SHOW_IMPLICIT);
        } else {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText_search_feed.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onBackPressed() {
        Timber.d("onBackPressed");
        presenter.back();
        return true;
    }
}
