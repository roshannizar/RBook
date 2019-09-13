package com.example.roshannizar.rbook;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ViewBook extends AppCompatActivity {

    DBHelper db;
    private RecyclerView rv;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;
    private ImageView img;
    private Button btncancel;
    private EditText txtsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_book);


        db = new DBHelper(this);

        rv = findViewById(R.id.recycle);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        img = findViewById(R.id.imageViewSideViewer);
        txtsearch = findViewById(R.id.txtsearch);
        btncancel = findViewById(R.id.btnsearch);

        loadSearch();
        load();
        onRedirect();
        Curve();
    }

    public void load()
    {
        listItems = new ArrayList<>();

        Cursor result = db.ViewAllBook();

        if(result.getCount() == 0)
        {
            showMessage("Invalid","Books are not available!");
            return;
        }

        while(result.moveToNext())
        {
            ListItem listitem = new ListItem(result.getString(2), result.getString(3),result.getBlob(result.getColumnIndex("image")),result.getString(1));

            listItems.add(listitem);
        }

        adapter = new MyAdapter(listItems,this);

        rv.setAdapter(adapter);
    }

    public void onRedirect()
    {
        img.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(ViewBook.this,Dashboard.class);
                        startActivity(i);
                        finish();
                    }
                }
        );
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

    public static byte[] imageViewToByte(ImageView image)
    {
        Bitmap bm = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, s);
        byte[] byteArray = s.toByteArray();
        return byteArray;
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

    public void search()
    {
        listItems = new ArrayList<>();

        listItems.clear();

        Cursor result = db.search(txtsearch.getText().toString());

        if(result.getCount() == 0)
        {
            showMessage("Invalid","Books are not available!");
            return;
        }

        while(result.moveToNext())
        {
            ListItem listitem = new ListItem(result.getString(2), result.getString(3),result.getBlob(0),result.getString(1));

            listItems.add(listitem);
        }

        adapter = new MyAdapter(listItems,this);

        rv.setAdapter(adapter);
    }

    public void loadSearch()
    {
        btncancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(txtsearch.getText().toString().equals(""))
                        {
                            load();
                        }
                        else {
                            search();
                        }
                    }
                }
        );
    }
}
