package com.triton.freshfish.customer;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.triton.freshfish.R;
import com.triton.freshfish.api.API;

import com.triton.freshfish.api.APIClient;
import com.triton.freshfish.api.RestApiInterface;
import com.triton.freshfish.fragmentcustomer.bottommenu.CustomerCareFragment;
import com.triton.freshfish.fragmentcustomer.bottommenu.CustomerCommunityFragment;
import com.triton.freshfish.fragmentcustomer.bottommenu.CustomerHomeFragment;
import com.triton.freshfish.fragmentcustomer.bottommenu.CustomerServicesFragment;
import com.triton.freshfish.fragmentcustomer.bottommenu.CustomerShopFragment;

import com.triton.freshfish.requestpojo.DefaultLocationRequest;
import com.triton.freshfish.responsepojo.DefaultLocationResponse;
import com.triton.freshfish.responsepojo.GetAddressResultResponse;

import com.triton.freshfish.service.GPSTracker;
import com.triton.freshfish.sessionmanager.SessionManager;

import com.triton.freshfish.utils.ConnectionDetector;
import com.triton.freshfish.utils.RestUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CustomerDashboardActivity extends CustomerNavigationDrawer implements Serializable,
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private String TAG = "CustomerDashboardActivity";


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.avi_indicator)
    AVLoadingIndicatorView avi_indicator;

 /*   @SuppressLint("NonConstantResourceId")
    @BindView(R.id.include_petlover_footer)
    View include_petlover_footer;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_location)
   public TextView txt_location;
*/
    /* Petlover Bottom Navigation */

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigation;

    final Fragment petHomeFragment = new CustomerHomeFragment();
    final Fragment petCareFragment = new CustomerCareFragment();
    final Fragment petServicesFragment = new CustomerServicesFragment();
    final Fragment vendorShopFragment = new CustomerShopFragment();
    final Fragment petCommunityFragment = new CustomerCommunityFragment();

    public static String active_tag = "1";


    Fragment active = petHomeFragment;
    String tag;

    String fromactivity;
    private int reviewcount;
    private String specialization;

    private static final int REQUEST_CHECK_SETTINGS_GPS = 0x1;
    private GoogleApiClient googleApiClient;
    Location mLastLocation;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private double latitude;
    private double longitude;
    public static String cityName;
    private Dialog dialog;
    private String userid;

    private int communication_type;
    private String searchString ;
    private String doctorid;


    @SuppressLint("LogNotTimber")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);
        ButterKnife.bind(this);
        Log.w(TAG,"onCreate-->");

        googleApiConnected();
        avi_indicator.setVisibility(View.GONE);


        SessionManager session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getProfileDetails();
        userid = user.get(SessionManager.KEY_ID);




        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            fromactivity = extras.getString("fromactivity");
            reviewcount = extras.getInt("reviewcount");
            specialization = extras.getString("specialization");

            communication_type = extras.getInt("communication_type");
            searchString = extras.getString("searchString");
            doctorid = extras.getString("doctorid");

            Log.w(TAG,"Bundle "+" communication_type : "+communication_type+ "searchString : "+searchString);



        }

        floatingActionButton.setImageResource(R.drawable.ic_hzhome_png);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                active = petHomeFragment;
                bottomNavigation.setSelectedItemId(R.id.home);
                loadFragment(new CustomerHomeFragment());
            }
        });

        bottomNavigation.getMenu().getItem(0).setCheckable(false);

        tag = getIntent().getStringExtra("tag");
        Log.w(TAG," tag : "+tag);
        if(tag != null){
            if(tag.equalsIgnoreCase("1")){
                active = petHomeFragment;
                bottomNavigation.setSelectedItemId(R.id.home);
                loadFragment(new CustomerHomeFragment());
            }else if(tag.equalsIgnoreCase("2")){
                active = vendorShopFragment;
                bottomNavigation.setSelectedItemId(R.id.shop);
                loadFragment(new CustomerShopFragment());
            }else if(tag.equalsIgnoreCase("3")){
                active = petServicesFragment;
                bottomNavigation.setSelectedItemId(R.id.services);
                loadFragment(new CustomerServicesFragment());
            }else if(tag.equalsIgnoreCase("4")){
                active = petCareFragment;
                bottomNavigation.setSelectedItemId(R.id.care);
                loadFragment(new CustomerCareFragment());
            } else if(tag.equalsIgnoreCase("5")){
                active = petCommunityFragment;
                bottomNavigation.setSelectedItemId(R.id.community);
                loadFragment(new CustomerCommunityFragment());
            }
        }
        else{
            bottomNavigation.setSelectedItemId(R.id.home);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_schedule, active, active_tag);
            transaction.commitNowAllowingStateLoss();
        }

        if(userid !=  null){
            if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
                defaultLocationResponseCall();
            }
        }


        bottomNavigation.setOnNavigationItemSelectedListener(this);

    }



    @SuppressLint("LogNotTimber")
    private void loadFragment(Fragment fragment) {
        Bundle bundle = new Bundle();
        if(fromactivity != null){
            Log.w(TAG,"fromactivity loadFragment : "+fromactivity);
            if(fromactivity.equalsIgnoreCase("FiltersActivity")) {
                bundle.putString("fromactivity", fromactivity);
                bundle.putString("specialization", specialization);
                bundle.putInt("reviewcount", reviewcount);
                bundle.putInt("communication_type", communication_type);
                // set Fragmentclass Arguments
                fragment.setArguments(bundle);
                Log.w(TAG,"fromactivity : "+fromactivity);
                // load fragment
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_schedule, fragment);
                transaction.addToBackStack(null);
                transaction.commitAllowingStateLoss();
            }
        }else {
            Log.w(TAG,"Else communication_type : "+communication_type+" searchString : "+searchString);
            bundle.putString("searchString", searchString);
            bundle.putInt("communication_type", communication_type);
            bundle.putString("doctorid", doctorid);

            // set Fragmentclass Arguments
            fragment.setArguments(bundle);

            // load fragment
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_schedule, fragment);
            transaction.addToBackStack(null);
            transaction.commitAllowingStateLoss();
        }
    }

    @Override
    public void onBackPressed() {
        Log.w(TAG,"tag : "+tag);
        if (bottomNavigation.getSelectedItemId() == R.id.home) {
            showExitAppAlert();
//            new android.app.AlertDialog.Builder(CustomerDashboardActivity.this)
//                    .setMessage("Are you sure you want to exit?")
//                    .setCancelable(false)
//                    .setPositiveButton("Yes", (dialog, id) -> CustomerDashboardActivity.this.finishAffinity())
//                    .setNegativeButton("No", null)
//                    .show();
        }
        else if(tag != null ){
            Log.w(TAG,"Else IF--->"+"fromactivity : "+fromactivity);
            if(fromactivity != null){


            }else{
                bottomNavigation.setSelectedItemId(R.id.home);
                // load fragment
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_schedule,new CustomerHomeFragment());
                transaction.commitNowAllowingStateLoss();
            }


        }else{
            bottomNavigation.setSelectedItemId(R.id.home);
            // load fragment
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_schedule,new CustomerHomeFragment());
            transaction.commitNowAllowingStateLoss();
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_schedule,fragment);
        transaction.commitNowAllowingStateLoss();
    }

    @SuppressLint("LogNotTimber")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        getMyLocation();
                        break;
                    case Activity.RESULT_CANCELED:
                        getMyLocation();
                        break;
                }
                break;
        }



        Fragment fragment = Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.frame_schedule));
        fragment.onActivityResult(requestCode,resultCode,data);
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String @NotNull [] permissions, @NotNull int @NotNull [] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    getMyLocation();

                }
            } else {
                Toast.makeText(getApplicationContext(), "permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void getLatandLong() {
        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(CustomerDashboardActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {
                GPSTracker gps = new GPSTracker(getApplicationContext());
                // Check if GPS enabled
                if (gps.canGetLocation()) {
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();

                    if(latitude != 0 && longitude != 0){
                        LatLng latLng = new LatLng(latitude,longitude);
                        getAddressResultResponse(latLng);


                    }




                }
            }




        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void googleApiConnected() {

        googleApiClient = new GoogleApiClient.Builder(Objects.requireNonNull(getApplicationContext())).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).
                addApi(LocationServices.API).build();
        googleApiClient.connect();

    }
    private void checkLocation() {
        try {
            LocationManager lm = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            boolean network_enabled = false;

            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ignored) {
            }

            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ignored) {
            }

            if (!gps_enabled && !network_enabled) {

                if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    getMyLocation();
                }

            } else {
                getLatandLong();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        latitude = mLastLocation.getLatitude();
        longitude = mLastLocation.getLongitude();







    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        permissionChecking();
    }
    @Override
    public void onConnectionSuspended(int i) {

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @SuppressLint("LongLogTag")
    @Override
    public void onMapReady(GoogleMap googleMap) {


    }
    private void permissionChecking() {
        if (getApplicationContext() != null) {
            if (Build.VERSION.SDK_INT >= 23 && (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) &&
                    (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

                ActivityCompat.requestPermissions(CustomerDashboardActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 5);

            } else {

                checkLocation();
            }
        }
    }
    public void getMyLocation() {
        if (googleApiClient != null) {

            if (googleApiClient.isConnected()) {
                if(getApplicationContext() != null){
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                }

                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                LocationRequest locationRequest = new LocationRequest();
                locationRequest.setInterval(2000);
                locationRequest.setFastestInterval(2000);
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
                builder.setAlwaysShow(true);
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
                PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
                result.setResultCallback(result1 -> {
                    Status status = result1.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            // All location settings are satisfied.
                            // You can initialize location requests here.
                            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);







                            Handler handler = new Handler();
                            int delay = 1000; //milliseconds

                            handler.postDelayed(new Runnable() {
                                @SuppressLint({"LongLogTag", "LogNotTimber"})
                                public void run() {
                                    //do something
                                    if(getApplicationContext() != null) {
                                        if(latitude != 0 && longitude != 0) {
                                            LatLng latLng = new LatLng(latitude,longitude);
                                            getAddressResultResponse(latLng);

                                        }
                                    }
                                }
                            }, delay);


                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                status.startResolutionForResult(CustomerDashboardActivity.this, REQUEST_CHECK_SETTINGS_GPS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                });
            }


        }
    }
    private void getAddressResultResponse(LatLng latLng) {
        //avi_indicator.setVisibility(View.VISIBLE);
        // avi_indicator.smoothToShow();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        API service = retrofit.create(API.class);
        String strlatlng = String.valueOf(latLng);
        String newString = strlatlng.replace("lat/lng:", "");

        String latlngs = newString.trim().replaceAll("\\(", "").replaceAll("\\)","").trim();



        String key = API.MAP_KEY;
        service.getAddressResultResponseCall(latlngs, key).enqueue(new Callback<GetAddressResultResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NotNull Call<GetAddressResultResponse> call, @NotNull Response<GetAddressResultResponse> response) {
                //avi_indicator.smoothToHide();
                Log.w(TAG,"url  :%s"+ call.request().url().toString());

                try{



                    if(response.body() != null) {
                        String currentplacename = null;
                        String compundcode = null;

                        if(response.body().getPlus_code().getCompound_code() != null){
                            compundcode = response.body().getPlus_code().getCompound_code();
                        }
                        if(compundcode != null) {
                            String[] separated = compundcode.split(",");
                            String placesname = separated[0];
                            String[] splitData = placesname.split("\\s", 2);
                            String code = splitData[0];
                            currentplacename = splitData[1];
                            Log.w(TAG,"currentplacename : "+currentplacename);
                        }




                        String localityName = null;
                        String sublocalityName = null;
                        String CityName = null;
                        String postalCode;


                        List<GetAddressResultResponse.ResultsBean> getAddressResultResponseList;
                        getAddressResultResponseList = response.body().getResults();
                        if (getAddressResultResponseList.size() > 0) {
                            String AddressLine = getAddressResultResponseList.get(0).getFormatted_address();

                        }
                        List<GetAddressResultResponse.ResultsBean.AddressComponentsBean> addressComponentsBeanList = response.body().getResults().get(0).getAddress_components();
                        if(addressComponentsBeanList != null) {
                            if (addressComponentsBeanList.size() > 0) {
                                for (int i = 0; i < addressComponentsBeanList.size(); i++) {

                                    for (int j = 0; j < addressComponentsBeanList.get(i).getTypes().size(); j++) {

                                        List<String> typesList = addressComponentsBeanList.get(i).getTypes();

                                        if (typesList.contains("postal_code")) {
                                            postalCode = addressComponentsBeanList.get(i).getShort_name();
                                            String PostalCode = postalCode;

                                        }
                                        if (typesList.contains("locality")) {
                                            CityName = addressComponentsBeanList.get(i).getLong_name();
                                            localityName = addressComponentsBeanList.get(i).getShort_name();
                                            Log.w(TAG,"CityName : "+CityName+"localityName : "+localityName);


                                        }

                                   /* if(currentplacename != null){
                                        txt_location.setText(currentplacename);
                                    }else if(CityName != null){
                                        txt_location.setText(CityName);
                                    }else if(localityName != null){
                                        txt_location.setText(localityName);
                                    }else{
                                        txt_location.setText("");
                                    }
*/
                                        if (typesList.contains("administrative_area_level_2")) {
                                            cityName = addressComponentsBeanList.get(i).getShort_name();
                                            //  CityName = cityName;




                                        }
                                        if (typesList.contains("sublocality_level_1")) {
                                            sublocalityName = addressComponentsBeanList.get(i).getShort_name();
                                            Log.w(TAG,"sublocalityName : "+sublocalityName);

                                        }

                                    }

                                }





                            }
                        }
                    }

                }
                catch (Exception e) {

                    e.printStackTrace();

                }




            }

            @Override
            public void onFailure(@NotNull Call<GetAddressResultResponse> call, @NotNull Throwable t) {
                //avi_indicator.smoothToHide();
                t.printStackTrace();
            }
        });
    }


    private void showExitAppAlert() {
        try {

            dialog = new Dialog(CustomerDashboardActivity.this);
            dialog.setContentView(R.layout.alert_exit_layout);
            Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
            Button btn_exit = dialog.findViewById(R.id.btn_exit);

            btn_exit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    CustomerDashboardActivity.this.finishAffinity();
                }
            });
            btn_cancel.setOnClickListener(new View.OnClickListener() {
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

    private void showComingSoonAlert() {

        try {

            Dialog dialog = new Dialog(CustomerDashboardActivity.this);
            dialog.setContentView(R.layout.alert_comingsoon_layout);
            dialog.setCanceledOnTouchOutside(false);

            ImageView img_close = dialog.findViewById(R.id.img_close);
            img_close.setOnClickListener(new View.OnClickListener() {
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



    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.rl_homes:
                active_tag = "1";

                replaceFragment(new CustomerHomeFragment());
                break;

            case R.id.rl_home:
                active_tag = "1";

                replaceFragment(new CustomerHomeFragment());
                break;

            case R.id.rl_shop:
                active_tag = "2";

                replaceFragment(new CustomerShopFragment());
                break;

            case R.id.rl_service:
                active_tag = "3";

                replaceFragment(new CustomerServicesFragment());
                break;


            case R.id.rl_care:
                active_tag = "4";

                replaceFragment(new CustomerCareFragment());
                break;

            case R.id.rl_comn:
                active_tag = "5";

                replaceFragment(new CustomerCommunityFragment());
                break;
        }

        }

    private void setMargins(RelativeLayout rl_layout, int i, int i1, int i2, int i3) {

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)rl_layout.getLayoutParams();
        params.setMargins(i, i1, i2, i3);
        rl_layout.setLayoutParams(params);
    }


    @SuppressLint("LogNotTimber")
    private void defaultLocationResponseCall() {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<DefaultLocationResponse> call = apiInterface.defaultLocationResponseCall(RestUtils.getContentType(), defaultLocationRequest());
        Log.w(TAG,"SignupResponse url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<DefaultLocationResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<DefaultLocationResponse> call, @NonNull Response<DefaultLocationResponse> response) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"DefaultLocationResponse" + new Gson().toJson(response.body()));
                if (response.body() != null) {

                    if (200 == response.body().getCode()) {
                        if(response.body().getData() != null) {
                            if (response.body().getData().getLocation_city() != null) {
                                txt_location.setText(response.body().getData().getLocation_city());
                            }
                        }



                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<DefaultLocationResponse> call, @NonNull Throwable t) {
                avi_indicator.smoothToHide();
                Log.e("DefaultLocationResponse flr", "--->" + t.getMessage());
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

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.w(TAG,"onRestart-->");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG,"onResume-->");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                active_tag = "1";
                item.setCheckable(true);
                replaceFragment(new CustomerHomeFragment());
                break;
            case R.id.shop:
                active_tag = "2";
                item.setCheckable(true);
                replaceFragment(new CustomerShopFragment());
                break;
            case R.id.services:
                active_tag = "3";
                item.setCheckable(true);
                replaceFragment(new CustomerServicesFragment());
                break;
            case R.id.care:
                active_tag = "4";
                item.setCheckable(true);
                replaceFragment(new CustomerCareFragment());
                break;
            case R.id.community:
                active_tag = "5";
                item.setCheckable(true);
                replaceFragment(new CustomerCommunityFragment());
                break;

            default:
                return  false;
        }
        return true;
    }
}