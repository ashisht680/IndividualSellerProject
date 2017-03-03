package com.javinindia.individualsellerpartner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.javinindia.individualsellerpartner.R;
import com.javinindia.individualsellerpartner.fontSeller.FontAsapBoldSingleTonClass;

public class SplashActivitySeller extends SellerBaseActivity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(getLayoutResourceId());
        TextView txt_splash = (TextView) findViewById(R.id.txt_splash);
        txt_splash.setTypeface(FontAsapBoldSingleTonClass.getInstance(getApplicationContext()).getTypeFace());

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splashIntent = new Intent(SplashActivitySeller.this, LoginActivitySeller.class);
                startActivity(splashIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);


    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_splash;
    }

}
