package com.example.roshannizar.rbook;

public class ReservationList
{
    private String id;
    private byte[] image;
    private String bookid;
    private String shelfno;
    private String rackno;
    private String title;

    public ReservationList()
    {

    }

    public ReservationList(String id,byte[] image,String bookid,String shelfno,String rackno,String title)
    {
        this.id = id;
        this.image = image;
        this.bookid = bookid;
        this.shelfno = shelfno;
        this.rackno = rackno;
        this.title = title;
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getShelfno()
    {
        return this.shelfno;
    }

    public String getRackno() {
        return rackno;
    }

    public String getID()
    {
        return this.id;
    }

    public byte[] getImage()
    {
        return image;
    }

    public String getBookID()
    {
        return this.bookid;
    }
}
