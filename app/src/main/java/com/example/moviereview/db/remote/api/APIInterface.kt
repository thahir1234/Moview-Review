package com.example.moviereview.db.remote.api

import com.example.moviereview.db.local.entities.Reviews
import com.example.moviereview.db.remote.model.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIInterface {

    @GET("movie/top_rated")
    fun getPopularMovies(
        @Query("api_key")
        apiKey:String ,
        @Query("language")
        lang:String ,
        @Query("page")
        page:String
    ) : Call<MovieList>

    @GET("trending/movie/week")
    fun getTrendingMovies(
        @Query("api_key")
        apiKey:String,
        @Query("page")
        page:String
    ) : Call<MovieList>

    @GET("movie/now_playing")
    fun getRecentMovies(
        @Query("api_key")
        apiKey:String,
        @Query("language")
        lang:String,
        @Query("page")
        page:String
    ) : Call<MovieList>

    @GET("discover/movie")
    fun getMoviesByGenre(
        @Query("api_key")
        apiKey:String,
        @Query("language")
        lang:String,
        @Query("sort_by")
        sort:String,
        @Query("include_adult")
        includeAdult:String,
        @Query("include_video")
        includeVideo:String,
        @Query("page")
        page:String,
        @Query("with_genres")
        genre:String
    ) : Call<MovieList>

    @GET("movie/{id}")
    fun getMovie(
        @Path("id")
        movieId:Int,
        @Query("api_key")
        apiKey:String
    ) : Call<LongMovieDesc>

    @GET("movie/{id}/credits")
    fun getCasts(
        @Path("id")
        movieId: Int,
        @Query("api_key")
        apiKey:String
    ) : Call<CastList>

    @GET("movie/{id}/similar")
    fun getSimilarMovies(
        @Path("id")
        movieId: Int,
        @Query("api_key")
        apiKey:String
    ) : Call<MovieList>

    @GET("search/movie")
    fun searchMovies(
        @Query("query")
        searchWord:String,
        @Query("api_key")
        apiKey:String,
        @Query("page")
        page:String
    ) : Call<MovieList>

    @GET("person/{id}")
    fun getPeople(
        @Path("id")
        personId : Int,
        @Query("api_key")
        apiKey : String
    ) : Call<PersonDesc>

    @GET("movie/{movieId}/reviews")
    fun getReviews(
        @Path("movieId")
        movieId: Int,
        @Query("api_key")
        apiKey: String,
    ) : Call<ReviewList>
}