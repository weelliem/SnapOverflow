package com.mad.snapoverflow.view.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mad.snapoverflow.R;

import java.util.ArrayList;

public class FourmDiscussionActivity extends AppCompatActivity {

    String userId;
    private ImageView mImage;
    private boolean mStarted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourm_discussion);

         Bundle b = getIntent().getExtras();
         userId = b.getString("userId");

         mImage = findViewById(R.id.imageFourm);



    }
    ArrayList<String> imageUrlList = new ArrayList<>();
    private void listenForData() {
            DatabaseReference fourmDb = FirebaseDatabase.getInstance().getReference().child("User").child(userId);
            fourmDb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String imageUrl = "";

                    for(DataSnapshot questionshot : dataSnapshot.child("Question").getChildren()){
                        imageUrlList.add(imageUrl);

                        }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
}
