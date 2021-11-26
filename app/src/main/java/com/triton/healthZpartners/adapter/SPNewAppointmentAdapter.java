package com.triton.healthZpartners.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.triton.healthZpartners.R;
import com.triton.healthZpartners.api.APIClient;
import com.triton.healthZpartners.interfaces.OnAppointmentCancel;
import com.triton.healthZpartners.interfaces.OnAppointmentComplete;

import com.triton.healthZpartners.responsepojo.SPAppointmentResponse;
import com.triton.healthZpartners.serviceprovider.SPAppointmentDetailsActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SPNewAppointmentAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  String TAG = "SPNewAppointmentAdapter";
    private final List<SPAppointmentResponse.DataBean> newAppointmentResponseList;
    private Context context;

    SPAppointmentResponse.DataBean currentItem;

    private OnAppointmentCancel onAppointmentCancel;
    private OnAppointmentComplete onAppointmentComplete;
    private int size;
    private boolean isVaildDate;


    public SPNewAppointmentAdapter(Context context, List<SPAppointmentResponse.DataBean> newAppointmentResponseList, RecyclerView inbox_list, int size, OnAppointmentCancel onAppointmentCancel,OnAppointmentComplete onAppointmentComplete) {
        this.newAppointmentResponseList = newAppointmentResponseList;
        this.context = context;
        this.size = size;
        this.onAppointmentCancel = onAppointmentCancel;
        this.onAppointmentComplete = onAppointmentComplete;



    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_doctor_new_appointment, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        initLayoutOne((ViewHolderOne) holder, position);


    }

    @SuppressLint({"SetTextI18n", "LogNotTimber"})
    private void initLayoutOne(ViewHolderOne holder, final int position) {


        currentItem = newAppointmentResponseList.get(position);
        if(newAppointmentResponseList.get(position).getFamily_id() != null) {
            if (newAppointmentResponseList.get(position).getFamily_id().getGender() != null) {
                holder.txt_gender.setText(newAppointmentResponseList.get(position).getFamily_id().getGender());
            }
            if (newAppointmentResponseList.get(position).getFamily_id().getName() != null) {
                holder.txt_patient_name.setText(newAppointmentResponseList.get(position).getFamily_id().getName());
            }
        }

        if(newAppointmentResponseList.get(position).getService_name() != null){
            holder.txt_type.setText(newAppointmentResponseList.get(position).getService_name());
        }
        if(newAppointmentResponseList.get(position).getService_amount() != null){
            holder.txt_service_cost.setText("\u20B9 "+newAppointmentResponseList.get(position).getService_amount());
        }

        if(newAppointmentResponseList.get(position).getBooking_date_time() != null){
            holder.txt_bookedon.setText("Booked For:"+" "+newAppointmentResponseList.get(position).getBooking_date_time());

        }


       try{
             if (newAppointmentResponseList.get(position).getFamily_id().getPic().get(0).getImage() != null && !newAppointmentResponseList.get(position).getFamily_id().getPic().get(0).getImage().isEmpty()) {
                 Glide.with(context)
                         .load(newAppointmentResponseList.get(position).getFamily_id().getPic().get(0).getImage())
                         .into(holder.img_pet_imge);

             }
             else{
                 Glide.with(context)
                         .load(APIClient.PROFILE_IMAGE_URL)
                         .into(holder.img_pet_imge);

             }

         }catch (Exception e){}


        holder.btn_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onAppointmentComplete.onAppointmentComplete(newAppointmentResponseList.get(position).get_id());

            }
        });

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        String bookingDateandTime = newAppointmentResponseList.get(position).getBooking_date_time();
        compareDatesandTime(currentDateandTime,bookingDateandTime);

        if(isVaildDate){
            holder.ll_cancel.setVisibility(View.VISIBLE);
        }else{
            holder.ll_cancel.setVisibility(View.INVISIBLE);
        }

        holder.txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAppointmentCancel.onAppointmentCancel(newAppointmentResponseList.get(position).get_id(),newAppointmentResponseList.get(position).getAppoinment_status(),newAppointmentResponseList.get(position).getUser_id().get_id(),newAppointmentResponseList.get(position).getSp_id(),newAppointmentResponseList.get(position).getAppointment_UID(),"",newAppointmentResponseList.get(position).getService_amount(),newAppointmentResponseList.get(position).getPayment_method());

            }
        });

        holder.ll_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent i = new Intent(context, SPAppointmentDetailsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("appointment_id",newAppointmentResponseList.get(position).get_id());
                    i.putExtra("bookedat",newAppointmentResponseList.get(position).getBooking_date_time());
                    i.putExtra("from",TAG);
                    context.startActivity(i);

            }
        });




    }


    @Override
    public int getItemCount() {
        return Math.min(newAppointmentResponseList.size(), size);

    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class ViewHolderOne extends RecyclerView.ViewHolder {
        public TextView txt_patient_name,txt_gender,txt_type,txt_service_cost,txt_bookedon,txt_lbl_type,txt_cancel;
        public ImageView img_pet_imge,img_emergency_appointment,img_videocall;
        public Button btn_complete;
        public LinearLayout ll_new,ll_cancel;



        public ViewHolderOne(View itemView) {
            super(itemView);
            img_pet_imge = itemView.findViewById(R.id.img_pet_imge);
            txt_patient_name = itemView.findViewById(R.id.txt_patient_name);
            txt_gender = itemView.findViewById(R.id.txt_gender);
            txt_lbl_type = itemView.findViewById(R.id.txt_lbl_type);
            txt_type = itemView.findViewById(R.id.txt_type);
            txt_service_cost = itemView.findViewById(R.id.txt_service_cost);
            txt_bookedon = itemView.findViewById(R.id.txt_bookedon);
            txt_cancel = itemView.findViewById(R.id.txt_cancel);
            btn_complete = itemView.findViewById(R.id.btn_complete);
            ll_new = itemView.findViewById(R.id.ll_new);
            ll_cancel = itemView.findViewById(R.id.ll_cancel);
            img_emergency_appointment = itemView.findViewById(R.id.img_emergency_appointment);
            img_videocall = itemView.findViewById(R.id.img_videocall);
            img_emergency_appointment.setVisibility(View.GONE);
            img_videocall.setVisibility(View.GONE);

            txt_cancel.setText("Reject");
            txt_lbl_type.setText("Service Name");



        }




    }



    @SuppressLint("LogNotTimber")
    private void compareDatesandTime(String currentDateandTime, String bookingDateandTime) {
        try{

            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");

            String str1 = currentDateandTime;
            Date currentDate = formatter.parse(str1);

            String str2 = bookingDateandTime;
            Date responseDate = formatter.parse(str2);

            Log.w(TAG,"compareDatesandTime--->"+"responseDate :"+responseDate+" "+"currentDate :"+currentDate);

            if (currentDate.compareTo(responseDate)<0 || responseDate.compareTo(currentDate) == 0)
            {
                Log.w(TAG,"date is equal");
                isVaildDate = true;

            }else{
                Log.w(TAG,"date is not equal");
                isVaildDate = false;
            }



        }catch (ParseException e1){
            e1.printStackTrace();
        }
    }





}
