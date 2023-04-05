package com.kenji1947.rssreader.presentation.settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.arellomobile.mvp.presenter.ProvidePresenter;
import com.kenji1947.rssreader.App;
import com.kenji1947.rssreader.R;
import com.kenji1947.rssreader.di.presenter.SettingsPresenterComponent;
import com.kenji1947.rssreader.domain.entities.AppSettings;
import com.kenji1947.rssreader.presentation.common.RadioDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

/**
 * Created by chamber on 27.12.2017.
 */

public class SettingsFragment extends MvpAppCompatFragment implements SettingsView, RadioDialog.RadioDialogCallback{

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.switch_enable_background_updates) Switch switch_enable_background_updates;
    @BindView(R.id.textView_update_intertval_title) TextView textView_update_intertval_title;
    @BindView(R.id.textView_update_intertval) TextView textView_update_intertval;
    @BindView(R.id.linearLayout_interval) LinearLayout linearLayout_interval;

    private Unbinder unbinder;

    @InjectPresenter
    SettingsPresenter presenter;

    ArrayList<String> intervalPeriods = new ArrayList<>();

    @ProvidePresenter
    SettingsPresenter providePresenter() {
        return SettingsPresenterComponent
                .Initializer.init(App.INSTANCE.getAppComponent()).provideSettingsPresenter();
    }

    public static SettingsFragment newInstance() {
        Bundle args = new Bundle();
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intervalPeriods.add("One");
        intervalPeriods.add("Two");
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_settings, container, false);
        unbinder = ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar(getString(R.string.menu_settings));
        initSwitch();
        initIntervalDialog();
    }

    private void initSwitch() {
        switch_enable_background_updates.setOnCheckedChangeListener((compoundButton, b) -> {
            presenter.setFeedSyncSchedulerStatus(b);
            adjustUpdateIntervalAccess(b);
        });
    }

    private void initIntervalDialog() {
        linearLayout_interval.setOnClickListener(view -> {
            Timber.d("initIntervalDialog");

            FragmentManager childFragmentManager = getChildFragmentManager();

            RadioDialog dialog = RadioDialog.newInstance("Choose interval", 0, intervalPeriods);
            dialog.show(childFragmentManager, "Choose interval");

        });
//        button_show_dialog_get_interval.setOnClickListener(view -> {
//            presenter.setFeedSyncSchedulerInterval(System.currentTimeMillis());
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initToolbar(String title) {
        toolbar.setTitle(title);
        AppCompatActivity activity = (AppCompatActivity)getActivity();
        activity.setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true); //TODO
        toolbar.setNavigationOnClickListener(view -> presenter.onBackPressed());
    }

    @Override
    public void setAppSettings(AppSettings appSettings) {
        switch_enable_background_updates.setChecked(appSettings.isFeedSyncSchedulerEnabled());
        //button_show_dialog_get_interval.setText(appSettings.getFeedSyncSchedulerInterval() + "");
        textView_update_intertval.setText(appSettings.getFeedSyncSchedulerInterval() + "");
        adjustUpdateIntervalAccess(appSettings.isFeedSyncSchedulerEnabled());
    }

    private void adjustUpdateIntervalAccess(boolean isFeedSyncSchedulerEnabled) {
        textView_update_intertval.setEnabled(isFeedSyncSchedulerEnabled);
        textView_update_intertval_title.setEnabled(isFeedSyncSchedulerEnabled);
    }

    @Override
    public void choose(int pos) {
        Timber.d("choose " + pos + " " + intervalPeriods.get(pos));
        //presenter.setFeedSyncSchedulerInterval();
    }
}
