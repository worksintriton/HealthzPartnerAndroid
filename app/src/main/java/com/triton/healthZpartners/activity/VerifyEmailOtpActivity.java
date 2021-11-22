package com.triton.healthZpartners.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.triton.healthZpartners.R;
import com.triton.healthZpartners.api.APIClient;
import com.triton.healthZpartners.api.RestApiInterface;
import com.triton.healthZpartners.appUtils.ApplicationData;
import com.triton.healthZpartners.requestpojo.EmailOTPRequest;
import com.triton.healthZpartners.responsepojo.EmailOTPResponse;
import com.triton.healthZpartners.utils.ConnectionDetector;
import com.triton.healthZpartners.utils.RestUtils;
import com.wang.avi.AVLoadingIndicatorView;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import in.aabhasjindal.otptextview.OtpTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyEmailOtpActivity extends AppCompatActivity implements View.OnClickListener {


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



    private final String TAG = "VerifyEmailOtpActivity";
    private CountDownTimer timer;

    private ApplicationData applicationData;
    private String phonenumber;
    private int otp;
    Dialog alertDialog;
    private String autoOTP;

    private String userstatus;
    private String fromactivity;


    private boolean isOTPExpired ;
    private String userid;
    private String token = "";
    private String firstname,lastname,useremail;
    private String UserType;
    private int UserTypeValue;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        applicationData = (ApplicationData) getApplication();

        ButterKnife.bind(this);

        avi_indicator.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            otp = extras.getInt("otp");
            UserType = extras.getString("UserType");
            UserTypeValue = extras.getInt("UserTypeValue");

            userid = extras.getString("userid");
            userstatus = extras.getString("userstatus");
            firstname = extras.getString("firstname");
            lastname = extras.getString("lastname");
            useremail = extras.getString("useremail");
            fromactivity = extras.getString("fromactivity");
            Log.w(TAG,"Bundle "+" phonenumber : "+phonenumber+" otp :"+otp+" UserType : "+UserType+" userstatus : "+userstatus+ " userid : "+userid);
        }


        btn_verifyotp.setOnClickListener(this);
        txt_resend.setOnClickListener(this);


    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_verifyotp:
                verifyValidator();
                break;

                case R.id.txt_resend:
                    if (new ConnectionDetector(VerifyEmailOtpActivity.this).isNetworkAvailable(VerifyEmailOtpActivity.this)) {
                        if(useremail != null){
                            resendOtpResponseCall();
                        }

                    }
                break;


        }


    }
    public void verifyValidator() {
        boolean can_proceed = true;
        String enteredotp = otp_view.getOTP();
        String responseotp = String.valueOf(otp);
        if (enteredotp.trim().equals("")) {

            can_proceed = false;

            Toasty.warning(getApplicationContext(), "Please Enter OTP", Toast.LENGTH_SHORT, true).show();

        } else if (!responseotp.equalsIgnoreCase(enteredotp)) {

            can_proceed = false;

            Toasty.warning(getApplicationContext(), "Incorrect OTP", Toast.LENGTH_SHORT, true).show();
        }


        if (can_proceed) {
             Intent intent = new Intent(VerifyEmailOtpActivity.this,SignUpActivity.class);
             intent.putExtra("verified","verified");
             intent.putExtra("useremail",useremail);
             intent.putExtra("firstname",firstname);
             intent.putExtra("lastname",lastname);
             intent.putExtra("UserType",UserType);
             intent.putExtra("UserTypeValue",UserTypeValue);
             startActivity(intent);





        }

    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();

    }

    @SuppressLint("LogNotTimber")
    private void resendOtpResponseCall() {
        txt_resend.setVisibility(View.GONE);
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<EmailOTPResponse> call = apiInterface.emailOTPResponseCall(RestUtils.getContentType(), emailOTPRequest());
        Log.w(TAG,"ResendOTPResponse url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<EmailOTPResponse>() {
            @Override
            public void onResponse(@NonNull Call<EmailOTPResponse> call, @NonNull Response<EmailOTPResponse> response) {
                  avi_indicator.smoothToHide();
                Log.w(TAG,"ResendOTPResponse" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    if (200 == response.body().getCode()) {
                        otp_view.setOTP("");
                        Toasty.success(getApplicationContext(),response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                        if(response.body().getData().getOtp() !=0){
                            otp = response.body().getData().getOtp();
                        }


                    } else {
                        showErrorLoading(response.body().getMessage());
                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<EmailOTPResponse> call,@NonNull Throwable t) {
                avi_indicator.smoothToHide();
                Log.e("ResendOTPResponse flr", "--->" + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    @SuppressLint("LogNotTimber")
    private EmailOTPRequest emailOTPRequest() {

        /* * user_email : mohammedimthi2395@gmail.com*/

        EmailOTPRequest emailOTPRequest = new EmailOTPRequest();
        emailOTPRequest.setUser_email(useremail);
        Log.w(TAG,"EmailOTPRequest "+ new Gson().toJson(emailOTPRequest));
        return emailOTPRequest;
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


}