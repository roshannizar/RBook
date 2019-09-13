package com.example.roshannizar.rbook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;

public class SignIn extends AppCompatActivity {

    public static String USER_SESSION = "";
    public static String USER_EMAILS = "";
    public static String USER_POSITION = "";
    KenBurnsView image;
    private Button btnSignin;
    private TextView txtForgot;
    private EditText txtusername,txtpassword;
    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_in);

        db = new DBHelper(this);

        txtusername = findViewById(R.id.txtfullname);
        txtpassword = findViewById(R.id.txtconpass);
        btnSignin = findViewById(R.id.btnreset);
        txtForgot = findViewById(R.id.txtforgot);

        onForgorPassword();
        onRedirect();
    }

    public void onRedirect()
    {
        btnSignin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cursor result = db.SignIn(txtusername.getText().toString());
                        if(result.getCount() == 0)
                        {
                            showMessage("Invalid","Username or Password not found!");
                            //customToast();
                            return;
                        }

                        String user="",pass="",name = "",position="";

                        while(result.moveToNext())
                        {
                            user = result.getString(0);
                            name = result.getString(1);
                            pass = result.getString(2);
                            position = result.getString(5);
                        }

                        if(txtusername.getText().toString().equals(user) && txtpassword.getText().toString().equals(pass))
                        {
                            USER_SESSION = name;
                            USER_EMAILS = user;
                            USER_POSITION = position;
                            Intent i = new Intent(SignIn.this, Dashboard.class);
                            startActivity(i);
                            finish();
                        }
                        else
                        {
                            showMessage("Invalid","Bad Username or Password");
                            //customToast();
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

    public void onCreateAccount(View view)
    {
        Intent i =  new Intent(SignIn.this,SignUp.class);
        startActivity(i);
    }

    public void onForgorPassword()
    {
        txtForgot.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (txtusername.getText().toString().equals("")) {
                            showMessage("Access Denied!","Enter your username to proceed");
                        } else {
                            Cursor result = db.SignIn(txtusername.getText().toString());

                            if (result.getCount() == 0) {
                                showMessage("Invalid", "Username or Password not found!");
                                return;
                            }

                            String user = "", pass = "", name = "";

                            while (result.moveToNext()) {
                                user = result.getString(0);
                            }

                            if (txtusername.getText().toString().equals(user)) {
                                USER_SESSION = name;
                                USER_EMAILS = txtusername.getText().toString();
                                Intent i = new Intent(SignIn.this, ForgotPassword.class);
                                startActivity(i);
                            } else {
                                showMessage("Invalid", "Bad Username or Password");
                                //customToast();
                            }
                        }
                    }
                }
        );
    }

    public void showPassword(View view)
    {
        CheckBox showpass = findViewById(R.id.checkBox);

        if(showpass.isChecked())
        {
            txtpassword.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
        }
        else
        {
            txtpassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    public void customToast()
    {
        View tostView = getLayoutInflater().inflate(R.layout.custom_toast,null);
        Toast t = new Toast(getApplicationContext());
        t.setView(tostView);
        t.setDuration(Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, 0,0);
        t.show();
    }
}
