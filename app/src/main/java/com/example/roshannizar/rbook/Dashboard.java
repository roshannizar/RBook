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
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roshannizar.rbook.Class.Reservation;

import java.io.ByteArrayOutputStream;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private String username = "";
    DBHelper db;
    private TextView txtUserActive,avai,position;
    private ImageView image,reser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dash);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = new DBHelper(this);

        NavigationView nv = findViewById(R.id.nav_view);
        View v = nv.getHeaderView(0);
        TextView lblUser = v.findViewById(R.id.lbluser);
        TextView lblEmail = v.findViewById(R.id.lblemail);
        txtUserActive = findViewById(R.id.txtactiveUser);
        image = v.findViewById(R.id.imageViewSide);
        reser = findViewById(R.id.reserimg);
        position = findViewById(R.id.lblPosition);
        txtUserActive.setText("Welcome "+SignIn.USER_SESSION+"!");
        lblUser.setText(SignIn.USER_SESSION);
        lblEmail.setText(SignIn.USER_EMAILS);
        position.setText(SignIn.USER_POSITION);

        avai = findViewById(R.id.totalAvai);

        Curve();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Working on it", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loadBooks();
        loadProfile();
        ClickableReservare();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this,SignIn.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent i = new Intent(Dashboard.this, ReservationView.class);
            startActivity(i);
            finish();
        } else if (id == R.id.nav_gallery) {


        } else if(id == R.id.nav_manage) {
            Intent i = new Intent(Dashboard.this,MyBooks.class);
            startActivity(i);
        }



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    public void profileClick(View view)
    {
        Intent i = new Intent(Dashboard.this, MyBooks.class);
        startActivity(i);
        finish();
    }

    public void ClickableReservare()
    {
        reser.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(Dashboard.this,ReservationView.class);
                        startActivity(i);
                        finish();
                    }
                }
        );
    }

    public void viewBooks(View view)
    {
        Intent i = new Intent(Dashboard.this,ViewBook.class);
        startActivity(i);
        finish();
    }

    public void loadProfile()
    {
        Cursor result = db.SignIn(SignIn.USER_EMAILS);

        while(result.moveToNext())
        {
            username = result.getString(1);

            NavigationView nv = findViewById(R.id.nav_view);
            View v = nv.getHeaderView(0);
            TextView lblUser = v.findViewById(R.id.lbluser);

            txtUserActive.setText("Welcome "+username+"!");
            lblUser.setText(username);

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
                image.setImageBitmap(imageRound);
            }
        }
    }

    public void loadBooks()
    {
        int total=0;
        Cursor result = db.ViewAllBook();

        while(result.moveToNext())
        {
            total = total + 1;
        }

        avai.setText(String.valueOf(total)+" Books Available");
    }

    public void Curve()
    {
        byte[] b = imageViewToByte(image);
        Bitmap bt = null;
        bt = BitmapFactory.decodeByteArray(b, 0, b.length);
        Bitmap imageRound = Bitmap.createBitmap(bt.getWidth(), bt.getHeight(), bt.getConfig());
        Canvas c = new Canvas(imageRound);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setShader(new BitmapShader(bt, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        c.drawCircle(bt.getWidth() / 2, bt.getHeight() / 2, bt.getWidth() / 2, p);
        image.setImageBitmap(imageRound);
    }

    public static byte[] imageViewToByte(ImageView image)
    {
        Bitmap bm = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, s);
        byte[] byteArray = s.toByteArray();
        return byteArray;
    }

    public void onAddRedirect(View view)
    {
        if(position.getText().toString().equals("Standard")) {
            showMessage("Access Denied!","You're not the Administrator, Check your position!");
        }
        else {
            Intent i = new Intent(Dashboard.this, Selector.class);
            startActivity(i);
            finish();
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
}
