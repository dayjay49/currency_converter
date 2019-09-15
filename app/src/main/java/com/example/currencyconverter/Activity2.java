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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.io.IOException;

import java.util.regex.*;

public class Activity2 extends AppCompatActivity {

    public Bitmap thePic;
    private Uri picUri;
    // keep track of cropping intent
    final int PIC_CROP = 3;
    private ImageView image;
    private Button crop_button;
    private Button convert_button;
    private Button retake_button;

    private String home_country_code;
    private String travel_country_code;
    private String price_to_convert;

    public FirebaseVisionImage final_image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_page);

        image = (ImageView) findViewById(R.id.image);
        crop_button = (Button) findViewById(R.id.crop_option);
        convert_button = (Button) findViewById(R.id.convert_button);
        retake_button = (Button) findViewById(R.id.retake);

        Intent intent = getIntent();
        picUri = intent.getParcelableExtra("imageUri");
//        Bitmap image = (Bitmap) intent.getParcelableExtra("BitmapImage");

        image.setImageURI(picUri);

        retake_button.setOnClickListener(new View.OnClickListener() {
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

        convert_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // convert uri to FirebaseVisionImage
                try {
                    final_image = FirebaseVisionImage.fromFilePath(Activity2.this, picUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
//                runTextRecog();

                // send double and country codes
                Intent intent = new Intent(Activity2.this, Activity3.class);
//                intent.putExtra("codesAndPrice", home_country_code + "/" + travel_country_code + "/" + price_to_convert);
                intent.putExtra("codesAndPrice", "CAD" + "/" + "USD" + "/" + price_to_convert);
                startActivity(intent);
            }
        });
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

        final_image = FirebaseVisionImage.fromBitmap(thePic);
//        runTextRecog();

        // move to last page
        Intent intent = new Intent(Activity2.this, Activity3.class);
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("country_name", COUNTRY_NAME);
//        bundle.putParcelable("price_image", uri);
//        intent.putExtras(bundle);
//        startActivity(intent);
//        intent.putExtra("convertUri", picUri);
//        intent.putExtra("codesAndPrice", home_country_code + "/" + travel_country_code + "/" + price_to_convert);
        intent.putExtra("codesAndPrice", "CAD" + "/" + "USD" + "/" + price_to_convert);
        startActivity(intent);
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

    private void runTextRecog() {

        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();

        // detects FirebaseVisionText from a FirebaseVisionImage
        detector.processImage(final_image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText texts) {
                processExtractedText(texts);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(Activity2.this, "Exception", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void processExtractedText(FirebaseVisionText firebaseVisionText) {
        String extractedText;

        if (firebaseVisionText.getTextBlocks().size() == 0) {
            price_to_convert = "-1";
            return;
        }
//        for (FirebaseVisionText.Block block : firebaseVisionText.getBlocks()) {
//            myTextView.append(block.getText());
//
//        }
        extractedText = firebaseVisionText.getText();

        Pattern p = Pattern.compile("^\\d*\\.\\d+|\\d+\\.\\d*$|\\d");
        Matcher m = p.matcher(extractedText);
        price_to_convert = m.group(0);
    }
}

