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


public class MainFragmentActivity extends AppCompatActivity {

    private FragmentPagerAdapter mAdapterViewPager;
    private ActivityMainBinding mBinding;
    private MainFragmentViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        mViewModel = new MainFragmentViewModel();
        mBinding.setMainFragmentViewModel(mViewModel);

        Toolbar toolbar = findViewById(R.id.mainToolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        ViewPager viewPager = findViewById(R.id.viewpager);

        mAdapterViewPager = new FragmentAdapter.MyPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapterViewPager);
        viewPager.setCurrentItem(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_menu_main,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this,LoginActivity.class));
                break;
        }

        return true;
    }
}
