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