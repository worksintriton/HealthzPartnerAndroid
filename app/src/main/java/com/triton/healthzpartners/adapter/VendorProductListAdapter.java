package com.triton.healthzpartners.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.triton.healthzpartners.R;
import com.triton.healthzpartners.api.APIClient;

import com.triton.healthzpartners.interfaces.ProductDeleteListener;
import com.triton.healthzpartners.responsepojo.ManageProductsListResponse;
import com.triton.healthzpartners.vendor.EditManageProdcutsActivity;
import com.triton.healthzpartners.vendor.VendorAddProductsActivity;

import java.util.ArrayList;
import java.util.List;


public class VendorProductListAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String TAG = "VendorProductListAdapter";

    private final Context context;

    ManageProductsListResponse.DataBean currentItem;
    private ProductDeleteListener productDeleteListener;


    List<ManageProductsListResponse.DataBean> productList;
    private List<ManageProductsListResponse.DataBean.ProductImgBean> picBeanList;
    private ArrayList<ManageProductsListResponse.DataBean.ProductImgBean> productImageList;
    private ArrayList<String> additionalDetailList;


    public VendorProductListAdapter(Context context,  List<ManageProductsListResponse.DataBean> productList,ProductDeleteListener productDeleteListener) {
        this.context = context;
        this.productList = productList;
        this.productDeleteListener = productDeleteListener;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_vendor_product_list, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        initLayoutOne((ViewHolderOne) holder, position);


    }

    @SuppressLint({"SetTextI18n", "LogNotTimber"})
    private void initLayoutOne(ViewHolderOne holder, final int position) {

        currentItem = productList.get(position);

        if (productList.get(position).getProduct_name() != null) {
            holder.txt_products_title.setText(productList.get(position).getProduct_name());
        }
        else {

            holder.txt_products_title.setVisibility(View.GONE);
        }

        if (productList.get(position).getCost() != 0) {
            holder.txt_products_price.setText("\u20B9 "+productList.get(position).getCost());
        }
        else {

            holder.txt_products_price.setText("\u20B9 0");
        }


        Log.w(TAG,"getPet_threshold : "+productList.get(position).getThreshould());
        if (productList.get(position).getThreshould() != null) {
            holder.txt_products_quantity.setText("Quantity -  "+productList.get(position).getThreshould());
        }
        else {

            holder.txt_products_quantity.setText("Quantity : 0");
        }



        if (productList.size() > 0) {

            Log.w(TAG,"dataBeanList : "+new Gson().toJson(productList));


            picBeanList =   productList.get(position).getProduct_img();

            String petImagePath = null;


            Log.w(TAG,"picBeanList : "+new Gson().toJson(picBeanList));

            if(picBeanList != null && picBeanList.size() > 0) {

                for (int j=0;j<picBeanList.size();j++) {

                    petImagePath= picBeanList.get(j).getProduct_img();

                }
            }





            Log.w(TAG,"petImagePath : "+petImagePath);

            if (productList.get(position).getThumbnail_image() != null && !productList.get(position).getThumbnail_image().isEmpty()) {
                Glide.with(context)
                        .load(productList.get(position).getThumbnail_image())
                        .into(holder.img_pet_imge);
            }else {
                Glide.with(context)
                        .load(APIClient.PROFILE_IMAGE_URL)
                        .into(holder.img_pet_imge);

            }


        }


        holder.ll_main_root.setOnClickListener(v -> {
            Intent i = new Intent(context, VendorAddProductsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        });

        holder.ll_delete.setOnClickListener(v -> {
            productDeleteListener.productDeleteListener(productList.get(position).get_id());

        });

        holder.ll_edit.setOnClickListener(v -> {
           productImageList= productList.get(position).getProduct_img();
           additionalDetailList= productList.get(position).getAddition_detail();
            Intent i = new Intent(context, EditManageProdcutsActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("productid",productList.get(position).get_id());
            i.putExtra("producttitle",productList.get(position).getProduct_name());
            i.putExtra("productprice",productList.get(position).getCost());
            i.putExtra("productthreshold",productList.get(position).getThreshould());
            i.putExtra("productdesc",productList.get(position).getProduct_discription());
            i.putExtra("productImageList",productImageList);
            i.putExtra("productcategoryname",productList.get(position).getCat_id().getProduct_cate());
            i.putExtra("condition",productList.get(position).getCondition());
            i.putExtra("pricetype",productList.get(position).getPrice_type());
            i.putExtra("additionalDetailList",additionalDetailList);
            context.startActivity(i);

        });


        //closing the setOnClickListener method

        if(position == productList.size()-1){
            holder.ll_main_root.setVisibility(View.VISIBLE);
        }


    }









    @Override
    public int getItemCount() {
        return productList.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class ViewHolderOne extends RecyclerView.ViewHolder {
        public TextView txt_deal_status,txt_products_price,txt_products_quantity,txt_products_title;
        public ImageView img_pet_imge,img_settings;
        public RelativeLayout ll_main_root;
        public LinearLayout ll_edit,ll_delete;


        public ViewHolderOne(View itemView) {
            super(itemView);
            img_pet_imge = itemView.findViewById(R.id.img_products_image);
            txt_products_title= itemView.findViewById(R.id.txt_products_title);
            txt_products_price = itemView.findViewById(R.id.txt_products_price);
            txt_products_quantity = itemView.findViewById(R.id.txt_products_quantity);
            txt_deal_status = itemView.findViewById(R.id.txt_deal_status);
            ll_main_root = itemView.findViewById(R.id.ll_main_root);
            ll_main_root.setVisibility(View.GONE);
            ll_edit = itemView.findViewById(R.id.ll_edit);
            ll_delete = itemView.findViewById(R.id.ll_delete);




        }




    }







}
