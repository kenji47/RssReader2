package com.kenji1947.rssreader.fakes;

import com.kenji1947.rssreader.domain.util.RxSchedulersProvider;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by chamber on 17.12.2017.
 */

public class SchedulersTrampoline implements RxSchedulersProvider {

    //TODO trampoline ведет себя странно. Виснет намертво

    @Override
    public Scheduler getMain() {

        //return Schedulers.trampoline();
        return AndroidSchedulers.mainThread();
    }

    @Override
    public Scheduler getComputation() {
        return Schedulers.trampoline();
        //return AndroidSchedulers.mainThread();
    }

    @Override
    public Scheduler getTrampoline() {
        return Schedulers.trampoline();
        //return AndroidSchedulers.mainThread();
    }

    @Override
    public Scheduler getNewThread() {
        return Schedulers.trampoline();
        //return AndroidSchedulers.mainThread();
    }

    @Override
    public Scheduler getIo() {
        return Schedulers.trampoline();
        //return AndroidSchedulers.mainThread();
    }
}
