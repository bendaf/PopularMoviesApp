package hu.bendaf.udacity.popularmovies.popularmoviesapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by bendaf on 2017. 02. 03. PopularMoviesApp.
 * This interface is for Retrofit to download movie data.
 */

interface MoviesApi {
    @GET("/3/movie/{movie}")
    Call<MovieList> getMovies(@Path("movie") String typeMovie, @Query("api_key") String keyApi);
}
