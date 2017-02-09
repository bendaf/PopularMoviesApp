package hu.bendaf.udacity.popularmovies.popularmoviesapp.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import hu.bendaf.udacity.popularmovies.popularmoviesapp.R;
import hu.bendaf.udacity.popularmovies.popularmoviesapp.activities.MainAct;
import hu.bendaf.udacity.popularmovies.popularmoviesapp.activities.MovieDetailsAct;
import hu.bendaf.udacity.popularmovies.popularmoviesapp.data.Movie;
import hu.bendaf.udacity.popularmovies.popularmoviesapp.data.MovieContract;
import hu.bendaf.udacity.popularmovies.popularmoviesapp.data.MovieProvider;
import hu.bendaf.udacity.popularmovies.popularmoviesapp.utils.MoviesApi;
import hu.bendaf.udacity.popularmovies.popularmoviesapp.utils.ResponseList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static hu.bendaf.udacity.popularmovies.popularmoviesapp.utils.MoviesApi.MOVIES_BASE_URL;

/**
 * Created by bendaf on 2017. 02. 03. PopularMoviesApp.
 * This fragment is displaying the list of the movies.
 */

public class MovieListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String ARG_MOVIE_TYPE = "movie_type";
    private static final String TAG = MovieListFragment.class.getSimpleName();
    private static final int LOADER_MOVIE = 13;
    private static final String[] MOVIE_PROJECTION = new String[]{
            MovieContract.id, MovieContract.title, MovieContract.posterPath,
            MovieContract.overview, MovieContract.releaseDate, MovieContract.voteAverage
    };
    public static final int INDEX_ID = 0;
    public static final int INDEX_TITLE = 1;
    public static final int INDEX_POSTER_PATH = 2;
    public static final int INDEX_OVERVIEW = 3;
    public static final int INDEX_RELEASE_DATE = 4;
    public static final int INDEX_VOTE_EVERAGE = 5;

    private List<Movie> mMovies = new ArrayList<>();
    private MoviesAdapter mAdapter;
    private String mType;

    public static MovieListFragment newInstance(String movieType) {
        MovieListFragment fragment = new MovieListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MOVIE_TYPE, movieType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mType = getArguments().getString(ARG_MOVIE_TYPE);
        if(mType == null) return rootView;
        if(mType.equals(MainAct.PATH_FAVORITES)) {
            getLoaderManager().initLoader(LOADER_MOVIE, null, MovieListFragment.this);
        } else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MOVIES_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MoviesApi service = retrofit.create(MoviesApi.class);
            Call<ResponseList<Movie>> mlc = service.getMovies(mType, getString(R.string.THE_MOVIE_DB_API_TOKEN));
            mlc.enqueue(new Callback<ResponseList<Movie>>() {
                @Override
                public void onResponse(Call<ResponseList<Movie>> call, Response<ResponseList<Movie>> response) {
                    mMovies = response.body().getResponses();
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<ResponseList<Movie>> call, Throwable t) {
                    Log.d(TAG, "doInBackground: fail ");
                    t.printStackTrace();
                }
            });
        }
        RecyclerView moviesRecycler = (RecyclerView) rootView.findViewById(R.id.rv_movie_images);
        moviesRecycler.setHasFixedSize(true);
        mAdapter = new MoviesAdapter();
        moviesRecycler.setAdapter(mAdapter);
        moviesRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        return rootView;
    }

    @Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), MovieProvider.Movies.CONTENT_URI, MOVIE_PROJECTION, null, null, null);
    }

    @Override public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(TAG, "onLoadFinished: " + cursor);
        if(mAdapter == null || cursor == null) return;
        mMovies = new ArrayList<>(cursor.getCount());
        while(cursor.moveToNext()) {
            mMovies.add(new Movie(cursor.getInt(INDEX_ID), cursor.getString(INDEX_TITLE),
                    cursor.getString(INDEX_POSTER_PATH), cursor.getString(INDEX_OVERVIEW),
                    cursor.getString(INDEX_RELEASE_DATE), cursor.getString(INDEX_VOTE_EVERAGE)));
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override public void onLoaderReset(Loader<Cursor> loader) {
        mMovies = new ArrayList<>();
    }

    class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {


        @Override
        public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.recycler_item_movies, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MoviesAdapter.ViewHolder holder, int position) {
            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w342" +
                    mMovies.get(position).getPosterPath()).into(holder.ivPic);
            holder.ivPic.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    Intent openDetails = new Intent(getContext(), MovieDetailsAct.class);
                    openDetails.putExtra(MovieDetailsAct.EXTRA_MOVIE, mMovies.get(holder.getLayoutPosition()));
                    if(holder.ivPic.getDrawable() != null) {
                        Bitmap bitmap = ((BitmapDrawable) holder.ivPic.getDrawable()).getBitmap();
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] b = byteArrayOutputStream.toByteArray();
                        openDetails.putExtra(MovieDetailsAct.EXTRA_PICTURE, b);
                    }
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(getActivity(), holder.ivPic, "picture");
                    startActivity(openDetails, options.toBundle());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mMovies.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            final ImageView ivPic;

            ViewHolder(View itemView) {
                super(itemView);
                ivPic = (ImageView) itemView.findViewById(R.id.iv_pic);
            }
        }
    }
}
