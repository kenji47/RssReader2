package com.kenji1947.rssreader.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.kenji1947.rssreader.App;
import com.kenji1947.rssreader.R;
import com.kenji1947.rssreader.presentation.article_detail.ArticleDetailFragment;
import com.kenji1947.rssreader.presentation.article_list.ArticleListArgumentHolder;
import com.kenji1947.rssreader.presentation.article_list.ArticleListFragment;
import com.kenji1947.rssreader.presentation.common.BackButtonListener;
import com.kenji1947.rssreader.presentation.feed_list.FeedListFragment;
import com.kenji1947.rssreader.presentation.new_feed.FeedNewDialog;
import com.kenji1947.rssreader.presentation.new_feed.FeedNewFragment;
import com.kenji1947.rssreader.presentation.settings.SettingsFragment;

import javax.inject.Inject;

import ru.terrakok.cicerone.Navigator;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.android.SupportFragmentNavigator;
import ru.terrakok.cicerone.commands.Command;
import ru.terrakok.cicerone.commands.Forward;
import timber.log.Timber;

/**
 * Created by kenji1947 on 11.11.2017.
 */

public class MainActivity extends AppCompatActivity {
    private Navigator navigator;

    @Inject
    NavigatorHolder navigatorHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Timber.d("onCreate");

        setContentView(R.layout.activity_container);
        if (savedInstanceState == null) {
            replaceFragment(FeedListFragment.class, FeedListFragment.TAG);
        }

        App.INSTANCE.getAppComponent().inject(this);

        initNavigation();
    }

    private void initNavigation() {
        navigator = new SupportFragmentNavigator(getSupportFragmentManager(), R.id.frameLayout_container) {

            @Override
            protected void forward(Forward command) {
                DialogFragment dialogFragment = createDialog(command.getScreenKey(), command.getTransitionData());

                if (dialogFragment != null) {
                    dialogFragment.show(getSupportFragmentManager(), command.getScreenKey());
                } else
                    super.forward(command);
            }

            private DialogFragment createDialog(String screenKey, Object data) {
                switch (screenKey) {
                    case Screens.NEW_FEED_DIALOG: return FeedNewDialog.newInstance();
                    default: return null;
                }
            }

            @Override
            protected void setupFragmentTransactionAnimation(
                    Command command,
                    Fragment currentFragment,
                    Fragment nextFragment,
                    FragmentTransaction fragmentTransaction) {

                super.setupFragmentTransactionAnimation(command, currentFragment, nextFragment, fragmentTransaction);

                if (nextFragment instanceof FeedNewFragment) {
                    //TODO Белый экран при смене ориентации
                    //fragmentTransaction.setCustomAnimations(R.anim.fragment_fade_in, R.anim.fragment_fade_out);
                } else {
                    fragmentTransaction.setCustomAnimations(
                            //TODO FragmentTransaction.TRANSIT_FRAGMENT_FADE
                            R.anim.fragment_right_enter,
                            R.anim.fragment_left_exit,
                            R.anim.fragment_left_enter,
                            R.anim.fragment_right_exit
                    );
                }
            }

            @Override
            protected Fragment createFragment(String screenKey, Object data) {
                switch (screenKey) {
                    case Screens.FEED_LIST_SCREEN: return FeedListFragment.newInstance();

                    case Screens.NEW_FEED_SCREEN: return FeedNewFragment.newInstance();

                    case Screens.ARTICLE_LIST_SCREEN: {
                        ArticleListArgumentHolder holder = (ArticleListArgumentHolder) data;
                        return ArticleListFragment.newInstance(holder.id, holder.title);
                    }

                    case Screens.ARTICLE_FAV_LIST_SCREEN: return ArticleListFragment.newInstanceFavMode();

                    case Screens.ARTICLE_DETAIL_SCREEN: return ArticleDetailFragment.newInstance((Long) data);

                    case Screens.SETTINGS_SCREEN: return SettingsFragment.newInstance();

                    case Screens.NEW_FEED_DIALOG: return FeedNewDialog.newInstance();
                }
                return null;
            }

            @Override
            protected void showSystemMessage(String message) {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            protected void exit() {
                finish();
            }
        };
    }

    @Override
    protected void onResume() {
        Timber.d("onResume");
        super.onResume();
        navigatorHolder.setNavigator(navigator);
    }

    @Override
    protected void onPause() {
        Timber.d("onPause");
        navigatorHolder.removeNavigator();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Timber.d("onDestroy");
        super.onDestroy();
//        startActivity(new Intent(this, DummyActivity.class));
//        finish();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frameLayout_container);
        if (fragment != null
                && fragment instanceof BackButtonListener
                && ((BackButtonListener) fragment).onBackPressed()) {
            return;
        } else {
            super.onBackPressed();
        }
    }

    private void replaceFragment(Class clazz, String TAG) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameLayout_container, fragment, TAG).commit();

    }
}
