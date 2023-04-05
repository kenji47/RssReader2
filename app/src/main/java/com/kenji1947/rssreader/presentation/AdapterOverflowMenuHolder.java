package com.kenji1947.rssreader.presentation;

import android.view.View;

/**
 * Created by chamber on 12.12.2017.
 */

public class AdapterOverflowMenuHolder{
    private View anchor;
    private int adapterPos;

    public AdapterOverflowMenuHolder(View anchor,  int adapterPos) {
        this.anchor = anchor;
        this.adapterPos = adapterPos;
    }

    public View getAnchor() {
        return anchor;
    }
    public int getAdapterPos() {
        return adapterPos;
    }

}
