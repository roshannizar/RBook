package com.example.roshannizar.rbook;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.Year;
import java.util.Calendar;

public class BookDetails extends AppCompatActivity {

    DBHelper db;
    String id;
    private Button btndate;
    int year,month,day;
    private ImageView img,imgB;
    private TextView txt,txtau,txtc,txtis,txty;
    public static final int DIALOG_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        final Calendar c = Calendar.getInstance();

        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        db = new DBHelper(this);

        imgB = findViewById(R.id.bookImg);
        img = findViewById(R.id.btnBack);
        txt = findViewById(R.id.txttitle);
        txtau = findViewById(R.id.txtauthor);
        txtis = findViewById(R.id.txtisbn);
        txty = findViewById(R.id.txtyear);
        txtc = findViewById(R.id.txtcopies);
        btndate = findViewById(R.id.btnreserve);

        id = getIntent().getStringExtra("id");

        onBack();
        loadBook();
        showDialog();
    }

    public void showDialog()
    {
        btndate.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDialog(DIALOG_ID);
                    }
                }
        );
    }

    @Override
    protected Dialog onCreateDialog(int id)
    {
        if(id == DIALOG_ID)
        {
            return new DatePickerDialog(this,dpickerListner,year,month,day);
        }

        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListner
            = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int y, int m, int d) {
            year = y;
            month = m;
            day = d;
            String da = d+"-"+m+"-"+y;


            boolean result = db.onReserve(SignIn.USER_EMAILS,id,da);
            boolean result1 = db.onUpdateBook(id);

            if(result)
            {
                if(result1) {
                    Toast.makeText(BookDetails.this, "Book Reserved!", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(BookDetails.this, ReservationView.class);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Toast.makeText(BookDetails.this,"Book Copies couldn;t update!",Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(BookDetails.this,"Book was not reserved",Toast.LENGTH_LONG).show();
            }
            //Toast.makeText(BookDetails.this, y+" "+m+" "+d, Toast.LENGTH_SHORT).show();
        }
    };

    public void loadBook()
    {
        Cursor result = db.checkAvai(id);

        if(result.getCount() == 0)
        {
            showMessage("Invalid","Profile not found!");
            return;
        }

        while(result.moveToNext())
        {
            byte[] b = result.getBlob(0);
            Bitmap bt = null;
            bt = BitmapFactory.decodeByteArray(b, 0, b.length);
            Bitmap imageRound = Bitmap.createBitmap(bt.getWidth(), bt.getHeight(), bt.getConfig());
            Canvas c = new Canvas(imageRound);
            Paint p = new Paint();
            p.setAntiAlias(true);
            p.setShader(new BitmapShader(bt, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
            c.drawCircle(bt.getWidth() / 2, bt.getHeight() / 2, bt.getWidth() / 2, p);
            imgB.setImageBitmap(imageRound);

            txt.setText(result.getString(2));
            txtau.setText("Author: " + result.getString(3));
            txty.setText("Year " + result.getString(4));
            txtis.setText("ISBN: " + result.getString(5));
            txtc.setText("Copies Left: " + result.getString(6));
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

    public void onBack()
    {
        img.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(BookDetails.this,ViewBook.class);
                        startActivity(i);
                        finish();
                    }
                }
        );
    }

}