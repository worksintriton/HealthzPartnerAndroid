package com.triton.healthzpartners.activity;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.triton.healthzpartners.R;
import com.triton.healthzpartners.activity.WelcomeScreenActivity;
import com.triton.healthzpartners.adapter.ViewPagerSliderBeforeSplashAdapter;
import com.triton.healthzpartners.api.APIClient;
import com.triton.healthzpartners.api.RestApiInterface;
import com.triton.healthzpartners.responsepojo.SplashScreenResponse;
import com.triton.healthzpartners.utils.ConnectionDetector;
import com.triton.healthzpartners.utils.RestUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SliderAfterSplashActivity extends AppCompatActivity implements View.OnClickListener {


    private String TAG = "SliderAfterSplashActivity";

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.avi_indicator)
    AVLoadingIndicatorView avi_indicator;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.pager)
    ViewPager viewPager;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tabDots)
    TabLayout tabLayout;

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;


    List<SplashScreenResponse.DataBean> dataBeanList;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_skip)
    Button btn_skip;

    String fromactivity;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_no_records)
    TextView tv_norecords;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider_after_splash);

        ButterKnife.bind(this);
        Log.w(TAG, "onCreate");


        btn_skip.setVisibility(View.GONE);

        avi_indicator.setVisibility(View.GONE);

        viewPager.setVisibility(View.GONE);

        tabLayout.setVisibility(View.GONE);

        tv_norecords.setVisibility(View.GONE);

        btn_skip.setOnClickListener(this);

        if (new ConnectionDetector(SliderAfterSplashActivity.this).isNetworkAvailable(SliderAfterSplashActivity.this)) {

            splashScreenResponseCall();
        }



    }

    @SuppressLint("LogNotTimber")
    public void splashScreenResponseCall(){
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        //Creating an object of our api interface
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<SplashScreenResponse> call = apiInterface.splashScreenResponseCall(RestUtils.getContentType());
        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<SplashScreenResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<SplashScreenResponse> call, @NonNull Response<SplashScreenResponse> response) {
                avi_indicator.smoothToHide();


                if (response.body() != null) {
                    if(200 == response.body().getCode()){
                        Log.w(TAG,"SplashScreenResponse" + new Gson().toJson(response.body()));

                        if(response.body().getData() != null){
                            dataBeanList = response.body().getData();
                        }


                        if(dataBeanList != null && dataBeanList.size()>0){

                            btn_skip.setVisibility(View.VISIBLE);


                            viewPager.setVisibility(View.VISIBLE);

                            tabLayout.setVisibility(View.VISIBLE);

                            tv_norecords.setVisibility(View.GONE);

                            viewpageData(dataBeanList);
                        }

                        else {

                            btn_skip.setVisibility(View.GONE);

                            viewPager.setVisibility(View.GONE);

                            tabLayout.setVisibility(View.GONE);

                            tv_norecords.setVisibility(View.VISIBLE);

                            tv_norecords.setText("No Images Found");

                        }

                    }


                }

            }


            @Override
            public void onFailure(@NonNull Call<SplashScreenResponse> call,@NonNull  Throwable t) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"SplashScreenResponse flr"+t.getMessage());
            }
        });

    }

    private void viewpageData(List<SplashScreenResponse.DataBean> dataBeanList) {
        tabLayout.setupWithViewPager(viewPager, true);

        ViewPagerSliderBeforeSplashAdapter viewPagerSliderBeforeSplashAdapter = new ViewPagerSliderBeforeSplashAdapter(getApplicationContext(), dataBeanList);
        viewPager.setAdapter(viewPagerSliderBeforeSplashAdapter);
        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update =  new Runnable() {
            public void run() {
                if (currentPage == dataBeanList.size()) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, false);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_skip) {
            gotoLoginActivity();
        }
    }

    private void gotoLoginActivity() {

        Intent intent = new Intent(SliderAfterSplashActivity.this, WelcomeScreenActivity.class);
        intent.putExtra("fromactivity", TAG);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        // super.onBackPressed(); commented this line in order to disable back press
        //Write your code here
        //Toast.makeText(getApplicationContext(), "Back press disabled!", Toast.LENGTH_SHORT).show();
    }
}