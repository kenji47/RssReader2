package com.kenji1947.rssreader.data.worker.image_loader;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.kenji1947.rssreader.data.util.CircleImageTransformation;

public final class ImageLoaderGlide implements ImageLoader {

    private final Context context;

    public ImageLoaderGlide(final Context context) {
        this.context = context;
    }

    @Override
    public void loadImage(final String url, final ImageView target, @DrawableRes final int placeholderDrawable, @DrawableRes final int errorDrawable) {
        Glide.with(context)
             .load(url)
             .placeholder(placeholderDrawable)
             .error(errorDrawable)
             .crossFade()
             .transform(new CircleImageTransformation(context))
                .fitCenter()
             .into(target);
    }
}
