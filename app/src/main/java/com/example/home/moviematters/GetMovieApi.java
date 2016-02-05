package com.example.home.moviematters;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Prateesh Goswami
 * @version 1.0
 * @date 2/2/2016
 */
public interface GetMovieApi {
    @GET("/3/discover/movie")
    Call<Movie> getMovie(
            @Query("sort_by") String sortKey,
            @Query("api_key") String apiKey
    );
    @GET("/3/movie/{movieId}")
    Call<Result> getFavouriteMovie(
            @Path("movieId") int movieId,
            @Query("api_key") String apiKey
    );
}
