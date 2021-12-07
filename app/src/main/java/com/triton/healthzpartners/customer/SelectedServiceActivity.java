package com.triton.healthzpartners.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.triton.healthzpartners.R;
import com.triton.healthzpartners.activity.NotificationActivity;
import com.triton.healthzpartners.adapter.SelectedServiceProviderAdapter;
import com.triton.healthzpartners.adapter.ViewPagerPetServiceAdapter;
import com.triton.healthzpartners.api.APIClient;
import com.triton.healthzpartners.api.RestApiInterface;
import com.triton.healthzpartners.requestpojo.SPSpecificServiceDetailsRequest;
import com.triton.healthzpartners.responsepojo.SPSpecificServiceDetailsResponse;
import com.triton.healthzpartners.serviceprovider.SPFiltersActivity;
import com.triton.healthzpartners.sessionmanager.SessionManager;
import com.triton.healthzpartners.utils.ConnectionDetector;
import com.triton.healthzpartners.utils.RestUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectedServiceActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {


    private String TAG = "SelectedServiceActivity";

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.avi_indicator)
    AVLoadingIndicatorView avi_indicator;

  /*  @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_selectedserviceimage)
    ImageView img_selectedserviceimage;*/

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_selected_service)
    TextView txt_selected_service;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_nearbyservices)
    RecyclerView rv_nearbyservices;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_no_records)
    TextView txt_no_records;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.include_petlover_header)
    View include_petlover_header;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rl_filters)
    RelativeLayout rl_filters;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.scrollablContent)
    NestedScrollView scrollablContent;


    private ShimmerFrameLayout mShimmerViewContainer;
    private View includelayout;

    private List<SPSpecificServiceDetailsResponse.DataBean.ServiceProviderBean> serviceProviderList;
    private String userid;
    private String catid = "";
    private String from;
    private Dialog dialog;
    private static final int REQUEST_PHONE_CALL =1 ;
    private String sosPhonenumber;

    private Dialog alertDialog;

    private String selectedprice;
    private int distance = 0;
    private int reviewcount = 0;
    private int Count_value_start = 0;
    private int Count_value_end = 0;
    private String fromactivity;

    // BottomSheetBehavior variable
    @SuppressWarnings("rawtypes")
    public BottomSheetBehavior bottomSheetBehavior;

    int currentPage = 0;

/*
    *//**//*
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refresh_layout;*/

    /**/
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.headerView)
    LinearLayout headerView;

    /* Petlover Bottom Navigation */

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigation;

    public static String active_tag = "3";


    @SuppressLint("LogNotTimber")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_service);

        ButterKnife.bind(this);
        Log.w(TAG,"onCreate");


        avi_indicator.setVisibility(View.GONE);

        headerView.setVisibility(View.GONE);

        scrollablContent.setVisibility(View.GONE);


        includelayout = findViewById(R.id.includelayout);
        mShimmerViewContainer = includelayout.findViewById(R.id.shimmer_layout);



        ImageView img_back = include_petlover_header.findViewById(R.id.img_back);
        ImageView img_sos = include_petlover_header.findViewById(R.id.img_sos);
        ImageView img_notification = include_petlover_header.findViewById(R.id.img_notification);
        ImageView img_cart = include_petlover_header.findViewById(R.id.img_cart);
        ImageView img_profile = include_petlover_header.findViewById(R.id.img_profile);
        TextView toolbar_title = include_petlover_header.findViewById(R.id.toolbar_title);
        img_sos.setVisibility(View.GONE);
        img_cart.setVisibility(View.GONE);
        toolbar_title.setText(getResources().getString(R.string.service_details));










        SessionManager session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getProfileDetails();
        userid = user.get(SessionManager.KEY_ID);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            catid = extras.getString("catid");
            from = extras.getString("from");
            fromactivity = extras.getString("fromactivity");
            reviewcount = extras.getInt("reviewcount");
            Count_value_start = extras.getInt("Count_value_start");
            Count_value_end = extras.getInt("Count_value_end");
            distance = extras.getInt("distance");
            selectedprice = extras.getString("selectedprice");


           }
        Log.w(TAG,"selectedprice : "+selectedprice+" distance  : "+distance);



        Log.w(TAG," userid : "+userid+ " catid : "+catid+" from : "+from);

        if(fromactivity != null && fromactivity.equalsIgnoreCase("SPFiltersActivity")) {
            if (new ConnectionDetector(SelectedServiceActivity.this).isNetworkAvailable(SelectedServiceActivity.this)) {
                SPSpecificServiceDetailsResponseCall(distance,reviewcount,Count_value_start,Count_value_end);
            }
        }else{
            if(catid != null && userid != null) {
                if (new ConnectionDetector(SelectedServiceActivity.this).isNetworkAvailable(SelectedServiceActivity.this)) {
                    SPSpecificServiceDetailsResponseCall(distance,reviewcount,Count_value_start,Count_value_end);
                }
            }
        }

   /*     refresh_layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(catid != null && userid != null) {
                    if (new ConnectionDetector(SelectedServiceActivity.this).isNetworkAvailable(SelectedServiceActivity.this)) {
                        SPSpecificServiceDetailsResponseCall(distance,reviewcount,Count_value_start,Count_value_end);
                    }
                }
            }
        });*/



        img_back.setOnClickListener(this);
        img_sos.setOnClickListener(this);
        img_notification.setOnClickListener(this);
        img_cart.setOnClickListener(this);
        img_profile.setOnClickListener(this);
        rl_filters.setOnClickListener(this);


        bottomNavigation.getMenu().getItem(0).setCheckable(false);


        floatingActionButton.setImageResource(R.drawable.ic_hzhome_png);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callDirections("1");
            }
        });


        if(active_tag != null){
            if(active_tag.equalsIgnoreCase("1")){
                bottomNavigation.setSelectedItemId(R.id.home);
            }else if(active_tag.equalsIgnoreCase("2")){
                bottomNavigation.setSelectedItemId(R.id.shop);
            }else if(active_tag.equalsIgnoreCase("3")){
                bottomNavigation.setSelectedItemId(R.id.services);
            }else if(active_tag.equalsIgnoreCase("4")){
                bottomNavigation.setSelectedItemId(R.id.care);
            } else if(active_tag.equalsIgnoreCase("5")){
                bottomNavigation.setSelectedItemId(R.id.community);
            }
        }
        else{
            bottomNavigation.setSelectedItemId(R.id.home);
        }

        bottomNavigation.setOnNavigationItemSelectedListener(this);

    }

    /**
     * method to setup the bottomsheet
     */
    private void setBottomSheet() {
/*
        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayouts));

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

        bottomSheetBehavior.setHideable(false);

        bottomSheetBehavior.setFitToContents(false);

        bottomSheetBehavior.setHalfExpandedRatio(0.7f);


        // Capturing the callbacks for bottom sheet
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.w("Bottom Sheet Behaviour", "STATE_COLLAPSED");
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.w("Bottom Sheet Behaviour", "STATE_DRAGGING");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        Log.w("Bottom Sheet Behaviour", "STATE_EXPANDED");
                        //  bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.w("Bottom Sheet Behaviour", "STATE_HIDDEN");
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.w("Bottom Sheet Behaviour", "STATE_SETTLING");
                        break;
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                        Log.w("Bottom Sheet Behaviour", "STATE_HALF_EXPANDED");
                        break;
                }


            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {


            }


        });*/
    }



    @SuppressLint("LogNotTimber")
    private void SPSpecificServiceDetailsResponseCall(int distance, int reviewcount, int count_value_start, int count_value_end) {
       /* avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();*/
        headerView.setVisibility(View.GONE);

        scrollablContent.setVisibility(View.GONE);

        includelayout.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmerAnimation();
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<SPSpecificServiceDetailsResponse> call = apiInterface.SPSpecificServiceDetailsResponseCall(RestUtils.getContentType(), spSpecificServiceDetailsRequest(distance,reviewcount,count_value_start,count_value_end));
        Log.w(TAG,"SPSpecificServiceDetailsResponseCall url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<SPSpecificServiceDetailsResponse>() {
            @SuppressLint({"SetTextI18n", "LogNotTimber"})
            @Override
            public void onResponse(@NonNull Call<SPSpecificServiceDetailsResponse> call, @NonNull Response<SPSpecificServiceDetailsResponse> response) {
                //avi_indicator.smoothToHide();
        //        refresh_layout.setRefreshing(false);

                mShimmerViewContainer.stopShimmerAnimation();
                includelayout.setVisibility(View.GONE);

                headerView.setVisibility(View.VISIBLE);
                scrollablContent.setVisibility(View.VISIBLE);

                Log.w(TAG,"SPSpecificServiceDetailsResponse" + new Gson().toJson(response.body()));
                if (response.body() != null) {

                    if (200 == response.body().getCode()) {
                        txt_no_records.setVisibility(View.GONE);

                        if (response.body().getData() != null) {

                            if(response.body().getBanner() != null && response.body().getBanner().size()>0){
                                setViewServicesBanner(response.body().getBanner());
                            }


                            if(response.body().getData().getService_Details().get_id() != null) {
                                catid = response.body().getData().getService_Details().get_id();
                            }
                            Log.w(TAG,"catid : "+catid);
                            if(response.body().getData().getService_provider() != null) {
                                serviceProviderList = response.body().getData().getService_provider();
                            }

                            if(serviceProviderList != null && serviceProviderList.size()>0){
                                //txt_totalproviders.setText(serviceProviderList.size()+" Providers");
                                txt_selected_service.setVisibility(View.VISIBLE);
                                rl_filters.setVisibility(View.VISIBLE);
                                //setBottomSheet();

                                if(response.body().getData().getService_Details().getTitle() != null){
                                    txt_selected_service.setText(response.body().getData().getService_Details().getTitle());
                                }
                                setViewListedSP(serviceProviderList);
                            }else{
                                showAlertSPNotAvlLoading(response.body().getAlert_msg());

                            }




                        }
                    }
                    else{
//                        txt_totalproviders.setText(serviceProviderList.size()+" Providers");

                        txt_selected_service.setVisibility(View.VISIBLE);
                        rl_filters.setVisibility(View.VISIBLE);
                        txt_no_records.setVisibility(View.VISIBLE);
                        txt_no_records.setText("No service found");

                        if(response.body().getData().getService_Details().getTitle() != null){
                            txt_selected_service.setText(response.body().getData().getService_Details().getTitle());
                        }
                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<SPSpecificServiceDetailsResponse> call,@NonNull Throwable t) {
                //avi_indicator.smoothToHide();
                mShimmerViewContainer.stopShimmerAnimation();
                includelayout.setVisibility(View.GONE);
                Log.w(TAG,"SPSpecificServiceDetailsResponse flr"+ t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    @SuppressLint("LogNotTimber")
    private SPSpecificServiceDetailsRequest spSpecificServiceDetailsRequest(int distance, int reviewcount, int count_value_start, int count_value_end) {
        /*
         * cata_id : 5fe185d61996f651f5133693
         * distance : 0
         * user_id : 5ffe70d5b699b42563933d90
         * Count_value_start : 0
         * Count_value_end : 500
         * review_count : 3
         */
        SPSpecificServiceDetailsRequest spSpecificServiceDetailsRequest = new SPSpecificServiceDetailsRequest();
        spSpecificServiceDetailsRequest.setCata_id(catid);
        spSpecificServiceDetailsRequest.setDistance(distance);
        spSpecificServiceDetailsRequest.setUser_id(userid);
        spSpecificServiceDetailsRequest.setCount_value_start(count_value_start);
        spSpecificServiceDetailsRequest.setCount_value_end(count_value_end);
        spSpecificServiceDetailsRequest.setReview_count(reviewcount);
        Log.w(TAG,"spSpecificServiceDetailsRequest "+ new Gson().toJson(spSpecificServiceDetailsRequest));
        return spSpecificServiceDetailsRequest;
    }

    private void setViewListedSP(List<SPSpecificServiceDetailsResponse.DataBean.ServiceProviderBean> serviceProviderList) {
        rv_nearbyservices.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_nearbyservices.setItemAnimator(new DefaultItemAnimator());
       // rv_nearbyservices.setNestedScrollingEnabled(true);
        SelectedServiceProviderAdapter doctorNewAppointmentAdapter = new SelectedServiceProviderAdapter(getApplicationContext(), serviceProviderList,catid,from,distance,reviewcount,Count_value_start,Count_value_end);
        rv_nearbyservices.setAdapter(doctorNewAppointmentAdapter);

    }


    private void setViewServicesBanner(List<SPSpecificServiceDetailsResponse.BannerBean> banner) {
        if(banner!=null && banner.size()>0){
            viewpageData(banner);
        }

    }

    private void viewpageData(List<SPSpecificServiceDetailsResponse.BannerBean> banner) {

        ViewPager viewPager = findViewById(R.id.pager);

        TabLayout tabLayout = findViewById(R.id.tabDots);

        Timer timer;
        final long DELAY_MS = 600;//delay in milliseconds before task is to be executed
        final long PERIOD_MS = 3000;

        currentPage = 0;

        tabLayout.setupWithViewPager(viewPager, true);

        ViewPagerPetServiceAdapter viewPagerPetServiceAdapter = new ViewPagerPetServiceAdapter(getApplicationContext(), banner);
        viewPager.setAdapter(viewPagerPetServiceAdapter);
        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == banner.size()) {
                currentPage = 0;
            }
            viewPager.setCurrentItem(currentPage++, true);
        };

        timer = new Timer(); // This will create a new Thread

        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {

                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!this.isDestroyed()) {
            Glide.with(SelectedServiceActivity.this).pauseRequests();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                onBackPressed();
                break;

            case R.id.img_notification:
                startActivity(new Intent(SelectedServiceActivity.this, NotificationActivity.class));
                break;
            case R.id.img_profile:
                goto_Profile();
                break;
                case R.id.rl_filters:
              //  goto_SPFilter();
                break;
                case R.id.edt_filter:
           //     goto_SPFilter();
                break;


        }
    }

    private void goto_SPFilter() {
        Intent intent = new Intent(getApplicationContext(), SPFiltersActivity.class);
        intent.putExtra("distance",distance);
        intent.putExtra("catid",catid);
        intent.putExtra("selectedprice",selectedprice);
        intent.putExtra("reviewcount",reviewcount);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(from != null && from.equalsIgnoreCase("PetServices")){
            callDirections("3");
        }else{
            Intent intent = new Intent(getApplicationContext(), CustomerDashboardActivity.class);
            startActivity(intent);
            finish();
        }

    }

    public void callDirections(String tag){
        Intent intent = new Intent(getApplicationContext(), CustomerDashboardActivity.class);
        intent.putExtra("tag",tag);
        startActivity(intent);
        finish();
    }

    private void goto_Profile() {
        Intent intent = new Intent(getApplicationContext(), CustomerProfileScreenActivity.class);
        intent.putExtra("fromactivity",TAG);
        intent.putExtra("catid",catid);
        intent.putExtra("from",from);
        intent.putExtra("distance",distance);
        intent.putExtra("reviewcount",reviewcount);
        intent.putExtra("Count_value_start",Count_value_start);
        intent.putExtra("Count_value_end",Count_value_end);
        startActivity(intent);
    }



    public void showAlertSPNotAvlLoading(String errormesage){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SelectedServiceActivity.this);
        alertDialogBuilder.setMessage(errormesage);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("ok",
                (arg0, arg1) -> hideLoadingSPnotavl());

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void hideLoadingSPnotavl() {
        try {
                distance = 1;

                if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
                    SPSpecificServiceDetailsResponseCall(distance, reviewcount, Count_value_start, Count_value_end);
                }


            alertDialog.dismiss();

        } catch (Exception ignored) {

        }
    }

    private void setMargins(RelativeLayout rl_layout, int i, int i1, int i2, int i3) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)rl_layout.getLayoutParams();
        params.setMargins(i, i1, i2, i3);
        rl_layout.setLayoutParams(params);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                callDirections("1");
                break;
            case R.id.shop:
                callDirections("2");
                break;
            case R.id.services:
                callDirections("3");
                break;
            case R.id.care:
                callDirections("4");
                break;
            case R.id.community:
                callDirections("5");
                break;

            default:
                return  false;
        }
        return true;
    }

}