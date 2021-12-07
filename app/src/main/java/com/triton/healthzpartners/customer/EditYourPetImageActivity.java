package com.triton.healthzpartners.customer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import com.triton.healthzpartners.appUtils.FileUtil;
import com.triton.healthzpartners.requestpojo.PetAddImageRequest;
import com.triton.healthzpartners.responsepojo.FileUploadResponse;
import com.triton.healthzpartners.responsepojo.PetAddImageResponse;
import com.triton.healthzpartners.responsepojo.PetListResponse;
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
import static android.os.Environment.DIRECTORY_DOCUMENTS;

public class EditYourPetImageActivity extends AppCompatActivity implements View.OnClickListener {
    private  String TAG = "EditYourPetImageActivity";
    @BindView(R.id.img_back)
    ImageView img_back;

    @BindView(R.id.txt_skip)
    TextView txt_skip;


    @BindView(R.id.img_pet_imge)
    ImageView img_pet_imge;


    @BindView(R.id.avi_indicator)
    AVLoadingIndicatorView avi_indicator;

    @BindView(R.id.txt_change_petimage)
    TextView txt_change_petimage;

    @BindView(R.id.btn_continue)
    Button btn_continue;

    private String petid;
    private String petimage;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rc_uploaded_pet_images)
    RecyclerView rc_uploaded_pet_images;

    public final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 1;
    private static final String CAMERA_PERMISSION = CAMERA ;
    private static final String READ_EXTERNAL_STORAGE_PERMISSION = READ_EXTERNAL_STORAGE;
    private static final String WRITE_EXTERNAL_STORAGE_PERMISSION = WRITE_EXTERNAL_STORAGE;







    private static final int REQUEST_READ_CLINIC_PIC_PERMISSION = 786;
    private static final int REQUEST_CLINIC_CAMERA_PERMISSION_CODE = 785 ;




    private static final int SELECT_CLINIC_CAMERA = 1000 ;

    private static final int SELECT_CLINIC_PICTURE = 1001 ;
    private MultipartBody.Part filePart;
    private Object userid;
    List<PetListResponse.DataBean.PetImgBean> petImgBeanList;
    List<PetAddImageRequest.PetImgBean> pet_img = new ArrayList();
    AddPetImageListAdapter addPetImageListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_your_pet_image);
        Log.w(TAG,"onCreate ");
        ButterKnife.bind(this);
        avi_indicator.setVisibility(View.GONE);
        btn_continue.setVisibility(View.VISIBLE);
        img_back.setOnClickListener(this);
        txt_skip.setOnClickListener(this);
        btn_continue.setOnClickListener(this);
        txt_change_petimage.setOnClickListener(this);
        img_pet_imge.setOnClickListener(this);

        SessionManager  session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getProfileDetails();

        userid = user.get(SessionManager.KEY_ID);

        Intent i = getIntent();

        Bundle args = i.getBundleExtra("petimage");

        if(args!=null&&!args.isEmpty()){

            petImgBeanList = (ArrayList<PetListResponse.DataBean.PetImgBean>) args.getSerializable("PETLIST");
        }

        Log.w(TAG,petImgBeanList.toString());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            petid = extras.getString("petid");
        //    petimage = extras.getString("petimage");
        }

        for(int j=0; j<petImgBeanList.size();j++){

            PetAddImageRequest.PetImgBean petImgBean = new PetAddImageRequest.PetImgBean(petImgBeanList.get(j).getPet_img());

            pet_img.add(petImgBean);

        }

//        if(petimage != null){
//            Glide.with(EditYourPetImageActivity.this)
//                    .load(petimage)
//                    .into(img_pet_imge);
//        }else{
//            Glide.with(EditYourPetImageActivity.this)
//                    .load(R.drawable.image_thumbnail)
//                    .into(img_pet_imge);
//
//        }

        setView();
    }

    private void setView() {

        rc_uploaded_pet_images.setHasFixedSize(true);

        rc_uploaded_pet_images.setNestedScrollingEnabled(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(EditYourPetImageActivity.this, LinearLayoutManager.HORIZONTAL, false);

        rc_uploaded_pet_images.setLayoutManager(layoutManager);

        addPetImageListAdapter = new AddPetImageListAdapter(this, pet_img);

        rc_uploaded_pet_images.setAdapter(addPetImageListAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_back:
                onBackPressed();
                break;
                case R.id.txt_skip:
                    gotoPetLoverProfileScreenActivity();
                break;
                case R.id.txt_change_petimage:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        checkMultiplePermissions(REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS, EditYourPetImageActivity.this);
                    }else{
                        choosePetImage();

                    }
                break;
                case R.id.img_pet_imge:
                    changePetImage();
                break;
                case R.id.btn_continue:
                    PetAddImageResponseCall();
                break;
        }
    }

    private void changePetImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkMultiplePermissions(REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS, EditYourPetImageActivity.this);
        }else{
            choosePetImage();

        }
    }

    private void gotoPetLoverProfileScreenActivity() {
        Intent intent = new Intent(EditYourPetImageActivity.this, CustomerProfileScreenActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(EditYourPetImageActivity.this, CustomerProfileScreenActivity.class));
        finish();
    }




    private void choosePetImage() {

/*
            final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
            //AlertDialog.Builder alert=new AlertDialog.Builder(this);
            AlertDialog.Builder builder = new AlertDialog.Builder(EditYourPetImageActivity.this);
            builder.setTitle("Choose option");
            builder.setItems(items, (dialog, item) -> {
                if (items[item].equals("Take Photo"))
                {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(EditYourPetImageActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
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

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(EditYourPetImageActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
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
            builder.show();*/



    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        try {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUriContent();

                    if (resultUri != null) {

                        Log.w("selectedImageUri", " " + resultUri);

                        String filename = getFileName(resultUri);

                        Log.w("filename", " " + filename);

                        String filePath = getFilePathFromURI(EditYourPetImageActivity.this, resultUri);

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

                            filePart = MultipartBody.Part.createFormData("sampleFile", userid + currentDateandTime + file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

                            uploadPetImage();

                        }


                    } else {

                        Toasty.warning(EditYourPetImageActivity.this, "Image Error!!Please upload Some other image", Toasty.LENGTH_LONG).show();
                    }


                }
            }

            if(requestCode== SELECT_CLINIC_PICTURE || requestCode == SELECT_CLINIC_CAMERA)
            {

                if(requestCode == SELECT_CLINIC_CAMERA)
                {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");

              File file = new File(getFilesDir(), "Healthz1" + ".jpg");

                    OutputStream os;
                    try {
                        os = new FileOutputStream(file);
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, os);
                        os.flush();
                        os.close();
                    } catch (Exception e) {
                        Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
                    String currentDateandTime = sdf.format(new Date());

                    RequestBody requestFile = RequestBody.create(MediaType.parse("image*/"), file);

                    filePart = MultipartBody.Part.createFormData("sampleFile",  userid+currentDateandTime+file.getName(), requestFile);

                    uploadPetImage();

                }

                else{

                    try {
                        if (resultCode == Activity.RESULT_OK)
                        {

                            Log.w("VALUEEEEEEE1111", " " + data);

                            Uri selectedImageUri = data.getData();

                            Log.w("selectedImageUri", " " + selectedImageUri);

                            String filename = getFileName(selectedImageUri);

                            Log.w("filename", " " + filename);

                            String filePath = FileUtil.getPath(EditYourPetImageActivity.this,selectedImageUri);

                            assert filePath != null;

                            File file = new File(filePath); // initialize file here

                            long length = file.length() / 1024; // Size in KB

                            Log.w("filesize", " " + length);

                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
                            String currentDateandTime = sdf.format(new Date());

                            filePart = MultipartBody.Part.createFormData("sampleFile", userid+currentDateandTime+file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

                            uploadPetImage();


                        }
                    } catch (Exception e) {

                        Log.w("Exception", " " + e);
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

                        petimage = response.body().getData();
                        btn_continue.setVisibility(View.VISIBLE);

                        Log.w(TAG, "ServerUrlImagePath " + petimage);

                        if(pet_img.size()>=4){

                            Toasty.warning(EditYourPetImageActivity.this,"Sorry You can't Upload more than 4", Toasty.LENGTH_LONG).show();

                        }

                        else {

                            if (petimage != null && !petimage.isEmpty()) {

                                pet_img.add(new PetAddImageRequest.PetImgBean(petimage));

                            } else {

                                pet_img.add(new PetAddImageRequest.PetImgBean(APIClient.PROFILE_IMAGE_URL));

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



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_READ_CLINIC_PIC_PERMISSION) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_CLINIC_PICTURE);

            } else {
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Permisson Required")
                        .setContentText("Please Allow Permissions for choosing Images from Gallery ")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(sDialog -> {

                            sDialog.dismissWithAnimation();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{READ_EXTERNAL_STORAGE}, REQUEST_READ_CLINIC_PIC_PERMISSION);
                            }


                        })
                        .setCancelButton("Cancel", sDialog -> {
                            sDialog.dismissWithAnimation();

                            showWarning(REQUEST_READ_CLINIC_PIC_PERMISSION);
                        })
                        .show();

            }

        } else if (requestCode == REQUEST_CLINIC_CAMERA_PERMISSION_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent, SELECT_CLINIC_CAMERA);

            } else {
                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Permisson Required")
                        .setContentText("Please Allow Camera for taking picture")
                        .setConfirmText("Ok")
                        .setConfirmClickListener(sDialog -> {

                            sDialog.dismissWithAnimation();

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(new String[]{CAMERA}, REQUEST_CLINIC_CAMERA_PERMISSION_CODE);
                            }


                        })
                        .setCancelButton("Cancel", sDialog -> {
                            sDialog.dismissWithAnimation();

                            showWarning(REQUEST_CLINIC_CAMERA_PERMISSION_CODE);
                        })
                        .show();

            }

        }
    }

    //check for camera and storage access permissions
    @TargetApi(Build.VERSION_CODES.M)
    private void checkMultiplePermissions(int permissionCode, Context context) {

        String[] PERMISSIONS = {CAMERA_PERMISSION, READ_EXTERNAL_STORAGE_PERMISSION, WRITE_EXTERNAL_STORAGE_PERMISSION};
        if (!hasPermissions(context, PERMISSIONS)) {
            ActivityCompat.requestPermissions((Activity) context, PERMISSIONS, permissionCode);
        } else {
            choosePetImage();
            // Open your camera here.
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

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
                    }


                })
                .setCancelButton("Cancel", SweetAlertDialog::dismissWithAnimation)
                .show();
    }

    public static String getFilePathFromURI(Context context, Uri contentUri) {
        //copy file and send new file path
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {

            String path = Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS).getPath() + "/" + "MyFirstApp/";
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
                        gotoPetLoverProfileScreenActivity();
                    }
                    else{
                        //showErrorLoading(response.body().getMessage());
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