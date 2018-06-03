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

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.ClusterManager;
import com.mad.snapoverflow.R;
import com.mad.snapoverflow.databinding.ActivityMainBinding;
import com.mad.snapoverflow.databinding.ActivityMapsFragmentBinding;
import com.mad.snapoverflow.model.MarkerModel;
import com.mad.snapoverflow.viewmodel.MapFragmentViewModel;

/* this fragment holds and handles the google map and gps of the application */
public class MapsFragmentActivity extends Fragment implements OnMapReadyCallback {


    private ActivityMapsFragmentBinding mBinding;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final String TAG = "MapActivity";
    private static final String QUESTION = "Question";
    private static final String GPSLAT = "gpsLat";
    private static final String GPSLONG = "gpsLong";
    private static final String TITLE = "title";
    private static final float DEFAULT_ZOOM = 15;
    final int CAMERA_REQUEST_CODE = 1;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_REQUEST_CODE = 2991;
    private boolean mLocationPermissionGranted = false;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mReference;

    private ClusterManager<MarkerModel> mClusterManager; //manager for the grouping of markers

    /* constructor for the fragment for the activity */
    public static MapsFragmentActivity newInstance() {

        MapsFragmentActivity fragment = new MapsFragmentActivity();

        return fragment;
    }

    /* on create for the fragment that binds the activity to the class */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.activity_maps_fragment, null, false);
        View view = mBinding.getRoot();


        getLocationPermission();

        return view;
    }

   /* when the map is generated calls this method if permission is grantted */
    private void StartMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); //calls back the map to refresh it
    }

    /* grabs the current GPS location of the user / devices in general */
    private void getDeviceLocation() {

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        try {
            if (mLocationPermissionGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    /* if able to grab the current location of the user move the camera and center it on the map */
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                            mBinding.setMapFragmentViewModel(new MapFragmentViewModel(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())));
                        } else {
                            Toast.makeText(getActivity(), getContext().getResources().getString(R.string.map_toast_one), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation : SecurityException: " + e.getMessage());
        }

    }

    /* moves and zoom the map to the location that the camera is given */
    private void moveCamera(LatLng latLng, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker into a cluster to group them
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mClusterManager = new ClusterManager<>(getContext(), googleMap);
        googleMap.setOnCameraIdleListener(mClusterManager);
        googleMap.setOnMarkerClickListener(mClusterManager);
        googleMap.setOnInfoWindowClickListener(mClusterManager);

        //addPersonItems();

        mClusterManager.cluster();


        getDeviceLocation();
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

    }

    /* readd the makers in the onResume lifecycle */
    @Override
    public void onResume() {
        super.onResume();
        addMarker();
    }

    /* when the activity is paused clear all markers on the map */
    @Override
    public void onPause() {
        super.onPause();
        if (mLocationPermissionGranted) {
            mMap.clear();
            mClusterManager.clearItems();
        }
        Log.d(TAG, "onPause: ");
    }


   /* adds makers to the map by accessing firebase and grabing gps locations */
    public void addMarker() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mReference = mDatabaseReference.child(QUESTION);

        if (mLocationPermissionGranted) {

            ValueEventListener eventListener = new ValueEventListener() {
                @Override
                /* grabs the GPS data and title from firebase */
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        double latTxt = ds.child(GPSLAT).getValue(double.class);
                        double longTxt = ds.child(GPSLONG).getValue(double.class);
                        String titleTxt = ds.child(TITLE).getValue(String.class);

                        Log.d(TAG, "onDataChange: Lat" + latTxt + " Long " + longTxt);
                        LatLng newGps = new LatLng(latTxt, longTxt);

                        //adds the manager to the markers to the cluster manager which allows for the grouping of the markers
                        mClusterManager.addItem(new MarkerModel(latTxt, longTxt, titleTxt, ""));
                        mClusterManager.cluster();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mReference.addListenerForSingleValueEvent(eventListener);
        }
    }

    /* checks the permissions that are granted by the user and if any are not granted send a result code */
    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission Called ");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.CAMERA};

        if (ContextCompat.checkSelfPermission(getContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                StartMap();
                Log.d(TAG, "mLocationPermissionGranted True");
            } else {
                ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity(), permissions, LOCATION_REQUEST_CODE);
        }
    }

    /* checks results code sent and reacts based on the code that were given */

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionResult Called ");


        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionGranted = false;
                            Log.d(TAG, "PERMISSION FAILED CALLED ");
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    Log.d(TAG, "PERMISSION GRANTED Called ");
                    StartMap();

                }

            }

            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(getContext(), getContext().getResources().getString(R.string.cam_toast_one), Toast.LENGTH_LONG).show();
                }

            }

        }
    }

}
