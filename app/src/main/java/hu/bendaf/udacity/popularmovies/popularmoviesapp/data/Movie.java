package hu.bendaf.udacity.popularmovies.popularmoviesapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bendaf on 2017. 02. 03. PopularMoviesApp.
 * This class is for storing movie data.
 */
public class Movie implements Parcelable{
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
    @SerializedName("id")
    private Integer id;
    private List<Trailer> trailers;
    private List<Review> reviews;

    public Movie() {
    }

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

    public Integer getId() {
        return id;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    Movie(Parcel in) {
        posterPath = in.readString();
        title = in.readString();
        voteAverage = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        id = in.readByte() == 0x00 ? null : in.readInt();
        if (in.readByte() == 0x01) {
            trailers = new ArrayList<>();
            in.readList(trailers, Trailer.class.getClassLoader());
        } else {
            trailers = null;
        }
        if (in.readByte() == 0x01) {
            reviews = new ArrayList<>();
            in.readList(reviews, Review.class.getClassLoader());
        } else {
            reviews = null;
        }
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
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        if (trailers == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(trailers);
        }
        if (reviews == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(reviews);
        }
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