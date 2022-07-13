package com.triton.healthzpartners.adapter;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.triton.healthzpartners.R;
import com.triton.healthzpartners.requestpojo.ProductVendorEditRequest;

import java.util.List;

public class EditImageListAdapter extends RecyclerView.Adapter<EditImageListAdapter.AddImageListHolder> {
    private String TAG = "EditImageListAdapter";
    Context context;
    List<ProductVendorEditRequest.ProductImgBean> pet_imgList;
    View view;

    public EditImageListAdapter(Context context, List<ProductVendorEditRequest.ProductImgBean> pet_imgList) {
        this.context = context;
        this.pet_imgList = pet_imgList;


    }

    @NonNull
    @Override
    public AddImageListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_imgupload, parent, false);

        return new AddImageListHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AddImageListHolder holder, final int position) {
        final ProductVendorEditRequest.ProductImgBean petImgBean = pet_imgList.get(position);

        Log.w(TAG,"ImagePic : "+petImgBean.getProduct_img());

        if (petImgBean.getProduct_img()!= null) {
            Glide.with(context)
                    .load(petImgBean.getProduct_img())
                    .into(holder.certificate_pics_1);

        }

        holder.removeImg.setOnClickListener(view -> {
            pet_imgList.remove(position);
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return pet_imgList.size();
    }

    public static class AddImageListHolder extends RecyclerView.ViewHolder {
        ImageView removeImg,certificate_pics_1;
        public AddImageListHolder(View itemView) {
            super(itemView);
            certificate_pics_1 = itemView.findViewById(R.id.certificate_pics_1);
            removeImg = itemView.findViewById(R.id.close);
        }
    }


}