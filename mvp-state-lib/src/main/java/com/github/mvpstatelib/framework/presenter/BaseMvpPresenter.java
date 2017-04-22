package com.github.mvpstatelib.framework.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.github.mvpstatelib.framework.state.ModelWithNonSerializable;
import com.github.mvpstatelib.framework.state.MvpState;
import com.github.mvpstatelib.framework.state.StateObserver;
import com.github.mvpstatelib.framework.state.StateReceiver;

import java.util.HashSet;

/**
 * Created by grishberg on 22.01.17.
 */
public abstract class BaseMvpPresenter
        implements StateReceiver<MvpState> {
    private static final String VIEW_STATE_SUFFIX = ":VIEW_STATE";
    private static final String PRESENTER_STATE_SUFFIX = ":PRESENTER_STATE";
    final HashSet<StateObserver<MvpState>> observers = new HashSet<>();

    @Nullable
    private MvpState viewState;

    @Nullable
    private MvpState presenterState;

    protected void updateViewState(@Nullable MvpState viewState) {
        this.viewState = viewState;

        notifyObservers();
    }

    private void notifyObservers() {
        if (viewState != null) {
            viewState.beforeStateReceived();
        }
        for (StateObserver<MvpState> observer : observers) {
            observer.onModelUpdated(viewState);
        }
        if (viewState != null) {
            viewState.afterStateReceived();
        }
    }

    public void subscribe(final StateObserver<MvpState> stateObserver) {
        if (observers.add(stateObserver) && viewState != null) {
            if (viewState instanceof ModelWithNonSerializable &&
                    ((ModelWithNonSerializable) viewState).isNonSerializableNull()) {
                onNonSerializableEmpty(viewState);
                return;
            }
            stateObserver.onModelUpdated(viewState);
        }
        onSubscribed();
    }

    protected void onSubscribed() {
        //to be overridden in subclass
    }

    public void unSubscribe(final StateObserver<MvpState> stateObserver) {
        observers.remove(stateObserver);
        onUnsubscribed();
    }

    protected void onUnsubscribed() {
        //to be overridden in subclass
    }

    public void restoreState(@Nullable Bundle savedInstanceState) {
        if (viewState == null) {
            viewState = restoreViewState(savedInstanceState);
        }
        if (presenterState == null) {
            final MvpState restoredState = restorePresenterState(savedInstanceState);
            if (restoredState != null) {
                updateState(restoredState);
            }
        }
    }

    protected void onNonSerializableEmpty(MvpState viewState) {
        //To be overriden in subclass
    }

    private MvpState restoreViewState(@Nullable final Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return null;
        }
        return (MvpState) savedInstanceState.getSerializable(this.getClass().getName() + VIEW_STATE_SUFFIX);
    }

    private MvpState restorePresenterState(@Nullable final Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return null;
        }
        return (MvpState) savedInstanceState.getSerializable(this.getClass().getName() + PRESENTER_STATE_SUFFIX);
    }

    public void saveInstanceState(final Bundle savedInstanceState) {
        if (viewState != null) {
            savedInstanceState.putSerializable(this.getClass().getName() + VIEW_STATE_SUFFIX, viewState);
            savedInstanceState.putSerializable(this.getClass().getName() + PRESENTER_STATE_SUFFIX, presenterState);
        }
    }

    public void onDestroy() {
    }

    @Override
    public void updateState(final MvpState presenterState) {
        if (presenterState != null && presenterState.isNeedToSaveState()) {
            this.presenterState = presenterState;
        }
        onStateUpdated(presenterState);
    }

    protected void onStateUpdated(final MvpState presenterState) {
        //to be overridden in subclass
    }
}
