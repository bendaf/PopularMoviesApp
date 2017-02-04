package hu.bendaf.udacity.popularmovies.popularmoviesapp.utils;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bendaf on 2017. 02. 03. PopularMoviesApp.
 * This class if for storing a page data.
 */

public class MovieList {

    @SerializedName("results")
    private List<Movie> movieList;

    public List<Movie> getMovieList() {
        return movieList;
    }
}
