package com.kenji1947.rssreader.presentation.new_feed;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatDialogFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.kenji1947.rssreader.App;
import com.kenji1947.rssreader.R;
import com.kenji1947.rssreader.di.presenter.FeedNewPresenterComponent;
import com.kenji1947.rssreader.domain.entities.Feed;
import com.kenji1947.rssreader.domain.entities.SearchedFeed;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * Created by chamber on 01.02.2018.
 */

public class FeedNewDialog extends MvpAppCompatDialogFragment implements FeedNewView {

    @BindView(R.id.textInputEditText_feed_url) TextInputEditText textInputEditText_feed_url;
    @BindView(R.id.textView_error_message) TextView textView_error_message;
    @BindView(R.id.progressBar_loading_indicator) ProgressBar progressBar_loading_indicator;
    private Button positiveButton;

    private Unbinder unbinder;

    public static FeedNewDialog newInstance() {
        Bundle args = new Bundle();
        FeedNewDialog fragment = new FeedNewDialog();
        fragment.setArguments(args);
        return fragment;
    }
    @InjectPresenter
    FeedNewPresenter presenter;

    @ProvidePresenter
    FeedNewPresenter providePresenter() {
        return FeedNewPresenterComponent.Initializer.init(App.INSTANCE.getAppComponent()).provideFeedNewPresenter();
    }

    @Override
    public void onStart() {
        super.onStart();
        unbinder = ButterKnife.bind(this, getDialog());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_fragment_new, null);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.dialog_new_feed_title)
                .setPositiveButton(R.string.dialog_new_feed_button_positive, null)
                .show();

        positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(view1 -> onAddButtonPress());

        return alertDialog;
    }

    private void onAddButtonPress() {
        Timber.d("onAddButtonPress");
        //presenter.searchFeeds("verge");
        //presenter.addNewFeed(textInputEditText_feed_url.getText().toString());
    }


    @Override
    public void showProgress(boolean progress) {
        Timber.d("showProgress " + progress);
        textView_error_message.setVisibility(progress ? View.GONE : View.VISIBLE);
        progressBar_loading_indicator.setVisibility(progress ? View.VISIBLE : View.GONE);
        positiveButton.setEnabled(!progress);
    }

    @Override
    public void showSearchFeedLoadingDialog() {

    }

    @Override
    public void showSubscribeFeedLoadingDialog(String feedTitle) {

    }

    @Override
    public void hideLoadingDialog() {

    }

    @Override
    public void showMessage(String message) {
        Timber.d("showMessage " + message);
        textView_error_message.setText(message);
    }

    @Override
    public void showErrorMessage(String message) {

    }

    @Override
    public void setSearchedFeeds(List<SearchedFeed> feeds) {

    }

    @Override
    public void showKeyboard(boolean isShowing) {

    }



//    @Override
//    public void closeDialog() {
//        if (getTargetFragment() == null)
//            return;
//        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, null);
//        dismiss();
//    }

}
