package com.example.currencyconverter;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private ImageButton button;

    // keep track of camera capture intent
    final int CAMERA_CAPTURE = 1;
    // captured picture uri
    private Uri picUri;

    public EditText home_country;
    public EditText curr_travelling;

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
        button = (ImageButton) findViewById(R.id.camera_button);
        home_country = (EditText) findViewById(R.id.edt_home_country);
        curr_travelling = (EditText) findViewById(R.id.edt_travelling);

        //Initialize Service
        checkPermission();
    }

    public void checkPermission(){

        List<String> PermissionRqList = new ArrayList<>();

        for(String permission : permission_list){
            // Check for permission request
            int check = checkCallingOrSelfPermission(permission);

            if(check == PackageManager.PERMISSION_DENIED){
                PermissionRqList.add(permission);
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
        if(requestCode==0)
        {
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            // user is returning from capturing an image using the camera
            if(requestCode == CAMERA_CAPTURE){
                // get the Uri for the captured image
                Uri uri = picUri;

                // ADD COUNTRY NAME STRING TOGETHER LATER
                Intent intent = new Intent(this, Activity2.class);
                intent.putExtra("imageUri", uri);
                startActivity(intent);

            }
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



