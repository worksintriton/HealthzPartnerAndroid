package com.triton.healthZpartners.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.triton.healthZpartners.R;
import com.triton.healthZpartners.adapter.ProductsSearchAdapter;
import com.triton.healthZpartners.api.APIClient;
import com.triton.healthZpartners.api.RestApiInterface;
import com.triton.healthZpartners.fragmentcustomer.bottommenu.CustomerCareFragment;
import com.triton.healthZpartners.fragmentcustomer.bottommenu.CustomerShopFragment;
import com.triton.healthZpartners.fragmentcustomer.bottommenu.PetHomeFragment;
import com.triton.healthZpartners.fragmentcustomer.bottommenu.CustomerServicesFragment;
import com.triton.healthZpartners.requestpojo.ProductSearchRequest;
import com.triton.healthZpartners.responsepojo.ProductSearchResponse;
import com.triton.healthZpartners.utils.ConnectionDetector;
import com.triton.healthZpartners.utils.RestUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = "SearchActivity";

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.avi_indicator)
    AVLoadingIndicatorView avi_indicator;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_back)
    ImageView img_back;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_relatedproducts)
    RecyclerView rv_relatedproducts;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_no_records)
    TextView txt_no_records;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_search)
    EditText edt_search;


    private String searchString = "";
    private List<ProductSearchResponse.DataBean> productSearchResponseCall;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.include_petlover_footer)
    View include_petlover_footer;


    final Fragment petHomeFragment = new PetHomeFragment();
    final Fragment petCareFragment = new CustomerCareFragment();
    final Fragment petServicesFragment = new CustomerServicesFragment();
    final Fragment vendorShopFragment = new CustomerShopFragment();

    public static String active_tag = "1";


    Fragment active = petHomeFragment;
    String tag;
    private String fromactivity;

   /**/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);

        img_back.setOnClickListener(this);
        avi_indicator.setVisibility(View.GONE);





        if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
            productSearchResponseCall(searchString);
        }

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
                    if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
                        productSearchResponseCall(searchString);
                    }


                }else{
                    searchString ="";
                    if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
                        productSearchResponseCall(searchString);
                    }

                }

            }
        });



    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                onBackPressed();
                break;

            case R.id.rl_homes:
                callDirections("1");
                break;
            case R.id.rl_home:
                callDirections("1");
                break;
            case R.id.rl_shop:
                callDirections("2");
                break;
            case R.id.rl_service:
                callDirections("3");
                break;
            case R.id.rl_care:
                callDirections("4");
                break;
            case R.id.rl_comn:
                callDirections("5");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        callDirections("2");
    }

    @SuppressLint("LogNotTimber")
    private void productSearchResponseCall(String searchString) {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<ProductSearchResponse> call = apiInterface.productSearchResponseCall(RestUtils.getContentType(), productSearchRequest(searchString));
        Log.w(TAG,"DoctorSearchResponse url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<ProductSearchResponse>() {
            @SuppressLint({"LogNotTimber", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<ProductSearchResponse> call, @NonNull Response<ProductSearchResponse> response) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"DoctorSearchResponse" + new Gson().toJson(response.body()));
                if (response.body() != null) {
                    if (200 == response.body().getCode()) {


                        if (response.body().getData() != null) {
                            productSearchResponseCall = response.body().getData();
                            Log.w(TAG, "productSearchResponseCall Size" + productSearchResponseCall.size());
                            if (productSearchResponseCall != null && productSearchResponseCall.size()>0) {
                                rv_relatedproducts.setVisibility(View.VISIBLE);
                                txt_no_records.setVisibility(View.GONE);
                                setViewProducts(productSearchResponseCall);
                            } else {
                                rv_relatedproducts.setVisibility(View.GONE);
                                txt_no_records.setVisibility(View.VISIBLE);
                                txt_no_records.setText("No products available");

                            }

                        }



                    }

                }


            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(@NonNull Call<ProductSearchResponse> call,@NonNull Throwable t) {
                avi_indicator.smoothToHide();
                Log.e("ProductSearchResponse flr", "--->" + t.getMessage());
            }
        });

    }
    private void setViewProducts(List<ProductSearchResponse.DataBean> productSearchResponseCall) {
        rv_relatedproducts.setLayoutManager(new GridLayoutManager(this, 2));
        rv_relatedproducts.setItemAnimator(new DefaultItemAnimator());
        ProductsSearchAdapter productsSearchAdapter = new ProductsSearchAdapter(getApplicationContext(), productSearchResponseCall,TAG,"");
        rv_relatedproducts.setAdapter(productsSearchAdapter);
    }
    private ProductSearchRequest productSearchRequest(String searchString) {
        /*
         * search_string :
        */
        ProductSearchRequest productSearchRequest = new ProductSearchRequest();
        productSearchRequest.setSearch_string(searchString);
        Log.w(TAG,"productSearchRequest"+ new Gson().toJson(productSearchRequest));
        return productSearchRequest;
    }

    public void callDirections(String tag){
        Intent intent = new Intent(SearchActivity.this, CustomerDashboardActivity.class);
        intent.putExtra("tag",tag);
        startActivity(intent);
        finish();

    }


    private void setMargins(RelativeLayout rl_layout, int i, int i1, int i2, int i3) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)rl_layout.getLayoutParams();
        params.setMargins(i, i1, i2, i3);
        rl_layout.setLayoutParams(params);
    }
}