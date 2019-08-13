package com.example.huynh.fablix;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SingleStarActivity extends AppCompatActivity {
    private Intent mIntent;
    private Star star;
    private CircleImageView starAvatarCircleImageView;
    private TextView starNameTextView;
    private TextView starDescriptionTextView;
    private ArrayList<Movie> movieList;

    private MovieSuggestionAdapter suggestionAdapter;
    private RecyclerView suggestionRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_star);
        mIntent = getIntent();
        star = mIntent.getParcelableExtra("star");

        starAvatarCircleImageView = findViewById(R.id.starAvatarCircleImageView);
        starNameTextView = findViewById(R.id.starNameTextView);
        starDescriptionTextView = findViewById(R.id.starDescriptionTextView);
        suggestionRecyclerView = findViewById(R.id.suggestionRecyclerView);

        starAvatarCircleImageView.setImageResource(star.getResId());
        starNameTextView.setText(star.getName());
        starDescriptionTextView.setText(star.getDescription());

        movieList = new ArrayList<>();
        suggestionAdapter = new MovieSuggestionAdapter(SingleStarActivity.this, R.layout.movie_suggestion_one_row, movieList);
        suggestionRecyclerView.setLayoutManager(new LinearLayoutManager(SingleStarActivity.this));
        //suggestionRecyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(SingleStarActivity.this,R.anim.fade_in_slide_from_right_manager));
        suggestionRecyclerView.setAdapter(suggestionAdapter);
        getStarInfo(SingleStarActivity.this, star.getId());

    }

    /**
     *Navigate to Single Movie Activity
     * @param v the view the user clicks on to navigate to Single Movie Activity
     */
    public void gotoSingleMovieActivity(final View v) {
        Movie m = (Movie) v.getTag(R.id.id1);
        getMovies(v,SingleStarActivity.this,"",0,"","","",m.getmTitle().replace(" ","+"),0,1,"RD");
    }

    /**
     *Get and show all the movies that satisfy the conditions
     * @param v the view the user clicks to get movies
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
    public void getMovies(final View v, final Context c, String title, int year, String director, String star, String genresId, String titleStartWith, int firstRecord, int numRecords, String sort) {
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        String yearString = "";
        if (year != 0)
            yearString = year + "";
        String url = "https://" + Utils.IP + ":8443/project4/api/movie?title=" + title + "&year=" + yearString + "&director=" + director + "&star=" + star + "&genreId=" + genresId + "&titleStartWith=" + titleStartWith + "&numRecords=" + numRecords + "&firstRecord=0" + firstRecord + "&sort=" + sort;
        Log.d("SingleStarActivity",url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("SingleStarActivity", response);
                try {
                    JSONArray jArray = new JSONArray(response);
                    final JSONObject movieObj = jArray.getJSONObject(0);
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
                    Intent intent  = new Intent(SingleStarActivity.this, SingleMovieActivity.class);
                    intent.putExtra("movie",m);
                    String[] tags = ((String) v.getTag(R.id.id2)).split("\\|");
                    Movie movieToGetImage = (Movie)v.getTag(R.id.id1);
                    m.setResID(movieToGetImage.getResID());
                    View imageview = suggestionAdapter.getLinearLayout(Integer.parseInt(tags[0])).findViewById(Integer.parseInt(tags[1]));
                    Log.d("SingleStarActivity","Name: " + m.getmTitle());
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SingleStarActivity.this,
                            Pair.create(imageview, getString(R.string.transition_avatar)));
                    startActivity(intent, options.toBundle());

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

    /**
     *Get movies from a star's info and add those movies to the list
     * @param c the context required
     * @param star_id a string to represent star id
     */
    public void getStarInfo(Context c, String star_id) {
        Log.d("SingleStarAcitvity", "Im here. Movie list size = " + movieList.size());
        RequestQueue queue = NetworkManager.sharedManager(this).queue;
        String url = "https://" + Utils.IP + ":8443/project4/api/single-star?id=" + star_id;
        Log.d("SingleStarAcitvity", "URL: " + url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    Log.d("SingleStarAcitvity", s);
                    JSONArray movieArray = new JSONObject(s).getJSONArray("movies");
                    int length = movieArray.length();
                    for (int i = 0; i < length; i++) {
                        Log.d("SingleStarAcitvity", "Suggestion index: " + i);
                        JSONObject movieObject = movieArray.getJSONObject(i);
                        Movie m = new Movie(movieObject.getString("movie_id"), movieObject.getString("movie_title"),
                                Integer.parseInt(movieObject.getString("movie_year")), movieObject.getString("movie_director"));
                        movieList.add(m);
                    }
                    suggestionAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Log.d("SingleStarAcitvity", e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("SingleStarAcitvity", volleyError.toString());
            }
        });

        queue.add(request);
    }
}

