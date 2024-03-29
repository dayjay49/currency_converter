package com.example.currencyconverter;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

//    private static final String TAG = "FirstScreen";
    private Button button;
    private ImageView imageView;

    // keep track of camera capture intent
    final int CAMERA_CAPTURE = 1;
    // captured picture uri
    private Uri picUri;
    // keep track of cropping intent
    final int PIC_CROP = 3;


    String[] permission_list = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page);
        button = findViewById(R.id.camera_button);
        imageView = findViewById(R.id.imageView);

        //Initialize Service
        checkPermission();
    }

    public void checkPermission(){

        List<String> PermissionRqList = new ArrayList<>();

        for(String permission : permission_list){
            // Check for permission request
            int check = checkCallingOrSelfPermission(permission);

            if(check == PackageManager.PERMISSION_DENIED){
                //권한 허용을여부를 확인하는 창을 띄운다
                PermissionRqList.add(permission);
                //requestPermissions(permission_list,0);
            }
        }
        if(!PermissionRqList.isEmpty()){
            requestPermissions(PermissionRqList.toArray(new String[PermissionRqList.size()]),0);
        }
        // finally once all permissions have been granted, initialize view
        else{
            initialize();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0) {
            if(grantResults.length > 0) {
                for (int i = 0; i < grantResults.length; i++) {
                    // permissions granted?
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), "Give appropriate permission(s) to use the app!", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), "Give appropriate permission(s) to use the app!", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
            initialize();
        }
    }

    public void initialize() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.camera_button:
                        try {
                            // capture an image
                            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                            StrictMode.setVmPolicy(builder.build());
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/picture.jpg";
                            File imageFile = new File(imageFilePath);

                            // convert path to Uri
                            picUri = Uri.fromFile(imageFile);
                            takePictureIntent.putExtra( MediaStore.EXTRA_OUTPUT, picUri );
                            startActivityForResult(takePictureIntent, CAMERA_CAPTURE);
                        } catch (ActivityNotFoundException e) {
                            // display an error message
                            String errorMessage = "Whoops - your device doesn't support capturing images!";
                            Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    default:
                        break;
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        private ImageView camera_image;
//        camera_image = imageView;

        if (resultCode == RESULT_OK) {
            // user is returning from capturing an image using the camera
            if(requestCode == CAMERA_CAPTURE){
                // get the Uri for the captured image
                Uri uri = picUri;
                performCrop();
                Log.d("picUri", uri.toString());
            } //user is returning from cropping the image
            else if(requestCode == PIC_CROP){
                //get the returned data
                Bundle extras = data.getExtras();
                //get the cropped bitmap
                Bitmap thePic = (Bitmap) extras.get("data");
                //display the returned cropped image
                imageView.setImageBitmap(thePic);
            }
        }
    }

    private void performCrop() {
        try {
            //call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            //indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            //set crop properties
            cropIntent.putExtra("crop", "true");
            //indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            //indicate output X and Y
            cropIntent.putExtra("outputX", 256);
            cropIntent.putExtra("outputY", 256);
            //retrieve data on return
            cropIntent.putExtra("return-data", true);
            //start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, PIC_CROP);
        }
        catch(ActivityNotFoundException e){
            //display an error message
            String errorMessage = "This device does not support the crop action.";
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private static long back_pressed;
    @Override
    public void onBackPressed(){
        if (back_pressed + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
        }
        else{
            Toast.makeText(getBaseContext(), "Press once again to exit", Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        }
    }
}
