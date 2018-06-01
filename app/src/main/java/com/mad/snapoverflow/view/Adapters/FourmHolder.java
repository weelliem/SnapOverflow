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
        TextView userText;
        TextView mTitleText;
        ImageView mImageView;
        View mView;

        public FourmHolder(View view) {
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
            Picasso.with(ctx).load(imageUrl).placeholder(R.drawable.progress_animation).into(mImageView);
        }

        public void setOnclick(final String ctx,final String imageUrl, final Context context,final String title, final String date, final String key){
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(context,FourmDiscussionActivity.class);
                    mIntent.putExtra("content",ctx);
                    mIntent.putExtra("url",imageUrl);
                    mIntent.putExtra("title",title);
                    mIntent.putExtra("date",date);
                    mIntent.putExtra("key",key);
                    context.startActivity(mIntent);
                }
            });
        }

}