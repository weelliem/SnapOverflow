package com.mad.snapoverflow;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class cameraActivityFragment extends Fragment implements SurfaceHolder.Callback{

    android.hardware.Camera camera;
    Button btnCapture;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;

    Uri image;
    ProgressDialog mProgress;

    android.hardware.Camera.PictureCallback jpegCallback;

    final int CAMERA_REQUEST_CODE = 1;

       public static cameraActivityFragment newInstance(){

        cameraActivityFragment fragment = new cameraActivityFragment();

        return fragment;
    }

    private Bitmap rotate(Bitmap decodeBitmap) {
        int width = decodeBitmap.getWidth();
        int height = decodeBitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.setRotate(90);

        return Bitmap.createBitmap(decodeBitmap,0,0,width,height,matrix,true);

    }

    private String saveToInternalStorage(Bitmap bitmapImage){
        Bitmap rotateBitmap = rotate(bitmapImage);
        ContextWrapper cw = new ContextWrapper(Objects.requireNonNull(getContext()).getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,"profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            rotateBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert fos != null;
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_camera_fragment , container, false);

        surfaceView = view.findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();

        btnCapture = view.findViewById(R.id.btnCapture);


        jpegCallback = new android.hardware.Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] bytes, android.hardware.Camera camera) {
                Intent intent = new Intent(getActivity(), uploadActivity.class);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                if (bitmap != null){
                    String s = saveToInternalStorage(bitmap);
                    intent.putExtra("image",s);
                }

                startActivity(intent);
                return;
            }
        };

        if(ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.CAMERA},CAMERA_REQUEST_CODE);
        }
        else {

            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        return view;
    }

    private void takePhoto() {
           camera.takePicture(null,null,jpegCallback);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = android.hardware.Camera.open();

        android.hardware.Camera.Parameters parameters;
        parameters = camera.getParameters();

        camera.setDisplayOrientation(90);
        parameters.setPreviewFrameRate(30);
        parameters.setFocusMode(android.hardware.Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

        android.hardware.Camera.Size size = null;
        List<android.hardware.Camera.Size> sizeList = camera.getParameters().getSupportedPreviewSizes();
        size = sizeList.get(0);

        for (int i = 1; i < sizeList.size(); i++){
            if((sizeList.get(i).width * sizeList.get(i).height) > (size.width * size.height)){
                size = sizeList.get(i);
            }
        }

        parameters.setPreviewSize(size.width, size.height);

        camera.setParameters(parameters);

        try {
            camera.setPreviewDisplay(surfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.startPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    surfaceHolder.addCallback(this);
                    surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                } else {
                  Toast.makeText(getContext(),"Please provide the permission",Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }


}
