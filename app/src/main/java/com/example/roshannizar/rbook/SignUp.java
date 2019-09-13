package com.example.roshannizar.rbook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class SignUp extends AppCompatActivity {

    private TextView bttn;
    private EditText txtfullname,txtusername,txtpassword,txtconpass;
    private Button btnsignup;
    private ImageView img;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);
        db = new DBHelper(this);
        txtfullname = findViewById(R.id.txtfullname);
        txtusername = findViewById(R.id.txtemail);
        txtpassword = findViewById(R.id.txtpass);
        txtconpass = findViewById(R.id.txtconpass);
        bttn = findViewById(R.id.btt);

        btnsignup = findViewById(R.id.btnreset);

        signupUser();
        User();
    }



    public void signupUser()
    {
        btnsignup.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(txtfullname.getText().toString().equals("") || (txtusername.getText().toString().equals("")) || (txtpassword.getText().toString().equals("")) || (txtconpass.getText().toString().equals("")))
                        {
                            showMessage("Fields Empty!","Please fill all the above details!");
                        }
                        else {
                            if (txtpassword.getText().toString().equals(txtconpass.getText().toString())) {
                                boolean result = db.signup(txtfullname.getText().toString(), txtusername.getText().toString(), txtpassword.getText().toString(),"Standard");

                                if (result) {
                                    Toast.makeText(SignUp.this, "Account Created Successfully!", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(SignUp.this, SignIn.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    Toast.makeText(SignUp.this, "Account was not created!", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(SignUp.this, "Password didn't match", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );
    }

    public void User()
    {
        bttn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(txtfullname.getText().toString().equals("") && (txtusername.getText().toString().equals("")) && (txtpassword.getText().toString().equals("")) && (txtconpass.getText().toString().equals("")))
                        {
                            showMessage("Fields Empty!","Please fill all the above details!");
                        }
                        else {
                            if (txtpassword.getText().toString().equals(txtconpass.getText().toString())) {
                                boolean result = db.signup(txtfullname.getText().toString(), txtusername.getText().toString(), txtpassword.getText().toString(),"Administrator");

                                if (result) {
                                    Toast.makeText(SignUp.this, "Account Created Successfully!", Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(SignUp.this, SignIn.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    Toast.makeText(SignUp.this, "Account was not created!", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(SignUp.this, "Password didn't match", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );
    }

    public void showMessage(String title,String message)
    {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setCancelable(true);
        b.setTitle(title);
        b.setIcon(R.drawable.info);
        b.setPositiveButton("OK",null);
        b.setMessage(message);
        b.show();
    }

    /*public static byte[] imageViewToByte(ImageView image)
    {
        Bitmap bm = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, s);
        byte[] byteArray = s.toByteArray();
        return byteArray;
    }*/
}
