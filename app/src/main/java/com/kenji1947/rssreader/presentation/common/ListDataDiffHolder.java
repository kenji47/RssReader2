package com.kenji1947.rssreader.presentation.common;

import android.support.v7.util.DiffUtil;
import android.util.Pair;

import com.kenji1947.rssreader.domain.entities.Feed;

import java.util.List;

/**
 * Created by chamber on 06.02.2018.
 */

public class ListDataDiffHolder<T> {
    private List<T> list;
    private DiffUtil.DiffResult diffResult;

    public ListDataDiffHolder(List<T> list) {
        this(list, null);
    }

    public ListDataDiffHolder(List<T> newList, DiffUtil.DiffResult diffResult) {
        this.list = newList;
        this.diffResult = diffResult;
    }

    public List<T> getList() {
        return list;
    }

    public DiffUtil.DiffResult getDiffResult() {
        return diffResult;
    }
}
