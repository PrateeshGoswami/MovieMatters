package com.example.home.moviematters;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
    public List<Result> favMoviesList;

    @Bind(R.id.moviegrid)
    public RecyclerView recyclerView;
    private MovieAdapter mAdapter;
    private String sortKey;
    Constants constants = new Constants();


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new MovieAdapter(getActivity(), movieslist);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(getActivity(), "onclick" + position, Toast.LENGTH_SHORT).show();
                Result result = mAdapter.getItem(position);
                int movieId = result.getId();
                ((CallBack) getActivity()).onItemSelected(movieId);

            }
        }));


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sortKey = sharedPref.getString(getString(R.string.pref_movie_sorting_key),
                getString(R.string.pref_movie_sorting_default));
        switch (sortKey) {
            case "popularity.desc":
                FetchMovieTask(sortKey);
                break;
            case "vote_average.desc":
                FetchMovieTask(sortKey);
                break;
            case "favourite":

                FetchFavouriteTask(281957);
                break;

//                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Data", 0);
//                int size = sharedPreferences.getInt("Status_Size", 0);
//                for (int i = 0; i < size; i++) {
//                    String string = sharedPreferences.getString("Status_" + i, null);

        }

    }


    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, RecyclerView recyclerView, ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {

                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));

            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public static interface ClickListener {
        public void onClick(View view, int position);
    }

    public interface CallBack {
        public void onItemSelected(int movieID);
    }

    public void FetchMovieTask(String sortingOrder) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final GetMovieApi movieApi = retrofit.create(GetMovieApi.class);
        Call<Movie> call = movieApi.getMovie(sortingOrder, constants.API_KEY);
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
                        movieslist.set(i, result);
                        mAdapter.updateData(movieslist);

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
    }


    public void FetchFavouriteTask(int movieId) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final GetMovieApi movieApi = retrofit.create(GetMovieApi.class);
        Call<Result> call = movieApi.getFavouriteMovie(movieId,constants.API_KEY);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Response<Result> response) {
                try {
                    Result movie = response.body();
                    String poster_path = Uri.parse("http://image.tmdb.org/t/p/w185").buildUpon()
                            .appendEncodedPath(movie.getPosterPath())
                            .build().toString();
                    movie.setPosterPath(poster_path);
//                    favMoviesList.add(movie);
//                    mAdapter.swap(favMoviesList);
                    String str = movie.getOriginalTitle();

                    Log.d("test","Original titile & Poster Path :"+str +" " + poster_path );


                }catch (NullPointerException e) {
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


    }
}
