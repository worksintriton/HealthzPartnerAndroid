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
import android.view.MenuItem;
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
import com.triton.healthzpartners.activity.NotificationActivity;
import com.triton.healthzpartners.adapter.AddProdDetailsListAdapter;
import com.triton.healthzpartners.adapter.ImageListAdapter;
import com.triton.healthzpartners.api.APIClient;
import com.triton.healthzpartners.api.RestApiInterface;
import com.triton.healthzpartners.appUtils.NumericKeyBoardTransformationMethod;
import com.triton.healthzpartners.requestpojo.ProductVendorCreateRequest;
import com.triton.healthzpartners.responsepojo.CatgoryGetListResponse;
import com.triton.healthzpartners.responsepojo.FetctProductByCatDetailsResponse;
import com.triton.healthzpartners.responsepojo.FileUploadResponse;
import com.triton.healthzpartners.responsepojo.SuccessResponse;
import com.triton.healthzpartners.sessionmanager.SessionManager;
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

public class VendorCreateProductsActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private  String TAG = "VendorCreateProductsActivity";

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

    List<ProductVendorCreateRequest.ProductImgBean> imgList = new ArrayList();
    List<String> additional_detail = new ArrayList();
    private static final int REQUEST_CLINIC_CAMERA_PERMISSION_CODE = 785;
    private static final int SELECT_CLINIC_CAMERA = 1000;
    private static final int REQUEST_READ_CLINIC_PIC_PERMISSION = 786;
    private static final int SELECT_CLINIC_PICTURE = 1001;

    MultipartBody.Part filePart;

    String uploadimagepath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_create_products);

        Log.w(TAG,"onCreate : ");

        Log.w(TAG,"VendorID : "+APIClient.VENDOR_ID);

        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {

            productid = extras.getString("productid");
            producttitle = extras.getString("producttitle");
            productdesc = extras.getString("productdesc");
            productimage = extras.getString("productimage");



            if(producttitle != null){
                txt_product_categprod.setText(producttitle);
            }
           /* if(productdesc != null){
                edt_product_descriptions.setText(productdesc);
            }*/


            btn_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addProductValidator();
                }
            });

            img_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });


        }

        img_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                finish();

            }
        });
        img_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(),VendorProfileScreenActivity.class);
                        intent.putExtra("fromactivity",TAG);
                        intent.putExtra("productid",productid);
                        intent.putExtra("producttitle",producttitle);
                        intent.putExtra("productdesc",productdesc);
                        intent.putExtra("productimage",productimage);
                        startActivity(intent);


                    }
                });

        edt_product_thresould.setTransformationMethod(new NumericKeyBoardTransformationMethod());


        SessionManager session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getProfileDetails();
        userid = user.get(SessionManager.KEY_ID);




        img_back.setOnClickListener(v -> onBackPressed());

        fab = include_vendor_footer.findViewById(R.id.fab);
        bottom_navigation_view = include_vendor_footer.findViewById(R.id.bottomNavigation);
        bottom_navigation_view.setOnNavigationItemSelectedListener(this);
        bottom_navigation_view.getMenu().findItem(R.id.shop).setChecked(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDirections("1");
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

    private void insert(String service) {

        if(additional_detail.size()>=3){
            Toasty.warning(getApplicationContext(),"Sorry you cannot add more than 3",Toast.LENGTH_SHORT).show();

        }

        else {
            Toasty.success(getApplicationContext(),"Details added successfully",Toast.LENGTH_SHORT).show();
           additional_detail.add(service);

            if(additional_detail != null && additional_detail.size()>0){
                rv_add_additional_details.setVisibility(View.VISIBLE);
                rv_add_additional_details.setHasFixedSize(true);
                rv_add_additional_details.setNestedScrollingEnabled(false);
                setViewDetails();

            }else{
                rv_add_additional_details.setVisibility(View.GONE);

            }
        }

    }

    private void setViewDetails() {
        rv_add_additional_details.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_add_additional_details.setItemAnimator(new DefaultItemAnimator());
        AddProdDetailsListAdapter addExpAdapter = new AddProdDetailsListAdapter(getApplicationContext(), additional_detail);
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


                CropImage.activity().start(VendorCreateProductsActivity.this);


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

                        String filePath = getFilePathFromURI(VendorCreateProductsActivity.this, resultUri);

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

                        Toasty.warning(VendorCreateProductsActivity.this, "Image Error!!Please upload Some other image", Toasty.LENGTH_LONG).show();
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
                        ProductVendorCreateRequest.ProductImgBean productImgBean = new ProductVendorCreateRequest.ProductImgBean();
                        productImgBean.setProduct_img(uploadimagepath);
                        imgList.add(productImgBean);
                        if (uploadimagepath != null) {
                            setView();
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

    private void setView() {
        rv_product_list.setVisibility(View.VISIBLE);
        rv_product_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        //rv_upload_pet_images.setLayoutManager(new LinearLayoutManager(this));
        rv_product_list.setItemAnimator(new DefaultItemAnimator());
        ImageListAdapter imageListAdapter = new ImageListAdapter(getApplicationContext(), imgList);
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
        startActivity(new Intent(getApplicationContext(),VendorAddProductsActivity.class));
        finish();
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                callDirections("1");
                break;
            case R.id.shop:
                callDirections("2");
                break;

            case R.id.community:
                callDirections("3");
                break;


        }
        return true;
    }


    public void callDirections(String tag){
        Intent intent = new Intent(getApplicationContext(), VendorDashboardActivity.class);
        intent.putExtra("tag",tag);
        startActivity(intent);
        finish();
    }

    public void addProductValidator() {
        boolean can_proceed = true;



        if (edt_product_price.getText().toString().isEmpty() && edt_product_descriptions.getText().toString().isEmpty() && edt_product_thresould.getText().toString().isEmpty() ) {
            Toasty.warning(getApplicationContext(), "Please enter the fields", Toast.LENGTH_SHORT, true).show();
            can_proceed = false;
        } else if (edt_product_price.getText().toString().trim().equals("")) {
            edt_product_price.setError("Please enter product price");
            edt_product_price.requestFocus();
            can_proceed = false;
        }
        else if (edt_product_thresould.getText().toString().trim().equals("")) {
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

        else if (additional_detail.size()==0) {
            Toasty.warning(getApplicationContext(), "Please add minimum one details", Toast.LENGTH_SHORT, true).show();
            can_proceed = false;
        }

        if (can_proceed) {
            if (new ConnectionDetector(VendorCreateProductsActivity.this).isNetworkAvailable(VendorCreateProductsActivity.this)) {
                vendor_product_create_ResponseCall();
            }

        }





    }

    @SuppressLint({"LogNotTimber", "LongLogTag"})
    private void vendor_product_create_ResponseCall() {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<SuccessResponse> call = apiInterface.vendor_product_create_ResponseCall(RestUtils.getContentType(), productVendorCreateRequest());
        Log.w(TAG,"vendor_product_create_ResponseCall url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<SuccessResponse>() {
            @Override
            public void onResponse(@NonNull Call<SuccessResponse> call, @NonNull Response<SuccessResponse> response) {

                Log.w(TAG,"vendor_product_create_ResponseCall"+ "--->" + new Gson().toJson(response.body()));

                avi_indicator.smoothToHide();

                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        Toasty.success(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                        startActivity(new Intent(getApplicationContext(),VendorAddProductsActivity.class));
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
    private ProductVendorCreateRequest productVendorCreateRequest() {


        /**
         * vendor_id : 6198b507518ad4520ab14790
         * cat_id : 6198b507518ad4520ab14790
         * thumbnail_image : http://google.png
         * product_img : [{"product_img":"http://google.png"},{"product_img":"http://google.png"}]
         * product_name : Good Food
         * cost : 200
         * product_discription : This is good food
         * condition : Testing
         * price_type : Testing
         * addition_detail : ["testing1","testing2","testing3"]
         * date_and_time : 23-10-2021 11:00 AM
         * threshould : 1000
         * mobile_type : Admin
         */
        String thumbnail_image;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        if(imgList!=null&&imgList.size()>0){

            thumbnail_image = imgList.get(0).getProduct_img();

        }

        else {

            thumbnail_image="";
        }

        ProductVendorCreateRequest productVendorCreateRequest = new ProductVendorCreateRequest();
        productVendorCreateRequest.setCat_id(productid);
        productVendorCreateRequest.setProduct_img(imgList);
        productVendorCreateRequest.setThumbnail_image(thumbnail_image);
        productVendorCreateRequest.setProduct_name(edt_product_title.getText().toString());
        productVendorCreateRequest.setCondition(edt_product_condition.getText().toString());
        productVendorCreateRequest.setAddition_detail(additional_detail);
        productVendorCreateRequest.setPrice_type(edt_product_pricetype.getText().toString());
        productVendorCreateRequest.setCost(Integer.parseInt(edt_product_price.getText().toString()));
        productVendorCreateRequest.setThreshould(edt_product_thresould.getText().toString());
        productVendorCreateRequest.setProduct_discription(edt_product_descriptions.getText().toString());
        productVendorCreateRequest.setVendor_id(APIClient.VENDOR_ID);
        productVendorCreateRequest.setDate_and_time(currentDateandTime);
        productVendorCreateRequest.setMobile_type("Android");
        Log.w(TAG,"productEditRequest"+ "--->" + new Gson().toJson(productVendorCreateRequest));
        return productVendorCreateRequest;
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
