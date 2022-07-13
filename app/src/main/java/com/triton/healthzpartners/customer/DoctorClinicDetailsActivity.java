package com.triton.healthzpartners.customer;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.triton.healthzpartners.R;
import com.triton.healthzpartners.adapter.DoctorClinicPetsHandledListAdapter;
import com.triton.healthzpartners.adapter.DoctorClinicSpecTypesListAdapter;
import com.triton.healthzpartners.adapter.ViewPagerClinicDetailsAdapter;
import com.triton.healthzpartners.api.APIClient;
import com.triton.healthzpartners.api.RestApiInterface;
import com.triton.healthzpartners.requestpojo.DoctorDetailsRequest;
import com.triton.healthzpartners.requestpojo.DoctorFavCreateRequest;
import com.triton.healthzpartners.responsepojo.DoctorDetailsResponse;
import com.triton.healthzpartners.responsepojo.SuccessResponse;
import com.triton.healthzpartners.sessionmanager.SessionManager;
import com.triton.healthzpartners.utils.ConnectionDetector;
import com.triton.healthzpartners.utils.GridSpacingItemDecoration;
import com.triton.healthzpartners.utils.RestUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorClinicDetailsActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, BottomNavigationView.OnNavigationItemSelectedListener {

    // BottomSheetBehavior variable
    @SuppressWarnings("rawtypes")
    public BottomSheetBehavior bottomSheetBehavior;

    private static final String TAG = "DoctorClinicDetailsActivity";

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.avi_indicator)
    AVLoadingIndicatorView avi_indicator;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.pager)
    ViewPager viewPager;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tabDots)
    TabLayout tabLayout;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_clinicname)
    TextView txt_clinicname;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_drname)
    TextView txt_drname;

    private SupportMapFragment mapFragment;
    private double latitude;
    private double longitude;
    private GoogleMap mMap;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_place)
    TextView txt_place;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_distance)
    TextView txt_distance;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_about_vet_desc)
    TextView txt_dr_desc;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_dr_experience)
    TextView txt_dr_experience;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_dr_consultationfees)
    TextView txt_dr_consultationfees;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_book_now)
    LinearLayout ll_book_now;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rl_back)
    RelativeLayout rl_back;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_review_count)
    TextView txt_review_count;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.view11)
    View view11;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_fav)
    ImageView img_fav;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_speclist)
    RecyclerView rv_speclist;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.toolbar_header)
    Toolbar toolbar_header;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_about_vet_label)
    TextView txt_about_vet_label;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_spec_label)
    TextView txt_spec_label;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_map)
    LinearLayout ll_map;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_location_label)
    TextView txt_location_label;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_root)
    LinearLayout ll_root;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.footerView)
    LinearLayout footerView;



    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;

    private String doctorid;
    private List<DoctorDetailsResponse.DataBean.ClinicPicBean> doctorclinicdetailsResponseList;
    private String doctorname;
    private Integer reviewcount;
    private Integer starcount;
    private String clinicname;
    private String distance;
    private String ClinicLocationname;
    private String fromactivity;
    private String searchString;
    private int amount;
    private String communicationtype,drtitle;
    private int Doctor_exp;

    List<DoctorDetailsResponse.DataBean.SpecializationBean> specializationBeanList;

    List<DoctorDetailsResponse.DataBean.PetHandledBean> petHandledBeanList;

    DoctorClinicSpecTypesListAdapter doctorClinicSpecTypesListAdapter;

    DoctorClinicPetsHandledListAdapter doctorClinicPetsHandledListAdapter;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.hand_img1)
    ImageView hand_img1;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.hand_img2)
    ImageView hand_img2;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.hand_img3)
    ImageView hand_img3;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.hand_img4)
    ImageView hand_img4;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.hand_img5)
    ImageView hand_img5;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_seemore_spec)
    TextView txt_seemore_spec;

    private int communication_type;
    private String userid;

    String strdistance, strprice,strkm;

    StringBuilder sb = new StringBuilder();


    /* Petlover Bottom Navigation */

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigation;

    public static String active_tag = "4";


    /**/
    @SuppressLint({"LongLogTag", "LogNotTimber"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_clinic_details);
        ButterKnife.bind(this);

        SessionManager sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getProfileDetails();
        userid = user.get(SessionManager.KEY_ID);


        avi_indicator.setVisibility(View.GONE);
        txt_seemore_spec.setVisibility(View.GONE);
        ll_book_now.setVisibility(View.GONE);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            doctorid = extras.getString("doctorid");
            doctorname = extras.getString("doctorname");
            distance = extras.getString("distance");
            fromactivity = extras.getString("fromactivity");
            communication_type = extras.getInt("communication_type");
            searchString = extras.getString("searchString");
            Log.w(TAG,"Bundle "+" doctorid : "+doctorid+ "fromactivity : "+fromactivity);
        }

        if(distance!=null&&!distance.isEmpty()){

            APIClient.DISTANCE = distance;
        }


        ll_book_now.setOnClickListener(this);

        rl_back.setOnClickListener(this);

        avi_indicator.setVisibility(View.VISIBLE);

        viewPager.setVisibility(View.GONE);

        tabLayout.setVisibility(View.GONE);

        hand_img1.setVisibility(View.GONE);

        hand_img2.setVisibility(View.GONE);

        hand_img3.setVisibility(View.GONE);

        hand_img4.setVisibility(View.GONE);

        hand_img5.setVisibility(View.GONE);

        txt_clinicname.setVisibility(View.GONE);

        txt_drname.setVisibility(View.GONE);

        txt_about_vet_label.setVisibility(View.GONE);

        txt_dr_desc.setVisibility(View.GONE);

        txt_spec_label.setVisibility(View.GONE);

        rv_speclist.setVisibility(View.GONE);

        txt_location_label.setVisibility(View.GONE);

        txt_place.setVisibility(View.GONE);

        ll_map.setVisibility(View.GONE);

        ll_root.setVisibility(View.GONE);

        txt_review_count.setVisibility(View.GONE);

        view11.setVisibility(View.GONE);
//
//        bottomSheetLayouts.setVisibility(View.GONE);


        if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
            if(doctorid != null){
                doctorDetailsResponseCall();
            }

        }


        txt_seemore_spec.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if(txt_seemore_spec.getText().toString() != null && txt_seemore_spec.getText().toString().equalsIgnoreCase("See more...")){
                    txt_seemore_spec.setText("Hide");
                    int size =specializationBeanList.size();
                    setSpecList(specializationBeanList,size);
                }else{
                    txt_seemore_spec.setText("See more...");
                    int size = 4;
                    setSpecList(specializationBeanList,size);

                }

            }
        });

//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        assert mapFragment != null;
//        mapFragment.getMapAsync(DoctorClinicDetailsActivity.this);
//
//
//
//        setBottomSheet();


        img_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
                    if(doctorid != null){
                        createDoctorFavListResponseCall();
                    }

                }
            }
        });


       //bottomNavigation.getMenu().getItem(0).setCheckable(false);


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
            bottomNavigation.setSelectedItemId(R.id.shop);
        }

        bottomNavigation.setOnNavigationItemSelectedListener(this);


    }


    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private void createDoctorFavListResponseCall() {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<SuccessResponse> call = ApiService.createDoctorFavListResponseCall(RestUtils.getContentType(),doctorFavCreateRequest());
        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @SuppressLint({"SetTextI18n", "LogNotTimber"})
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"SuccessResponse Fav"+ "--->" + new Gson().toJson(response.body()));

                if (response.body() != null) {
                    if(response.body().getCode() ==  200){
                        Toasty.success(getApplicationContext(),""+response.body().getMessage(),Toasty.LENGTH_SHORT).show();

                        if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
                            if(doctorid != null){
                                doctorDetailsResponseCall();
                            }

                        }
                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                avi_indicator.smoothToHide();

                Log.w(TAG,"SuccessResponse fav flr"+"--->" + t.getMessage());
            }
        });

    }



    @SuppressLint({"LogNotTimber", "LongLogTag"})
    private DoctorFavCreateRequest doctorFavCreateRequest() {
        /*
         * user_id : 603e262e2c2b43125f8cb801
         * doctor_id : 603e31a02c2b43125f8cb806
         */
        DoctorFavCreateRequest doctorFavCreateRequest = new DoctorFavCreateRequest();
        doctorFavCreateRequest.setUser_id(userid);
        doctorFavCreateRequest.setDoctor_id(doctorid);
        Log.w(TAG,"doctorFavCreateRequest"+ "--->" + new Gson().toJson(doctorFavCreateRequest));
        return doctorFavCreateRequest;
    }



    /**
     * method to setup the bottomsheet
     */
    private void setBottomSheet() {

        /*bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottomSheetLayouts));

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


    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private void doctorDetailsResponseCall() {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<DoctorDetailsResponse> call = ApiService.doctorDetailsResponseCall(RestUtils.getContentType(),doctorDetailsRequest());
        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<DoctorDetailsResponse>() {
            @SuppressLint({"SetTextI18n", "LogNotTimber"})
            @Override
            public void onResponse(@NonNull Call<DoctorDetailsResponse> call, @NonNull Response<DoctorDetailsResponse> response) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"DoctorDetailsResponse"+ "--->" + new Gson().toJson(response.body()));


                if (response.body() != null) {
                    if(200 == response.body().getCode()){
                        ll_book_now.setVisibility(View.VISIBLE);
                        if(response.body().getData().getClinic_pic() != null) {
                            doctorclinicdetailsResponseList = response.body().getData().getClinic_pic();
                        }
                        if(response.body().getData() != null) {
                            clinicname = response.body().getData().getClinic_name();
                            doctorname = response.body().getData().getDr_name();
                            drtitle = response.body().getData().getDr_title();
                            reviewcount = response.body().getData().getReview_count();
                            starcount = response.body().getData().getStar_count();
                            amount = response.body().getData().getAmount();
                            Log.w(TAG, "amount : " + amount);
                            communicationtype = response.body().getData().getCommunication_type();
                            ClinicLocationname = response.body().getData().getClinic_loc();
                            Doctor_exp = response.body().getData().getDoctor_exp();

                            viewPager.setVisibility(View.VISIBLE);

                            tabLayout.setVisibility(View.VISIBLE);

                            hand_img1.setVisibility(View.VISIBLE);

                            hand_img2.setVisibility(View.VISIBLE);

                            hand_img3.setVisibility(View.VISIBLE);

                            hand_img4.setVisibility(View.VISIBLE);

                            hand_img5.setVisibility(View.VISIBLE);

                            txt_clinicname.setVisibility(View.VISIBLE);

                            txt_drname.setVisibility(View.VISIBLE);

                            txt_about_vet_label.setVisibility(View.VISIBLE);

                            txt_dr_desc.setVisibility(View.VISIBLE);

                            txt_spec_label.setVisibility(View.VISIBLE);

                            rv_speclist.setVisibility(View.VISIBLE);

                            txt_location_label.setVisibility(View.VISIBLE);

                            txt_place.setVisibility(View.VISIBLE);

                            ll_map.setVisibility(View.VISIBLE);

                            ll_root.setVisibility(View.VISIBLE);

                            txt_review_count.setVisibility(View.VISIBLE);

                            view11.setVisibility(View.VISIBLE);
//                            bottomSheetLayouts.setVisibility(View.VISIBLE);

                         //   setBottomSheet();
                            if(response.body().getData().getReview_count() != 0){
                                txt_review_count.setText(""+response.body().getData().getAmount()+" Reviews");
                            }
                            else {

                                txt_review_count.setText("");
                            }

                            if(response.body().getData().getAmount() != 0){
                                txt_dr_consultationfees.setText("\u20B9 "+response.body().getData().getAmount());
                            }


                            avi_indicator.setVisibility(View.GONE);

//                            toolbar_header.setVisibility(View.VISIBLE);

                          /////////  scrollView.setVisibility(View.VISIBLE);

                          //  footerView.setVisibility(View.GONE);


                            if(response.body().getData().isFav()){
                                img_fav.setBackgroundResource(R.drawable.like);
                            }else{
                                img_fav.setBackgroundResource(R.drawable.icn_heart_love);
                            }
                        }



                        if(response.body().getData().getAmount() != 0){
                            txt_dr_consultationfees.setText("\u20B9 "+response.body().getData().getAmount());
                        }





                        if(clinicname != null){
                            txt_clinicname.setText(clinicname);
                        }
                        if(doctorname != null&&drtitle!=null){
                            txt_drname.setText(drtitle+" "+doctorname);

                        }


                        if(starcount != null ){

                            if(starcount == 1){
                                hand_img1.setBackgroundResource(R.drawable.ic_logo_color);
                                hand_img2.setBackgroundResource(R.drawable.ic_logo_graycolor);
                                hand_img3.setBackgroundResource(R.drawable.ic_logo_graycolor);
                                hand_img4.setBackgroundResource(R.drawable.ic_logo_graycolor);
                                hand_img5.setBackgroundResource(R.drawable.ic_logo_graycolor);
                            } else if(starcount == 2){
                                hand_img1.setBackgroundResource(R.drawable.ic_logo_color);
                                hand_img2.setBackgroundResource(R.drawable.ic_logo_color);
                                hand_img3.setBackgroundResource(R.drawable.ic_logo_graycolor);
                                hand_img4.setBackgroundResource(R.drawable.ic_logo_graycolor);
                                hand_img5.setBackgroundResource(R.drawable.ic_logo_graycolor);
                            }else if(starcount == 3){
                                hand_img1.setBackgroundResource(R.drawable.ic_logo_color);
                                hand_img2.setBackgroundResource(R.drawable.ic_logo_color);
                                hand_img3.setBackgroundResource(R.drawable.ic_logo_color);
                                hand_img4.setBackgroundResource(R.drawable.ic_logo_graycolor);
                                hand_img5.setBackgroundResource(R.drawable.ic_logo_graycolor);
                            }else if(starcount == 4){
                                hand_img1.setBackgroundResource(R.drawable.ic_logo_color);
                                hand_img2.setBackgroundResource(R.drawable.ic_logo_color);
                                hand_img3.setBackgroundResource(R.drawable.ic_logo_color);
                                hand_img4.setBackgroundResource(R.drawable.ic_logo_color);
                                hand_img5.setBackgroundResource(R.drawable.ic_logo_graycolor);
                            } else if(starcount == 5){
                                hand_img1.setBackgroundResource(R.drawable.ic_logo_color);
                                hand_img2.setBackgroundResource(R.drawable.ic_logo_color);
                                hand_img3.setBackgroundResource(R.drawable.ic_logo_color);
                                hand_img4.setBackgroundResource(R.drawable.ic_logo_color);
                                hand_img5.setBackgroundResource(R.drawable.ic_logo_color);
                            }

                        }
                        if(ClinicLocationname != null ){
                            txt_place.setText(ClinicLocationname+"");

                            latitude = response.body().getData().getClinic_lat();

                            longitude = response.body().getData().getClinic_long();

                            Log.w(TAG,"latitude"+ latitude );

                            Log.w(TAG,"longitude"+ longitude );

                            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                            mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                    .findFragmentById(R.id.map);


                            if(mapFragment != null){
                                mapFragment.getMapAsync(DoctorClinicDetailsActivity.this);
                            }



                        }

                        if(response.body().getData().getAmount() != 0){

                            txt_dr_consultationfees.setText("\u20B9 "+response.body().getData().getAmount());
                        }




                        if(Doctor_exp != 0) {
                            Log.w(TAG,"Doctor_exp : "+Doctor_exp);
                            //txt_dr_experience.setText(Doctor_exp+" Years");

                            sb.append(Doctor_exp);

                            sb.append(" Years experience");

                        }
                        sb.append(" | ");

                        if(distance != null && ClinicLocationname != null){

                            /*txt_distance.setText(""+distance);*/

                            sb.append(distance);

                            sb.append(" km Away");

                        }
                        else if(APIClient.DISTANCE != null && ClinicLocationname != null){

                            //txt_distance.setText(""+APIClient.DISTANCE);
                            sb.append(distance);

                            sb.append(" km Away");
                        }


                        if(sb!=null){
                            txt_dr_experience.setText(sb);

                        }

                        else {

                            txt_dr_experience.setText("");
                        }

                        Log.w(TAG,"Size"+doctorclinicdetailsResponseList.size());
                        Log.w(TAG,"doctorclinicdetailsResponseList : "+new Gson().toJson(doctorclinicdetailsResponseList));

                        if(doctorclinicdetailsResponseList != null && doctorclinicdetailsResponseList.size()>0){

                           // cl_banner.setVisibility(View.VISIBLE);

                            for (int i = 0; i < doctorclinicdetailsResponseList.size(); i++) {
                                doctorclinicdetailsResponseList.get(i).getClinic_pic();
                                Log.w(TAG, "RES" + ", " +  doctorclinicdetailsResponseList.get(i).getClinic_pic());
                            }


                            viewpageData(doctorclinicdetailsResponseList);

                        }

                        Log.w(TAG," Descri : "+response.body().getData().getDescri());

                        if(response.body().getData().getDescri() != null){
                            txt_dr_desc.setText(response.body().getData().getDescri());

                        }


                        if(response.body().getData().getSpecialization() != null&&response.body().getData().getSpecialization().size()>0){

                           // specializationBeanList = new ArrayList<>();

                            specializationBeanList=response.body().getData().getSpecialization();

                            Log.w(TAG,"SpecilaziationList : "+new Gson().toJson(specializationBeanList));

                            setSpecList(specializationBeanList,4);



                        }




                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<DoctorDetailsResponse> call, @NonNull Throwable t) {
                avi_indicator.smoothToHide();

                Log.w(TAG,"DoctorDetailsResponse flr"+"--->" + t.getMessage());
            }
        });

    }



    @SuppressLint({"LogNotTimber", "LongLogTag"})
    private DoctorDetailsRequest doctorDetailsRequest() {
        /*
         * user_id : 603e262e2c2b43125f8cb801
         * doctor_id : 603e31a02c2b43125f8cb806
         */
        DoctorDetailsRequest doctorDetailsRequest = new DoctorDetailsRequest();
        doctorDetailsRequest.setUser_id(userid);
        doctorDetailsRequest.setDoctor_id(doctorid);
        Log.w(TAG,"doctorDetailsRequest"+ "--->" + new Gson().toJson(doctorDetailsRequest));
        return doctorDetailsRequest;
    }
    private void viewpageData(List<DoctorDetailsResponse.DataBean.ClinicPicBean> doctorclinicdetailsResponseList) {
        tabLayout.setupWithViewPager(viewPager, true);

        ViewPagerClinicDetailsAdapter viewPagerClinicDetailsAdapter = new ViewPagerClinicDetailsAdapter(getApplicationContext(), doctorclinicdetailsResponseList);
        viewPager.setAdapter(viewPagerClinicDetailsAdapter);
        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == doctorclinicdetailsResponseList.size()) {
                currentPage = 0;
            }
            viewPager.setCurrentItem(currentPage++, false);
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
    public void onBackPressed() {
        super.onBackPressed();

        if(fromactivity != null && fromactivity.equalsIgnoreCase("CustomerCareFragment")){
            callDirections("4");
        }else if(fromactivity != null && fromactivity.equalsIgnoreCase("PetLoverDoctorNewFavAdapter")){
            Intent intent = new Intent(DoctorClinicDetailsActivity.this,PetloverFavListActivity.class);
            startActivity(intent);
        }else {
            Intent intent = new Intent(DoctorClinicDetailsActivity.this, CustomerDashboardActivity.class);
            startActivity(intent);

        }



    }

    public void callDirections(String tag){
        Intent intent = new Intent(DoctorClinicDetailsActivity.this, CustomerDashboardActivity.class);
        intent.putExtra("tag",tag);
        intent.putExtra("communication_type",communication_type);
        intent.putExtra("searchString",searchString);
        intent.putExtra("doctorid",doctorid);
        intent.putExtra("tag",tag);
        startActivity(intent);
        finish();

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_book_now:
                goto_PetAppointment_Doctor_Date_Time_Activity();
                break;
            case R.id.rl_back:
                onBackPressed();
                break;

        }

    }

    private  void goto_PetAppointment_Doctor_Date_Time_Activity(){
        Intent intent = new Intent(DoctorClinicDetailsActivity.this,PetAppointment_Doctor_Date_Time_Activity.class);
        intent.putExtra("doctorid",doctorid);
        intent.putExtra("fromactivity",fromactivity);
        intent.putExtra("amount",amount);
        intent.putExtra("communicationtype",communicationtype);
        intent.putExtra("distance",distance);
        intent.putExtra("doctorname",doctorname);
        intent.putExtra("clinicname",clinicname);
        startActivity(intent);
    }

    private void setSpecList(List<DoctorDetailsResponse.DataBean.SpecializationBean> specializationBeanList,int size) {

        int spanCount = 2; // 3 columns
        int spacing = 0; // 50px
        boolean includeEdge = true;
        rv_speclist.setLayoutManager(new GridLayoutManager(this, 2));
        rv_speclist.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        rv_speclist.setItemAnimator(new DefaultItemAnimator());
        doctorClinicSpecTypesListAdapter = new DoctorClinicSpecTypesListAdapter(DoctorClinicDetailsActivity.this, specializationBeanList,size);
        rv_speclist.setAdapter(doctorClinicSpecTypesListAdapter);

        if(specializationBeanList.size()>4){
            txt_seemore_spec.setVisibility(View.VISIBLE);
        }else {
            txt_seemore_spec.setVisibility(View.GONE);
        }


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this case, we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device.
     * This method will only be triggered once the user has installed
     Google Play services and returned to the app.
     */

    @SuppressLint({"LongLogTag", "LogNotTimber"})
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        Log.w(TAG,"Map latitude"+ latitude );

        Log.w(TAG,"Map longitude"+ longitude );

        if(latitude!=0&&longitude!=0){

            LatLng currentLocation = new LatLng(latitude, longitude);

            mMap.addMarker(new
                    MarkerOptions().position(currentLocation));

            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,15));
            // Zoom in, animating the camera.
            mMap.animateCamera(CameraUpdateFactory.zoomIn());
            // Zoom out to zoom level 10, animating with a duration of 2 seconds.
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(@NonNull LatLng latLng) {
                    Log.w(TAG,"mMap onclick : "+"latitude : "+latitude+" longitude : "+longitude+" ClinicLocationname : "+ClinicLocationname);
                    String strUri = "http://maps.google.com/maps?q=loc:" + latitude + "," + longitude + " (" + ClinicLocationname + ")";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strUri));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
                }
            });




        }

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