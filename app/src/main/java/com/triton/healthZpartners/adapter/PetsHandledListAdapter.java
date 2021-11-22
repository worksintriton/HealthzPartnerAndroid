package com.triton.healthZpartners.adapter;

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
import com.triton.healthZpartners.interfaces.PetHandledTypeCheckedListener;
import com.triton.healthZpartners.requestpojo.DocBusInfoUploadRequest;
import com.triton.healthZpartners.responsepojo.DropDownListResponse;

import java.util.List;


public class PetsHandledListAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final String TAG = "PetsHandledListAdapter";
    private Context context;
    private List<DropDownListResponse.DataBean.PetHandleBean> petHandleBeanList;
    DropDownListResponse.DataBean.PetHandleBean currentItem;
    private PetHandledTypeCheckedListener petHandledTypeCheckedListener;
    List<DocBusInfoUploadRequest.PetHandledBean> pethandleListEdit;


    public PetsHandledListAdapter(Context context, List<DropDownListResponse.DataBean.PetHandleBean> pettypedataBeanList,List<DocBusInfoUploadRequest.PetHandledBean> pethandleListEdit, PetHandledTypeCheckedListener petHandledTypeCheckedListener) {
        this.petHandleBeanList = pettypedataBeanList;
        this.pethandleListEdit = pethandleListEdit;
        this.context = context;
        this.petHandledTypeCheckedListener = petHandledTypeCheckedListener;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pets_type_list, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        initLayoutOne((ViewHolderOne) holder, position);


    }

    private void initLayoutOne(ViewHolderOne holder, final int position) {

        currentItem = petHandleBeanList.get(position);
        holder.txt_spectypes.setText(currentItem.getPet_handle());
        holder.chx_spectypes.setChecked(currentItem.isSelected());
        holder.chx_spectypes.setTag(position);



       /* holder.chx_spectypes.setOnClickListener(v -> {

            Integer pos = (Integer) holder.chx_spectypes.getTag();

            if (petHandleBeanList.get(pos).isSelected())
            {
                petHandleBeanList.get(pos).setSelected(false);

                petHandledTypeCheckedListener.onItemPetUnCheck(pos,petHandleBeanList.get(pos).getPet_handle());

            }

            else
            {

                petHandledTypeCheckedListener.onItemPetCheck(pos,petHandleBeanList.get(pos).getPet_handle(),petHandleBeanList);

            }

        });*/

        for(int i=0;i<pethandleListEdit.size();i++){
            if(null!=pethandleListEdit && null!=currentItem.getPet_handle() && pethandleListEdit.get(i).getPet_handled().equalsIgnoreCase(currentItem.getPet_handle().trim())){
                holder.chx_spectypes.setChecked(true);
                Log.w(TAG,"pethandleListEdit"+new Gson().toJson(pethandleListEdit));

            }

        }





        holder.chx_spectypes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String chpetshandled = petHandleBeanList.get(position).getPet_handle().trim();

                if(isChecked){
                    if (holder.chx_spectypes.isChecked()) {
                        petHandledTypeCheckedListener.onItemPetCheck(position,chpetshandled,petHandleBeanList);
                    }

                }else{

                    petHandledTypeCheckedListener.onItemPetUnCheck(position,chpetshandled);

                }

            }
        });



    }
    @Override
    public int getItemCount() {
        return petHandleBeanList.size();
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

            txt_spectypes = itemView.findViewById(R.id.pet_name);

            chx_spectypes = itemView.findViewById(R.id.checkbox_pet_type);


        }

    }

}
