package com.triton.healthZpartners.vendor;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.triton.healthZpartners.R;
import com.triton.healthZpartners.activity.LoginActivity;
import com.triton.healthZpartners.activity.NotificationActivity;
import com.triton.healthZpartners.api.APIClient;
import com.triton.healthZpartners.api.RestApiInterface;
import com.triton.healthZpartners.requestpojo.DefaultLocationRequest;
import com.triton.healthZpartners.requestpojo.NotificationCartCountRequest;
import com.triton.healthZpartners.responsepojo.NotificationCartCountResponse;
import com.triton.healthZpartners.responsepojo.SuccessResponse;
import com.triton.healthZpartners.sessionmanager.SessionManager;
import com.triton.healthZpartners.utils.ConnectionDetector;
import com.triton.healthZpartners.utils.RestUtils;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VendorNavigationDrawer extends AppCompatActivity implements View.OnClickListener {

    private String TAG ="VendorNavigationDrawer";

    public NavigationView navigationView;
    private DrawerLayout drawerLayout;
    LayoutInflater inflater;
    View view, header;
    Toolbar toolbar;


    ImageView drawerImg;
    CircleImageView nav_header_imageView;
    TextView nav_header_profilename, nav_header_emailid,nav_header_ref_code,nav_header_logout;

    FrameLayout frameLayout;

    //SessionManager session;
    String name, image_url, phoneNo;



     public Menu menu;


    BroadcastReceiver imgReceiver;






//    SessionManager session;



    String emailid = "";
    private SessionManager session;
    private Dialog dialog;
    private String refcode;
    private String userid;

    //TextView txt_notification_count_badge;

    public View toolbar_layout;


    @SuppressLint("LogNotTimber")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        Log.w(TAG,"onCreate---->");

        inflater = LayoutInflater.from(this);
        view = inflater.inflate(R.layout.vendor_navigation_drawer_layout, null);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getProfileDetails();
        String firstname = user.get(SessionManager.KEY_FIRST_NAME);
        String lastname = user.get(SessionManager.KEY_LAST_NAME);
        name = firstname+" "+lastname;
        emailid = user.get(SessionManager.KEY_EMAIL_ID);
        phoneNo = user.get(SessionManager.KEY_MOBILE);
        refcode = user.get(SessionManager.KEY_REF_CODE);
        image_url = user.get(SessionManager.KEY_PROFILE_IMAGE);
        userid = user.get(SessionManager.KEY_ID);






        Log.w(TAG,"user details--->"+"name :"+" "+ name+" " +"image_url :"+ image_url);

        initUI(view);
        initToolBar(view);


       // myBoradcastReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (imgReceiver != null) {
            unregisterReceiver(imgReceiver);
        }
    }

    @SuppressLint("SetTextI18n")
    private void initUI(View view) {

        //Initializing NavigationView
        navigationView = view.findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);

        frameLayout = view.findViewById(R.id.base_container);




        menu = navigationView.getMenu();


        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = view.findViewById(R.id.drawer_layout);
        header = navigationView.getHeaderView(0);
        nav_header_imageView = header.findViewById(R.id.nav_header_imageView);
        nav_header_emailid = header.findViewById(R.id.nav_header_emailid);
        nav_header_profilename = header.findViewById(R.id.nav_header_profilename);
        if(image_url != null && !image_url.isEmpty()){
            Glide.with(this).load(image_url).into(nav_header_imageView);
        }else{
            Glide.with(this).load(APIClient.PROFILE_IMAGE_URL).into(nav_header_imageView);
        }


        nav_header_ref_code = view.findViewById(R.id.nav_header_ref_code);
        if(refcode != null && !refcode.isEmpty() ){
            nav_header_ref_code.setVisibility(View.VISIBLE);
            nav_header_ref_code.setText(getResources().getString(R.string.ref_code)+" : "+refcode);
        }else{
            nav_header_ref_code.setVisibility(View.GONE);
            nav_header_ref_code.setText("");
        }

        nav_header_logout = view.findViewById(R.id.nav_header_logout);

        nav_header_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogOutAppAlert();
            }
        });


        nav_header_emailid.setText(emailid);
        nav_header_profilename.setText(name);
        FrameLayout llheader = header.findViewById(R.id.llheader);
        llheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), VendorProfileScreenActivity.class));
            }
        });





        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                drawerLayout.closeDrawers();



                //Check to see which item was being clicked and perform appropriate action
                switch (item.getItemId()) {

                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_item_one:
                        return true;

                    // For rest of the options we just show a toast on click
                    case R.id.nav_item_two:
                        gotoManageProducts();
                        return true;

                    case R.id.nav_item_three:
                        gotoDashboard();
                        return true;

                    case R.id.nav_item_five:
                        return true;

                    case R.id.nav_item_six:
                        return true;

                    case R.id.nav_item_seven:
                        gotoAddProducts();
                        return true;
                    case R.id.nav_item_eight:
                       gotoNotifications();
                        return true;

                        case R.id.nav_item_nine:
                        showLogOutAppAlert();
                        //confirmLogoutDialog();
                        return true;


                    default:
                        return true;
                }
            }
        });

    }

    private void gotoDashboard() {
        Intent intent = new Intent(getApplicationContext(),VendorDashboardActivity.class);
        intent.putExtra("fromactivity",TAG);
        startActivity(intent);
    }

    private void gotoNotifications() {
        Intent intent = new Intent(getApplicationContext(),NotificationActivity.class);
        intent.putExtra("fromactivity",TAG);
        startActivity(intent);
    }

    private void gotoAddProducts() {
        Intent i = new Intent(VendorNavigationDrawer.this, VendorAddProductsActivity.class);
        startActivity(i);
    }


    private void initToolBar(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar_layout = view.findViewById(R.id.include_customer_header);

        drawerImg = toolbar_layout.findViewById(R.id.img_menu);
       // header_title = (TextView) toolbar.findViewById(R.id.header_title);

        ImageView img_notification = toolbar_layout.findViewById(R.id.img_notification);
        ImageView img_profile = toolbar_layout.findViewById(R.id.img_profile);
        //txt_notification_count_badge = toolbar_layout.findViewById(R.id.txt_notification_count_badge);

        img_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), NotificationActivity.class));

            }
        });
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), VendorProfileScreenActivity.class);
                startActivity(intent);

            }
        });

        if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
            notificationandCartCountResponseCall();
        }








        toggleView();
    }




    private void toggleView() {
        drawerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        if (v.getId() == R.id.img_menu) {
            drawerMethod();
        }
    }



    void gotoManageProducts() {
        Intent i = new Intent(VendorNavigationDrawer.this, ManageProductsActivity.class);
        startActivity(i);

    }


    private void confirmLogoutDialog(){

        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(VendorNavigationDrawer.this);
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
    private void showLogOutAppAlert() {
        try {

            dialog = new Dialog(VendorNavigationDrawer.this);
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
                    startActivity(new Intent(getApplicationContext(),VendorDashboardActivity.class));
                    finish();
                }
            });
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }




    }

    private void gotoLogout() {
        logoutResponseCall();
        /*session.logoutUser();
        session.setIsLogin(false);
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();*/

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


    @Override
    protected void onResume() {
        super.onResume();
        notificationandCartCountResponseCall();
        try{
            Log.w(TAG,"onResume--->");



       /* session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        name = user.get(SessionManager.KEY_USER_NAME);
        user_mode = user.get(SessionManager.KEY_USER_MODE);
        phoneNo = user.get(SessionManager.KEY_MOBILE);
        image_url = session.getImagePath();
        jockey_id = Integer.valueOf(user.get(SessionManager.JOCKEY_ID));*/


            //  checkisAppliedForJockeyandCountAPI();

            Menu menu = navigationView.getMenu();
            MenuItem target = menu.findItem(R.id.nav_item_seven);

           // checkLocation();
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @SuppressLint("LogNotTimber")
    private void notificationandCartCountResponseCall() {
       /* avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();*/

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
                            /*if(Notification_count != 0){
                                txt_notification_count_badge.setVisibility(View.VISIBLE);
                                txt_notification_count_badge.setText(""+Notification_count);
                            }else{
                                txt_notification_count_badge.setVisibility(View.GONE);
                            }*/
                            if(Product_count != 0){

                            }else{
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
        /*
         * user_id : 6048589d0b3a487571a1c567
         */

        NotificationCartCountRequest notificationCartCountRequest = new NotificationCartCountRequest();
        notificationCartCountRequest.setUser_id(userid);
        Log.w(TAG,"notificationCartCountRequest"+ "--->" + new Gson().toJson(notificationCartCountRequest));
        return notificationCartCountRequest;
    }


}
