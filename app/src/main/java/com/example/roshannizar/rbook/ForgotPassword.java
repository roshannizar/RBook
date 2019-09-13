package com.example.roshannizar.rbook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPassword extends AppCompatActivity {

    DBHelper db;
    EditText txtemail,txtconpass,txtpassword;
    Button btnreset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot_password);

        db = new DBHelper(this);

        txtemail = findViewById(R.id.txtfullname);
        txtpassword = findViewById(R.id.txtpass);
        txtconpass = findViewById(R.id.txtconpass);
        btnreset = findViewById(R.id.btnreset);

        txtemail.setText(SignIn.USER_EMAILS);
        onUpdateProfile();
    }

    public void onUpdateProfile()
    {
        btnreset.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        if(txtpassword.getText().toString().equals(txtconpass.getText().toString()))
                        {
                            boolean result = db.opUpdate(txtemail.getText().toString(),txtpassword.getText().toString());

                            if(result)
                            {
                                Toast.makeText(ForgotPassword.this,"Password reset was successfully!",Toast.LENGTH_LONG).show();
                                Intent i = new Intent(ForgotPassword.this,SignIn.class);
                                startActivity(i);
                                finish();
                            }
                            else
                            {
                                Toast.makeText(ForgotPassword.this, "There was an error while updating your password!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );
    }
}
