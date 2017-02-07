package hu.bendaf.udacity.popularmovies.popularmoviesapp.fragments;

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

import hu.bendaf.udacity.popularmovies.popularmoviesapp.R;
import hu.bendaf.udacity.popularmovies.popularmoviesapp.activities.MovieDetailsAct;
import hu.bendaf.udacity.popularmovies.popularmoviesapp.data.Movie;
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

public class MovieListFragment extends Fragment {

    private static final String ARG_MOVIE_TYPE = "movie_type";
    private static final String TAG = "MovieListFragment";
    private List<Movie> movies = new ArrayList<>();
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
        MoviesApi service = retrofit.create(MoviesApi.class);
        Call<ResponseList<Movie>> mlc = service.getMovies(getArguments().getString(ARG_MOVIE_TYPE), getString(R.string.THE_MOVIE_DB_API_TOKEN));
        mlc.enqueue(new Callback<ResponseList<Movie>>() {
            @Override
            public void onResponse(Call<ResponseList<Movie>> call, Response<ResponseList<Movie>> response) {
                movies = response.body().getResponses();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ResponseList<Movie>> call, Throwable t) {
                Log.d(TAG, "doInBackground: fail ");
                t.printStackTrace();
            }
        });

        RecyclerView moviesRecycler = (RecyclerView) rootView.findViewById(R.id.rv_movie_images);
        moviesRecycler.setHasFixedSize(true);
        mAdapter = new MoviesAdapter();
        moviesRecycler.setAdapter(mAdapter);
        moviesRecycler.setLayoutManager(new GridLayoutManager(getContext(), 2));
        return rootView;
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
                    movies.get(position).getPosterPath()).into(holder.ivPic);
            holder.ivPic.setOnClickListener(view -> {
                Intent openDetails = new Intent(getContext(), MovieDetailsAct.class);
                openDetails.putExtra(MovieDetailsAct.EXTRA_MOVIE, movies.get(holder.getLayoutPosition()));
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
            });
        }

        @Override
        public int getItemCount() {
            return movies.size();
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
