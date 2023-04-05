package com.kenji1947.rssreader.presentation.common;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.kenji1947.rssreader.R;

import timber.log.Timber;

/**
 * Created by chamber on 12.02.2018.
 */

public class ThemedSnackbar {

    public static Snackbar makeErrorSnackbar(View view, String message) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(20); //TODO

        snackbar.setActionTextColor(Color.WHITE);

        snackbar.getView().setBackgroundResource(R.color.material_red_400);

        return snackbar;
    }
    public static Snackbar makeNewArticlesSnackbar(View view, String message) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_SHORT);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(20); //TODO

        snackbar.setActionTextColor(Color.WHITE);

        snackbar.getView().setBackgroundResource(R.color.colorPrimary);

        return snackbar;
    }



    public static Snackbar make(View view, CharSequence text, int duration) {
        Snackbar snackbar = Snackbar.make(view, text, duration);
        snackbar.getView().setBackgroundColor(getAttribute(view.getContext(), R.attr.colorAccent));
        return snackbar;
    }


    public static Snackbar make(View view, int resId, int duration) {
        return make(view, view.getResources().getText(resId), duration);
    }


    private static int getAttribute(Context context, int resId) {
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(resId, typedValue, true);
        return typedValue.data;
    }
}
