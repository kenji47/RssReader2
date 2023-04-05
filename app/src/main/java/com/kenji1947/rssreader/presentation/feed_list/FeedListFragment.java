package com.kenji1947.rssreader.presentation.feed_list;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.kenji1947.rssreader.App;
import com.kenji1947.rssreader.R;
import com.kenji1947.rssreader.data.worker.image_loader.ImageLoader;
import com.kenji1947.rssreader.di.presenter.FeedListPresenterComponent;
import com.kenji1947.rssreader.domain.entities.Feed;
import com.kenji1947.rssreader.presentation.AdapterOverflowMenuHolder;
import com.kenji1947.rssreader.presentation.common.BackButtonListener;
import com.kenji1947.rssreader.presentation.common.DeleteDialog;
import com.kenji1947.rssreader.presentation.common.ListDataDiffHolder;
import com.kenji1947.rssreader.presentation.common.ThemedSnackbar;
import com.kenji1947.rssreader.presentation.new_feed.FeedNewDialog;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * Created by kenji1947 on 11.11.2017.
 */

public class FeedListFragment extends MvpAppCompatFragment implements FeedListView, DeleteDialog.DeleteDialogCallback {
    public static final String TAG = FeedListFragment.class.getSimpleName();

    private static final String CONFIRM_DIALOG_DELETE_FEED = "delete_feed";
    private static final String CONFIRM_DIALOG_SHOW_INFO_FEED = "show_info_feed";

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recyclerView_feeds) RecyclerView listRecyclerView;
    @BindView(R.id.swipeRefreshLayout_refresh_feeds) SwipeRefreshLayout swipeRefreshLayout_refresh_feeds;

    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.loadProgressBar) ProgressBar loadProgressBar;
    @BindView(R.id.linearlayout_empty_list) LinearLayout linearlayout_empty_list;
    @BindView(R.id.coordinator_top) CoordinatorLayout coordinator_top;

    Toast toast;

    @Inject
    ImageLoader imageLoader;

    private Unbinder unbinder;
    private boolean isBackgroundFeedUpdateEnabled;
    private FeedListAdapter adapter;
    private List<Feed> feeds;

    public static FeedListFragment newInstance() {
        Bundle args = new Bundle();
        FeedListFragment fragment = new FeedListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @InjectPresenter
    FeedListPresenter presenter;

    @ProvidePresenter
    FeedListPresenter providePresenter() {
        Timber.d("providePresenter");
        FeedListPresenterComponent component =
                FeedListPresenterComponent.Initializer.init(App.INSTANCE.getAppComponent(), 43);
        return component.providePresenter();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getActivity().getSupportFragmentManager();
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Timber.d("onCreate");
        setRetainInstance(true);
        App.INSTANCE.getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Timber.d("onCreateView");
        View view = inflater.inflate(R.layout.fragment_feed_list, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Timber.d("onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onStart() {
        Timber.d("onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        Timber.d("onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Timber.d("onPause");
        if (toast != null)
            toast.cancel();
        super.onPause();

        //if (!getActivity().isChangingConfigurations())
    }

    private void initToolbar() {
        toolbar.setTitle(getString(R.string.app_name));
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        setHasOptionsMenu(true);
    }

    private void initList() {
        adapter = new FeedListAdapter(imageLoader);
        listRecyclerView.setAdapter(adapter);
        listRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Subscribe to click Subject
        //TODO Почему не положили подписку в Composite?
        adapter.onItemClick().subscribe(this::onFeedClicked);
        adapter.onItemLongClick().subscribe(this::onFeedLongClick);
        adapter.onOverflowMenuClick().subscribe(this::onFeedOverflowMenuClick);

        swipeRefreshLayout_refresh_feeds.setSize(SwipeRefreshLayout.LARGE);
        swipeRefreshLayout_refresh_feeds.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout_refresh_feeds.setOnRefreshListener(() -> {
            //presenter.updateAllFeedsNewArticlesCount();
            presenter.syncFeeds();
            swipeRefreshLayout_refresh_feeds.post(() -> swipeRefreshLayout_refresh_feeds.setRefreshing(false));
        });
    }

    //TODO Адаптер должен возвращать position
    private void onFeedLongClick(Feed feed) {
        Timber.d("onFeedLongClick " + feed.id);
    }

    private void onFeedClicked(Feed feed) {
        Timber.d("onFeedClicked " + feed.id);
        presenter.showArticles(feed);
    }

    private void onFeedOverflowMenuClick(AdapterOverflowMenuHolder holder) {
        Timber.d("onFeedOverflowMenuClick " + holder.getAdapterPos());

        PopupMenu popupMenu = new PopupMenu(getActivity(), holder.getAnchor());
        popupMenu.inflate(R.menu.menu_popup_feed_list);

        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_show_information:
                    Timber.d("menu_show_information");
                    break;
                case R.id.menu_delete:
                    Timber.d("menu_delete");
                    showConfirmDialogListItem(
                            feeds.get(holder.getAdapterPos()).id,
                            getString(
                            R.string.dialog_message_delete_feed, feeds.get(holder.getAdapterPos()).title)
                    );

//                    showConfirmDeleteDialog(getString(
//                            R.string.dialog_message_delete_feed, feeds.get(holder.getAdapterPos()).title)
//                    );

                    break;
            }
            return false;
        });
        popupMenu.show();
    }

    private void showConfirmDialogListItem(long id, String msg)  {
        FragmentManager childFragmentManager = getChildFragmentManager();
        //TODO Из фрагмента можно запускать другие фрагменты как через SupportFragManager (От хост активности?)
        //TODO Так и через childFragManager

        DeleteDialog dialog = DeleteDialog
                .newInstance(
                        id,
                        "",
                        msg, //TODO Put rss title
                        getString(android.R.string.ok),
                        getString(android.R.string.no),
                        CONFIRM_DIALOG_DELETE_FEED
                );
        dialog.show(childFragmentManager, CONFIRM_DIALOG_DELETE_FEED);
    }


    @Override
    public void confirm(long id, String dialogTag) {
        Timber.d("confirm");
        switch (dialogTag) {
            case CONFIRM_DIALOG_DELETE_FEED:
                Timber.d("CONFIRM_DIALOG_DELETE_FEED id: " + id);
                presenter.unSubscribeFromFeed(id);
                break;
            case CONFIRM_DIALOG_SHOW_INFO_FEED: //TODO Убрать отсюда.
                Timber.d("CONFIRM_DIALOG_SHOW_INFO_FEED");
                break;
        }
    }

    @OnClick(R.id.fab)
    public void onFabClick() {
        Timber.d("onFabClick");
        showNewFeedDialog();
        //presenter.showAddNewFeed();
    }

    //TODO Move dialog to Navigator
    private void showNewFeedDialog() {
        presenter.showAddNewFeed();
//        FragmentManager fm = getActivity().getSupportFragmentManager();
//        FeedNewDialog dialog = FeedNewDialog.newInstance();
//        dialog.setTargetFragment(this, 911);
//        dialog.show(fm, "TAG_DIALOG_SEARCH_RADIUS");
    }

    //TODO Change to interface callback?
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.getAllFeeds();
    }

    //TODO Для чего используется
    @Override
    public void onFeedChange(boolean b) {
        Timber.d("onFeedChange");
        presenter.getAllFeeds();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        Timber.d("onPrepareOptionsMenu");
        menu.getItem(0).setIcon(
                isBackgroundFeedUpdateEnabled
                        ? R.drawable.ic_active_notification
                        : R.drawable.ic_inactive_notification
        );
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Timber.d("onCreateOptionsMenu");
        //getActivity().invalidateOptionsMenu();
        //getActivity().onPrepareOptionsMenu()
        inflater.inflate(R.menu.menu_feed_list, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //TODO Менять icon
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.toggle_notifications:
                if (isBackgroundFeedUpdateEnabled) {
                    Timber.d("enableBackgroundFeedUpdates");
                    presenter.disableBackgroundFeedUpdates();
                } else {
                    Timber.d("disableBackgroundFeedUpdates");
                    presenter.enableBackgroundFeedUpdates();
                }
                return true;

            case R.id.show_favourites:
                Timber.d("showFavouriteArticles");
                presenter.showFavouriteArticles();
                return true;

            case R.id.show_settings:
                presenter.showSettings();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //---
    @Override
    public void showErrorMessage(String message) {
        ThemedSnackbar
                .makeErrorSnackbar(coordinator_top, message)
                .setAction(R.string.snackbar_action_button_retry, view -> {
                    presenter.updateAllFeedsNewArticlesCount();
                }).show();
    }

    @Override
    public void showMessage(String message) {
        Snackbar snackbar = Snackbar.make(coordinator_top, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    @Override
    public void showNewArticlesCountMessage(Integer count) {
        ThemedSnackbar
                .makeNewArticlesSnackbar(coordinator_top, getString(R.string.snackbar_text_updated_articles, count))
                .show();
    }

    @Override
    public void showFeedSubscriptions(List<Feed> feedSubscriptions) {
        feeds = feedSubscriptions;
        adapter.onFeedsUpdate(feedSubscriptions);
        adjustEmptyState(feedSubscriptions.isEmpty());

    }

    //TODO Вызывается множество раз во время синхро
    @Override
    public void showFeedObservableSubscriptions(ListDataDiffHolder<Feed> dataDiffHolder) {
        Timber.d("showFeedObservableSubscriptions " + dataDiffHolder.getList().size());
        feeds = dataDiffHolder.getList();
        adapter.updateData(dataDiffHolder.getList());
        //adapter.notifyDataSetChanged();

        if (dataDiffHolder.getDiffResult() == null)
            adapter.notifyDataSetChanged();
        else
            dataDiffHolder.getDiffResult().dispatchUpdatesTo(adapter);
        adjustEmptyState(dataDiffHolder.getList().isEmpty());
    }

    private void adjustEmptyState(final boolean isViewEmpty) {
        linearlayout_empty_list.setVisibility(isViewEmpty ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setIsBackgroundFeedUpdateEnabled(boolean isEnabled) {
        Timber.d("setIsBackgroundFeedUpdateEnabled " + isEnabled);
        isBackgroundFeedUpdateEnabled = isEnabled;
        getActivity().invalidateOptionsMenu(); //TODO
        //backgroundUpdatesNotification.setImageResource(isEnabled ? R.drawable.ic_active_notification : R.drawable.ic_inactive_notification);
    }

    @Override
    public void showProgress(boolean progress) {
        //swipeRefreshLayout_refresh_feeds.post(() -> swipeRefreshLayout_refresh_feeds.setRefreshing(progress));
//        if (progress)
//            loadProgressBar.setVisibility(View.VISIBLE);
//        else
//            loadProgressBar.setVisibility(View.GONE);
    }
}
