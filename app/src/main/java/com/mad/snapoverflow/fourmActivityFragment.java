package com.mad.snapoverflow;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class fourmActivityFragment extends Fragment {

    private RecyclerView mRecyclerView;
    DatabaseReference mQuestions;
    private RecyclerView.Adapter mAdapter;
    public  ArrayList<fourmObject> sFourmObjects = new ArrayList<>();
    private RecyclerView.LayoutManager mLayoutManager;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    public static fourmActivityFragment newInstance() {

        fourmActivityFragment fragment = new fourmActivityFragment();

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
        FirebaseRecyclerAdapter<fourmObject,fourmHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<fourmObject, fourmHolder>
                (fourmObject.class,R.layout.activity_fourm_item,fourmHolder.class,mDatabaseReference) {
            @Override
            protected void populateViewHolder(fourmHolder viewHolder, fourmObject model, int position) {
//              viewHolder.setUser(model.getUsername());
                viewHolder.setTitles(model.gettitle());
                viewHolder.setImage(getContext(),model.getimageUrl());
            }
        };
        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }
}

