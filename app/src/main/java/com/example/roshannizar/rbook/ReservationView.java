package com.example.roshannizar.rbook;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.roshannizar.rbook.Class.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ReservationView extends AppCompatActivity {

    DBHelper db;
    private ImageView img;
    private RecyclerView rv;
    private RecyclerView.Adapter adapter;
    private List<ReservationList> rlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_view);

        db = new DBHelper(this);

        rv = findViewById(R.id.reserRecycle);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        img = findViewById(R.id.imgRese);

        onDashboard();
        load();
    }

    public void load()
    {
        rlist = new ArrayList<>();

        Cursor result = db.showReser(SignIn.USER_EMAILS);

        if(result.getCount() == 0)
        {
            showMessage("Invalid","No Reservations Made Yet!");
            return;
        }

        while(result.moveToNext())
        {
            ReservationList r = new ReservationList(result.getString(result.getColumnIndex("reserveid")),result.getBlob(result.getColumnIndex("image")),result.getString(result.getColumnIndex("bookid")),result.getString(result.getColumnIndex("shelfno")),result.getString(result.getColumnIndex("rackno")),result.getString(result.getColumnIndex("title")));

            rlist.add(r);
        }

        adapter = new ReserveAdapter(rlist,this);

        rv.setAdapter(adapter);
    }

    public void onDashboard()
    {
        img.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(ReservationView.this,Dashboard.class);
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
}
