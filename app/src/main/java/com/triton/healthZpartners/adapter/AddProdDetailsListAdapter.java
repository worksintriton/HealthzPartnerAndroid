package com.triton.healthZpartners.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.triton.healthZpartners.R;
import com.triton.healthZpartners.requestpojo.DocBusInfoUploadRequest;

import java.util.List;

public class AddProdDetailsListAdapter extends RecyclerView.Adapter<AddProdDetailsListAdapter.AddExpViewHolder> {
    Context context;
    List<String> stringList;
    View view;

    public AddProdDetailsListAdapter(Context context, List<String> stringList) {
        this.context = context;
        this.stringList = stringList;

    }

    @NonNull
    @Override
    public AddExpViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_prod_added_details, parent, false);
        return new AddExpViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddExpViewHolder holder, final int position) {

        if (stringList.get(position)!= null) {
            holder.added_details.setText(stringList.get(position));
        }

        holder.removeImg.setOnClickListener(view -> {
            stringList.remove(position);
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public static class AddExpViewHolder extends RecyclerView.ViewHolder {
        TextView added_details;
        ImageView removeImg;
        public AddExpViewHolder(View itemView) {
            super(itemView);
            added_details = itemView.findViewById(R.id.added_details);
            removeImg = itemView.findViewById(R.id.close);


        }
    }
}