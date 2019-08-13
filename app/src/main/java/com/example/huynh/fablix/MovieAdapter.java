package com.example.huynh.fablix;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private Context mContext;
    private int resID;
    private ArrayList<Movie> mMovieList;
    private int[] drawables;
    private List<CardView>  cardViews;
    private int lastPosition = -1;

    public MovieAdapter(Context c, int resID, ArrayList<Movie> movieList) {
        mContext = c;
        this.resID = resID;
        mMovieList = movieList;
        drawables = new int[]{R.drawable.avg4, R.drawable.ff8, R.drawable.cap2};
        cardViews = new ArrayList<>();
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

    public CardView getCardView(int position)
    {
        return cardViews.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(mContext).inflate(resID, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        CardView cardView = viewHolder.movieCardView;
        Movie movie = mMovieList.get(i);
        ImageView movieImageView = viewHolder.movieImageView;
        TextView titleTextView = viewHolder.movieNameTextView;
        TextView subtitleTextView = viewHolder.movieSubtitleTextView;
        TextView movieYearTextView = viewHolder.movieYearTextView;
        TextView movieDirectorTextView = viewHolder.movieDirectorTextView;
        TextView movieGenresTextView = viewHolder.movieGenresTextView;

        Button addtoCartButton = viewHolder.addtoCartButton;

        movieImageView.setImageResource(drawables[i % 3]);
        movie.setResID(drawables[i%3]);
        ArrayList<Star> stars = movie.getmStars();
        String starString = "";
        for (Star star : stars)
            starString += star.getName() + ", ";
        if(!starString.isEmpty())
            subtitleTextView.setText("Stars: " + starString.substring(0, starString.length() - 2));

        titleTextView.setText(movie.getmTitle());
        movieYearTextView.setText("Year: " + movie.getYear());
        movieDirectorTextView.setText("Director: " + movie.getDirector());
        String genreString = "";
        ArrayList<Genre> genres = movie.getmGenres();
        for (Genre g : genres)
            genreString += g.getName() + ", ";
        if(!genreString.isEmpty())
            movieGenresTextView.setText("Genres: " + genreString.substring(0, genreString.length() - 2));

        cardView.setTag(R.id.id1,movie);
        cardView.setTag(R.id.id2,i+"|"+movieImageView.getId()+"|"+titleTextView.getId());
        addtoCartButton.setTag(R.id.id1,movie);
        cardViews.add(cardView);

//        Animation animation = AnimationUtils.loadAnimation(mContext,
//                (i > lastPosition)? R.anim.fade_in_recyclerview :R.anim.fade_in_recyclerview);
        cardView.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_in_recyclerview));
        lastPosition = i;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CardView movieCardView;
        public ImageView movieImageView;
        public TextView movieNameTextView;
        public TextView movieSubtitleTextView;
        public TextView movieYearTextView;
        public TextView movieDirectorTextView;
        public TextView movieGenresTextView;
        public Button addtoCartButton;
        public ViewHolder(View itemView) {
            super(itemView);
            movieCardView = itemView.findViewById(R.id.movieCardView);
            movieImageView = itemView.findViewById(R.id.movieImageView);
            movieNameTextView = itemView.findViewById(R.id.movieTitleTextView);
            movieSubtitleTextView = itemView.findViewById(R.id.movieSubtitleTextView);
            movieYearTextView = itemView.findViewById(R.id.movieYearTextView);
            movieDirectorTextView = itemView.findViewById(R.id.movieDirectorTextView);
            addtoCartButton = itemView.findViewById(R.id.addtoCartButton);
            movieGenresTextView = itemView.findViewById(R.id.movieGenresTextView);
        }

    }
}
