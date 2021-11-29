package com.triton.healthZpartners.serviceprovider;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.triton.healthZpartners.R;
import com.triton.healthZpartners.activity.LoginActivity;
import com.triton.healthZpartners.activity.NotificationActivity;
import com.triton.healthZpartners.adapter.ViewPagerSPGalleryDetailsAdapter;
import com.triton.healthZpartners.api.APIClient;
import com.triton.healthZpartners.api.RestApiInterface;

import com.triton.healthZpartners.requestpojo.DefaultLocationRequest;
import com.triton.healthZpartners.requestpojo.SPDetailsByUserIdRequest;
import com.triton.healthZpartners.responsepojo.PetListResponse;
import com.triton.healthZpartners.responsepojo.ServiceProviderRegisterFormCreateResponse;
import com.triton.healthZpartners.responsepojo.SuccessResponse;
import com.triton.healthZpartners.serviceprovider.shop.SPMyOrdrersActivity;
import com.triton.healthZpartners.sessionmanager.SessionManager;
import com.triton.healthZpartners.utils.ConnectionDetector;
import com.triton.healthZpartners.utils.RestUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SPProfileScreenActivity extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private  String TAG = "SPProfileScreenActivity";



    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_profile)
    ImageView img_profile;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_edit_image)
    TextView txt_edit_image;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_usrname)
    TextView txt_usrname;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_name)
    TextView txt_name;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_business_number)
    TextView txt_business_number;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_business_name)
    TextView txt_business_name;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_business_email)
    TextView txt_business_email;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_mail)
    TextView txt_mail;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_phn_num)
    TextView txt_phn_num;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_edit_profile)
    TextView txt_edit_profile;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_profile1)
    ImageView img_profile1;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_manage_address)
    TextView txt_manage_address;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_change_password)
    TextView txt_change_password;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.avi_indicator)
    AVLoadingIndicatorView avi_indicator;



    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_logout)
    TextView txt_logout;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.pager)
    ViewPager viewPager;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tabDots)
    TabLayout tabLayout;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_edit_doc_business_info)
    TextView txt_edit_doc_business_info;

 /*   @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_myservices)
    TextView txt_myservices;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_sp_specialization)
    TextView txt_sp_specialization;
*/
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.include_petlover_header)
    View include_petlover_header;

    /*@SuppressLint("NonConstantResourceId")
    @BindView(R.id.include_doctor_footer)
    View include_doctor_footer;*/

    private SessionManager session;
    String name,emailid,phoneNo,userid;
    private List<PetListResponse.DataBean> petList;
    private Dialog dialog;
    private String clinicname,doctorname;
    private String concatenatedSpcNames = "";
    private String concatenatedServiceNames = "";

    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 3000;
    private String profileimage;
    private List<ServiceProviderRegisterFormCreateResponse.DataBean.BusServiceGallBean> servieGalleryResponseList;
    private String fromactivity;
    private String appointment_id;
    private String bookedat;
    private String from;

/*
    */
/* Bottom Navigation *//*


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rl_home)
    RelativeLayout rl_home;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rl_service)
    RelativeLayout rl_service;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rl_shop)
    RelativeLayout rl_shop;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.title_shop)
    TextView title_shop;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_shop)
    ImageView img_shop;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rl_comn)
    RelativeLayout rl_comn;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.title_community)
    TextView title_community;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_business_name)
    TextView txt_business_name;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_community)
    ImageView img_community;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rl_homes)
    RelativeLayout rl_homes;

*/

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigation;

    public static String active_tag = "1";

    @SuppressLint("LogNotTimber")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_profile_screen);
        ButterKnife.bind(this);

        Log.w(TAG,"onCreate : ");
        avi_indicator.setVisibility(View.GONE);


        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getProfileDetails();
        String firstname = user.get(SessionManager.KEY_FIRST_NAME);
        String lastname = user.get(SessionManager.KEY_LAST_NAME);
        name = firstname+" "+lastname;
        emailid = user.get(SessionManager.KEY_EMAIL_ID);
        phoneNo = user.get(SessionManager.KEY_MOBILE);
        userid = user.get(SessionManager.KEY_ID);
        profileimage = user.get(SessionManager.KEY_PROFILE_IMAGE);



        Log.w(TAG,"session profileimage : "+profileimage);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fromactivity = extras.getString("fromactivity");
            /*SPAppointmentDetailsActivity*/
            appointment_id = extras.getString("appointment_id");
            fromactivity = extras.getString("fromactivity");
            bookedat = extras.getString("bookedat");
            from = extras.getString("from");


        }

        ImageView img_back = include_petlover_header.findViewById(R.id.img_back);
        ImageView img_sos = include_petlover_header.findViewById(R.id.img_sos);
        ImageView img_notification = include_petlover_header.findViewById(R.id.img_notification);
        ImageView img_cart = include_petlover_header.findViewById(R.id.img_cart);
        ImageView img_profile = include_petlover_header.findViewById(R.id.img_profile);
        TextView toolbar_title = include_petlover_header.findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.profile));

        img_back.setOnClickListener(v -> onBackPressed());


        img_sos.setVisibility(View.GONE);
        img_cart.setVisibility(View.GONE);
        img_profile.setVisibility(View.GONE);
        img_notification.setVisibility(View.GONE);

        img_notification.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), NotificationActivity.class)));

     /*   //bottom_navigation_view.getMenu().findItem(R.id.home).setChecked(true);
        *//*home*//*

        title_shop.setTextColor(getResources().getColor(R.color.darker_grey_new,getTheme()));
        img_shop.setImageResource(R.drawable.grey_shop);
        title_community.setTextColor(getResources().getColor(R.color.darker_grey_new,getTheme()));
        img_community.setImageResource(R.drawable.grey_community);

        rl_home.setOnClickListener(this);
        rl_shop.setOnClickListener(this);
        rl_comn.setOnClickListener(this);
        rl_homes.setOnClickListener(this);
*/
        bottomNavigation.setSelectedItemId(R.id.shop);

        floatingActionButton.setImageResource(R.drawable.ic_hzhome_png);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                callDirections("1");
            }
        });

        bottomNavigation.setOnNavigationItemSelectedListener(this);


        txt_usrname.setText(name);
        txt_name.setText(name);
        txt_mail.setText(emailid);
        txt_business_email.setText(emailid);
        txt_phn_num.setText(phoneNo);
        txt_business_number.setText(phoneNo);


        if(profileimage != null && !profileimage.isEmpty()){
            Log.w(TAG,"session profileimage if : "+profileimage);
            Glide.with(SPProfileScreenActivity.this)
                    .load(profileimage)
                    .into(img_profile1);
        }else{
            Log.w(TAG,"session profileimage else : "+profileimage);
            Glide.with(SPProfileScreenActivity.this)
                    .load(R.drawable.upload)
                    .into(img_profile1);

        }






        if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
            spDetailsReponseByUserIdCall();
        }


        img_back.setOnClickListener(this);
        txt_manage_address.setOnClickListener(this);
        txt_change_password.setOnClickListener(this);
        txt_logout.setOnClickListener(this);
        txt_edit_profile.setOnClickListener(this);
        txt_edit_image.setOnClickListener(this);
        txt_edit_doc_business_info.setOnClickListener(this);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(fromactivity != null && fromactivity.equalsIgnoreCase("SPMyOrdrersActivity")){
            startActivity(new Intent(getApplicationContext(), SPMyOrdrersActivity.class));
            finish();
        }if(from != null && from.equalsIgnoreCase("SPAppointmentDetailsActivity")){
            Intent intent = new Intent(new Intent(getApplicationContext(), SPAppointmentDetailsActivity.class));
            intent.putExtra("appointment_id",appointment_id);
            intent.putExtra("fromactivity",fromactivity);
            intent.putExtra("bookedat",bookedat);
            intent.putExtra("from",TAG);
            startActivity(intent);
        }else{
            startActivity(new Intent(getApplicationContext(), ServiceProviderDashboardActivity.class));
            finish();
        }

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                onBackPressed();
                break;
                case R.id.txt_edit_profile:
                startActivity(new Intent(getApplicationContext(), SPEditProfileActivity.class));
                break;
                case R.id.txt_edit_image:
                startActivity(new Intent(getApplicationContext(), SPEditProfileImageActivity.class));
                break;
                case R.id.txt_edit_doc_business_info:
                startActivity(new Intent(getApplicationContext(), ServiceProviderEditFormActivity.class));
                break;
                case R.id.txt_manage_address:
                    gotoManageAddress();
                break;
                case R.id.txt_change_password:
                break;
                case R.id.txt_logout:
                    //confirmLogoutDialog();
                    showLogOutAppAlert();
                break;

            case R.id.rl_homes:
                callDirections("1");
                break;

            case R.id.rl_home:
                callDirections("1");
                break;

            case R.id.rl_shop:
                callDirections("2");
                break;

            case R.id.rl_comn:
                callDirections("3");
                break;

        }
    }

    private void showLogOutAppAlert() {
        try {

            dialog = new Dialog(SPProfileScreenActivity.this);
            dialog.setContentView(R.layout.alert_logout_layout);
            Button btn_no = dialog.findViewById(R.id.btn_no);
            Button btn_yes = dialog.findViewById(R.id.btn_yes);

            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    gotoLogout();

                }
            });
            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }




    }




    private void gotoManageAddress() {
        startActivity(new Intent(SPProfileScreenActivity.this, ManageAddressSPActivity.class));
    }

    private void confirmLogoutDialog(){
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(SPProfileScreenActivity.this);
        alertDialogBuilder.setMessage("Are you sure want to logout?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1)
                    {

                        gotoLogout();


                    }
                });

        alertDialogBuilder.setNegativeButton("No", (dialog, which) -> alertDialogBuilder.setCancelable(true));

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
    private void gotoLogout() {
       /* session.logoutUser();
        session.setIsLogin(false);
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();*/
        logoutResponseCall();



    }

    @SuppressLint("LogNotTimber")
    private void logoutResponseCall() {
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<SuccessResponse> call = apiInterface.logoutResponseCall(RestUtils.getContentType(), defaultLocationRequest());
        Log.w(TAG,"SignupResponse url  :%s"+" "+ call.request().url().toString());
        call.enqueue(new Callback<SuccessResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                Log.w(TAG,"SuccessResponse" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    if (200 == response.body().getCode()) {
                        session.logoutUser();
                        session.setIsLogin(false);
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();


                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<SuccessResponse> call,@NonNull Throwable t) {

                Log.e("SuccessResponse flr", "--->" + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private DefaultLocationRequest defaultLocationRequest() {
        DefaultLocationRequest defaultLocationRequest = new DefaultLocationRequest();
        defaultLocationRequest.setUser_id(userid);

        Log.w(TAG,"defaultLocationRequest "+ new Gson().toJson(defaultLocationRequest));
        return defaultLocationRequest;
    }






    @SuppressLint("LogNotTimber")
    private void spDetailsReponseByUserIdCall() {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<ServiceProviderRegisterFormCreateResponse> call = ApiService.spDetailsReponseByUserIdCall(RestUtils.getContentType(),spDetailsByUserIdRequest());
        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<ServiceProviderRegisterFormCreateResponse>() {
            @Override
            public void onResponse(@NonNull Call<ServiceProviderRegisterFormCreateResponse> call, @NonNull Response<ServiceProviderRegisterFormCreateResponse> response) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"ServiceProviderRegisterFormCreateResponse"+ "--->" + new Gson().toJson(response.body()));


               if (response.body() != null) {
                    if(200 == response.body().getCode()){
                        if(response.body().getData().getBus_service_gall() != null) {
                            servieGalleryResponseList = response.body().getData().getBus_service_gall();
                        }
                        if(response.body().getData().getBussiness_name() != null) {
                            clinicname = response.body().getData().getBussiness_name();
                            txt_business_name.setText(clinicname);
                        }





                        if(servieGalleryResponseList != null && servieGalleryResponseList.size()>0){

                            for (int i = 0; i < servieGalleryResponseList.size(); i++) {
                                servieGalleryResponseList.get(i).getBus_service_gall();
                            }


                            viewpageData(servieGalleryResponseList);

                        }


                    /*    if(response.body().getData().getBus_spec_list() != null){
                            for (int i = 0; i < response.body().getData().getBus_spec_list().size(); i++) {
                                concatenatedSpcNames += response.body().getData().getBus_spec_list().get(i).getBus_spec_list();
                                if (i < response.body().getData().getBus_spec_list().size() - 1) concatenatedSpcNames += ", ";
                            }
                            txt_sp_specialization.setText(concatenatedSpcNames);

                        }
                        if(response.body().getData().getBus_service_list() != null){
                            for (int i = 0; i < response.body().getData().getBus_service_list().size(); i++) {
                                concatenatedServiceNames += response.body().getData().getBus_service_list().get(i).getBus_service_list();
                                if (i < response.body().getData().getBus_service_list().size() - 1) concatenatedServiceNames += ", ";
                            }
                            txt_myservices.setText(concatenatedServiceNames);

                        }*/

                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<ServiceProviderRegisterFormCreateResponse> call, @NonNull Throwable t) {
                avi_indicator.smoothToHide();

                Log.w(TAG,"ServiceProviderRegisterFormCreateResponse flr"+"--->" + t.getMessage());
            }
        });

    }
    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private SPDetailsByUserIdRequest spDetailsByUserIdRequest() {
        /* user_id : 5fc61b82b750da703e48da78 */
        SPDetailsByUserIdRequest spDetailsByUserIdRequest = new SPDetailsByUserIdRequest();
        spDetailsByUserIdRequest.setUser_id(userid);
        Log.w(TAG,"spDetailsByUserIdRequest"+ "--->" + new Gson().toJson(spDetailsByUserIdRequest));
        return spDetailsByUserIdRequest;
    }
    private void viewpageData(List<ServiceProviderRegisterFormCreateResponse.DataBean.BusServiceGallBean> servieGalleryResponseList) {
        tabLayout.setupWithViewPager(viewPager, true);
        ViewPagerSPGalleryDetailsAdapter viewPagerSPGalleryDetailsAdapter = new ViewPagerSPGalleryDetailsAdapter(getApplicationContext(), servieGalleryResponseList);
        viewPager.setAdapter(viewPagerSPGalleryDetailsAdapter);
        /*After setting the adapter use the timer */
        final Handler handler = new Handler();
        final Runnable Update =  new Runnable() {
            public void run() {
                if (currentPage == servieGalleryResponseList.size()) {
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


    public void callDirections(String tag){
        Intent intent = new Intent(getApplicationContext(), ServiceProviderDashboardActivity.class);
        intent.putExtra("tag",tag);
        startActivity(intent);
        finish();
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