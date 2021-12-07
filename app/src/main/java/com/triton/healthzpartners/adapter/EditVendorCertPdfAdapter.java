package com.triton.healthzpartners.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.triton.healthzpartners.R;
import com.triton.healthzpartners.requestpojo.VendorRegisterFormCreateRequest;
import com.triton.healthzpartners.responsepojo.VendorGetsOrderIDResponse;

import java.util.ArrayList;
import java.util.List;

public class EditVendorCertPdfAdapter extends RecyclerView.Adapter<EditVendorCertPdfAdapter.AddImageListHolder> {
    Context context;
    private List<VendorGetsOrderIDResponse.DataBean.CertifiBean> bus_certif_list_edit;
    List<VendorRegisterFormCreateRequest.CertifiBean> bus_certif_list = new ArrayList<>();

    View view;
    String extension;
    public EditVendorCertPdfAdapter(Context context,List<VendorGetsOrderIDResponse.DataBean.CertifiBean> bus_certif_list_edit, List<VendorRegisterFormCreateRequest.CertifiBean> bus_certif_list) {
        this.context = context;
        this.bus_certif_list_edit = bus_certif_list_edit;
        this.bus_certif_list = bus_certif_list;

    }

    @NonNull
    @Override
    public AddImageListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pdf_upload, parent, false);
        return new AddImageListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddImageListHolder holder, final int position) {
        final VendorGetsOrderIDResponse.DataBean.CertifiBean certificatePicBean = bus_certif_list_edit.get(position);
      /*  if (certificatePicBean.getCertifi()!= null) {


        }
*/
        if (certificatePicBean.getCertifi()!= null) {

            String uri = certificatePicBean.getCertifi();

            if(uri.contains(".")) {

                extension = uri.substring(uri.lastIndexOf("."));

                Log.w("extension",extension);
            }
            if(extension != null && !extension.isEmpty()) {

                if (extension.equals(".png") || extension.equals(".jpg") || (extension.equals(".jpeg"))) {


                    Glide.with(context)
                            .load(certificatePicBean.getCertifi())
                            .into(holder.pdf_file);

                } else {

                    holder.pdf_file.setImageResource(R.drawable.pdf_icon);
                }

            }

            else {

                holder.material_cardview_education_details.setVisibility(View.VISIBLE);
            }

        }

        else {

            holder.material_cardview_education_details.setVisibility(View.VISIBLE);
        }

        holder.removeImg.setOnClickListener(view -> {
            bus_certif_list_edit.remove(position);
            bus_certif_list.remove(position);
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return bus_certif_list_edit.size();
    }

    public static class AddImageListHolder extends RecyclerView.ViewHolder {
        ImageView removeImg,pdf_file;
        CardView material_cardview_education_details;
        public AddImageListHolder(View itemView) {
            super(itemView);
            pdf_file = itemView.findViewById(R.id.pdf_file);
            removeImg = itemView.findViewById(R.id.close);
            material_cardview_education_details = itemView.findViewById(R.id.material_cardview_education_details);
        }
    }


}