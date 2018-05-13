package com.mad.snapoverflow;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class fourmActivityFragment extends Fragment {

    public static fourmActivityFragment newInstance(){

        fourmActivityFragment fragment = new fourmActivityFragment();

        return fragment;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fourm_fragment , container, false);
        return view;
    }
}
