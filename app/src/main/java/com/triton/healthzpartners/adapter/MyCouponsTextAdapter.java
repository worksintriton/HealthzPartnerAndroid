package com.triton.healthzpartners.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.triton.healthzpartners.R;
import com.triton.healthzpartners.api.APIClient;
import com.triton.healthzpartners.interfaces.OnAppointmentSuccessfullyCancel;
import com.triton.healthzpartners.responsepojo.CouponCodeTextResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class MyCouponsTextAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  String TAG = "MyCouponsTextAdapter";
    private Context context;
    private String ServiceCost;

    CouponCodeTextResponse.DataBean currentItem;
    private List<CouponCodeTextResponse.DataBean> myCouponsTextList;



    OnAppointmentSuccessfullyCancel onAppointmentSuccessfullyCancel;



    public MyCouponsTextAdapter(Context context, List<CouponCodeTextResponse.DataBean> myCouponsTextList,String ServiceCost, OnAppointmentSuccessfullyCancel onAppointmentSuccessfullyCancel) {
        this.context = context;
        this.myCouponsTextList = myCouponsTextList;
        this.ServiceCost = ServiceCost;
        this.onAppointmentSuccessfullyCancel = onAppointmentSuccessfullyCancel;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_mycoupons_text_cardview, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        initLayoutOne((ViewHolderOne) holder, position);


    }

  @SuppressLint("SetTextI18n")
  private void initLayoutOne(ViewHolderOne holder, final int position) {

        Log.w(TAG,"ServiceCost : "+ServiceCost);
        currentItem = myCouponsTextList.get(position);
        if(currentItem.getTitle() != null) {
            holder.txt_notification_title.setText(currentItem.getTitle());
        }
        if(currentItem.getDescri() != null){
            holder.txt_message.setText(currentItem.getDescri());

        }

      @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMhhmmss");
      String currentDateandTime = simpleDateFormat.format(new Date());

        if(currentItem.getRefund() != null && !currentItem.getRefund().isEmpty()) {
            holder.ll_referralcode.setVisibility(View.VISIBLE);
            holder.txt_refund.setText("REF"+currentDateandTime);
        }else{
            holder.ll_referralcode.setVisibility(View.GONE);
        }
        if (currentItem.getImage() != null && !currentItem.getImage().isEmpty()) {

            Glide.with(context)
                    .load(currentItem.getImage())
                    //.load(R.drawable.logo)
                    .into(holder.img_notify_imge);

        }
        else{
            Glide.with(context)
                    .load(APIClient.PROFILE_IMAGE_URL)
                    .into(holder.img_notify_imge);

        }
        holder.ll_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.ll_root.setClickable(false);
                holder.ll_root.setBackgroundResource(R.drawable.user_type_bgm);
                onAppointmentSuccessfullyCancel.onAppointmentSuccessfullyCancel(myCouponsTextList.get(position).getRefund(),ServiceCost);

            }




        });



   }

   @Override
    public int getItemCount() {
        
        return myCouponsTextList.size();
    }
   

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    class ViewHolderOne extends RecyclerView.ViewHolder {
        public TextView txt_message,txt_notification_title,txt_refund;
        public LinearLayout ll_root,ll_referralcode;
        public ImageView img_notify_imge;



        public ViewHolderOne(View itemView) {
            super(itemView);

            txt_notification_title = itemView.findViewById(R.id.txt_notification_title);
            txt_message = itemView.findViewById(R.id.txt_message);
            txt_refund = itemView.findViewById(R.id.txt_refund);
            img_notify_imge = itemView.findViewById(R.id.img_notify_imge);
            ll_root = itemView.findViewById(R.id.ll_root);
            ll_referralcode = itemView.findViewById(R.id.ll_referralcode);
            ll_referralcode.setVisibility(View.GONE);


        }

    }

}
