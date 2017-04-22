package com.github.mvpstatelib.framework.state;

/**
 * Created by grishberg on 22.04.17.
 */

public abstract class AbsMvpState implements MvpState {
    public void beforeStateReceived() {
        //to be implemented in subclass
    }

    public void afterStateReceived() {
        //to be implemented in subclass
    }

    public boolean isNeedToSaveState() {
        return true;
    }
}
