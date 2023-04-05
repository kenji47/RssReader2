package com.kenji1947.rssreader.presentation.common;

import android.support.v7.util.DiffUtil;

import com.kenji1947.rssreader.domain.entities.Feed;
import com.kenji1947.rssreader.presentation.common.diff_calc.FeedListDiffCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chamber on 07.02.2018.
 */

public class DiffCalculator {
    public static ListDataDiffHolder<Feed> calculateFeedListDiff(List<Feed> newList, List<Feed> oldList) {
        return calculate(newList, oldList, new FeedListDiffCallback(newList, oldList));
    }

    private static <T> ListDataDiffHolder<T> calculate(List<T> newList, List<T> oldList, DiffUtil.Callback callback) {
        if (newList == null)
            throw new RuntimeException("New list can not be null");
        if (oldList == null)
            return new ListDataDiffHolder<T>(newList, null);

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);
        return new ListDataDiffHolder<T>(newList, diffResult);
    }
}
