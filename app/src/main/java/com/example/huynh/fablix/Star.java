package com.example.huynh.fablix;

import android.os.Parcel;
import android.os.Parcelable;

public class Star implements Parcelable {
    private String id;
    private String name;
    private String description;
    private String dob;
    private int resId;

    public Star(String id, String name, String dob) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.description ="DOB: " + dob+"\n"
            +"Jennifer Shrader Lawrence (born August 15, 1990) is an American actress. Her films have grossed over $5.7 billion worldwide, and she was the highest-paid actress in the world in 2015 and 2016. Lawrence appeared in Time's 100 most influential people in the world list in 2013 and in the Forbes Celebrity 100 list in 2014 and 2016.\n"
              ;

        this.resId = R.drawable.vindiesel;
    }

    protected Star(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        dob = in.readString();
        resId = in.readInt();
    }

    public static final Creator<Star> CREATOR = new Creator<Star>() {
        @Override
        public Star createFromParcel(Parcel in) {
            return new Star(in);
        }

        @Override
        public Star[] newArray(int size) {
            return new Star[size];
        }
    };

    /**
     *Get resource id of the star
     * @return resource id
     */
    public int getResId() {
        return resId;
    }

    /**
     *Set resource id for the star
     * @param resId resource id
     */
    public void setResId(int resId) {
        this.resId = resId;
    }
    /**
     *Get the star's id
     * @return the star's id
     */
    public String getId() {
        return id;
    }

    /**
     *Set id for the star
     * @return id id for the star
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *Get the star's name
     * @return star's name
     */
    public String getName() {
        return name;
    }

    /**
     *Set the name of the star
     * @return star's name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(dob);
        dest.writeInt(resId);
    }
}
