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

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mad.snapoverflow.R;

/* the holder for the recycle view basically creates the individual items of the recycle view */
public class CommentsHolder extends RecyclerView.ViewHolder {

    private View mView;
    private TextView mTitleText;

    /* the constructor  */
    public CommentsHolder(View view) {
        super(view);
        mView = view;

    }

    public void setTitles(String title){
        mTitleText = mView.findViewById(R.id.textComments);
        mTitleText.setText(title);

    }


}
