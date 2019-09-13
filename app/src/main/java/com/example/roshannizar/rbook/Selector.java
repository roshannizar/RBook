package com.example.roshannizar.rbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Selector extends AppCompatActivity {

    private ImageView imgadd,imgupdate,imgdelete,btngo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selector);

        imgadd = findViewById(R.id.btnadd);
        btngo = findViewById(R.id.btnGoTo);

        onDashboardRedirect();
        onAddRedirect();
    }

    public void onDashboardRedirect()
    {
        btngo.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Selector.this,Dashboard.class);
                        startActivity(i);
                        finish();
                    }
                }
        );
    }

    public void onAddRedirect()
    {
        imgadd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Selector.this,CheckAvailability.class);
                        startActivity(i);
                        finish();
                    }
                }
        );
    }
}
