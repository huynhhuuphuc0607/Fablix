package com.example.huynh.fablix;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private Context mContext;
    private int resID;
    private List<Movie> movieList;
    private Map<Movie, Integer> map;

    public ItemAdapter(Context c, int resID, Map<Movie, Integer> map) {
        mContext = c;
        this.resID = resID;
        this.map = map;
        movieList = new ArrayList<>();
        for(Map.Entry<Movie,Integer> entry : map.entrySet())
        {
            movieList.add(entry.getKey());
        }
        Log.d("ItemAdapter", "Cart size: " + movieList.size());
    }
    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(mContext).inflate(resID, parent, false);
        return new ViewHolder(v);
    }

    public double getTotalPrice()
    {
        double total = 0.0;
        for(Map.Entry<Movie,Integer> entry : map.entrySet())
        {
            total += entry.getValue()*entry.getKey().getPrice();
        }

        return total;
    }
    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder viewHolder, int i) {
        ImageView movieAvatarImageView = viewHolder.movieAvatarImageView;
        TextView movieTitleTextView = viewHolder.movieTitleTextView;
        TextView movieQuantityTextView = viewHolder.movieQuantityTextView;
        TextView moviePriceTextView = viewHolder.moviePriceTextView;

        Movie m = movieList.get(i);
        movieAvatarImageView.setImageResource(m.getResID());
        movieTitleTextView.setText(m.getmTitle());
        movieQuantityTextView.setText("Qty: "+ map.get(m) );
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
        moviePriceTextView.setText(format.format(m.getPrice()));
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView movieAvatarImageView;
        public TextView movieTitleTextView;
        public TextView movieQuantityTextView;
        public TextView moviePriceTextView;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            movieAvatarImageView = itemView.findViewById(R.id.movieAvatarImageView);
            movieTitleTextView = itemView.findViewById(R.id.movieTitleTextView);
            movieQuantityTextView = itemView.findViewById(R.id.movieQuantityTextView);
            moviePriceTextView = itemView.findViewById(R.id.moviePriceTextView);
        }
    }
}
