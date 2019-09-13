package com.example.roshannizar.rbook.Class;

public class Book
{
    public static final String TABLE_NAME = "book";
    public static final String COLUMN_PIC = "image";
    public static final String COLUMN_ID = "bookid";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_ISBN = "isbn";
    public static final String COLUMN_COPIES = "Copies";
    public static final String COLUMN_SHELF = "shelfno";
    public static final String COLUMN_RACK = "rackno";

    private byte[] image;
    private int id;
    private String title;
    private String author;
    private int year;
    private int isbn;
    private int copies;
    private String shelfno;
    private String rackno;

    public Book()
    {

    }

    public void setImage(byte[] image)
    {
        this.image = image;
    }

    public byte[] getImage()
    {
        return this.image;
    }

    public void setID(int id)
    {
        this.id = id;
    }

    public int getID()
    {
        return id;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return this.title;
    }

    public void setAuthor(String author)
    {
        this.author = author;
    }

    public String getAuthor()
    {
        return this.author;
    }

    public void setYear(int year)
    {
        this.year = year;
    }

    public int getYear()
    {
        return this.year;
    }

    public void setISBN(int isbn)
    {
        this.isbn = isbn;
    }

    public int getIsbn()
    {
        return this.isbn;
    }

    public void setCopies(int copies)
    {
        this.copies = copies;
    }

    public int getCopies()
    {
        return this.copies;
    }

    public void setShelfno(String shelfno)
    {
        this.shelfno = shelfno;
    }

    public String getShelfno()
    {
        return shelfno;
    }

    public void setRackno(String rackno)
    {
        this.rackno = rackno;
    }

    public String getRackno()
    {
        return rackno;
    }
}
