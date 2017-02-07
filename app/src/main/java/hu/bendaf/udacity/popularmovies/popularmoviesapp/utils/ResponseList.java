package hu.bendaf.udacity.popularmovies.popularmoviesapp.utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bendaf on 2017. 02. 07. PopularMoviesApp.
 * This class helps unpack the responses from TheMoviesDb API
 */

public class ResponseList<T> {

    @SerializedName("results")
    private List<T> responses;

    public List<T> getResponses() {
        return responses;
    }
}
