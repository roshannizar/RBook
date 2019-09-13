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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ReserveAdapter extends RecyclerView.Adapter<ReserveAdapter.ViewHolder>
{
    private List<ReservationList> Rlist;
    private Context c;

    public ReserveAdapter(List<ReservationList> Rlist, Context c)
    {
        this.Rlist = Rlist;
        this.c = c;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_list,parent,false);
        ViewHolder vi = new ViewHolder(v,c, (ArrayList<ReservationList>) Rlist);
        return vi;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReservationList rlist = Rlist.get(position);

        holder.txtr.setText("Rack No: "+rlist.getRackno());
        holder.txts.setText("Shelf NO: "+rlist.getShelfno());
        holder.txtbid.setText(rlist.getBookID());
        holder.title.setText(rlist.getTitle());
        byte[] b = rlist.getImage();
        Bitmap bt = null;
        bt = BitmapFactory.decodeByteArray(b, 0, b.length);
        Bitmap imageRound = Bitmap.createBitmap(bt.getWidth(), bt.getHeight(), bt.getConfig());
        Canvas c = new Canvas(imageRound);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setShader(new BitmapShader(bt, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
        c.drawCircle(bt.getWidth() / 2, bt.getHeight() / 2, bt.getWidth() / 2, p);
        holder.img.setImageBitmap(imageRound);
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
        return Rlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public ImageView img;
        public Button btnView;
        public TextView txtbid,txtr,txts,title;

        private List<ReservationList> rl;
        Context c;

        public ViewHolder(View itemView,Context c, ArrayList<ReservationList> rl)
        {
            super(itemView);
            btnView = itemView.findViewById(R.id.btnCancel);
            this.rl = rl;
            btnView.setOnClickListener(this);
            this.c = c;
            title = itemView.findViewById(R.id.title);
            txtbid = itemView.findViewById(R.id.textBookID);
            img = itemView.findViewById(R.id.reserveBook);
            txtr = itemView.findViewById(R.id.txtrack);
            txts = itemView.findViewById(R.id.txtshelf);
        }

        @Override
        public void onClick(View view)
        {
            DBHelper db = new DBHelper(this.c);
            int position = getAdapterPosition();
            ReservationList li = this.rl.get(position);
            Intent i = new Intent(this.c,ReservationView.class);
            i.putExtra("id",li.getID());

            String id = li.getID();

            boolean result = db.onDelete(id);
            boolean result1 = db.onUpdateBookVal(li.getBookID());
            if(result)
            {
                if(result1) {
                    Toast.makeText(this.c, "Reservation was cancelled successfully!", Toast.LENGTH_LONG).show();
                    this.c.startActivity(i);
                }
                else
                {
                    Toast.makeText(this.c,"Book copies couldn't update!",Toast.LENGTH_LONG).show();
                }
            }
            else
            {
                Toast.makeText(this.c,"Reservation was not cancelled!",Toast.LENGTH_LONG).show();
            }
        }
    }
}

