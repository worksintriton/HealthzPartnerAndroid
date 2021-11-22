package com.triton.healthZpartners.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.triton.healthZpartners.R;
import com.triton.healthZpartners.api.APIClient;
import com.triton.healthZpartners.api.RestApiInterface;
import com.triton.healthZpartners.appUtils.NumericKeyBoardTransformationMethod;
import com.triton.healthZpartners.requestpojo.LoginRequest;
import com.triton.healthZpartners.responsepojo.LoginResponse;
import com.triton.healthZpartners.utils.ConnectionDetector;
import com.triton.healthZpartners.utils.RestUtils;
import com.wang.avi.AVLoadingIndicatorView;
import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "LoginActivity";


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.avi_indicator)
    AVLoadingIndicatorView avi_indicator;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_phoneno)
    EditText edt_emailorphone;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_signup)
    TextView txt_signup;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_next)
    Button btn_next;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.spr_phnnum)
    Spinner spr_phnnum;

    Dialog alertDialog;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    String[] permissionString = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.RECEIVE_SMS,
            "check"};


    @SuppressLint("LogNotTimber")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        Log.w(TAG,"onCreate-->");

        edt_emailorphone.setTransformationMethod(new NumericKeyBoardTransformationMethod());

        avi_indicator.setVisibility(View.GONE);
        txt_signup.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String phonenumber = extras.getString("phonemumber");
            if(phonenumber != null){
                edt_emailorphone.setText(phonenumber);
            }
        }

        spr_phnnum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE); /* if you want your item to be white */
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
          case R.id.txt_signup:
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
                break;
            case R.id.btn_next:
                verifyValidator();
                break;
        }

    }

    public void verifyValidator() {
        boolean can_proceed = true;
        if (edt_emailorphone.getText().toString().trim().equals("")) {
            edt_emailorphone.setError("Please enter your phone number");
            edt_emailorphone.requestFocus();
            can_proceed = false;
        }




        if (can_proceed) {

            insertmappermission();

        }

    }


    @SuppressLint("LogNotTimber")
    private void loginResponseCall() {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<LoginResponse> call = apiInterface.loginResponseCall(RestUtils.getContentType(), loginRequest());
        Log.w(TAG,"ResendOTPResponse url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<LoginResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"ResendOTPResponse" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    if (200 == response.body().getCode()) {

                        Toasty.success(getApplicationContext(),response.body().getMessage(), Toast.LENGTH_SHORT, true).show();

                        if(response.body().getData().getUser_details() != null) {
                            Intent intent = new Intent(LoginActivity.this, VerifyOtpActivity.class);
                            intent.putExtra("phonemumber", response.body().getData().getUser_details().getUser_phone());
                            intent.putExtra("otp", response.body().getData().getUser_details().getOtp());
                            intent.putExtra("userstatus", response.body().getData().getUser_details().getUser_status());
                            intent.putExtra("usertype", response.body().getData().getUser_details().getUser_type());
                            intent.putExtra("userid", response.body().getData().getUser_details().get_id());

                            intent.putExtra("firstname", response.body().getData().getUser_details().getFirst_name());
                            intent.putExtra("lastname", response.body().getData().getUser_details().getLast_name());
                            intent.putExtra("useremail", response.body().getData().getUser_details().getUser_email());
                            intent.putExtra("fromactivity", TAG);
                            startActivity(intent);
                        }

                    }
                    else {
                        showErrorLoading(response.body().getMessage());
                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call,@NonNull Throwable t) {
                avi_indicator.smoothToHide();
                Log.e("LoginResponse flr", "--->" + t.getMessage());
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    @SuppressLint("LogNotTimber")
    private LoginRequest loginRequest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUser_phone(edt_emailorphone.getText().toString());
        Log.w(TAG,"loginRequest"+ new Gson().toJson(loginRequest));
        return loginRequest;
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

    private void insertmappermission() {

        int haslocationpermission;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            haslocationpermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) | checkSelfPermission(Manifest.permission.RECEIVE_SMS);

            if (haslocationpermission != PackageManager.PERMISSION_GRANTED) {

                if (!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) | !shouldShowRequestPermissionRationale(Manifest.permission.RECEIVE_SMS)) {

                    requestPermissions(permissionString,
                            REQUEST_CODE_ASK_PERMISSIONS);

                    return;
                }
                requestPermissions(permissionString,
                        REQUEST_CODE_ASK_PERMISSIONS);
            }else{
                if (new ConnectionDetector(LoginActivity.this).isNetworkAvailable(LoginActivity.this)) {

                    loginResponseCall();
                }

            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ASK_PERMISSIONS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               /* startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();*/
                // Permission Granted
                 if (new ConnectionDetector(LoginActivity.this).isNetworkAvailable(LoginActivity.this)) {

                    loginResponseCall();
                }




            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

    }
}