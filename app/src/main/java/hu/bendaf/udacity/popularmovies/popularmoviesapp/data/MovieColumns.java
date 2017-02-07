package hu.bendaf.udacity.popularmovies.popularmoviesapp.data;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

/**
 * Created by bendaf on 2017. 02. 07. PopularMoviesApp.
 */

public interface MovieColumns {
    @DataType(INTEGER) @PrimaryKey String id = "id";
    @DataType(TEXT) @NotNull String title = "title";
    @DataType(TEXT) String posterPath = "posterPath";
    @DataType(TEXT) String voteAverage = "voteAverage";
    @DataType(TEXT) String overview = "overview";
    @DataType(TEXT) String releaseDate = "releaseDate";
}
