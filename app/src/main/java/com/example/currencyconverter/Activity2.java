package com.example.currencyconverter;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Activity2 extends AppCompatActivity {

    public String COUNTRY_NAME;
    private Uri picUri;
    // keep track of cropping intent
    final int PIC_CROP = 3;
    private ImageView image;
    private Button crop_button;
    private Button convert_button;
    private Button back_button;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_page);

        image = (ImageView) findViewById(R.id.image);
        crop_button = (Button) findViewById(R.id.crop_option);
        convert_button = (Button) findViewById(R.id.convert_button);
        back_button = (Button) findViewById(R.id.back_button);

//        TextView textView = (TextView) findViewById(R.id.country_name);
//        textView.setText(COUNTRY_NAME);

        Intent intent = getIntent();
        picUri = intent.getParcelableExtra("imageUri");
//        Bitmap image = (Bitmap) intent.getParcelableExtra("BitmapImage");

        image.setImageURI(picUri);

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity2.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        crop_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performCrop();
            }
        });

//        convert_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(Activity2.this, Activity3.class);
////                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if(requestCode == PIC_CROP) {
                //get the returned data
                Bundle extras = data.getExtras();
                //get the cropped bitmap
                Bitmap thePic = (Bitmap) extras.get("data");
                //display the returned cropped image
                image.setImageBitmap(thePic);
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
}
