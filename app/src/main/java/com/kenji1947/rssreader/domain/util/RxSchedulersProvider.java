package com.kenji1947.rssreader.domain.util;

import io.reactivex.Scheduler;

/**
 * Created by kenji1947 on 07.11.2017.
 */

//TODO Rename Rx
public interface RxSchedulersProvider {
    Scheduler getMain();
    Scheduler getComputation();
    Scheduler getTrampoline();
    Scheduler getNewThread();
    Scheduler getIo();
}
