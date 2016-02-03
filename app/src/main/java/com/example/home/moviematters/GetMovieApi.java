package com.example.home.moviematters;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @author Prateesh Goswami
 * @version 1.0
 * @date 2/2/2016
 */
public interface GetMovieApi {
    @GET("/3/discover/movie?sort_by=popularity.desc&api_key=4d8f552f39388ab79c89e70dd5eafd2f")
    Call<Movie> getMovie(
//                @Path("movieId") String movieId,
//                @Query("q") String apiKey

    );
}
