package com.triton.healthzpartners.vendor;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.canhub.cropper.CropImage;
import com.google.android.gms.common.util.IOUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.triton.healthzpartners.R;
import com.triton.healthzpartners.adapter.AddProdDetailsListAdapter;
import com.triton.healthzpartners.adapter.EditImageListAdapter;
import com.triton.healthzpartners.api.APIClient;
import com.triton.healthzpartners.api.RestApiInterface;
import com.triton.healthzpartners.appUtils.NumericKeyBoardTransformationMethod;
import com.triton.healthzpartners.requestpojo.ProductVendorEditRequest;
import com.triton.healthzpartners.responsepojo.CatgoryGetListResponse;
import com.triton.healthzpartners.responsepojo.FetctProductByCatDetailsResponse;
import com.triton.healthzpartners.responsepojo.FileUploadResponse;
import com.triton.healthzpartners.responsepojo.ManageProductsListResponse;
import com.triton.healthzpartners.responsepojo.SuccessResponse;
import com.triton.healthzpartners.utils.ConnectionDetector;
import com.triton.healthzpartners.utils.RestUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    ArrayList<ProductVendorEditRequest.ProductImgBean> imgList = new ArrayList();
    private String productcategoryname;
    private ArrayList<String> additionalDetailList;
    private String condition;
    private String pricetype;
    String productcategoryid;

    @SuppressLint({"SetTextI18n", "LogNotTimber"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_manage_prodcuts);
        ButterKnife.bind(this);

        edt_product_thresould.setTransformationMethod(new NumericKeyBoardTransformationMethod());


        avi_indicator.setVisibility(View.GONE);

        Intent intent = getIntent();

        Bundle args = intent.getBundleExtra("productImageList");

        if(args!=null&&!args.isEmpty()){

            productImageList = (ArrayList<ManageProductsListResponse.DataBean.ProductImgBean>) args.getSerializable("PRODUCTLIST");

            Log.w(TAG,"productImageList : "+new Gson().toJson(productImageList));
        }

        if(productImageList!=null){

            for(int i=0; i<productImageList.size();i++){

                ProductVendorEditRequest.ProductImgBean productImgBean = new ProductVendorEditRequest.ProductImgBean();
                productImgBean.setProduct_img(productImageList.get(i).getProduct_img());
                imgList.add(productImgBean);
            }

            if(imgList!=null){

                setView(imgList);
            }
        }

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            productid = extras.getString("productid");
            productcategoryid = extras.getString("productcategoryid");
            producttitle = extras.getString("producttitle");
            productprice = extras.getInt("productprice");
            productthreshold = extras.getString("productthreshold");
            productdesc = extras.getString("productdesc");
           // productImageList = (ArrayList<ManageProductsListResponse.DataBean.ProductImgBean>) getIntent().getSerializableExtra("productImageList");
            additionalDetailList = getIntent().getExtras().getStringArrayList("additionalDetailList");
            Log.w(TAG,"additionalDetailList : "+new Gson().toJson(additionalDetailList));
            productcategoryname = extras.getString("productcategoryname");
            condition = extras.getString("condition");
            pricetype = extras.getString("pricetype");


            if(additionalDetailList!=null){

                rv_add_additional_details.setVisibility(View.VISIBLE);
                rv_add_additional_details.setHasFixedSize(true);
                rv_add_additional_details.setNestedScrollingEnabled(false);
                setViewDetails(additionalDetailList);
            }
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

            ll_main_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    chooseImage();
                }
            });


            btn_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(!(edt_addmore_service.getText().toString().equals(""))){

                        insert(edt_addmore_service.getText().toString());
                    }

                    else {

                        Toasty.warning(getApplicationContext(),"Plz add details",Toast.LENGTH_SHORT).show();

                    }
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

        else if (edt_product_condition.getText().toString().trim().equals("")) {
            edt_product_condition.setError("Please enter product condition");
            edt_product_condition.requestFocus();
            can_proceed = false;
        }

        else if (edt_product_pricetype.getText().toString().trim().equals("")) {
            edt_product_pricetype.setError("Please enter price type");
            edt_product_pricetype.requestFocus();
            can_proceed = false;
        }

        else if (imgList.size()==0) {
            Toasty.warning(getApplicationContext(), "Please add minimum one image", Toast.LENGTH_SHORT, true).show();
            can_proceed = false;
        }

        else if (additionalDetailList.size()==0) {
            Toasty.warning(getApplicationContext(), "Please add minimum one details", Toast.LENGTH_SHORT, true).show();
            can_proceed = false;
        }


        if (can_proceed) {
            if (new ConnectionDetector(EditManageProdcutsActivity.this).isNetworkAvailable(EditManageProdcutsActivity.this)) {
                productEditResponseCall();
            }

        }





    }

    private void insert(String service) {

        if(additional_detail.size()>=3){
            Toasty.warning(getApplicationContext(),"Sorry you cannot add more than 3",Toast.LENGTH_SHORT).show();

        }

        else {
            Toasty.success(getApplicationContext(),"Details added successfully",Toast.LENGTH_SHORT).show();
            edt_addmore_service.setText("");
            additionalDetailList.add(service);

            if(additionalDetailList != null && additionalDetailList.size()>0){
                rv_add_additional_details.setVisibility(View.VISIBLE);
                rv_add_additional_details.setHasFixedSize(true);
                rv_add_additional_details.setNestedScrollingEnabled(false);
                setViewDetails(additionalDetailList);

            }else{
                rv_add_additional_details.setVisibility(View.GONE);

            }
        }

    }

    private void setViewDetails(ArrayList<String> additionalDetailList) {
        rv_add_additional_details.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_add_additional_details.setItemAnimator(new DefaultItemAnimator());
        AddProdDetailsListAdapter addExpAdapter = new AddProdDetailsListAdapter(getApplicationContext(), additionalDetailList);
        rv_add_additional_details.setAdapter(addExpAdapter);
    }




    private void chooseImage() {

        if (imgList!=null && imgList.size() >= 4) {

            Toasty.warning(getApplicationContext(), "Sorry you can't Add more than 4", Toast.LENGTH_SHORT).show();

        } else {


            if (!hasPermissions(this, PERMISSIONS)) {
                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_CLINIC);
            }


            else
            {


                CropImage.activity().start(EditManageProdcutsActivity.this);


            }


        }

    }


    private boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @SuppressLint("LogNotTimber")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //	Toast.makeText(getActivity(),"kk",Toast.LENGTH_SHORT).show();

        try {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUriContent();

                    if (resultUri != null) {

                        Log.w("selectedImageUri", " " + resultUri);

                        String filename = getFileName(resultUri);

                        Log.w("filename", " " + filename);

                        String filePath = getFilePathFromURI(EditManageProdcutsActivity.this, resultUri);

                        assert filePath != null;

                        File file = new File(filePath); // initialize file here

                        long length = file.length() / 1024; // Size in KB

                        Log.w("filesize", " " + length);

                        if (length > 2000) {

                            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("File Size")
                                    .setContentText("Please choose file size less than 2 MB ")
                                    .setConfirmText("Ok")
                                    .show();
                        } else {


                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
                            String currentDateandTime = sdf.format(new Date());

                            filePart = MultipartBody.Part.createFormData("sampleFile", userid + currentDateandTime + filename, RequestBody.create(MediaType.parse("image/*"), file));

                            uploadImage();

                        }


                    } else {

                        Toasty.warning(EditManageProdcutsActivity.this, "Image Error!!Please upload Some other image", Toasty.LENGTH_LONG).show();
                    }


                }
            }


        }


        catch (Exception e){
            Log.w(TAG,"onActivityResult exception"+e.toString());
        }





    }

    private void uploadImage() {

        avi_indicator.show();

        RestApiInterface apiInterface = APIClient.getImageClient().create(RestApiInterface.class);


        Call<FileUploadResponse> call = apiInterface.getImageStroeResponse(filePart);


        Log.w(TAG, "url  :%s" + call.request().url().toString());

        call.enqueue(new Callback<FileUploadResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<FileUploadResponse> call, @NonNull Response<FileUploadResponse> response) {
                avi_indicator.smoothToHide();

                if (response.body() != null) {
                    if (200 == response.body().getCode()) {
                        Log.w(TAG, "Profpic" + "--->" + new Gson().toJson(response.body()));
/*

                        DocBusInfoUploadRequest.ClinicPicBean clinicPicBean = new DocBusInfoUploadRequest.ClinicPicBean(response.body().getData().trim());
                        clinicPicBeans.add(clinicPicBean);
*/

                        uploadimagepath = response.body().getData();
                        ProductVendorEditRequest.ProductImgBean productImgBean = new ProductVendorEditRequest.ProductImgBean();
                        productImgBean.setProduct_img(uploadimagepath);
                        imgList.add(productImgBean);
                        if (uploadimagepath != null) {
                            setView(imgList);
                        }


                    }

                }


            }

            @SuppressLint("LogNotTimber")
            @Override
            public void onFailure(@NonNull Call<FileUploadResponse> call, @NonNull Throwable t) {
                // avi_indicator.smoothToHide();
                Log.w(TAG, "ServerUrlImagePath" + "On failure working" + t.getMessage());
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setView(ArrayList<ProductVendorEditRequest.ProductImgBean> productImageList) {
        rv_product_list.setVisibility(View.VISIBLE);
        rv_product_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //rv_upload_pet_images.setLayoutManager(new LinearLayoutManager(this));
        rv_product_list.setItemAnimator(new DefaultItemAnimator());
        EditImageListAdapter imageListAdapter = new EditImageListAdapter(getApplicationContext(), productImageList);
        rv_product_list.setAdapter(imageListAdapter);
    }

    public static String getFilePathFromURI(Context context, Uri contentUri) {
        //copy file and send new file path
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            String path = context.getFilesDir() + "/" + "MyFirstApp/";

            //String path = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS).getPath() + "/" + "MyFirstApp/";
            // Create the parent path
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fullName = path + "mylog";
            File copyFile = new File (fullName);

            //  File copyFile = new File(Environment.DIRECTORY_DOWNLOADS + File.separator + fileName);

            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }

    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            IOUtils.copyStream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
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


        /**
         * _id : 61a614dcd681bf5291cb9b34
         * addition_detail : ["good product"]
         * cat_id : 6198b594518ad4520ab14792
         * condition : organic
         * cost : 200
         * date_and_time : 20/12/2021 03:51 PM
         * mobile_type : Android
         * price_type : unit price
         * product_discription : nice product
         * product_img : [{"product_img":"http://35.165.75.97:3000/api/uploads/1639995610197.jpg"}]
         * product_name : organic food in india
         * threshould : 66
         * thumbnail_image : http://35.165.75.97:3000/api/uploads/1639995610197.jpg
         * user_id : 61af3cea7a64122107fc8e49
         */



        int myCost = 0;

        try {
            myCost = Integer.parseInt(edt_product_price.getText().toString());
        } catch(NumberFormatException nfe) {
            System.out.println("Could not parse " + nfe);
        }

        String thumbnail_image;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        if(imgList!=null&&imgList.size()>0){

            thumbnail_image = imgList.get(0).getProduct_img();

        }

        else {

            thumbnail_image="";
        }



        ProductVendorEditRequest productVendorEditRequest = new ProductVendorEditRequest();
        productVendorEditRequest.set_id(productid);
        productVendorEditRequest.setCost(myCost);
        productVendorEditRequest.setThreshould(edt_product_thresould.getText().toString());
        productVendorEditRequest.setProduct_name(edt_product_title.getText().toString());
        productVendorEditRequest.setProduct_discription(edt_product_descriptions.getText().toString());
        productVendorEditRequest.setCat_id(productcategoryid);
        productVendorEditRequest.setProduct_img(imgList);
       productVendorEditRequest.setThumbnail_image(thumbnail_image);
        productVendorEditRequest.setProduct_name(edt_product_title.getText().toString());
        productVendorEditRequest.setCondition(edt_product_condition.getText().toString());
        productVendorEditRequest.setAddition_detail(additionalDetailList);
        productVendorEditRequest.setPrice_type(edt_product_pricetype.getText().toString());
        productVendorEditRequest.setCost(Integer.parseInt(edt_product_price.getText().toString()));
        productVendorEditRequest.setThreshould(edt_product_thresould.getText().toString());
        productVendorEditRequest.setProduct_discription(edt_product_descriptions.getText().toString());
        productVendorEditRequest.setUser_id(APIClient.VENDOR_ID);
      productVendorEditRequest.setDate_and_time(currentDateandTime);
        productVendorEditRequest.setMobile_type("Android");
        Log.w(TAG,"productVendorEditRequest"+ "--->" + new Gson().toJson(productVendorEditRequest));
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