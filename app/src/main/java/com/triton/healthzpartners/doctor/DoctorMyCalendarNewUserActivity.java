package com.triton.healthzpartners.doctor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.triton.healthzpartners.R;
import com.triton.healthzpartners.adapter.DoctorMyCalendarAvailableNewUserAdapter;
import com.triton.healthzpartners.api.APIClient;
import com.triton.healthzpartners.api.RestApiInterface;
import com.triton.healthzpartners.interfaces.OnItemClickSpecialization;
import com.triton.healthzpartners.requestpojo.DoctorMyCalendarAvlDaysRequest;
import com.triton.healthzpartners.responsepojo.DoctorMyCalendarAvlDaysResponse;
import com.triton.healthzpartners.sessionmanager.SessionManager;
import com.triton.healthzpartners.utils.ConnectionDetector;
import com.triton.healthzpartners.utils.RestUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorMyCalendarNewUserActivity extends AppCompatActivity implements OnItemClickSpecialization {

    private static final String TAG = "DoctorMyCalendarNewUserActivity" ;
    RecyclerView rv_doctor_mycalendar_avldays;
    private SharedPreferences preferences;

    private List<DoctorMyCalendarAvlDaysResponse.DataBean> dataBeanList = null;

    private ArrayList<String> dateList = new ArrayList<>();
    private SessionManager session;
    String doctorname = "",doctoremailid = "";
    Button btn_next;
    private String userid;
    AVLoadingIndicatorView avi_indicator;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.include_sp_header)
    View include_sp_header;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_my_calendar);
        ButterKnife.bind(this);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        rv_doctor_mycalendar_avldays = findViewById(R.id.rv_doctor_mycalendar_avldays);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getProfileDetails();
        doctorname = user.get(SessionManager.KEY_FIRST_NAME);
        doctoremailid = user.get(SessionManager.KEY_EMAIL_ID);
        userid = user.get(SessionManager.KEY_ID);

        avi_indicator = findViewById(R.id.avi_indicator);
        avi_indicator.setVisibility(View.GONE);


        if (new ConnectionDetector(DoctorMyCalendarNewUserActivity.this).isNetworkAvailable(DoctorMyCalendarNewUserActivity.this)) {
            doctorMyCalendarAvlDaysResponseCall();
        }
        btn_next = findViewById(R.id.btn_next);
        btn_next.setVisibility(View.GONE);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                Log.w(TAG,"dateList-->"+new Gson().toJson(dateList));
                if(dateList != null && dateList.size()>0){
                    Intent intent = new Intent(DoctorMyCalendarNewUserActivity.this,DoctorMyCalendarTimeActivity.class);
                    intent.putExtra("dateList",dateList);
                    startActivity(intent);
                }else{
                    Toasty.warning(getApplicationContext(), "Please select any one day", Toast.LENGTH_SHORT, true).show();

                }

            }
        });



        TextView txtAddHoliday = findViewById(R.id.txtAddHoliday);
        txtAddHoliday.setVisibility(View.GONE);
        ImageView img_back = include_sp_header.findViewById(R.id.img_back);
        ImageView img_notification = include_sp_header.findViewById(R.id.img_notification);
        ImageView img_cart = include_sp_header.findViewById(R.id.img_cart);
        ImageView img_profile = include_sp_header.findViewById(R.id.img_profile);
        TextView toolbar_title = include_sp_header.findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.my_calendar));
        img_cart.setVisibility(View.GONE);
        img_notification.setVisibility(View.GONE);
        img_profile.setVisibility(View.GONE);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txtAddHoliday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DoctorMyCalendarNewUserActivity.this,Doctor_Holiday_Activity.class));
            }
        });
        }

    @SuppressLint("LongLogTag")
    private void doctorMyCalendarAvlDaysResponseCall() {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<DoctorMyCalendarAvlDaysResponse> call = ApiService.doctorMyCalendarAvlDaysResponseCall(RestUtils.getContentType(),doctorMyCalendarAvlDaysRequest());
        Log.w(TAG,"url  :%s"+" "+call.request().url().toString());

        call.enqueue(new Callback<DoctorMyCalendarAvlDaysResponse>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(@NonNull Call<DoctorMyCalendarAvlDaysResponse> call, @NonNull Response<DoctorMyCalendarAvlDaysResponse> response) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"DoctorMyCalendarAvlDaysResponse"+ "--->" + new Gson().toJson(response.body()));


                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        if(response.body().getData() != null) {
                            dataBeanList = response.body().getData();
                        }

                        if(dataBeanList != null && dataBeanList.size()>0) {
                            for (int i = 0; i < dataBeanList.size(); i++) {
                                boolean isStatus = dataBeanList.get(i).isStatus();
                                if (isStatus) {
                                    btn_next.setVisibility(View.GONE);
                                } else {
                                    btn_next.setVisibility(View.VISIBLE);
                                    break;
                                }
                            }
                            setViewAvlDays();
                        }



                    }

                }


            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(@NonNull Call<DoctorMyCalendarAvlDaysResponse> call, @NonNull Throwable t) {
                avi_indicator.smoothToHide();

                Log.w(TAG,"DoctorMyCalendarAvlDaysResponse"+"--->" + t.getMessage());
            }
        });

    }
    @SuppressLint("LongLogTag")
    private DoctorMyCalendarAvlDaysRequest doctorMyCalendarAvlDaysRequest() {
        /*
         * Doctor_email_id : mohammedimthi23956@gmail.com
         * Doctor_name : mohammed6
         * types : 1
         */
        DoctorMyCalendarAvlDaysRequest doctorMyCalendarAvlDaysRequest = new DoctorMyCalendarAvlDaysRequest();
        doctorMyCalendarAvlDaysRequest.setUser_id(userid);
        doctorMyCalendarAvlDaysRequest.setDoctor_name(doctorname);
        doctorMyCalendarAvlDaysRequest.setTypes(1);
        Log.w(TAG,"doctorMyCalendarAvlDaysRequest"+ "--->" + new Gson().toJson(doctorMyCalendarAvlDaysRequest));
        return doctorMyCalendarAvlDaysRequest;
    }

    private void setViewAvlDays() {
        rv_doctor_mycalendar_avldays.setLayoutManager(new LinearLayoutManager(this));
        rv_doctor_mycalendar_avldays.setItemAnimator(new DefaultItemAnimator());
        DoctorMyCalendarAvailableNewUserAdapter doctorMyCalendarAvailableAdapter = new DoctorMyCalendarAvailableNewUserAdapter(getApplicationContext(), dataBeanList, rv_doctor_mycalendar_avldays, this);
        rv_doctor_mycalendar_avldays.setAdapter(doctorMyCalendarAvailableAdapter);

   }

    @SuppressLint("LongLogTag")
    @Override
    public void onItemCheckSpecialization(String item) {
        dateList.add(item);
        Log.w(TAG,"onItemCheckSpecialization : "+item);
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onItemUncheckSpecialization(String item) {
        Log.w(TAG,"onItemUncheckSpecialization : "+item);
        dateList.remove(item);


    }

    @Override
    public void onBackPressed() {
        //Toasty.warning(getApplicationContext(), "This action is disabled in this screen..", Toast.LENGTH_SHORT, true).show();

       /* super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), DoctorDashboardActivity.class));
        finish();*/
    }
}