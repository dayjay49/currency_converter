package com.example.currencyconverter;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Activity3 extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.third_page);

          TextView country = (TextView) findViewById(R.id.country_name);
//        country.setText(COUNTRY_NAME + " !");
    }


}
