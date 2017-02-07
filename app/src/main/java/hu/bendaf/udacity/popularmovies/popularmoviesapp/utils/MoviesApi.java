package hu.bendaf.udacity.popularmovies.popularmoviesapp.utils;

import hu.bendaf.udacity.popularmovies.popularmoviesapp.data.Movie;
import hu.bendaf.udacity.popularmovies.popularmoviesapp.data.Review;
import hu.bendaf.udacity.popularmovies.popularmoviesapp.data.Trailer;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by bendaf on 2017. 02. 03. PopularMoviesApp.
 * This interface is for Retrofit to download movie data.
 */

public interface MoviesApi {
    String MOVIES_BASE_URL = "http://api.themoviedb.org/3/";

    @GET("movie/{movie}")
    Call<ResponseList<Movie>> getMovies(@Path("movie") String typeOfMovies, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ResponseList<Review>> getReviews(@Path("id") Integer movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<ResponseList<Trailer>> getTrailers(@Path("id") Integer movieId, @Query("api_key") String apiKey);
}
