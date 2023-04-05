package com.kenji1947.rssreader.presentation.article_detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.kenji1947.rssreader.App;
import com.kenji1947.rssreader.R;
import com.kenji1947.rssreader.di.presenter.ArticleDetailPresenterComponent;
import com.kenji1947.rssreader.domain.entities.Article;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by chamber on 15.12.2017.
 */

public class ArticleDetailFragment extends MvpAppCompatFragment implements ArticleDetailView{
    public static final String TAG = ArticleDetailFragment.class.getSimpleName();
    private static final String ARG_ARTICLE_CONTENT_URL = "article_content_url";
    private static final String ARG_ARTICLE_ID = "article_id";

    @BindView(R.id.webView_article_content) WebView webView_article_content;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.button_visit_website) Button button_visit_website;

    private Unbinder unbinder;

    private Article article;

    @InjectPresenter
    ArticleDetailPresenter presenter;
    @ProvidePresenter
    ArticleDetailPresenter providePresenter() {
        return ArticleDetailPresenterComponent.Initializer.init(App.INSTANCE.getAppComponent(),
                getArguments().getLong(ARG_ARTICLE_ID)
        ).providePresenter();
    }

    public static ArticleDetailFragment newInstance(final String articleContentUrl) {
        final ArticleDetailFragment fragment = new ArticleDetailFragment();
        final Bundle arguments = new Bundle();
        arguments.putString(ARG_ARTICLE_CONTENT_URL, articleContentUrl);
        fragment.setArguments(arguments);
        return fragment;
    }

    public static ArticleDetailFragment newInstance(long feedId) {
        final ArticleDetailFragment fragment = new ArticleDetailFragment();
        final Bundle arguments = new Bundle();
        arguments.putLong(ARG_ARTICLE_ID, feedId);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_article_detail2, container, false);
        unbinder = ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar();
        initButtonVisitWebsite();
        //extractArguments(getArguments());
    }

    private void initButtonVisitWebsite() {
        button_visit_website.setOnClickListener(view -> {
            //TODO Доработать
            //https://stackoverflow.com/questions/3004515/sending-an-intent-to-browser-to-open-specific-url
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(article.link));
            startActivity(i);
        });
    }

    private void initToolbar() {
        toolbar.setTitle("");
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void setupWebView() {
//        settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
//        webView_article_content.setPadding(0, 0, 0, 0);
//        webView_article_content.setInitialScale(100);
//
//
//        webView_article_content.addJavascriptInterface(this, "");
//
//        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        settings.setLoadWithOverviewMode(true);
//        settings.setUseWideViewPort(true);
//        webView_article_content.loadUrl(url);
    }

    private String decodeFeedlyFeed(String html) {

//        html = html.replaceAll("\"", "\"");
//        html = html.replaceAll("\n", "");
        try {
            html = URLDecoder.decode(html, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //html = Html.fromHtml(html).toString(); Removes images
        return html;
    }


    @JavascriptInterface
    public void resize(final float height) {
        ArticleDetailFragment.this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView_article_content.setLayoutParams(new LinearLayout.LayoutParams(getResources()
                        .getDisplayMetrics().widthPixels, (int) (height * getResources().getDisplayMetrics().density)));
            }
        });
    }

    @Override
    public void setArticle(Article article) {
        this.article = article;
        WebSettings settings = webView_article_content.getSettings();
        webView_article_content.getSettings().setJavaScriptEnabled(true);
        webView_article_content
                .loadData(article.content,"text/html; charset=utf-8", "utf-8");
        settings.setJavaScriptEnabled(true);

        toolbar.setTitle(article.title);
    }
}
