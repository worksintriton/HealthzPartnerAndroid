package com.triton.healthZpartners.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.triton.healthZpartners.R;
import com.triton.healthZpartners.interfaces.CategoryTypeSelectListener;
import com.triton.healthZpartners.interfaces.PetBreedTypeSelectListener;
import com.triton.healthZpartners.responsepojo.BreedTypeResponse;
import com.triton.healthZpartners.responsepojo.CatgoryGetListResponse;

import java.util.List;


public class CategoryTypesListAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  String TAG = "CategoryTypesListAdapter";
    private Context context;
    CatgoryGetListResponse.DataBean currentItem;
    List<CatgoryGetListResponse.DataBean> catgoryGetList;
    private CategoryTypeSelectListener categoryTypeSelectListener;




    public CategoryTypesListAdapter(Context context,  List<CatgoryGetListResponse.DataBean> catgoryGetList, CategoryTypeSelectListener categoryTypeSelectListener ) {
        this.context = context;
        this.catgoryGetList = catgoryGetList;
        this.categoryTypeSelectListener = categoryTypeSelectListener;


    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_categorytype_cardview, parent, false);
        return new ViewHolderOne(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        initLayoutOne((ViewHolderOne) holder, position);


    }

    @SuppressLint("LogNotTimber")
    private void initLayoutOne(ViewHolderOne holder, final int position) {
        currentItem = catgoryGetList.get(position);

        if(currentItem.getProduct_cate_name() != null){
            holder.txt_breedtype.setText(currentItem.getProduct_cate_name());

        }

        holder.ll_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (int i=0;i<catgoryGetList.size();i++){
                    catgoryGetList.get(i).setSelected(false);
                }
                catgoryGetList.get(position).setSelected(true);
                notifyDataSetChanged();
                if(catgoryGetList.get(position).getProduct_cate_name() != null && catgoryGetList.get(position).get_id() != null){
                    categoryTypeSelectListener.categoryTypeSelectListener(catgoryGetList.get(position).getProduct_cate_name(),catgoryGetList.get(position).get_id());

                }
            }
        });



        if (catgoryGetList.get(position).isSelected()) {
            Log.w(TAG, "IF isSelected--->");
            holder.ll_root.setBackgroundResource(R.drawable.selected_background);
            holder.txt_breedtype.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            Log.w(TAG, "ELSE isSelected--->");
            holder.ll_root.setBackgroundResource(R.drawable.user_bgm_trans);
            holder.txt_breedtype.setTextColor(ContextCompat.getColor(context, R.color.black));



        }



    }
    @Override
    public int getItemCount() {
        return catgoryGetList.size();
    }






    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void filterList(List<CatgoryGetListResponse.DataBean> catgoryGetListFiltered) {
        catgoryGetList = catgoryGetListFiltered;
        Log.w(TAG,"catgoryGetList : "+new Gson().toJson(catgoryGetList));

        notifyDataSetChanged();
    }



    static class ViewHolderOne extends RecyclerView.ViewHolder {
        public TextView txt_breedtype;
        public LinearLayout ll_root;



        public ViewHolderOne(View itemView) {
            super(itemView);

            txt_breedtype = itemView.findViewById(R.id.txt_breedtype);
            ll_root = itemView.findViewById(R.id.ll_root);










        }




    }

















}
