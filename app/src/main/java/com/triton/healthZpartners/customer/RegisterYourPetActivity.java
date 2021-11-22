package com.triton.healthZpartners.customer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.google.android.gms.common.util.IOUtils;
import com.google.gson.Gson;
import com.triton.healthZpartners.R;
import com.triton.healthZpartners.activity.LoginActivity;
import com.triton.healthZpartners.adapter.AddPetImageListAdapter;
import com.triton.healthZpartners.api.APIClient;
import com.triton.healthZpartners.api.RestApiInterface;

import com.triton.healthZpartners.requestpojo.PetAddImageRequest;
import com.triton.healthZpartners.responsepojo.FileUploadResponse;
import com.triton.healthZpartners.responsepojo.PetAddImageResponse;
import com.triton.healthZpartners.sessionmanager.SessionManager;
import com.triton.healthZpartners.utils.RestUtils;
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

public class RegisterYourPetActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RegisterYourPetActivity";

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

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rc_uploaded_pet_images)
    RecyclerView rc_uploaded_pet_images;

    private String petid;

    public final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;
    private static final String CAMERA_PERMISSION = CAMERA ;
    private static final String READ_EXTERNAL_STORAGE_PERMISSION = READ_EXTERNAL_STORAGE;
    private static final String WRITE_EXTERNAL_STORAGE_PERMISSION = WRITE_EXTERNAL_STORAGE;





    private String ServerUrlImagePath;

    private static final int REQUEST_READ_CLINIC_PIC_PERMISSION = 786;
    private static final int REQUEST_CLINIC_CAMERA_PERMISSION_CODE = 785 ;




    private static final int SELECT_CLINIC_CAMERA = 1000 ;

    private static final int SELECT_CLINIC_PICTURE = 1001 ;
    private MultipartBody.Part filePart;
    private Object userid;

    List<PetAddImageRequest.PetImgBean> pet_img = new ArrayList<>();
    AddPetImageListAdapter addPetImageListAdapter;

    int PERMISSION_CLINIC = 1;
    int PERMISSION_CERT = 2;
    int PERMISSION_GOVT = 3;
    int PERMISSION_PHOTO = 4;

    String[] PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_your_pet);
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
                    gotoPetloverDashboard();
                break;
                case R.id.txt_uploadpetimage:
                    gotoUploadPetImage();
                break;
                case R.id.img_pet_imge:
                      choosePetImage();
                break;

                case R.id.btn_continue:
                    PetAddImageResponseCall();
                break;
        }
    }

    private void gotoUploadPetImage() {
            choosePetImage();

    }

    private void gotoPetloverDashboard() {
        Intent intent = new Intent(RegisterYourPetActivity.this, CustomerDashboardActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterYourPetActivity.this, LoginActivity.class));
        finish();
    }




    private void choosePetImage() {

/*

            final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
            //AlertDialog.Builder alert=new AlertDialog.Builder(this);
            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterYourPetActivity.this);
            builder.setTitle("Choose option");
            builder.setItems(items, (dialog, item) -> {
                if (items[item].equals("Take Photo"))
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(RegisterYourPetActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CLINIC_CAMERA_PERMISSION_CODE);
                    }
                    else
                    {


                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        startActivityForResult(intent, SELECT_CLINIC_CAMERA);
                    }

                }

                else if (items[item].equals("Choose from Library"))
                {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(RegisterYourPetActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                    {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_CLINIC_PIC_PERMISSION);
                    }

                    else{

                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_CLINIC_PICTURE);


                    }
                }

                else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            });
            builder.show();
*/
        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_CLINIC);
        }

        else {


            CropImage.activity().start(RegisterYourPetActivity.this);

            /*CropImage.activity().start(AddYourPetImageOlduserActivity.this);*/
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUriContent();

                    if(resultUri!=null){

                        Log.w("selectedImageUri", " " + resultUri);

                        String filename = getFileName(resultUri);

                        Log.w("filename", " " + filename);

                        String filePath = getFilePathFromURI(RegisterYourPetActivity.this, resultUri);

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

                        Toasty.warning(RegisterYourPetActivity.this,"Image Error!!Please upload Some other image",Toasty.LENGTH_LONG).show();
                    }


                }
            }


        }

        catch (Exception e){
            Log.w(TAG,"onActivityResult exception"+e.toString());
        }

        //	Toast.makeText(getActivity(),"kk",Toast.LENGTH_SHORT).show();


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

                        if( response.body().getData() != null)
                        {

                            if(pet_img!=null&&pet_img.size()>=3){

                                Toasty.warning(RegisterYourPetActivity.this,"Sorry You can't Upload more than 3", Toasty.LENGTH_LONG).show();

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

                        else
                        {
                            Toasty.warning(RegisterYourPetActivity.this,"Failed to Upload", Toasty.LENGTH_LONG).show();

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

        LinearLayoutManager layoutManager = new LinearLayoutManager(RegisterYourPetActivity.this, LinearLayoutManager.HORIZONTAL, false);

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

                CropImage.activity().start(RegisterYourPetActivity.this);

            } else {
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Permisson Required")
                        .setContentText("Please Allow Permissions for choosing Images from Gallery ")
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



    private void PetAddImageResponseCall() {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<PetAddImageResponse> call = apiInterface.PetAddImageResponseCall(RestUtils.getContentType(), petAddImageRequest());
        Log.w(TAG,"PetAddImageResponse url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<PetAddImageResponse>() {
            @Override
            public void onResponse(@NonNull Call<PetAddImageResponse> call, @NonNull Response<PetAddImageResponse> response) {

                Log.w(TAG,"PetAddImageResponse"+ "--->" + new Gson().toJson(response.body()));

                avi_indicator.smoothToHide();

                if (response.body() != null) {
                    if(response.body().getCode() == 200){
                        gotoPetloverDashboard();
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
    private PetAddImageRequest petAddImageRequest() {



        PetAddImageRequest petAddImageRequest = new PetAddImageRequest();
        petAddImageRequest.set_id(petid);
        petAddImageRequest.setPet_img(pet_img);
        Log.w(TAG,"petAddImageRequest"+ "--->" + new Gson().toJson(petAddImageRequest));
        return petAddImageRequest;
    }
}