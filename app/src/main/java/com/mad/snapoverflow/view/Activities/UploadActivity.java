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

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mad.snapoverflow.R;
import com.mad.snapoverflow.databinding.ActivityUploadBinding;
import com.mad.snapoverflow.viewmodel.UploadViewModel;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity implements OnMapReadyCallback {


    private ActivityUploadBinding mUpBinding;
    private UploadViewModel mUpViewModel;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final String TAG = "MapActivity";

    private TextView mGps;

    private TextView mTimeAndDate;
    private Button mBackBtn;
    private String mUid;
    private Button mUploadBtn;
    private byte[] mByteArray;
    private EditText mTitles;
    private EditText mDetails;
    private double mLong;
    private double mLat;
    private ProgressBar mProgressDialog;

    private static final String IMAGE = "image";
    private static final String QUESTIONS = "Question";
    private static final String CAMERA = "camera_picture";
    private static final String IMAGEURL = "url";
    private static final String CONTENT = "content";
    private static final String KEY = "key";
    private static final String COMMENT = "comments";
    private static final String GPSLONG = "gpsLong";
    private static final String GPSLat = "gpsLat";
    private static final String TITLE = "title";
    private static final String SYSTEMTIME = "systemtime";
    private static final String TIMESTAMPEND = "timestampEnd";
    private static final String TIMESTAMPSTART = "timestamp";
    private static final String PROFILE = "profile.jpg";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUpBinding = DataBindingUtil.setContentView(this,R.layout.activity_upload);


        mGps = findViewById(R.id.GPS);
        mTimeAndDate = findViewById(R.id.timeDate);
        mBackBtn = findViewById(R.id.backBtn);
        mUploadBtn = findViewById(R.id.uploadBtn);
        mUid = FirebaseAuth.getInstance().getUid();
        mTitles = findViewById(R.id.titleText);
        mDetails = findViewById(R.id.contentText);
        mProgressDialog = findViewById(R.id.progress);







        String image = getIntent().getStringExtra(IMAGE);
        final Date currentTime = Calendar.getInstance().getTime();
        mTimeAndDate.setText(currentTime.toString());

       loadImageFromStorage(image);

       mUploadBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               final DatabaseReference data = FirebaseDatabase.getInstance().getReference().child(QUESTIONS);
               final String key = data.push().getKey();



               // final double longi = map.getLong();
              // final double lat = map.getLong();

               mProgressDialog.setVisibility(View.VISIBLE);
               StorageReference filePath = FirebaseStorage.getInstance().getReference().child(CAMERA).child(key);
               UploadTask upload = filePath.putBytes(mByteArray);

               upload.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       Uri imageUrl = taskSnapshot.getDownloadUrl();
                       Long currentTimestamp = System.currentTimeMillis();
                       Long endTimestamp = currentTimestamp + (24 * 60 * 60 * 1000);

                       Map<String, Object> maptoUpload = new HashMap<>();
                       maptoUpload.put(IMAGEURL, imageUrl.toString());
                       maptoUpload.put(TIMESTAMPSTART, currentTimestamp);
                       maptoUpload.put(TIMESTAMPEND, endTimestamp);
                       maptoUpload.put(SYSTEMTIME,currentTime.toString());
                       maptoUpload.put(GPSLONG,mLong);
                       maptoUpload.put(GPSLat, mLat);
                       maptoUpload.put(TITLE , mTitles.getText().toString());
                       maptoUpload.put(CONTENT, mDetails.getText().toString());
                       maptoUpload.put(KEY,key);

                       data.child(key).setValue(maptoUpload);
                       mProgressDialog.setVisibility(View.GONE);
                       finish();

                   }
               });

               upload.addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(getApplicationContext(),getApplicationContext().getResources().getString(R.string.error_upload_one),Toast.LENGTH_SHORT).show();
                        mProgressDialog.setVisibility(View.GONE);

                   }
               });

               upload.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                           double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                           System.out.println("upload_title is " + progress + "% done");
                           int currentprogress = (int) progress;
                           mProgressDialog.setProgress(currentprogress);
                   }
               });


           }
       });



       mBackBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });
    }


    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, PROFILE);
            ImageView img = findViewById(R.id.tempImage);
            Picasso.with(this)
                    .load(f)
                    .placeholder(R.drawable.progress_animation)
                    .into(img);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));

           //img.setImageBitmap(b);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.PNG, 100, stream);
            mByteArray = stream.toByteArray();




        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    private void getDeviceLocation() {

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            mGps.setText(getApplicationContext().getResources().getString(R.string.upload_long_one) + currentLocation.getLongitude() + getApplicationContext().getResources().getString(R.string.upload_lat_one) + currentLocation.getLatitude());
                            mLong = currentLocation.getLongitude();
                            mLat = currentLocation.getLatitude();
                            Log.d(TAG,"Lat " + currentLocation.getLatitude()+ " Long " +currentLocation.getLongitude() );
                        } else {
                            Toast.makeText(getApplicationContext(), getApplicationContext().getResources().getString(R.string.map_toast_one), Toast.LENGTH_LONG).show();

                        }
                    }
                });

        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation : SecurityException: " + e.getMessage());
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        getDeviceLocation();
    }
}
