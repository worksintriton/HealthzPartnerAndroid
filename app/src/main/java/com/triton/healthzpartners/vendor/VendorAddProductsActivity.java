package com.triton.healthzpartners.vendor;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.triton.healthzpartners.R;
import com.triton.healthzpartners.activity.NotificationActivity;
import com.triton.healthzpartners.adapter.CategoryTypesListAdapter;
import com.triton.healthzpartners.adapter.VendorAddProductsAdapter;
import com.triton.healthzpartners.api.APIClient;
import com.triton.healthzpartners.api.RestApiInterface;

import com.triton.healthzpartners.interfaces.CategoryTypeSelectListener;
import com.triton.healthzpartners.requestpojo.FetctProductByCatRequest;
import com.triton.healthzpartners.responsepojo.CatgoryGetListResponse;
import com.triton.healthzpartners.responsepojo.FetctProductByCatDetailsResponse;
import com.triton.healthzpartners.sessionmanager.SessionManager;
import com.triton.healthzpartners.utils.ConnectionDetector;
import com.triton.healthzpartners.utils.RestUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VendorAddProductsActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, CategoryTypeSelectListener {

     String TAG = "VendorAddProductsActivity";

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_back)
    ImageView img_back;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.avi_indicator)
    AVLoadingIndicatorView avi_indicator;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rv_manage_productlist)
    RecyclerView rv_manage_productlist;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_no_records)
    TextView txt_no_records;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.txt_category_type)
    TextView txt_category_type;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_notification)
    ImageView img_notification;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.img_profile)
    ImageView img_profile;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.include_vendor_footer)
    View include_vendor_footer;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rl_category)
    RelativeLayout rl_category;


    private String strCatTypeId;

    /* Bottom Navigation */

    private List<CatgoryGetListResponse.DataBean> catgoryGetList;

    HashMap<String, String> hashMap_CatTypeid = new HashMap<>();
    private String strCatType;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bottomNavigation)
    BottomNavigationView bottomNavigation;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    public static final int PAGE_START = 1;
    private int CURRENT_PAGE = PAGE_START;
    private boolean isLoading = false;
    private List<FetctProductByCatDetailsResponse.DataBean> catListSeeMore;
    private final List<FetctProductByCatDetailsResponse.DataBean>  catListSeeMoreAll = new ArrayList<>();
    private Button btn_done;
    private RecyclerView rv_categorytype;
    private EditText edt_search_categorytype;
    private TextView txt_category_norecords;
    private CategoryTypesListAdapter categoryTypesListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_add_products);

        ButterKnife.bind(this);

        SessionManager session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getProfileDetails();
        String userid = user.get(SessionManager.KEY_ID);

        floatingActionButton.setImageResource(R.drawable.ic_hzhome_png);

        floatingActionButton.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),VendorDashboardActivity.class)));

        bottomNavigation.getMenu().getItem(0).setCheckable(false);
        bottomNavigation.setOnNavigationItemSelectedListener(this);



        img_back.setOnClickListener(v -> onBackPressed());

        img_notification.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
            finish();

        });
        img_profile.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),VendorProfileScreenActivity.class);
            intent.putExtra("fromactivity",TAG);
            startActivity(intent);


        });

/*
        spr_category_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int arg2, long arg3) {
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.green));
                strCatType = spr_category_type.getSelectedItem().toString();
                strCatTypeId = hashMap_CatTypeid.get(strCatType);

                Log.w(TAG,"strCatType : "+strCatType+" strCatTypeId :"+strCatTypeId);
                  if(strCatTypeId != null) {
                      if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
                          fetctProductByCatDetailsResponse(strCatTypeId);
                      }
                  }else{
                      if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
                          fetctProductByCatDetailsResponse("");
                      }
                  }


            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
*/




        if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
            getlistCatResponseCall();
        }

        initResultRecylerView();

        rl_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupCategoryType();
            }
        });





    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),VendorDashboardActivity.class));
        finish();
    }

    @SuppressLint("LogNotTimber")
    public void getlistCatResponseCall(){
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        //Creating an object of our api interface
        RestApiInterface apiInterface = APIClient.getClient().create(RestApiInterface.class);
        Call<CatgoryGetListResponse> call = apiInterface.getlistCatResponseCall(RestUtils.getContentType());
        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<CatgoryGetListResponse>() {
            @SuppressLint("LogNotTimber")
            @Override
            public void onResponse(@NonNull Call<CatgoryGetListResponse> call, @NonNull Response<CatgoryGetListResponse> response) {
                avi_indicator.smoothToHide();
                if (response.body() != null) {
                    if(200 == response.body().getCode()){
                        Log.w(TAG,"CatgoryGetListResponse" + new Gson().toJson(response.body()));
                        if(response.body().getData() != null) {
                            catgoryGetList = response.body().getData();
                        }
                        if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
                            fetctProductByCatDetailsResponse("");
                        }


                    }



                }

            }
            @Override
            public void onFailure(@NonNull Call<CatgoryGetListResponse> call,@NonNull  Throwable t) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"CatgoryGetListResponse flr"+t.getMessage());
            }
        });

    }

    private void setCategoryTypeView(List<CatgoryGetListResponse.DataBean> catgoryGetList) {
        rv_categorytype.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rv_categorytype.setItemAnimator(new DefaultItemAnimator());
         categoryTypesListAdapter = new CategoryTypesListAdapter(getApplicationContext(), catgoryGetList,this);
        rv_categorytype.setAdapter(categoryTypesListAdapter);
    }

    @SuppressLint("LogNotTimber")
    private void setCategoryType(List<CatgoryGetListResponse.DataBean> catgoryGetList) {
        ArrayList<String> cattypeArrayList = new ArrayList<>();
        cattypeArrayList.add("Select Category Type");
        for (int i = 0; i < catgoryGetList.size(); i++) {
            String catType = catgoryGetList.get(i).getProduct_cate_name();
            hashMap_CatTypeid.put(catgoryGetList.get(i).getProduct_cate_name(), catgoryGetList.get(i).get_id());
            Log.w(TAG,"catType-->"+catType);
            cattypeArrayList.add(catType);
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(VendorAddProductsActivity.this, R.layout.spinner_item, cattypeArrayList);
            spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item); // The drop down view
            //spr_category_type.setAdapter(spinnerArrayAdapter);

        }
    }

    @SuppressLint("LogNotTimber")
    public void fetctProductByCatDetailsResponse(String strCatTypeId){
        avi_indicator.setVisibility(View.VISIBLE);
        avi_indicator.smoothToShow();
        //Creating an object of our api interface
        RestApiInterface ApiService = APIClient.getClient().create(RestApiInterface.class);
        Call<FetctProductByCatDetailsResponse> call = ApiService.fetctProductByCatDetailsResponse(RestUtils.getContentType(),fetctProductByCatRequest(strCatTypeId));

        Log.w(TAG,"url  :%s"+ call.request().url().toString());

        call.enqueue(new Callback<FetctProductByCatDetailsResponse>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<FetctProductByCatDetailsResponse> call, @NonNull Response<FetctProductByCatDetailsResponse> response) {
                avi_indicator.smoothToHide();


                if (response.body() != null) {
                    if(200 == response.body().getCode()){
                        Log.w(TAG,"FetctProductByCatDetailsResponse" + new Gson().toJson(response.body()));

                     /*   if(response.body().getData()!= null && response.body().getData().size()>0){
                            fetctProductByCatDetailsList = response.body().getData();
                            txt_no_records.setVisibility(View.GONE);
                            rv_manage_productlist.setVisibility(View.VISIBLE);
                            setViewProducts ();

                        }
                        else{
                            rv_manage_productlist.setVisibility(View.GONE);
                            txt_no_records.setVisibility(View.VISIBLE);
                            txt_no_records.setText("No products found");
                        }*/

                        if(response.body().getData()!= null && response.body().getData().size()>0){
                            catListSeeMore = response.body().getData();
                            for(int i=0;i<catListSeeMore.size();i++) {
                                /*
                                 * _id : 60e5aabd5af36c5c3605bab4
                                 * product_img : http://54.212.108.156:3000/api/uploads/1625748054901.png
                                 * product_title : HUL Natural Shampoo for Puppy
                                 * product_price : 180
                                 * thumbnail_image : http://54.212.108.156:3000/api/uploads/1625748027413.png
                                 * product_discount : 10
                                 * product_discount_price : 0
                                 * product_fav : false
                                 * product_rating : 5
                                 * product_review : 0
                                 */
                                FetctProductByCatDetailsResponse.DataBean  dataBean = new FetctProductByCatDetailsResponse.DataBean();
                                dataBean.set_id(catListSeeMore.get(i).get_id());
                                dataBean.setImg_path(catListSeeMore.get(i).getImg_path());
                                dataBean.setProduct_cate(catListSeeMore.get(i).getProduct_cate());
                                //dataBean.setc(catListSeeMore.get(i).getProduct_discription());
                                //dataBean.setStatus(catListSeeMore.get(i).isStatus());
                                catListSeeMoreAll.add(dataBean);


                            }
                            Log.w(TAG,"catListSeeMoreAll : "+new Gson().toJson(catListSeeMoreAll));
                            Log.w(TAG,"catListSeeMoreAll size : "+catListSeeMoreAll.size());
                            setViewProducts(catListSeeMoreAll);

                        }
                        if (catListSeeMore != null) {
                            catListSeeMore.size();
                        }


                    }
                }

            }


            @Override
            public void onFailure(@NonNull Call<FetctProductByCatDetailsResponse> call, @NonNull  Throwable t) {
                avi_indicator.smoothToHide();
                Log.w(TAG,"FetctProductByCatDetailsResponse flr"+t.getMessage());
            }
        });

    }
    @SuppressLint("LogNotTimber")
    private FetctProductByCatRequest fetctProductByCatRequest(String strCatTypeId) {
        /*
         * cat_id : 5fec14a5ea832e2e73c1fc79
         * skip_count : 1
         */

        FetctProductByCatRequest fetctProductByCatRequest = new FetctProductByCatRequest();
        fetctProductByCatRequest.setCat_id(strCatTypeId);
        fetctProductByCatRequest.setSkip_count(CURRENT_PAGE);
        Log.w(TAG,"fetctProductByCatRequest"+ "--->" + new Gson().toJson(fetctProductByCatRequest));
        return fetctProductByCatRequest;
    }


    public boolean validdSelectCategoryType() {
        if(strCatType.equalsIgnoreCase("Select Category Type")){
            final AlertDialog alertDialog = new AlertDialog.Builder(VendorAddProductsActivity.this).create();
            alertDialog.setMessage(getString(R.string.err_msg_type_of_cattype));
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Ok",
                    (dialog, which) -> alertDialog.cancel());
            alertDialog.show();

            return false;
        }

        return true;
    }

    @SuppressLint("LogNotTimber")
    private void setViewProducts(List<FetctProductByCatDetailsResponse.DataBean> catListSeeMoreAll) {
        Log.w(TAG,"setViewProducts catListSeeMoreAll : "+new Gson().toJson(catListSeeMoreAll));
        Log.w(TAG,"setViewProducts catListSeeMoreAll size : "+catListSeeMoreAll.size());
        rv_manage_productlist.setLayoutManager(new GridLayoutManager(this, 2));
        rv_manage_productlist.setItemAnimator(new DefaultItemAnimator());
        VendorAddProductsAdapter vendorAddProductsAdapter = new VendorAddProductsAdapter(getApplicationContext(), catListSeeMoreAll,TAG,"");
        rv_manage_productlist.setAdapter(vendorAddProductsAdapter);
        isLoading = false;
    }

    public void callDirections(String tag){
        Intent intent = new Intent(getApplicationContext(), VendorDashboardActivity.class);
        intent.putExtra("tag",tag);
        startActivity(intent);
        finish();
    }

    private void initResultRecylerView() {
        rv_manage_productlist.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @SuppressLint("LogNotTimber")
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);


                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();



                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == catListSeeMoreAll.size() - 1) {
                        CURRENT_PAGE += 1;
                        Log.w(TAG, "isLoading? " + isLoading + " currentPage " + CURRENT_PAGE);
                        isLoading = true;
                        if(strCatTypeId != null) {
                            if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
                                fetctProductByCatDetailsResponse(strCatTypeId);
                            }
                        }else{
                            if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
                                fetctProductByCatDetailsResponse("");
                            }
                        }

                    }
                }
            }
        });
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

            default:
                return  false;
        }
        return true;
    }

    private void showPopupCategoryType() {
        try {

            Dialog dialog = new Dialog(VendorAddProductsActivity.this);
            dialog.setContentView(R.layout.alert_categorytype_layout);
            dialog.setCanceledOnTouchOutside(false);

            ImageView img_close = dialog.findViewById(R.id.img_close);
            btn_done = dialog.findViewById(R.id.btn_done);
            rv_categorytype = dialog.findViewById(R.id.rv_categorytype);
            edt_search_categorytype = dialog.findViewById(R.id.edt_search_categorytype);
            txt_category_norecords = dialog.findViewById(R.id.txt_category_norecords);

            btn_done.setVisibility(View.GONE);
            btn_done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });





            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                }
            });

            if(catgoryGetList != null && catgoryGetList.size()>0){
                setCategoryTypeView(catgoryGetList);
                rv_categorytype.setVisibility(View.VISIBLE);
                txt_category_norecords.setVisibility(View.GONE);
            }
            else{
                rv_categorytype.setVisibility(View.GONE);
                txt_category_norecords.setVisibility(View.VISIBLE);
                txt_category_norecords.setText("No category type");
            }


            edt_search_categorytype.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @SuppressLint("LogNotTimber")
                @Override
                public void afterTextChanged(Editable editable) {
                    //after the change calling the method and passing the search input
                    filter(editable.toString());
                    Log.w(TAG,"afterTextChanged : "+editable.toString());
                }
            });



            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();

        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }




    }

    private void filter(String text) {
        //new array list that will hold the filtered data
        List<CatgoryGetListResponse.DataBean> catgoryGetListFiltered = new ArrayList<>();


        if(catgoryGetList !=null && catgoryGetList.size()>0){
            //looping through existing elements
            for (CatgoryGetListResponse.DataBean s : catgoryGetList) {
                //if the existing elements contains the search input
                if (s.getProduct_cate_name().toLowerCase().contains(text.toLowerCase())) {
                    //adding the element to filtered list
                    catgoryGetListFiltered.add(s);
                }
            }

        }

        //calling a method of the adapter class and passing the filtered list
        categoryTypesListAdapter.filterList(catgoryGetListFiltered);
    }


    @SuppressLint("LogNotTimber")
    @Override
    public void categoryTypeSelectListener(String categorytitle, String categoryid) {
        Log.w(TAG,"categoryTypeSelectListener : "+"categorytitle : "+categorytitle+" categoryid : "+categoryid);
        btn_done.setVisibility(View.VISIBLE);
        txt_category_type.setText(categorytitle);
        strCatTypeId = categoryid;
           if(strCatTypeId != null) {
                      if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
                          fetctProductByCatDetailsResponse(strCatTypeId);
                      }
                  }else{
                      if (new ConnectionDetector(getApplicationContext()).isNetworkAvailable(getApplicationContext())) {
                          fetctProductByCatDetailsResponse("");
                      }
                  }
    }
}
