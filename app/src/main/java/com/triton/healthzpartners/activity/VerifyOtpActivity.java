package com.triton.healthzpartners.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.triton.healthzpartners.R;
import com.triton.healthzpartners.api.APIClient;
import com.triton.healthzpartners.api.RestApiInterface;
import com.triton.healthzpartners.appUtils.ApplicationData;
import com.triton.healthzpartners.customer.AddMembersNewActivity;
import com.triton.healthzpartners.customer.CustomerDashboardActivity;
import com.triton.healthzpartners.doctor.DoctorBusinessInfoActivity;
import com.triton.healthzpartners.doctor.DoctorDashboardActivity;
import com.triton.healthzpartners.receiver.SmsBroadcastListener;
import com.triton.healthzpartners.requestpojo.FBTokenUpdateRequest;
import com.triton.healthzpartners.requestpojo.ResendOTPRequest;
import com.triton.healthzpartners.responsepojo.FBTokenUpdateResponse;
import com.triton.healthzpartners.responsepojo.ResendOTPResponse;
import com.triton.healthzpartners.serviceprovider.ServiceProviderDashboardActivity;
import com.triton.healthzpartners.serviceprovider.ServiceProviderRegisterFormActivity;
import com.triton.healthzpartners.sessionmanager.SessionManager;
import com.triton.healthzpartners.utils.ConnectionDetector;
import com.triton.healthzpartners.utils.RestUtils;
import com.triton.healthzpartners.vendor.VendorDashboardActivity;
import com.triton.healthzpartners.vendor.VendorRegisterFormActivity;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyOtpActivity extends AppCompatActivity implements View.OnClickListener {

   private final String TAG = "VerifyOtpActivity";

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.avi_indicator)
    AVLoadingIndicatorView avi_indicator;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_verifyotp)
    Button btn_verifyotp;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.otp_view)
    OtpTextView otp_view;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_resend)
    TextView txt_resend;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_lbl_verifyphnno)
    TextView txt_lbl_verifyphnno;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_timer_count)
    TextView txt_timer_count;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llresendotp)
    LinearLayout llresendotp;

    private ApplicationData applicationData;
    private String phonenumber;
    private int otp;
    Dialog alertDialog;
    private String autoOTP;

    private String userstatus;
    private String fromactivity;
    private int usertype = 0;

    private boolean isOTPExpired ;
    private String userid;
    private String token = "";
    private String firstname,lastname,useremail;
    private String verifyemailstatus = "false";
    private String myrefcode = "";

    String enteredotp , responseotp;
    boolean can_proceed = true;

    private CountDownTimer timer;


    @SuppressLint("LogNotTimber")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        ButterKnife.bind(this);
        applicationData = (ApplicationData) getApplication();

        Log.w(TAG,"onCreate-->");


        avi_indicator.setVisibility(View.GONE);

        btn_verifyotp.setOnClickListener(this);

        txt_lbl_verifyphnno.setText("Phone Verification");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            phonenumber = extras.getString("phonemumber");
            otp = extras.getInt("otp");
            usertype = extras.getInt("usertype");
            userid = extras.getString("userid");
            userstatus = extras.getString("userstatus");
            firstname = extras.getString("firstname");
            lastname = extras.getString("lastname");
            useremail = extras.getString("useremail");
            fromactivity = extras.getString("fromactivity");
            Log.w(TAG,"Bundle "+" phonenumber : "+phonenumber+" otp :"+otp+" usertype : "+usertype+" userstatus : "+userstatus+ " userid : "+userid);
        }


        try{
            // Initialize Firebase
            FirebaseApp.initializeApp(getApplicationContext());
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
            FirebaseMessaging.getInstance().setAutoInitEnabled(true);
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();
                        Log.w(TAG,"token--->"+ token);



                    });



        }
        catch (Exception e){
            Log.w(TAG,"FCM : "+e.getLocalizedMessage());
            Log.w(TAG,"FCM Message : "+e.getMessage());
            e.printStackTrace();
        }



        btn_verifyotp.setOnClickListener(this);
        txt_resend.setOnClickListener(this);


        SmsBroadcastListener.bindListener(otpText -> {
            avi_indicator.smoothToHide();
            Log.w(TAG,"extractedOTP--->"+otpText);
            autoOTP = otpText;

            if(autoOTP != null) {
                otp_view.setOTP(autoOTP);
            }


        });







      /*
        otp_view.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {
                // fired when user types something in the Otpbox
            }

            @Override
            public void onOTPComplete(String otp) {
                // fired when user has entered the OTP fully.


            }


        });
*/

        startTimer();

    }


    private void startTimer() {
        isOTPExpired = false;
        long timer_milliseconds = 120000;
        timer = new CountDownTimer(timer_milliseconds, 1000) {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void onTick(long millisUntilFinished) {
                llresendotp.setVisibility(View.GONE);
                txt_timer_count.setVisibility(View.VISIBLE);

                applicationData.setTimer_milliseconds(millisUntilFinished);
                txt_timer_count.setText(getResources().getString(R.string.resendotp)+" " + String.format("%02d : %02d ",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

            }

            @Override
            public void onFinish() {
                isOTPExpired = true;
                txt_timer_count.setVisibility(View.GONE);
                llresendotp.setVisibility(View.VISIBLE);
                timer.cancel();
            }
        };
        timer.start();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_verifyotp:
                verifyValidator();
                break;

            case R.id.txt_resend:
                if (new ConnectionDetector(VerifyOtpActivity.this).isNetworkAvailable(VerifyOtpActivity.this)) {
                    resendOtpResponseCall();
                }
                break;


        }


    }

    public void verifyValidator() {

        enteredotp = otp_view.getOTP();

        Log.w(TAG, "enteredotp : "+enteredotp);

        responseotp = String.valueOf(otp);

        Log.w(TAG, "responseotp : "+responseotp);

        if (enteredotp.trim().equals("")) {

            can_proceed = false;

            Toasty.warning(getApplicationContext(), "Please Enter OTP", Toast.LENGTH_SHORT, true).show();

        } else if (!responseotp.equalsIgnoreCase(enteredotp)) {

            can_proceed = false;

            Toasty.warning(getApplicationContext(), "Incorrect OTP", Toast.LENGTH_SHORT, true).show();
        }else if(enteredotp.equalsIgnoreCase(responseotp)){
            can_proceed = true;
        }

        if (can_proceed) {

            //Toasty.success(getApplicationContext(),"userid : "+userid+ "fbtoken : "+token, Toast.LENGTH_SHORT, true).show();

            if (new ConnectionDetector(VerifyOtpActivity.this).isNetworkAvailable(VerifyOtpActivity.this)) {
                if (userid != null) {
                    fBTokenUpdateResponseCall();
                }
            }

        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(fromactivity!=null){

            if(fromactivity.equals("LoginActivity")){

                startActivity(new Intent(VerifyOtpActivity.this, LoginActivity.class));
            }

      /*      else if(fromactivity.equals("SignUpActivity")){

                startActivity(new Intent(VerifyOtpActivity.this, SignUpActivity.class));
            }*/
        }


    }

    private void resendOtpResponseCall() {
        llresendotp.setVisibility(View.GONE);
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<ResendOTPResponse> call = apiInterface.resendOTPResponsecall(RestUtils.getContentType(), resendOTPRequest());
        Log.w(TAG,"ResendOTPResponse url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<ResendOTPResponse>() {
            @Override
            public void onResponse(@NonNull Call<ResendOTPResponse> call, @NonNull Response<ResendOTPResponse> response) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"ResendOTPResponse" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    if (200 == response.body().getCode()) {
                        otp_view.setOTP("");
                        startTimer();
                        Toasty.success(getApplicationContext(),response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                        if(response.body().getData().getUser_Details() != null) {
                            otp = response.body().getData().getUser_Details().getOtp();
                        }

                    } else {
                        showErrorLoading(response.body().getMessage());
                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<ResendOTPResponse> call,@NonNull Throwable t) {
                avi_indicator.smoothToHide();
                Log.e("ResendOTPResponse flr", "--->" + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    private ResendOTPRequest resendOTPRequest() {
        ResendOTPRequest resendOTPRequest = new ResendOTPRequest();
        resendOTPRequest.setUser_phone(phonenumber);
        Log.w(TAG,"OTP resendOTPRequest"+ new Gson().toJson(resendOTPRequest));
        return resendOTPRequest;
    }
    public void showErrorLoading(String errormesage){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
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

    @Override
    protected void onPause() {
        super.onPause();

    }

    @SuppressLint("LogNotTimber")
    private void fBTokenUpdateResponseCall() {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<FBTokenUpdateResponse> call = apiInterface.fBTokenUpdateResponseCall(RestUtils.getContentType(), fbTokenUpdateRequest());
        Log.w(TAG,"fBTokenUpdateResponseCall url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<FBTokenUpdateResponse>() {
            @Override
            public void onResponse(@NonNull Call<FBTokenUpdateResponse> call, @NonNull Response<FBTokenUpdateResponse> response) {

                Log.w(TAG,"fBTokenUpdateResponseCall"+ "--->" + new Gson().toJson(response.body()));


                avi_indicator.smoothToHide();

                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        if(response.body().getData() != null) {
                            if (response.body().getData().isUser_email_verification()) {
                                verifyemailstatus = "true";
                            }
                            if(response.body().getData().getMy_ref_code() != null){
                                Log.w(TAG,"MyRefCode : "+ response.body().getData().getMy_ref_code());
                                myrefcode = response.body().getData().getMy_ref_code();
                            }else{
                                myrefcode = "";
                            }
                        }




                        SessionManager sessionManager = new SessionManager(VerifyOtpActivity.this);
                        sessionManager.setIsLogin(true);
                        sessionManager.createLoginSession(
                                userid,
                                firstname,
                                lastname,
                                useremail,
                                phonenumber,
                                String.valueOf(usertype),
                                userstatus,
                                response.body().getData().getProfile_img(),
                                verifyemailstatus,
                                myrefcode

                        );
                        sessionManager.createRazorpayDetails(
                                response.body().getPayment_gateway_detail().getRzpkey(),
                               String.valueOf(response.body().getPayment_gateway_detail().isIsproduction()));
                        Log.w(TAG,"ref_code : "+response.body().getData().getRef_code()+" fromactivity : "+fromactivity+" usertype : "+usertype);


                        if(fromactivity != null && fromactivity.equalsIgnoreCase("LoginActivity")){
                            if(usertype != 0){
                                if(usertype == 1){
                                    startActivity(new Intent(VerifyOtpActivity.this, CustomerDashboardActivity.class));

                                }else if(usertype == 2 ){
                                    startActivity(new Intent(VerifyOtpActivity.this, ServiceProviderDashboardActivity.class));

                                }else if(usertype == 3 ){
                                    startActivity(new Intent(VerifyOtpActivity.this, VendorDashboardActivity.class));

                                }else if(usertype == 4 ){
                                    startActivity(new Intent(VerifyOtpActivity.this, DoctorDashboardActivity.class));

                                }
                            }
                        } else{
                            if(usertype != 0){
                                if(usertype == 1 ){
                                    startActivity(new Intent(VerifyOtpActivity.this, AddMembersNewActivity.class));

                                }else if(usertype == 2 ){
                                    startActivity(new Intent(VerifyOtpActivity.this, ServiceProviderRegisterFormActivity.class));

                                }else if(usertype == 3 ){
                                    startActivity(new Intent(VerifyOtpActivity.this, VendorRegisterFormActivity.class));

                                }else if(usertype == 4 ){
                                    startActivity(new Intent(VerifyOtpActivity.this, DoctorBusinessInfoActivity.class));

                                }
                            }
                        }
                    }
                    else{

                        showErrorLoading(response.body().getMessage());
                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<FBTokenUpdateResponse> call, @NonNull Throwable t) {

                avi_indicator.smoothToHide();
                Log.w(TAG,"FBTokenUpdateResponse flr"+"--->" + t.getMessage());
                //Toasty.success(getApplicationContext(),"NotificationUpdateResponse flr : "+t.getMessage(), Toast.LENGTH_SHORT, true).show();

            }
        });

    }
    private FBTokenUpdateRequest fbTokenUpdateRequest() {
        FBTokenUpdateRequest fbTokenUpdateRequest = new FBTokenUpdateRequest();
        fbTokenUpdateRequest.setUser_id(userid);
        fbTokenUpdateRequest.setFb_token(token);
        Log.w(TAG,"fbTokenUpdateRequest"+ "--->" + new Gson().toJson(fbTokenUpdateRequest));
        //  Toasty.success(getApplicationContext(),"fbTokenUpdateRequest : "+new Gson().toJson(fbTokenUpdateRequest), Toast.LENGTH_SHORT, true).show();

        return fbTokenUpdateRequest;
    }


}