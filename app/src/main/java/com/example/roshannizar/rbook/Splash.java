package com.example.roshannizar.rbook;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.flaviofaria.kenburnsview.KenBurnsView;

public class Splash extends AppCompatActivity {

    KenBurnsView image;
    private static int WELCOME_TIMEOUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        image = (KenBurnsView) findViewById(R.id.imageViewSide);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Splash.this, SelectionPart.class);
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                startActivity(i);
                finish();
            }
        }, WELCOME_TIMEOUT);
    }
}
