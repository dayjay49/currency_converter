
package com.example.currencyconverter;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Feature.Type;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PictureScanner {
    ImageAnnotatorClient vision;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public PictureScanner(){

        try {
            vision = ImageAnnotatorClient.create();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public double getPrice(String filePath){
        ByteString imgBytes = null;
        try {
            imgBytes = ByteString.readFrom(new FileInputStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Builds the image annotation request
        List<AnnotateImageRequest> requests = new ArrayList<>();
        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Type.DOCUMENT_TEXT_DETECTION).build();
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feat)
                .setImage(img)
                .build();
        requests.add(request);
        BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
        List<AnnotateImageResponse> responses = response.getResponsesList();
        String ans="";
        for (AnnotateImageResponse res : responses) {
            if (res.hasError()) {
                System.out.printf("Error: %s\n", res.getError().getMessage());
                continue;
            }

            for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                ans+=annotation.getDescription();
            }
        }
        try{
            return Double.parseDouble(ans);
        }catch (Exception e){
            //do something
        }
        return -1;

    }



}