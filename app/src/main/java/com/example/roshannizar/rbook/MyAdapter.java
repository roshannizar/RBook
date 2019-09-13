package com.example.roshannizar.rbook;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>
{
    private List<ListItem> listitems;
    private Context c;

    public MyAdapter(List<ListItem> listitems, Context c)
    {
        this.listitems = listitems;
        this.c = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        ViewHolder vi = new ViewHolder(v,c, (ArrayList<ListItem>) listitems);
        return vi;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ListItem listItem = listitems.get(position);

        holder.txtviewHead.setText(listItem.getHead());
        holder.txtviewDesc.setText(listItem.getDesc());
        byte[] b = listItem.getImage();
        Bitmap bt = null;
        bt = BitmapFactory.decodeByteArray(b, 0, b.length);
        Bitmap imageRound = Bitmap.createBitmap(bt.getWidth(), bt.getHeight(), bt.getConfig());
        Canvas c = new Canvas(imageRound);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setShader(new BitmapShader(bt, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        c.drawCircle(bt.getWidth() / 2, bt.getHeight() / 2, bt.getWidth() / 2, p);
        holder.image.setImageBitmap(imageRound);
    }

    public static byte[] imageViewToByte(ImageView image)
    {
        Bitmap bm = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, s);
        byte[] byteArray = s.toByteArray();
        return byteArray;
    }

    @Override
    public int getItemCount() {
        return listitems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public Button btnView;
        public ImageView image;
        public TextView txtviewHead;
        public TextView txtviewDesc;
        private List<ListItem> listitems = new ArrayList<ListItem>();
        Context c;

        public ViewHolder(View itemView,Context c, ArrayList<ListItem> li)
        {
            super(itemView);
            btnView = itemView.findViewById(R.id.btnDetails);
            this.listitems = li;
            btnView.setOnClickListener(this);
            this.c = c;
            txtviewHead = itemView.findViewById(R.id.textViewHead);
            txtviewDesc = itemView.findViewById(R.id.textViewDesc);
            image = itemView.findViewById(R.id.imagebook);
        }


        @Override
        public void onClick(View view)
        {
            int position = getAdapterPosition();
            ListItem li = this.listitems.get(position);
            Intent i = new Intent(this.c,BookDetails.class);
            i.putExtra("id",li.getID());
            this.c.startActivity(i);
        }
    }
}
