package hu.bendaf.udacity.popularmovies.popularmoviesapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by bendaf on 2017. 02. 07. PopularMoviesApp.
 * This class is for storing trailer data.
 */

public class Trailer implements Parcelable {
    @SerializedName("name")
    private String name;
    @SerializedName("site")
    private String site;
    @SerializedName("type")
    private String type;
    @SerializedName("key")
    private String key;

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public String getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    Trailer(Parcel in) {
        name = in.readString();
        site = in.readString();
        type = in.readString();
        key = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(site);
        dest.writeString(type);
        dest.writeString(key);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
