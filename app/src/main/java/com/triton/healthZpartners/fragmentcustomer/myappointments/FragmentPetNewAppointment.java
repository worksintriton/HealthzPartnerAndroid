package com.triton.healthZpartners.fragmentcustomer.myappointments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.triton.healthZpartners.R;
import com.triton.healthZpartners.adapter.MyCouponsTextAdapter;
import com.triton.healthZpartners.adapter.PetNewAppointmentAdapter;
import com.triton.healthZpartners.api.APIClient;
import com.triton.healthZpartners.api.RestApiInterface;
import com.triton.healthZpartners.interfaces.OnAppointmentCancel;
import com.triton.healthZpartners.interfaces.OnAppointmentSuccessfullyCancel;
import com.triton.healthZpartners.customer.MyCouponsActivity;

import com.triton.healthZpartners.customer.PetMyappointmentsActivity;
import com.triton.healthZpartners.requestpojo.AppoinmentCancelledRequest;
import com.triton.healthZpartners.requestpojo.AppointmentDetailsRequest;
import com.triton.healthZpartners.requestpojo.NotificationSendRequest;
import com.triton.healthZpartners.requestpojo.PetLoverAppointmentRequest;
import com.triton.healthZpartners.requestpojo.PetNewAppointmentDetailsRequest;
import com.triton.healthZpartners.requestpojo.RefundCouponCreateRequest;
import com.triton.healthZpartners.requestpojo.SPNotificationSendRequest;
import com.triton.healthZpartners.responsepojo.AppoinmentCancelledResponse;
import com.triton.healthZpartners.responsepojo.CouponCodeTextResponse;
import com.triton.healthZpartners.responsepojo.NotificationSendResponse;
import com.triton.healthZpartners.responsepojo.PetAppointmentResponse;
import com.triton.healthZpartners.responsepojo.PetNewAppointmentDetailsResponse;
import com.triton.healthZpartners.responsepojo.SPAppointmentDetailsResponse;
import com.triton.healthZpartners.responsepojo.SuccessResponse;
import com.triton.healthZpartners.sessionmanager.SessionManager;
import com.triton.healthZpartners.utils.ConnectionDetector;
import com.triton.healthZpartners.utils.RestUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentPetNewAppointment extends Fragment implements OnAppointmentCancel, View.OnClickListener, OnAppointmentSuccessfullyCancel {
    private String TAG = "FragmentPetNewAppointment";

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.avi_indicator)
    AVLoadingIndicatorView avi_indicator;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_no_records)
    TextView txt_no_records;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_newappointment)
    RecyclerView rv_newappointment;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_load_more)
    Button btn_load_more;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refresh_layout;


   private ShimmerFrameLayout mShimmerViewContainer;
   private View includelayout;




    SessionManager session;
    String type = "",name = "",userid = "";
    private SharedPreferences preferences;
    private Context mContext;
    private List<PetAppointmentResponse.DataBean> newAppointmentResponseList;
    private Dialog dialog;
    private String doctorid;

    TextView txt_no_records_coupon;
    RecyclerView rv_successfully_cancelled;
    private List<CouponCodeTextResponse.DataBean> myCouponsTextList;
    private String ServiceCost ="";
    private String Appointmenttype = "";
    private String Paymentmethod;
    private String Appointmetnt_id;
    private boolean isrefund;
    private String appointment_id;


    public FragmentPetNewAppointment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.w(TAG,"onCreateView");

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        View view = inflater.inflate(R.layout.fragment_pet_new_appointment, container, false);

        ButterKnife.bind(this, view);
        mContext = getActivity();

         includelayout = view.findViewById(R.id.includelayout);
         mShimmerViewContainer = includelayout.findViewById(R.id.shimmer_layout);

        avi_indicator.setVisibility(View.GONE);
        btn_load_more.setVisibility(View.GONE);
        btn_load_more.setOnClickListener(this);

        session = new SessionManager(getContext());
        HashMap<String, String> user = session.getProfileDetails();
        userid = user.get(SessionManager.KEY_ID);
        Log.w(TAG," userid : "+userid);

      

        if (new ConnectionDetector(getActivity()).isNetworkAvailable(getActivity())) {
            petNewAppointmentResponseCall();
        }
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            //your method here
                                petNewAppointmentResponseCall();


                        } catch (Exception ignored) {
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 30000);//you can put 30000(30 secs)


       /* ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);

        // This schedule a runnable task every 2 minutes
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                petNewAppointmentResponseCall();
            }
        }, 0, 5, TimeUnit.SECONDS);*/


        refresh_layout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (new ConnectionDetector(getActivity()).isNetworkAvailable(getActivity())) {
                            petNewAppointmentResponseCall();

                        }

                    }
                }
        );



        return view;
    }



    @SuppressLint("LogNotTimber")
    private void petNewAppointmentResponseCall() {
       /* avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();*/
        mShimmerViewContainer.startShimmerAnimation();
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<PetAppointmentResponse> call = ApiService.petAppointmentResponseCall(RestUtils.getContentType(),petLoverAppointmentRequest());
        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<PetAppointmentResponse>() {
            @SuppressLint({"LogNotTimber", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<PetAppointmentResponse> call, @NonNull Response<PetAppointmentResponse> response) {
             //  avi_indicator.smoothToHide();
                refresh_layout.setRefreshing(false);
                mShimmerViewContainer.stopShimmerAnimation();
                includelayout.setVisibility(View.GONE);

                Log.w(TAG,"PetNewAppointmentResponse"+ "--->" + new Gson().toJson(response.body()));


               if (response.body() != null) {

                   if(200 == response.body().getCode()){

                       if(response.body().getData() != null && response.body().getData().size()>0){
                           newAppointmentResponseList = response.body().getData();
                           Log.w(TAG,"Size"+newAppointmentResponseList.size());
                           Log.w(TAG,"newAppointmentResponseList : "+new Gson().toJson(newAppointmentResponseList));
                           txt_no_records.setVisibility(View.GONE);
                           rv_newappointment.setVisibility(View.VISIBLE);
                           if(newAppointmentResponseList.size()>3){
                               btn_load_more.setVisibility(View.VISIBLE);
                           }else{
                               btn_load_more.setVisibility(View.GONE);
                           }
                           setView();


                       }else{
                           rv_newappointment.setVisibility(View.GONE);
                           btn_load_more.setVisibility(View.GONE);
                           txt_no_records.setVisibility(View.VISIBLE);
                           txt_no_records.setText("No new appointments");
                       }

                   }



                }
            }

            @Override
            public void onFailure(@NonNull Call<PetAppointmentResponse> call, @NonNull Throwable t) {
                //avi_indicator.smoothToHide();
                mShimmerViewContainer.stopShimmerAnimation();
                includelayout.setVisibility(View.GONE);

                Log.w(TAG,"PetNewAppointmentResponse"+"--->" + t.getMessage());
            }
        });

    }
    @SuppressLint("LogNotTimber")
    private PetLoverAppointmentRequest petLoverAppointmentRequest() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentDateandTime = simpleDateFormat.format(new Date());

        PetLoverAppointmentRequest petLoverAppointmentRequest = new PetLoverAppointmentRequest();
        petLoverAppointmentRequest.setUser_id(userid);
        petLoverAppointmentRequest.setCurrent_time(currentDateandTime);
        Log.w(TAG,"petLoverAppointmentRequest"+ "--->" + new Gson().toJson(petLoverAppointmentRequest));
        return petLoverAppointmentRequest;
    }
    private void setView() {
        rv_newappointment.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_newappointment.setItemAnimator(new DefaultItemAnimator());
        int size = 3;
        PetNewAppointmentAdapter petNewAppointmentAdapter = new PetNewAppointmentAdapter(getContext(), newAppointmentResponseList, rv_newappointment,size,this);
        rv_newappointment.setAdapter(petNewAppointmentAdapter);

    }
    private void setViewLoadMore() {
        rv_newappointment.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_newappointment.setItemAnimator(new DefaultItemAnimator());
        int size = newAppointmentResponseList.size();
        PetNewAppointmentAdapter petNewAppointmentAdapter = new PetNewAppointmentAdapter(getContext(), newAppointmentResponseList, rv_newappointment,size,this);
        rv_newappointment.setAdapter(petNewAppointmentAdapter);

    }

    @Override
    public void onAppointmentCancel(String id,String appointmenttype,String userid, String doctorid,String appointmentid,String spid,String cost,String paymentmethod) {
        Paymentmethod =paymentmethod;
        Appointmetnt_id =id;
        if(id != null){
            showStatusAlert(id,appointmenttype,userid,doctorid,appointmentid,spid);
            ServiceCost = cost;
        }
    }

    private void showStatusAlert(String id,String appointmenttype,String userid, String doctorid,String appointmentid, String spid) {
        try {
            appointment_id =id;
            dialog = new Dialog(mContext);
            dialog.setContentView(R.layout.alert_approve_reject_layout);
            TextView tvheader = (TextView)dialog.findViewById(R.id.tvInternetNotConnected);
            tvheader.setText(R.string.cancelappointment);
            Button dialogButtonApprove = (Button) dialog.findViewById(R.id.btnApprove);
            dialogButtonApprove.setText("Yes");
            Button dialogButtonRejected = (Button) dialog.findViewById(R.id.btnReject);
            dialogButtonRejected.setText("No");

            dialogButtonApprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    if(appointmenttype != null && appointmenttype.equalsIgnoreCase("Doctor")){
                        Appointmenttype = "1";
                       // petAppointmentResponseCall(id,appointmenttype,userid,doctorid,appointmentid,"1");
                      appoinmentCancelledResponseCall(id,appointmenttype,userid,doctorid,appointmentid,"1");

                    } else if(appointmenttype != null && appointmenttype.equalsIgnoreCase("SP")){
                        Appointmenttype = "2";
                       // spAppointmentDetailsResponse(id,appointmenttype,userid,doctorid,appointmentid,spid,"2");
                        spappoinmentCancelledResponseCall(id,appointmenttype,userid,doctorid,appointmentid,spid,"2");

                    }



                }
            });
            dialogButtonRejected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Toasty.info(context, "Rejected Successfully", Toast.LENGTH_SHORT, true).show();
                    dialog.dismiss();




                }
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }




    }
    private void showSuccessfullyCancelled(String type) {
        try {
            dialog = new Dialog(mContext);
            dialog.setContentView(R.layout.alert_successfulley_cancelled_layout);
            dialog.setCancelable(false);
            txt_no_records_coupon = dialog.findViewById(R.id.txt_no_records);
            rv_successfully_cancelled = dialog.findViewById(R.id.rv_successfully_cancelled);
            txt_no_records_coupon.setVisibility(View.GONE);
            rv_successfully_cancelled.setVisibility(View.GONE);

            if (new ConnectionDetector(mContext).isNetworkAvailable(mContext)) {
                CouponCodeTextResponseCall();
            }


            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();


        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }




    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_load_more:
                setViewLoadMore();
                break;
        }
    }

    @SuppressLint("LogNotTimber")
    private void appoinmentCancelledResponseCall(String id, String appointmenttype, String userid, String doctorid, String appointmentid, String type) {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<AppoinmentCancelledResponse> call = apiInterface.appoinmentCancelledResponseCall(RestUtils.getContentType(), appoinmentCancelledRequest(id));
        Log.w(TAG,"appoinmentCancelledResponseCall url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<AppoinmentCancelledResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<AppoinmentCancelledResponse> call, @NonNull Response<AppoinmentCancelledResponse> response) {

                Log.w(TAG,"appoinmentCancelledResponseCall"+ "--->" + new Gson().toJson(response.body()));

                avi_indicator.smoothToHide();

                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        notificationSendResponseCall(userid,doctorid,appointmentid,type);
                    }
                    else{
                        //showErrorLoading(response.body().getMessage());
                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<AppoinmentCancelledResponse> call, @NonNull Throwable t) {

                avi_indicator.smoothToHide();
                Log.w(TAG,"appoinmentCancelledResponseCall flr"+"--->" + t.getMessage());
            }
        });

    }

    @SuppressLint("LogNotTimber")
    private void spappoinmentCancelledResponseCall(String id, String appointmenttype, String userid, String doctorid, String appointmentid, String spid, String s) {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<AppoinmentCancelledResponse> call = apiInterface.spappoinmentCancelledResponseCall(RestUtils.getContentType(), appoinmentCancelledRequest(id));
        Log.w(TAG,"spappoinmentCancelledResponseCall url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<AppoinmentCancelledResponse>() {
            @Override
            public void onResponse(@NonNull Call<AppoinmentCancelledResponse> call, @NonNull Response<AppoinmentCancelledResponse> response) {

                Log.w(TAG,"appoinmentCancelledResponseCall"+ "--->" + new Gson().toJson(response.body()));

                avi_indicator.smoothToHide();

                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        spnotificationSendResponseCall(userid,spid,appointmentid);

                    }
                    else{
                        //showErrorLoading(response.body().getMessage());
                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<AppoinmentCancelledResponse> call, @NonNull Throwable t) {

                avi_indicator.smoothToHide();
                Log.w(TAG,"appoinmentCancelledResponseCall flr"+"--->" + t.getMessage());
            }
        });

    }

    @SuppressLint("LogNotTimber")
    private AppoinmentCancelledRequest appoinmentCancelledRequest(String id) {

        /*
         * _id : 5fc639ea72fc42044bfa1683
         * missed_at : 23-10-2000 10 : 00 AM
         * doc_feedback :
         * appoint_patient_st:Patient Appointment Cancelled
         * appoinment_status : Missed
         */


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        AppoinmentCancelledRequest appoinmentCancelledRequest = new AppoinmentCancelledRequest();
        appoinmentCancelledRequest.set_id(id);
        appoinmentCancelledRequest.setMissed_at(currentDateandTime);
        appoinmentCancelledRequest.setDoc_feedback("");
        appoinmentCancelledRequest.setAppoint_patient_st("Patient Appointment Cancelled");
        appoinmentCancelledRequest.setAppoinment_status("Missed");
        Log.w(TAG,"appoinmentCancelledRequest"+ "--->" + new Gson().toJson(appoinmentCancelledRequest));
        return appoinmentCancelledRequest;
    }

    @SuppressLint("LogNotTimber")
    private void notificationSendResponseCall(String userid, String doctorid, String appointmentid, String type) {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<NotificationSendResponse> call = ApiService.notificationSendResponseCall(RestUtils.getContentType(),notificationSendRequest(userid,doctorid,appointmentid));

        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<NotificationSendResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<NotificationSendResponse> call, @NonNull Response<NotificationSendResponse> response) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"notificationSendResponseCall"+ "--->" + new Gson().toJson(response.body()));


                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        if(Paymentmethod != null && Paymentmethod.equalsIgnoreCase("Online")){
                            if(ServiceCost != null && !ServiceCost.equalsIgnoreCase("0")) {
                                showSuccessfullyCancelled(type);
                            }else{
                                startActivity(new Intent(mContext, PetMyappointmentsActivity.class));
                            }
                        }else{
                            startActivity(new Intent(mContext, PetMyappointmentsActivity.class));
                        }



                    }

                }


            }

            @Override
            public void onFailure(@NonNull Call<NotificationSendResponse> call, @NonNull Throwable t) {
                avi_indicator.smoothToHide();

                Log.w(TAG,"NotificationSendResponse flr"+"--->" + t.getMessage());
            }
        });

    }
    @SuppressLint("LogNotTimber")
    private NotificationSendRequest notificationSendRequest(String userid, String doctorid, String appointmentid) {

        /**
         * status : Payment Failed
         * date : 23-10-2020 11:00 AM
         * appointment_UID :
         * user_id : 601b8ac3204c595ee52582f2
         * doctor_id :
         */
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
        String currentDateandTime = simpleDateFormat.format(new Date());



        NotificationSendRequest notificationSendRequest = new NotificationSendRequest();
        notificationSendRequest.setStatus("Patient Appointment Cancelled");
        notificationSendRequest.setDate(currentDateandTime);
        notificationSendRequest.setAppointment_UID(appointmentid);
        notificationSendRequest.setUser_id(userid);
        notificationSendRequest.setDoctor_id(doctorid);


        Log.w(TAG,"notificationSendRequest"+ "--->" + new Gson().toJson(notificationSendRequest));
        return notificationSendRequest;
    }

    private void spnotificationSendResponseCall(String userid, String spid, String appointmentid) {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<NotificationSendResponse> call = ApiService.spnotificationSendResponseCall(RestUtils.getContentType(),spNotificationSendRequest(userid,spid,appointmentid));

        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<NotificationSendResponse>() {
            @Override
            public void onResponse(@NonNull Call<NotificationSendResponse> call, @NonNull Response<NotificationSendResponse> response) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"notificationSendResponseCall"+ "--->" + new Gson().toJson(response.body()));


                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        if(Paymentmethod != null && Paymentmethod.equalsIgnoreCase("Online")){
                            if(ServiceCost != null && !ServiceCost.equalsIgnoreCase("0")) {
                                showSuccessfullyCancelled(type);
                            }else{
                                startActivity(new Intent(mContext, PetMyappointmentsActivity.class));

                            }
                        }else{
                            startActivity(new Intent(mContext, PetMyappointmentsActivity.class));
                        }


                    }

                }


            }

            @Override
            public void onFailure(@NonNull Call<NotificationSendResponse> call, @NonNull Throwable t) {
                avi_indicator.smoothToHide();

                Log.w(TAG,"NotificationSendResponse flr"+"--->" + t.getMessage());
            }
        });

    }
    private SPNotificationSendRequest spNotificationSendRequest(String userid, String spid, String appointmentid) {

        /*
         * status : Payment Failed
         * date : 23-10-2020 11:00 AM
         * appointment_UID : PET-2923029239123
         * user_id : 601b8ac3204c595ee52582f2
         * sp_id : 601ba9c6270cbe79fd900183
         */
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
        String currentDateandTime = simpleDateFormat.format(new Date());



        SPNotificationSendRequest spNotificationSendRequest = new SPNotificationSendRequest();
        spNotificationSendRequest.setStatus("Patient Appointment Cancelled");
        spNotificationSendRequest.setDate(currentDateandTime);
        spNotificationSendRequest.setAppointment_UID(appointmentid);
        spNotificationSendRequest.setUser_id(userid);
        spNotificationSendRequest.setSp_id(spid);


        Log.w(TAG,"spNotificationSendRequest"+ "--->" + new Gson().toJson(spNotificationSendRequest));
        return spNotificationSendRequest;
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
        rv_successfully_cancelled.setLayoutManager(new LinearLayoutManager(mContext));
        rv_successfully_cancelled.setItemAnimator(new DefaultItemAnimator());
        MyCouponsTextAdapter myCouponsTextAdapter = new MyCouponsTextAdapter(mContext, myCouponsTextList,ServiceCost,this);
        rv_successfully_cancelled.setAdapter(myCouponsTextAdapter);

    }

    @Override
    public void onAppointmentSuccessfullyCancel(String refund, String cost) {
        Log.w(TAG,"onAppointmentSuccessfullyCancel : "+"refund : "+refund+"cost : "+cost);

        if(cost != null && cost.equalsIgnoreCase("0")){
            startActivity(new Intent(mContext, PetMyappointmentsActivity.class));
        }else{
            if(refund != null && !refund.isEmpty()){
                RefundCouponCreateRequestCall(refund,cost);
            }else{
                RefundCouponBankCreateRequestCall(refund,cost);

            }

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
        refundCouponCreateRequest.setCoupon_type(Appointmenttype);
        refundCouponCreateRequest.setCode("REF"+currentDateandTime);
        if(cost != null && !cost.isEmpty()){
            refundCouponCreateRequest.setAmount(Integer.parseInt(cost));
        }else{
            refundCouponCreateRequest.setAmount(0);
        }

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
        refundCouponCreateRequest.setCoupon_type(Appointmenttype);
        refundCouponCreateRequest.setCode("Bank");
        refundCouponCreateRequest.setAmount(0);
        refundCouponCreateRequest.setUser_details(Appointmetnt_id);
        refundCouponCreateRequest.setUsed_status("");
        refundCouponCreateRequest.setMobile_type("Android");


        Log.w(TAG,"refundCouponCreateRequest"+ "--->" + new Gson().toJson(refundCouponCreateRequest));
        return refundCouponCreateRequest;
    }
    private void showRefundSuccessfully(String Message) {

        try {

            dialog = new Dialog(mContext);
            dialog.setContentView(R.layout.alert_approve_reject_layout);
            TextView tvheader = dialog.findViewById(R.id.tvInternetNotConnected);
            tvheader.setText(Message);
            Button dialogButtonApprove = dialog.findViewById(R.id.btnApprove);
            dialogButtonApprove.setText("Ok");
            Button dialogButtonRejected = dialog.findViewById(R.id.btnReject);
            dialogButtonRejected.setText("No");
            dialogButtonRejected.setVisibility(View.GONE);

            dialogButtonApprove.setOnClickListener(view -> {
                startActivity(new Intent(mContext, MyCouponsActivity.class));
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



    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private void spAppointmentDetailsResponse(String id, String appointmenttype, String userid, String doctorid, String appointmentid, String spid, String s) {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<SPAppointmentDetailsResponse> call = ApiService.spAppointmentDetailsResponse(RestUtils.getContentType(), appointmentDetailsRequest());
        Log.w(TAG, "url  :%s" + call.request().url().toString());

        call.enqueue(new Callback<SPAppointmentDetailsResponse>() {
            @SuppressLint({"LongLogTag", "LogNotTimber"})
            @Override
            public void onResponse(@NonNull Call<SPAppointmentDetailsResponse> call, @NonNull Response<SPAppointmentDetailsResponse> response) {
                avi_indicator.smoothToHide();
                Log.w(TAG, "SPAppointmentDetailsResponse" + "--->" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    if (200 == response.body().getCode()) {
                        spappoinmentCancelledResponseCall(id,appointmenttype,userid,doctorid,appointmentid,spid,"2");


                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<SPAppointmentDetailsResponse> call, @NonNull Throwable t) {
                avi_indicator.smoothToHide();

                Log.w(TAG, "PetNewAppointmentDetailsResponse" + "--->" + t.getMessage());
            }
        });

    }
    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private AppointmentDetailsRequest appointmentDetailsRequest() {

        AppointmentDetailsRequest appointmentDetailsRequest = new AppointmentDetailsRequest();
        appointmentDetailsRequest.setApppointment_id(appointment_id);
        Log.w(TAG, "appointmentDetailsRequest" + "--->" + new Gson().toJson(appointmentDetailsRequest));
        return appointmentDetailsRequest;
    }


    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private void petAppointmentResponseCall(String id, String appointmenttype, String userid, String doctorid, String appointmentid, String s) {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<PetNewAppointmentDetailsResponse> call = ApiService.petNewAppointDetailResponseCall(RestUtils.getContentType(), petNewAppointmentDetailsRequest());
        Log.w(TAG, "url  :%s" + call.request().url().toString());
        call.enqueue(new Callback<PetNewAppointmentDetailsResponse>() {
            @SuppressLint({"LongLogTag", "LogNotTimber", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<PetNewAppointmentDetailsResponse> call, @NonNull Response<PetNewAppointmentDetailsResponse> response) {
                avi_indicator.smoothToHide();
                Log.w(TAG, "petAppointmentResponseCall" + "--->" + new Gson().toJson(response.body()));


                if (response.body() != null) {

                    if (200 == response.body().getCode()) {
                        appoinmentCancelledResponseCall(id,appointmenttype,userid,doctorid,appointmentid,"1");

                    }


                }
            }

            @Override
            public void onFailure(@NonNull Call<PetNewAppointmentDetailsResponse> call, @NonNull Throwable t) {
                avi_indicator.smoothToHide();

                Log.w(TAG, "petAppointmentResponseCall flr" + "--->" + t.getMessage());
            }
        });

    }
    @SuppressLint({"LongLogTag", "LogNotTimber"})
    private PetNewAppointmentDetailsRequest petNewAppointmentDetailsRequest() {
        PetNewAppointmentDetailsRequest petNewAppointmentDetailsRequest = new PetNewAppointmentDetailsRequest();
        petNewAppointmentDetailsRequest.setApppointment_id(appointment_id);
        Log.w(TAG, "petNewAppointmentDetailsRequest" + "--->" + new Gson().toJson(petNewAppointmentDetailsRequest));
        return petNewAppointmentDetailsRequest;
    }






}