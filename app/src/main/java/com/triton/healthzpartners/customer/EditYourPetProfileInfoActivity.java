package com.triton.healthzpartners.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.triton.healthzpartners.R;
import com.triton.healthzpartners.api.APIClient;
import com.triton.healthzpartners.api.RestApiInterface;
import com.triton.healthzpartners.requestpojo.BreedTypeRequest;
import com.triton.healthzpartners.requestpojo.PetEditRequest;
import com.triton.healthzpartners.responsepojo.BreedTypeResponse;
import com.triton.healthzpartners.responsepojo.DropDownListResponse;
import com.triton.healthzpartners.responsepojo.PetAddImageResponse;
import com.triton.healthzpartners.responsepojo.PetListResponse;
import com.triton.healthzpartners.responsepojo.PetTypeListResponse;
import com.triton.healthzpartners.sessionmanager.SessionManager;
import com.triton.healthzpartners.utils.ConnectionDetector;
import com.triton.healthzpartners.utils.RestUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditYourPetProfileInfoActivity extends AppCompatActivity {

    private String TAG = "EditYourPetProfileInfoActivity";
    private boolean vaccinatedstatus,defaultstatus;
    private String petid,userid,petimage,petname,pettype,petbreed,petgender,petcolor;
    private double petweight;

    private String petAgeandMonth = "";



    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.avi_indicator)
    AVLoadingIndicatorView avi_indicator;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rgvaccinated)
    RadioGroup rgvaccinated;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.radioButton_Yes)
    RadioButton radioButton_Yes;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.radioButton_No)
    RadioButton radioButton_No;

    private String selectedRadioButton;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_back)
    ImageView img_back;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_petname)
    EditText edt_petname;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.sprpettype)
    Spinner sprpettype;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.sprpetbreed)
    Spinner sprpetbreed;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.sprpetgender)
    Spinner sprpetgender;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_petcolor)
    EditText edt_petcolor;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_petweight)
    EditText edt_petweight;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rl_petdob)
    RelativeLayout rl_petdob;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_petdob)
    TextView txt_petdob;



    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rlpetlastvaccinatedagedate)
    RelativeLayout rlpetlastvaccinatedagedate;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.llpetlastvaccinatedagedate)
    LinearLayout llpetlastvaccinatedagedate;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_petlastvaccinatedage)
    TextView txt_petlastvaccinatedage;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_save_changes)
    Button btn_save_changes;


    private List<DropDownListResponse.DataBean.GenderBean> genderTypeList;

    private String strPetType;
    private String strPetBreedType;
    private String strPetGenderType;

    private int year, month, day;
    String SelectedLastVaccinateddate = "";
    private static final int DATE_PICKER_ID = 0 ;
    private static final int PET_DATE_PICKER_ID = 1 ;

    private Dialog alertDialog;

    private List<PetTypeListResponse.DataBean.UsertypedataBean> usertypedataBeanList;



    HashMap<String, String> hashMap_PetTypeid = new HashMap<>();
    private String petTypeId;
    private List<BreedTypeResponse.DataBean> breedTypedataBeanList;
    private String petdob;
    String SelectedPetDOB = "";


    private boolean pet_spayed;
    private boolean pet_purebred;
    private boolean pet_frnd_with_dog;
    private boolean pet_frnd_with_cat;
    private boolean pet_frnd_with_kit;
    private boolean pet_microchipped;
    private boolean pet_tick_free;
    private boolean pet_private_part;
    List<PetListResponse.DataBean.PetImgBean> petImgBeanList;

    @SuppressLint({"LogNotTimber", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_your_pet_profile_info);
        ButterKnife.bind(this);
        SessionManager sessionManager = new SessionManager(EditYourPetProfileInfoActivity.this);
        HashMap<String, String> user = sessionManager.getProfileDetails();
        userid = user.get(SessionManager.KEY_ID);
        Log.w(TAG,"userid--->"+userid);
        avi_indicator.setVisibility(View.GONE);

        edt_petweight.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(4,2)});

        Intent intent = getIntent();

        Bundle args = intent.getBundleExtra("petimage");

        if(args!=null&&!args.isEmpty()){

            petImgBeanList = (ArrayList<PetListResponse.DataBean.PetImgBean>) args.getSerializable("PETLIST");
        }

        Log.w(TAG , petImgBeanList.toString());

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            petid = extras.getString("id");
            userid = extras.getString("userid");
           // petimage = extras.getString("petimage");
            petname = extras.getString("petname");
            strPetType = extras.getString("pettype");
            strPetBreedType = extras.getString("petbreed");
            strPetGenderType = extras.getString("petgender");
            petcolor = extras.getString("petcolor");
            petweight = extras.getDouble("petweight");
            vaccinatedstatus = extras.getBoolean("vaccinatedstatus");
            SelectedLastVaccinateddate = extras.getString("vaccinateddate");
            defaultstatus = extras.getBoolean("defaultstatus");
            petdob = extras.getString("petdob");

           pet_spayed = extras.getBoolean("pet_spayed");
           pet_purebred  = extras.getBoolean("pet_purebred");
           pet_frnd_with_dog  = extras.getBoolean("pet_frnd_with_dog");
           pet_frnd_with_cat  = extras.getBoolean("pet_frnd_with_cat");
           pet_frnd_with_kit  = extras.getBoolean("pet_frnd_with_kit");
           pet_microchipped  = extras.getBoolean("pet_microchipped");
           pet_tick_free  = extras.getBoolean("pet_tick_free");
           pet_private_part  = extras.getBoolean("pet_private_part");

            Log.w(TAG,"strPetType : "+strPetType+" strPetBreedType : "+strPetBreedType+" strPetGenderType : "+strPetGenderType);

            if(petname != null){
                edt_petname.setText(petname);
            }

            if(petcolor != null){
                edt_petcolor.setText(petcolor);
            }


            if(petweight != 0){
                edt_petweight.setText(petweight+"");
            }
            if(SelectedLastVaccinateddate != null){
                txt_petlastvaccinatedage.setText(SelectedLastVaccinateddate);
            }

            if(petdob != null){
                txt_petdob.setText(petdob);
            }


            if(vaccinatedstatus){
                radioButton_Yes.setChecked(true);
                rlpetlastvaccinatedagedate.setVisibility(View.VISIBLE);
                llpetlastvaccinatedagedate.setVisibility(View.VISIBLE);
            }else{
                radioButton_No.setChecked(true);
                rlpetlastvaccinatedagedate.setVisibility(View.GONE);
                llpetlastvaccinatedagedate.setVisibility(View.GONE);

            }

            rgvaccinated.setOnCheckedChangeListener((group, checkedId) -> {
                int radioButtonID = rgvaccinated.getCheckedRadioButtonId();
                RadioButton radioButton = rgvaccinated.findViewById(radioButtonID);
                selectedRadioButton = (String) radioButton.getText();
                Log.w(TAG,"selectedRadioButton" + selectedRadioButton);


                if(selectedRadioButton.equalsIgnoreCase("Yes")){
                    vaccinatedstatus = true;
                    rlpetlastvaccinatedagedate.setVisibility(View.VISIBLE);
                    llpetlastvaccinatedagedate.setVisibility(View.VISIBLE);
                }
                else{
                    vaccinatedstatus = false;
                    rlpetlastvaccinatedagedate.setVisibility(View.GONE);
                    llpetlastvaccinatedagedate.setVisibility(View.GONE);

                }


            });




        }


        if (new ConnectionDetector(EditYourPetProfileInfoActivity.this).isNetworkAvailable(EditYourPetProfileInfoActivity.this)) {
            petTypeListResponseCall();

        }
        sprpettype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int arg2, long arg3) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.green));
                strPetType = sprpettype.getSelectedItem().toString();
                petTypeId = hashMap_PetTypeid.get(strPetType);
                breedTypeResponseByPetIdCall(petTypeId);

                Log.w(TAG,"petTypeId : "+petTypeId+" strPetType :"+strPetType);


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        sprpetbreed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int arg2, long arg3) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.green));
                strPetBreedType = sprpetbreed.getSelectedItem().toString();
                Log.w(TAG,"strPetBreedType :"+strPetBreedType);


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        sprpetgender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int arg2, long arg3) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.green));
                strPetGenderType = sprpetgender.getSelectedItem().toString();
                Log.w(TAG,"strPetGenderType :"+strPetGenderType);


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        rlpetlastvaccinatedagedate.setOnClickListener(v -> SelectDate());
        btn_save_changes.setOnClickListener(v -> addYourPetValidator());
        img_back.setOnClickListener(v -> onBackPressed());

        rl_petdob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectPetDOB();
            }
        });
    }


    public void addYourPetValidator() {
        boolean can_proceed = true;

        int petnamelength = edt_petname.getText().toString().trim().length();
        int petweightlength = edt_petweight.getText().toString().trim().length();

        if (Objects.requireNonNull(edt_petname.getText()).toString().trim().equals("") && Objects.requireNonNull(edt_petweight.getText()).toString().trim().equals("")) {
            Toasty.warning(getApplicationContext(), "Please enter the fields", Toast.LENGTH_SHORT, true).show();
            can_proceed = false;
        } else if (edt_petname.getText().toString().trim().equals("")) {
            edt_petname.setError("Please enter pet name");
            edt_petname.requestFocus();
            can_proceed = false;
        }else if (petnamelength > 25) {
            edt_petname.setError("The maximum length for an pet name is 25 characters.");
            edt_petname.requestFocus();
            can_proceed = false;
        }
        else if (edt_petweight.getText().toString().trim().equals("")) {
            edt_petweight.setError("Please enter pet weight");
            edt_petweight.requestFocus();
            can_proceed = false;
        }
        else if (petweightlength > 5) {
            edt_petweight.setError("The maximum length for an pet weight is 5 characters.");
            edt_petweight.requestFocus();
            can_proceed = false;
        }


        if (can_proceed) {
            if (new ConnectionDetector(EditYourPetProfileInfoActivity.this).isNetworkAvailable(EditYourPetProfileInfoActivity.this)) {

                if(validdSelectPetType() && validdSelectPetBreedType() && validdSelectPetGenderType() ) {
                    petUpdateResponseCall();
                }
            }

        }





    }
    public void dropDownListResponseCall(){
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        //Creating an object of our api interface
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<DropDownListResponse> call = apiInterface.dropDownListResponseCall(RestUtils.getContentType());
        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<DropDownListResponse>() {
            @Override
            public void onResponse(@NonNull Call<DropDownListResponse> call, @NonNull Response<DropDownListResponse> response) {
                avi_indicator.smoothToHide();


                if (response.body() != null) {
                    if(200 == response.body().getCode()){
                        Log.w(TAG,"DropDownListResponse" + new Gson().toJson(response.body()));
                        if(response.body().getData().getGender() != null) {
                            genderTypeList = response.body().getData().getGender();
                        }
                        if(genderTypeList != null && genderTypeList.size()>0){
                            setPetGenderType(genderTypeList);
                        }


                    }

                }








            }


            @Override
            public void onFailure(@NonNull Call<DropDownListResponse> call,@NonNull  Throwable t) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"DropDownListResponse flr"+t.getMessage());
            }
        });

    }
    private void setPetGenderType(List<DropDownListResponse.DataBean.GenderBean> genderTypeList) {
        ArrayList<String> petGendertypeArrayList = new ArrayList<>();
        petGendertypeArrayList.add("Select Pet Gender");
        for (int i = 0; i < genderTypeList.size(); i++) {

            String petGenderType = genderTypeList.get(i).getGender();
            Log.w(TAG,"petGenderType-->"+petGenderType);
            petGendertypeArrayList.add(petGenderType);

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(EditYourPetProfileInfoActivity.this, R.layout.spinner_item, petGendertypeArrayList);
            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item); // The drop down view
            sprpetgender.setAdapter(spinnerArrayAdapter);

            if (strPetGenderType != null) {
                int spinnerPosition = spinnerArrayAdapter.getPosition(strPetGenderType);
                sprpetgender.setSelection(spinnerPosition);
            }

        }
    }
    private void SelectDate() {

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        showDialog(DATE_PICKER_ID);

    }
    private void SelectPetDOB() {

        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);


        showDialog(PET_DATE_PICKER_ID);

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == DATE_PICKER_ID) {// open datepicker dialog.
            // set date picker for current date
            // add pickerListener listner to date picker
            // return new DatePickerDialog(this, pickerListener, year, month,day);
            DatePickerDialog dialog = new DatePickerDialog(this, pickerListener, year, month, day);
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            return dialog;
        }else if (id == PET_DATE_PICKER_ID) {
            // open datepicker dialog.
            // set date picker for current date
            // add pickerListener listner to date picker
            // return new DatePickerDialog(this, pickerListener, year, month,day);
            DatePickerDialog dialog = new DatePickerDialog(this, petdobpickerListener, year, month, day);
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            return dialog;
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;



            String strdayOfMonth;
            String strMonth;
            int month1 =(month + 1);
            if(day == 9 || day <9){
                strdayOfMonth = "0"+day;
                Log.w(TAG,"Selected dayOfMonth-->"+strdayOfMonth);
            }else{
                strdayOfMonth = String.valueOf(day);
            }

            if(month1 == 9 || month1 <9){
                strMonth = "0"+month1;
                Log.w(TAG,"Selected month1-->"+strMonth);
            }else{
                strMonth = String.valueOf(month1);
            }

            SelectedLastVaccinateddate = strdayOfMonth + "-" + strMonth + "-" + year;

            // Show selected date
            txt_petlastvaccinatedage.setText(SelectedLastVaccinateddate);

        }
    };

    private final DatePickerDialog.OnDateSetListener petdobpickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @SuppressLint("LogNotTimber")
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;



            String strdayOfMonth;
            String strMonth;
            int month1 =(month + 1);
            if(day == 9 || day <9){
                strdayOfMonth = "0"+day;
                Log.w(TAG,"Selected dayOfMonth-->"+strdayOfMonth);
            }else{
                strdayOfMonth = String.valueOf(day);
            }

            if(month1 == 9 || month1 <9){
                strMonth = "0"+month1;
                Log.w(TAG,"Selected month1-->"+strMonth);
            }else{
                strMonth = String.valueOf(month1);
            }

            getAge(year,month1,day);

            SelectedPetDOB = strdayOfMonth + "-" + strMonth + "-" + year;

            // Show selected date
            txt_petdob.setText(SelectedPetDOB);

        }
    };


    public boolean validdSelectPetType() {
        if(strPetType.equalsIgnoreCase("Select Pet Type")){
            final AlertDialog alertDialog = new AlertDialog.Builder(EditYourPetProfileInfoActivity.this).create();
            alertDialog.setMessage(getString(R.string.err_msg_type_of_pettype));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    (dialog, which) -> alertDialog.cancel());
            alertDialog.show();

            return false;
        }

        return true;
    }
    public boolean validdSelectPetBreedType() {
        if(strPetBreedType.equalsIgnoreCase("Select Pet Breed")){
            final AlertDialog alertDialog = new AlertDialog.Builder(EditYourPetProfileInfoActivity.this).create();
            alertDialog.setMessage(getString(R.string.err_msg_type_of_petbreedtype));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    (dialog, which) -> alertDialog.cancel());
            alertDialog.show();

            return false;
        }

        return true;
    }
    public boolean validdSelectPetGenderType() {
        if(strPetGenderType.equalsIgnoreCase("Select Pet Gender")){
            final AlertDialog alertDialog = new AlertDialog.Builder(EditYourPetProfileInfoActivity.this).create();
            alertDialog.setMessage(getString(R.string.err_msg_type_of_petgendertype));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    (dialog, which) -> alertDialog.cancel());
            alertDialog.show();

            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //startActivity(new Intent(EditYourPetProfileInfoActivity.this, CustomerProfileScreenActivity.class));
        finish();
    }


    public void petTypeListResponseCall(){
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        //Creating an object of our api interface
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<PetTypeListResponse> call = apiInterface.petTypeListResponseCall(RestUtils.getContentType());
        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<PetTypeListResponse>() {
            @Override
            public void onResponse(@NonNull Call<PetTypeListResponse> call, @NonNull Response<PetTypeListResponse> response) {
                avi_indicator.smoothToHide();


                if (response.body() != null) {
                    if(200 == response.body().getCode()){
                        Log.w(TAG,"PetTypeListResponse" + new Gson().toJson(response.body()));
                        dropDownListResponseCall();
                        if(response.body().getData().getUsertypedata() != null) {
                            usertypedataBeanList = response.body().getData().getUsertypedata();
                        }
                        if(usertypedataBeanList != null && usertypedataBeanList.size()>0){
                            setPetType(usertypedataBeanList);
                        }
                    }



                }








            }


            @Override
            public void onFailure(@NonNull Call<PetTypeListResponse> call,@NonNull  Throwable t) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"PetTypeListResponse flr"+t.getMessage());
            }
        });

    }
    private void setPetType(List<PetTypeListResponse.DataBean.UsertypedataBean> usertypedataBeanList) {
        ArrayList<String> pettypeArrayList = new ArrayList<>();
        pettypeArrayList.add("Select Pet Type");
        for (int i = 0; i < usertypedataBeanList.size(); i++) {

            String petType = usertypedataBeanList.get(i).getPet_type_title();
            hashMap_PetTypeid.put(usertypedataBeanList.get(i).getPet_type_title(), usertypedataBeanList.get(i).get_id());

            Log.w(TAG,"petType-->"+petType);
            pettypeArrayList.add(petType);

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(EditYourPetProfileInfoActivity.this, R.layout.spinner_item, pettypeArrayList);
            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item); // The drop down view
            sprpettype.setAdapter(spinnerArrayAdapter);

            if (strPetType != null) {
                int spinnerPosition = spinnerArrayAdapter.getPosition(strPetType);
                sprpettype.setSelection(spinnerPosition);
            }


        }
    }

    @SuppressLint("LogNotTimber")
    private void breedTypeResponseByPetIdCall(String petTypeId) {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<BreedTypeResponse> call = ApiService.breedTypeResponseByPetIdCall(RestUtils.getContentType(),breedTypeRequest(petTypeId));
        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<BreedTypeResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<BreedTypeResponse> call, @NonNull Response<BreedTypeResponse> response) {
                avi_indicator.smoothToHide();
                Log.w(TAG, "BreedTypeResponse" + "--->" + new Gson().toJson(response.body()));


                if (response.body() != null) {
                    if (200 == response.body().getCode()) {
                        if(response.body().getData() != null) {
                            breedTypedataBeanList = response.body().getData();
                        }
                        if(breedTypedataBeanList != null && breedTypedataBeanList.size()>0){
                            setBreedType(breedTypedataBeanList);
                        }

                    }

                } else {

                }


            }



            @Override
            public void onFailure(@NonNull Call<BreedTypeResponse> call, @NonNull Throwable t) {
                avi_indicator.smoothToHide();

                Log.w(TAG,"BreedTypeResponse flr"+"--->" + t.getMessage());
            }
        });

    }
    private void setBreedType(List<BreedTypeResponse.DataBean> breedTypedataBeanList) {
        ArrayList<String> pettypeArrayList = new ArrayList<>();
        pettypeArrayList.add("Select Pet Breed");
        for (int i = 0; i < breedTypedataBeanList.size(); i++) {

            String petType = breedTypedataBeanList.get(i).getPet_breed();

            Log.w(TAG,"petType-->"+petType);
            pettypeArrayList.add(petType);

            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(EditYourPetProfileInfoActivity.this, R.layout.spinner_item, pettypeArrayList);
            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item); // The drop down view
            sprpetbreed.setAdapter(spinnerArrayAdapter);

            if (strPetBreedType != null) {
                int spinnerPosition = spinnerArrayAdapter.getPosition(strPetBreedType);
                sprpetbreed.setSelection(spinnerPosition);
            }


        }
    }
    private BreedTypeRequest breedTypeRequest(String petTypeId) {
        BreedTypeRequest breedTypeRequest = new BreedTypeRequest();
        breedTypeRequest.setPet_type_id(petTypeId);
        Log.w(TAG,"breedTypeRequest"+ "--->" + new Gson().toJson(breedTypeRequest));
        return breedTypeRequest;
    }


    private void petUpdateResponseCall() {
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<PetAddImageResponse> call = apiInterface.petUpdateResponseCall(RestUtils.getContentType(), petEditRequest());
        Log.w(TAG,"PetAddImageResponse url  :%s"+" "+ call.request().url().toString());

        call.enqueue(new Callback<PetAddImageResponse>() {
            @Override
            public void onResponse(@NonNull Call<PetAddImageResponse> call, @NonNull Response<PetAddImageResponse> response) {
                Log.w(TAG,"PetAddImageResponse"+ "--->" + new Gson().toJson(response.body()));
                avi_indicator.smoothToHide();
                if (response.body() != null) {
                    if(response.body().getCode() == 200){

                        Intent intent = new Intent(getApplicationContext(),PetEditOtherInformationsActivity.class);
                        intent.putExtra("petid",petid);
                        intent.putExtra("userid",userid);
                        Bundle args = new Bundle();
                        args.putSerializable("PETLIST",(Serializable)petImgBeanList);
                        intent.putExtra("petimage",args);
                        intent.putExtra("pet_spayed",pet_spayed);
                        intent.putExtra("pet_purebred",pet_purebred);
                        intent.putExtra("pet_frnd_with_dog",pet_frnd_with_dog);
                        intent.putExtra("pet_frnd_with_cat",pet_frnd_with_cat);
                        intent.putExtra("pet_frnd_with_kit",pet_frnd_with_kit);
                        intent.putExtra("pet_microchipped",pet_microchipped);
                        intent.putExtra("pet_tick_free",pet_tick_free);
                        intent.putExtra("pet_private_part",pet_private_part);
                        startActivity(intent);

                        /*Intent intent = new Intent(getApplicationContext(),EditYourPetImageActivity.class);
                        intent.putExtra("petid",petid);
                        intent.putExtra("userid",userid);
                        intent.putExtra("petimage",petimage);
                        startActivity(intent);*/
                    }
                    else{
                        showErrorLoading(response.body().getMessage());
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
    private PetEditRequest petEditRequest() {
        /*
         * _id:
         * user_id : 5fb36ca169f71e30a0ffd3f7
         * pet_name : POP
         * pet_type : Dog
         * pet_breed : breed 1
         * pet_gender : Male
         * pet_color : white
         * pet_weight : 120
         * vaccinated : true
         * last_vaccination_date : 23-10-1996
         * default_status : true
         * date_and_time : 23-10-1996 12:09 AM
         * pet_age : 2 years 2 month
         * pet_dob : 23-10-2012
         * mobile_type: android
         */

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        PetEditRequest petEditRequest = new PetEditRequest();
        petEditRequest.set_id(petid);
        petEditRequest.setUser_id(userid);

        petEditRequest.setPet_name(edt_petname.getText().toString());
        petEditRequest.setPet_type(strPetType);
        petEditRequest.setPet_breed(strPetBreedType);
        petEditRequest.setPet_gender(strPetGenderType);
        petEditRequest.setPet_color(edt_petcolor.getText().toString());
        petEditRequest.setPet_weight(Double.parseDouble(edt_petweight.getText().toString()));
        petEditRequest.setPet_age(petAgeandMonth);
        petEditRequest.setPet_dob(txt_petdob.getText().toString());
        petEditRequest.setVaccinated(vaccinatedstatus);
        petEditRequest.setLast_vaccination_date(SelectedLastVaccinateddate);
        petEditRequest.setDefault_status(defaultstatus);
        petEditRequest.setDate_and_time(currentDateandTime);
        petEditRequest.setMobile_type("Android");
        Log.w(TAG,"petEditRequest"+ "--->" + new Gson().toJson(petEditRequest));
        return petEditRequest;
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

    @SuppressLint("LogNotTimber")
    private void getAge(int year, int month, int day){
        Log.w(TAG,"getAge : year "+year+" month : "+ month+" day : "+day);
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        Log.w(TAG,"age : "+age+" todayyear : "+today.get(Calendar.YEAR)+" dobyear : "+ dob.get(Calendar.YEAR));


        int months = dob.get(Calendar.MONTH) - today.get(Calendar.MONTH);
        int currentmonths = (today.get(Calendar.MONTH))+1;
        Log.w(TAG,"dob months: "+dob.get(Calendar.MONTH)+" currentmonths : "+ currentmonths);

        Log.w(TAG," todayyear : "+today.get(Calendar.YEAR)+" dobyear : "+ dob.get(Calendar.YEAR));

        Log.w(TAG,"Conditions : "+(today.get(Calendar.YEAR) < dob.get(Calendar.YEAR)));
        if(today.get(Calendar.YEAR) < dob.get(Calendar.YEAR)){
            age--;
        }

        Log.w(TAG,"age: "+age+" monthsInt : "+ months);
        String ageS = Integer.toString(age);
        String monthsS = Integer.toString(months);

        Log.w(TAG,"ageS: "+ageS+" months : "+monthsS);

        if(age != 0){
            petAgeandMonth = ageS+" years "+monthsS+" months";
        }else{
            petAgeandMonth = monthsS+" months";

        }



        Log.w(TAG,"ageS: "+ageS+" months : "+monthsS);

    }
}