package hu.bendaf.udacity.popularmovies.popularmoviesapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by bendaf on 2017. 02. 03. PopularMoviesApp.
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
     * @param movieType
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
        public void onBindViewHolder(MoviesAdapter.ViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder: " + "http://image.tmdb.org/t/p/w185" + movies.get(position).getPosterPath());
            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185" +
                    movies.get(position).getPosterPath()).into(holder.ivPic);
//            holder.ivPic.setImageResource(R.mipmap.ic_launcher);
//            Picasso.with(getContext()).load("http://i.imgur.com/DvpvklR.png").into(holder.ivPic);
        }

        @Override
        public int getItemCount() {
//            Log.d(TAG, "getItemCount: " + movies.size());
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
