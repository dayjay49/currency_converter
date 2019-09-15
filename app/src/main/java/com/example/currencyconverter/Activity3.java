package com.example.currencyconverter;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

public class Activity3 extends AppCompatActivity {

    private String home_country_code;
    private String travel_country_code;
    private double converted_price;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_page);

//        Intent intent = getIntent();
// ADD MORE LINES RECEIVING INTENT

        new GetUrlContentTask().execute(new XEGetParams(home_country_code,
                travel_country_code, converted_price));
    }

    //region XE API
//    public void sendRequestToXE(android.view.View v){
//        //TODO: Retrieve parameters convertFrom, convertTo, amount and use for input to execute()
//    }
    public void updateTextView(String toThis) {
        //TODO: Change id
        TextView textView = findViewById(R.id.converted_price);
        textView.setText(toThis + home_country_code);
    }
    private static class XEGetParams {
        String convertFrom;
        String convertTo;
        double amount;
        XEGetParams(String convertFrom, String convertTo, double amount) {
            this.convertFrom = convertFrom;
            this.convertTo = convertTo;
            this.amount = amount;
        }
    }
    private class GetUrlContentTask extends AsyncTask<XEGetParams, Integer, String> {
        protected String doInBackground(XEGetParams... params){
            String convertedNumber = "N/A";
            try {
                String url = MessageFormat.format("https://xecdapi.xe.com/v1/convert_from.json/?from={0}&to={1}&amount={2}", params[0].convertFrom, params[0].convertTo, params[0].amount);
                URL urlForGetRequest = new URL(url);
                String readLine;
                HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
                connection.setRequestMethod("GET");
                String userpass = "hackthenorth948103889:1u305t9u90k1f1hnqeppmb5heo";
                String basicAuth = "Basic :" + new String(Base64.getEncoder().encode(userpass.getBytes()));
                connection.setRequestProperty ("Authorization", basicAuth);
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    while ((readLine = in.readLine()) != null) {
                        response.append(readLine);
                    }
                    in.close();
                    // print result
                    //System.out.println(“JSON String Result ” + response.toString());
                    try {
                        JSONObject r = new JSONObject(response.toString());
                        JSONArray content = r.getJSONArray("to");
                        JSONObject rec = content.getJSONObject(0);
                        convertedNumber = String.format("%.2f", (double)rec.getDouble("mid"));
                    } catch (JSONException err) {
                        Log.d("JSON Conversion Error", err.toString());
                    }
                    return convertedNumber;
                    //GetAndPost.POSTRequest(response.toString());
                }
            }
            catch (IOException ex){
                Log.d("HTTP Connection Error", ex.toString());
            }
            return convertedNumber;
        }
        protected void onProgressUpdate(Integer... progress) {
        }
        protected void onPostExecute(String result) {
            // this is executed on the main thread after the process is over
            // update your UI here
            updateTextView(result);
        }
    }
    //endregion
}
