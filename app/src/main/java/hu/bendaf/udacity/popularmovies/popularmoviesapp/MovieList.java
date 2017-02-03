package hu.bendaf.udacity.popularmovies.popularmoviesapp;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bendaf on 2017. 02. 03. PopularMoviesApp.
 * This class if for storing a page data.
 */

class MovieList {
    @SerializedName("results")
    List<Movie> movieList;
}
