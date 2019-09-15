package com.example.currencyconverter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        ImageView avo = (ImageView) findViewById(R.id.gif_image);
        Glide.with(this).load(R.drawable.travelcc).into(avo);

        Handler hd = new Handler();
        hd.postDelayed(new splashhandler(), 1500);
    }

    private class splashhandler implements Runnable{
        public void run(){
            startActivity(new Intent(getApplication(), MainActivity.class));
            Splash.this.finish(); // Remove from loading page activity stack
        }
    }

    @Override
    public void onBackPressed() {
        // Prevents pressing of the back button during the transition after splash screen
    }
}