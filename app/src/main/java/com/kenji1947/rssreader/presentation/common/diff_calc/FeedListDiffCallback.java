package com.kenji1947.rssreader.presentation.common.diff_calc;

import android.support.v7.util.DiffUtil;

import com.kenji1947.rssreader.domain.entities.Feed;

import java.util.List;

/**
 * Created by chamber on 07.02.2018.
 */

public class FeedListDiffCallback extends DiffUtil.Callback {
    private List<Feed> newList;
    private List<Feed> oldList;

    public FeedListDiffCallback(List<Feed> newList, List<Feed> oldList) {
        this.newList = newList;
        this.oldList = oldList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Feed oldItem = oldList.get(oldItemPosition);
        Feed newItem = newList.get(newItemPosition);
        return oldItem.id == newItem.id;
    }

    //TODO
    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }
}
