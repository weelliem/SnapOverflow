package com.mad.snapoverflow.view.Activities;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mad.snapoverflow.R;
import com.mad.snapoverflow.databinding.ActivityUploadBinding;
import com.mad.snapoverflow.view.Fragments.MapsFragmentActivity;
import com.mad.snapoverflow.viewmodel.CameraFragmentViewModel;
import com.mad.snapoverflow.viewmodel.UploadViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity{


    private ActivityUploadBinding mUpBinding;
    private UploadViewModel mUpViewModel;

    TextView gps;

    TextView timeAnddate;
    Button backBtn;
    String Uid;
    Button uploadBtn;
    byte[] byteArray;
    EditText titles;
    EditText details;
    double mLong;
    double mLat;
    ProgressBar mProgressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUpBinding = DataBindingUtil.setContentView(this,R.layout.activity_upload);


        gps = findViewById(R.id.GPS);
        timeAnddate = findViewById(R.id.timeDate);
        backBtn = findViewById(R.id.backBtn);
        uploadBtn = findViewById(R.id.uploadBtn);
        Uid = FirebaseAuth.getInstance().getUid();
        titles = findViewById(R.id.titleText);
        details = findViewById(R.id.contentText);
        mProgressDialog = findViewById(R.id.progress);

        MapsFragmentActivity map = new MapsFragmentActivity();
      //  mLat = map.getLat();
      //  mLong = map.getLong();

        gps.setText("Long " + mLong + " Lat " + mLat);


        String image = getIntent().getStringExtra("image");
        final Date currentTime = Calendar.getInstance().getTime();
        timeAnddate.setText(currentTime.toString());

       loadImageFromStorage(image);

       uploadBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               //final DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("UsersSignupModel").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Uid").child("Question");
               final DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("Question");
               final String key = data.push().getKey();
               MapsFragmentActivity map = new MapsFragmentActivity();
              // final double longi = map.getLong();
              // final double lat = map.getLong();

               mProgressDialog.setVisibility(View.VISIBLE);
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
                      // maptoUpload.put("Gps Long",longi);
                      // maptoUpload.put("Gps Lat", lat);
                       maptoUpload.put("title" , titles.getText().toString());
                       maptoUpload.put("content",details.getText().toString());

                       data.child(key).setValue(maptoUpload);
                       mProgressDialog.setVisibility(View.GONE);

                   }
               });

               upload.addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(getApplicationContext(),"error uploading",Toast.LENGTH_SHORT).show();
                        mProgressDialog.setVisibility(View.GONE);

                   }
               });

               upload.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                   @Override
                   public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                           double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                           System.out.println("Upload is " + progress + "% done");
                           int currentprogress = (int) progress;
                           mProgressDialog.setProgress(currentprogress);
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


}
