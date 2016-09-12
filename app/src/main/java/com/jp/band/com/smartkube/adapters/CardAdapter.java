package com.jp.band.com.smartkube.adapters;

/**
 * Created by kai on 8/4/16.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.jp.band.com.smartkube.R;
import com.jp.band.com.smartkube.models.Item;
import com.jp.band.com.smartkube.networks.CustomVolleyRequestQueue;

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
        holder.textViewItemHeader.setText(item.getName());
        holder.textViewItemDescription.setText(item.getDescription());
        holder.textViewOldPrice.setText(item.getPlace());
        holder.textViewNewPrice.setText(item.getLastDate());
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

