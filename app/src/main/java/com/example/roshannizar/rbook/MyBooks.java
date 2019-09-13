package com.example.roshannizar.rbook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class MyBooks extends AppCompatActivity {

    final int REQUEST_CODE_GALLERY = 999;
    DBHelper db;
    TextView txtemail,txtpassword,txtfullname,txtcontact;
    TextView txtsession;
    Button btnupdate;
    ImageView imgProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.mybooks);

        db = new DBHelper(this);

        txtsession = findViewById(R.id.lblStatusLabel);

        txtsession.setText(SignIn.USER_EMAILS);

        txtemail = findViewById(R.id.txtEmailtxt);
        txtpassword = findViewById(R.id.txtpasswordtxt);
        txtcontact = findViewById(R.id.txtContactnotxt);
        txtfullname = findViewById(R.id.txtfullnametxt);
        imgProfile = findViewById(R.id.imgprofPic);

        btnupdate = findViewById(R.id.button);

        loadProfile();
        onProfileUpdate();
        chooseProfile();
    }

    public void loadProfile()
    {
        Cursor result = db.SignIn(SignIn.USER_EMAILS);

        if(result.getCount() == 0)
        {
            showMessage("Invalid","Profile not found!");
            return;
        }

        while(result.moveToNext())
        {
            txtfullname.setText(result.getString(1));
            txtemail.setText(result.getString(0));
            txtpassword.setText(result.getString(2));
            txtcontact.setText(result.getString(3));

            if(result.getBlob(4) != null) {
                byte[] b = result.getBlob(4);
                Bitmap bt = null;
                bt = BitmapFactory.decodeByteArray(b, 0, b.length);
                Bitmap imageRound = Bitmap.createBitmap(bt.getWidth(), bt.getHeight(), bt.getConfig());
                Canvas c = new Canvas(imageRound);
                Paint p = new Paint();
                p.setAntiAlias(true);
                p.setShader(new BitmapShader(bt, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                c.drawCircle(bt.getWidth() / 2, bt.getHeight() / 2, bt.getWidth() / 2, p);
                imgProfile.setImageBitmap(imageRound);
            }
        }
    }

    public void showMessage(String title,String message)
    {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setCancelable(true);
        b.setTitle(title);
        b.setPositiveButton("OK",null);
        b.setMessage(message);
        b.show();
    }

    public void onProfileUpdate()
    {
        btnupdate.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        boolean result = db.onUpdateProfile(txtfullname.getText().toString(),txtemail.getText().toString(),txtcontact.getText().toString(),imageViewToByte(imgProfile));

                        if(result)
                        {
                            Toast.makeText(MyBooks.this,"Profile update successfully!",Toast.LENGTH_LONG).show();
                            loadProfile();
                        }
                        else
                        {
                            Toast.makeText(MyBooks.this,"Profile was not updated!",Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    public void onRedirect(View view)
    {
        Intent i = new Intent(MyBooks.this,Dashboard.class);
        startActivity(i);
        finish();
    }

    public static byte[] imageViewToByte(ImageView image)
    {
        Bitmap bm = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, s);
        byte[] byteArray = s.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode == REQUEST_CODE_GALLERY)
        {
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else
            {
                Toast.makeText(getApplicationContext(), "You don't have permission to access the file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null)
        {
            Uri uri = data.getData();

            try
            {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imgProfile.setImageBitmap(bitmap);
                Curve();
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void chooseProfile()
    {
        imgProfile.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        ActivityCompat.requestPermissions(MyBooks.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
                    }
                }
        );
    }

    public void Curve()
    {
        byte[] b = imageViewToByte(imgProfile);
        Bitmap bt = null;
        bt = BitmapFactory.decodeByteArray(b, 0, b.length);
        Bitmap imageRound = Bitmap.createBitmap(bt.getWidth(), bt.getHeight(), bt.getConfig());
        Canvas c = new Canvas(imageRound);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setShader(new BitmapShader(bt, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        c.drawCircle(bt.getWidth() / 2, bt.getHeight() / 2, bt.getWidth() / 2, p);
        imgProfile.setImageBitmap(imageRound);
    }
}
