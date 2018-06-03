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


package com.mad.snapoverflow.view.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.mad.snapoverflow.R;
import com.mad.snapoverflow.databinding.ActivityCameraFragmentBinding;
import com.mad.snapoverflow.databinding.ActivityUploadBinding;
import com.mad.snapoverflow.view.Activities.UploadActivity;
import com.mad.snapoverflow.viewmodel.CameraFragmentViewModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/* creates a the camera, captures it and sends it to another activity */
public class CameraActivityFragment extends Fragment {

    private Button mBtnCapture;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private ActivityCameraFragmentBinding mBinding;
    private ProgressDialog mProgress;
    private android.hardware.Camera.PictureCallback mJpegCallback;

       public static CameraActivityFragment newInstance(){

        CameraActivityFragment fragment = new CameraActivityFragment();

        return fragment;
    }

    /* the on create version for the fragment that binds the variables and activity */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {



        mBinding = DataBindingUtil.inflate(inflater, R.layout.activity_camera_fragment,null,false);
        View view = mBinding.getRoot();
        mSurfaceView = mBinding.surfaceView;
        mSurfaceHolder = mSurfaceView.getHolder();

        //sends the data to the model view to handel
        mBinding.setCameraFragmentViewModel(new CameraFragmentViewModel(mJpegCallback, getContext(),mSurfaceHolder,getActivity(),mProgress,mBinding.btnCapture));


        return view;
    }



}
