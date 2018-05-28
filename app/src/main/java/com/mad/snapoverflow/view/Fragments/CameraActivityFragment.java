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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {



        mBinding = DataBindingUtil.inflate(inflater, R.layout.activity_camera_fragment,null,false);
        View view = mBinding.getRoot();
        mSurfaceView = mBinding.surfaceView;
        mSurfaceHolder = mSurfaceView.getHolder();


        mBinding.setCameraFragmentViewModel(new CameraFragmentViewModel(mJpegCallback, getContext(),mSurfaceHolder,getActivity(),mProgress,mBinding.btnCapture));


        return view;
    }



}
