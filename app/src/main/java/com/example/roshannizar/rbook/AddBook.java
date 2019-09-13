package com.example.roshannizar.rbook;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.roshannizar.rbook.Class.Book;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddBook extends AppCompatActivity {

    Book b;
    final int REQUEST_CODE_GALLERY = 999;
    public static byte[] imageb;
    public static String id="",title="",author="";
    public static String year="",isbn="";
    private EditText txtid,txttitle,txtauthor,txtyear,txtisbn;
    private Button btnext,btimage;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        txtid = findViewById(R.id.txtbookid);
        txttitle = findViewById(R.id.txttitle);
        txtauthor = findViewById(R.id.txtauthor);
        txtyear = findViewById(R.id.txtyear);
        txtisbn = findViewById(R.id.txtisbn);

        btnext = findViewById(R.id.btnnext);
        btimage = findViewById(R.id.btnimage);
        img = findViewById(R.id.imgBook);

        txtid.setText(CheckAvailability.BOOKID);

        Curve();
        chooseProfile();
        onRedirect();
    }

    public void onAvai(View view)
    {
        Intent i = new Intent(AddBook.this,CheckAvailability.class);
        startActivity(i);
        finish();
    }

    public void onRedirect()
    {
        btnext.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(txtid.getText().toString().equals("") || txttitle.getText().toString().equals("") || txtauthor.getText().toString().equals("") || txtyear.getText().toString().equals("") || txtisbn.getText().toString().equals(""))
                        {
                            showMessage("Invalid!","Please fill valid informations!");
                        }
                        else {
                            imageb = imageViewToByte(img);
                            id = txtid.getText().toString();
                            title = txttitle.getText().toString();
                            author = txtauthor.getText().toString();
                            year = txtyear.getText().toString();
                            isbn = txtisbn.getText().toString();

                            Intent i = new Intent(AddBook.this, Copies.class);
                            startActivity(i);
                            finish();
                        }
                    }
                }
        );
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
                img.setImageBitmap(bitmap);
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
        btimage.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        ActivityCompat.requestPermissions(AddBook.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
                    }
                }
        );
    }

    public void Curve()
    {
        byte[] b = imageViewToByte(img);
        Bitmap bt = null;
        bt = BitmapFactory.decodeByteArray(b, 0, b.length);
        Bitmap imageRound = Bitmap.createBitmap(bt.getWidth(), bt.getHeight(), bt.getConfig());
        Canvas c = new Canvas(imageRound);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setShader(new BitmapShader(bt, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        c.drawCircle(bt.getWidth() / 2, bt.getHeight() / 2, bt.getWidth() / 2, p);
        img.setImageBitmap(imageRound);
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
}
