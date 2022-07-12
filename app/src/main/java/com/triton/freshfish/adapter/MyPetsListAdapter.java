package com.triton.freshfish.adapter;

import android.annotation.SuppressLint;
import android.content.Context;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.triton.freshfish.R;
import com.triton.freshfish.api.APIClient;
import com.triton.freshfish.interfaces.MyPetsSelectListener;

import com.triton.freshfish.responsepojo.FamilyMemberListResponse;

import java.util.List;


public class MyPetsListAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<FamilyMemberListResponse.DataBean> petListResponseList;

    private final Context context;

    FamilyMemberListResponse.DataBean currentItem;

    MyPetsSelectListener myPetsSelectListener;


    public static String id = "";
    private List<FamilyMemberListResponse.DataBean.PicBean> petImgBeanList;
    private String TAG = "MyPetsListAdapter";



    public MyPetsListAdapter(Context context, List<FamilyMemberListResponse.DataBean> petListResponseList,  MyPetsSelectListener myPetsSelectListener) {
        this.petListResponseList = petListResponseList;
        this.context = context;
        this.myPetsSelectListener = myPetsSelectListener;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_my_pets_list, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        initLayoutOne((ViewHolderOne) holder, position);


    }

    @SuppressLint({"SetTextI18n", "LogNotTimber"})
    private void initLayoutOne(ViewHolderOne holder, final int position) {
        currentItem = petListResponseList.get(position);
        if (petListResponseList.get(position).getName() != null) {
                holder.txt_pet_name.setText(petListResponseList.get(position).getName());
        }

        if(petListResponseList.size() > 0) {
            Log.w(TAG,"petListResponseList : "+new Gson().toJson(petListResponseList));
            petImgBeanList =   petListResponseList.get(position).getPic();
            String petImagePath = null;
            Log.w(TAG,"petImgBeanList : "+new Gson().toJson(petImgBeanList));
            if (petImgBeanList != null && petImgBeanList.size() > 0) {
                for(int j=0;j<petImgBeanList.size();j++) {
                    petImagePath = petImgBeanList.get(j).getImage();

                }
            }




            Log.w(TAG,"petImagePath : "+petImagePath);

            if (petImagePath != null && !petImagePath.isEmpty()) {
                Glide.with(context)
                        .load(petImagePath)
                        .into(holder.img_pet_imge);
            }else {
                Glide.with(context)
                        .load(APIClient.PROFILE_IMAGE_URL)
                        .into(holder.img_pet_imge);

            }


        }
        holder.ll_pet_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.w(TAG,"ll_pet_profile onclick : "+petListResponseList.get(position).getPic().get(0).getImage());


                String petImagePath = null;

                for (int i=0;i<petListResponseList.size();i++){
                    petListResponseList.get(i).setSelected(false);

                }
                petListResponseList.get(position).setSelected(true);
                notifyDataSetChanged();
                if(petListResponseList.get(position).get_id() != null ){
                    if(petListResponseList.size() > 0) {
                        Log.w(TAG,"petListResponseList : "+new Gson().toJson(petListResponseList));
                        petImgBeanList =   petListResponseList.get(position).getPic();

                        Log.w(TAG,"petImgBeanList : "+new Gson().toJson(petImgBeanList));
                        if (petImgBeanList != null && petImgBeanList.size() > 0) {
                            for(int j=0;j<petImgBeanList.size();j++) {
                                petImagePath = petImgBeanList.get(j).getImage();
                                Log.w(TAG,"petImagePath lop : "+petImagePath);



                            }

                        }





                    }
                    myPetsSelectListener.myPetsSelectListener(petListResponseList.get(position).get_id(),petListResponseList.get(position).getName(),petImagePath);


                }
            }
        });
        if (petListResponseList.get(position).isSelected()) {
            Log.w(TAG, "IF isSelected--->");
            holder.ll_pet_profile.setBackgroundResource(R.drawable.user_type_bgm);
            holder.chx_usertypes.setVisibility(View.VISIBLE);
            holder.chx_usertypes.setChecked(true);
            return;

        }
        else {
            Log.w(TAG, "ELSE isSelected--->");
            holder.ll_pet_profile.setBackgroundResource(R.drawable.user_bgm_trans);
            holder.chx_usertypes.setVisibility(View.INVISIBLE);
            holder.chx_usertypes.setChecked(false);

        }



    }









    @Override
    public int getItemCount() {
        return petListResponseList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class ViewHolderOne extends RecyclerView.ViewHolder {
        public TextView txt_pet_name;
        public ImageView img_pet_imge;
        public LinearLayout ll_pet_profile;
        public CheckBox chx_usertypes;


        public ViewHolderOne(View itemView) {
            super(itemView);
            img_pet_imge = itemView.findViewById(R.id.img_pet_imge);
            txt_pet_name = itemView.findViewById(R.id.txt_pet_name);
            ll_pet_profile = itemView.findViewById(R.id.ll_pet_profile);
            chx_usertypes = itemView.findViewById(R.id.chx_usertypes);
            chx_usertypes.setClickable(false);


        }




    }







}