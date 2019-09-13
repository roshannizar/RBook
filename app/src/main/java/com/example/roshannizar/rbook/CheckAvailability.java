package com.example.roshannizar.rbook;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.roshannizar.rbook.Class.Book;

public class CheckAvailability extends AppCompatActivity {
    DBHelper db;
    Book b;
    public static String BOOKID = "";
    private EditText bookid;
    private Button check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_availability);

        db = new DBHelper(this);
        b = new Book();
        bookid = findViewById(R.id.txtbookidcheck);
        check = findViewById(R.id.btncheck);

        onCheck();
    }

    public void onDashboard(View view)
    {
        Intent i = new Intent(CheckAvailability.this,Selector.class);
        startActivity(i);
        finish();
    }

    public void onCheck()
    {
        check.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Cursor result = db.checkAvai(bookid.getText().toString());
                        if(result.getCount() == 0)
                        {
                            BOOKID = bookid.getText().toString();
                            //showMessage("Not Found","Books not avaialble!");
                            Intent i = new Intent(CheckAvailability.this, AddBook.class);
                            startActivity(i);
                            finish();
                            return;
                        }

                        String id="";

                        while(result.moveToNext())
                        {
                            id = result.getString(1);
                        }

                        if(bookid.getText().toString().equals(id))
                        {
                            showMessage("Found",bookid.getText().toString()+" Book ID available, try a different book id to proceed");
                        }
                        else
                        {
                            BOOKID = bookid.getText().toString();
                            Intent i = new Intent(CheckAvailability.this, AddBook.class);
                            startActivity(i);
                            finish();
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
}
