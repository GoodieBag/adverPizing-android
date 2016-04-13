package com.jp.band.com.smartkube;

/**
 * Created by kai on 8/4/16.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    private ImageLoader imageLoader;
    private Context context;

    //List of superHeroes
    List<Item> items;

    public CardAdapter(List<Item> items, Context context){
        super();
        //Getting all the superheroes
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_data, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Item item =  items.get(position);


        imageLoader = CustomVolleyRequestQueue.getInstance(context).getImageLoader();
        imageLoader.get(item.getImage(), ImageLoader.getImageListener(holder.imageView, R.mipmap.ic_launcher, android.R.drawable.ic_dialog_alert));







        holder.textViewItemHeader.setText(item.getHeader());
        holder.textViewItemDescription.setText(item.getDescription());
        holder.textViewOldPrice.setText(item.getold_price());
        holder.textViewNewPrice.setText(item.getNew_price());





    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView imageView;
        public TextView textViewItemHeader;
        public TextView textViewItemDescription;
        public TextView textViewOldPrice;
        public TextView textViewNewPrice;


        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.item_image);
            textViewItemHeader = (TextView) itemView.findViewById(R.id.item_header);
            textViewItemDescription= (TextView) itemView.findViewById(R.id.item_description);
            textViewOldPrice= (TextView) itemView.findViewById(R.id.item_old_price);
            textViewNewPrice= (TextView) itemView.findViewById(R.id.item_new_price);

        }
    }
}

