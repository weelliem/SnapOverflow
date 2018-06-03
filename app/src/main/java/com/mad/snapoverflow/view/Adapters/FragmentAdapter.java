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


package com.mad.snapoverflow.view.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mad.snapoverflow.view.Fragments.CameraActivityFragment;
import com.mad.snapoverflow.view.Fragments.FourmActivityFragment;
import com.mad.snapoverflow.view.Fragments.MapsFragmentActivity;

/* fragment adpator helps manages the fragments for the main activity */
public class FragmentAdapter {
    public static class MyPageAdapter extends FragmentPagerAdapter {

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }


        /* tells the activity which fragment to place to display  */
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
