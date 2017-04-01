package com.grishberg.mviexample.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.grishberg.mviexample.R;
import com.grishberg.mvpstatelibrary.framework.state.MvpState;
import com.grishberg.mvpstatelibrary.framework.ui.BaseMvpActivity;
import com.grishberg.mviexample.mvp.presenters.SecondScreenPresenter;
import com.grishberg.mviexample.mvp.state.presenter.SecondPresenterStateModel;
import com.grishberg.mviexample.mvp.state.view.SecondViewStateModel;

import java.util.List;
import java.util.Locale;

public class SecondActivity extends BaseMvpActivity<SecondScreenPresenter> {

    private ProgressBar progressBar;
    private TextView resultTextView;
    private List<String> values;

    public static void start(final Context context) {
        context.startActivity(new Intent(context, SecondActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_second);
        initWidgets();
        initButtonHandlers();
    }

    private void initWidgets() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        resultTextView = (TextView) findViewById(R.id.resultTextView);
    }

    private void initButtonHandlers() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(view ->
                getPresenter().updateState(SecondPresenterStateModel.makeFromClick())
        );
    }

    @Override
    protected SecondScreenPresenter createPresenter() {
        return new SecondScreenPresenter();
    }

    @Override
    public void onModelUpdated(final MvpState state) {
        if (!(state instanceof SecondViewStateModel)) {
            return;
        }
        SecondViewStateModel viewStateModel = (SecondViewStateModel) state;
        if (viewStateModel.isError()) {
            showError();
            return;
        }

        progressBar.setVisibility(viewStateModel.isProgress() ? View.VISIBLE : View.GONE);

        if (viewStateModel.isProgress()) {
            return;
        }
        // update values for result
        this.values = viewStateModel.getValues();
        resultTextView.setText(String.format(Locale.US, "%d", values.size()));
    }

    private void showError() {
        Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
    }
}