package com.triton.healthzpartners.customer;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.triton.healthzpartners.R;
import com.triton.healthzpartners.activity.NotificationActivity;
import com.triton.healthzpartners.fragmentcustomer.favourites.DoctorFavFragment;
import com.triton.healthzpartners.fragmentcustomer.favourites.SPFavFragment;
import com.triton.healthzpartners.fragmentcustomer.favourites.ShopFavFragment;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PetloverFavListActivity extends AppCompatActivity  implements View.OnClickListener {

    private String TAG = "PetloverFavListActivity";

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.include_petlover_footer)
    View include_petlover_footer;


    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tablayout)
    TabLayout tabLayout;




    private String active_tag = "1";







    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.include_petlover_header)
    View include_petlover_header;


/**/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_petlover_fav_list);

        ButterKnife.bind(this);
        Log.w(TAG,"onCreate");

        ImageView img_back = include_petlover_header.findViewById(R.id.img_back);
        ImageView img_sos = include_petlover_header.findViewById(R.id.img_sos);
        ImageView img_notification = include_petlover_header.findViewById(R.id.img_notification);
        ImageView img_cart = include_petlover_header.findViewById(R.id.img_cart);
        ImageView img_profile = include_petlover_header.findViewById(R.id.img_profile);
        TextView toolbar_title = include_petlover_header.findViewById(R.id.toolbar_title);
        toolbar_title.setText(getResources().getString(R.string.my_favourites));

        img_sos.setVisibility(View.GONE);
        img_cart.setVisibility(View.GONE);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        img_back.setOnClickListener(v -> onBackPressed());

        img_sos.setOnClickListener(this);
        img_notification.setOnClickListener(this);
        img_cart.setOnClickListener(this);
        img_profile.setOnClickListener(this);



       /**/


    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DoctorFavFragment(), "Doctor");
        adapter.addFragment(new SPFavFragment(), "Service");
        adapter.addFragment(new ShopFavFragment(), "Shop");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(PetloverFavListActivity.this, CustomerDashboardActivity.class);
        startActivity(i);
        finish();
    }

 
    public void callDirections(String tag){
        Intent intent = new Intent(getApplicationContext(), CustomerDashboardActivity.class);
        intent.putExtra("tag",tag);
        startActivity(intent);
        finish();
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


}