package com.triton.healthzpartners.customer;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.util.IOUtils;
import com.google.gson.Gson;
import com.triton.healthzpartners.R;
import com.triton.healthzpartners.adapter.AddPetImageListAdapter;
import com.triton.healthzpartners.api.APIClient;
import com.triton.healthzpartners.api.RestApiInterface;
import com.triton.healthzpartners.requestpojo.PetAddImageRequest;
import com.triton.healthzpartners.responsepojo.FileUploadResponse;
import com.triton.healthzpartners.responsepojo.PetAddImageResponse;
import com.triton.healthzpartners.sessionmanager.SessionManager;
import com.triton.healthzpartners.utils.RestUtils;
import com.canhub.cropper.CropImage;
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

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class AddYourPetImageOlduserActivity extends AppCompatActivity implements View.OnClickListener {
    private  String TAG = "AddYourPetImageOlduserActivity";

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_back)
    ImageView img_back;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_skip)
    TextView txt_skip;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_pet_imge)
    ImageView img_pet_imge;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.avi_indicator)
    AVLoadingIndicatorView avi_indicator;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_uploadpetimage)
    TextView txt_uploadpetimage;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_continue)
    Button btn_continue;

    private String petid;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rc_uploaded_pet_images)
    RecyclerView rc_uploaded_pet_images;


    public final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;
    private static final String CAMERA_PERMISSION = CAMERA ;
    private static final String READ_EXTERNAL_STORAGE_PERMISSION = READ_EXTERNAL_STORAGE;
    private static final String WRITE_EXTERNAL_STORAGE_PERMISSION = WRITE_EXTERNAL_STORAGE;





    private String ServerUrlImagePath;

    private static final int REQUEST_READ_CLINIC_PIC_PERMISSION = 786;
    private static final int REQUEST_CLINIC_CAMERA_PERMISSION_CODE = 785 ;

    List<PetAddImageRequest.PetImgBean> pet_img = new ArrayList<>();
    AddPetImageListAdapter addPetImageListAdapter;

    private static final int SELECT_CLINIC_CAMERA = 1000 ;

    private static final int SELECT_CLINIC_PICTURE = 1001 ;
    private MultipartBody.Part filePart;
    private String userid;

    private String selectedAppointmentType = "Normal";
    private String selectedVisitType = "";
    private String petId;
    private String doctorid;
    private String fromto;
    private String Payment_id = "";
    private String Doctor_ava_Date = "";
    private String selectedTimeSlot = "";
    private int amount;
    private String communicationtype = "";
    private String fromactivity;

    private String spid,catid,from;
    private String spuserid;
    private String selectedServiceTitle;
    private String petcolor;
    private double petweight;
    private String servicetime;
    private int serviceamount;
    private String petage;
    private int distance;
    private String SP_ava_Date;

    int PERMISSION_CLINIC = 1;
    int PERMISSION_CERT = 2;
    int PERMISSION_GOVT = 3;
    int PERMISSION_PHOTO = 4;

    String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };


    @SuppressLint("LogNotTimber")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_your_pet_image_old_user);
        Log.w(TAG,"onCreate ");
        ButterKnife.bind(this);
        avi_indicator.setVisibility(View.GONE);
        btn_continue.setVisibility(View.GONE);
        img_back.setOnClickListener(this);
        txt_skip.setOnClickListener(this);
        btn_continue.setOnClickListener(this);
        txt_uploadpetimage.setOnClickListener(this);
        img_pet_imge.setOnClickListener(this);

        SessionManager  session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getProfileDetails();

        userid = user.get(SessionManager.KEY_ID);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            petid = extras.getString("petid");
            doctorid = extras.getString("doctorid");
            fromactivity = extras.getString("fromactivity");
            fromto = extras.getString("fromto");
            Doctor_ava_Date = extras.getString("Doctor_ava_Date");
            selectedTimeSlot = extras.getString("selectedTimeSlot");
            amount = extras.getInt("amount");
            communicationtype = extras.getString("communicationtype");
            petId = extras.getString("petId");
            Log.w(TAG,"Bundle "+" doctorid : "+doctorid+" selectedTimeSlot : "+selectedTimeSlot+"communicationtype : "+communicationtype+" amount : "+amount+" fromactivity : "+fromactivity+" fromto : "+fromto);

            /*PetServiceAppointment_Doctor_Date_Time_Activity*/
            fromactivity = extras.getString("fromactivity");
            spid = extras.getString("spid");
            catid = extras.getString("catid");
            from = extras.getString("from");
            spuserid = extras.getString("spuserid");
            selectedServiceTitle = extras.getString("selectedServiceTitle");
            serviceamount = extras.getInt("serviceamount");
            servicetime = extras.getString("servicetime");
            SP_ava_Date = extras.getString("SP_ava_Date");
            selectedTimeSlot = extras.getString("selectedTimeSlot");
            distance = extras.getInt("distance");
            Log.w(TAG,"spid : "+spid +" catid : "+catid+" from : "+from+" serviceamount : "+serviceamount+" servicetime : "+servicetime+" SP_ava_Date : "+SP_ava_Date+" selectedTimeSlot : "+selectedTimeSlot);

            Log.w(TAG,"fromactivity : "+fromactivity+" from : "+from);

        }


    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                onBackPressed();
                break;
                case R.id.txt_skip:
                    gotoPetLoverProfileScreenActivity();
                break;

                case R.id.txt_uploadpetimage:
                   gotoUplodPetImage();
                break;

                case R.id.img_pet_imge:
                    choosePetImage();

                break;

                case R.id.btn_continue:
                    PetAddImageResponseCall();
                break;
        }
    }

    private void gotoUplodPetImage() {
            choosePetImage();
    }

    private void gotoPetLoverProfileScreenActivity() {
        if(fromactivity != null && fromactivity.equalsIgnoreCase("AddNewPetActivity")){
            Intent intent = new Intent(getApplicationContext(), ConsultationActivity.class);
            intent.putExtra("doctorid", doctorid);
            intent.putExtra("fromactivity", TAG);
            intent.putExtra("Doctor_ava_Date", Doctor_ava_Date);
            intent.putExtra("selectedTimeSlot", selectedTimeSlot);
            intent.putExtra("amount", amount);
            intent.putExtra("communicationtype", communicationtype);
            intent.putExtra("fromto", TAG);
            intent.putExtra("petId", petId);
            startActivity(intent);
        }else if(fromactivity != null && fromactivity.equalsIgnoreCase("PetServiceAppointment_Doctor_Date_Time_Activity")){
            Intent intent = new Intent(getApplicationContext(),ConsultationActivity.class);
            intent.putExtra("spid",spid);
            intent.putExtra("catid",catid);
            intent.putExtra("from",from);
            intent.putExtra("spuserid",spuserid);
            intent.putExtra("selectedServiceTitle",selectedServiceTitle);
            intent.putExtra("serviceamount",serviceamount);
            intent.putExtra("servicetime",servicetime);
            intent.putExtra("SP_ava_Date",SP_ava_Date);
            intent.putExtra("selectedTimeSlot",selectedTimeSlot);
            intent.putExtra("distance",distance);
            intent.putExtra("fromactivity",fromactivity);
            intent.putExtra("petid", petid);
            intent.putExtra("petId", petId);
            startActivity(intent);
        }else {
            Intent intent = new Intent(AddYourPetImageOlduserActivity.this, CustomerProfileScreenActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(fromactivity != null && fromactivity.equalsIgnoreCase("AddNewPetActivity")){
            Intent intent = new Intent(getApplicationContext(), ConsultationActivity.class);
            intent.putExtra("doctorid", doctorid);
            intent.putExtra("fromactivity", TAG);
            intent.putExtra("Doctor_ava_Date", Doctor_ava_Date);
            intent.putExtra("selectedTimeSlot", selectedTimeSlot);
            intent.putExtra("amount", amount);
            intent.putExtra("communicationtype", communicationtype);
            intent.putExtra("fromto", TAG);
            intent.putExtra("petId", petId);
            startActivity(intent);
        }else{
            startActivity(new Intent(AddYourPetImageOlduserActivity.this, CustomerProfileScreenActivity.class));
            finish();
        }

    }




    private void choosePetImage() {


        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_CLINIC);
        }

        else {


            CropImage.activity().start(AddYourPetImageOlduserActivity.this);

            /*CropImage.activity().start(AddYourPetImageOlduserActivity.this);*/
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        try{
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {

                    Uri resultUri = result.getUriContent();

                    if(resultUri!=null){

                        Log.w(TAG,"selectedImageUri" + " " + resultUri);

                        String filename = getFileName(resultUri);

                        Log.w(TAG,"filename"+ " " + filename);

                        String filePath = getFilePathFromURI(AddYourPetImageOlduserActivity.this, resultUri);

                        Log.w(TAG ,"filePath" + " " + filePath);

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

                            uploadPetImage();

                        }


                    }

                    else {

                        Toasty.warning(AddYourPetImageOlduserActivity.this,"Image Error!!Please upload Some other image",Toasty.LENGTH_LONG).show();
                    }

                }
            }

        }catch (Exception e){
            Log.w(TAG,"onActivityResult exception"+e.toString());
        }




    }




    private void uploadPetImage() {

        avi_indicator.show();

        RestApiInterface apiInterface = APIClient.getImageClient().create(RestApiInterface.class);


        Call<FileUploadResponse> call = apiInterface.getImageStroeResponse(filePart);


        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<FileUploadResponse>() {
            @Override
            public void onResponse(@NonNull Call<FileUploadResponse> call, @NonNull Response<FileUploadResponse> response) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"Profpic"+ "--->" + new Gson().toJson(response.body()));

                if (response.body() != null) {
                    if (200 == response.body().getCode()) {

                        ServerUrlImagePath = response.body().getData();
                        btn_continue.setVisibility(View.VISIBLE);

                        Log.w(TAG, "ServerUrlImagePath " + ServerUrlImagePath);


                        if(pet_img!=null&&pet_img.size()>=3){

                            Toasty.warning(AddYourPetImageOlduserActivity.this,"Sorry You can't Upload more than 3", Toasty.LENGTH_LONG).show();

                        }

                        else
                        {
                            PetAddImageRequest.PetImgBean petImgBean = new PetAddImageRequest.PetImgBean();

                            if(ServerUrlImagePath != null&&!ServerUrlImagePath.isEmpty())
                            {
                                petImgBean.setPet_img(ServerUrlImagePath);

                                pet_img.add(petImgBean);

                            }
                            else
                            {
                                petImgBean.setPet_img(APIClient.PROFILE_IMAGE_URL);

                                pet_img.add(petImgBean);

                            }


                            setView();

                        }




                    }

                }


            }

            @Override
            public void onFailure(@NonNull Call<FileUploadResponse> call, @NonNull Throwable t) {
                // avi_indicator.smoothToHide();
                Log.w(TAG,"ServerUrlImagePath"+ "On failure working"+ t.getMessage());
                //Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setView() {

        rc_uploaded_pet_images.setHasFixedSize(true);

        rc_uploaded_pet_images.setNestedScrollingEnabled(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(AddYourPetImageOlduserActivity.this, LinearLayoutManager.HORIZONTAL, false);

        rc_uploaded_pet_images.setLayoutManager(layoutManager);

        addPetImageListAdapter = new AddPetImageListAdapter(this, pet_img);

        rc_uploaded_pet_images.setAdapter(addPetImageListAdapter);

    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CLINIC) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                CropImage.activity().start(AddYourPetImageOlduserActivity.this);

            } else {
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Permisson Required")
                        .setContentText("Plz Allow Permissions for choosing Images from Gallery ")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(sDialog -> {

                            sDialog.dismissWithAnimation();


                            if (!hasPermissions(this, PERMISSIONS)) {
                                ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_CLINIC);
                            }


                        })
                        .setCancelButton("Cancel", sDialog -> {
                            sDialog.dismissWithAnimation();

                            showWarning(PERMISSION_CLINIC);
                        })
                        .show();

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

    private void showWarning(int REQUEST_PERMISSION_CODE) {

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Sorry!!")
                .setContentText("You Can't proceed further unless you allow permission")
                .setConfirmText("Ok")
                .setConfirmClickListener(sDialog -> {

                    sDialog.dismissWithAnimation();

                    if (!hasPermissions(this, PERMISSIONS)) {
                        ActivityCompat.requestPermissions(this, PERMISSIONS, REQUEST_PERMISSION_CODE);
                    }


                })
                .setCancelButton("Cancel", SweetAlertDialog::dismissWithAnimation)
                .show();
    }

    public static String getFilePathFromURI(Context context, Uri contentUri) {
        //copy file and send new file path
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {

            String path = context.getFilesDir() + "/" + "MyFirstApp/";
            // Create the parent path
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String fullName = path + "mylog";
            File copyFile = new File (fullName);

            /* File copyFile = new File(Environment.DIRECTORY_DOWNLOADS + File.separator + fileName);*/
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



    @SuppressLint("LogNotTimber")
    private void PetAddImageResponseCall() {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<PetAddImageResponse> call = apiInterface.PetAddImageResponseCall(RestUtils.getContentType(), petAddImageRequest());
        Log.w(TAG,"PetAddImageResponse url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<PetAddImageResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<PetAddImageResponse> call, @NonNull Response<PetAddImageResponse> response) {

                Log.w(TAG,"PetAddImageResponse"+ "--->" + new Gson().toJson(response.body()));

                avi_indicator.smoothToHide();

                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        gotoPetLoverProfileScreenActivity();
                    }

                }


            }

            @Override
            public void onFailure(@NonNull Call<PetAddImageResponse> call, @NonNull Throwable t) {

                avi_indicator.smoothToHide();
                Log.w(TAG,"PetAddImageResponse flr"+"--->" + t.getMessage());
            }
        });

    }
    @SuppressLint("LogNotTimber")
    private PetAddImageRequest petAddImageRequest() {
        /*
         * _id : 603e098e2c2b43125f8cb7f8
         * pet_img : [{"pet_img":"http://54.212.108.156:3000/api/uploads/Pic_empty.jpg"},{"pet_img":"http://54.212.108.156:3000/api/uploads/Pic_empty.jpg"}]
         */

        PetAddImageRequest petAddImageRequest = new PetAddImageRequest();

        petAddImageRequest.set_id(petid);

        petAddImageRequest.setPet_img(pet_img);

        Log.w(TAG,"petAddImageRequest"+ "--->" + new Gson().toJson(petAddImageRequest));

        return petAddImageRequest;
    }
}