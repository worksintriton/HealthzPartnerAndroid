package com.triton.healthZpartners.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.triton.healthZpartners.R;
import com.triton.healthZpartners.interfaces.SpecTypeChckedListener;
import com.triton.healthZpartners.requestpojo.DocBusInfoUploadRequest;
import com.triton.healthZpartners.responsepojo.DropDownListResponse;

import java.util.List;


public class SpecTypesListAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  String TAG = "SpecTypesListAdapter";
    private Context mcontext;
    private List<DropDownListResponse.DataBean.SpecialzationBean> spectypedataBeanList;
    DropDownListResponse.DataBean.SpecialzationBean currentItem;
    private SpecTypeChckedListener specTypeChckedListener;

    List<DocBusInfoUploadRequest.SpecializationBean> specialzationListEdit;


    public SpecTypesListAdapter(Context context, List<DropDownListResponse.DataBean.SpecialzationBean> spectypedataBeanList,List<DocBusInfoUploadRequest.SpecializationBean> specialzationListEdit, SpecTypeChckedListener specTypeChckedListener) {
        this.mcontext = context;
        this.spectypedataBeanList = spectypedataBeanList;
        this.specialzationListEdit = specialzationListEdit;
        this.specTypeChckedListener = specTypeChckedListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_specialization_list, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        initLayoutOne((ViewHolderOne) holder, position);


    }

    @SuppressLint("LogNotTimber")
    private void initLayoutOne(ViewHolderOne holder, final int position) {

        currentItem = spectypedataBeanList.get(position);

        holder.txt_spectypes.setText(currentItem.getSpecialzation());

        holder.chx_spectypes.setChecked(currentItem.isSelected());

        holder.chx_spectypes.setTag(position);


        for(int i=0;i<specialzationListEdit.size();i++){
            if(null!=specialzationListEdit && null!=currentItem.getSpecialzation() && specialzationListEdit.get(i).getSpecialization().equalsIgnoreCase(currentItem.getSpecialzation().trim())){
                holder.chx_spectypes.setChecked(true);
                Log.w(TAG,"specialzationListEdit"+new Gson().toJson(specialzationListEdit));

            }

        }


      /*  holder.chx_spectypes.setOnClickListener(v -> {

            Integer pos = (Integer) holder.chx_spectypes.getTag();

            if (spectypedataBeanList.get(pos).isSelected())
            {
                spectypedataBeanList.get(pos).setSelected(false);

                specTypeChckedListener.onItemSpecUnCheck(pos,spectypedataBeanList.get(pos).getSpecialzation());

            } else {
                specTypeChckedListener.onItemSpecCheck(pos,spectypedataBeanList.get(pos).getSpecialzation(),spectypedataBeanList);

            }

        });*/


        holder.chx_spectypes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String chspecialization = spectypedataBeanList.get(position).getSpecialzation();

                if(isChecked){
                    if (holder.chx_spectypes.isChecked()) {
                        specTypeChckedListener.onItemSpecCheck(position,chspecialization,spectypedataBeanList);
                    }

                }else{

                    specTypeChckedListener.onItemSpecUnCheck(position,chspecialization);

                }

            }
        });


    }
    @Override
    public int getItemCount() {
        return spectypedataBeanList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class ViewHolderOne extends RecyclerView.ViewHolder {

        public TextView txt_spectypes;
        public CheckBox chx_spectypes;


        public ViewHolderOne(View itemView) {
            super(itemView);

            txt_spectypes = itemView.findViewById(R.id.spec_name);

            chx_spectypes = itemView.findViewById(R.id.checkbox_specialization_type);


        }

    }

}
