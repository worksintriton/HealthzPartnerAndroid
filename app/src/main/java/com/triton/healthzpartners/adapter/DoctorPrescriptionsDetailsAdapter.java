package com.triton.healthzpartners.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.triton.healthzpartners.R;

import com.triton.healthzpartners.responsepojo.PrescriptionFetchResponse;


import java.util.List;


public class DoctorPrescriptionsDetailsAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  String TAG = "DoctorPrescriptionsDetailsAdapter";
    private Context mcontext;

    PrescriptionFetchResponse.DataBean.PrescriptionDataBean currentItem;

    private List<PrescriptionFetchResponse.DataBean.PrescriptionDataBean> prescriptionDataList;



    public DoctorPrescriptionsDetailsAdapter(Context context, List<PrescriptionFetchResponse.DataBean.PrescriptionDataBean> prescriptionDataList) {
        this.prescriptionDataList = prescriptionDataList;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_doctor_prescriptions_details, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        initLayoutOne((ViewHolderOne) holder, position);


    }
    @SuppressLint("SetTextI18n")
    private void initLayoutOne(ViewHolderOne holder, final int position) {
        currentItem = prescriptionDataList.get(position);
        Log.w(TAG,"prescriptionDataList : "+new Gson().toJson(prescriptionDataList));
        holder.txt_medicine.setText(prescriptionDataList.get(position).getTablet_name());
        holder.txt_noofdays.setText(prescriptionDataList.get(position).getQuantity());
     //   holder.tv_consumption.setText(""+prescriptionDataList.get(position).getConsumption());
        /*if(prescriptionDataList.get(position).getConsumption()!=null&&!prescriptionDataList.get(position).getConsumption().isEmpty()){

            String[] namesList = prescriptionDataList.get(position).getConsumption().split(",");

            StringBuilder sb = new StringBuilder();

            for(String name : namesList){

                sb.append(name).append("\n");

            }


        }
*/

        if(currentItem.getConsumption().isMorning()){
            holder.chx_m.setChecked(true);
        }
        if(currentItem.getConsumption().isEvening()){
            holder.chx_a.setChecked(true);
        }
        if(currentItem.getConsumption().isNight()){
            holder.chx_n.setChecked(true);
        }

        if(currentItem.getIntakeBean().isAfterfood()){
            holder.chx_afterfood.setChecked(true);
        }
        if(currentItem.getIntakeBean().isBeforefood()){
            holder.chx_beforefood.setChecked(true);
        }






    }
    @Override
    public int getItemCount() {
        return prescriptionDataList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class ViewHolderOne extends RecyclerView.ViewHolder {

        TextView txt_medicine,txt_noofdays,tv_consumption;
        CheckBox chx_m,chx_a,chx_n,chx_afterfood,chx_beforefood;



        public ViewHolderOne(View itemView) {
            super(itemView);
            txt_medicine = itemView.findViewById(R.id.txt_medicine);
            txt_noofdays = itemView.findViewById(R.id.txt_noofdays);
            tv_consumption = itemView.findViewById(R.id.tv_consumption);
            chx_m = itemView.findViewById(R.id.chx_m);
            chx_a = itemView.findViewById(R.id.chx_a);
            chx_n = itemView.findViewById(R.id.chx_n);
            chx_afterfood = itemView.findViewById(R.id.chx_afterfood);
            chx_beforefood = itemView.findViewById(R.id.chx_beforefood);
            chx_m.setClickable(false);
            chx_a.setClickable(false);
            chx_n.setClickable(false);
            chx_afterfood.setClickable(false);
            chx_beforefood.setClickable(false);



        }


    }

}