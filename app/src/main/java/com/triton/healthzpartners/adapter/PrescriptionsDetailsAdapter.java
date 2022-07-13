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
import com.triton.healthzpartners.requestpojo.PrescriptionCreateRequest;

import java.util.List;


public class PrescriptionsDetailsAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  String TAG = "PrescriptionsDetailsAdapter";
    private Context mcontext;

    PrescriptionCreateRequest.PrescriptionDataBean currentItem;

    List<PrescriptionCreateRequest.PrescriptionDataBean> prescriptionDataList;



    public PrescriptionsDetailsAdapter(Context context,  List<PrescriptionCreateRequest.PrescriptionDataBean> prescriptionDataList) {
        this.prescriptionDataList = prescriptionDataList;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_doctor_prescriptions_details_list, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        initLayoutOne((ViewHolderOne) holder, position);


    }

    @SuppressLint({"SetTextI18n", "LogNotTimber"})
    private void initLayoutOne(ViewHolderOne holder, final int position) {
            currentItem = prescriptionDataList.get(position);

            Log.w(TAG,"prescriptionDataList : "+new Gson().toJson(prescriptionDataList));
            holder.tv_tabletname.setText(prescriptionDataList.get(position).getTablet_name());
            holder.tv_quanity.setText(prescriptionDataList.get(position).getQuantity());

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

        TextView tv_tabletname,tv_quanity;
        CheckBox chx_m,chx_a,chx_n,chx_afterfood,chx_beforefood;



        public ViewHolderOne(View itemView) {
            super(itemView);
            tv_tabletname = itemView.findViewById(R.id.tv_tabletname);
            tv_quanity = itemView.findViewById(R.id.tv_quanity);
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
