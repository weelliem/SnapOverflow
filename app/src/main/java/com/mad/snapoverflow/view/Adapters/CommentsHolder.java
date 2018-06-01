package com.mad.snapoverflow.view.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mad.snapoverflow.R;

public class CommentsHolder extends RecyclerView.ViewHolder {

    View mView;
    TextView mTitleText;

    public CommentsHolder(View view) {
        super(view);
        mView = view;

    }

    public void setTitles(String title){
        mTitleText = mView.findViewById(R.id.textComments);
        mTitleText.setText(title);

    }


}
