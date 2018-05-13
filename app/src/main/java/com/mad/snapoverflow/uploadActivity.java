package com.mad.snapoverflow;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

public class uploadActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

       /* if (bytes != null){
            ImageView image = findViewById(R.id.imageCaptured);

            Bitmap decodeBitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length );

            Bitmap rotateBitmap = rotate(decodeBitmap);


            image.setImageBitmap(rotateBitmap);

        } */
    }

    /* private Bitmap rotate(Bitmap decodeBitmap) {
        int width = decodeBitmap.getWidth();
        int height = decodeBitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.setRotate(90);

        return Bitmap.createBitmap(decodeBitmap,0,0,width,height,matrix,true);

    } */
}
