package com.example.roshannizar.rbook;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class Copies extends AppCompatActivity {

    DBHelper db;
    private EditText txtcopies,txtshelf,txtrack;
    private Button btnSetBook;
    private String id,title,author,year,isbn;
    private ImageView img;
    private byte[] b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copies);

        db = new DBHelper(this);
        txtcopies = findViewById(R.id.txtcopies);
        txtshelf = findViewById(R.id.txtshelf);
        txtrack = findViewById(R.id.txtrack);
        btnSetBook = findViewById(R.id.btnsetbook);
        img = findViewById(R.id.imageView16);

        id = AddBook.id;
        year = AddBook.year;
        isbn = AddBook.isbn;
        b = AddBook.imageb;
        title = AddBook.title;
        author = AddBook.author;

        onRedirect();
        onAdd();
    }

    public void onAdd()
    {
        btnSetBook.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(txtcopies.getText().toString().equals("") || txtshelf.getText().toString().equals("") || txtrack.getText().toString().equals(""))
                        {
                            showMessage("Invalid!","Feilds or some details are missing!");
                        }
                        else {
                            boolean isInserted = db.onAddBook(b, id, title, author, year, isbn, txtcopies.getText().toString(), txtshelf.getText().toString(), txtrack.getText().toString());

                            if (isInserted) {
                                showMessage("Success", "Book details added successfully!");
                                Intent i = new Intent(Copies.this, ViewBook.class);
                                startActivity(i);
                                finish();
                            } else {
                                showMessage("Failed", "Book details was not added successfully!");
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

    public void onRedirect()
    {
        img.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Copies.this,AddBook.class);
                        startActivity(i);
                        finish();
                    }
                }
        );
    }
}
