package com.mad.snapoverflow;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class uploadActivity extends AppCompatActivity implements LocationListener{

    TextView gps;
    Location current;
    LocationManager mLocationManager;
    TextView timeAnddate;
    Button backBtn;
    String Uid;
    Button uploadBtn;
    byte[] byteArray;
    EditText titles;
    EditText details;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        gps = findViewById(R.id.GPS);
        timeAnddate = findViewById(R.id.timeDate);
        backBtn = findViewById(R.id.backBtn);
        uploadBtn = findViewById(R.id.uploadBtn);
        Uid = FirebaseAuth.getInstance().getUid();
        titles = findViewById(R.id.titleText);
        details = findViewById(R.id.contentText);


        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        current = mLocationManager.getLastKnownLocation(mLocationManager.NETWORK_PROVIDER);
        onLocationChanged(current);


        String image = getIntent().getStringExtra("image");
        final Date currentTime = Calendar.getInstance().getTime();
        timeAnddate.setText(currentTime.toString());

        loadImageFromStorage(image);

       uploadBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               final DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Uid").child("Question");
               final String key = data.push().getKey();
               final double longi = current.getLongitude();
               final double lat = current.getLatitude();


               StorageReference filePath = FirebaseStorage.getInstance().getReference().child("camera_picture").child(key);
               UploadTask upload = filePath.putBytes(byteArray);

               upload.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                       Uri imageUrl = taskSnapshot.getDownloadUrl();
                       Long currentTimestamp = System.currentTimeMillis();
                       Long endTimestamp = currentTimestamp + (24 * 60 * 60 * 1000);

                       Map<String, Object> maptoUpload = new HashMap<>();
                       maptoUpload.put("imageUrl", imageUrl.toString());
                       maptoUpload.put("timestamp", currentTimestamp);
                       maptoUpload.put("timestampEnd", endTimestamp);
                       maptoUpload.put("systemtime",currentTime.toString());
                       maptoUpload.put("Gps Long", longi);
                       maptoUpload.put("Gps Lat", lat);
                       maptoUpload.put("title" , titles.getText().toString());
                       maptoUpload.put("content",details.getText().toString());

                       data.child(key).setValue(maptoUpload);

                   }
               });

               upload.addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(getApplicationContext(),"error uploading",Toast.LENGTH_SHORT).show();
                       return;
                   }
               });
           }
       });



       backBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });
    }


    private void loadImageFromStorage(String path)
    {

        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img = findViewById(R.id.tempImage);
            img.setImageBitmap(b);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            b.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();




        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        double longi = location.getLongitude();
        double lat = location.getLatitude();
        gps.setText("Longitude " + longi + " Latitude " + lat);
    }
}
