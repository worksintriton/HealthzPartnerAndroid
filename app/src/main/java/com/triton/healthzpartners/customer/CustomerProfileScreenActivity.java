package com.triton.healthzpartners.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.triton.healthzpartners.R;
import com.triton.healthzpartners.activity.LoginActivity;
import com.triton.healthzpartners.activity.NotificationActivity;
import com.triton.healthzpartners.activity.location.AddMyAddressOldUserActivity;
import com.triton.healthzpartners.activity.location.EditMyAddressActivity;
import com.triton.healthzpartners.activity.location.ManageAddressActivity;
import com.triton.healthzpartners.activity.location.PickUpLocationActivity;
import com.triton.healthzpartners.activity.location.PickUpLocationEditActivity;
import com.triton.healthzpartners.adapter.ManagePetListAdapter;
import com.triton.healthzpartners.api.APIClient;
import com.triton.healthzpartners.api.RestApiInterface;

import com.triton.healthzpartners.interfaces.FamilyMembersDeleteListener;
import com.triton.healthzpartners.customer.myaddresses.MyAddressesListActivity;
import com.triton.healthzpartners.interfaces.GotoAddFamilyMembersOldActivityListener;
import com.triton.healthzpartners.requestpojo.DefaultLocationRequest;
import com.triton.healthzpartners.requestpojo.FamilyMemberDeleteRequest;
import com.triton.healthzpartners.requestpojo.FamilyMemberListRequest;
import com.triton.healthzpartners.responsepojo.FamilyMemberDeleteResponse;
import com.triton.healthzpartners.responsepojo.FamilyMemberListResponse;
import com.triton.healthzpartners.responsepojo.PetListResponse;
import com.triton.healthzpartners.responsepojo.SuccessResponse;
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
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerProfileScreenActivity extends AppCompatActivity implements View.OnClickListener, FamilyMembersDeleteListener, BottomNavigationView.OnNavigationItemSelectedListener, GotoAddFamilyMembersOldActivityListener {
    private  String TAG = "CustomerProfileScreenActivity";


/**/    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_usrname)
    TextView txt_usrname;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_mail)
    TextView txt_mail;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_phn_num)
    TextView txt_phn_num;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_manage_address)
    TextView txt_manage_address;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_my_addresses)
    TextView txt_my_addresses;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_change_password)
    TextView txt_change_password;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.avi_indicator)
    AVLoadingIndicatorView avi_indicator;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_familylist)
    RecyclerView rv_family;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_no_records)
    TextView txt_no_records;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_logout)
    TextView txt_logout;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_add)
    LinearLayout ll_add;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.include_petlover_footer)
    View include_petlover_footer;

    BottomNavigationView bottom_navigation_view;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_edit_profile)
    TextView txt_edit_profile;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_profile1)
    ImageView img_profile1;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_edit_image)
    TextView txt_edit_image;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_name)
    TextView txt_name;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_email)
    TextView txt_email;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.include_petlover_header)
    View include_petlover_header;



    private SessionManager session;
    String name,emailid,phoneNo,userid;
    private List<PetListResponse.DataBean> petList;
    private Dialog dialog;
    private String profileimage;
    private String fromactivity;
    private String doctorid,doctorname,strdistance;

    private String catid;
    private String spid;
    private String from;

    private static final int REQUEST_PHONE_CALL =1 ;
    private String sosPhonenumber;

    private int distance;
    private int reviewcount;
    private int Count_value_start;
    private int Count_value_end;
    private String spuserid;
    private String _id;

    ArrayList<Integer> product_idList;
    private int product_id;
    private String orderid;
    private String cancelorder;
    private String latlng,CityName,AddressLine,PostalCode,nickname;

    String locationtype;
    private String pincode,cityname,address;
    private boolean defaultstatus;
    private String id;
    double latitude, longtitude;

    String appointment_id;
    String appoinment_status;
    private String bookedat;
    private String startappointmentstatus;
    private String appointmentfor;
    private String userrate;

    private List<FamilyMemberListResponse.DataBean> dataBeanList;

    /* Petlover Bottom Navigation */

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigation;

    public static String active_tag = "1";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile_screen);
        ButterKnife.bind(this);


        ImageView img_back = include_petlover_header.findViewById(R.id.img_back);
        ImageView img_sos = include_petlover_header.findViewById(R.id.img_sos);
        ImageView img_notification = include_petlover_header.findViewById(R.id.img_notification);
        ImageView img_cart = include_petlover_header.findViewById(R.id.img_cart);
        ImageView img_profile = include_petlover_header.findViewById(R.id.img_profile);
        TextView toolbar_title = include_petlover_header.findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.profile));
        img_sos.setVisibility(View.GONE);
        img_cart.setVisibility(View.GONE);



        Log.w(TAG,"onCreate : ");
        avi_indicator.setVisibility(View.GONE);
        ll_add.setVisibility(View.GONE);


        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getProfileDetails();
        String firstname = user.get(SessionManager.KEY_FIRST_NAME);
        String lastname = user.get(SessionManager.KEY_LAST_NAME);
        name = firstname+" "+lastname;
        emailid = user.get(SessionManager.KEY_EMAIL_ID);
        phoneNo = user.get(SessionManager.KEY_MOBILE);
        userid = user.get(SessionManager.KEY_ID);
        profileimage = user.get(SessionManager.KEY_PROFILE_IMAGE);
        Log.w(TAG,"profileimage"+ "--->" + profileimage);




        txt_usrname.setText(name);
        txt_name.setText(name);
        txt_mail.setText(emailid);
        txt_email.setText(emailid);
        txt_phn_num.setText(phoneNo);

        if(profileimage != null && !profileimage.isEmpty()){
            Glide.with(CustomerProfileScreenActivity.this)
                    .load(profileimage)
                    .into(img_profile1);
        }else{
            Glide.with(CustomerProfileScreenActivity.this)
                    .load(APIClient.PROFILE_IMAGE_URL)
                    .into(img_profile1);

        }





        if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
            familymembersListResponseCall();
        }


        img_back.setOnClickListener(this);
        txt_manage_address.setOnClickListener(this);
        txt_my_addresses.setOnClickListener(this);

        txt_change_password.setOnClickListener(this);
        txt_logout.setOnClickListener(this);
        ll_add.setOnClickListener(this);
        txt_edit_profile.setOnClickListener(this);
        txt_edit_image.setOnClickListener(this);

        img_notification.setOnClickListener(this);
        img_cart.setOnClickListener(this);
        img_profile1.setOnClickListener(this);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fromactivity = extras.getString("fromactivity");
            active_tag = extras.getString("active_tag");
            /*DoctorClinicDetailsActivity*/
            doctorid = extras.getString("doctorid");
            doctorname = extras.getString("doctorname");
            strdistance = extras.getString("distance");
            Log.w(TAG,"fromactivity : "+fromactivity+" active_tag : "+active_tag);

            /*SelectedServiceActivity*/
            catid = extras.getString("catid");
            from = extras.getString("from");
            distance = extras.getInt("distance");
            reviewcount = extras.getInt("reviewcount");
            Count_value_start = extras.getInt("Count_value_start");
            Count_value_end = extras.getInt("Count_value_end");

            /*Service_Details_Activity*/
            spid = extras.getString("spid");
            catid = extras.getString("catid");
            from = extras.getString("from");

            spuserid = extras.getString("spuserid");
            _id = extras.getString("_id");

            Log.w(TAG,"distance : "+distance);

            _id = extras.getString("_id");
            orderid = extras.getString("orderid");
            product_id = extras.getInt("product_id");
            cancelorder = extras.getString("cancelorder");
            Log.w(TAG,"_id : "+_id);
            product_idList = getIntent().getIntegerArrayListExtra("product_idList");


            latlng = extras.getString("latlng");
            CityName = extras.getString("CityName");
            AddressLine = extras.getString("AddressLine");
            PostalCode = extras.getString("PostalCode");
            nickname = extras.getString("nickname");

            id = extras.getString("id");
            locationtype = extras.getString("locationtype");
            defaultstatus = extras.getBoolean("defaultstatus");
            latitude = extras.getDouble("lat");
            longtitude = extras.getDouble("lon");
            pincode = extras.getString("pincode");
            cityname = extras.getString("cityname");
            address = extras.getString("address");

            /*PetAppointmentDetailsActivity*/
            appointment_id = extras.getString("appointment_id");
            bookedat = extras.getString("bookedat");
            startappointmentstatus = extras.getString("startappointmentstatus");
            appointmentfor = extras.getString("appointmentfor");
            userrate = extras.getString("userrate");
            from = extras.getString("from");

        }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(fromactivity != null && fromactivity.equalsIgnoreCase("DoctorClinicDetailsActivity")){
            Intent intent = new Intent(getApplicationContext(),DoctorClinicDetailsActivity.class);
            intent.putExtra("doctorid",doctorid);
            intent.putExtra("doctorname",doctorname);
            intent.putExtra("distance",distance);
            startActivity(intent);
            finish();
        }
        else if(fromactivity != null && fromactivity.equalsIgnoreCase("SelectedServiceActivity")){
            Intent intent = new Intent(getApplicationContext(),SelectedServiceActivity.class);
            intent.putExtra("catid",catid);
            intent.putExtra("from",from);
            intent.putExtra("distance",distance);
            intent.putExtra("reviewcount",reviewcount);
            intent.putExtra("Count_value_start",Count_value_start);
            intent.putExtra("Count_value_end",Count_value_end);
            startActivity(intent);
            finish();
        }else if(fromactivity != null && fromactivity.equalsIgnoreCase("Service_Details_Activity")){
            Intent intent = new Intent(getApplicationContext(),Service_Details_Activity.class);
            intent.putExtra("spid",spid);
            intent.putExtra("catid",catid);
            intent.putExtra("from",from);
            startActivity(intent);
            finish();
        }else if(fromactivity != null && fromactivity.equalsIgnoreCase("PetMyappointmentsActivity")){
            Intent intent = new Intent(getApplicationContext(),PetMyappointmentsActivity.class);
            startActivity(intent);
            finish();
        }else if(fromactivity != null && fromactivity.equalsIgnoreCase("MedicalHistoryActivity")){
            Intent intent = new Intent(getApplicationContext(),MedicalHistoryActivity.class);
            startActivity(intent);
            finish();
        }else if(fromactivity != null && fromactivity.equalsIgnoreCase("PetloverPaymentDetailsActivity")){
            Intent intent = new Intent(getApplicationContext(),PetloverPaymentDetailsActivity.class);
            startActivity(intent);
            finish();
        }else if(fromactivity != null && fromactivity.equalsIgnoreCase("PetServiceAppointment_Doctor_Date_Time_Activity")){
            Intent intent = new Intent(getApplicationContext(),PetServiceAppointment_Doctor_Date_Time_Activity.class);
            intent.putExtra("spuserid",spuserid);
            intent.putExtra("spid",spid);
            intent.putExtra("catid",catid);
            intent.putExtra("distance",distance);
            startActivity(intent);
            finish();
        }else if(fromactivity != null && fromactivity.equalsIgnoreCase("ManageAddressActivity")){
            Intent intent = new Intent(getApplicationContext(),ManageAddressActivity.class);
            startActivity(intent);
            finish();
        }else if(fromactivity != null && fromactivity.equalsIgnoreCase("PetMyOrdrersNewActivity")){
            Intent intent = new Intent(getApplicationContext(),PetMyOrdrersNewActivity.class);
            startActivity(intent);
            finish();
        }else if(fromactivity != null && fromactivity.equalsIgnoreCase("ServiceBookAppointmentActivity")){
            Intent intent = new Intent(getApplicationContext(),ServiceBookAppointmentActivity.class);
            intent.putExtra("spuserid",spuserid);
            intent.putExtra("spid",spid);
            intent.putExtra("catid",catid);
            intent.putExtra("distance",distance);
            startActivity(intent);
            finish();
        }else if(fromactivity != null && fromactivity.equalsIgnoreCase("PetAppointment_Doctor_Date_Time_Activity")){
            Intent intent = new Intent(getApplicationContext(),PetAppointment_Doctor_Date_Time_Activity.class);
            intent.putExtra("doctorid",doctorid);
            startActivity(intent);
            finish();
        }else if(fromactivity != null && fromactivity.equalsIgnoreCase("BookAppointmentActivity")){
            Intent intent = new Intent(getApplicationContext(),BookAppointmentActivity.class);
            intent.putExtra("doctorid",doctorid);
            startActivity(intent);
            finish();
        }else if(fromactivity != null && fromactivity.equalsIgnoreCase("PetLoverVendorOrderDetailsActivity")){
            Intent intent = new Intent(getApplicationContext(),PetLoverVendorOrderDetailsActivity.class);
            intent.putExtra("_id",_id);
            startActivity(intent);
            finish();
        }else if(fromactivity != null && fromactivity.equalsIgnoreCase("PetVendorCancelOrderActivity")){
            Intent intent = new Intent(getApplicationContext(),PetVendorCancelOrderActivity.class);
            intent.putExtra("_id",_id);
            intent.putExtra("orderid",orderid);
            intent.putExtra("product_id",product_id);
            intent.putExtra("cancelorder",cancelorder);
            intent.putExtra("product_idList",product_idList);
            startActivity(intent);
            finish();
        }else if(fromactivity != null && fromactivity.equalsIgnoreCase("PickUpLocationActivity")){
            Intent intent = new Intent(getApplicationContext(), PickUpLocationActivity.class);
            startActivity(intent);
            finish();
        }else if(fromactivity != null && fromactivity.equalsIgnoreCase("AddMyAddressOldUserActivity")){
            Intent intent = new Intent(getApplicationContext(), AddMyAddressOldUserActivity.class);
            intent.putExtra("latlng",latlng);
            intent.putExtra("CityName",CityName);
            intent.putExtra("AddressLine",AddressLine);
            intent.putExtra("PostalCode",PostalCode);
            intent.putExtra("userid",userid);
            intent.putExtra("nickname",nickname);
            startActivity(intent);
            finish();
        }else if(fromactivity != null && fromactivity.equalsIgnoreCase("EditMyAddressActivity")){
            Intent intent = new Intent(getApplicationContext(), EditMyAddressActivity.class);
            intent.putExtra("id",id);
            intent.putExtra("userid",userid);
            intent.putExtra("nickname",nickname);
            intent.putExtra("locationtype",locationtype);
            intent.putExtra("defaultstatus",defaultstatus);
            intent.putExtra("lat",latitude);
            intent.putExtra("lon",longtitude);
            intent.putExtra("pincode",pincode);
            intent.putExtra("cityname",cityname);
            intent.putExtra("address",address);
            startActivity(intent);
            finish();
        }else if(fromactivity != null && fromactivity.equalsIgnoreCase("PickUpLocationEditActivity")){
            Intent intent = new Intent(getApplicationContext(), PickUpLocationEditActivity.class);
            intent.putExtra("id",id);
            intent.putExtra("userid",userid);
            intent.putExtra("nickname",nickname);
            intent.putExtra("locationtype",locationtype);
            intent.putExtra("defaultstatus",defaultstatus);
            intent.putExtra("lat",latitude);
            intent.putExtra("lon",longtitude);
            startActivity(intent);
               finish();
        }else if(fromactivity != null && fromactivity.equalsIgnoreCase("PetloverFavListActivity")){
            Intent intent = new Intent(getApplicationContext(), PetloverFavListActivity.class);
            startActivity(intent);
            finish();
        }else if(fromactivity != null && fromactivity.equalsIgnoreCase("PetAppointmentDetailsActivity")){
            Intent intent = new Intent(getApplicationContext(), PetAppointmentDetailsActivity.class);
            intent.putExtra("fromactivity",TAG);
            intent.putExtra("appointment_id",appointment_id);
            intent.putExtra("bookedat",bookedat);
            intent.putExtra("startappointmentstatus",startappointmentstatus);
            intent.putExtra("appointmentfor",appointmentfor);
            intent.putExtra("userrate",userrate);
            intent.putExtra("from",from);
            startActivity(intent);
            finish();
        } else if(active_tag != null){
            callDirections(active_tag);
        }else if(fromactivity != null && fromactivity.equalsIgnoreCase("TrackOrderActivity")){
            Intent intent = new Intent(getApplicationContext(), TrackOrderActivity.class);
            intent.putExtra("_id",_id);
            intent.putExtra("orderid",orderid);
            startActivity(intent);

        }else{
            Intent intent = new Intent(getApplicationContext(), CustomerDashboardActivity.class);
            startActivity(intent);
            finish();
        }



    }



    private void showLogOutAppAlert() {
        try {

            dialog = new Dialog(CustomerProfileScreenActivity.this);
            dialog.setContentView(R.layout.alert_logout_layout);
            Button btn_no = dialog.findViewById(R.id.btn_no);
            Button btn_yes = dialog.findViewById(R.id.btn_yes);

            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    //gotoLogout();
                    session.logoutUser();
                    session.setIsLogin(false);
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();

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


    private void gotoMyAddresses() {
        startActivity(new Intent(CustomerProfileScreenActivity.this, MyAddressesListActivity.class));

    }

    private void gotoAddYourPet() {
        Intent intent = new Intent(getApplicationContext(), AddYourFamilyMembersOldActivity.class);
        intent.putExtra("doctorid",doctorid);
        intent.putExtra("doctorname",doctorname);
        intent.putExtra("distance",distance);
        intent.putExtra("catid",catid);
        intent.putExtra("from",from);
        intent.putExtra("reviewcount",reviewcount);
        intent.putExtra("Count_value_start",Count_value_start);
        intent.putExtra("Count_value_end",Count_value_end);
        intent.putExtra("spid",spid);
        intent.putExtra("spuserid",spuserid);
        intent.putExtra("_id",_id);
        intent.putExtra("orderid",orderid);
        intent.putExtra("product_id",product_id);
        intent.putExtra("cancelorder",cancelorder);
        intent.putExtra("product_idList",product_idList);
        intent.putExtra("id",id);
        intent.putExtra("userid",userid);
        intent.putExtra("nickname",nickname);
        intent.putExtra("locationtype",locationtype);
        intent.putExtra("defaultstatus",defaultstatus);
        intent.putExtra("lat",latitude);
        intent.putExtra("lon",longtitude);
        intent.putExtra("pincode",pincode);
        intent.putExtra("cityname",cityname);
        intent.putExtra("address",address);
        startActivity(intent);
        //startActivity(new Intent(getApplicationContext(),AddYourPetOldUserActivity.class));
    }

    private void gotoManageAddress() {
        startActivity(new Intent(CustomerProfileScreenActivity.this, ManageAddressActivity.class));
    }

    private void confirmLogoutDialog(){
        android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(CustomerProfileScreenActivity.this);
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
    private void gotoLogout() {
      /*  session.logoutUser();
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

    private void familymembersListResponseCall() {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<FamilyMemberListResponse> call = apiInterface.familymembersListResponseCall(RestUtils.getContentType(),FamilyMemberListRequest());
        Log.w(TAG,"FamilyMemberListResponse url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<FamilyMemberListResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<FamilyMemberListResponse> call, @NonNull Response<FamilyMemberListResponse> response) {

                Log.w(TAG,"FamilyMemberListResponse"+ "--->" + new Gson().toJson(response.body()));

                avi_indicator.smoothToHide();

                if (response.body() != null) {
                    if(response.body().getCode() == 200){

                        if(response.body().getData() != null && response.body().getData().size()>0){
                            txt_no_records.setVisibility(View.GONE);
                            rv_family.setVisibility(View.VISIBLE);
                            ll_add.setVisibility(View.GONE);
                            dataBeanList = response.body().getData();
                            setView();

                        }
                        else{
                            txt_no_records.setVisibility(View.VISIBLE);
                            txt_no_records.setText(getResources().getString(R.string.no_new_fam_memb));
                            ll_add.setVisibility(View.VISIBLE);
                            rv_family.setVisibility(View.GONE);
                        }



                    }

                }


            }

            @Override
            public void onFailure(@NonNull Call<FamilyMemberListResponse> call, @NonNull Throwable t) {

                avi_indicator.smoothToHide();
                Log.w(TAG,"FamilyMemberListResponse flr"+"--->" + t.getMessage());
            }
        });

    }
    private FamilyMemberListRequest FamilyMemberListRequest() {
        FamilyMemberListRequest FamilyMemberListRequest = new FamilyMemberListRequest();
        FamilyMemberListRequest.setUser_id(userid);
        Log.w(TAG,"FamilyMemberListRequest"+ "--->" + new Gson().toJson(FamilyMemberListRequest));
        return FamilyMemberListRequest;
    }
    private void setView() {
        rv_family.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //  rv_family.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_family.setItemAnimator(new DefaultItemAnimator());
        ManagePetListAdapter managePetListAdapter = new ManagePetListAdapter(getApplicationContext(), dataBeanList, this,this);
        rv_family.setAdapter(managePetListAdapter);

    }


    @Override
    public void familyMemberDeleteListener(String id) {
        if(id != null){
            showStatusAlert(id);
        }

    }

    private void showStatusAlert(final String id) {

        try {

            dialog = new Dialog(CustomerProfileScreenActivity.this);
            dialog.setContentView(R.layout.alert_approve_reject_layout);
            TextView tvheader = (TextView)dialog.findViewById(R.id.tvInternetNotConnected);
            tvheader.setText(R.string.deletepetmsg);
            Button dialogButtonApprove = (Button) dialog.findViewById(R.id.btnApprove);
            dialogButtonApprove.setText("Yes");
            Button dialogButtonRejected = (Button) dialog.findViewById(R.id.btnReject);
            dialogButtonRejected.setText("No");

            dialogButtonApprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                    familyDeleteResponseCall(id);


                }
            });
            dialogButtonRejected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();




                }
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }




    }
    @SuppressLint("LogNotTimber")
    private void familyDeleteResponseCall(String id) {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();

        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<FamilyMemberDeleteResponse> call = apiInterface.familyDeleteResponseCall(RestUtils.getContentType(),petDeleteRequest(id));

        Log.w(TAG,"url  :%s"+call.request().url().toString());

        call.enqueue(new Callback<FamilyMemberDeleteResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NotNull Call<FamilyMemberDeleteResponse> call, @NotNull Response<FamilyMemberDeleteResponse> response) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"FamilyMemberDeleteResponse"+ "--->" + new Gson().toJson(response.body()));

                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        Toasty.success(getApplicationContext(), "Members Removed Successfully", Toast.LENGTH_SHORT, true).show();
                        finish();
                        overridePendingTransition( 0, 0);
                        startActivity(getIntent());
                        overridePendingTransition( 0, 0);

                    }
                }



            }

            @Override
            public void onFailure(@NotNull Call<FamilyMemberDeleteResponse> call, @NotNull Throwable t) {
                avi_indicator.smoothToHide();

                Log.w(TAG,"FamilyMemberDeleteResponse flr"+"--->" + t.getMessage());
            }
        });

    }
    @SuppressLint("LogNotTimber")
    private FamilyMemberDeleteRequest petDeleteRequest(String id) {

        /*
          _id : 5f05d911f3090625a91f40c7
         */
        FamilyMemberDeleteRequest familyMemberDeleteRequest = new FamilyMemberDeleteRequest();
        familyMemberDeleteRequest.set_id(id);
        Log.w(TAG,"familyMemberDeleteRequest"+ "--->" + new Gson().toJson(familyMemberDeleteRequest));
        return familyMemberDeleteRequest;
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

    public void callDirections(String tag){
        Intent intent = new Intent(getApplicationContext(), CustomerDashboardActivity.class);
        intent.putExtra("tag",tag);
        startActivity(intent);
        finish();
    }





    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.txt_manage_address:
                gotoManageAddress();
                break;
            case R.id.txt_my_addresses:
                gotoMyAddresses();
                break;
            case R.id.txt_change_password:
                break;
            case R.id.txt_logout:
                showLogOutAppAlert();
                //  confirmLogoutDialog();
                break;
            case R.id.ll_add:
                gotoAddYourPet();
                break;
            case R.id.txt_edit_profile:
                startActivity(new Intent(getApplicationContext(), PetLoverEditProfileActivity.class));
                break;
            case R.id.txt_edit_image:
                startActivity(new Intent(getApplicationContext(), PetLoverEditProfileImageActivity.class));
                break;

            case R.id.img_notification:
                startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                break;
        }
    }


    private void setMargins(RelativeLayout rl_layout, int i, int i1, int i2, int i3) {

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)rl_layout.getLayoutParams();
        params.setMargins(i, i1, i2, i3);
        rl_layout.setLayoutParams(params);
    }


    @Override
    public void gotoAddFamilyMembersOldActivityListener(String id) {

        Intent intent = new Intent(getApplicationContext(), AddYourFamilyMembersOldActivity.class);
        intent.putExtra("doctorid",doctorid);
        intent.putExtra("doctorname",doctorname);
        intent.putExtra("distance",distance);
        intent.putExtra("catid",catid);
        intent.putExtra("from",from);
        intent.putExtra("reviewcount",reviewcount);
        intent.putExtra("Count_value_start",Count_value_start);
        intent.putExtra("Count_value_end",Count_value_end);
        intent.putExtra("spid",spid);
        intent.putExtra("spuserid",spuserid);
        intent.putExtra("_id",_id);
        intent.putExtra("orderid",orderid);
        intent.putExtra("product_id",product_id);
        intent.putExtra("cancelorder",cancelorder);
        intent.putExtra("product_idList",product_idList);
        intent.putExtra("id",id);
        intent.putExtra("userid",userid);
        intent.putExtra("nickname",nickname);
        intent.putExtra("locationtype",locationtype);
        intent.putExtra("defaultstatus",defaultstatus);
        intent.putExtra("lat",latitude);
        intent.putExtra("lon",longtitude);
        intent.putExtra("pincode",pincode);
        intent.putExtra("cityname",cityname);
        intent.putExtra("address",address);
        startActivity(intent);

    }
}