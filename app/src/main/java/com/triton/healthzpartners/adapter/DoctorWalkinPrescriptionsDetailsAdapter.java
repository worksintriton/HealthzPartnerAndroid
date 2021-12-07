package com.triton.healthzpartners.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.triton.healthzpartners.R;
import com.triton.healthzpartners.responsepojo.PrescriptionFetchResponse;

import java.util.List;


public class DoctorWalkinPrescriptionsDetailsAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  String TAG = "DoctorWalkinPrescriptionsDetailsAdapter";
    private Context mcontext;

    PrescriptionFetchResponse.DataBean.PrescriptionDataBean currentItem;

    private List<PrescriptionFetchResponse.DataBean.PrescriptionDataBean> prescriptionDataList;



    public DoctorWalkinPrescriptionsDetailsAdapter(Context context, List<PrescriptionFetchResponse.DataBean.PrescriptionDataBean> prescriptionDataList) {
        this.prescriptionDataList = prescriptionDataList;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_doctor_walkin_prescriptions_details_list, parent, false);
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



        public ViewHolderOne(View itemView) {
            super(itemView);
            txt_medicine = itemView.findViewById(R.id.txt_medicine);
            txt_noofdays = itemView.findViewById(R.id.txt_noofdays);
            txt_consumptionperday = itemView.findViewById(R.id.txt_consumptionperday);



        }


    }

}
