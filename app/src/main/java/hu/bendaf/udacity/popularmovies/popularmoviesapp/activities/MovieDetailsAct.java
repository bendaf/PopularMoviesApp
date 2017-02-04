package hu.bendaf.udacity.popularmovies.popularmoviesapp.activities;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import hu.bendaf.udacity.popularmovies.popularmoviesapp.R;
import hu.bendaf.udacity.popularmovies.popularmoviesapp.utils.Movie;

;

public class MovieDetailsAct extends AppCompatActivity {

    public static final String EXTRA_MOVIE = "MOVIE";
    private static final String TAG = "MovieDerailsAct";
    public static final String EXTRA_PICTURE = "PICTURE";

    ImageView mPic;
    TextView mTitle;
    TextView mRating;
    TextView mYear;
    TextView mOverview;
    Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mPic = (ImageView) findViewById(R.id.iv_pic);
        mTitle = (TextView) findViewById(R.id.tv_title);
        mRating = (TextView) findViewById(R.id.tv_rating);
        mYear = (TextView) findViewById(R.id.tv_year);
        mOverview = (TextView) findViewById(R.id.tv_overview);

        if(getIntent().hasExtra(EXTRA_MOVIE)) {
            mMovie = getIntent().getExtras().getParcelable(EXTRA_MOVIE);
            byte[] b = getIntent().getExtras().getByteArray(EXTRA_PICTURE);
            mPic.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b != null ? b.length : 0));
            mTitle.setText(mMovie.getTitle());
            mRating.setText(mMovie.getVoteAverage());
            mYear.setText(mMovie.getReleaseDate());
            mOverview.setText(mMovie.getOverview());
        }
    }

}
