package hu.bendaf.udacity.popularmovies.popularmoviesapp.data;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by bendaf on 2017. 02. 07. PopularMoviesApp.
 */

@ContentProvider(authority = MovieProvider.AUTHORITY, database = MovieDatabase.class)
public class MovieProvider {
    public static final String AUTHORITY = "hu.bendaf.udacity.popularmovies.MovieProvider";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    @TableEndpoint(table = MovieDatabase.MOVIE)
    public static class Movies {
        @ContentUri(
                path = "movie",
                type = "vnd.android.cursor.dir/movie",
                defaultSort = MovieColumns.id + " DESC")
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath("movie").build();

        @InexactContentUri(
                path = "movie/#",
                name = "MOVIE_ID",
                type = "vnd.android.cursor.item/movie",
                whereColumn = MovieColumns.id,
                pathSegment = 1)
        public static Uri withId(int id) {
            return BASE_CONTENT_URI.buildUpon().appendPath("movie").appendPath(String.valueOf(id)).build();
        }
    }
}
