package com.triton.healthzpartners.customer;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.triton.healthzpartners.R;
import com.triton.healthzpartners.activity.NotificationActivity;
import com.triton.healthzpartners.adapter.PetMyCalendarSlotNotAvailableAdapter;
import com.triton.healthzpartners.api.APIClient;
import com.triton.healthzpartners.api.RestApiInterface;
import com.triton.healthzpartners.interfaces.OnItemSelectedTime;
import com.triton.healthzpartners.requestpojo.AppointmentCheckRequest;
import com.triton.healthzpartners.requestpojo.PetAppointmentCreateRequest;
import com.triton.healthzpartners.requestpojo.PetDoctorAvailableTimeRequest;
import com.triton.healthzpartners.requestpojo.RescheduleAppointmentRequest;
import com.triton.healthzpartners.responsepojo.AppointmentCheckResponse;
import com.triton.healthzpartners.responsepojo.PetAppointmentCreateResponse;
import com.triton.healthzpartners.responsepojo.PetDoctorAvailableTimeResponse;
import com.triton.healthzpartners.responsepojo.SuccessResponse;
import com.triton.healthzpartners.sessionmanager.SessionManager;
import com.triton.healthzpartners.utils.ConnectionDetector;
import com.triton.healthzpartners.utils.RestUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PetAppointment_SlotNotAvailable_Doctor_Date_Time_Activity extends AppCompatActivity implements OnItemSelectedTime {

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_bookappointment)
    Button btn_bookappointment;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.chat)
    CheckBox chat;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.video)
    CheckBox video;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_doctoravailabeslottime)
    RecyclerView rv_doctoravailabeslottime;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.sub_layer1)
    RelativeLayout sub_layer1;


    String TAG = "PetAppointment_SlotNotAvailable_Doctor_Date_Time_Activity";

   // CalendarView calendar;

    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;

    private String Doctor_ava_Date = "";
    private String selectedTimeSlot = "";

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.view)
    View view;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvlblavailabletime)
    TextView tvlblavailabletime;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tvlbldoctoravailable)
    TextView tvlbldoctoravailable;





    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.avi_indicator)
    AVLoadingIndicatorView avi_indicator;

//    @SuppressLint("NonConstantResourceId")
//    @BindView(R.id.datePickerTimeline)
//    DatePickerTimeline datePickerTimeline ;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.include_petlover_header)
    View include_petlover_header;

    private List<PetDoctorAvailableTimeResponse.DataBean> doctorDateAvailabilityResponseList;
    private List<PetDoctorAvailableTimeResponse.DataBean.TimesBean> timesBeanList;
    private String doctorid;
    private String fromactivity;
    private String fromto;
    private int amount;
    private String communicationtype;
    private String bookingdateandtime;
    private Dialog dialog;
    private String id;
    private String distance;
    private String currentDateandTime;
    private String currenttime;
    private String currentdate;
    private String doctorname;
    private String clinicname;

    ArrayList<PetAppointmentCreateRequest> PetAppointmentCreateRequestList = new ArrayList<>();
    private String petname;
    private String selectedVisitType;
    private String petId;
    private String health_issue_title;
    private String selectedCommunicationtype;

    private String Doctor_id;
    private String Booking_date;
    private List<PetAppointmentCreateRequest.DocAttchedBean> Doc_attched;
    private String Booking_time;
    private String Booking_date_time;
    private String Communication_type;
    private String User_id;
    private String Pet_id;
    private String Problem_info;
    private String Display_date;
    private String Appointment_types;
    private String Allergies;
    private int Amount;
    private String Date_and_time;
    private String Visit_type;
    private String Location_id;
    private String Health_issue_title;
    List<PetAppointmentCreateRequest.PetImgBean> pet_imgList = new ArrayList();
    private String userid;
    private String Payment_id;
    private String selectedPaymentMethod;
    private String selectedAppointmentType;
    private int Total_price;
    private int Original_price;
    private int Discount_price;
    private String Coupon_status;
    private String Coupon_code;
    private String formattedDate;

    @SuppressLint("LogNotTimber")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petappointment_doctor_date_time);
        Log.w(TAG,"onCreateView");

        ButterKnife.bind(this);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            doctorname = extras.getString("doctorname");
            clinicname = extras.getString("clinicname");
            petname = extras.getString("petname");
            doctorid = extras.getString("doctorid");
            fromactivity = extras.getString("fromactivity");
            fromto = extras.getString("fromto");
            Doctor_ava_Date = extras.getString("Doctor_ava_Date");
            selectedTimeSlot = extras.getString("selectedTimeSlot");
            amount = extras.getInt("amount");
            communicationtype = extras.getString("communicationtype");
            selectedVisitType = extras.getString("selectedVisitType");
            petId = extras.getString("petId");
            health_issue_title = extras.getString("health_issue_title");
            selectedCommunicationtype = extras.getString("selectedCommunicationtype");
            PetAppointmentCreateRequestList = (ArrayList<PetAppointmentCreateRequest>) getIntent().getSerializableExtra("PetAppointmentCreateRequestList");

        }

        if(PetAppointmentCreateRequestList != null && PetAppointmentCreateRequestList.size()>0) {
            for (int i = 0; i < PetAppointmentCreateRequestList.size(); i++) {
                Doctor_id =  PetAppointmentCreateRequestList.get(i).getDoctor_id();
                Booking_date =  PetAppointmentCreateRequestList.get(i).getBooking_date();
                Booking_time =  PetAppointmentCreateRequestList.get(i).getBooking_time();
                Booking_date_time =  PetAppointmentCreateRequestList.get(i).getBooking_date_time();
                Communication_type =  PetAppointmentCreateRequestList.get(i).getCommunication_type();
                User_id =  PetAppointmentCreateRequestList.get(i).getUser_id();
                Pet_id =  PetAppointmentCreateRequestList.get(i).getFamily_id();
                Problem_info =  PetAppointmentCreateRequestList.get(i).getProblem_info();
                Doc_attched =  PetAppointmentCreateRequestList.get(i).getDoc_attched();
                Display_date =  PetAppointmentCreateRequestList.get(i).getDisplay_date();
                Appointment_types =  PetAppointmentCreateRequestList.get(i).getAppointment_types();
                Allergies =  PetAppointmentCreateRequestList.get(i).getAllergies();
                Amount =  PetAppointmentCreateRequestList.get(i).getAmount();
                Date_and_time =  PetAppointmentCreateRequestList.get(i).getDate_and_time();
                Visit_type =  PetAppointmentCreateRequestList.get(i).getVisit_type();
                Location_id =  PetAppointmentCreateRequestList.get(i).getLocation_id();
                Health_issue_title =  PetAppointmentCreateRequestList.get(i).getHealth_issue_title();
                pet_imgList =  PetAppointmentCreateRequestList.get(i).getPet_img();
                Payment_id = PetAppointmentCreateRequestList.get(i).getPayment_id();
                selectedPaymentMethod = PetAppointmentCreateRequestList.get(i).getPayment_method();
                selectedAppointmentType = PetAppointmentCreateRequestList.get(i).getAppointment_types();
                Total_price = PetAppointmentCreateRequestList.get(i).getAmount();
                Original_price = PetAppointmentCreateRequestList.get(i).getOriginal_price();
                Discount_price = PetAppointmentCreateRequestList.get(i).getDiscount_price();
                Coupon_status = PetAppointmentCreateRequestList.get(i).getCoupon_status();
                Coupon_code = PetAppointmentCreateRequestList.get(i).getCoupon_code();

            }
            Log.w(TAG,"doctorid : "+Doctor_id);
            Log.w(TAG,"pet_imgList : "+new Gson().toJson(pet_imgList));


        }


        ImageView img_back = include_petlover_header.findViewById(R.id.img_back);
        ImageView img_sos = include_petlover_header.findViewById(R.id.img_sos);
        ImageView img_notification = include_petlover_header.findViewById(R.id.img_notification);
        ImageView img_cart = include_petlover_header.findViewById(R.id.img_cart);
        ImageView img_profile = include_petlover_header.findViewById(R.id.img_profile);
        TextView toolbar_title = include_petlover_header.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Reschedule Appointment");


        img_sos.setVisibility(View.GONE);
        img_back.setVisibility(View.INVISIBLE);
        img_cart.setVisibility(View.GONE);
        img_notification.setVisibility(View.GONE);
        img_profile.setVisibility(View.GONE);
        avi_indicator.setVisibility(View.GONE);


        img_notification.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), NotificationActivity.class)));
        img_profile.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), CustomerProfileScreenActivity.class);
            intent.putExtra("doctorid",doctorid);
            intent.putExtra("fromactivity",TAG);
            startActivity(intent);
        });





        SessionManager sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getProfileDetails();
        userid = user.get(SessionManager.KEY_ID);


        Log.w(TAG,"userid :"+ userid);


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
         formattedDate = df.format(c);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDateandTime = sf.format(new Date());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
        String currentDateandTime24hrs = simpleDateFormat.format(new Date());
        currenttime = currentDateandTime24hrs.substring(currentDateandTime24hrs.indexOf(' ') + 1);
        currentdate =  currentDateandTime24hrs.substring(0, currentDateandTime24hrs.indexOf(' '));

        if(fromactivity != null && fromactivity.equalsIgnoreCase("PetLoverDoctorChoosePaymentMethodActivity")){
            currentDateandTime = sf.format(new Date(System.currentTimeMillis() + 3600000));
            Log.w(TAG,"currentDateandTime : "+currentDateandTime);

            String newcurrentDateandTime12hrs = simpleDateFormat.format(new Date(System.currentTimeMillis() + 3600000));
            Log.w(TAG,"newcurrentDateandTime12hrs : "+newcurrentDateandTime12hrs);
            currenttime = newcurrentDateandTime12hrs.substring(currentDateandTime24hrs.indexOf(' ') + 1);
            currentdate =  newcurrentDateandTime12hrs.substring(0, currentDateandTime24hrs.indexOf(' '));

            if (new ConnectionDetector(PetAppointment_SlotNotAvailable_Doctor_Date_Time_Activity.this).isNetworkAvailable(PetAppointment_SlotNotAvailable_Doctor_Date_Time_Activity.this)) {
                petDoctorAvailableTimeResponseCall(formattedDate,currentDateandTime,currenttime,currentdate);
            }


        }
        else{
            if (new ConnectionDetector(PetAppointment_SlotNotAvailable_Doctor_Date_Time_Activity.this).isNetworkAvailable(PetAppointment_SlotNotAvailable_Doctor_Date_Time_Activity.this)) {
                petDoctorAvailableTimeResponseCall(formattedDate,currentDateandTime,currenttime,currentdate);
            }
        }




        btn_bookappointment.setOnClickListener(v -> {
            Log.w(TAG,"btn_bookappointment selectedTimeSlot : "+selectedTimeSlot);
           if(selectedTimeSlot != null && !selectedTimeSlot.isEmpty()){

                if (new ConnectionDetector(PetAppointment_SlotNotAvailable_Doctor_Date_Time_Activity.this).isNetworkAvailable(PetAppointment_SlotNotAvailable_Doctor_Date_Time_Activity.this)) {
                    appointmentCheckResponseCall();
                }
            }else{
                showErrorLoading("Please select time slot ");

            }

        });

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int date = calendar.get(Calendar.DATE);

//        // Set a Start date (Default, 1 Jan 1970)
//        datePickerTimeline.setInitialDate(year, month, date);
//
//        datePickerTimeline.setDateTextColor(getResources().getColor(R.color.new_gree_color));
//        //datePickerTimeline.setDayTextColor(Color.parseColor("#009675"));
//        datePickerTimeline.setDayTextColor(getResources().getColor(R.color.new_gree_color));
//        datePickerTimeline.setMonthTextColor(getResources().getColor(R.color.new_gree_color));
//
//      // Set a date Selected Listener
//        datePickerTimeline.setOnDateSelectedListener(new OnDateSelectedListener() {
//            @SuppressLint("LogNotTimber")
//            @Override
//            public void onDateSelected(int year, int month, int dayOfMonth, int dayOfWeek) {
//                // Do Something
//                selectedTimeSlot = "";
//                String strdayOfMonth;
//                String strMonth;
//                int month1 =(month + 1);
//                if(dayOfMonth == 9 || dayOfMonth <9){
//                    strdayOfMonth = "0"+dayOfMonth;
//                    Log.w(TAG,"Selected dayOfMonth-->"+strdayOfMonth);
//                }else{
//                    strdayOfMonth = String.valueOf(dayOfMonth);
//                }
//
//                if(month1 == 9 || month1 <9){
//                    strMonth = "0"+month1;
//                    Log.w(TAG,"Selected month1-->"+strMonth);
//                }else{
//                    strMonth = String.valueOf(month1);
//                }
//
//                String Date = strdayOfMonth + "-" + strMonth + "-" + year;
//                Log.w(TAG,"Selected Date-->"+Date);
//
//                if (new ConnectionDetector(PetAppointment_SlotNotAvailable_Doctor_Date_Time_Activity.this).isNetworkAvailable(PetAppointment_SlotNotAvailable_Doctor_Date_Time_Activity.this)) {
//                    petDoctorAvailableTimeResponseCall(formattedDate, currentDateandTime, currenttime, Date);
//                }
//
//
//            }
//
//            @Override
//            public void onDisabledDateSelected(int year, int month, int day, int dayOfWeek, boolean isDisabled) {
//                // Do Something
//            }
//        });

      /*// Disable date
        Date[] dates = {Calendar.getInstance().getTime()};
        datePickerTimeline.deactivateDates(dates);*/

        img_back.setOnClickListener(v -> onBackPressed());

    }
    @SuppressLint("LogNotTimber")
    private void petDoctorAvailableTimeResponseCall(String formattedDate, String currentDateandTime, String currenttime, String Date) {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<PetDoctorAvailableTimeResponse> call = ApiService.petDoctorAvailableTimeResponseCall(RestUtils.getContentType(),petDoctorAvailableTimeRequest(Date));

        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<PetDoctorAvailableTimeResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<PetDoctorAvailableTimeResponse> call, @NonNull Response<PetDoctorAvailableTimeResponse> response) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"PetDoctorAvailableTimeResponse"+ "--->" + new Gson().toJson(response.body()));


                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        if(response.body().getData() != null){
                            doctorDateAvailabilityResponseList = response.body().getData();

                        }
                        if (response.body().getData() != null && response.body().getData().get(0).getTimes() != null) {
                            timesBeanList = response.body().getData().get(0).getTimes();
                        }
                        Log.w(TAG,"Size"+doctorDateAvailabilityResponseList.size());
                        if(response.body().getData() != null && !response.body().getData().isEmpty()){

                            if(response.body().getData().get(0).getDoctor_ava_Date() != null){
                                Doctor_ava_Date = response.body().getData().get(0).getDoctor_ava_Date();
                            }

                            sub_layer1.setVisibility(View.VISIBLE);
                            btn_bookappointment.setVisibility(View.VISIBLE);

                            if(doctorDateAvailabilityResponseList.size()>0) {


                                setViewAvlDays();

                            }

                            chat.setChecked(false);
                            video.setChecked(false);
                            chat.setVisibility(View.GONE);
                            video.setVisibility(View.GONE);
                            view.setVisibility(View.GONE);
                            tvlblavailabletime.setVisibility(View.VISIBLE);

                            String  doctorChatAvailable = response.body().getData().get(0).getComm_type_chat();
                            String doctorVideoAvailable = response.body().getData().get(0).getComm_type_video();



                            if (doctorChatAvailable.equalsIgnoreCase("Yes")) {
                                chat.setVisibility(View.VISIBLE);
                                chat.setChecked(true);
                                chat.setClickable(false);

                            }
                            if (doctorVideoAvailable.equalsIgnoreCase("Yes")) {
                                video.setVisibility(View.VISIBLE);
                                video.setChecked(true);
                                video.setClickable(false);
                            }
                            if(doctorChatAvailable.equalsIgnoreCase("Yes") && doctorVideoAvailable.equalsIgnoreCase("Yes")){
                                chat.setChecked(false);
                                video.setChecked(false);
                                chat.setClickable(true);
                                video.setClickable(true);
                                view.setVisibility(View.VISIBLE);


                            }

                        }


                    }
                    else{
                        sub_layer1.setVisibility(View.GONE);
                        btn_bookappointment.setVisibility(View.GONE);
                        tvlblavailabletime.setVisibility(View.GONE);
                        tvlbldoctoravailable.setVisibility(View.GONE);
                        showErrorLoading(response.body().getMessage());
                        rv_doctoravailabeslottime.setVisibility(View.GONE);
                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<PetDoctorAvailableTimeResponse> call, @NonNull Throwable t) {
                avi_indicator.smoothToHide();

                Log.w(TAG,"PetDoctorAvailableTimeResponseflr"+"--->" + t.getMessage());
            }
        });

    }
    @SuppressLint("LogNotTimber")
    private PetDoctorAvailableTimeRequest petDoctorAvailableTimeRequest(String Date) {

        /*
         * Date : 31-11-2020
         * user_id : 1234567890
         * cur_date : 31-11-2020
         * cur_time : 01:00 AM
         * current_time
         */

        PetDoctorAvailableTimeRequest petDoctorAvailableTimeRequest = new PetDoctorAvailableTimeRequest();
        petDoctorAvailableTimeRequest.setUser_id(doctorid);
        petDoctorAvailableTimeRequest.setDate(Date);
        petDoctorAvailableTimeRequest.setCurrent_time(currentDateandTime);
        petDoctorAvailableTimeRequest.setCur_time(currenttime);
        petDoctorAvailableTimeRequest.setCur_date(currentdate);
        Log.w(TAG,"petDoctorAvailableTimeRequest"+ "--->" + new Gson().toJson(petDoctorAvailableTimeRequest));
        return petDoctorAvailableTimeRequest;
    }


    private void setViewAvlDays() {
        rv_doctoravailabeslottime.setVisibility(View.VISIBLE);
        rv_doctoravailabeslottime.setLayoutManager(new GridLayoutManager(this, 4));
        rv_doctoravailabeslottime.setItemAnimator(new DefaultItemAnimator());
        PetMyCalendarSlotNotAvailableAdapter petMyCalendarSlotNotAvailableAdapter = new PetMyCalendarSlotNotAvailableAdapter(getApplicationContext(), timesBeanList, rv_doctoravailabeslottime, this);
        rv_doctoravailabeslottime.setAdapter(petMyCalendarSlotNotAvailableAdapter);







    }

    public void showErrorLoading(String errormesage){
        alertDialogBuilder = new AlertDialog.Builder(this);
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
    public void onBackPressed() {
       // super.onBackPressed();



    }
    public void callDirections(String tag){
        Intent intent = new Intent(getApplicationContext(), CustomerDashboardActivity.class);
        intent.putExtra("tag",tag);
        startActivity(intent);
        finish();

    }



    @SuppressLint("LogNotTimber")
    @Override
    public void onItemSelectedTime(String selectedTime) {
        Log.w(TAG,"onItemSelectedTime : "+selectedTime);
        selectedTimeSlot = selectedTime;

    }


    @SuppressLint("LogNotTimber")
    private void appointmentCheckResponseCall() {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<AppointmentCheckResponse> call = ApiService.appointmentCheckResponseCall(RestUtils.getContentType(),appointmentCheckRequest());
        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<AppointmentCheckResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<AppointmentCheckResponse> call, @NonNull Response<AppointmentCheckResponse> response) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"appointmentCheckResponseCall"+ "--->" + new Gson().toJson(response.body()));


                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        petAppointmentCreateResponseCall();
                    }else{
                        showAppointmentBookErrorLoading(response.body().getMessage());
                    }

                }


            }

            @Override
            public void onFailure(@NonNull Call<AppointmentCheckResponse> call, @NonNull Throwable t) {
                avi_indicator.smoothToHide();

                Log.w(TAG,"AppointmentCheckResponseflr"+"--->" + t.getMessage());
            }
        });

    }
    @SuppressLint("LogNotTimber")
    private AppointmentCheckRequest appointmentCheckRequest() {
        /*
         * Booking_Date : 02-12-2020
         * Booking_Time : 09:00 AM
         * doctor_id : 5fc4eb2c913fec4204e4b15d
         */

        AppointmentCheckRequest appointmentCheckRequest = new AppointmentCheckRequest();
        appointmentCheckRequest.setDoctor_id(doctorid);
        appointmentCheckRequest.setBooking_Date(Doctor_ava_Date);
        appointmentCheckRequest.setBooking_Time(selectedTimeSlot);
        Log.w(TAG,"appointmentCheckRequest"+ "--->" + new Gson().toJson(appointmentCheckRequest));
        return appointmentCheckRequest;
    }



    @SuppressLint("LogNotTimber")
    private void rescheduleResponseCall() {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<SuccessResponse> call = ApiService.rescheduleResponseCall(RestUtils.getContentType(),rescheduleAppointmentRequest());
        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"appointmentCheckResponseCall"+ "--->" + new Gson().toJson(response.body()));


                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        showRescheduleAppointmentSuccessalert(response.body().getMessage());


                    }else{
                        showErrorLoading(response.body().getMessage());
                    }

                }


            }

            @Override
            public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {
                avi_indicator.smoothToHide();

                Log.w(TAG,"AppointmentCheckResponseflr"+"--->" + t.getMessage());
            }
        });

    }
    @SuppressLint("LogNotTimber")
    private RescheduleAppointmentRequest rescheduleAppointmentRequest() {
        /*
         * _id : 604090e72c2b43125f8cb84e
         * already_booked_date : 03-05-2021 03:30 AM
         * reschedule_date : 05-03-2021 11:00 AM
         * booking_date : 05-03-2021
         * booking_time : 11:00 AM
         */

        RescheduleAppointmentRequest  rescheduleAppointmentRequest = new RescheduleAppointmentRequest();
        rescheduleAppointmentRequest.set_id(id);
        rescheduleAppointmentRequest.setAlready_booked_date(bookingdateandtime);
        rescheduleAppointmentRequest.setReschedule_date(Doctor_ava_Date+" "+selectedTimeSlot);
        rescheduleAppointmentRequest.setBooking_date(Doctor_ava_Date);
        rescheduleAppointmentRequest.setBooking_time(selectedTimeSlot);
        Log.w(TAG,"rescheduleAppointmentRequest"+ "--->" + new Gson().toJson(rescheduleAppointmentRequest));
        return rescheduleAppointmentRequest;
    }


    @SuppressLint("LogNotTimber")
    private void petAppointmentCreateResponseCall() {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<PetAppointmentCreateResponse> call = ApiService.petAppointmentCreateResponseCall(RestUtils.getContentType(),petAppointmentCreateRequest());

        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<PetAppointmentCreateResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<PetAppointmentCreateResponse> call, @NonNull Response<PetAppointmentCreateResponse> response) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"PetDoctorAvailableTimeResponse"+ "--->" + new Gson().toJson(response.body()));


                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        if(response.body().getMessage() != null){
                            showRescheduleAppointmentSuccessalert(response.body().getMessage());

                        }



                    }
                    else{
                        if(response.body().getMessage() != null && response.body().getMessage().equalsIgnoreCase("Slot Not Available") ){
                            showAppointmentBookErrorLoading(response.body().getMessage());

                        }


                    }
                }


            }

            @Override
            public void onFailure(@NonNull Call<PetAppointmentCreateResponse> call, @NonNull Throwable t) {
                avi_indicator.smoothToHide();

                Log.w(TAG,"PetDoctorAvailableTimeResponseflr"+"--->" + t.getMessage());
            }
        });

    }
    @SuppressLint("LogNotTimber")
    private PetAppointmentCreateRequest petAppointmentCreateRequest() {

        /*
         * doctor_id : 5fb62a1924583828f10f8731
         * booking_date : 19/11/2020
         * booking_time : 12:22 pm
         * booking_date_time : 19/11/2020 12:22 pm
         * communication_type :
         * video_id : http://vidoe.com
         * user_id : 5fb6162a211fce241eaf53a9
         * pet_id : 5fb38ea334f6014ea9013d30
         * problem_info : problem info
         * doc_attched : [{"file":"http://google.pdf"}]
         * doc_feedback : doc feedback
         * doc_rate : 5
         * user_feedback : user feedback
         * user_rate : 4.5
         * display_date : 19/11/2020 01:00 PM
         * server_date_time : 09/12/2020 03:00 PM
         * payment_id : 1234567890
         * payment_method : Card
         * appointment_types : Normal
         * allergies : this is
         * amount : 400
         * location_id,
         * visit_type
         * original_price : 100
         * discount_price : 10
         * total_price : 90
         * coupon_status : String,
         *  coupon_code : String,
         *  */
        List<PetAppointmentCreateRequest.DocAttchedBean> doc_attched = new ArrayList<>();


        @SuppressLint("SimpleDateFormat") DateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");
        @SuppressLint("SimpleDateFormat") DateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        Date date = null;
        try {
            date = inputFormat.parse(Doctor_ava_Date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String outputDateStr = outputFormat.format(date);
        String outputTimeStr = null;

        @SuppressLint("SimpleDateFormat") SimpleDateFormat h_mm_a   = new SimpleDateFormat("hh:mm aa");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat hh_mm_ss = new SimpleDateFormat("HH:mm:ss");

        try {
            Date d1 = h_mm_a.parse(selectedTimeSlot);
            outputTimeStr =hh_mm_ss.format(d1);

        } catch (Exception e) {
            e.printStackTrace();
        }
        String displaydateandtime = outputDateStr+" "+outputTimeStr;

        if(userid != null){
            userid = userid;
        }else{
            userid = "";
        }

        PetAppointmentCreateRequest petAppointmentCreateRequest = new PetAppointmentCreateRequest();
        petAppointmentCreateRequest.setDoctor_id(doctorid);
        petAppointmentCreateRequest.setBooking_date(Doctor_ava_Date);
        petAppointmentCreateRequest.setBooking_time(selectedTimeSlot);
        petAppointmentCreateRequest.setBooking_date_time(Doctor_ava_Date+" "+selectedTimeSlot);
        petAppointmentCreateRequest.setCommunication_type(Communication_type);
        petAppointmentCreateRequest.setVideo_id("");
        petAppointmentCreateRequest.setUser_id(userid);
        petAppointmentCreateRequest.setFamily_id(petId);
        petAppointmentCreateRequest.setProblem_info(Problem_info);
        petAppointmentCreateRequest.setDoc_attched(doc_attched);
        petAppointmentCreateRequest.setDoc_feedback("");
        petAppointmentCreateRequest.setDoc_rate(0);
        petAppointmentCreateRequest.setUser_feedback("");
        petAppointmentCreateRequest.setUser_rate(0);
        petAppointmentCreateRequest.setDisplay_date(displaydateandtime);
        petAppointmentCreateRequest.setServer_date_time("");
        petAppointmentCreateRequest.setPayment_id(Payment_id);
        petAppointmentCreateRequest.setPayment_method(selectedPaymentMethod);
        petAppointmentCreateRequest.setAppointment_types(selectedAppointmentType);
        petAppointmentCreateRequest.setAllergies(Allergies);
        petAppointmentCreateRequest.setAmount(Total_price);
        petAppointmentCreateRequest.setMobile_type("Android");
        petAppointmentCreateRequest.setService_name("");
        petAppointmentCreateRequest.setService_amount("");
        petAppointmentCreateRequest.setDate_and_time(Date_and_time);
        petAppointmentCreateRequest.setVisit_type(selectedVisitType);
        petAppointmentCreateRequest.setLocation_id(Location_id);
        petAppointmentCreateRequest.setHealth_issue_title(health_issue_title);
        petAppointmentCreateRequest.setOriginal_price(Original_price);
        petAppointmentCreateRequest.setDiscount_price(Discount_price);
        petAppointmentCreateRequest.setTotal_price(Total_price);
        petAppointmentCreateRequest.setCoupon_status(Coupon_status);
        petAppointmentCreateRequest.setCoupon_code(Coupon_code);
        petAppointmentCreateRequest.setPet_img(pet_imgList);
        Log.w(TAG,"petAppointmentCreateRequest : "+new Gson().toJson(petAppointmentCreateRequest));

        return petAppointmentCreateRequest;

    }

    @SuppressLint("SetTextI18n")
    public void showAppointmentBookErrorLoading(String errormesage) {
        dialog = new Dialog(PetAppointment_SlotNotAvailable_Doctor_Date_Time_Activity.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_slotnotavailable_reschedule_appointment_layout);
        Button btn_view = dialog.findViewById(R.id.btn_view);
        ImageView img_success = dialog.findViewById(R.id.img_success);
        img_success.setVisibility(View.GONE);
        TextView txt_header = dialog.findViewById(R.id.txt_header);
        txt_header.setText(errormesage);
        btn_view.setText("Reschedule Appointment");
        btn_view.setOnClickListener(view -> {
            hideAppointmentBookLoading();

        });
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
    public void hideAppointmentBookLoading() {
        try {
            petDoctorAvailableTimeResponseCall(formattedDate,currentDateandTime,currenttime,currentdate);
            dialog.dismiss();

        } catch (Exception ignored) {

        }
    }


    private void showRescheduleAppointmentSuccessalert(String message) {
        try {
            dialog = new Dialog(PetAppointment_SlotNotAvailable_Doctor_Date_Time_Activity.this);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_reschedule_appointment_layout);
            Button btn_view = dialog.findViewById(R.id.btn_view);
            TextView txt_header = dialog.findViewById(R.id.txt_header);
            txt_header.setText(message);
            btn_view.setOnClickListener(view -> {
                dialog.dismiss();
                Intent intent = new Intent(PetAppointment_SlotNotAvailable_Doctor_Date_Time_Activity.this, PetMyappointmentsActivity.class);
                startActivity(intent);
                finish();


            });
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }




    }




}
