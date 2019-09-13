package com.example.roshannizar.rbook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.roshannizar.rbook.Class.Book;
import com.example.roshannizar.rbook.Class.Reservation;

import java.sql.Blob;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DBHelper extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "rbook.db";
    private static final String TABLE_NAME = "member";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_FIRST = "fullname";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_CONTACTNO = "contactno";
    private static final String COLUMN_IMAGE = "profilepic";
    private static final String COLUMN_POSITION = "position";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE "+TABLE_NAME+"(email TEXT PRIMARY KEY, fullname TEXT, password TEXT, contactno TEXT, profilepic blob,position TEXT NOT NULL)");
        sqLiteDatabase.execSQL("CREATE TABLE "+Book.TABLE_NAME+"(image blob NOT NULL,bookid TEXT PRIMARY KEY, title TEXT NOT NULL, author TEXT NOT NULL, year TEXT, isbn TEXT NOT NULL, copies TEXT NOT NULL, shelfno TEXT NOT NULL, rackno TEXT NOT NULL)");
        sqLiteDatabase.execSQL("CREATE TABLE "+ Reservation.TABLE_NAME+"(reserveid integer PRIMARY KEY AUTOINCREMENT, username TEXT NOT NULL, bookid TEXT NOT NULL, date DATE NOT NULL," +" FOREIGN KEY ("+Reservation.COLUMN_BOOKID+") REFERENCES "+Book.TABLE_NAME+"("+Book.COLUMN_ID+"))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Book.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+Reservation.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean signup(String fullname,String email, String password,String position)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_FIRST,fullname);
        values.put(COLUMN_EMAIL,email);
        values.put(COLUMN_PASSWORD,password);
        values.put(COLUMN_CONTACTNO,"0");
        values.put(COLUMN_POSITION,position);

        long result = db.insert(TABLE_NAME,null,values);

        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public Cursor SignIn(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE email='"+name+"'",null);
        return result;
    }

    public Cursor ViewAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return result;
    }

    public Cursor ViewAllBook()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM "+Book.TABLE_NAME,null);
        return result;
    }

    public boolean opUpdate(String email,String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_EMAIL,email);
        values.put(COLUMN_PASSWORD,password);

        long result = db.update(TABLE_NAME, values,"email = ?", new String[]{email});

        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean onUpdateProfile(String fullname, String email, String contactno, byte[] profilepic)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_EMAIL,email);
        values.put(COLUMN_FIRST,fullname);
        values.put(COLUMN_CONTACTNO,contactno);
        values.put(COLUMN_IMAGE,profilepic);

        long result = db.update(TABLE_NAME, values,"email = ?", new String[]{email});

        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean onUpdateBook(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Cursor count = db.rawQuery("SELECT * FROM "+Book.TABLE_NAME+ " WHERE bookid='"+id+"'",null);
        int copiescount =0,val=0;

        while(count.moveToNext())
        {
            val = Integer.parseInt(count.getString(6));
        }
        copiescount = val - 1;

        values.put(Book.COLUMN_ID,id);
        values.put(Book.COLUMN_COPIES,copiescount);

        long result = db.update(Book.TABLE_NAME, values,"bookid = ?", new String[]{id});

        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean onUpdateBookVal(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Cursor count = db.rawQuery("SELECT * FROM "+Book.TABLE_NAME+ " WHERE bookid='"+id+"'",null);
        int copiescount =1,val=0;

        while(count.moveToNext())
        {
            val = count.getInt(6);
        }

        copiescount = val + 1;

        values.put(Book.COLUMN_ID,id);
        values.put(Book.COLUMN_COPIES,copiescount);

        long result = db.update(Book.TABLE_NAME, values,"bookid = ?", new String[]{id});

        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean onAddBook(byte[] pic,String bookid, String title, String author, String year, String isbn, String copies,String shelf,String rack)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Book.COLUMN_PIC,pic);
        values.put(Book.COLUMN_ID,bookid);
        values.put(Book.COLUMN_TITLE,title);
        values.put(Book.COLUMN_AUTHOR,author);
        values.put(Book.COLUMN_YEAR,year);
        values.put(Book.COLUMN_ISBN,isbn);
        values.put(Book.COLUMN_COPIES,copies);
        values.put(Book.COLUMN_SHELF,shelf);
        values.put(Book.COLUMN_RACK,rack);

        long result = db.insert(Book.TABLE_NAME, null, values);

        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean onReserve(String username,String bookid,String date)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        String d = null;
        Cursor c = db.rawQuery("SELECT * FROM "+Reservation.TABLE_NAME,null);
        int count = c.getCount();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        try
        {
            d = String.valueOf(sdf.parse(date));
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }

        values.put(Reservation.COLUMN_ID,count+1);
        values.put(Reservation.COLUMN_USERNAME,username);
        values.put(Reservation.COLUMN_BOOKID,bookid);
        values.put(Reservation.COLUMN_DATE,d);

        long result = db.insert(Reservation.TABLE_NAME,null,values);

        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public Cursor checkAvai(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM "+Book.TABLE_NAME+" WHERE bookid='"+id+"'",null);
        return result;
    }

    public Cursor search(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM "+Book.TABLE_NAME+" WHERE title like '%"+id+"%'",null);
        return result;
    }

    public Cursor showReser(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM reservation r, book b,member m WHERE b.bookid=r.bookid AND r.username=m.email AND r.username='"+id+"'",null);
        return result;
    }

    public Cursor showRes(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM "+Reservation.TABLE_NAME+" WHERE reserveid = '"+id+"'",null);
        return result;
    }

    public Cursor showReserList()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM "+Reservation.TABLE_NAME,null);
        return result;
    }

    public boolean onDelete(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Reservation.COLUMN_ID,id);


        long result = db.delete(Reservation.TABLE_NAME,"reserveid = ?", new String[]{id});

        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }


}
