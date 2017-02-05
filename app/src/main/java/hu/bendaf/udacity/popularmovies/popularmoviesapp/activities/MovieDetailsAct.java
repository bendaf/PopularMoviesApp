package hu.bendaf.udacity.popularmovies.popularmoviesapp.activities;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.bendaf.udacity.popularmovies.popularmoviesapp.R;
import hu.bendaf.udacity.popularmovies.popularmoviesapp.utils.Movie;

public class MovieDetailsAct extends AppCompatActivity {
    private static final String TAG = "MovieDerailsAct";

    public static final String EXTRA_MOVIE = "MOVIE";
    public static final String EXTRA_PICTURE = "PICTURE";

    @BindView(R.id.iv_pic) private ImageView mPic;
    @BindView(R.id.tv_title) private TextView mTitle;
    @BindView(R.id.tv_rating) private TextView mRating;
    @BindView(R.id.tv_year) private TextView mYear;
    @BindView(R.id.tv_overview) private TextView mOverview;
    private Movie mMovie;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        if(getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        if(getIntent().hasExtra(EXTRA_MOVIE)) {
            mMovie = getIntent().getExtras().getParcelable(EXTRA_MOVIE);
            if(getIntent().hasExtra(EXTRA_PICTURE)) {
                byte[] b = getIntent().getExtras().getByteArray(EXTRA_PICTURE);
                mPic.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b != null ? b.length : 0));
            } else {
                Picasso.with(this).load("http://image.tmdb.org/t/p/w342" +
                        mMovie.getPosterPath()).into(mPic);
            }
            mTitle.setText(mMovie.getTitle());
            mRating.setText(mMovie.getVoteAverage());
            mYear.setText(mMovie.getReleaseDate());
            mOverview.setText(mMovie.getOverview());
        }
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
