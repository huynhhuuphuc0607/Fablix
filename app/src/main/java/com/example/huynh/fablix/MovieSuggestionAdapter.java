package com.example.huynh.fablix;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MovieSuggestionAdapter extends RecyclerView.Adapter<MovieSuggestionAdapter.ViewHolder> {
    private Context mContext;
    private int resID;
    private ArrayList<Movie> mMovieList;
    private int[] drawables;
    private ArrayList<LinearLayout> linearLayouts;
    private int lastPosition = -1;

    public MovieSuggestionAdapter(Context c, int resID, ArrayList<Movie> movieList)
    {
        mContext = c;
        this.resID = resID;
        mMovieList = movieList;
        drawables = new int[]{R.drawable.avg4, R.drawable.ff8, R.drawable.cap2};
        linearLayouts = new ArrayList<>();
    }

    @Override
    public MovieSuggestionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(resID, parent, false);
        return new MovieSuggestionAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieSuggestionAdapter.ViewHolder holder, int position) {
        ImageView suggestionImageView = holder.suggestionImageView;
        TextView suggestionTitleTextView = holder.suggestionTitleTextView;
        TextView suggestionYearTextView = holder.suggestionYearTextView;
        TextView suggestionDirectorTextView = holder.suggestionDirectorTextView;
        final LinearLayout suggestionLineaLayout = holder.suggestionLineaLayout;

        suggestionImageView.setImageResource(drawables[position%3]);
        Movie m = mMovieList.get(position);
        m.setResID(drawables[position%3]);
        suggestionTitleTextView.setText(m.getmTitle());
        suggestionYearTextView.setText(m.getYear()+"");
        suggestionDirectorTextView.setText(m.getDirector());
        suggestionLineaLayout.setTag(R.id.id1,m);
        suggestionLineaLayout.setTag(R.id.id2,position+"|"+suggestionImageView.getId()+"|"+suggestionTitleTextView.getId());
        linearLayouts.add(suggestionLineaLayout);

        suggestionLineaLayout.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_in));


        lastPosition = position;
    }
    /**
     *Get the linear layout of a specific position (needed for further customization)
     * @param position the position of the view
     * @return the linear layout that contains the view
     */
    public LinearLayout getLinearLayout (int position)
    {
        return linearLayouts.get(position);
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView suggestionImageView;
        public TextView suggestionTitleTextView;
        public TextView suggestionYearTextView;
        public TextView suggestionDirectorTextView;
        public LinearLayout suggestionLineaLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            suggestionImageView = itemView.findViewById(R.id.suggestionImageView);
            suggestionTitleTextView = itemView.findViewById(R.id.suggestionTitleTextView);
            suggestionYearTextView = itemView.findViewById(R.id.suggestionYearTextView);
            suggestionDirectorTextView = itemView.findViewById(R.id.suggestionDirectorTextView);
            suggestionLineaLayout = itemView.findViewById(R.id.suggestionLineaLayout);
        }
    }
}
