package com.mad.snapoverflow;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class fourmHolder extends RecyclerView.ViewHolder{
        TextView userText;
        TextView mTitleText;
        ImageView mImageView;
        View mView;
        public fourmHolder(View view) {
            super(view);
            mView = view;


        }

       /* public void setUser(String username){
            userText = mView.findViewById(R.id.textUser);
            userText.setText(username);
        }*/

        public void setTitles(String title){
        mTitleText = mView.findViewById(R.id.textTitle);
        mTitleText.setText(title);

        }

        public void setImage(Context ctx, String imageUrl){
            mImageView = mView.findViewById(R.id.imageRec);
            Picasso.with(ctx).load(imageUrl).into(mImageView);
        }


}