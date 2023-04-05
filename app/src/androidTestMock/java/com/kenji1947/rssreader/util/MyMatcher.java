package com.kenji1947.rssreader.util;

import android.content.res.Resources;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.BoundedMatcher;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kenji1947.rssreader.domain.entities.Article;
import com.kenji1947.rssreader.domain.entities.Feed;
import com.kenji1947.rssreader.presentation.article_list.ArticleListAdapter;
import com.kenji1947.rssreader.presentation.feed_list.FeedListAdapter;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.concurrent.TimeUnit;

import timber.log.Timber;

/**
 * Created by chamber on 20.12.2017.
 */

public class MyMatcher {


    //TODO Найти холдер, вью которого содержат указанные данные.
    //Здесь происходит неявная проверка через действие: если указанный холдер не найден - действие невозможно
    public static Matcher<RecyclerView.ViewHolder> holderFeedListWithFeed(Feed feed) {

        return new BoundedMatcher <RecyclerView.ViewHolder, FeedListAdapter.FeedViewHolder>(
                FeedListAdapter.FeedViewHolder.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("ViewHolder found with feed url: " + feed.url);
            }

            @Override
            protected boolean matchesSafely(FeedListAdapter.FeedViewHolder item) {
                String title = item.feedTitle.getText().toString();
                String description = item.feedDescription.getText().toString();

                return title.equals(feed.title) && description.equals(feed.description);
            }
        };
    }


    public static Matcher<RecyclerView.ViewHolder> holderArticleListWithArticle(Article article) {

        return new BoundedMatcher <RecyclerView.ViewHolder, ArticleListAdapter.ArticleHolder>(
                ArticleListAdapter.ArticleHolder.class) {

            @Override
            public void describeTo(Description description) {
                description.appendText("ViewHolder found with article title: " + article.title);
            }

            @Override
            protected boolean matchesSafely(ArticleListAdapter.ArticleHolder item) {
                String titleActual = item.textView_article_title.getText().toString();
                String dateActual = item.textView_article_date.getText().toString();

                //TODO Получить ресурс
                item.imageView_article_favourite_indicator.getDrawable();


                isNewIndicatorVisible(item.textView_article_new_indicator.getVisibility());

                return titleActual.equals(article.title)
                        && dateActual.equals(article.publicationDate + "")
                        && isNewIndicatorVisible(item.textView_article_new_indicator.getVisibility()) == article.isNew;
            }

            private boolean isNewIndicatorVisible(int visibility) {
                if (visibility == View.VISIBLE)
                    return true;
                return false;
            }
        };
    }

    //TODO Кастомный матчер обернутый в класс. Поиск view внутри RecyclerView для совершения действия
    public static RecyclerViewFindView withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewFindView(recyclerViewId);
    }

    public static class RecyclerViewFindView {
        private int recyclerId;

        public RecyclerViewFindView(int recyclerId) {
            this.recyclerId = recyclerId;
        }
        //Find view with id in holder at pos
        public Matcher<View> atPositionWithId(final int position, final int targetViewId) {

            return new TypeSafeMatcher<View>() {
                Resources resources = null;
                View childView;

                public void describeTo(Description description) {
                    int id = targetViewId == -1 ? recyclerId : targetViewId;
                    String idDescription = Integer.toString(id);
                    if (this.resources != null) {
                        try {
                            idDescription = this.resources.getResourceName(id);
                        } catch (Resources.NotFoundException var4) {
                            idDescription = String.format("%s (resource name not found)", id);
                        }
                    }

                    description.appendText("with id: " + idDescription);
                }

                public boolean matchesSafely(View view) {

                    this.resources = view.getResources();

                    if (childView == null) {
                        RecyclerView recyclerView =
                                (RecyclerView) view.getRootView().findViewById(recyclerId);
                        if (recyclerView != null) {

                            childView = recyclerView.findViewHolderForAdapterPosition(position).itemView;
                        }
                        else {
                            return false;
                        }
                    }

                    if (targetViewId == -1) {
                        return view == childView;
                    } else {
                        View targetView = childView.findViewById(targetViewId);
                        return view == targetView;
                    }

                }
            };
        }
        //Find view with id in holder which contains view with text
        public Matcher<View> withTextWithId(final String text, final int textViewId, final int targetViewId) {

            return new TypeSafeMatcher<View>() {
                View childView; //Найденное вью

                @Override
                protected boolean matchesSafely(View view) {

                    if (childView == null) {
                        RecyclerView recyclerView = view.getRootView().findViewById(recyclerId);
                        if (recyclerView != null) {
                            Timber.d("RV getChildCount: " + recyclerView.getChildCount() + " thread " +
                            Thread.currentThread().getName());
                            Timber.d("RV getAdapter().getItemCount(): " + recyclerView.getAdapter().getItemCount());
                            for (int i = 0; i < recyclerView.getChildCount(); i++) {
                                Timber.d("withTextWithId " + i);
                                RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(i);
                                TextView textView = holder.itemView.findViewById(textViewId);
                                if (textView.getText().toString().equals(text)) {
                                    childView = holder.itemView.findViewById(targetViewId);
                                    break;
                                }
                            }
                        }
                    }

                   if (childView == view)
                       return true;
                    return false;
                }
                //TODO Refactor
                @Override
                public void describeTo(Description description) {
                    description.appendText("Not found");
                }
            };
        }
    }


    //TODO Вынесение методов из класса
    //--------------------------------------------------------------------
    public static Matcher<View> withTextWithId(final int recyclerId, final String text, final int textViewId, final int targetViewId) {

        return new TypeSafeMatcher<View>() {
            View childView; //Найденное вью

            @Override
            protected boolean matchesSafely(View view) {

                if (childView == null) {
                    RecyclerView recyclerView = view.getRootView().findViewById(recyclerId);
                    if (recyclerView != null) {
                        for (int i = 0; i < recyclerView.getAdapter().getItemCount(); i++) {
                            RecyclerView.ViewHolder holder = recyclerView.findViewHolderForAdapterPosition(i);
                            TextView textView = holder.itemView.findViewById(textViewId);
                            if (textView.getText().toString().equals(text)) {
                                childView = holder.itemView.findViewById(targetViewId);
                            }
                        }
                    }
                }

                if (childView == view)
                    return true;
                return false;
            }
            //TODO Refactor
            @Override
            public void describeTo(Description description) {
                description.appendText("Not found");
            }
        };
    }

    public static Matcher<View> atPositionWithId(final int recyclerId, final int position, final int targetViewId) {

        return new TypeSafeMatcher<View>() {
            Resources resources = null;
            View childView;

            public void describeTo(Description description) {
                int id = targetViewId == -1 ? recyclerId : targetViewId;
                String idDescription = Integer.toString(id);
                if (this.resources != null) {
                    try {
                        idDescription = this.resources.getResourceName(id);
                    } catch (Resources.NotFoundException var4) {
                        idDescription = String.format("%s (resource name not found)", id);
                    }
                }

                description.appendText("with id: " + idDescription);
            }

            public boolean matchesSafely(View view) {
                this.resources = view.getResources();

                if (childView == null) {
                    RecyclerView recyclerView =
                            (RecyclerView) view.getRootView().findViewById(recyclerId);
                    if (recyclerView != null) {

                        childView = recyclerView.findViewHolderForAdapterPosition(position).itemView;
                    }
                    else {
                        return false;
                    }
                }

                if (targetViewId == -1) {
                    return view == childView;
                } else {
                    View targetView = childView.findViewById(targetViewId);
                    return view == targetView;
                }

            }
        };
    }

//    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
//        return new RecyclerViewMatcher(recyclerViewId);
//    }
//    public static class RecyclerViewMatcher {
//        final int mRecyclerViewId;
//
//        public RecyclerViewMatcher(int recyclerViewId) {
//            this.mRecyclerViewId = recyclerViewId;
//        }
//
//        public Matcher<View> atPosition(final int position) {
//            return atPositionWithId(position, -1);
//        }
//
//        public Matcher<View> atPositionWithId(final int position, final int targetViewId) {
//
//            return new TypeSafeMatcher<View>() {
//                Resources resources = null;
//                View childView;
//
//                public void describeTo(Description description) {
//                    int id = targetViewId == -1 ? mRecyclerViewId : targetViewId;
//                    String idDescription = Integer.toString(id);
//                    if (this.resources != null) {
//                        try {
//                            idDescription = this.resources.getResourceName(id);
//                        } catch (Resources.NotFoundException var4) {
//                            idDescription = String.format("%s (resource name not found)", id);
//                        }
//                    }
//
//                    description.appendText("with id: " + idDescription);
//                }
//
//                public boolean matchesSafely(View view) {
//
//                    this.resources = view.getResources();
//
//                    if (childView == null) {
//                        RecyclerView recyclerView =
//                                (RecyclerView) view.getRootView().findViewById(mRecyclerViewId);
//                        if (recyclerView != null) {
//
//                            childView = recyclerView.findViewHolderForAdapterPosition(position).itemView;
//                        }
//                        else {
//                            return false;
//                        }
//                    }
//
//                    if (targetViewId == -1) {
//                        return view == childView;
//                    } else {
//                        View targetView = childView.findViewById(targetViewId);
//                        return view == targetView;
//                    }
//
//                }
//            };
//        }
//    }
}
