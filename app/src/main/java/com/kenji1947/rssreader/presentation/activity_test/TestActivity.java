package com.kenji1947.rssreader.presentation.activity_test;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.kenji1947.rssreader.R;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

/**
 * Created by chamber on 15.12.2017.
 */

public class TestActivity extends MvpAppCompatActivity implements TestActivityView{
    @BindView(R.id.button_check_equality)
    Button button_check_equality;

    @BindView(R.id.button_check_fragman_equality)
    Button button_check_fragman_equality;

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.button_next)
    Button button_next;

    @BindView(R.id.button_finish)
    Button button_finish;

    @InjectPresenter
    TestActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String uuid = UUID.randomUUID().toString();
        Timber.d("onCreate " + uuid);

        setContentView(R.layout.activity_test_container);

        ButterKnife.bind(this);
        textView.setText(uuid);
        presenter.setActivity(this);
    }

    @OnClick(R.id.button_check_equality)
    void check() {
        presenter.checkActivityEquality();
    }
    @OnClick(R.id.button_check_fragman_equality)
    void checkFM() {
        presenter.checkFragmentManagerEquality();
    }

    @OnClick(R.id.button_next)
    void next() {
        //presenter.next();
        presenter.nextFragment();
    }
    @OnClick(R.id.button_finish)
    void finishActivity() {
        presenter.finishActivity();
    }
    @Override
    public void next(TestActivity source) {
        startActivity(new Intent(source, SecondActivity.class));
    };
}
