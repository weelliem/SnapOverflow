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


package com.mad.snapoverflow.view.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mad.snapoverflow.R;
import com.mad.snapoverflow.view.Activities.FourmDiscussionActivity;
import com.mad.snapoverflow.view.Fragments.FourmActivityFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/* fourm holder this creates the item for the recycling view */
public class FourmHolder extends RecyclerView.ViewHolder{

        private TextView mTitleText;
        private ImageView mImageView;
        private View mView;

        private static final String CONTENT = "content";
        private static final String IMAGEURL = "url";
        private static final String TITLE = "title";
        private static final String DATE = "date";
        private static final String KEY = "key";

        public FourmHolder(View view) {
            super(view);
            mView = view;


        }

        public void setTitles(String title){
        mTitleText = mView.findViewById(R.id.textTitle);
        mTitleText.setText(title);

        }

        public void setImage(Context ctx, String imageUrl){
            mImageView = mView.findViewById(R.id.imageRec);
            Picasso.with(ctx).load(imageUrl).placeholder(R.drawable.progress_animation).into(mImageView);
        }

        /* allows the individual click of each item on the recycling view  */
        public void setOnclick(final String ctx,final String imageUrl, final Context context,final String title, final String date, final String key){
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(context,FourmDiscussionActivity.class);
                    mIntent.putExtra(CONTENT,ctx);
                    mIntent.putExtra(IMAGEURL,imageUrl);
                    mIntent.putExtra(TITLE,title);
                    mIntent.putExtra(DATE,date);
                    mIntent.putExtra(KEY,key);
                    context.startActivity(mIntent);
                }
            });
        }

}