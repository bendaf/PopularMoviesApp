package hu.bendaf.udacity.popularmovies.popularmoviesapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bendaf on 2017. 02. 03. PopularMoviesApp.
 * This fragment is displaying the list of the movies.
 */

public class MovieListFragment extends Fragment {

    private static final String MOVIES_BASE_URL = "http://api.themoviedb.org/";
    private static final String ARG_MOVIE_TYPE = "movie_type";
    private static final String TAG = "MovieListFragment";
    private MoviesApi service;
    private List<Movie> movies = new ArrayList<>();
    private RecyclerView mRecycler;
    private MoviesAdapter mAdapter;

    public MovieListFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     *
     * @param movieType Type of the movie list.
     */
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MOVIES_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(MoviesApi.class);
        Call<MovieList> mlc = service.getMovies(getArguments().getString(ARG_MOVIE_TYPE), getString(R.string.THE_MOVIE_DB_API_TOKEN));
        mlc.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {

                movies = response.body().movieList;
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {

                Log.d(TAG, "doInBackground: fail ");
                t.printStackTrace();
            }
        });

        mRecycler = (RecyclerView) rootView.findViewById(R.id.rl_movie_images);
        mRecycler.setHasFixedSize(true);
        mAdapter = new MoviesAdapter();
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        return rootView;
    }

    class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

        @Override
        public MoviesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.movies_recycler_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MoviesAdapter.ViewHolder holder, int position) {
            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w342" +
                    movies.get(position).getPosterPath()).into(holder.ivPic);
            holder.ivPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bitmap bitmap = ((BitmapDrawable) holder.ivPic.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                    byte[] b = baos.toByteArray();

                    Intent openDetails = new Intent(getContext(), MovieDetailsAct.class);
                    openDetails.putExtra(MovieDetailsAct.EXTRA_MOVIE, movies.get(holder.getLayoutPosition()));
                    openDetails.putExtra(MovieDetailsAct.EXTRA_PICTURE, b);
                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation(getActivity(), holder.ivPic, "picture");
                    startActivity(openDetails, options.toBundle());
                }
            });
        }

        @Override
        public int getItemCount() {
            return movies.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            ImageView ivPic;

            ViewHolder(View itemView) {
                super(itemView);
                ivPic = (ImageView) itemView.findViewById(R.id.iv_pic);
            }
        }
    }
}
