package com.triton.healthzpartners.customer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.triton.healthzpartners.R;


import com.triton.healthzpartners.activity.NotificationActivity;
import com.triton.healthzpartners.fragmentcustomer.myappointments.FragmentPetMissedAppointment;
import com.triton.healthzpartners.fragmentcustomer.myappointments.FragmentPetCompletedAppointment;
import com.triton.healthzpartners.fragmentcustomer.myappointments.FragmentPetNewAppointment;
import com.triton.healthzpartners.responsepojo.PetLoverDashboardResponse;


import butterknife.BindView;
import butterknife.ButterKnife;


import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class PetMyappointmentsActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private String TAG = "PetMyappointmentsActivity";

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.include_petlover_footer)
    View include_petlover_footer;

    BottomNavigationView bottom_navigation_view;

    FloatingActionButton fab;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tablayout)
    TabLayout tabLayout;




    private String active_tag = "1";


    String tag;

    String fromactivity;
    private Dialog dialog;

    private static final int REQUEST_PHONE_CALL =1 ;
    private String sosPhonenumber;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.include_petlover_header)
    View include_petlover_header;




    String appintments;
    private int someIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_myappointments);
        ButterKnife.bind(this);
        Log.w(TAG,"onCreate");

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            appintments = bundle.getString("appintments");
            Log.w(TAG,"appintments : "+appintments);
        }

        ImageView img_back = include_petlover_header.findViewById(R.id.img_back);
        ImageView img_sos = include_petlover_header.findViewById(R.id.img_sos);
        ImageView img_notification = include_petlover_header.findViewById(R.id.img_notification);
        ImageView img_cart = include_petlover_header.findViewById(R.id.img_cart);
        ImageView img_profile = include_petlover_header.findViewById(R.id.img_profile);
        TextView toolbar_title = include_petlover_header.findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.my_appointments));

        setupViewPager(viewPager);

        if(appintments != null && appintments.equalsIgnoreCase("New")){
            someIndex = 0;
        }
        else if(appintments != null && appintments.equalsIgnoreCase("Completed")){
            someIndex = 1;
        }
        else if(appintments != null && appintments.equalsIgnoreCase("Missed")){
            someIndex = 2;
        }

        tabLayout.setupWithViewPager(viewPager);
        TabLayout.Tab tab = tabLayout.getTabAt(someIndex);
        tab.select();

        img_back.setOnClickListener(v -> onBackPressed());

        img_sos.setVisibility(View.GONE);
        img_cart.setVisibility(View.GONE);
        img_notification.setOnClickListener(this);
        img_cart.setOnClickListener(this);
        img_profile.setOnClickListener(this);

        fab = include_petlover_footer.findViewById(R.id.fab);

        bottom_navigation_view = include_petlover_footer.findViewById(R.id.bottomNavigation);
        bottom_navigation_view.setItemIconTintList(null);
        bottom_navigation_view.setOnNavigationItemSelectedListener(this);
        bottom_navigation_view.getMenu().findItem(R.id.home).setChecked(true);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDirections("1");
            }
        });





    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentPetNewAppointment(), "New");
        adapter.addFragment(new FragmentPetCompletedAppointment(), "Completed");
        adapter.addFragment(new FragmentPetMissedAppointment(), "Missed");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(PetMyappointmentsActivity.this, CustomerDashboardActivity.class);
        startActivity(i);
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
            case R.id.services:
                callDirections("3");
                break;
            case R.id.care:
                callDirections("4");
                break;
            case R.id.community:
                callDirections("5");
                break;

            default:
                return  false;
        }
        return true;
    }
    public void callDirections(String tag){
        Intent intent = new Intent(getApplicationContext(), CustomerDashboardActivity.class);
        intent.putExtra("tag",tag);
        startActivity(intent);
        finish();
    }



    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    private void showSOSAlert(List<PetLoverDashboardResponse.DataBean.SOSBean> sosList) {

        try {

            dialog = new Dialog(PetMyappointmentsActivity.this);
            dialog.setContentView(R.layout.sos_popup_layout);
            RecyclerView rv_sosnumbers = (RecyclerView)dialog.findViewById(R.id.rv_sosnumbers);
            Button btn_call = (Button)dialog.findViewById(R.id.btn_call);
            TextView txt_no_records = (TextView)dialog.findViewById(R.id.txt_no_records);
            ImageView img_close = (ImageView)dialog.findViewById(R.id.img_close);
            img_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });


            btn_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(PetMyappointmentsActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                    }
                    else
                    {
                        gotoPhone();
                    }

                }
            });



            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();


        } catch (WindowManager.BadTokenException e) {
            e.printStackTrace();
        }




    }
    private void gotoPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + sosPhonenumber));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){


            case R.id.img_notification:
                startActivity(new Intent(getApplicationContext(), NotificationActivity.class));
                break;
            case R.id.img_cart:
                break;
            case R.id.img_profile:
                Intent intent = new Intent(getApplicationContext(), CustomerProfileScreenActivity.class);
                intent.putExtra("fromactivity",TAG);
                startActivity(intent);
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


    private void setMargins(RelativeLayout rl_layout, int i, int i1, int i2, int i3) {

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)rl_layout.getLayoutParams();
        params.setMargins(i, i1, i2, i3);
        rl_layout.setLayoutParams(params);
    }

}