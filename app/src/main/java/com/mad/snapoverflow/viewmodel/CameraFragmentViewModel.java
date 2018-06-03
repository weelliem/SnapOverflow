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


package com.mad.snapoverflow.viewmodel;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.mad.snapoverflow.R;
import com.mad.snapoverflow.view.Activities.UploadActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/* handels all the logic for the camera fragments it creates and sets the functions of the camrea */
public class CameraFragmentViewModel extends AppCompatActivity implements SurfaceHolder.Callback {


    private android.hardware.Camera.PictureCallback mPictureCallback;
    private static final String TAG = "Camera";
    private static final String IMAGE = "image";
    private static final String PROFILE = "profile.jpg";
    private static final String DIR = "imageDir";

    private Context mContext;
    private android.hardware.Camera mCamera;
    private SurfaceHolder mSurfaceHolder;
    final int CAMERA_REQUEST_CODE = 1;
    private Activity mActivity;
    private ProgressDialog mProgress;
    private Button mButton;
    private String mPath;
    private Intent mIntent;

    /* the constructor for the view model*/
    public CameraFragmentViewModel(android.hardware.Camera.PictureCallback callback, Context context,
                                   SurfaceHolder SurfaceHolder, Activity activity, ProgressDialog progress, Button btnCapture){

        mButton = btnCapture;
        mProgress = progress;
        mContext = context;
        mPictureCallback = callback;
        mSurfaceHolder = SurfaceHolder;
        mActivity = activity;

        //checks the permission of the device to allow for access to the camera and external storage
        if(ActivityCompat.checkSelfPermission(mContext, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(mActivity, new String[]{android.Manifest.permission.CAMERA},CAMERA_REQUEST_CODE);
        }
        else {

            mSurfaceHolder.addCallback(this);
            mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        
        //call back to the camera and perform an action
        mPictureCallback = new android.hardware.Camera.PictureCallback() {

            /* takes a picture then decodes the image into a bitmap image and sends it to be encoded even more */
            @Override
            public void onPictureTaken(byte[] bytes, Camera camera) {
                Bitmap mBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                if (mBitmap != null){
                    mPath = saveToInternalStorage(mBitmap);
                    mIntent.putExtra(IMAGE,mPath);

                }



            }
        };



    }

    /* calls back the camera then takes a photo */
    private void takePhoto() {
        mCamera.takePicture(null,null, mPictureCallback);
    }

    /* on click sends the intent of the image and runes the asyctask */
    public View.OnClickListener onClickCapture(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mIntent = new Intent(mContext, UploadActivity.class);
                cameraLoading imagePhoto = new cameraLoading();
                imagePhoto.execute();



            }
        };
    }

    /* this creates the surface which where the camera resides */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mCamera = android.hardware.Camera.open();

        //stores the parameter for the camera
        android.hardware.Camera.Parameters parameters;
        parameters = mCamera.getParameters();

        mCamera.setDisplayOrientation(90);
        parameters.setPreviewFrameRate(30);
        parameters.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

        android.hardware.Camera.Size size = null;
        List<Camera.Size> sizeList = mCamera.getParameters().getSupportedPreviewSizes();
        size = sizeList.get(0);

        for (int i = 1; i < sizeList.size(); i++){
            if((sizeList.get(i).width * sizeList.get(i).height) > (size.width * size.height)){
                size = sizeList.get(i);
            }
        }

        parameters.setPreviewSize(size.width, size.height);

        mCamera.setParameters(parameters);

        try {
            mCamera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mCamera.startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /* result codes that are sent are handled acordingly within this function  */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    mSurfaceHolder.addCallback(this);
                    mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                } else {
                    Toast.makeText(mContext,mContext.getResources().getString(R.string.cam_toast_one),Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }




/* when the image captured by the  camera it is roated to make sure it is the right position it is rotated to the right position*/
    private Bitmap rotate(Bitmap decodeBitmap) {
        int width = decodeBitmap.getWidth();
        int height = decodeBitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.setRotate(90);

        return Bitmap.createBitmap(decodeBitmap,0,0,width,height,matrix,true);

    }
        /* saves the image to temp memory and then sends the file path back  */
        private String saveToInternalStorage(Bitmap bitmapImage){
        Bitmap rotateBitmap = rotate(bitmapImage);
        ContextWrapper cw = new ContextWrapper(Objects.requireNonNull(mContext).getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir(DIR, Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory,PROFILE);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            rotateBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert fos != null;
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }




/* this asyctask it creates a progress dialog and processes the image in the background as well as starts an new activity */
    public class cameraLoading extends AsyncTask<Void, Void, String> {



        @Override
        protected void  onPreExecute(){
            mProgress = new ProgressDialog(mContext);
            mProgress.setMessage("processing");
            mProgress.setIndeterminate(false);
            mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgress.setCancelable(false); //makes the progress not cancelable
            mProgress.show();
            mButton.setEnabled(false);
        }
        @Override
        protected String doInBackground(Void... voids) {


            takePhoto();


            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }




            return null;
        }


        @Override
        protected void onPostExecute(String result) {




            mProgress.hide();
            mButton.setEnabled(true);
            mContext.startActivity(mIntent);

        }
    }

}
