package hu.bendaf.udacity.popularmovies.popularmoviesapp.data;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.IfNotExists;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by bendaf on 2017. 02. 07. PopularMoviesApp.
 */

@Database(version = MovieDatabase.VERSION)
public final class MovieDatabase {
    public static final int VERSION = 2;

    @Table(MovieColumns.class) @IfNotExists
    public static final String MOVIE = "movie";
}
