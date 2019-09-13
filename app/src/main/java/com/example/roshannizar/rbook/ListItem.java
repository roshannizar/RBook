package com.example.roshannizar.rbook;

public class ListItem
{
    private String id;
    private byte[] image;
    private String head;
    private String desc;

    public ListItem()
    {

    }

    public ListItem(String head,String desc,byte[] image,String id)
    {
        this.image= image;
        this.head = head;
        this.desc = desc;
        this.id = id;
    }

    public String getID()
    {
        return this.id;
    }

    public String getHead()
    {
        return head;
    }

    public String getDesc()
    {
        return desc;
    }

    public byte[] getImage()
    {
        return image;
    }
}
