package com.example.home.moviematters;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    public List<Result> movieslist;
    @Bind(R.id.moviegrid)
    public RecyclerView recyclerView;
    private MovieAdapter mAdapter;


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this,view);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new MovieAdapter(getActivity(),movieslist);
        recyclerView.setAdapter(mAdapter);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final GetMovieApi movieApi = retrofit.create(GetMovieApi.class);
        Call<Movie> call = movieApi.getMovie();
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Response<Movie> response) {
                try {


                    Movie movie = response.body();
                    movieslist = movie.getResults();
                    for (int i = 0; i < movieslist.size(); i++) {
                        Result result = movieslist.get(i);

                        String poster_path = Uri.parse("http://image.tmdb.org/t/p/w185").buildUpon()
                                .appendEncodedPath(result.getPosterPath())
                                .build().toString();
                        result.setPosterPath(poster_path);
                        movieslist.set(i,result);
                        mAdapter.updateData(movieslist);
                        Log.d("test", "Poster path  :" + i + " " + poster_path);

                    }


                } catch (NullPointerException e) {
                    Toast toast = null;
                    if (response.code() == 401) {
                        toast = Toast.makeText(getActivity(), "Unauthenticated", Toast.LENGTH_SHORT);
                    } else if (response.code() >= 400) {
                        toast = Toast.makeText(getActivity(), "Client Error " + response.code()
                                + " " + response.message(), Toast.LENGTH_SHORT);
                    }
                    toast.show();

                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("getQuestions threw: ", t.getMessage());


            }
        });


        return view;
    }
}
