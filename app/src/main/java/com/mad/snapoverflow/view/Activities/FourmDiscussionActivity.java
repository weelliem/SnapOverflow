package com.mad.snapoverflow.view.Activities;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.UploadTask;
import com.mad.snapoverflow.R;
import com.mad.snapoverflow.model.CommentsModel;
import com.mad.snapoverflow.model.FourmModel;
import com.mad.snapoverflow.view.Adapters.CommentsHolder;
import com.mad.snapoverflow.view.Adapters.FourmHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FourmDiscussionActivity extends AppCompatActivity {

    private String mImageUrl;
    private String mContent;
    private ImageView mImageView;
    private String mDatetxt;
    private String mTitleTxt;
    private TextView mDate;
    private TextView mCtxt;
    private TextView mTitle;
    private Button mButton;
    private String mComment;
    private EditText mCommentEditTxt;

    private RecyclerView mRecyclerViewComment;
    private RecyclerView.LayoutManager mLayoutManagerComment;
    private FirebaseDatabase mFirebaseDatabaseComment;
    private DatabaseReference mDatabaseReferenceComment;
    private String mkey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourm_discussion);

        mImageUrl = getIntent().getStringExtra("url");
        mContent = getIntent().getStringExtra("content");
        mTitleTxt = getIntent().getStringExtra("title");
        mDatetxt = getIntent().getStringExtra("date");
        mkey = getIntent().getStringExtra("key");

        mImageView = findViewById(R.id.imageFourm);
        mCtxt = findViewById(R.id.ctxt);
        mTitle = findViewById(R.id.titletxt);
        mDate = findViewById(R.id.datetxt);
        mCommentEditTxt = findViewById(R.id.comments);
        mButton = findViewById(R.id.send);


        mCtxt.setText(mContent);
        mDate.setText(mDatetxt);
        mTitle.setText(mTitleTxt);

        Picasso.with(this)
                .load(mImageUrl)
                .placeholder(R.drawable.progress_animation)
                .into(mImageView);

        mRecyclerViewComment = findViewById(R.id.recycler_view2);
        mRecyclerViewComment.setNestedScrollingEnabled(false);
        mRecyclerViewComment.setHasFixedSize(true);
        mLayoutManagerComment = new LinearLayoutManager(this);
        mRecyclerViewComment.setLayoutManager(mLayoutManagerComment);
        mRecyclerViewComment.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewComment.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));


        mFirebaseDatabaseComment = FirebaseDatabase.getInstance();

        mDatabaseReferenceComment = FirebaseDatabase.getInstance().getReference("Question").child(mkey).child("comments");

        mDatabaseReferenceComment.keepSynced(true);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference data = FirebaseDatabase.getInstance().getReference().child("Question");
                final String uid = data.child(mkey).child("comments").push().getKey();

                Map<String, Object> maptoUpload = new HashMap<>();
                maptoUpload.put("text",mCommentEditTxt.getText().toString());
                data.child(mkey).child("comments").child(uid).setValue(maptoUpload);

                mCommentEditTxt.setText("");

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<CommentsModel,CommentsHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CommentsModel, CommentsHolder>
                (CommentsModel.class,R.layout.activity_comments_item,CommentsHolder.class,mDatabaseReferenceComment) {
            @Override
            protected void populateViewHolder(CommentsHolder viewHolder, CommentsModel model, int position) {
//              viewHolder.setUser(model.getUsername());
                viewHolder.setTitles(model.getText());

            }

        };
        mRecyclerViewComment.setAdapter(firebaseRecyclerAdapter);
    }
}

