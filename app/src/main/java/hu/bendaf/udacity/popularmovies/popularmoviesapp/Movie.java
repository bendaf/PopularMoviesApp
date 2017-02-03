package hu.bendaf.udacity.popularmovies.popularmoviesapp;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bendaf on 2017. 02. 03. PopularMoviesApp.
 * This class is for storing movie datas.
 */
class Movie implements Parcelable {
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

    public Movie() {
    }

    String getPosterPath() {
        return posterPath;
    }

    public String getTitle() {
        return title;
    }

    String getVoteAverage() {
        return voteAverage;
    }

    String getOverview() {
        return overview;
    }

    String getReleaseDate() {
        return releaseDate;
    }

    private Movie(Parcel in) {
        posterPath = in.readString();
        title = in.readString();
        voteAverage = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(posterPath);
        dest.writeString(title);
        dest.writeString(voteAverage);
        dest.writeString(overview);
        dest.writeString(releaseDate);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
