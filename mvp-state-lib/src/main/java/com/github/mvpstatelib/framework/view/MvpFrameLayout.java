package com.github.mvpstatelib.framework.view;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.github.mvpstatelib.framework.lifecycle.LifeCycleObservable;
import com.github.mvpstatelib.framework.presenter.BaseMvpPresenter;
import com.github.mvpstatelib.framework.state.StateObserver;
import com.github.mvpstatelib.framework.state.ViewState;

/**
 * Created by grishberg on 26.01.17.
 */
public abstract class MvpFrameLayout<P extends BaseMvpPresenter>
        extends LinearLayout implements StateObserver<ViewState>, DelegateTagHolder<ViewState> {

    private MvpHelper<P> helper;

    public MvpFrameLayout(Context context) {
        this(context, null);
    }

    public MvpFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MvpFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        helper = new MvpHelper<>(this);
        if (helper.getPresenter() == null) {
            helper.setPresenter(providePresenter());
        }
    }

    protected abstract P providePresenter();

    public void registerNestedView(final LifeCycleObservable parent, Bundle savedInstanceState) {
        helper.registerNestedView(parent, savedInstanceState);
    }

    protected P getPresenter() {
        return helper.getPresenter();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        helper.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        helper.onDetachedFromWindow();
    }

    @Override
    public void onStateUpdated(ViewState model) {
    }
}
