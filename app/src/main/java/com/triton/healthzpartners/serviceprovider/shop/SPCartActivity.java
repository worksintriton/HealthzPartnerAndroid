package com.triton.healthzpartners.serviceprovider.shop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.triton.healthzpartners.R;
import com.triton.healthzpartners.adapter.Cart_Adapter;
import com.triton.healthzpartners.api.APIClient;
import com.triton.healthzpartners.api.RestApiInterface;
import com.triton.healthzpartners.interfaces.AddandRemoveProductListener;
import com.triton.healthzpartners.requestpojo.CouponCodeCheckRequest;
import com.triton.healthzpartners.requestpojo.FetchByIdRequest;
import com.triton.healthzpartners.requestpojo.NotificationCartCountRequest;
import com.triton.healthzpartners.responsepojo.CartDetailsResponse;
import com.triton.healthzpartners.responsepojo.CartSuccessResponse;
import com.triton.healthzpartners.responsepojo.CouponCodeCheckResponse;
import com.triton.healthzpartners.responsepojo.NotificationCartCountResponse;
import com.triton.healthzpartners.responsepojo.SuccessResponse;
import com.triton.healthzpartners.serviceprovider.ServiceProviderDashboardActivity;
import com.triton.healthzpartners.serviceprovider.ShippingAddressSPActivity;
import com.triton.healthzpartners.sessionmanager.SessionManager;
import com.triton.healthzpartners.utils.ConnectionDetector;
import com.triton.healthzpartners.utils.RestUtils;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SPCartActivity extends AppCompatActivity implements AddandRemoveProductListener, PaymentResultListener, View.OnClickListener {

    private String TAG = "SPCartActivity";


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.avi_indicator)
    AVLoadingIndicatorView avi_indicator;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.include_doctor_footer)
    View include_doctor_footer;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_cart)
    RecyclerView rv_cart;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_back)
    ImageView img_back;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_lbl_subtotal)
    TextView txt_lbl_subtotal;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_sub_total)
    TextView txt_sub_total;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_discount_amount)
    TextView txt_discount_amount;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_shipping_amount)
    TextView txt_shipping_amount;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_total_amount)
    TextView txt_total_amount;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_procced_to_buy)
    Button btn_procced_to_buy;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_cart_is_empty)
    LinearLayout ll_cart_is_empty;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_content)
    LinearLayout ll_content;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.scrollablContent)
    ScrollView scrollablContent;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.footerView)
    LinearLayout footerView;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_content_amount)
    LinearLayout ll_content_amount;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_removeall_products)
    TextView txt_removeall_products;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_cart_count_badge)
    TextView txt_cart_count_badge;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_shopnow)
    Button btn_shopnow;




    String tag;
    String fromactivity;
    String fromto;

    private String userid;
    private String productid;
    private String productdetails_productid;

    List<CartDetailsResponse.DataBean> Data = new ArrayList<>();
    private int prodouct_total;
    private int shipping_charge;
    private int discount_price;
    private int grand_total;
    private int prodcut_count;
    private int prodcut_item_count;
    private String Payment_id = "";

    private int product_cart_counts = 0;
    private String threshould;

    private String active_tag;
    private String cat_id;

    /* Bottom Navigation */

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rl_home)
    RelativeLayout rl_home;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rl_service)
    RelativeLayout rl_service;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rl_shop)
    RelativeLayout rl_shop;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.title_shop)
    TextView title_shop;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_shop)
    ImageView img_shop;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rl_comn)
    RelativeLayout rl_comn;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.title_community)
    TextView title_community;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_community)
    ImageView img_community;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rl_homes)
    RelativeLayout rl_homes;

     @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_coupon)
     EditText edt_coupon;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_apply_coupon)
    Button btn_apply_coupon;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_coupon_discount_amount)
    LinearLayout ll_coupon_discount_amount;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_coupon_discount_amount)
    TextView txt_coupon_discount_amount;

    private String Coupon_code = "";
    private String Coupon_status = "Not Applied";
    private int Original_price = 0;
    private int Discount_price = 0;
    private int Total_price = 0;
    private Dialog alertDialog;
    private int Original_Discount_Price;
    private int Grand_total;
    private int Coupon_discount_price;
    private Dialog dialog;

    private String rzpkey;
    private boolean isproduction;

    @SuppressLint("LogNotTimber")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_cart);
        ButterKnife.bind(this);
        Log.w(TAG,"onCreate");

        scrollablContent.setVisibility(View.GONE);
        btn_procced_to_buy.setVisibility(View.GONE);
        ll_content_amount.setVisibility(View.GONE);
        footerView.setVisibility(View.GONE);
        txt_cart_count_badge.setVisibility(View.GONE);

        ll_coupon_discount_amount.setVisibility(View.GONE);


        SessionManager sessionManager = new SessionManager(getApplicationContext());
        HashMap<String, String> user = sessionManager.getProfileDetails();
        userid = user.get(SessionManager.KEY_ID);

        HashMap<String, String> razorpayDetails = sessionManager.getRazorpayDetails();
        rzpkey =  razorpayDetails.get(SessionManager.KEY_RZP_KEY);
        String production =  razorpayDetails.get(SessionManager.KEY_RZP_PRODUCTION);
        if(production != null){
            if(production.equalsIgnoreCase("true")){
                isproduction = true;
            }else{
                isproduction = false;
            }
        }

        Log.w(TAG, "rzpkey : " + rzpkey+ " isproduction : "+isproduction);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            productdetails_productid = extras.getString("productid");
            fromactivity = extras.getString("fromactivity");
            active_tag = extras.getString("active_tag");
            cat_id = extras.getString("cat_id");



            /*SPProductDetailsActivity*/
            productid = extras.getString("productid");
            cat_id = extras.getString("cat_id");
            fromactivity = extras.getString("fromactivity");
            tag = extras.getString("tag");
            fromto = extras.getString("fromto");
        }


        img_back.setOnClickListener(v -> onBackPressed());
        if(userid != null && !userid.isEmpty()){
              if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
                            notificationandCartCountResponseCall();
                        }
        }



        btn_shopnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDirections("2");

            }
        });


        btn_procced_to_buy.setOnClickListener(v -> {
            if(grand_total!=0){
                Intent i = new Intent(getApplicationContext(), ShippingAddressSPActivity.class);
                i.putExtra("fromactivity",TAG);
                i.putExtra("data", (Serializable) Data);
                i.putExtra("product_total",prodouct_total);
                i.putExtra("shipping_charge",shipping_charge);
                i.putExtra("discount_price",discount_price);
                i.putExtra("grand_total",grand_total);
                i.putExtra("prodcut_count",prodcut_count);
                i.putExtra("prodcut_item_count",prodcut_item_count);
                i.putExtra("Original_price",Original_price);
                i.putExtra("Coupon_discount_price",Coupon_discount_price);
                i.putExtra("Coupon_code",Coupon_code);
                i.putExtra("Coupon_status",Coupon_status);
                i.putExtra("Total_price",Total_price);
                startActivity(i);

            }else{
                grand_total =0;
                Intent i = new Intent(getApplicationContext(), ShippingAddressSPActivity.class);
                i.putExtra("fromactivity",TAG);
                i.putExtra("data", (Serializable) Data);
                i.putExtra("product_total",prodouct_total);
                i.putExtra("shipping_charge",shipping_charge);
                i.putExtra("discount_price",discount_price);
                i.putExtra("grand_total",grand_total);
                i.putExtra("prodcut_count",prodcut_count);
                i.putExtra("prodcut_item_count",prodcut_item_count);
                i.putExtra("Original_price",Original_price);
                i.putExtra("Coupon_discount_price",Coupon_discount_price);
                i.putExtra("Coupon_code",Coupon_code);
                i.putExtra("Coupon_status",Coupon_status);
                i.putExtra("Total_price",Total_price);
                startActivity(i);
            }
        });

            txt_removeall_products.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //remove_overall_products_ResponseCall();
                    showremove_overall_products_ResponseCall();
                }
            });



            //bottom_navigation_view.getMenu().findItem(R.id.shop).setChecked(true);
        /*shop*/

        title_community.setTextColor(getResources().getColor(R.color.darker_grey_new,getTheme()));
        img_community.setImageResource(R.drawable.grey_community);
        title_shop.setTextColor(getResources().getColor(R.color.new_gree_color,getTheme()));
        img_shop.setImageResource(R.drawable.green_shop);

        rl_home.setOnClickListener(this);
        rl_shop.setOnClickListener(this);
        rl_comn.setOnClickListener(this);
        rl_homes.setOnClickListener(this);

        btn_apply_coupon.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
                if(btn_apply_coupon.getText().toString().equalsIgnoreCase("Remove")){
                    edt_coupon.setText("");
                    btn_apply_coupon.setText("Apply");
                    ll_coupon_discount_amount.setVisibility(View.GONE);
                    txt_total_amount.setText("\u20B9 "+Grand_total);
                    Coupon_status = "Not Applied";
                    Coupon_code = "";
                    Original_price = 0;
                    Discount_price = 0;
                    grand_total = Grand_total;

                }else if(btn_apply_coupon.getText().toString().equalsIgnoreCase("Apply")){
                    if(edt_coupon.getText().toString().isEmpty()){
                        edt_coupon.setError("Please enter the coupon code");
                        edt_coupon.requestFocus();
                    }else {
                        Coupon_status = "Applied";
                        Coupon_code = edt_coupon.getText().toString();
                        CouponCodeCheckResponseCall();
                    }


                }


            }
        });





    }

    private void fetch_cart_details_by_userid_Call() {
        if(userid != null){
            if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
                fetch_cart_details_by_userid_ResponseCall();
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(fromto != null && fromto.equalsIgnoreCase("SPProductDetailsActivity")){
            Intent intent = new Intent(SPCartActivity.this, SPProductDetailsActivity.class);
            intent.putExtra("productid",productid);
            intent.putExtra("cat_id",cat_id);
            intent.putExtra("tag",tag);
            startActivity(intent);
            finish();
        }else if(fromactivity != null && fromactivity.equalsIgnoreCase("SPProductDetailsActivity")){
            Intent i = new Intent(SPCartActivity.this, SPProductDetailsActivity.class);
            i.putExtra("productid",productdetails_productid);
            startActivity(i);
            finish();
        }else if(fromactivity != null && fromactivity.equalsIgnoreCase("SPShopTodayDealsSeeMoreActivity")){
            Intent i = new Intent(SPCartActivity.this, SPShopTodayDealsSeeMoreActivity.class);
            startActivity(i);
            finish();
        }else if(fromactivity != null && fromactivity.equalsIgnoreCase("SPListOfProductsSeeMoreActivity")){
            Intent i = new Intent(SPCartActivity.this, SPListOfProductsSeeMoreActivity.class);
            i.putExtra("cat_id",cat_id);
            startActivity(i);
            finish();
        }else if(fromactivity != null && fromactivity.equalsIgnoreCase("SPMyOrdrersActivity")){
            Intent i = new Intent(SPCartActivity.this, SPMyOrdrersActivity.class);
            startActivity(i);
            finish();
        }
        else if(active_tag != null){
            callDirections(active_tag);
        } else{
            Intent i = new Intent(SPCartActivity.this, ServiceProviderDashboardActivity.class);
            startActivity(i);
            finish();
        }

    }

    @SuppressLint("LogNotTimber")
    public void fetch_cart_details_by_userid_ResponseCall(){
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        //Creating an object of our api interface
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<CartDetailsResponse> call = ApiService.fetch_cart_details_by_userid_ResponseCall(RestUtils.getContentType(),fetchByIdRequest());
        Log.w(TAG,"url  :%s"+ call.request().url().toString());
        call.enqueue(new Callback<CartDetailsResponse>() {
            @SuppressLint({"LogNotTimber", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<CartDetailsResponse> call, @NonNull Response<CartDetailsResponse> response) {
                avi_indicator.smoothToHide();
                if (response.body() != null) {
                    if(200 == response.body().getCode()){


                        Log.w(TAG,"CartDetailsResponse" + new Gson().toJson(response.body()));
                        footerView.setVisibility(View.VISIBLE);

                        if(response.body().getData() != null && response.body().getData().size()>0){
                            scrollablContent.setVisibility(View.VISIBLE);
                            ll_content.setVisibility(View.VISIBLE);
                            ll_cart_is_empty.setVisibility(View.GONE);
                            btn_procced_to_buy.setVisibility(View.VISIBLE);
                            ll_content_amount.setVisibility(View.VISIBLE);

                            Data = response.body().getData();
                            for(int i=0;i<Data.size();i++){
                                threshould = Data.get(i).getProduct_id().getThreshould();

                            }
                            setView(response.body().getData());

                            if (response.body().getProdcut_item_count() != 0) {
                                if (response.body().getProdcut_item_count() == 1) {
                                    txt_lbl_subtotal.setText("Subtotal ( " + response.body().getProdcut_item_count() + " item)");
                                } else {
                                    txt_lbl_subtotal.setText("Subtotal ( " + response.body().getProdcut_item_count() + " items)");
                                }
                            }
                            if(response.body().getProdouct_total() != 0){
                                Total_price = response.body().getProdouct_total();
                                txt_sub_total.setText("\u20B9 "+response.body().getProdouct_total());
                            }else{
                                Total_price = 0;
                                txt_sub_total.setText("\u20B9 "+0);

                            }
                            if(response.body().getDiscount_price() != 0){
                                txt_discount_amount.setText("\u20B9 "+response.body().getDiscount_price());
                            }else{
                                txt_discount_amount.setText("\u20B9 "+0);
                            }
                            if(response.body().getShipping_charge() != 0){
                                txt_shipping_amount.setText("\u20B9 "+response.body().getShipping_charge());
                            }else{
                                txt_shipping_amount.setText("\u20B9 "+0);

                            }
                            if(response.body().getGrand_total() != 0){
                                txt_total_amount.setText("\u20B9 "+response.body().getGrand_total());
                            }else{
                                txt_total_amount.setText("\u20B9 "+0);

                            }






                        }
                        else {
                            txt_cart_count_badge.setText("0");
                            scrollablContent.setVisibility(View.VISIBLE);
                            ll_content.setVisibility(View.GONE);
                            ll_cart_is_empty.setVisibility(View.VISIBLE);
                            btn_procced_to_buy.setVisibility(View.GONE);
                            ll_content_amount.setVisibility(View.GONE);

                        }

                        if(response.body() != null) {
                            prodouct_total = response.body().getProdouct_total();
                            shipping_charge = response.body().getShipping_charge();
                            discount_price  = response.body().getDiscount_price();
                            grand_total = response.body().getGrand_total();
                            prodcut_count = response.body().getProdcut_count();
                            prodcut_item_count  = response.body().getProdcut_item_count();
                        }



                    }
                }
            }


            @SuppressLint("LogNotTimber")
            @Override
            public void onFailure(@NonNull Call<CartDetailsResponse> call, @NonNull  Throwable t) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"CartDetailsResponse flr"+t.getMessage());
            }
        });

    }

    private void setView(List<CartDetailsResponse.DataBean> data) {
        rv_cart.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        rv_cart.setItemAnimator(new DefaultItemAnimator());
        Cart_Adapter cart_adapter = new Cart_Adapter(getApplicationContext(), data,this,TAG);
        rv_cart.setAdapter(cart_adapter);
    }

    @SuppressLint("LogNotTimber")
    private FetchByIdRequest fetchByIdRequest() {
        /*
         * user_id : 603e27792c2b43125f8cb802
         */
        FetchByIdRequest fetchByIdRequest = new FetchByIdRequest();
        fetchByIdRequest.setUser_id(userid);
        Log.w(TAG,"fetchByIdRequest"+ "--->" + new Gson().toJson(fetchByIdRequest));
        return fetchByIdRequest;
    }

    @SuppressLint("LogNotTimber")
    @Override
    public void addandRemoveProductListener(String id, String name,String threshould,int prodcutcount) {
        if(name != null){
            if(name.equalsIgnoreCase("add")){
                if(threshould != null){
                    productid = id;
                    int productqty = Integer.parseInt(threshould);
                    Log.w(TAG," productqty : "+productqty+" prodcutcount : "+prodcutcount);
                    if(prodcutcount == productqty || prodcutcount >productqty){
                        Toasty.warning(getApplicationContext(), "You can buy only up to "+productqty+" quantity of this product", Toast.LENGTH_SHORT, true).show();
                    }else{
                        if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
                            add_product_ResponseCall();
                        }
                    }
                }
                edt_coupon.setText("");
                btn_apply_coupon.setText("Apply");
                ll_coupon_discount_amount.setVisibility(View.GONE);
                txt_total_amount.setText("\u20B9 "+Grand_total);
                Coupon_status = "Not Applied";
                Coupon_code = "";
                Original_price = 0;
                Discount_price = 0;
                grand_total = Grand_total;

            }
            else if(name.equalsIgnoreCase("remove")){
                productid = id;
                if(productid != null){
                    if(prodcutcount != 0) {
                        if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
                            remove_product_ResponseCall();
                        }
                    }
                }
                edt_coupon.setText("");
                btn_apply_coupon.setText("Apply");
                ll_coupon_discount_amount.setVisibility(View.GONE);
                txt_total_amount.setText("\u20B9 "+Grand_total);
                Coupon_status = "Not Applied";
                Coupon_code = "";
                Original_price = 0;
                Discount_price = 0;
                grand_total = Grand_total;
            }
            else if(name.equalsIgnoreCase("singleproductremove")){
                productid = id;
                if(productid != null){
                    edt_coupon.setText("");
                    btn_apply_coupon.setText("Apply");
                    ll_coupon_discount_amount.setVisibility(View.GONE);
                    txt_total_amount.setText("\u20B9 "+Grand_total);
                    Coupon_status = "Not Applied";
                    Coupon_code = "";
                    Original_price = 0;
                    Discount_price = 0;
                    grand_total = Grand_total;
                    if(prodcutcount != 0) {
                        showremove_single_products_ResponseCall();
                       /* if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
                            remove_single_products_ResponseCall();
                        }*/
                    }
                }
            }
        }
    }
    @SuppressLint("LogNotTimber")
    public void remove_product_ResponseCall(){
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        //Creating an object of our api interface
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<SuccessResponse> call = ApiService.remove_product_ResponseCall(RestUtils.getContentType(),addandRemoveCartRequest());

        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @SuppressLint({"LogNotTimber", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                avi_indicator.smoothToHide();
                if (response.body() != null) {
                    if(200 == response.body().getCode()){
                        Log.w(TAG,"Remove SuccessResponse" + new Gson().toJson(response.body()));
                        Toasty.success(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                       notificationandCartCountResponseCall();
                    }
                }
            }


            @SuppressLint("LogNotTimber")
            @Override
            public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull  Throwable t) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"Remove SuccessResponse flr"+t.getMessage());
            }
        });

    }
    public void remove_single_products_ResponseCall(){
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        //Creating an object of our api interface
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<SuccessResponse> call = ApiService.remove_single_products_ResponseCall(RestUtils.getContentType(),addandRemoveCartRequest());

        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @SuppressLint({"LogNotTimber", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                avi_indicator.smoothToHide();
                if (response.body() != null) {
                    if(200 == response.body().getCode()){
                        Log.w(TAG,"Remove SuccessResponse" + new Gson().toJson(response.body()));
                        Toasty.success(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                       notificationandCartCountResponseCall();
                    }
                }
            }


            @SuppressLint("LogNotTimber")
            @Override
            public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull  Throwable t) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"Remove SuccessResponse flr"+t.getMessage());
            }
        });

    }
    public void remove_overall_products_ResponseCall(){
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        //Creating an object of our api interface
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<SuccessResponse> call = ApiService.remove_overall_products_ResponseCall(RestUtils.getContentType(),removeAllProductsRequest());

        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @SuppressLint({"LogNotTimber", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                avi_indicator.smoothToHide();
                if (response.body() != null) {
                    if(200 == response.body().getCode()){
                        Log.w(TAG,"Remove SuccessResponse" + new Gson().toJson(response.body()));
                        Toasty.success(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                        notificationandCartCountResponseCall();
                    }
                }
            }


            @SuppressLint("LogNotTimber")
            @Override
            public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull  Throwable t) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"Remove SuccessResponse flr"+t.getMessage());
            }
        });

    }
    @SuppressLint("LogNotTimber")
    public void add_product_ResponseCall(){
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        //Creating an object of our api interface
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<SuccessResponse> call = ApiService.add_product_ResponseCall(RestUtils.getContentType(),addandRemoveCartRequest());

        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @SuppressLint({"LogNotTimber", "SetTextI18n"})
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {
                avi_indicator.smoothToHide();


                if (response.body() != null) {
                    if(200 == response.body().getCode()){
                        Log.w(TAG,"Add SuccessResponse" + new Gson().toJson(response.body()));
                        Toasty.success(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                        notificationandCartCountResponseCall();


                    }
                }
            }


            @SuppressLint("LogNotTimber")
            @Override
            public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull  Throwable t) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"Add SuccessResponse flr"+t.getMessage());
            }
        });

    }
    @SuppressLint("LogNotTimber")
    private FetchByIdRequest addandRemoveCartRequest() {
        /*
         * user_id : 603e27792c2b43125f8cb802
         * product_id : 6034d6a5888af7628e7e17d4
         */
        FetchByIdRequest fetchByIdRequest = new FetchByIdRequest();
        fetchByIdRequest.setUser_id(userid);
        fetchByIdRequest.setProduct_id(productid);
        Log.w(TAG,"fetchByIdRequest"+ "--->" + new Gson().toJson(fetchByIdRequest));
        return fetchByIdRequest;
    }
    private FetchByIdRequest removeAllProductsRequest() {
        /*
         * user_id : 603e27792c2b43125f8cb802
       */
        FetchByIdRequest fetchByIdRequest = new FetchByIdRequest();
        fetchByIdRequest.setUser_id(userid);
        Log.w(TAG,"fetchByIdRequest"+ "--->" + new Gson().toJson(fetchByIdRequest));
        return fetchByIdRequest;
    }
    @SuppressLint("LogNotTimber")
    public void vendor_order_booking_create_ResponseCall(){
       avi_indicator.setVisibility(View.VISIBLE);
       avi_indicator.smoothToShow();
        //Creating an object of our api interface
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<CartSuccessResponse> call = ApiService.vendor_order_booking_create_ResponseCall(RestUtils.getContentType(),vendorOrderBookingCreateRequest());

       Log.w(TAG,"url  :%s"+ call.request().url().toString());

       call.enqueue(new Callback<CartSuccessResponse>() {
            @SuppressLint({"LogNotTimber", "SetTextI18n"})
           @Override
           public void onResponse(@NonNull Call<CartSuccessResponse> call, @NonNull Response<CartSuccessResponse> response) {
                avi_indicator.smoothToHide();
                if (response.body() != null) {
                    if(200 == response.body().getCode()){
                        Log.w(TAG,"SuccessResponse "+new Gson().toJson(response.body().getData()));

                        Toasty.success(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                        callDirections("2");




                    }
                }
            }


            @SuppressLint("LogNotTimber")
            @Override
            public void onFailure(@NonNull Call<CartSuccessResponse> call, @NonNull  Throwable t) {
            avi_indicator.smoothToHide();
               Log.w(TAG,"SuccessResponse flr"+t.getMessage());
          }
        });

    }

    @SuppressLint("LogNotTimber")
    private CartDetailsResponse vendorOrderBookingCreateRequest() {
        /*
         * user_id : 603e27792c2b43125f8cb802
         * Data : [{"_id":"6046fa59cb48ca0b68cda50c","user_id":"603e27792c2b43125f8cb802","product_id":{"breed_type":["602d1c20562e0916bc9b3218"],"pet_type":["602d1c6b562e0916bc9b321d"],"age":[3],"product_img":["http://54.212.108.156:3000/api/uploads/1614075552394.jpg"],"_id":"6034d6a5888af7628e7e17d4","user_id":"602a2061b3c2dd2c152d77d8","cat_id":"5fec14a5ea832e2e73c1fc79","cost":1000,"threshould":"100","product_name":"Cat Dinner","product_discription":"This cat  food","discount":10,"related":"","count":0,"status":"true","verification_status":"Not Verified","date_and_time":"Tue Feb 23 2021 15:49:15 GMT+0530 (India Standard Time)","mobile_type":"Admin","delete_status":true,"fav_status":false,"today_deal":true,"updatedAt":"2021-03-08T09:15:24.812Z","createdAt":"2021-02-23T10:19:17.691Z","__v":0},"product_count":7,"updatedAt":"2021-03-09T06:10:04.116Z","createdAt":"2021-03-09T04:32:25.151Z","__v":0},{"_id":"60471192760fff2968288bbd","user_id":"603e27792c2b43125f8cb802","product_id":{"breed_type":["602d1c17562e0916bc9b3217"],"pet_type":["602d1c6b562e0916bc9b321d"],"age":[3],"product_img":["http://54.212.108.156:3000/api/uploads/1614075490400.jpg"],"_id":"6034d66598fa826140f6a3a3","user_id":"602a2061b3c2dd2c152d77d8","cat_id":"5fec14a5ea832e2e73c1fc79","cost":40000,"threshould":"100","product_name":"CAT Lunch","product_discription":"This is cat lunch","discount":40,"related":"","count":0,"status":"true","verification_status":"Not Verified","date_and_time":"Tue Feb 23 2021 15:48:14 GMT+0530 (India Standard Time)","mobile_type":"Admin","delete_status":true,"fav_status":false,"today_deal":true,"updatedAt":"2021-03-08T09:15:22.710Z","createdAt":"2021-02-23T10:18:13.989Z","__v":0},"product_count":1,"updatedAt":"2021-03-09T06:11:30.904Z","createdAt":"2021-03-09T06:11:30.904Z","__v":0}]
         * prodouct_total : 47000
         * shipping_charge : 0
         * discount_price : 0
         * grand_total : 0
         * prodcut_count : 0
         * prodcut_item_count : 0
         * "date_of_booking_display" : "23-Jan-2020",
            "date_of_booking" : "23-10-2021  11 : 00 PM",
            "coupon_code" : "",
             "shipping_address_id" : "",
            "billling_address_id" : "",
            "shipping_address" : "",
             "billing_address" : "",
         */

        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");
        String currentDateandTime = simpleDateFormat.format(new Date());

        CartDetailsResponse vendorOrderBookingCreateRequest = new CartDetailsResponse();
        vendorOrderBookingCreateRequest.setUser_id(userid);
        vendorOrderBookingCreateRequest.setData(Data);
        vendorOrderBookingCreateRequest.setProdouct_total(prodouct_total);
        vendorOrderBookingCreateRequest.setShipping_charge(shipping_charge);
        vendorOrderBookingCreateRequest.setDiscount_price(discount_price);
        vendorOrderBookingCreateRequest.setGrand_total(grand_total);
        vendorOrderBookingCreateRequest.setProdcut_count(prodcut_count);
        vendorOrderBookingCreateRequest.setProdcut_item_count(prodcut_item_count);
        vendorOrderBookingCreateRequest.setDate_of_booking_display(currentDateandTime);
        vendorOrderBookingCreateRequest.setDate_of_booking(currentDateandTime);
        vendorOrderBookingCreateRequest.setCoupon_code("");
        vendorOrderBookingCreateRequest.setShipping_address_id("");
        vendorOrderBookingCreateRequest.setBillling_address_id("");
        vendorOrderBookingCreateRequest.setShipping_address("");
        vendorOrderBookingCreateRequest.setBilling_address("");
        vendorOrderBookingCreateRequest.setPayment_id(Payment_id);
        Log.w(TAG,"vendorOrderBookingCreateRequest"+ "--->" + new Gson().toJson(vendorOrderBookingCreateRequest));
        return vendorOrderBookingCreateRequest;
    }


    @SuppressLint({"LongLogTag", "LogNotTimber"})
    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout checkout = new Checkout();
        if(rzpkey != null) {
            // set your id as below
            checkout.setKeyID(rzpkey);
        }

        Integer totalamout = grand_total*100;
        // rounding off the amount.
        int amount = Math.round(totalamout);

        try {
            JSONObject options = new JSONObject();
            options.put("name", "PetFolio");
            options.put("description", userid);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", amount);


            checkout.open(activity, options);
        } catch (Exception e) {
            Log.w(TAG,"Error in payment: " + e.getMessage());

            e.printStackTrace();
        }
    }
    @SuppressLint({"LongLogTag", "LogNotTimber"})
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Payment_id = razorpayPaymentID;

            Log.w(TAG, "Payment Successful: " + razorpayPaymentID);
            Toasty.success(getApplicationContext(), "Payment Successful. View your booking details in upcoming appointments.", Toast.LENGTH_SHORT, true).show();


            if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
              vendor_order_booking_create_ResponseCall();

            }




        } catch (Exception e) {
            Log.w(TAG, "Exception in onPaymentSuccess", e);
        }
    }
    @SuppressLint({"LongLogTag", "LogNotTimber"})
    @Override
    public void onPaymentError(int code, String response) {
        try {
            if(new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {

            }
            Log.w(TAG,  "Payment failed: " + code + " " + response);
            Toasty.error(getApplicationContext(), "Payment failed. Please try again with another payment method..", Toast.LENGTH_SHORT, true).show();

        } catch (Exception e) {
            Log.w(TAG, "Exception in onPaymentError", e);
        }
    }

    public void callDirections(String tag){
        Intent intent = new Intent(getApplicationContext(), ServiceProviderDashboardActivity.class);
        intent.putExtra("tag",tag);
        startActivity(intent);
        finish();
    }



    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){


            case R.id.rl_homes:
                callDirections("1");
                break;

            case R.id.rl_home:
                callDirections("1");
                break;

            case R.id.rl_shop:
                callDirections("2");
                break;

            case R.id.rl_comn:
                callDirections("3");
                break;


        }

    }

    @SuppressLint("LogNotTimber")
    private void notificationandCartCountResponseCall() {
        btn_procced_to_buy.setClickable(false);
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();

        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<NotificationCartCountResponse> call = apiInterface.notificationandCartCountResponseCall(RestUtils.getContentType(), notificationCartCountRequest());
        Log.w(TAG,"NotificationCartCountResponse url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<NotificationCartCountResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<NotificationCartCountResponse> call, @NonNull Response<NotificationCartCountResponse> response) {

                Log.w(TAG,"NotificationCartCountResponse"+ "--->" + new Gson().toJson(response.body()));

                avi_indicator.smoothToHide();

                if (response.body() != null) {
                    if(response.body().getCode() == 200) {
                        if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
                            fetch_cart_details_by_userid_Call();
                        }
                        if(response.body().getData()!=null){
                            int Notification_count = response.body().getData().getNotification_count();
                            int Product_count = response.body().getData().getProduct_count();
                            if(Product_count != 0){
                                txt_cart_count_badge.setVisibility(View.VISIBLE);
                                txt_cart_count_badge.setText(""+Product_count);
                            }else{
                                txt_cart_count_badge.setVisibility(View.GONE);
                            }

                        }
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<NotificationCartCountResponse> call, @NonNull Throwable t) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"NotificationCartCountResponse flr"+"--->" + t.getMessage());
            }
        });


    }
    @SuppressLint("LogNotTimber")
    private NotificationCartCountRequest notificationCartCountRequest() {
        /*
         * user_id : 6048589d0b3a487571a1c567
         */

        NotificationCartCountRequest notificationCartCountRequest = new NotificationCartCountRequest();
        notificationCartCountRequest.setUser_id(userid);
        Log.w(TAG,"notificationCartCountRequest"+ "--->" + new Gson().toJson(notificationCartCountRequest));
        return notificationCartCountRequest;
    }

    @SuppressLint("LogNotTimber")
    private void CouponCodeCheckResponseCall() {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<CouponCodeCheckResponse> call = ApiService.CouponCodeCheckResponseCall(RestUtils.getContentType(),couponCodeCheckRequest());
        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<CouponCodeCheckResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<CouponCodeCheckResponse> call, @NonNull Response<CouponCodeCheckResponse> response) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"notificationMarkResponseCall SuccessResponse"+ "--->" + new Gson().toJson(response.body()));

                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        Toasty.success(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT, true).show();

                        btn_apply_coupon.setText("Remove");

                        ll_coupon_discount_amount.setVisibility(View.VISIBLE);

                        if(response.body().getData().getDiscount_price() != 0){
                            Coupon_discount_price = response.body().getData().getDiscount_price();
                            txt_coupon_discount_amount.setText("\u20B9 "+response.body().getData().getDiscount_price());
                        }else{
                            txt_coupon_discount_amount.setText("\u20B9 "+0);
                        }
                        if(response.body().getData().getOriginal_price() != 0){
                            Original_price = response.body().getData().getOriginal_price();
                            // txt_serv_cost.setText("\u20B9 "+response.body().getData().getOriginal_price());

                        }else{
                            // txt_serv_cost.setText("\u20B9 "+0);

                        }

                        if(response.body().getData().getTotal_price() != 0){
                            grand_total = response.body().getData().getTotal_price();
                            txt_total_amount.setText("\u20B9 "+response.body().getData().getTotal_price());

                        }else{
                            txt_total_amount.setText("\u20B9 "+0);
                        }


                    }else{
                        showErrorLoading(response.body().getMessage());
                    }

                }


            }

            @SuppressLint("LogNotTimber")
            @Override
            public void onFailure(@NonNull Call<CouponCodeCheckResponse> call, @NonNull Throwable t) {
                avi_indicator.smoothToHide();

                Log.w(TAG,"CouponCodeCheckResponse SuccessResponse flr"+"--->" + t.getMessage());
            }
        });

    }
    @SuppressLint("LogNotTimber")
    private CouponCodeCheckRequest couponCodeCheckRequest() {
        /*
         * current_date : 10/09/2021
         * total_amount : 10000
         * coupon_type : 2
         * user_id :
         * code : TRITON123
         */

        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        CouponCodeCheckRequest couponCodeCheckRequest = new CouponCodeCheckRequest();
        couponCodeCheckRequest.setCurrent_date(currentDate);
        couponCodeCheckRequest.setTotal_amount(grand_total);
        couponCodeCheckRequest.setCoupon_type("3");
        couponCodeCheckRequest.setUser_id(userid);
        couponCodeCheckRequest.setCode(edt_coupon.getText().toString());
        Log.w(TAG,"couponCodeCheckRequest"+ "--->" + new Gson().toJson(couponCodeCheckRequest));
        return couponCodeCheckRequest;
    }
    public void showErrorLoading(String errormesage) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(errormesage);
        alertDialogBuilder.setPositiveButton("ok",
                (arg0, arg1) -> hideLoading());


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void hideLoading() {
        try {
            alertDialog.dismiss();
        } catch (Exception ignored) {

        }
    }

    private void showremove_overall_products_ResponseCall() {
        try{
            dialog = new Dialog(SPCartActivity.this);
            dialog.setContentView(R.layout.alert_yes_no);
            dialog.setCanceledOnTouchOutside(false);
            Button btn_yes = dialog.findViewById(R.id.btn_yes);
            Button btn_no = dialog.findViewById(R.id.btn_no);
            TextView txt_msg = dialog.findViewById(R.id.txt_content_message);
            txt_msg.setText("Are you sure that you want to Empty the Cart?");
            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (new ConnectionDetector(SPCartActivity.this).isNetworkAvailable(SPCartActivity.this)) {
                        remove_overall_products_ResponseCall();
                    }

                    dialog.dismiss();

                }
            });
            btn_no.setOnClickListener(view -> dialog.dismiss());

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.show();


        }
        catch(
                WindowManager.BadTokenException e){
            e.printStackTrace();
        }
    }
    private void showremove_single_products_ResponseCall() {
        try{
            dialog = new Dialog(SPCartActivity.this);
            dialog.setContentView(R.layout.alert_yes_no);
            dialog.setCanceledOnTouchOutside(false);
            Button btn_yes = dialog.findViewById(R.id.btn_yes);
            Button btn_no = dialog.findViewById(R.id.btn_no);
            TextView txt_msg = dialog.findViewById(R.id.txt_content_message);
            txt_msg.setText("Are you sure that you want to delete the products?");
            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (new ConnectionDetector(SPCartActivity.this).isNetworkAvailable(SPCartActivity.this)) {
                        remove_single_products_ResponseCall();
                    }
                    dialog.dismiss();

                }
            });
            btn_no.setOnClickListener(view -> dialog.dismiss());

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.show();


        }
        catch(
                WindowManager.BadTokenException e){
            e.printStackTrace();
        }
    }

}