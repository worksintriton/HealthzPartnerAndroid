package com.triton.healthzpartners.vendor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.triton.healthzpartners.R;
import com.triton.healthzpartners.api.APIClient;
import com.triton.healthzpartners.api.RestApiInterface;
import com.triton.healthzpartners.appUtils.NumericKeyBoardTransformationMethod;
import com.triton.healthzpartners.requestpojo.ProductVendorEditRequest;
import com.triton.healthzpartners.responsepojo.CatgoryGetListResponse;
import com.triton.healthzpartners.responsepojo.FetctProductByCatDetailsResponse;
import com.triton.healthzpartners.responsepojo.ManageProductsListResponse;
import com.triton.healthzpartners.responsepojo.SuccessResponse;
import com.triton.healthzpartners.utils.ConnectionDetector;
import com.triton.healthzpartners.utils.RestUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditManageProdcutsActivity extends AppCompatActivity {

    private String TAG = "EditManageProdcutsActivity";
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_back)
    ImageView img_back;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_product_title)
    TextView txt_product_title;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_update)
    Button btn_update;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.avi_indicator)
    AVLoadingIndicatorView avi_indicator;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_notification)
    ImageView img_notification;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_profile)
    ImageView img_profile;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.ll_main_root)
    LinearLayout ll_main_root;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_product_list)
    RecyclerView rv_product_list;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_add_additional_details)
    RecyclerView rv_add_additional_details;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_product_title)
    EditText edt_product_title;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_product_categprod)
    TextView txt_product_categprod;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_product_price)
    EditText edt_product_price;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_product_thresould)
    EditText edt_product_thresould;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_product_descriptions)
    EditText edt_product_descriptions;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_product_condition)
    EditText edt_product_condition;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_product_pricetype)
    EditText edt_product_pricetype;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_addmore_service)
    EditText edt_addmore_service;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_add)
    Button btn_add;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.include_vendor_footer)
    View include_vendor_footer;

    private String userid;
    private List<CatgoryGetListResponse.DataBean> catgoryGetList;

    HashMap<String, String> hashMap_CatTypeid = new HashMap<>();
    private String strCatType;
    private String strCatTypeId;
    private List<FetctProductByCatDetailsResponse.DataBean> fetctProductByCatDetailsList;
    private String productid;
    private String producttitle;
    private String productdesc;
    private String productimage;
    private Dialog alertDialog;


    BottomNavigationView bottom_navigation_view;

    FloatingActionButton fab;

    int PERMISSION_CLINIC = 1;
    int PERMISSION_CERT = 2;
    int PERMISSION_GOVT = 3;
    int PERMISSION_PHOTO = 4;

    String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    List<String> additional_detail = new ArrayList();
    private static final int REQUEST_CLINIC_CAMERA_PERMISSION_CODE = 785;
    private static final int SELECT_CLINIC_CAMERA = 1000;
    private static final int REQUEST_READ_CLINIC_PIC_PERMISSION = 786;
    private static final int SELECT_CLINIC_PICTURE = 1001;

    MultipartBody.Part filePart;

    String uploadimagepath;

    private String productthreshold;
    private int productprice;

    private ArrayList<ManageProductsListResponse.DataBean.ProductImgBean> productImageList;
    private String productcategoryname;
    private ArrayList<String> additionalDetailList;
    private String condition;
    private String pricetype;


    @SuppressLint({"SetTextI18n", "LogNotTimber"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_manage_prodcuts);
        ButterKnife.bind(this);

        edt_product_thresould.setTransformationMethod(new NumericKeyBoardTransformationMethod());


        avi_indicator.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            productid = extras.getString("productid");
            producttitle = extras.getString("producttitle");
            productprice = extras.getInt("productprice");
            productthreshold = extras.getString("productthreshold");
            productdesc = extras.getString("productdesc");
            productImageList = (ArrayList<ManageProductsListResponse.DataBean.ProductImgBean>) getIntent().getSerializableExtra("productImageList");
            additionalDetailList = (ArrayList<String>) getIntent().getSerializableExtra("additionalDetailList");
            Log.w(TAG,"productImageList : "+new Gson().toJson(productImageList));
            productcategoryname = extras.getString("productcategoryname");
            condition = extras.getString("condition");
            pricetype = extras.getString("pricetype");



            if(producttitle != null){
                edt_product_title.setText(producttitle);
            }
            if(productcategoryname != null){
                txt_product_categprod.setText(productcategoryname);
            }
            if(productprice != 0){
                edt_product_price.setText(productprice+"");
            }else{
                edt_product_price.setText("0");
            }
            if(productthreshold != null){
                edt_product_thresould.setText(productthreshold);
            }if(productdesc != null){
                edt_product_descriptions.setText(productdesc);
            }if(condition != null){
                edt_product_condition.setText(condition);
            }if(pricetype != null){
                edt_product_pricetype.setText(pricetype);
            }

            btn_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addYourProductValidator();
                }
            });

            img_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });



        }

    }

    public void addYourProductValidator() {
        boolean can_proceed = true;



        if (edt_product_title.getText().toString().isEmpty() && edt_product_price.getText().toString().isEmpty() && edt_product_thresould.getText().toString().isEmpty() && edt_product_descriptions.getText().toString().isEmpty()) {
            Toasty.warning(getApplicationContext(), "Please enter the fields", Toast.LENGTH_SHORT, true).show();
            can_proceed = false;
        } else if (edt_product_title.getText().toString().trim().equals("")) {
            edt_product_title.setError("Please enter product title");
            edt_product_title.requestFocus();
            can_proceed = false;
        }
        else if (edt_product_price.getText().toString().trim().equals("")) {
            edt_product_price.setError("Please enter product price");
            edt_product_price.requestFocus();
            can_proceed = false;
        }else if (edt_product_thresould.getText().toString().trim().equals("")) {
            edt_product_thresould.setError("Please enter product thresould");
            edt_product_thresould.requestFocus();
            can_proceed = false;
        }else if (edt_product_descriptions.getText().toString().trim().equals("")) {
            edt_product_descriptions.setError("Please enter product descriptions");
            edt_product_descriptions.requestFocus();
            can_proceed = false;
        }


        if (can_proceed) {
            if (new ConnectionDetector(EditManageProdcutsActivity.this).isNetworkAvailable(EditManageProdcutsActivity.this)) {
                productEditResponseCall();
            }

        }





    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @SuppressLint({"LogNotTimber", "LongLogTag"})
    private void productEditResponseCall() {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<SuccessResponse> call = apiInterface.vendor_product_edit_ResponseCall(RestUtils.getContentType(), productVendorEditRequest());
        Log.w(TAG,"productEditResponseCall url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {

                Log.w(TAG,"productEditResponseCall"+ "--->" + new Gson().toJson(response.body()));

                avi_indicator.smoothToHide();

                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        Toasty.success(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                        startActivity(new Intent(getApplicationContext(),ManageProductsActivity.class));
                        finish();
                    }
                    else{
                        showErrorLoading(response.body().getMessage());
                    }
                }


            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(@NonNull Call<SuccessResponse> call, @NonNull Throwable t) {

                avi_indicator.smoothToHide();
                Log.w(TAG,"productEditResponseCall flr"+"--->" + t.getMessage());
            }
        });

    }
    @SuppressLint({"LogNotTimber", "LongLogTag"})
    private ProductVendorEditRequest productVendorEditRequest() {

        /*
         * _id : 6034d66598fa826140f6a3a3
         * cost : 100
         * threshould : 100
         * product_name : Cat Food
         * product_discription : This is cat lunch.......
         */


        int myCost = 0;

        try {
            myCost = Integer.parseInt(edt_product_price.getText().toString());
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }


        ProductVendorEditRequest productVendorEditRequest = new ProductVendorEditRequest();
        productVendorEditRequest.set_id(productid);
        productVendorEditRequest.setCost(myCost);
        productVendorEditRequest.setThreshould(edt_product_thresould.getText().toString());
        productVendorEditRequest.setProduct_name(edt_product_title.getText().toString());
        productVendorEditRequest.setProduct_discription(edt_product_descriptions.getText().toString());

        productVendorEditRequest.setCat_id(productid);
      //  productVendorEditRequest.setProduct_img(imgList);
      //  productVendorEditRequest.setThumbnail_image(thumbnail_image);
        productVendorEditRequest.setProduct_name(edt_product_title.getText().toString());
        productVendorEditRequest.setCondition(edt_product_condition.getText().toString());
        productVendorEditRequest.setAddition_detail(additional_detail);
        productVendorEditRequest.setPrice_type(edt_product_pricetype.getText().toString());
        productVendorEditRequest.setCost(Integer.parseInt(edt_product_price.getText().toString()));
        productVendorEditRequest.setThreshould(edt_product_thresould.getText().toString());
        productVendorEditRequest.setProduct_discription(edt_product_descriptions.getText().toString());
       // productVendorEditRequest.setVendor_id(APIClient.VENDOR_ID);
        //productVendorEditRequest.setDate_and_time(currentDateandTime);
        productVendorEditRequest.setMobile_type("Android");
        return productVendorEditRequest;
    }

    public void showErrorLoading(String errormesage){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(errormesage);
        alertDialogBuilder.setPositiveButton("ok",
                (arg0, arg1) -> hideLoading());



        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void hideLoading(){
        try {
            alertDialog.dismiss();
        }catch (Exception ignored){

        }
    }
}