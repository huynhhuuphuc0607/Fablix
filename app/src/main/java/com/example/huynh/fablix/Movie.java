package com.example.huynh.fablix;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Movie implements Parcelable {
    private String movieId;
    private String mTitle;
    private ArrayList<Star> mStars;
    private ArrayList<Genre> mGenres;
    private String director;
    private float price;
    private int year;
    private float rating;

    private int resID;

    protected Movie(Parcel in) {
        movieId = in.readString();
        mTitle = in.readString();
        mStars = in.createTypedArrayList(Star.CREATOR);
        mGenres = in.createTypedArrayList(Genre.CREATOR);
        director = in.readString();
        price = in.readFloat();
        resID = in.readInt();
        year = in.readInt();
        rating = in.readFloat();
    }

    public Movie(String movieId, String mTitle, int year, float rating, ArrayList<Star> mStars, ArrayList<Genre> mGenres, String director) {
        this.movieId = movieId;
        this.mTitle = mTitle;
        this.mStars = mStars;
        this.mGenres = mGenres;
        this.director = director;
        this.year = year;
        this.rating = rating;
        price = 5;
    }


    public Movie(String movieId, String mTitle, int year, String director) {
        this.movieId = movieId;
        this.mTitle = mTitle;
        this.mStars = new ArrayList<>();
        this.mGenres = new ArrayList<>();
        this.director = director;
        this.year = year;
        this.rating = 0;
        price = 5;
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    /**
     *Get movie title
     *@return a string to represent movie title
     */
    public String getmTitle() {
        return mTitle;
    }

    /**
     *Get all the stars in the movie
     *@return an arraylist of stars in the movie
     */
    public ArrayList<Star> getmStars() {
        return mStars;
    }

    /**
     *Get all the genres related to the movie
     *@return an arraylist of genres of the movie
     */
    public ArrayList<Genre> getmGenres() {
        return mGenres;
    }

    /**
     *Get the name of the director
     *@return a string to represent the name of the director
     */
    public String getDirector() {
        return director;
    }

    /**
     *Get resource ID for the thumbnail of the movie
     *@return an int to represent the resource ID
     */
    public int getResID() {
        return resID;
    }

    /**
     *Set new resource ID for the thumbnail of the movie
     *@param resID resource id of the thumbnail
     */
    public void setResID(int resID) {
        this.resID = resID;
    }

    /**
     *Get the price of the movie
     *@return a float to represent the price of the movie
     */
    public float getPrice() {
        return price;
    }

    /**
     *Get the year the movie was released
     *@return an int to represent the year of the movie
     */
    public int getYear() {
        return year;
    }

    /**
     *Get the rating of the movie
     *@return a float to represent the rating of the movie
     */
    public float getRating() {
        return rating;
    }

    /**
     *Compare to check if the two movie objects are the same
     *@return a boolean to represent the result - True if they are the same, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie m = (Movie) o;

        return (movieId.equals(m.movieId));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieId);
        dest.writeString(mTitle);
        dest.writeTypedList(mStars);
        dest.writeTypedList(mGenres);
        dest.writeString(director);
        dest.writeFloat(price);
        dest.writeInt(resID);
        dest.writeInt(year);
        dest.writeFloat(rating);
    }

    @Override
    public int hashCode() {
        int result = movieId.hashCode();
        return result;
    }
}
