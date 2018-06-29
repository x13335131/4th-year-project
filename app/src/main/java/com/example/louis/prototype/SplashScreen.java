package com.example.louis.prototype;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {
    private static final int DELAY_MILLISECONDS = 6000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Handler handler = new Handler();

        ImageView imageView = (ImageView) findViewById(R.id.splashLogo); //image produced on fade
        Animation loadAnimation = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in); //loading fade in animation
        loadAnimation.setDuration(5000);//animation lasts 5000 milliseconds
        imageView.startAnimation(loadAnimation);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() { //delay content change
                Intent i = new Intent(SplashScreen.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, DELAY_MILLISECONDS);
    }
}
