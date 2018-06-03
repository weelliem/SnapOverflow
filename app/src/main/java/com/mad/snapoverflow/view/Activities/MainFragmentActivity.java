/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      Author: 11025400
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.mad.snapoverflow.view.Activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.mad.snapoverflow.R;
import com.mad.snapoverflow.databinding.ActivityMainBinding;
import com.mad.snapoverflow.view.Adapters.FragmentAdapter;
import com.mad.snapoverflow.viewmodel.MainFragmentViewModel;

/* this is the main activity for which contains all the fragments. it controls and maintains the camera,map and fourm fragments */
public class MainFragmentActivity extends AppCompatActivity {

    private FragmentPagerAdapter mAdapterViewPager;
    private ActivityMainBinding mBinding;
    private MainFragmentViewModel mViewModel;


    /* on create lifecycle to connect the class to the activity */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        mViewModel = new MainFragmentViewModel();
        mBinding.setMainFragmentViewModel(mViewModel);

        //the toolbar or action bar bindings
        Toolbar toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ViewPager viewPager = findViewById(R.id.viewpager);

        //this adaptor manages the fragments
        mAdapterViewPager = new FragmentAdapter.MyPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapterViewPager);
        viewPager.setCurrentItem(1);
    }


    /* basically creates the layout of the actionbar */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu_main,menu);

        return true;
    }

    /* the overflow options for the action bar  */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut(); //allows the user to logout though firebase
                finish();
                startActivity(new Intent(this,LoginActivity.class));
                break;
        }

        return true;
    }
}
