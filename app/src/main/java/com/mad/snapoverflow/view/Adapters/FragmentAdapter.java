package com.mad.snapoverflow.view.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mad.snapoverflow.view.Fragments.CameraActivityFragment;
import com.mad.snapoverflow.view.Fragments.FourmActivityFragment;
import com.mad.snapoverflow.view.Fragments.MapsFragmentActivity;

public class FragmentAdapter {
    public static class MyPageAdapter extends FragmentPagerAdapter {

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    return FourmActivityFragment.newInstance();
                case 1:
                    return MapsFragmentActivity.newInstance();
                case 2:
                    return CameraActivityFragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }


}
