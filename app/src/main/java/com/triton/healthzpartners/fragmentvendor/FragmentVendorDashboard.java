package com.triton.healthzpartners.fragmentvendor;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.triton.healthzpartners.R;
import com.triton.healthzpartners.adapter.VendorProductListAdapter;
import com.triton.healthzpartners.api.APIClient;
import com.triton.healthzpartners.api.RestApiInterface;
import com.triton.healthzpartners.interfaces.ProductDeleteListener;
import com.triton.healthzpartners.requestpojo.ManageProductsListRequest;
import com.triton.healthzpartners.requestpojo.ProductVendorEditRequest;
import com.triton.healthzpartners.requestpojo.SPCheckStatusRequest;
import com.triton.healthzpartners.requestpojo.VendorGetsOrderIdRequest;
import com.triton.healthzpartners.responsepojo.ManageProductsListResponse;
import com.triton.healthzpartners.responsepojo.SPCheckStatusResponse;
import com.triton.healthzpartners.responsepojo.SuccessResponse;
import com.triton.healthzpartners.responsepojo.VendorGetsOrderIDResponse;
import com.triton.healthzpartners.sessionmanager.SessionManager;
import com.triton.healthzpartners.utils.ConnectionDetector;
import com.triton.healthzpartners.utils.RestUtils;
import com.triton.healthzpartners.vendor.VendorAddProductsActivity;
import com.triton.healthzpartners.vendor.VendorDashboardActivity;
import com.triton.healthzpartners.vendor.VendorEditProfileActivity;
import com.triton.healthzpartners.vendor.VendorProfileScreenActivity;
import com.triton.healthzpartners.vendor.VendorRegisterFormActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentVendorDashboard extends Fragment implements ProductDeleteListener {

    private   String TAG = "FragmentVendorDashboard";


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.avi_indicator)
    AVLoadingIndicatorView avi_indicator;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_search)
    EditText edt_search;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_view_profile)
    TextView txt_view_profile;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_edit_profile)
    TextView txt_edit_profile;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_remove_store)
    TextView txt_remove_store;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_business_name)
    TextView txt_business_name;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_service_pic)
    ImageView img_service_pic;

    private SharedPreferences preferences;
    private Context mContext;
    private String userid;
    private boolean isDoctorStatus = false;
    private boolean isProfileUpdatedClose;

    SessionManager session;

    FragmentManager  childFragMang;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_productList)
    RecyclerView rv_productList;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_no_records)
    TextView txt_no_records;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_add_product)
    TextView txt_add_product;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_lbl_products)
    TextView txt_lbl_products;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rl_category)
    RelativeLayout rl_category;

    String searchString="";

    private int someIndex = 0;
    private List<VendorGetsOrderIDResponse.DataBean.BussinessGalleryBean> businessgalerydetailsResponseList;

    private Dialog dialog;
    private List<ManageProductsListResponse.DataBean> productList;


    public FragmentVendorDashboard() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vendor_dashboard, container, false);


        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        ButterKnife.bind(this, view);
        mContext = getActivity();
        avi_indicator.setVisibility(View.GONE);

         session = new SessionManager(mContext);
        HashMap<String, String> user = session.getProfileDetails();
        userid = user.get(SessionManager.KEY_ID);
        Log.w(TAG,"userid : "+userid);



        if(userid != null){
            if (new ConnectionDetector(getActivity()).isNetworkAvailable(getActivity())) {
                VendorCheckStatusResponseCall();
            }
        }


        txt_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, VendorEditProfileActivity.class));
            }
        });
        txt_view_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, VendorProfileScreenActivity.class));
            }
        });

        edt_search.addTextChangedListener(new TextWatcher() {
            @SuppressLint("LogNotTimber")
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.w(TAG,"beforeTextChanged-->"+s.toString());
            }

            @SuppressLint("LogNotTimber")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.w(TAG,"onTextChanged-->"+s.toString());
                searchString = s.toString();


            }

            @SuppressLint("LogNotTimber")
            @Override
            public void afterTextChanged(Editable s) {
                Log.w(TAG,"afterTextChanged-->"+s.toString());
                searchString = s.toString();
                if(!searchString.isEmpty()){
                    if (new ConnectionDetector(getContext()).isNetworkAvailable(getContext())) {
                        getlist_from_vendor_id_ResponseCall();
                    }
                }else{
                    searchString ="";
                    if (new ConnectionDetector(getContext()).isNetworkAvailable(getContext())) {
                        getlist_from_vendor_id_ResponseCall();
                    }

                }

            }
        });


        return view;


    }









    @SuppressLint("LogNotTimber")
    private void VendorCheckStatusResponseCall() {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<SPCheckStatusResponse> call = apiInterface.VendorCheckStatusResponseCall(RestUtils.getContentType(), spCheckStatusRequest());
        Log.w(TAG,"VendorCheckStatusResponseCall url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<SPCheckStatusResponse>() {
            @Override
            public void onResponse(@NonNull Call<SPCheckStatusResponse> call, @NonNull Response<SPCheckStatusResponse> response) {

                Log.w(TAG,"VendorCheckStatusResponseCall"+ "--->" + new Gson().toJson(response.body()));

                avi_indicator.smoothToHide();

                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        if(response.body().getData() != null){
                            if(!response.body().getData().isProfile_status()){
                                Intent intent = new Intent(mContext, VendorRegisterFormActivity.class);
                                intent.putExtra("fromactivity",TAG);
                                startActivity(intent);
                            }
                            else{
                                String profileVerificationStatus = response.body().getData().getProfile_verification_status();
                                if( profileVerificationStatus != null && profileVerificationStatus.equalsIgnoreCase("Not verified")){
                                    showProfileStatus(response.body().getMessage());

                                }else if( profileVerificationStatus != null && profileVerificationStatus.equalsIgnoreCase("profile updated")){
                                    if(!session.isProfileUpdate()){
                                        showProfileUpdateStatus(response.body().getMessage());

                                    }


                                }else{
                                    isDoctorStatus = true;

                                    getVendorOrderIDResponseCall();

                                    if (new ConnectionDetector(getContext()).isNetworkAvailable(getContext())) {
                                        getlist_from_vendor_id_ResponseCall();
                                    }


                                    Log.w(TAG,"isDoctorStatus else : "+isDoctorStatus);
                                    Log.w(TAG,"isDoctorStatus orders : "+VendorDashboardActivity.orders );

                                    if(isDoctorStatus){
                                     /*   if(viewPager != null) {
                                           // setupViewPager(viewPager);
                                            if(VendorDashboardActivity.orders != null && VendorDashboardActivity.orders.equalsIgnoreCase("New")){
                                                someIndex = 0;
                                            }
                                            else if(VendorDashboardActivity.orders != null && VendorDashboardActivity.orders.equalsIgnoreCase("Completed")){
                                                someIndex = 1;
                                            }
                                            else if(VendorDashboardActivity.orders != null && VendorDashboardActivity.orders.equalsIgnoreCase("Cancelled")){
                                                someIndex = 2;
                                            }

                                            tablayout.setupWithViewPager(viewPager);
                                            TabLayout.Tab tab = tablayout.getTabAt(someIndex);
                                            if (tab != null) {
                                                tab.select();
                                            }

                                        }*/
                                    }

                                }


                            }

                        }

                    }

                }


            }

            @Override
            public void onFailure(@NonNull Call<SPCheckStatusResponse> call, @NonNull Throwable t) {

                avi_indicator.smoothToHide();
                Log.w(TAG,"VendorCheckStatusResponseCall flr"+"--->" + t.getMessage());
            }
        });

    }
    private SPCheckStatusRequest spCheckStatusRequest() {
        SPCheckStatusRequest spCheckStatusRequest = new SPCheckStatusRequest();
        spCheckStatusRequest.setUser_id(userid);
        Log.w(TAG,"spCheckStatusRequest"+ "--->" + new Gson().toJson(spCheckStatusRequest));
        return spCheckStatusRequest;
    }
    private void showProfileStatus(String message) {

        try {

            Dialog dialog = new Dialog(mContext);
            dialog.setContentView(R.layout.alert_no_internet_layout);
            dialog.setCancelable(false);
            Button dialogButton = dialog.findViewById(R.id.btnDialogOk);
            dialogButton.setText("Refresh");
            TextView tvInternetNotConnected = dialog.findViewById(R.id.tvInternetNotConnected);
            tvInternetNotConnected.setText(message);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (new ConnectionDetector(getActivity()).isNetworkAvailable(getActivity())) {
                        VendorCheckStatusResponseCall();
                    }
                    dialog.dismiss();

                }
            });
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }




    }
    private void showProfileUpdateStatus(String message) {

        try {

            Dialog dialog = new Dialog(mContext);
            dialog.setContentView(R.layout.alert_profile_update_layout);
            dialog.setCancelable(false);
            Button dialogButton = dialog.findViewById(R.id.btnDialogOk);
            dialogButton.setText("Refresh");
            TextView tvInternetNotConnected = dialog.findViewById(R.id.tvInternetNotConnected);
            tvInternetNotConnected.setText(message);
            ImageView img_close = dialog.findViewById(R.id.img_close);

            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (new ConnectionDetector(getActivity()).isNetworkAvailable(getActivity())) {
                        VendorCheckStatusResponseCall();
                    }
                    dialog.dismiss();

                }
            });

            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    session.setIsProfileUpdate(true);
                    isProfileUpdatedClose = true;
                    dialog.dismiss();

                }
            });
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }




    }


    private void getVendorOrderIDResponseCall() {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<VendorGetsOrderIDResponse> call = apiInterface.vendor_gets_orderbyId_ResponseCall(RestUtils.getContentType(), vendorGetsOrderIdRequest());
        Log.w(TAG,"getVendorOrderIDResponseCall url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<VendorGetsOrderIDResponse>() {
            @SuppressLint({"LongLogTag", "LogNotTimber"})
            @Override
            public void onResponse(@NonNull Call<VendorGetsOrderIDResponse> call, @NonNull Response<VendorGetsOrderIDResponse> response) {

                Log.w(TAG,"getVendorOrderIDResponseCall"+ "--->" + new Gson().toJson(response.body()));

                avi_indicator.smoothToHide();

                if (response.body() != null) {
                    if(response.body().getCode() == 200){

                        if(response.body().getData() != null) {
                                if (response.body().getData().getBussiness_name() != null) {
                                    txt_business_name.setText(response.body().getData().getBussiness_name());
                                }else{
                                    txt_business_name.setText("");
                                }
                                if (response.body().getData().getBussiness_gallery() != null) {
                                    businessgalerydetailsResponseList = response.body().getData().getBussiness_gallery();
                                    Log.w(TAG, "Size" + businessgalerydetailsResponseList.size());
                                    Log.w(TAG, "businessgalerydetailsResponseList : " + new Gson().toJson(businessgalerydetailsResponseList));
                                }

                                if (businessgalerydetailsResponseList != null && businessgalerydetailsResponseList.size() > 0) {

                                    for (int i = 0; i < businessgalerydetailsResponseList.size(); i++) {
                                        if (businessgalerydetailsResponseList.get(i).getBussiness_gallery() != null && !businessgalerydetailsResponseList.get(i).getBussiness_gallery().isEmpty()) {
                                            Glide.with(mContext)
                                                    .load(businessgalerydetailsResponseList.get(i).getBussiness_gallery())
                                                    .into(img_service_pic);

                                        }
                                        else{
                                            Glide.with(mContext)
                                                    .load(APIClient.PROFILE_IMAGE_URL)
                                                    .into(img_service_pic);

                                        }
                                    }






                                }

                            }




                    }

                }


            }

            @SuppressLint({"LongLogTag", "LogNotTimber"})
            @Override
            public void onFailure(@NonNull Call<VendorGetsOrderIDResponse> call, @NonNull Throwable t) {

                avi_indicator.smoothToHide();

                Log.w(TAG,"getVendorOrderIDResponseCall flr"+"--->" + t.getMessage());
            }
        });

    }
    private VendorGetsOrderIdRequest vendorGetsOrderIdRequest() {
        VendorGetsOrderIdRequest vendorGetsOrderIdRequest = new VendorGetsOrderIdRequest();
        vendorGetsOrderIdRequest.setUser_id(userid);
        Log.w(TAG,"vendorGetsOrderIdRequest"+ "--->" + new Gson().toJson(vendorGetsOrderIdRequest));
        return vendorGetsOrderIdRequest;
    }


    @SuppressLint("LogNotTimber")
    public void getlist_from_vendor_id_ResponseCall(){
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        //Creating an object of our api interface
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<ManageProductsListResponse> call = ApiService.getlist_from_vendor_id_ResponseCall(RestUtils.getContentType(),manageProductsListRequest());

        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<ManageProductsListResponse>() {
            @Override
            public void onResponse(@NonNull Call<ManageProductsListResponse> call, @NonNull Response<ManageProductsListResponse> response) {
                avi_indicator.smoothToHide();


                if (response.body() != null) {
                    if(200 == response.body().getCode()){
                        Log.w(TAG,"ManageProductsListResponse" + new Gson().toJson(response.body()));

                        if(response.body().getData()!= null && response.body().getData().size()>0){
                            productList = response.body().getData();

                            txt_no_records.setVisibility(View.GONE);

                            txt_add_product.setVisibility(View.GONE);

                            txt_lbl_products.setVisibility(View.VISIBLE);
                            rl_category.setVisibility(View.VISIBLE);

                            setView(productList);

                        }
                        else {

                            txt_lbl_products.setVisibility(View.GONE);
                            rl_category.setVisibility(View.GONE);

                            txt_no_records.setVisibility(View.VISIBLE);

                            txt_add_product.setVisibility(View.VISIBLE);

                            txt_add_product.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent i = new Intent(getContext(), VendorAddProductsActivity.class);
                                    startActivity(i);
                                }
                            });
                        }

                    }
                }

            }


            @Override
            public void onFailure(@NonNull Call<ManageProductsListResponse> call, @NonNull  Throwable t) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"ManageProductsListResponse flr"+t.getMessage());
            }
        });

    }
    @SuppressLint("LogNotTimber")
    private ManageProductsListRequest manageProductsListRequest() {
        /*
         * vendor_id : 6048589d0b3a487571a1c567
         * search_string : CAT
         */
        ManageProductsListRequest manageProductsListRequest = new ManageProductsListRequest();
        manageProductsListRequest.setVendor_id(APIClient.VENDOR_ID);
        manageProductsListRequest.setSearch_string(searchString);
        Log.w(TAG,"manageProductsListRequest"+ "--->" + new Gson().toJson(manageProductsListRequest));
        return manageProductsListRequest;
    }
    private void setView(List<ManageProductsListResponse.DataBean> productList) {

        rv_productList.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        VendorProductListAdapter vendorProductListAdapter = new VendorProductListAdapter(getContext(), productList,this);
        rv_productList.setAdapter(vendorProductListAdapter);



    }

    @Override
    public void productDeleteListener(String productid) {
        showStatusAlert(productid);
    }

    @SuppressLint("SetTextI18n")
    private void showStatusAlert(String productid) {

        try {
            dialog = new Dialog(mContext);
            dialog.setContentView(R.layout.alert_approve_reject_layout);
            TextView tvheader = dialog.findViewById(R.id.tvInternetNotConnected);
            tvheader.setText(R.string.deletemsgproduct);
            Button dialogButtonApprove = dialog.findViewById(R.id.btnApprove);
            dialogButtonApprove.setText("Yes");
            Button dialogButtonRejected = dialog.findViewById(R.id.btnReject);
            dialogButtonRejected.setText("No");

            dialogButtonApprove.setOnClickListener(view -> {
                dialog.dismiss();
                vendor_product_edit_ResponseCall(productid);



            });
            dialogButtonRejected.setOnClickListener(view -> dialog.dismiss());
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }




    }


    @SuppressLint("LogNotTimber")
    private void vendor_product_edit_ResponseCall(String productid) {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();

        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<SuccessResponse> call = apiInterface.vendor_product_edit_ResponseCall(RestUtils.getContentType(),productVendorEditRequest(productid));

        Log.w(TAG,"url  :%s"+call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NotNull Call<SuccessResponse> call, @NotNull Response<SuccessResponse> response) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"SuccessResponse"+ "--->" + new Gson().toJson(response.body()));

                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        Toasty.success(mContext, "Product Removed Successfully", Toast.LENGTH_SHORT, true).show();
                        getlist_from_vendor_id_ResponseCall();

                    }
                }



            }

            @Override
            public void onFailure(@NotNull Call<SuccessResponse> call, @NotNull Throwable t) {
                avi_indicator.smoothToHide();

                Log.w(TAG,"SuccessResponse"+"--->" + t.getMessage());
            }
        });

    }
    @SuppressLint("LogNotTimber")
    private ProductVendorEditRequest productVendorEditRequest(String productid) {

        /*
          _id : 5f05d911f3090625a91f40c7
          delete_status:true
         */
        ProductVendorEditRequest productVendorEditRequest = new ProductVendorEditRequest();
        productVendorEditRequest.set_id(productid);
        productVendorEditRequest.setDelete_status(true);
        Log.w(TAG,"productVendorEditRequest"+ "--->" + new Gson().toJson(productVendorEditRequest));
        return productVendorEditRequest;
    }

}