package com.example.roshannizar.rbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class SelectionPart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_selection_part);
    }

    public void signinClick(View view)
    {
        Intent i = new Intent(this,SignIn.class);
        startActivity(i);
        finish();
    }

    public void signupClick(View view)
    {
        Intent i = new Intent(this, SignUp.class);
        startActivity(i);
        finish();
    }
}
