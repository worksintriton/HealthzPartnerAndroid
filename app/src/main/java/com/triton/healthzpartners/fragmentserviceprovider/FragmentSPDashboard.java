package com.triton.healthzpartners.fragmentserviceprovider;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.triton.healthzpartners.R;
import com.triton.healthzpartners.api.APIClient;
import com.triton.healthzpartners.api.RestApiInterface;


import com.triton.healthzpartners.fragmentserviceprovider.myappointments.FragmentSPCompletedAppointment;
import com.triton.healthzpartners.fragmentserviceprovider.myappointments.FragmentSPMissedAppointment;
import com.triton.healthzpartners.fragmentserviceprovider.myappointments.FragmentSPNewAppointment;
import com.triton.healthzpartners.requestpojo.SPCheckStatusRequest;
import com.triton.healthzpartners.requestpojo.SPDetailsByUserIdRequest;
import com.triton.healthzpartners.responsepojo.SPCheckStatusResponse;
import com.triton.healthzpartners.responsepojo.ServiceProviderRegisterFormCreateResponse;
import com.triton.healthzpartners.serviceprovider.SPEditProfileActivity;
import com.triton.healthzpartners.serviceprovider.SPMyCalendarNewUserActivity;
import com.triton.healthzpartners.serviceprovider.SPMyappointmentsActivity;
import com.triton.healthzpartners.serviceprovider.SPProfileScreenActivity;
import com.triton.healthzpartners.serviceprovider.ServiceProviderDashboardActivity;
import com.triton.healthzpartners.serviceprovider.ServiceProviderRegisterFormActivity;
import com.triton.healthzpartners.sessionmanager.SessionManager;
import com.triton.healthzpartners.utils.ConnectionDetector;
import com.triton.healthzpartners.utils.RestUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentSPDashboard extends Fragment  {

    private   String TAG = "FragmentSPDashboard";

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.avi_indicator)
    AVLoadingIndicatorView avi_indicator;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tablayout)
    TabLayout tablayout;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.viewPager)
    ViewPager viewPager;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_myappointments)
    TextView txt_myappointments;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_view_profile)
    TextView txt_view_profile;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_edit_profile)
    TextView txt_edit_profile;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_service_name)
    TextView txt_service_name;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_service_imge)
    ImageView img_service_imge;

    private SharedPreferences preferences;
    private Context mContext;
    private String userid;
    private boolean isDoctorStatus = false;
    SessionManager session;
    private boolean isProfileUpdatedClose;

    private int someIndex = 0;
    private List<ServiceProviderRegisterFormCreateResponse.DataBean.BusServiceGallBean> servieGalleryResponseList;

    public FragmentSPDashboard() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sp_dashboard, container, false);


        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        ButterKnife.bind(this, view);
        mContext = getActivity();
        avi_indicator.setVisibility(View.GONE);

         session = new SessionManager(mContext);
        HashMap<String, String> user = session.getProfileDetails();
        userid = user.get(SessionManager.KEY_ID);
        Log.w(TAG,"userid : "+userid);



        if(userid != null){
            if (new ConnectionDetector(getActivity()).isNetworkAvailable(getActivity())) {
                SPCheckStatusResponseCall();
            }
        }

        txt_myappointments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, SPMyappointmentsActivity.class));
            }
        });
        txt_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, SPEditProfileActivity.class));
            }
        });
        txt_view_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, SPProfileScreenActivity.class));
            }
        });




        return view;


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager(),3);
        adapter.addFragment(new FragmentSPNewAppointment(), "New");
        adapter.addFragment(new FragmentSPCompletedAppointment(), "Completed");
        adapter.addFragment(new FragmentSPMissedAppointment(), "Missed");
        viewPager.setAdapter(adapter);
    }




    static class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager,int number) {
            super(manager,number);
        }

        @Override
        public @NotNull Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }



    @SuppressLint("LogNotTimber")
    private void SPCheckStatusResponseCall() {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<SPCheckStatusResponse> call = apiInterface.SPCheckStatusResponseCall(RestUtils.getContentType(), spCheckStatusRequest());
        Log.w(TAG,"SPCheckStatusResponse url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<SPCheckStatusResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<SPCheckStatusResponse> call, @NonNull Response<SPCheckStatusResponse> response) {

                Log.w(TAG,"SPCheckStatusResponse"+ "--->" + new Gson().toJson(response.body()));

                avi_indicator.smoothToHide();

                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        if(response.body().getData() != null) {
                            if (!response.body().getData().isProfile_status()) {
                                Intent intent = new Intent(mContext, ServiceProviderRegisterFormActivity.class);
                                intent.putExtra("fromactivity", TAG);
                                startActivity(intent);
                            }
                            else if (!response.body().getData().isCalender_status()) {
                                Intent intent = new Intent(mContext, SPMyCalendarNewUserActivity.class);
                                intent.putExtra("fromactivity", TAG);
                                startActivity(intent);
                            }
                            else {
                                String profileVerificationStatus = response.body().getData().getProfile_verification_status();
                                if (profileVerificationStatus != null && profileVerificationStatus.equalsIgnoreCase("Not verified")) {
                                    showProfileStatus(response.body().getMessage());

                                } else if (profileVerificationStatus != null && profileVerificationStatus.equalsIgnoreCase("profile updated")) {
                                    if (!session.isProfileUpdate()) {
                                        showProfileUpdateStatus(response.body().getMessage());

                                    }

                                } else {
                                    isDoctorStatus = true;
                                    Log.w(TAG, "isDoctorStatus else : " + isDoctorStatus);

                                    spDetailsReponseByUserIdCall();

                                    Log.w(TAG,"appintments : "+ServiceProviderDashboardActivity.appintments);

                                    if (isDoctorStatus) {
                                        //setupViewPager(viewPager);
                                        if(ServiceProviderDashboardActivity.appintments != null && ServiceProviderDashboardActivity.appintments.equalsIgnoreCase("New")){
                                            someIndex = 0;
                                        }
                                        else if(ServiceProviderDashboardActivity.appintments != null && ServiceProviderDashboardActivity.appintments.equalsIgnoreCase("Completed")){
                                            someIndex = 1;
                                        }
                                        else if(ServiceProviderDashboardActivity.appintments != null && ServiceProviderDashboardActivity.appintments.equalsIgnoreCase("Missed")){
                                            someIndex = 2;
                                        }

                                        tablayout.setupWithViewPager(viewPager);
                                        TabLayout.Tab tab = tablayout.getTabAt(someIndex);
                                        if (tab != null) {
                                            tab.select();
                                        }


                                    }

                                }


                            }
                        }

                    }

                }


            }

            @Override
            public void onFailure(@NonNull Call<SPCheckStatusResponse> call, @NonNull Throwable t) {

                avi_indicator.smoothToHide();
                Log.w(TAG,"SPCheckStatusResponse flr"+"--->" + t.getMessage());
            }
        });

    }
    @SuppressLint("LogNotTimber")
    private SPCheckStatusRequest spCheckStatusRequest() {
        SPCheckStatusRequest spCheckStatusRequest = new SPCheckStatusRequest();
        spCheckStatusRequest.setUser_id(userid);
        Log.w(TAG,"spCheckStatusRequest"+ "--->" + new Gson().toJson(spCheckStatusRequest));
        return spCheckStatusRequest;
    }
    private void showProfileStatus(String message) {

        try {

            Dialog dialog = new Dialog(mContext);
            dialog.setContentView(R.layout.alert_no_internet_layout);
            dialog.setCancelable(false);
            Button dialogButton = dialog.findViewById(R.id.btnDialogOk);
            dialogButton.setText("Refresh");
            TextView tvInternetNotConnected = dialog.findViewById(R.id.tvInternetNotConnected);
            tvInternetNotConnected.setText(message);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (new ConnectionDetector(getActivity()).isNetworkAvailable(getActivity())) {
                        SPCheckStatusResponseCall();
                    }
                    dialog.dismiss();

                }
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }




    }
    private void showProfileUpdateStatus(String message) {

        try {

            Dialog dialog = new Dialog(mContext);
            dialog.setContentView(R.layout.alert_profile_update_layout);
            dialog.setCancelable(false);
            Button dialogButton = dialog.findViewById(R.id.btnDialogOk);
            dialogButton.setText("Refresh");
            TextView tvInternetNotConnected = dialog.findViewById(R.id.tvInternetNotConnected);
            tvInternetNotConnected.setText(message);
            ImageView img_close = dialog.findViewById(R.id.img_close);

            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (new ConnectionDetector(getActivity()).isNetworkAvailable(getActivity())) {
                        SPCheckStatusResponseCall();
                    }
                    dialog.dismiss();

                }
            });

            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    session.setIsProfileUpdate(true);
                    isProfileUpdatedClose = true;
                    dialog.dismiss();

                }
            });
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }




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
                           String  servicename = response.body().getData().getBussiness_name();
                            txt_service_name.setText(servicename);
                        }





                        if(servieGalleryResponseList != null && servieGalleryResponseList.size()>0){

                            for (int i = 0; i < servieGalleryResponseList.size(); i++) {
                                servieGalleryResponseList.get(i).getBus_service_gall();

                                if (servieGalleryResponseList.get(i).getBus_service_gall() != null && !servieGalleryResponseList.get(i).getBus_service_gall().isEmpty()) {
                                    Glide.with(mContext)
                                            .load(servieGalleryResponseList.get(i).getBus_service_gall())
                                            .into(img_service_imge);

                                }
                                else{
                                    Glide.with(mContext)
                                            .load(APIClient.PROFILE_IMAGE_URL)
                                            .into(img_service_imge);

                                }
                            }




                        }




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



}