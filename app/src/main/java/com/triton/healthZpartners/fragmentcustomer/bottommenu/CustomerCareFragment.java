package com.triton.healthZpartners.fragmentcustomer.bottommenu;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;


import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.facebook.shimmer.ShimmerFrameLayout;


import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.triton.healthZpartners.R;

import com.triton.healthZpartners.adapter.PetLoverDoctorFilterAdapter;
import com.triton.healthZpartners.adapter.PetLoverNearByDoctorAdapter;

import com.triton.healthZpartners.adapter.ViewPagerPetCareAdapter;
import com.triton.healthZpartners.adapter.ViewPagerPetCareFilterAdapter;
import com.triton.healthZpartners.api.APIClient;
import com.triton.healthZpartners.api.RestApiInterface;
import com.triton.healthZpartners.requestpojo.DoctorSearchRequest;
import com.triton.healthZpartners.requestpojo.FilterDoctorRequest;
import com.triton.healthZpartners.responsepojo.DoctorSearchResponse;
import com.triton.healthZpartners.responsepojo.FilterDoctorResponse;
import com.triton.healthZpartners.sessionmanager.SessionManager;
import com.triton.healthZpartners.utils.ConnectionDetector;
import com.triton.healthZpartners.utils.RestUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CustomerCareFragment extends Fragment implements Serializable, View.OnClickListener {


    private String TAG = "CustomerCareFragment";
    int currentPage = 0;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_communicationtype)
    TextView txt_communicationtype;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.switchButton_communcationtype)
    SwitchCompat switchButton_communcationtype;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_no_records)
    TextView txt_no_records;

    Dialog dialog;
    private String userid;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.avi_indicator)
    AVLoadingIndicatorView avi_indicator;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_nearbydoctors)
    RecyclerView rv_nearbydoctors;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_totaldrs)
    TextView txt_totaldrs;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_filter)
    TextView txt_filter;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_search)
    EditText edt_search;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rl_search)
    RelativeLayout rl_search;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.scrollablContent)
    ScrollView bottomSheetLayout;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.linear1)
    LinearLayout linear1;


    private ShimmerFrameLayout mShimmerViewContainer;
    private View includelayout;

    private Context mContext;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private List<DoctorSearchResponse.DataBean> doctorDetailsResponseList;
    private Dialog alertDialog;
    private String searchString = "";
    private int communication_type = 0;
    private int reviewcount;
    private String fromactivity,specialization;

    private List<FilterDoctorResponse.DataBean> doctorFilterDetailsResponseList = new ArrayList<>();

    private String doctorid;

    // BottomSheetBehavior variable
    @SuppressWarnings("rawtypes")
    public BottomSheetBehavior bottomSheetBehavior;


    View view;
    private List<FilterDoctorResponse.BannerBean> doctorFilterBannerResponseList;

    public CustomerCareFragment() {
        // Required empty public constructor
    }


   @SuppressLint("LogNotTimber")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.w(TAG,"onCreate-->");



    }

//    private Activity mActivity;
//
//    @Override
//    public void onAttach(@NotNull Activity activity) {
//        super.onAttach(activity);
//        mActivity=activity;
//    }
//
//    public Activity getMyActivity() {
//        return mActivity;
//    }

    @SuppressLint({"SetTextI18n", "LogNotTimber"})
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.w(TAG,"onCreateView-->");

        view = inflater.inflate(R.layout.fragment_customer_care, container, false);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        ButterKnife.bind(this, view);
        mContext = getActivity();



        avi_indicator.setVisibility(View.GONE);
        txt_totaldrs.setVisibility(View.GONE);

        rl_search.setVisibility(View.GONE);

        linear1.setVisibility(View.GONE);

        includelayout = view.findViewById(R.id.includelayout);
        mShimmerViewContainer = includelayout.findViewById(R.id.shimmer_layout);

        if(getArguments() != null){
            fromactivity = getArguments().getString("fromactivity");
            reviewcount = getArguments().getInt("reviewcount");
            specialization = getArguments().getString("specialization");
            Log.w(TAG,"fromactivity : "+fromactivity+" reviewcount : "+reviewcount+" specialization : "+specialization);

            searchString = getArguments().getString("searchString");
            doctorid = getArguments().getString("doctorid");
            communication_type = getArguments().getInt("communication_type");
            Log.w(TAG," communication_type : "+communication_type+" searchString : "+searchString);


        }


        SessionManager sessionManager = new SessionManager(mContext);
        HashMap<String, String> user = sessionManager.getProfileDetails();
        userid = user.get(SessionManager.KEY_ID);
        Log.w(TAG,"customerid-->"+userid);

        if(communication_type == 1){
            switchButton_communcationtype.setChecked(true);
            txt_communicationtype.setText("Online");

        }else{
            txt_communicationtype.setText("Visit Clinic");

        }


        switchButton_communcationtype.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    txt_communicationtype.setText("Online");
                    communication_type = 1;
                    if (new ConnectionDetector(mContext).isNetworkAvailable(mContext)) {
                        doctorSearchResponseCall(searchString,communication_type);
                    }
                }else{
                    txt_communicationtype.setText("Clinic vists");
                    communication_type = 0;
                    if (new ConnectionDetector(mContext).isNetworkAvailable(mContext)) {
                        doctorSearchResponseCall(searchString,communication_type);
                    }

                }
            }
        });

        if(fromactivity != null && fromactivity.equalsIgnoreCase("FiltersActivity")) {

            Log.w(TAG,"callfilter --> true");

            if (new ConnectionDetector(mContext).isNetworkAvailable(mContext)) {
                filterDoctorResponseCall();
            }
        }else{

            Log.w(TAG,"calldrsearch --> true");

            if (new ConnectionDetector(mContext).isNetworkAvailable(mContext)) {
                //doctorSearchRequest(searchString,communication_type);
                doctorSearchResponseCall(searchString,communication_type);
            }
        }



        edt_search.addTextChangedListener(new TextWatcher() {
            @SuppressLint("LogNotTimber")
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                Log.w(TAG,"beforeTextChanged-->"+s.toString());

                searchString="";
            }

            @SuppressLint("LogNotTimber")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.w(TAG,"onTextChanged-->"+s.toString());
                searchString = s.toString();

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.w(TAG,"afterTextChanged-->"+s.toString());
                if(!searchString.isEmpty()){
                    doctorSearchResponseCall(searchString, communication_type);
                   /* img_search.setVisibility(View.VISIBLE);
                    img_clear.setVisibility(View.VISIBLE);*/

                }else{
                    searchString ="";
                    doctorSearchResponseCall(searchString, communication_type);
                    /*img_search.setVisibility(View.GONE);
                    img_clear.setVisibility(View.GONE);*/
                }


            }
        });

        txt_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           /*     Intent intent = new Intent(mContext, FiltersActivity.class);
                intent.putExtra("specialization",specialization);
                intent.putExtra("reviewcount",reviewcount);
                intent.putExtra("communication_type",communication_type);
                startActivity(intent);*/
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){



        }

    }


    /**
     * method to setup the bottomsheet
     */
    private void setBottomSheet() {

       /* bottomSheetBehavior = BottomSheetBehavior.from(view.findViewById(R.id.bottomSheetLayout));

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        bottomSheetBehavior.setHideable(false);

        bottomSheetBehavior.setFitToContents(false);

        // Capturing the callbacks for bottom sheet
        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        Log.w("Bottom Sheet Behaviour", "STATE_COLLAPSED");
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.w("Bottom Sheet Behaviour", "STATE_DRAGGING");
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
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
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

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

    private void viewpageData(List<DoctorSearchResponse.BannerBean> banner) {

        ViewPager viewPager = view.findViewById(R.id.pager);

        TabLayout tabLayout = view.findViewById(R.id.tabDots);

        Timer timer;
        final long DELAY_MS = 600;//delay in milliseconds before task is to be executed
        final long PERIOD_MS = 3000;

        currentPage = 0;

        tabLayout.setupWithViewPager(viewPager, true);

        ViewPagerPetCareAdapter viewPagerClinicDetailsAdapter = new ViewPagerPetCareAdapter(getContext(), banner);
        viewPager.setAdapter(viewPagerClinicDetailsAdapter);
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
    private void viewpageFilterData(List<FilterDoctorResponse.BannerBean> doctorFilterBannerResponseList) {

        ViewPager viewPager = view.findViewById(R.id.pager);

        TabLayout tabLayout = view.findViewById(R.id.tabDots);

        Timer timer;
        final long DELAY_MS = 600;//delay in milliseconds before task is to be executed
        final long PERIOD_MS = 3000;

        currentPage = 0;

        tabLayout.setupWithViewPager(viewPager, true);

        ViewPagerPetCareFilterAdapter viewPagerPetCareFilterAdapter = new ViewPagerPetCareFilterAdapter(getContext(), doctorFilterBannerResponseList);
        viewPager.setAdapter(viewPagerPetCareFilterAdapter);
        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == doctorFilterBannerResponseList.size()) {
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



    @SuppressLint("LogNotTimber")
    private void doctorSearchResponseCall(String searchString, int communication_type) {
      /*  avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();*/
        rl_search.setVisibility(View.GONE);
        linear1.setVisibility(View.GONE);
        bottomSheetLayout.setVisibility(View.GONE);
        includelayout.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmerAnimation();

        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<DoctorSearchResponse> call = apiInterface.doctorSearchResponseCall(RestUtils.getContentType(), doctorSearchRequest(searchString,communication_type));
        String url = "DoctorSearchResponse url  :%s"+" "+ call.request().url().toString();
        Log.w(TAG,"URL String "+url);
        Log.w(TAG,"DoctorSearchResponse url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<DoctorSearchResponse>() {
            @SuppressLint({"LogNotTimber", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<DoctorSearchResponse> call, @NonNull Response<DoctorSearchResponse> response) {
                //avi_indicator.smoothToHide();
                mShimmerViewContainer.stopShimmerAnimation();
                includelayout.setVisibility(View.GONE);
                rl_search.setVisibility(View.VISIBLE);
                linear1.setVisibility(View.VISIBLE);
                bottomSheetLayout.setVisibility(View.VISIBLE);
                Log.w(TAG,"DoctorSearchResponse" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    if (200 == response.body().getCode()) {

                        setBottomSheet();

                        if (response.body().getData() != null) {
                            doctorDetailsResponseList = response.body().getData();
                            Log.w(TAG, "doctorDetailsResponseList Size" + doctorDetailsResponseList.size());
                            if (doctorDetailsResponseList != null && doctorDetailsResponseList.size()>0) {

                                rl_search.setVisibility(View.VISIBLE);
                                rv_nearbydoctors.setVisibility(View.VISIBLE);
                                txt_no_records.setVisibility(View.GONE);
                              //  txt_totaldrs.setVisibility(View.VISIBLE);
                              //  txt_totaldrs.setText(doctorDetailsResponseList.size()+" "+"Doctors");

                            }
                            else {
                                rv_nearbydoctors.setVisibility(View.GONE);
                                txt_totaldrs.setVisibility(View.GONE);
                                txt_no_records.setVisibility(View.VISIBLE);
                                txt_no_records.setText("No doctors available");

                            }

                            if(response.body().getBanner() != null && response.body().getBanner().size()>0){
                                setViewDoctors(response.body().getBanner());
                            }

                        }



                    }
                    else {
                        showErrorLoading(response.body().getMessage());
                    }

                }


            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(@NonNull Call<DoctorSearchResponse> call,@NonNull Throwable t) {
                //avi_indicator.smoothToHide();

                mShimmerViewContainer.stopShimmerAnimation();
                includelayout.setVisibility(View.GONE);
                Log.w(TAG,"DoctorSearchResponse flr" + t.getMessage());
            }
        });

    }
    private void setViewDoctors(List<DoctorSearchResponse.BannerBean> banner) {


        if(banner!=null && banner.size()>0){
            viewpageData(banner);

            /*for(int i=0; i<banner.size(); i++){

                if(banner.get(i).getImage_path() !=null && !banner.get(i).getImage_path().isEmpty()){

                    imagelist.add(doctorDetailsResponseList.get(i).getDoctor_img());
                }
                else {

                    imagelist.add(APIClient.BANNER_IMAGE_URL);
                }


            }*/

        }
        else {

            banner.add(new DoctorSearchResponse.BannerBean(APIClient.BANNER_IMAGE_URL));

            viewpageData(banner);
        }



        rv_nearbydoctors.setLayoutManager(new LinearLayoutManager(mContext));
        rv_nearbydoctors.setItemAnimator(new DefaultItemAnimator());
        PetLoverNearByDoctorAdapter petLoverNearByDoctorAdapter = new PetLoverNearByDoctorAdapter(mContext, doctorDetailsResponseList,communication_type,searchString);
        rv_nearbydoctors.setAdapter(petLoverNearByDoctorAdapter);
    }
    @SuppressLint("LogNotTimber")
    private DoctorSearchRequest doctorSearchRequest(String searchString, int communication_type) {
        /*
         * search_string :
         * communication_type : 0
         * user_id : 5fd227ac80791a71361baad3
         */

        if(searchString==null||searchString.isEmpty()){
            searchString="";
        }

        DoctorSearchRequest doctorSearchRequest = new DoctorSearchRequest();
        doctorSearchRequest.setSearch_string(searchString);
        doctorSearchRequest.setCommunication_type(communication_type);
        doctorSearchRequest.setUser_id(userid);
        Log.w(TAG,"doctorSearchRequest"+ new Gson().toJson(doctorSearchRequest));
        return doctorSearchRequest;
    }




    public void showErrorLoading(String errormesage){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setMessage(errormesage);
        alertDialogBuilder.setPositiveButton("ok",
                (arg0, arg1) -> hideLoading());




        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void hideLoading(){
        try {
            alertDialog.dismiss();
        }catch (Exception ignored){

        }
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(mContext,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String @NotNull [] permissions, @NotNull int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {


                }
            } else {
                Toast.makeText(mContext, "permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }





    @SuppressLint("LogNotTimber")
    private void filterDoctorResponseCall() {
        /*avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();*/
        rl_search.setVisibility(View.GONE);
        linear1.setVisibility(View.GONE);
        bottomSheetLayout.setVisibility(View.GONE);
        includelayout.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmerAnimation();

        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<FilterDoctorResponse> call = apiInterface.filterDoctorResponseCall(RestUtils.getContentType(), filterDoctorRequest());
        Log.w(TAG,"filterDoctorResponseCall url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<FilterDoctorResponse>() {
            @SuppressLint({"LogNotTimber", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<FilterDoctorResponse> call, @NonNull Response<FilterDoctorResponse> response) {
                //avi_indicator.smoothToHide();
                rl_search.setVisibility(View.VISIBLE);
                linear1.setVisibility(View.VISIBLE);
                bottomSheetLayout.setVisibility(View.VISIBLE);
                mShimmerViewContainer.stopShimmerAnimation();
                includelayout.setVisibility(View.GONE);
                Log.w(TAG,"filterDoctorResponseCall" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    if (200 == response.body().getCode()) {

                        setBottomSheet();

                        if (response.body().getData() != null) {
                            doctorFilterDetailsResponseList.clear();
                            doctorFilterDetailsResponseList = response.body().getData();
                            doctorFilterBannerResponseList = response.body().getBanner();
                            Log.w(TAG,"doctorFilterDetailsResponseList size :"+doctorFilterDetailsResponseList.size());
                            Log.w(TAG,"doctorFilterDetailsResponseList :"+new Gson().toJson(doctorFilterDetailsResponseList));
                            if (doctorFilterDetailsResponseList != null && doctorFilterDetailsResponseList.size()>0) {
                                rv_nearbydoctors.setVisibility(View.VISIBLE);
                                rl_search.setVisibility(View.VISIBLE);
                                txt_no_records.setVisibility(View.GONE);

                                bottomSheetBehavior.setHalfExpandedRatio(0.9f);
                               // txt_totaldrs.setVisibility(View.GONE);
                               // txt_totaldrs.setText(doctorFilterDetailsResponseList.size()+" "+"Doctors");
                                setViewDoctorFilters(doctorFilterDetailsResponseList);

                            } else {
                                rv_nearbydoctors.setVisibility(View.GONE);
                                txt_totaldrs.setVisibility(View.GONE);
                                txt_no_records.setVisibility(View.VISIBLE);
                                bottomSheetBehavior.setPeekHeight(0);
                                bottomSheetBehavior.setHalfExpandedRatio(0.8f);
                                txt_no_records.setText("No doctors available");

                            }

                        }



                    }
                    else {
                        showErrorLoading(response.body().getMessage());
                    }

                }


            }

            @SuppressLint({"LongLogTag", "LogNotTimber"})
            @Override
            public void onFailure(@NonNull Call<FilterDoctorResponse> call,@NonNull Throwable t) {
               // avi_indicator.smoothToHide();
                mShimmerViewContainer.stopShimmerAnimation();
                includelayout.setVisibility(View.GONE);
                Log.w(TAG,"FilterDoctorResponse flr"+ t.getMessage());
            }
        });

    }
    @SuppressLint("LogNotTimber")
    private void setViewDoctorFilters(List<FilterDoctorResponse.DataBean> doctorFilterDetailsResponseList) {
        if(doctorFilterDetailsResponseList!=null&&doctorFilterDetailsResponseList.size()>0){

            List<DoctorSearchResponse.BannerBean> banner = new ArrayList<>();

            for(int i=0; i<doctorFilterDetailsResponseList.size(); i++){

                Log.w(TAG,""+doctorFilterDetailsResponseList.get(i).getDoctor_img());

                if(doctorFilterBannerResponseList != null
                        &&doctorFilterBannerResponseList.size()>0){

                   // banner.add(new DoctorSearchResponse.BannerBean(doctorFilterDetailsResponseList.get(i).getDoctor_img()));
                    viewpageFilterData(doctorFilterBannerResponseList);
                }
                else {

                    banner.add(new DoctorSearchResponse.BannerBean(doctorFilterDetailsResponseList.get(i).getDoctor_img()));
                    viewpageData(banner);
                }


            }
            Log.w(TAG,"banner if size1 : "+banner.size());



        }
        else {
            List<DoctorSearchResponse.BannerBean> banner = new ArrayList<>();
            Log.w(TAG,"banner size else : "+banner.size());
            banner.add(new DoctorSearchResponse.BannerBean(APIClient.BANNER_IMAGE_URL));
            viewpageData(banner);
        }

        rv_nearbydoctors.setLayoutManager(new LinearLayoutManager(mContext));
        rv_nearbydoctors.setItemAnimator(new DefaultItemAnimator());
        PetLoverDoctorFilterAdapter petLoverDoctorFilterAdapter = new PetLoverDoctorFilterAdapter(mContext, doctorFilterDetailsResponseList);
        rv_nearbydoctors.setAdapter(petLoverDoctorFilterAdapter);
    }
    @SuppressLint("LogNotTimber")
    private FilterDoctorRequest filterDoctorRequest() {
        /*
         * user_id : 5fd841a67aa4cc1c6a1e5636
         * specialization :
         * nearby : 0
         * Review_count : 5
         */

        Log.w(TAG,"specialization "+ specialization);

        if(specialization == null){
            specialization = "";
        }
        FilterDoctorRequest filterDoctorRequest = new FilterDoctorRequest();
        filterDoctorRequest.setUser_id(userid);
        filterDoctorRequest.setSpecialization(specialization);
        filterDoctorRequest.setNearby(0);
        filterDoctorRequest.setReview_count(reviewcount);
        Log.w(TAG,"filterDoctorRequest"+ new Gson().toJson(filterDoctorRequest));
        return filterDoctorRequest;
    }




}
