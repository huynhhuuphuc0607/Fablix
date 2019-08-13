package com.example.huynh.fablix;

import android.os.Parcel;
import android.os.Parcelable;

public class Genre implements Parcelable {
    private String id;
    private String name;

    public Genre(String id, String name) {
        this.id = id;
        this.name = name;
    }

    protected Genre(Parcel in) {
        id = in.readString();
        name = in.readString();
    }

    public static final Creator<Genre> CREATOR = new Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel in) {
            return new Genre(in);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };

    /**
     *get gerne ID
     *@return genre ID
     */
    public String getId() {
        return id;
    }

    /**
     *set genre ID
     *@param id string to represent id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *Get the name of genre
     * @return genre name
     */
    public String getName() {
        return name;
    }

    /**
     *Set genre name
     *@param name string to represent the name of genre
     */
    public void setName(String name) {
        this.name = name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
    }
}
