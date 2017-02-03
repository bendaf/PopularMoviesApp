package hu.bendaf.udacity.popularmovies.popularmoviesapp;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bendaf on 2017. 02. 03. PopularMoviesApp.
 * This class is for storing movie datas.
 */
class Movie {
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("original_title")
    private String title;
    @SerializedName("vote_average")
    private String voteAverage;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;

    public String getPosterPath() {
        return posterPath;
    }

    public String getTitle() {
        return title;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
