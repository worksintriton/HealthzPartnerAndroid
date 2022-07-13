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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class VendorAddCertPdfAdapter extends RecyclerView.Adapter<VendorAddCertPdfAdapter.AddImageListHolder> {
    Context context;
    List<VendorRegisterFormCreateRequest.CertifiBean> bus_certif_list;
    View view;
    String extension;
    URL url;
    public VendorAddCertPdfAdapter(Context context, List<VendorRegisterFormCreateRequest.CertifiBean> bus_certif_list) {
        this.context = context;
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
        final VendorRegisterFormCreateRequest.CertifiBean certificatePicBean = bus_certif_list.get(position);

        Log.w("cert_link",certificatePicBean.getCertifi());

        try {
            url = new URL(certificatePicBean.getCertifi());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.w("url", String.valueOf(url));

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
                            .into(holder.certificate_pics_1);

                }else {

                    holder.certificate_pics_1.setImageResource(R.drawable.pdf_icon);
                }
            } else {

                holder.material_cardview_education_details.setVisibility(View.VISIBLE);
            }

        }
        else {

            holder.material_cardview_education_details.setVisibility(View.VISIBLE);
        }

        holder.removeImg.setOnClickListener(view -> {
            bus_certif_list.remove(position);
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return bus_certif_list.size();
    }

    public static class AddImageListHolder extends RecyclerView.ViewHolder {
        ImageView removeImg,certificate_pics_1;
        CardView material_cardview_education_details;
        public AddImageListHolder(View itemView) {
            super(itemView);
            certificate_pics_1 = itemView.findViewById(R.id.pdf_file);
            material_cardview_education_details = itemView.findViewById(R.id.material_cardview_education_details);
            removeImg = itemView.findViewById(R.id.close);
        }
    }


}