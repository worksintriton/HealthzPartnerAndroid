package com.triton.healthzpartners.customer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.triton.healthzpartners.R;
import com.triton.healthzpartners.activity.LoginActivity;
import com.triton.healthzpartners.activity.NotificationActivity;
import com.triton.healthzpartners.activity.SoSActivity;
import com.triton.healthzpartners.activity.location.ManageAddressActivity;
import com.triton.healthzpartners.api.APIClient;
import com.triton.healthzpartners.api.RestApiInterface;
import com.triton.healthzpartners.requestpojo.DefaultLocationRequest;
import com.triton.healthzpartners.responsepojo.PetLoverDashboardResponse;
import com.triton.healthzpartners.responsepojo.SuccessResponse;
import com.triton.healthzpartners.sessionmanager.SessionManager;
import com.triton.healthzpartners.utils.RestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CustomerNavigationDrawer extends AppCompatActivity implements View.OnClickListener{
         private String TAG ="CustomerNavigationDrawer";

    private DrawerLayout drawerLayout;
    LayoutInflater inflater;
    View view, header;
    Toolbar toolbar;

    // Make sure to be using android.support.v7.app.ActionBarDrawerToggle version.
    // The android.support.v4.app.ActionBarDrawerToggle has been deprecated.
    private ActionBarDrawerToggle drawerToggle;
    ImageView drawerImg;
    CircleImageView nav_header_imageView;
    FrameLayout frameLayout;
    TextView nav_header_profilename, nav_header_emailid,nav_header_ref_code;
    //SessionManager session;
    String name, image_url, phoneNo;

     public TextView toolbar_title;
     Button btnNotificationPatient;

     public Menu menu;




    ProgressDialog progressDialog;


    SessionManager session;

    private double latitude, longitude;
    private String addressLine = "";

    String emailid = "",patientid = "";
    private Dialog dialog;

    private static final int REQUEST_PHONE_CALL =1 ;
    private String sosPhonenumber;

    public View toolbar_layout;
   public TextView txt_location;
    private String refcode;

    TextView txt_notification_count_badge;
    TextView txt_cart_count_badge;
    private String userid;
    private String profileimage;

    TextView nav_header_logout;


    @SuppressLint({"InflateParams", "LogNotTimber"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        Log.w(TAG, "onCreate---->");

        inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.navigation_drawer_layout_customer, null);


        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getProfileDetails();
        String firstname = user.get(SessionManager.KEY_FIRST_NAME);
        String lastname = user.get(SessionManager.KEY_LAST_NAME);
        name = firstname+" "+lastname;
        emailid = user.get(SessionManager.KEY_EMAIL_ID);
        phoneNo = user.get(SessionManager.KEY_MOBILE);
        String userid = user.get(SessionManager.KEY_ID);
        Log.w(TAG, "userid : " + userid);

        refcode = user.get(SessionManager.KEY_REF_CODE);


        Log.w(TAG, "user details--->" + "name :" + " " + name + " " + "image_url :" + image_url);

        initUI(view);
        initToolBar(view);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    private void initUI(View view) {

        //Initializing NavigationView
        NavigationView navigationView = view.findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        frameLayout = view.findViewById(R.id.base_container);
        menu = navigationView.getMenu();

        SessionManager session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getProfileDetails();
        userid = user.get(SessionManager.KEY_ID);
        profileimage = user.get(SessionManager.KEY_PROFILE_IMAGE);




        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = view.findViewById(R.id.drawer_layout);
        header = navigationView.getHeaderView(0);
        nav_header_imageView = header.findViewById(R.id.nav_header_imageView);
        nav_header_emailid = header.findViewById(R.id.nav_header_emailid);
        nav_header_profilename = header.findViewById(R.id.nav_header_profilename);

        nav_header_ref_code = view.findViewById(R.id.nav_header_ref_code);
        nav_header_logout = view.findViewById(R.id.nav_header_logout);

        nav_header_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogOutAppAlert();
            }
        });





        if(refcode != null && !refcode.isEmpty() ){
            nav_header_ref_code.setText(getResources().getString(R.string.ref_code)+" : "+refcode);
            nav_header_ref_code.setVisibility(View.VISIBLE);

        }else{
            nav_header_ref_code.setVisibility(View.GONE);
            nav_header_ref_code.setText("");
        }
        if(profileimage != null && !profileimage.isEmpty()) {
            Glide.with(this).load(profileimage).circleCrop().into(nav_header_imageView);
        }else{
            Glide.with(this).load(APIClient.PROFILE_IMAGE_URL).circleCrop().into(nav_header_imageView);
        }

        nav_header_emailid.setText(emailid);
        nav_header_profilename.setText(name);

        FrameLayout llheader = header.findViewById(R.id.llheader);
        llheader.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), CustomerProfileScreenActivity.class)));


        navigationView.setNavigationItemSelectedListener(menuItem -> {
            //Closing drawer on item click
            drawerLayout.closeDrawers();
            //Check to see which item was being clicked and perform appropriate action
            switch (menuItem.getItemId()) {
                //Replacing the main content with ContentFragment Which is our Inbox View;
                case R.id.nav_item_one:
                    gotoMyFavourites();
                    return true;

                // For rest of the options we just show a toast on click
                case R.id.nav_item_two:
                    gotoMyOrders();
                    return true;

                case R.id.nav_item_three:
                    gotoMyAppointments();
                    return true;

                case R.id.nav_item_four:
                    gotoMyCoupons();
                    return true;

                case R.id.nav_item_five:
                    gotoMedicalHistory();
                    return true;
//
//                case R.id.nav_item_six:
//                    gotoPaymentdetails();
//                    return true;
                case R.id.nav_item_seven:
                     gotoNotifications();
                    return true;

                case R.id.nav_item_nine:
                    gotoSOS();
                    return true;

                case R.id.nav_item_ten:
                    gotoMyWalkinAppointments();
                    return true;

                default:
                    return true;

            }
        });

    }

    private void gotoMyWalkinAppointments() {
        startActivity(new Intent(getApplicationContext(),PetWalkinappointmentsActivity.class));
    }

    private void gotoMyCoupons() {
        startActivity(new Intent(getApplicationContext(), MyCouponsActivity.class));

    }

    private void gotoSOS() {
        Intent intent = new Intent(getApplicationContext(),SoSActivity.class);
        intent.putExtra("fromactivity",TAG);
        startActivity(intent);


    }

    private void gotoNotifications() {
        Intent intent = new Intent(getApplicationContext(),NotificationActivity.class);
        intent.putExtra("fromactivity",TAG);
        startActivity(intent);
    }

    private void gotoPaymentdetails() {

        Intent intent = new Intent(getApplicationContext(),PetloverPaymentDetailsActivity.class);
        startActivity(intent);

    }

    private void gotoMedicalHistory() {
        Intent intent = new Intent(getApplicationContext(),MedicalHistoryActivity.class);
        startActivity(intent);
    }

    private void gotoMyFavourites() {
        Intent intent = new Intent(getApplicationContext(),PetloverFavListActivity.class);
        startActivity(intent);
    }


    @SuppressLint("SetTextI18n")
    private void initToolBar(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar_layout = view.findViewById(R.id.include_customer_header);

        txt_location = toolbar_layout.findViewById(R.id.txt_location);
        drawerImg = toolbar_layout.findViewById(R.id.img_menu);

        RelativeLayout ll_location = toolbar_layout.findViewById(R.id.rl_mappin);
        ll_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ManageAddressActivity.class);
                intent.putExtra("fromactivity",TAG);
                startActivity(intent);

            }
        });

        ImageView img_cart = toolbar_layout.findViewById(R.id.img_cart);
        ImageView img_notification = toolbar_layout.findViewById(R.id.img_notification);
      /*   txt_notification_count_badge = toolbar_layout.findViewById(R.id.txt_notification_count_badge);
         txt_cart_count_badge = toolbar_layout.findViewById(R.id.txt_cart_count_badge);
        txt_notification_count_badge.setVisibility(View.GONE);
        txt_cart_count_badge.setVisibility(View.GONE);
*/
      




        img_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),NotificationActivity.class));
            }
        });
        img_cart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onClick(View v) {
                if(CustomerDashboardActivity.active_tag != null){
                    Log.w(TAG,"active_tag : "+ CustomerDashboardActivity.active_tag);
                }
                startActivity(new Intent(getApplicationContext(), PetCartActivity.class));
            }
        });

      /*  if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
            notificationandCartCountResponseCall();
        }*/


       /*
        toolbar_title = toolbar.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Home " );
        ImageView img_sos = toolbar.findViewById(R.id.img_sos);
        ImageView img_notification = toolbar.findViewById(R.id.img_notification);
        ImageView img_cart = toolbar.findViewById(R.id.img_cart);
        ImageView img_profile = toolbar.findViewById(R.id.img_profile);
        img_sos.setOnClickListener(v -> {
            Log.w(TAG,"SOSLIST"+new Gson().toJson(APIClient.sosList));
            showSOSAlert(APIClient.sosList);

        });
        img_notification.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), NotificationActivity.class)));
        img_cart.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onClick(View v) {
                if(CustomerDashboardActivity.active_tag != null){
                    Log.w(TAG,"active_tag : "+CustomerDashboardActivity.active_tag);
                }
                startActivity(new Intent(getApplicationContext(), PetCartActivity.class));
            }
        });
        img_profile.setOnClickListener(v -> {

           Intent intent = new Intent(getApplicationContext(),CustomerProfileScreenActivity.class);
           intent.putExtra("fromactivity",TAG);
            if(CustomerDashboardActivity.active_tag != null){
                intent.putExtra("active_tag",CustomerDashboardActivity.active_tag);

            }
           startActivity(intent);
        });*/


        toggleView();
    }

    private void showSOSAlert(List<PetLoverDashboardResponse.DataBean.SOSBean> sosList) {

        try {

            dialog = new Dialog(CustomerNavigationDrawer.this);
            dialog.setContentView(R.layout.sos_popup_layout);
            RecyclerView rv_sosnumbers = (RecyclerView)dialog.findViewById(R.id.rv_sosnumbers);
            Button btn_call = (Button)dialog.findViewById(R.id.btn_call);
            TextView txt_no_records = (TextView)dialog.findViewById(R.id.txt_no_records);
            ImageView img_close = (ImageView)dialog.findViewById(R.id.img_close);
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
           /* if(sosList != null && sosList.size()>0){
                rv_sosnumbers.setVisibility(View.VISIBLE);
                btn_call.setVisibility(View.VISIBLE);
                txt_no_records.setVisibility(View.GONE);
                rv_sosnumbers.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                rv_sosnumbers.setItemAnimator(new DefaultItemAnimator());
                PetLoverSOSAdapter petLoverSOSAdapter = new PetLoverSOSAdapter(getApplicationContext(), sosList,this);
                rv_sosnumbers.setAdapter(petLoverSOSAdapter);
            }
            else{
                rv_sosnumbers.setVisibility(View.GONE);
                btn_call.setVisibility(View.GONE);
                txt_no_records.setVisibility(View.VISIBLE);
                txt_no_records.setText(getResources().getString(R.string.no_phone_numbers));

            }*/

            btn_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(CustomerNavigationDrawer.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                    }
                    else
                    {
                        gotoPhone();
                    }

                }
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();


        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }




    }
    private void gotoPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + sosPhonenumber));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }





    private void toggleView() {
        drawerImg.setOnClickListener(v -> {
            if (v.isClickable()) {
                drawerMethod();
            } else {

                Intent intent_re = getIntent();
                overridePendingTransition(0, 0);
                intent_re.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                finish();
                overridePendingTransition(0, 0);
                startActivity(intent_re);

            }
        });
    }
    public void drawerMethod() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);

        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }

    }
    public void setContentView(int layoutId) {

        Log.e("BaseOncreate", "setContentView");
        View activityView = inflater.inflate(layoutId, null);
        frameLayout.addView(activityView);
        super.setContentView(view);
        //drawerMethod();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_menu:
                drawerMethod();
                break;


        }
    }




    private void confirmLogoutDialog(){

        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CustomerNavigationDrawer.this);
        alertDialogBuilder.setMessage("Are you sure want to logout?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1)
                    {

                        gotoLogout();


                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialogBuilder.setCancelable(true);
            }
        });

        android.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }
    private void gotoMyAppointments() {
        startActivity(new Intent(getApplicationContext(),PetMyappointmentsActivity.class));

    }
    private void gotoMyOrders() {
        startActivity(new Intent(getApplicationContext(),PetMyOrdrersNewActivity.class));

    }
    private void gotoLogout() {
        logoutResponseCall();
       /* session.logoutUser();
        session.setIsLogin(false);
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();*/




    }


   /* @Override
    public void soSCallListener(long phonenumber) {
        if(phonenumber != 0){
            sosPhonenumber = String.valueOf(phonenumber);
        }
    }*/

    private void showLogOutAppAlert() {
        try {

            dialog = new Dialog(CustomerNavigationDrawer.this);
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
                   /* startActivity(new Intent(getApplicationContext(),CustomerDashboardActivity.class));
                    finish();*/
                }
            });
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }




    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG,"onResume-->");
//        notificationandCartCountResponseCall();
    }


   /* @SuppressLint("LogNotTimber")
    private void notificationandCartCountResponseCall() {

      avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();


        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<NotificationCartCountResponse> call = apiInterface.notificationandCartCountResponseCall(RestUtils.getContentType(), notificationCartCountRequest());
        Log.w(TAG,"NotificationCartCountResponse url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<NotificationCartCountResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<NotificationCartCountResponse> call, @NonNull Response<NotificationCartCountResponse> response) {

                Log.w(TAG,"NotificationCartCountResponse"+ "--->" + new Gson().toJson(response.body()));

               // avi_indicator.smoothToHide();

                if (response.body() != null) {
                    if(response.body().getCode() == 200) {
                        if(response.body().getData()!=null){
                            int Notification_count = response.body().getData().getNotification_count();
                            int Product_count = response.body().getData().getProduct_count();
                            if(Notification_count != 0){
                                txt_notification_count_badge.setVisibility(View.VISIBLE);
                                txt_notification_count_badge.setText(""+Notification_count);
                            }else{
                                txt_notification_count_badge.setVisibility(View.GONE);
                            }
                            if(Product_count != 0){
                                txt_cart_count_badge.setVisibility(View.VISIBLE);
                                txt_cart_count_badge.setText(""+Product_count);
                            }else{
                                txt_cart_count_badge.setVisibility(View.GONE);
                            }


                        }
                    }



                }




            }

            @Override
            public void onFailure(@NonNull Call<NotificationCartCountResponse> call, @NonNull Throwable t) {

                // avi_indicator.smoothToHide();
                Log.w(TAG,"NotificationCartCountResponse flr"+"--->" + t.getMessage());
            }
        });


    }
    @SuppressLint("LogNotTimber")
    private NotificationCartCountRequest notificationCartCountRequest() {
      *//*
         * user_id : 6048589d0b3a487571a1c567*//*


        NotificationCartCountRequest notificationCartCountRequest = new NotificationCartCountRequest();
        notificationCartCountRequest.setUser_id(userid);
        Log.w(TAG,"notificationCartCountRequest"+ "--->" + new Gson().toJson(notificationCartCountRequest));
        return notificationCartCountRequest;
    }
*/


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


}
