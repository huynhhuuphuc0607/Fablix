package com.example.huynh.fablix;

import android.util.Log;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map;

public class Cart {
    private Map<Movie, Integer> map;

    public Cart() {
        map = new Hashtable<>();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     *Get size of cart
     * @return size of cart
     */
    public int size() {
        return map.size();
    }

    /**
    *Add movie to cart
    *@param m movie object
    */
    public void addMovie(Movie m) {
        try {
            int qty = map.get(m);
            map.put(m, qty + 1);
            Log.d("Cart", m.getmTitle() + " qty: " + map.get(m));
        } catch (NullPointerException e) {
            Log.d("Cart", m.getmTitle() + " does not exist in cart");
            map.put(m, 1);
        }
    }

    /**
     *Clear cart
     */
    public void clear()
    {
        map.clear();
    }

    /**
     *remove movie from cart
     *@param m movie object
     */
    public void remove(Movie m)
    {
        map.remove(m);
    }

    /**
     *Get a map of  movie and its ID
     * @return map of movie and its ID
     */
    public Map<Movie, Integer> getMap() {
        return map;
    }
}
