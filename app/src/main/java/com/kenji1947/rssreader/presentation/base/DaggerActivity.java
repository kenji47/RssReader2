package com.kenji1947.rssreader.presentation.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.kenji1947.rssreader.di.activity.ActivityComponent;


public abstract class DaggerActivity extends MvpAppCompatActivity {

    private ActivityComponent activityComponent;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inject(getActivityComponent());
    }

    public ActivityComponent getActivityComponent() {
        if (activityComponent == null) {
            //activityComponent = ComponentFactory.createActivityComponent(this, getReedlyApplication());
        }
        return activityComponent;
    }

//    private ReedlyApplication getReedlyApplication() {
//        return ((ReedlyApplication) getApplication());
//    }

    protected abstract void inject(final ActivityComponent activityComponent);
}
