package com.grishberg.mviexample.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.grishberg.mviexample.R;
import com.grishberg.mvpstatelibrary.framework.ui.BaseMvpActivity;
import com.grishberg.mviexample.mvp.presenters.FirstScreenPresenter;
import com.grishberg.mviexample.mvp.state.presenter.FirstPresenterStateModel;
import com.grishberg.mviexample.mvp.state.view.FirstViewStateModel;
import com.grishberg.mviexample.mvp.view.FirstView;

import java.util.Locale;

public class MainActivity extends BaseMvpActivity<FirstScreenPresenter, FirstViewStateModel>
        implements FirstView, View.OnClickListener {

    private ProgressBar progressBar;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private TextView countTextView;
    private Button buttonStart;
    private Button buttonSecondStep;
    private Button buttonThirdStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        countTextView = (TextView) findViewById(R.id.countTextView);
        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(this);

        buttonSecondStep = (Button) findViewById(R.id.buttonSecondStep);
        buttonSecondStep.setOnClickListener(this);

        buttonThirdStep = (Button) findViewById(R.id.buttonThirdStep);
        buttonThirdStep.setOnClickListener(this);
    }

    @Override
    protected FirstScreenPresenter createPresenter() {
        return new FirstScreenPresenter();
    }

    @Override
    public void updateView(final FirstViewStateModel viewStateModel) {
        buttonStart.setEnabled(false);
        buttonSecondStep.setEnabled(false);
        if (viewStateModel.isError()) {
            buttonStart.setEnabled(true);
            showError();
            return;
        }

        progressBar.setVisibility(viewStateModel.isProgress() ? View.VISIBLE : View.GONE);

        if (viewStateModel.isProgress()) {
            return;
        }
        titleTextView.setText(viewStateModel.getTitle());
        descriptionTextView.setText(viewStateModel.getDescription());
        countTextView.setText(String.format(Locale.US, "%d", viewStateModel.getCount()));
        buttonSecondStep.setEnabled(true);
    }

    private void showError() {
        Toast.makeText(this, "Error!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonStart:
                getPresenter().updateState(FirstPresenterStateModel.makeClick());
                break;

            case R.id.buttonSecondStep:
                SecondActivity.start(this);
                break;

            default:
                ThirdActivity.start(this);
                break;
        }
    }
}
