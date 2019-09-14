package com.example.currencyconverter;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Base64;

public class XERequest {

    public static double GetConvertedCurrency(String convertFrom, String convertTo, double amount) throws IOException {
        String url = MessageFormat.format("https://xecdapi.xe.com/v1/convert_from.json/?from={0}&to={1}&amount={2}", convertFrom, convertTo, amount);
        URL urlForGetRequest = new URL(url);
        String readLine;
        HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
        connection.setRequestMethod("GET");
        String userpass = "hackthenorth948103889:1u305t9u90k1f1hnqeppmb5heo";
        String basicAuth = "Basic :" + new String(Base64.getEncoder().encode(userpass.getBytes()));
        connection.setRequestProperty ("Authorization", basicAuth);
        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            while ((readLine = in .readLine()) != null) {
                response.append(readLine);
            } in .close();
            // print result
            //System.out.println("JSON String Result " + response.toString());
            double convertedNumber = -1.0;

            try {
                JSONObject r = new JSONObject(response.toString());
                JSONArray content = r.getJSONArray("to");
                JSONObject rec = content.getJSONObject(0);
                convertedNumber = rec.getInt("mid");
            }
            catch (JSONException err){
                Log.d("JSON Conversion Error", err.toString());
            }
            return convertedNumber;
            //GetAndPost.POSTRequest(response.toString());
        } else {
            return -1.0;
        }
    }

}

