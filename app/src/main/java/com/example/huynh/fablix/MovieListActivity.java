package com.example.huynh.fablix;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieListActivity extends AppCompatActivity {
    private RecyclerView movieListRecyclerView;
    private ArrayList<Movie> movieList;
    private MovieAdapter mMovieAdapter;
    public static Cart cart = new Cart();
    private TextView empty_view;

    String title = "Rain";
    int firstRecord = 0;
    private static final int NUMRECORDS = 6;

    long lastBack = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieListRecyclerView = findViewById(R.id.movieListRecyclerView);
        empty_view = findViewById(R.id.empty_view);

        movieList = new ArrayList<Movie>();
        mMovieAdapter = new MovieAdapter(this, R.layout.movie_one_row, movieList);
        movieListRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        movieListRecyclerView.setAdapter(mMovieAdapter);


        getMovies(MovieListActivity.this, title, 0, "", "", "", "", firstRecord, NUMRECORDS, "RD");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem item = menu.findItem(R.id.searchviewMenu);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Search titles");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                firstRecord = 0;
                title = query;
                getMovies(MovieListActivity.this, title, 0, "", "", "", "", firstRecord, NUMRECORDS, "RD");
                item.collapseActionView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return true;
            }
        });


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cartMenu:
                startActivity(new Intent(MovieListActivity.this, ShoppingCartActivity.class));
                break;
            case R.id.accountMenu:
                startActivity(new Intent(MovieListActivity.this, LoginActivity.class));
                Toast.makeText(MovieListActivity.this, getString(R.string.log_out_successfully), Toast.LENGTH_SHORT).show();
                MovieListActivity.this.finish();
                break;
        }
        return true;
    }

    /**
     *Navigate to the next page and show all the movie within the new page
     * if there's none, a toast will be displayed to inform that they have reached the end of the result list
     */
    public void showNext(View v) {
        if (movieList.size() > 0) {
            firstRecord += NUMRECORDS;
            getMovies(MovieListActivity.this, title, 0, "", "", "", "", firstRecord, NUMRECORDS, "RD");
        } else {
            Toast.makeText(MovieListActivity.this, "Bruhhh... There's really nothing to see here", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *Navigate to the previous page and show all the movie within the new page
     * if there's none, a toast will be displayed to inform that they have reached the end of the result list
     */
    public void showPrev(View v) {
        if (movieList.size() > 0) {
            if (firstRecord == 0) {
                Toast.makeText(MovieListActivity.this, "You are at first page", Toast.LENGTH_SHORT).show();
            } else {
                firstRecord -= NUMRECORDS;
                if (firstRecord < 0)
                    firstRecord = 0;
                getMovies(MovieListActivity.this, title, 0, "", "", "", "", firstRecord, NUMRECORDS, "RD");
            }
        } else if (firstRecord == 0) {
            Toast.makeText(MovieListActivity.this, "Bruhhh... There's really nothing to see here", Toast.LENGTH_SHORT).show();
        } else {
            firstRecord -= NUMRECORDS;
            if (firstRecord < 0)
                firstRecord = 0;
            getMovies(MovieListActivity.this, title, 0, "", "", "", "", firstRecord, NUMRECORDS, "RD");
        }
    }

    /**
     *Hide some views to reflect the empty list
     * @param show a boolean to indicate if the app should display empty view
     */
    public void showEmptyView(boolean show) {
        if (show) {
            movieListRecyclerView.setVisibility(View.GONE);
            empty_view.setVisibility(View.VISIBLE);
        } else {
            movieListRecyclerView.setVisibility(View.VISIBLE);
            empty_view.setVisibility(View.GONE);
        }
    }

    /**
     *Go to Single Movie Activity
     *@param v a view that the user clicks on to navigate to Single Movie Activity
     */
    public void gotoSingleMovieActivity(View v) {
        Intent intent = new Intent(MovieListActivity.this, SingleMovieActivity.class);
        Movie m = (Movie) v.getTag(R.id.id1);

        //tags [] = {position, imageView id, titleTextView id}
        String[] tags = ((String) v.getTag(R.id.id2)).split("\\|");

        // make object parcelable
        Log.d("MovieListActivity", "get res id: " + m.getResID());
        intent.putExtra("movie", m);

        View imageview = mMovieAdapter.getCardView(Integer.parseInt(tags[0])).findViewById(Integer.parseInt(tags[1]));
        //  View titleTextView = mMovieAdapter.getCardView(Integer.parseInt(tags[0])).findViewById(Integer.parseInt(tags[2]));
//        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MovieListActivity.this,
//                Pair.create(imageview, getString(R.string.transition_avatar)),
//                Pair.create(titleTextView, getString(R.string.transition_title)));

        ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MovieListActivity.this,
                Pair.create(imageview, getString(R.string.transition_avatar)));
        startActivity(intent, options.toBundle());
    }

    /**
     *Add a movie to the shopping cart
     * @param v a view that the user clicks on to add the movie to the shopping cart
     */
    public void addtoCart(View v) {
        cart.addMovie((Movie) v.getTag(R.id.id1));
    }

    /**
     *Prevent user from accidentally exiting the app by pressing the back button
     * To properly exit the app, the user needs to double tap the back button (2 taps are 200 milliseconds away)
     */
    @Override
    public void onBackPressed() {

        long current = System.currentTimeMillis();
        if (current - lastBack <= 200)
            super.onBackPressed();
        else {
            lastBack = current;
            Toast.makeText(MovieListActivity.this, "Double tap to exit", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     *Get and show all the movies that satisfy the conditions
     * @param c the context required
     * @param title title of the movie
     * @param year year the movie was released in
     * @param director director's name
     * @param star one of the stars 's name
     * @param genresId genre id
     * @param titleStartWith a string that the title starts with
     * @param firstRecord the "from" record number
     * @param numRecords number of records to show
     * @param sort the order to sort the result by
     */
    public void getMovies(final Context c, String title, int year, String director, String star, String genresId, String titleStartWith, int firstRecord, int numRecords, String sort) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        movieList.clear();
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        String yearString = "";
        if (year != 0)
            yearString = year + "";
        String url = "https://" + Utils.IP + ":8443/project4/api/movie?title=" + title + "&year=" + yearString + "&director=" + director + "&star=" + star + "&genreId=" + genresId + "&titleStartWith=" + titleStartWith + "&numRecords=" + numRecords + "&firstRecord=0" + firstRecord + "&sort=" + sort;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("MovieListActivity", response);
                try {
                    JSONArray jArray = new JSONArray(response);
                    int length = jArray.length();
                    for (int i = 0; i < length; i++) {
                        Log.d("MovieListActivity", "Movie " + i);
                        final JSONObject movieObj = jArray.getJSONObject(i);
                        ArrayList<Star> stars = new ArrayList<>();
                        JSONArray starsArray = movieObj.getJSONArray("movie_stars");
                        int numStars = starsArray.length();
                        for (int j = 0; j < numStars; j++) {
                            JSONObject starObj = starsArray.getJSONObject(j);
                            Star s = new Star(starObj.getString("id"), starObj.getString("name"), starObj.getString("birthYear"));
                            stars.add(s);
                        }

                        ArrayList<Genre> genres = new ArrayList<>();
                        JSONArray genresArray = movieObj.getJSONArray("movie_genres");
                        int numGenres = genresArray.length();
                        for (int z = 0; z < numGenres; z++) {
                            JSONObject genreObj = genresArray.getJSONObject(z);
                            Genre g = new Genre(genreObj.getString("id"), genreObj.getString("name"));
                            genres.add(g);
                        }

                        Movie m = new Movie(movieObj.getString("movie_id"), movieObj.getString("movie_title"), Integer.parseInt(movieObj.getString("movie_year")), Float.parseFloat(movieObj.getString("movie_rating")), stars, genres, movieObj.getString("movie_director"));
                        movieList.add(m);
                    }
                    mMovieAdapter.notifyDataSetChanged();
                    progressDialog.cancel();

                    if (movieList.size() > 0)
                        showEmptyView(false);
                    else
                        showEmptyView(true);
                } catch (Exception e) {
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        queue.add(request);
    }
}
