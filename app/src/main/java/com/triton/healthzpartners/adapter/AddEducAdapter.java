package com.triton.healthzpartners.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.triton.healthzpartners.R;
import com.triton.healthzpartners.requestpojo.DocBusInfoUploadRequest;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class AddEducAdapter extends RecyclerView.Adapter<AddEducAdapter.AddEduViewHolder> {
    Context context;
    List<DocBusInfoUploadRequest.EducationDetailsBean> educationDetailsBeans;
    View view;

    public AddEducAdapter(Context context, List<DocBusInfoUploadRequest.EducationDetailsBean> educationDetailsBeans) {
        this.context = context;
        this.educationDetailsBeans = educationDetailsBeans;

    }

    @NonNull
    @Override
    public AddEduViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_education_details_model, parent, false);
        return new AddEduViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddEduViewHolder holder, final int position) {
        final DocBusInfoUploadRequest.EducationDetailsBean educationDetailsBean = educationDetailsBeans.get(position);
        if (educationDetailsBean.getEducation()!= null) {
            holder.eduName.setText(educationDetailsBean.getEducation());
        }

        if (educationDetailsBean.getYear()!= null) {
            holder.eduyr.setText(educationDetailsBean.getYear());
        }

        holder.removeImg.setOnClickListener(view -> {
            educationDetailsBeans.remove(position);
            notifyDataSetChanged();
            Toasty.warning(context,"Removed Successfully", Toast.LENGTH_SHORT).show();

        });

    }

    @Override
    public int getItemCount() {
        return educationDetailsBeans.size();
    }

    public static class AddEduViewHolder extends RecyclerView.ViewHolder {
        TextView eduName,eduyr;
        ImageView removeImg;
        public AddEduViewHolder(View itemView) {
            super(itemView);
            eduName = itemView.findViewById(R.id.educ_name);
            removeImg = itemView.findViewById(R.id.close);
            eduyr = itemView.findViewById(R.id.edu_yr);
        }
    }
}