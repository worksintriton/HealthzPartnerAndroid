package com.triton.healthzpartners.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
            holder.txt_medicine.setText(prescriptionDataList.get(position).getTablet_name());
            holder.txt_noofdays.setText(prescriptionDataList.get(position).getQuantity());
            holder.txt_consumptionperday.setText(prescriptionDataList.get(position).getConsumption());

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

        public TextView txt_medicine,txt_noofdays,txt_consumptionperday;
        CheckBox chx_afterfood,chx_beforefood;



        public ViewHolderOne(View itemView) {
            super(itemView);
            txt_medicine = itemView.findViewById(R.id.txt_medicine);
            txt_noofdays = itemView.findViewById(R.id.txt_noofdays);
            txt_consumptionperday = itemView.findViewById(R.id.txt_consumptionperday);

            chx_afterfood = itemView.findViewById(R.id.chx_afterfood);
            chx_beforefood = itemView.findViewById(R.id.chx_beforefood);

            chx_afterfood.setClickable(false);
            chx_beforefood.setClickable(false);



        }


    }

}
