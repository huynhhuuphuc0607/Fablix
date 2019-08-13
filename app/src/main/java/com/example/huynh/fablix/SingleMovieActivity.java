package com.example.huynh.fablix;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

public class SingleMovieActivity extends AppCompatActivity {
    private Intent mIntent;
    private ImageView movieAvatarImageView;
    private TextView movieTitleTextView;
    private RatingBar mRatingBar;
    private TextView mRatingTextView;
    private TextView yearTextYear;
    private TextView directorTextView;
    private TextView genresTextView;

    private RecyclerView starRecyclerView;
    private ArrayList<Star> starList;
    private StarAdapter starAdapter;

    private FloatingActionButton fab;

    private Movie m;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_movie);
        mIntent = getIntent();
        fab = findViewById(R.id.fab);
        m = mIntent.getParcelableExtra("movie");

        mRatingTextView = findViewById(R.id.ratingTextView);
        movieAvatarImageView = findViewById(R.id.movieAvatarImageView);
        movieTitleTextView = findViewById(R.id.movieTitleTextView);
        mRatingBar = findViewById(R.id.ratingBar);
        yearTextYear = findViewById(R.id.yearTextYear);
        directorTextView = findViewById(R.id.directorTextView);
        genresTextView = findViewById(R.id.genresTextView);


        Log.d("SingleMovieActivitiy", "receive res id:" + mIntent.getIntExtra("resId", 0));
        movieAvatarImageView.setImageResource(m.getResID());
        movieTitleTextView.setText(m.getmTitle());
        //--------stars------
        starRecyclerView = findViewById(R.id.starRecyclerView);
        starList = m.getmStars();
        starAdapter = new StarAdapter(this, R.layout.star_one_row, starList);
        starRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        starRecyclerView.setAdapter(starAdapter);

        yearTextYear.setText("Year: " + m.getYear());
        directorTextView.setText("Director: " + m.getDirector());
        String genreString = "";
        ArrayList<Genre> genres = m.getmGenres();
        for(Genre g : genres)
            genreString += g.getName() + ", ";
        genreString = genreString.substring(0,genreString.length() - 2);

        genresTextView.setText("Genre(s): " + genreString );

        mRatingBar.setRating(m.getRating());
        mRatingTextView.setText("Rating: " + m.getRating());
        fab.setTag(R.id.id1, m);

    }

    /**
     *Navigate to Single Star Activity
     * @param v the view the user clicks on to navigate to Single Star Activity
     */
    public void gotoSingleStarActivity(View v) {
        Intent intent = new Intent(SingleMovieActivity.this, SingleStarActivity.class);
        Star s = (Star) v.getTag(R.id.id1);

        //tags [] = {position, imageView id, titleTextView id}
        String[] tags = ((String) v.getTag(R.id.id2)).split("\\|");

        // make object parcelable
        intent.putExtra("star", s);

        View imageview = starAdapter.getLinearLayout(Integer.parseInt(tags[0])).findViewById(Integer.parseInt(tags[1]));
        View titleTextView = starAdapter.getLinearLayout(Integer.parseInt(tags[0])).findViewById(Integer.parseInt(tags[2]));
        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SingleMovieActivity.this,
                Pair.create(imageview, getString(R.string.transition_star_avatar)),
                Pair.create(titleTextView, getString(R.string.transition_star_name)));
        startActivity(intent, options.toBundle());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cartMenu:
                startActivity(new Intent(SingleMovieActivity.this, ShoppingCartActivity.class));
                break;
            case R.id.accountMenu:
                break;
        }
        return true;
    }

    /**
     *Add a movie to the shopping cart
     * @param v the view the user clicks on to add a movie to the shopping cart
     */
    public void addtoCart(View v) {
        MovieListActivity.cart.addMovie((Movie) v.getTag(R.id.id1));
    }
}
