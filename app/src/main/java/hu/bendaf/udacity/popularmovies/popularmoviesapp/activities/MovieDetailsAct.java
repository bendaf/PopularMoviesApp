package hu.bendaf.udacity.popularmovies.popularmoviesapp.activities;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hu.bendaf.udacity.popularmovies.popularmoviesapp.R;
import hu.bendaf.udacity.popularmovies.popularmoviesapp.data.Movie;
import hu.bendaf.udacity.popularmovies.popularmoviesapp.data.MovieColumns;
import hu.bendaf.udacity.popularmovies.popularmoviesapp.data.MovieProvider;
import hu.bendaf.udacity.popularmovies.popularmoviesapp.data.Review;
import hu.bendaf.udacity.popularmovies.popularmoviesapp.data.Trailer;
import hu.bendaf.udacity.popularmovies.popularmoviesapp.utils.MoviesApi;
import hu.bendaf.udacity.popularmovies.popularmoviesapp.utils.ResponseList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@SuppressWarnings("unchecked") public class MovieDetailsAct extends AppCompatActivity {
    private static final String TAG = "MovieDerailsAct";

    public static final String EXTRA_MOVIE = "MOVIE";
    public static final String EXTRA_PICTURE = "PICTURE";

    @BindView(R.id.iv_pic) ImageView mPic;
    @BindView(R.id.tv_rating) TextView mRating;
    @BindView(R.id.tv_year) TextView mYear;
    @BindView(R.id.tv_overview) TextView mOverview;
    @BindView(R.id.rv_reviews) RecyclerView mReviews;
    @BindView(R.id.rv_trailers) RecyclerView mTrailers;
    private Movie mMovie;
    private RecyclerView.Adapter mTrailerAdapter = new TrailerAdapter();
    private RecyclerView.Adapter mReviewAdapter = new ReviewAdapter();
    private boolean isFavorite = false;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        if(getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        if(!getIntent().hasExtra(EXTRA_MOVIE)) return;

        mMovie = getIntent().getExtras().getParcelable(EXTRA_MOVIE);
        if(getIntent().hasExtra(EXTRA_PICTURE)) {
            byte[] b = getIntent().getExtras().getByteArray(EXTRA_PICTURE);
            mPic.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b != null ? b.length : 0));
        } else {
            Picasso.with(this).load("http://image.tmdb.org/t/p/w342" +
                    mMovie.getPosterPath()).into(mPic);
        }
        getSupportActionBar().setTitle(mMovie.getTitle());
        mRating.setText(mMovie.getVoteAverage());
        mYear.setText(mMovie.getReleaseDate());
        mOverview.setText(mMovie.getOverview());
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MoviesApi.MOVIES_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MoviesApi service = retrofit.create(MoviesApi.class);
        Call<ResponseList<Review>> getReviews = service.getReviews(mMovie.getId(), getString(R.string.THE_MOVIE_DB_API_TOKEN));
        getReviews.enqueue((Callback<ResponseList<Review>>) mResponseCallback);
        Call<ResponseList<Trailer>> getTrailers = service.getTrailers(mMovie.getId(), getString(R.string.THE_MOVIE_DB_API_TOKEN));
        getTrailers.enqueue((Callback<ResponseList<Trailer>>) mResponseCallback);

        mTrailers.setLayoutManager(new LinearLayoutManager(getBaseContext(), LinearLayoutManager.HORIZONTAL, false));
        mTrailers.setAdapter(mTrailerAdapter);
        mReviews.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mReviews.setAdapter(mReviewAdapter);

        LoaderManager.LoaderCallbacks<Cursor> callback = new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                return new CursorLoader(getBaseContext(), MovieProvider.Movies.withId(mMovie.getId()), null, null, null, null);
            }

            @Override public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                if(cursor == null) return;
                if(cursor.moveToNext()) {
                    isFavorite = true;
                    invalidateOptionsMenu();
                }
            }

            @Override public void onLoaderReset(Loader<Cursor> loader) {
            }
        };

        getLoaderManager().initLoader(1, null, callback);
    }

    private Callback<?> mResponseCallback = new Callback<ResponseList<?>>() {
        @Override
        public void onResponse(Call<ResponseList<?>> call, Response<ResponseList<?>> response) {

            List<?> responseList = response.body().getResponses();
            if(responseList != null && responseList.size() > 0) {
                if(responseList.get(0) instanceof Review) {
                    mMovie.setReviews((List<Review>) responseList);
                    mReviewAdapter.notifyDataSetChanged();
                } else if(responseList.get(0) instanceof Trailer) {
                    mMovie.setTrailers(filterYoutubeVideos((List<Trailer>) responseList));
                    mTrailerAdapter.notifyDataSetChanged();
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseList<?>> call, Throwable t) {
            Log.w(TAG, "doInBackground: fail ");
            t.printStackTrace();
        }
    };

    private List<Trailer> filterYoutubeVideos(List<Trailer> videoList) {
        if(videoList == null) return null;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            videoList.removeIf(r -> !(r.getSite().toLowerCase().equals("youtube")
                    && r.getType().toLowerCase().equals("trailer")));
            return videoList;
        }
        List<Trailer> retList = new ArrayList<>();
        //noinspection Convert2streamapi
        for(Trailer t : videoList) {
            if(t.getType().toLowerCase().equals("trailer") && t.getSite().toLowerCase().equals("youtube")) {
                retList.add(t);
            }
        }
        return retList;
    }

    @Override public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        if(isFavorite) {
            setMenuItemIcon(menu.findItem(R.id.action_star), R.drawable.ic_star_black_24dp);
        } else {
            setMenuItemIcon(menu.findItem(R.id.action_star), R.drawable.ic_star_border_black_24dp);
        }
        return true;
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_star:
                if(isFavorite) {
                    getBaseContext().getContentResolver().delete(MovieProvider.Movies.withId(mMovie.getId()), null, null);
                } else {
                    ContentValues cv = new ContentValues();
                    cv.put(MovieColumns.id, mMovie.getId());
                    cv.put(MovieColumns.title, mMovie.getTitle());
                    getBaseContext().getContentResolver().insert(MovieProvider.Movies.CONTENT_URI, cv);
                }
                isFavorite = !isFavorite;
                invalidateOptionsMenu();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setMenuItemIcon(MenuItem item, int iconId) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            item.setIcon(getDrawable(iconId));
        } else {
            //noinspection deprecation
            item.setIcon(getResources().getDrawable(iconId));
        }
    }

    class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

        @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getBaseContext()).inflate(R.layout.recycler_item_trailer, parent, false);
            return new ViewHolder(v);
        }

        @Override public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIbPlay.setImageResource(R.drawable.ic_play_circle);
            holder.mIbPlay.setOnClickListener(view ->
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" +
                            mMovie.getTrailers().get(holder.getLayoutPosition()).getKey()))));
        }

        @Override public int getItemCount() {
            return mMovie.getTrailers() != null ? mMovie.getTrailers().size() : 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.ib_play) ImageButton mIbPlay;

            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

        @Override public ReviewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getBaseContext()).inflate(R.layout.recycler_item_review, parent, false);
            return new ViewHolder(v);
        }

        @Override public void onBindViewHolder(ReviewAdapter.ViewHolder holder, int position) {
            holder.mTvAuthor.setText(mMovie.getReviews().get(position).getAuthor());
            holder.mTvText.setText(mMovie.getReviews().get(position).getContent());
            holder.mCardView.setOnClickListener(view -> startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(mMovie.getReviews().get(holder.getLayoutPosition()).getUrl()))));
        }

        @Override public int getItemCount() {
            return mMovie.getReviews() != null ? mMovie.getReviews().size() : 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_author) TextView mTvAuthor;
            @BindView(R.id.tv_text) TextView mTvText;
            @BindView(R.id.cd_review) View mCardView;

            ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
