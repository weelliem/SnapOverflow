package com.mad.snapoverflow.view.Fragments;

import android.Manifest;
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


public class MapsFragmentActivity extends Fragment  implements OnMapReadyCallback {


    private ActivityMapsFragmentBinding mBinding;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final String TAG = "MapActivity";
    private static final float DEFAULT_ZOOM = 15;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_REQUEST_CODE = 2991;
    public double Long;
    public double Lat;
    private boolean mLocationPermissionGranted = false;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mReference;

    private ClusterManager<MarkerModel> mClusterManager;



    public static MapsFragmentActivity newInstance(){

        MapsFragmentActivity fragment = new MapsFragmentActivity();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mBinding = DataBindingUtil.inflate(inflater, R.layout.activity_maps_fragment,null,false);
        View view = mBinding.getRoot();

        getLocationPermission();

        return view;
    }

//@BindingAdapter("initMap")
    private void StartMap(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getDeviceLocation() {

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        try {
            if(mLocationPermissionGranted){
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM);
                            mBinding.setMapFragmentViewModel(new MapFragmentViewModel(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude())));
                        } else {
                            Toast.makeText(getActivity(), "Unable to Find Current Location", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation : SecurityException: " + e.getMessage());
        }

    }


    private void moveCamera(LatLng latLng, float zoom) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
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
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);



        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    @Override
    public void onResume() {
        super.onResume();
        addMarker();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMap.clear();
        mClusterManager.clearItems();
        Log.d(TAG, "onPause: ");
    }

    public void updateMakers(){

    }

    public void addMarker(){
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mReference = mDatabaseReference.child("Question");

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    double latTxt = ds.child("gpsLat").getValue(double.class);
                    double longTxt = ds.child("gpsLong").getValue(double.class);
                    String titleTxt = ds.child("title").getValue(String.class);

                    Log.d(TAG, "onDataChange: Lat" + latTxt + " Long " + longTxt);
                   /* double Lat = Double.parseDouble(latTxt);
                    double Long = Double.parseDouble(longTxt);*/
                    LatLng newGps = new LatLng(latTxt,longTxt);
                    mClusterManager.addItem(new MarkerModel(latTxt,longTxt,titleTxt,""));
                    mClusterManager.cluster();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mReference.addListenerForSingleValueEvent(eventListener);
    }

    private void getLocationPermission(){
        Log.d(TAG,"getLocationPermission Called ");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(getContext(),FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(getContext(),COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
                StartMap();
                Log.d(TAG,"mLocationPermissionGranted True");
            }
            else {
                ActivityCompat.requestPermissions(getActivity(),permissions,LOCATION_REQUEST_CODE);
            }
        }
        else {
            ActivityCompat.requestPermissions(getActivity(),permissions,LOCATION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG,"onRequestPermissionResult Called ");
        mLocationPermissionGranted = false;

        switch (requestCode){
            case LOCATION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++) {
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionGranted = false;
                            Log.d(TAG,"PERMISSION FAILED Called ");
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    Log.d(TAG,"PERMISSION GRANTED Called ");
                    StartMap();
                }
            }
        }
    }


}
