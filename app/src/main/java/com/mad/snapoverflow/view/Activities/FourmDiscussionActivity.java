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

/* this activity is the page for the display of the questions set by the users*/
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

    private static final String IMAGEURL = "url";
    private static final String CONTENT = "content";
    private static final String TITLE = "title";
    private static final String DATE = "date";
    private static final String KEY = "key";
    private static final String QUESTION = "Question";
    private static final String COMMENT = "comments";
    private static final String NULL = "";
    private static final String TEXT = "text";

    /* the oncreate lifecycle that does on the binding and other instances of the application such as activites */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourm_discussion);

        mImageUrl = getIntent().getStringExtra(IMAGEURL);
        mContent = getIntent().getStringExtra(CONTENT);
        mTitleTxt = getIntent().getStringExtra(TITLE);
        mDatetxt = getIntent().getStringExtra(DATE);
        mkey = getIntent().getStringExtra(KEY);

        mImageView = findViewById(R.id.imageFourm);
        mCtxt = findViewById(R.id.ctxt);
        mTitle = findViewById(R.id.titletxt);
        mDate = findViewById(R.id.datetxt);
        mCommentEditTxt = findViewById(R.id.comments);
        mButton = findViewById(R.id.send);


        mCtxt.setText(mContent);
        mDate.setText(mDatetxt);
        mTitle.setText(mTitleTxt);

        //picasso api that deals with loading images from urls
        Picasso.with(this)
                .load(mImageUrl)
                .placeholder(R.drawable.progress_animation)
                .into(mImageView);


        //recycleview bindings and options
        mRecyclerViewComment = findViewById(R.id.recycler_view2);
        mRecyclerViewComment.setNestedScrollingEnabled(false);
        mRecyclerViewComment.setHasFixedSize(true);
        mLayoutManagerComment = new LinearLayoutManager(this);
        mRecyclerViewComment.setLayoutManager(mLayoutManagerComment);
        mRecyclerViewComment.setItemAnimator(new DefaultItemAnimator());
        mRecyclerViewComment.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));



        //firebase bindings
        mFirebaseDatabaseComment = FirebaseDatabase.getInstance();
        mDatabaseReferenceComment = FirebaseDatabase.getInstance().getReference(QUESTION).child(mkey).child(COMMENT);
        mDatabaseReferenceComment.keepSynced(true); //keeps the recycle view sync allowing for live updates



        mButton.setOnClickListener(new View.OnClickListener() {

            /* this uploads the comments to firebase and sets it onto the recycleview */
            @Override
            public void onClick(View v) {
                final DatabaseReference data = FirebaseDatabase.getInstance().getReference().child(QUESTION);
                final String uid = data.child(mkey).child(COMMENT).push().getKey();

                Map<String, Object> maptoUpload = new HashMap<>();
                maptoUpload.put(TEXT,mCommentEditTxt.getText().toString());
                data.child(mkey).child(COMMENT).child(uid).setValue(maptoUpload);

                mCommentEditTxt.setText(NULL);

            }
        });
    }
    /* the on start lifecycle which is called on the start of the applications*/
    @Override
    public void onStart() {
        super.onStart();
        // Uses the firebase Recycler adappter to create connect firebase to the recycler view.
        FirebaseRecyclerAdapter<CommentsModel,CommentsHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CommentsModel, CommentsHolder>
                (CommentsModel.class,R.layout.activity_comments_item,CommentsHolder.class,mDatabaseReferenceComment) { // links the item activity, firebase as well as the holders.
            @Override
            /* this populates and sets what ever data is pulled from firebase  */
            protected void populateViewHolder(CommentsHolder viewHolder, CommentsModel model, int position) {
                viewHolder.setTitles(model.getText());

            }

        };
        mRecyclerViewComment.setAdapter(firebaseRecyclerAdapter); //sets the addapter as the firebase adapter.
    }
}

