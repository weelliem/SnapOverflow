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

public class FourmActivityFragment extends Fragment {

    private RecyclerView mRecyclerView;
    DatabaseReference mQuestions;
    private RecyclerView.Adapter mAdapter;
    public  ArrayList<FourmModel> sFourmObjects = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    public static FourmActivityFragment newInstance() {

        FourmActivityFragment fragment = new FourmActivityFragment();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fourm_fragment, container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));


        mFirebaseDatabase = FirebaseDatabase.getInstance();

       mDatabaseReference = FirebaseDatabase.getInstance().getReference("Question");

       mDatabaseReference.keepSynced(true);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<FourmModel,FourmHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FourmModel, FourmHolder>
                (FourmModel.class,R.layout.activity_fourm_item,FourmHolder.class,mDatabaseReference) {
            @Override
            protected void populateViewHolder(FourmHolder viewHolder, FourmModel model, int position) {
//              viewHolder.setUser(model.getUsername());
                viewHolder.setTitles(model.gettitle());
                viewHolder.setImage(getContext(),model.getimageUrl());
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}

