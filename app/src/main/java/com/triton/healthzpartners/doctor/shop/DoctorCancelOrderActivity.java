package com.triton.healthzpartners.doctor.shop;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.triton.healthzpartners.R;
import com.triton.healthzpartners.activity.NotificationActivity;
import com.triton.healthzpartners.adapter.MyCouponsTextAdapter;
import com.triton.healthzpartners.api.APIClient;
import com.triton.healthzpartners.api.RestApiInterface;
import com.triton.healthzpartners.doctor.DoctorDashboardActivity;
import com.triton.healthzpartners.doctor.DoctorOrderDetailsActivity;
import com.triton.healthzpartners.doctor.DoctorProfileScreenActivity;
import com.triton.healthzpartners.doctor.MyCouponsDoctorActivity;
import com.triton.healthzpartners.interfaces.OnAppointmentSuccessfullyCancel;
import com.triton.healthzpartners.customer.PetMyOrdrersActivity;
import com.triton.healthzpartners.requestpojo.PetLoverCancelOrderRequest;
import com.triton.healthzpartners.requestpojo.PetLoverCancelSingleOrderRequest;
import com.triton.healthzpartners.requestpojo.RefundCouponCreateRequest;
import com.triton.healthzpartners.requestpojo.UpdateStatusCancelRequest;
import com.triton.healthzpartners.responsepojo.CouponCodeTextResponse;
import com.triton.healthzpartners.responsepojo.DropDownListResponse;
import com.triton.healthzpartners.responsepojo.SuccessResponse;
import com.triton.healthzpartners.responsepojo.VendorOrderUpdateResponse;
import com.triton.healthzpartners.responsepojo.VendorReasonListResponse;
import com.triton.healthzpartners.sessionmanager.SessionManager;
import com.triton.healthzpartners.utils.ConnectionDetector;
import com.triton.healthzpartners.utils.RestUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorCancelOrderActivity extends AppCompatActivity implements View.OnClickListener, OnAppointmentSuccessfullyCancel {

    private static final String TAG = "DoctorCancelOrderActivity" ;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.spr_reason)
    Spinner spr_reason;



    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_cancel_order)
    Button btn_cancel_order;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_comment)
    EditText edt_comment;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.avi_indicator)
    AVLoadingIndicatorView avi_indicator;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.include_doctor_footer)
    View include_doctor_footer;


    private List<DropDownListResponse.DataBean.SpecialzationBean> petSpecilaziationList;
    private String _id;
    HashMap<String, String> hashMap_ReasonTypeid = new HashMap<>();
    private String strSelectedReason = "";
    private Dialog dialog;

    String User_cancell_info = "";
    private String cancelorder;

    ArrayList<Integer> product_idList;
    private int product_id;
    private String orderid;
    private String fromactivity;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.include_doctor_header)
    View include_doctor_header;


    /* Bottom Navigation */

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
    @BindView(R.id.img_community)
    ImageView img_community;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rl_homes)
    RelativeLayout rl_homes;

    TextView txt_no_records_coupon;
    RecyclerView rv_successfully_cancelled;
    private List<CouponCodeTextResponse.DataBean> myCouponsTextList;
    private String userid;
    private int Order_price = 0;


    @SuppressLint({"LogNotTimber", "LongLogTag"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_cancel_order);
        ButterKnife.bind(this);

        SessionManager session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getProfileDetails();
        userid = user.get(SessionManager.KEY_ID);

        ImageView img_back = include_doctor_header.findViewById(R.id.img_back);
        ImageView img_notification = include_doctor_header.findViewById(R.id.img_notification);
        ImageView img_cart = include_doctor_header.findViewById(R.id.img_cart);
        ImageView img_profile = include_doctor_header.findViewById(R.id.img_profile);
        TextView toolbar_title = include_doctor_header.findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.cancel_order));

        img_cart.setVisibility(View.GONE);

        img_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
            }
        });
        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), DoctorProfileScreenActivity.class));
            }
        });

        img_back.setOnClickListener(this);

        edt_comment.setVisibility(View.GONE);


        //bottom_navigation_view.getMenu().findItem(R.id.shop).setChecked(true);
        /*shop*/
        title_community.setTextColor(getResources().getColor(R.color.darker_grey_new,getTheme()));
        img_community.setImageResource(R.drawable.grey_community);
        title_shop.setTextColor(getResources().getColor(R.color.new_gree_color,getTheme()));
        img_shop.setImageResource(R.drawable.green_shop);


        rl_home.setOnClickListener(this);
        rl_shop.setOnClickListener(this);
        rl_comn.setOnClickListener(this);
        rl_homes.setOnClickListener(this);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            _id = extras.getString("_id");
            orderid = extras.getString("orderid");
            product_id = extras.getInt("product_id");
            cancelorder = extras.getString("cancelorder");
            Order_price = extras.getInt("Order_price");
            fromactivity = extras.getString("fromactivity");
            Log.w(TAG,"_id : "+_id+" fromactivity : "+fromactivity);
           product_idList = getIntent().getIntegerArrayListExtra("product_idList");
            Log.w(TAG,"product_idList : "+ new Gson().toJson(product_idList)+" cancelorder :"+cancelorder);

        }

        if (new ConnectionDetector(DoctorCancelOrderActivity.this).isNetworkAvailable(DoctorCancelOrderActivity.this)) {
            vendorReasonListResponseCall();

        }

        spr_reason.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int arg2, long arg3) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.green));
                strSelectedReason = spr_reason.getSelectedItem().toString();
                Log.w(TAG,"strSelectedReason : "+strSelectedReason);

                if(strSelectedReason != null){
                    if(strSelectedReason.equalsIgnoreCase("Other")){
                        edt_comment.setVisibility(View.VISIBLE);
                    }else{
                        edt_comment.setVisibility(View.GONE);
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        btn_cancel_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validdSelectedReason()){
                    showCancelAlert();
                }
            }
        });





    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                onBackPressed();
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






    @SuppressLint({"LogNotTimber", "LongLogTag"})
    public void vendorReasonListResponseCall(){

        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        //Creating an object of our api interface
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<VendorReasonListResponse> call = apiInterface.vendorReasonListResponseCall(RestUtils.getContentType());
        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<VendorReasonListResponse>() {
            @SuppressLint({"SetTextI18n", "LongLogTag"})
            @Override
            public void onResponse(@NonNull Call<VendorReasonListResponse> call, @NonNull Response<VendorReasonListResponse> response) {
                avi_indicator.smoothToHide();


                if (response.body() != null) {
                    if(200 == response.body().getCode()){
                        Log.w(TAG,"DropDownListResponse" + new Gson().toJson(response.body()));
                        if(response.body().getData().getCancel_status() != null &&response.body().getData().getCancel_status().size()>0 ) {
                            setReasonList(response.body().getData().getCancel_status());
                        }




                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<VendorReasonListResponse> call, @NonNull  Throwable t) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"VendorReasonListResponse flr"+t.getMessage());
            }
        });

    }
    @SuppressLint("LongLogTag")
    private void setReasonList(List<VendorReasonListResponse.DataBean.CancelStatusBean> cancel_status) {
        ArrayList<String> pettypeArrayList = new ArrayList<>();
        pettypeArrayList.add("Select the reason");
        for (int i = 0; i < cancel_status.size(); i++) {
            String petType = cancel_status.get(i).getTitle();
            Log.w(TAG,"petType-->"+petType);
            pettypeArrayList.add(petType);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(DoctorCancelOrderActivity.this, R.layout.spinner_item, pettypeArrayList);
            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item); // The drop down view
            spr_reason.setAdapter(spinnerArrayAdapter);


        }
    }
    private void showCancelAlert() {

        try {

             dialog = new Dialog(DoctorCancelOrderActivity.this);
            dialog.setContentView(R.layout.alert_cancel_layout);
            dialog.setCanceledOnTouchOutside(false);
            Button btn_ok = dialog.findViewById(R.id.btn_ok);
            Button btn_cancel = dialog.findViewById(R.id.btn_cancel);
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (new ConnectionDetector(DoctorCancelOrderActivity.this).isNetworkAvailable(DoctorCancelOrderActivity.this)) {
                        if(cancelorder != null && cancelorder.equalsIgnoreCase("bulk")){
                            petlover_update_order_cancel_ResponseCall(product_idList);
                        }else{
                            petlover_update_order_cancel_single_ResponseCall();
                        }


                    }






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


    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private void update_status_cancelResponseCall() {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<SuccessResponse> call = apiInterface.update_status_cancelResponseCall(RestUtils.getContentType(), updateStatusCancelRequest());
        Log.w(TAG,"vendorOrderDetailsResponseCall url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @SuppressLint({"LongLogTag", "LogNotTimber", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {

                Log.w(TAG,"update_status_cancelResponseCall"+ "--->" + new Gson().toJson(response.body()));

                avi_indicator.smoothToHide();

                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        dialog.dismiss();
                        startActivity(new Intent(DoctorCancelOrderActivity.this, PetMyOrdrersActivity.class));
                        finish();


                    }

                }


            }

            @SuppressLint({"LongLogTag", "LogNotTimber"})
            @Override
            public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {

                avi_indicator.smoothToHide();
                Log.w(TAG,"update_status_cancelResponseCall flr"+"--->" + t.getMessage());
            }
        });

    }
    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private UpdateStatusCancelRequest updateStatusCancelRequest() {
        /*
         * _id : 604f4386a358454d3208f685
         * activity_id : 4
         * activity_title : Order Cancelled
         * activity_date : 11-03-2021 03:07 PM
         * order_status : Cancelled
         * user_cancell_info : I have order wrongly
         * user_cancell_date : 11-03-2021 03:07 PM
         */


        if(strSelectedReason.equalsIgnoreCase("Other")){
            edt_comment.setVisibility(View.VISIBLE);
            User_cancell_info = edt_comment.getText().toString();
        }else{
            User_cancell_info = strSelectedReason;
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        UpdateStatusCancelRequest updateStatusCancelRequest = new UpdateStatusCancelRequest();
        updateStatusCancelRequest.set_id(_id);
        updateStatusCancelRequest.setActivity_id(4);
        updateStatusCancelRequest.setActivity_title("Order Cancelled");
        updateStatusCancelRequest.setActivity_date(currentDateandTime);
        updateStatusCancelRequest.setOrder_status("Cancelled");
        updateStatusCancelRequest.setUser_cancell_info(User_cancell_info);
        updateStatusCancelRequest.setUser_cancell_date(currentDateandTime);

        Log.w(TAG,"updateStatusCancelRequest"+ "--->" + new Gson().toJson(updateStatusCancelRequest));

        return updateStatusCancelRequest;
    }


    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private void petlover_update_order_cancel_ResponseCall(List<Integer> product_id) {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<VendorOrderUpdateResponse> call = apiInterface.petlover_update_order_cancel_ResponseCall(RestUtils.getContentType(), petLoverCancelOrderRequest(product_id));
        Log.w(TAG,"petlover_update_order_cancel_ResponseCall url  :%s"+" "+ call.request().url().toString());
        call.enqueue(new Callback<VendorOrderUpdateResponse>() {
            @SuppressLint({"LongLogTag", "LogNotTimber", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<VendorOrderUpdateResponse> call, @NonNull Response<VendorOrderUpdateResponse> response) {
                Log.w(TAG,"petlover_update_order_cancel_ResponseCall"+ "--->" + new Gson().toJson(response.body()));
                avi_indicator.smoothToHide();
                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        dialog.dismiss();
                        if(Order_price != 0) {
                            showSuccessfullyCancelled();
                        }else{
                            Intent intent = new Intent(DoctorCancelOrderActivity.this, DoctorOrderDetailsActivity.class);
                            intent.putExtra("_id",orderid);
                            intent.putExtra("fromactivity",fromactivity);
                            startActivity(intent);
                            finish();
                        }


                    }

                }


            }

            @SuppressLint({"LongLogTag", "LogNotTimber"})
            @Override
            public void onFailure(@NonNull Call<VendorOrderUpdateResponse> call, @NonNull Throwable t) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"vendor_update_order_confirm_ResponseCall flr"+"--->" + t.getMessage());
            }
        });

    }

    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private PetLoverCancelOrderRequest petLoverCancelOrderRequest(List<Integer> product_id) {
        /*
         * order_id : ORDER-1618919599393
         * product_id : [0,1,2]
         * date : 20-04-2021 12:47 PM
         * reject_reason :
         */

        if(strSelectedReason.equalsIgnoreCase("Other")){
            edt_comment.setVisibility(View.VISIBLE);
            User_cancell_info = edt_comment.getText().toString();
        }else{
            User_cancell_info = strSelectedReason;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        PetLoverCancelOrderRequest petLoverCancelOrderRequest = new PetLoverCancelOrderRequest();
        petLoverCancelOrderRequest.setOrder_id(orderid);
        petLoverCancelOrderRequest.setProduct_id(product_id);
        petLoverCancelOrderRequest.setDate(currentDateandTime);
        petLoverCancelOrderRequest.setReject_reason(User_cancell_info);
        Log.w(TAG,"petLoverCancelOrderRequest"+ "--->" + new Gson().toJson(petLoverCancelOrderRequest));
        return petLoverCancelOrderRequest;
    }
   @SuppressLint({"LongLogTag", "LogNotTimber"})

    private void petlover_update_order_cancel_single_ResponseCall() {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<VendorOrderUpdateResponse> call = apiInterface.petlover_update_order_cancel_single_ResponseCall(RestUtils.getContentType(), petLoverCancelSingleOrderRequest());
        Log.w(TAG,"vendor_update_order_confirm_ResponseCall url  :%s"+" "+ call.request().url().toString());
        call.enqueue(new Callback<VendorOrderUpdateResponse>() {
            @SuppressLint({"LongLogTag", "LogNotTimber", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<VendorOrderUpdateResponse> call, @NonNull Response<VendorOrderUpdateResponse> response) {
                Log.w(TAG,"vendorOrderDetailsResponseCall"+ "--->" + new Gson().toJson(response.body()));
                avi_indicator.smoothToHide();
                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        dialog.dismiss();
                        if(Order_price !=0) {
                            showSuccessfullyCancelled();
                        }else{
                            Intent intent = new Intent(DoctorCancelOrderActivity.this,DoctorOrderDetailsActivity.class);
                            intent.putExtra("_id",orderid);
                            intent.putExtra("fromactivity",fromactivity);
                            startActivity(intent);
                            finish();
                        }
                       /* Intent intent = new Intent(DoctorCancelOrderActivity.this,DoctorOrderDetailsActivity.class);
                        intent.putExtra("_id",orderid);
                        intent.putExtra("fromactivity",fromactivity);
                        startActivity(intent);
                        finish();*/



                    }

                }


            }

            @SuppressLint({"LongLogTag", "LogNotTimber"})
            @Override
            public void onFailure(@NonNull Call<VendorOrderUpdateResponse> call, @NonNull Throwable t) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"vendor_update_order_confirm_ResponseCall flr"+"--->" + t.getMessage());
            }
        });

    }
    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private PetLoverCancelSingleOrderRequest petLoverCancelSingleOrderRequest() {
        /*
         * order_id : ORDER-1618919599393
         * product_id : 0
         * date : 20-04-2021 12:47 PM
         * reject_reason :
         */

        if(strSelectedReason.equalsIgnoreCase("Other")){
            edt_comment.setVisibility(View.VISIBLE);
            User_cancell_info = edt_comment.getText().toString();
        }else{
            User_cancell_info = strSelectedReason;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        PetLoverCancelSingleOrderRequest petLoverCancelSingleOrderRequest = new PetLoverCancelSingleOrderRequest();
        petLoverCancelSingleOrderRequest.setOrder_id(orderid);
        petLoverCancelSingleOrderRequest.setProduct_id(product_id);
        petLoverCancelSingleOrderRequest.setDate(currentDateandTime);
        petLoverCancelSingleOrderRequest.setReject_reason(User_cancell_info);
        Log.w(TAG,"petLoverCancelSingleOrderRequest"+ "--->" + new Gson().toJson(petLoverCancelSingleOrderRequest));
        return petLoverCancelSingleOrderRequest;
    }




    public void callDirections(String tag){
        Intent intent = new Intent(getApplicationContext(), DoctorDashboardActivity.class);
        intent.putExtra("tag",tag);
        startActivity(intent);
        finish();
    }




    private void showSuccessfullyCancelled() {
        try {
            dialog = new Dialog(DoctorCancelOrderActivity.this);
            dialog.setContentView(R.layout.alert_successfulley_cancelled_layout);
            dialog.setCancelable(false);
            txt_no_records_coupon = dialog.findViewById(R.id.txt_no_records);
            rv_successfully_cancelled = dialog.findViewById(R.id.rv_successfully_cancelled);
            txt_no_records_coupon.setVisibility(View.GONE);
            rv_successfully_cancelled.setVisibility(View.GONE);

            if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
                CouponCodeTextResponseCall();
            }


            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();


        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }




    }

    @SuppressLint("LogNotTimber")
    private void CouponCodeTextResponseCall() {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<CouponCodeTextResponse> call = apiInterface.CouponCodeTextResponseCall(RestUtils.getContentType());
        Log.w(TAG,"CouponCodeTextResponse url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<CouponCodeTextResponse>() {
            @SuppressLint({"SetTextI18n", "LogNotTimber"})
            @Override
            public void onResponse(@NonNull Call<CouponCodeTextResponse> call, @NonNull Response<CouponCodeTextResponse> response) {

                Log.w(TAG,"CouponCodeTextResponse"+ "--->" + new Gson().toJson(response.body()));

                avi_indicator.smoothToHide();

                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        if(response.body().getData() != null && response.body().getData().size()>0){
                            txt_no_records_coupon.setVisibility(View.GONE);
                            rv_successfully_cancelled.setVisibility(View.VISIBLE);
                            myCouponsTextList = response.body().getData();
                            setViewCouponText();

                        }
                        else{
                            rv_successfully_cancelled.setVisibility(View.GONE);
                            txt_no_records_coupon.setVisibility(View.VISIBLE);
                            txt_no_records_coupon.setText("No data found");

                        }



                    }

                }


            }

            @Override
            public void onFailure(@NonNull Call<CouponCodeTextResponse> call, @NonNull Throwable t) {

                avi_indicator.smoothToHide();
                Log.w(TAG,"CouponCodeTextResponse flr"+"--->" + t.getMessage());
            }
        });

    }
    private void setViewCouponText() {
        String ServiceCost = "0";

        try {
            ServiceCost = String.valueOf(Order_price);
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }
        rv_successfully_cancelled.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_successfully_cancelled.setItemAnimator(new DefaultItemAnimator());
        MyCouponsTextAdapter myCouponsTextAdapter = new MyCouponsTextAdapter(getApplicationContext(), myCouponsTextList,ServiceCost,this);
        rv_successfully_cancelled.setAdapter(myCouponsTextAdapter);

    }

    @Override
    public void onAppointmentSuccessfullyCancel(String refund, String cost) {
        Log.w(TAG,"onAppointmentSuccessfullyCancel : "+"refund : "+refund+"cost : "+cost);
        if(refund != null && !refund.isEmpty()){
            RefundCouponCreateRequestCall(refund,cost);
        }else{
            RefundCouponBankCreateRequestCall(refund,cost);

        }

    }

    private void RefundCouponCreateRequestCall(String refund, String cost) {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<SuccessResponse> call = ApiService.RefundCouponCreateRequestCall(RestUtils.getContentType(),refundCouponCreateRequest(refund,cost));

        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"RefundCouponCreateRequestCall"+ "--->" + new Gson().toJson(response.body()));


                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        dialog.dismiss();
                        showRefundSuccessfully("Coupon code generated successfully. Generated coupon will also be available in My Coupons.");

                    }

                }


            }

            @Override
            public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                avi_indicator.smoothToHide();

                Log.w(TAG,"RefundCouponCreateRequestCall flr"+"--->" + t.getMessage());
            }
        });

    }
    private RefundCouponCreateRequest refundCouponCreateRequest(String refund, String cost) {

        /*
         * created_by : User
         * coupon_type : 1
         * code : REF100
         * amount : 100
         * user_details : 123123
         * used_status : Not Used
         */
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMhhmmss");
        String currentDateandTime = simpleDateFormat.format(new Date());



        RefundCouponCreateRequest refundCouponCreateRequest = new RefundCouponCreateRequest();
        refundCouponCreateRequest.setCreated_by("User");
        refundCouponCreateRequest.setCoupon_type("3");
        refundCouponCreateRequest.setCode("REF"+currentDateandTime);
        refundCouponCreateRequest.setAmount(Order_price);
        refundCouponCreateRequest.setUser_details(userid);
        refundCouponCreateRequest.setUsed_status("Not Used");
        refundCouponCreateRequest.setMobile_type("Android");
        Log.w(TAG,"refundCouponCreateRequest"+ "--->" + new Gson().toJson(refundCouponCreateRequest));
        return refundCouponCreateRequest;
    }

    private void RefundCouponBankCreateRequestCall(String refund, String cost) {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<SuccessResponse> call = ApiService.RefundCouponCreateRequestCall(RestUtils.getContentType(),refundCouponCreateRequest1(refund,cost));

        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"RefundCouponCreateRequestCall"+ "--->" + new Gson().toJson(response.body()));


                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        dialog.dismiss();
                        showRefundSuccessfully("Your refund will be processed in 4-5 working days.");
                    }

                }


            }

            @Override
            public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                avi_indicator.smoothToHide();

                Log.w(TAG,"RefundCouponCreateRequestCall flr"+"--->" + t.getMessage());
            }
        });

    }
    private RefundCouponCreateRequest refundCouponCreateRequest1(String refund, String cost) {

        /*
         * created_by : User
         * coupon_type : 1
         * code : REF100
         * amount : 100
         * user_details : 123123
         * used_status : Not Used
         */
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
        String currentDateandTime = simpleDateFormat.format(new Date());



        RefundCouponCreateRequest refundCouponCreateRequest = new RefundCouponCreateRequest();
        refundCouponCreateRequest.setCreated_by("");
        refundCouponCreateRequest.setCoupon_type("3");
        refundCouponCreateRequest.setCode("Bank");
        refundCouponCreateRequest.setAmount(0);
        refundCouponCreateRequest.setUser_details(orderid);
        refundCouponCreateRequest.setUsed_status("");
        refundCouponCreateRequest.setMobile_type("Android");


        Log.w(TAG,"refundCouponCreateRequest"+ "--->" + new Gson().toJson(refundCouponCreateRequest));
        return refundCouponCreateRequest;
    }

    private void showRefundSuccessfully(String Message) {

        try {

            dialog = new Dialog(DoctorCancelOrderActivity.this);
            dialog.setContentView(R.layout.alert_approve_reject_layout);
            TextView tvheader = dialog.findViewById(R.id.tvInternetNotConnected);
            tvheader.setText(Message);
            Button dialogButtonApprove = dialog.findViewById(R.id.btnApprove);
            dialogButtonApprove.setText("Ok");
            Button dialogButtonRejected = dialog.findViewById(R.id.btnReject);
            dialogButtonRejected.setText("No");
            dialogButtonRejected.setVisibility(View.GONE);

            dialogButtonApprove.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), MyCouponsDoctorActivity.class);
                intent.putExtra("_id",orderid);
                startActivity(intent);
                finish();
                //startActivity(new Intent(getApplicationContext(), MyCouponsActivity.class));
                dialog.dismiss();




            });
            dialogButtonRejected.setOnClickListener(view -> {
                // Toasty.info(context, "Rejected Successfully", Toast.LENGTH_SHORT, true).show();
                dialog.dismiss();




            });
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }




    }

    public boolean validdSelectedReason() {
        if (strSelectedReason.equalsIgnoreCase("Select the reason")) {
            final AlertDialog alertDialog = new AlertDialog.Builder(DoctorCancelOrderActivity.this).create();
            alertDialog.setMessage(getString(R.string.err_msg_type_of_reason_for_cancellation));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    (dialog, which) -> alertDialog.cancel());
            alertDialog.show();

            return false;
        }

        return true;
    }

}