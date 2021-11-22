package com.triton.healthZpartners.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.triton.healthZpartners.R;
import com.triton.healthZpartners.responsepojo.SPAppointmentResponse;
import com.triton.healthZpartners.serviceprovider.SPAppointmentDetailsActivity;

import java.util.List;


public class SPCompletedAppointmentAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private  String TAG = "SPCompletedAppointmentAdapter";
    private List<SPAppointmentResponse.DataBean> completedAppointmentResponseList;
    private Context context;
    private int size;



    public SPCompletedAppointmentAdapter(Context context, List<SPAppointmentResponse.DataBean> completedAppointmentResponseList, RecyclerView inbox_list, int size) {
        this.completedAppointmentResponseList = completedAppointmentResponseList;
        this.context = context;
        this.size = size;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_doctor_completed_appointment, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        initLayoutOne((ViewHolderOne) holder, position);


    }

    @SuppressLint("SetTextI18n")
    private void initLayoutOne(ViewHolderOne holder, final int position) {

        if(completedAppointmentResponseList.get(position).getPet_id().getPet_name() != null){
            holder.txt_petname.setText(completedAppointmentResponseList.get(position).getPet_id().getPet_name());
        }
        if(completedAppointmentResponseList.get(position).getPet_id().getPet_type() != null) {
            holder.txt_pettype.setText(completedAppointmentResponseList.get(position).getPet_id().getPet_type());
        }
        if(completedAppointmentResponseList.get(position).getCompleted_at() != null) {
            holder.txt_completed_date.setText("Completed on:" + " " + completedAppointmentResponseList.get(position).getCompleted_at());
        }

        holder.txt_lbl_type.setText("Service Name");
        if(completedAppointmentResponseList.get(position).getService_name() != null){
            holder.txt_type.setText(completedAppointmentResponseList.get(position).getService_name());
        }
        if(completedAppointmentResponseList.get(position).getService_amount() != null){
            holder.txt_service_cost.setText("INR "+completedAppointmentResponseList.get(position).getService_amount());
        }

      /*  Log.w(TAG,"PetImage : "+completedAppointmentResponseList.get(position).getPet_id().getPet_img().get(0).getPet_img());

        if (completedAppointmentResponseList.get(position).getPet_id().getPet_img().get(0).getPet_img() != null && !completedAppointmentResponseList.get(position).getPet_id().getPet_img().get(0).getPet_img().isEmpty()) {
            Glide.with(context)
                    .load(completedAppointmentResponseList.get(position).getPet_id().getPet_img().get(0).getPet_img())
                    .into(holder.img_pet_imge);

        }
        else{
            Glide.with(context)
                    .load(APIClient.PROFILE_IMAGE_URL)
                    .into(holder.img_pet_imge);

        }*/

        holder.ll_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SPAppointmentDetailsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("appointment_id",completedAppointmentResponseList.get(position).get_id());
                i.putExtra("fromactivity",TAG);
                context.startActivity(i);

            }
        });
















    }


    @Override
    public int getItemCount() {
        return Math.min(completedAppointmentResponseList.size(), size);
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class ViewHolderOne extends RecyclerView.ViewHolder {
        public TextView txt_petname,txt_pettype,txt_type,txt_service_cost,txt_completed_date,txt_lbl_type;
        public ImageView img_pet_imge,img_emergency_appointment;
        public Button btn_cancel,btn_complete,btn_prescriptiondetails;
        public LinearLayout ll_new;

        public ViewHolderOne(View itemView) {
            super(itemView);
            img_pet_imge = itemView.findViewById(R.id.img_pet_imge);
            txt_petname = itemView.findViewById(R.id.txt_petname);
            txt_pettype = itemView.findViewById(R.id.txt_pettype);
            txt_lbl_type = itemView.findViewById(R.id.txt_lbl_type);
            txt_type = itemView.findViewById(R.id.txt_type);
            txt_service_cost = itemView.findViewById(R.id.txt_service_cost);
            txt_completed_date = itemView.findViewById(R.id.txt_completed_date);
            btn_cancel = itemView.findViewById(R.id.btn_cancel);
            btn_complete = itemView.findViewById(R.id.btn_complete);
            btn_prescriptiondetails = itemView.findViewById(R.id.btn_prescriptiondetails);
            btn_prescriptiondetails.setVisibility(View.GONE);
            ll_new = itemView.findViewById(R.id.ll_new);

            img_emergency_appointment = itemView.findViewById(R.id.img_emergency_appointment);
            img_emergency_appointment.setVisibility(View.GONE);





        }




    }








}
