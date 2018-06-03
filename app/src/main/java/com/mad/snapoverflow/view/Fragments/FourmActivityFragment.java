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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mad.snapoverflow.R;
import com.mad.snapoverflow.model.FourmModel;
import com.mad.snapoverflow.view.Adapters.FourmHolder;

import java.util.ArrayList;

/* FourmActivityFragment fragment contains all instances of the questions in the recycleview */
public class FourmActivityFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private DatabaseReference mQuestions;
    private RecyclerView.Adapter mAdapter;
    public ArrayList<FourmModel> sFourmObjects = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private static final String QUESTION = "Question";

    public static FourmActivityFragment newInstance() {

        FourmActivityFragment fragment = new FourmActivityFragment();

        return fragment;
    }

    /* basically the bindings for the activity to the class  */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fourm_fragment, container, false);

        //recycler bindings
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));


        //firebase bindings
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(QUESTION);
        mDatabaseReference.keepSynced(true);

        return view;
    }

    /* onStart lifecycle that binds the firebase recycleview adpator to the activity*/
    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<FourmModel, FourmHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FourmModel, FourmHolder>
                (FourmModel.class, R.layout.activity_fourm_item, FourmHolder.class, mDatabaseReference) {
            @Override
            /* populates the activity to the recycleview and individual items */
            protected void populateViewHolder(FourmHolder viewHolder, FourmModel model, int position) {
                viewHolder.setTitles(model.gettitle());
                viewHolder.setImage(getContext(), model.getimageUrl());
                viewHolder.setOnclick(model.getContent(), model.getimageUrl(), getContext(), model.gettitle(), model.getSystemTime(), model.getKey());

            }

        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}

