package com.triton.healthZpartners.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.triton.healthZpartners.R;
import com.triton.healthZpartners.customer.ListOfProductsSeeMoreActivity;
import com.triton.healthZpartners.responsepojo.ShopDashboardResponse;

import java.util.List;


public class PetLoverCateAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  String TAG = "PetLoverCateAdapter";

    private Context context;





    List<ShopDashboardResponse.DataBean.ProductCateBean> product_cate;
    ShopDashboardResponse.DataBean.ProductCateBean currentItem;


    int size;

    public PetLoverCateAdapter(Context context,   List<ShopDashboardResponse.DataBean.ProductCateBean> product_cate, int size) {
        this.product_cate = product_cate;
        this.context = context;
        this.size = size;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_category_services, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        initLayoutOne((ViewHolderOne) holder, position);

    }

    @SuppressLint("SetTextI18n")
    private void initLayoutOne(ViewHolderOne holder, final int position) {
          currentItem = product_cate.get(position);
          if(currentItem.getProduct_cate() != null) {
              holder.txt_petlover_servicesname.setText(currentItem.getProduct_cate());
          }
          if (currentItem.getImg_path() != null && !currentItem.getImg_path().isEmpty()) {

            Glide.with(context)
                    .load(currentItem.getImg_path())
                    //.load(R.drawable.logo)
                    .into(holder.cv_serviceimage);

           }
          else{
            Glide.with(context)
                    .load(R.drawable.image_thumbnail)
                    .into(holder.cv_serviceimage);

        }

         /* if(currentItem.getBackground_color() != null) {
              int color = Color.parseColor(currentItem.getBackground_color());
              Drawable unwrappedDrawable = AppCompatResources.getDrawable(context, R.drawable.layout_bg_service);
              Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
              DrawableCompat.setTint(wrappedDrawable, color);
              holder.ll_root.setBackgroundResource(R.drawable.layout_bg_service);


          }*/


        holder.ll_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(product_cate.get(position).get_id() != null) {
                    Intent intent = new Intent(context, ListOfProductsSeeMoreActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("cat_id", product_cate.get(position).get_id());
                    context.startActivity(intent);
                }
                }




        });

    }












    @Override
    public int getItemCount() {
        return Math.min(product_cate.size(), size);



    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class ViewHolderOne extends RecyclerView.ViewHolder {
        public TextView txt_petlover_servicesname;
        public RelativeLayout ll_root;
        public ImageView cv_serviceimage;




        public ViewHolderOne(View itemView) {
            super(itemView);
            txt_petlover_servicesname = itemView.findViewById(R.id.txt_petlover_servicesname);
            cv_serviceimage = itemView.findViewById(R.id.cv_serviceimage);
            ll_root = itemView.findViewById(R.id.ll_root);



        }




    }










}
