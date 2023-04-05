package com.kenji1947.rssreader.presentation.common;

import ru.terrakok.cicerone.commands.Command;

/**
 * Created by chamber on 05.02.2018.
 */

public class ShowDialog implements Command {
    private String screenKey;
    private Object transitionData;

    public ShowDialog(String screenKey, Object transitionData) {
        this.screenKey = screenKey;
        this.transitionData = transitionData;
    }

    public String getScreenKey() {
        return screenKey;
    }

    public Object getTransitionData() {
        return transitionData;
    }
}
