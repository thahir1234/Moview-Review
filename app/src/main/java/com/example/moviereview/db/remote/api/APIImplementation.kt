package com.example.moviereview.db.remote.api

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviereview.db.remote.model.*
import com.example.moviereview.view.list_screen_clean.data.api.dto.MovieList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class APIImplementation {

    companion object{
        val api_key = "b709a1805d375b12bf5d4bf89adbd9f3"
        val language = "en-US"
        private val retrofit:Retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        private val apiInterface:APIInterface = retrofit.create(APIInterface::class.java)

        fun getPopularMovies(
            apiKey:String = api_key,
            lang:String = language,
            page:String = "1"
        ) : LiveData<MovieList>?
        {
            val movies : MutableLiveData<MovieList>? = MutableLiveData<MovieList>()
            var dummyMovies : MovieList? = null
            val callResult : Call<MovieList> = apiInterface.getPopularMovies(apiKey,lang,page)


            callResult.enqueue(
                object : Callback<MovieList>
                {
                    override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                        if(!response.isSuccessful)
                        {

                            Log.i("popApi","Respoce unsuccess : ${lang}")
                        }

                        dummyMovies = response.body()
                        movies?.value=dummyMovies!!
                        Log.i("popApi",dummyMovies.toString())
                    }

                    override fun onFailure(call: Call<MovieList>, t: Throwable) {
                        Log.i("popApi","Respose failed")

                    }

                }
            )

            Log.i("api","1 call")
            return movies
        }

        fun getTrendingMovies(
            apiKey:String = api_key,
            page:String = "1"
        ) : LiveData<MovieList>?
        {
            val movies : MutableLiveData<MovieList>? = MutableLiveData<MovieList>()
            val callResult : Call<MovieList> = apiInterface.getTrendingMovies(apiKey,page)

            callResult.enqueue(
                object : Callback<MovieList>
                {
                    override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                        if(!response.isSuccessful)
                        {

                        }

                        movies?.value = response.body()
                        Log.i("trending",response.body()?.results?.size.toString())
                    }

                    override fun onFailure(call: Call<MovieList>, t: Throwable) {

                    }

                }
            )

            return movies
        }

        fun getRecentMovies(
            apiKey:String = api_key,
            lang:String = language,
            page:String = "1"
        ) : MutableLiveData<MovieList>?
        {
            var movies : MutableLiveData<MovieList>? = MutableLiveData<MovieList>()
            val callResult : Call<MovieList> = apiInterface.getRecentMovies(apiKey,lang,page)

            callResult.enqueue(
                object : Callback<MovieList>
                {
                    override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                        if(!response.isSuccessful)
                        {

                        }

                        movies?.value = response.body()
                    }

                    override fun onFailure(call: Call<MovieList>, t: Throwable) {

                    }

                }
            )

            return movies
        }

        fun getMovie(
            movieId:Int,
            apiKey:String = api_key
        ) : MutableLiveData<LongMovieDesc>?
        {
            var movie : MutableLiveData<LongMovieDesc>? = MutableLiveData<LongMovieDesc>()
            val callResult : Call<LongMovieDesc> = apiInterface.getMovie(movieId,apiKey)

            callResult.enqueue(
                object : Callback<LongMovieDesc>
                {
                    override fun onResponse(
                        call: Call<LongMovieDesc>,
                        response: Response<LongMovieDesc>
                    ) {
                        if(!response.isSuccessful)
                        {
                            Log.i("movie","UnSuccess"+response.message())
                        }
                        Log.i("movie","Success"+response.body()?.name)
                        movie?.value = response.body()

                    }

                    override fun onFailure(call: Call<LongMovieDesc>, t: Throwable) {

                    }

                }
            )

            return movie
        }

        fun getSimilarMovies(
            movieId: Int,
            apiKey:String = api_key
        ) : MutableLiveData<MovieList>?
        {
            var movies : MutableLiveData<MovieList>? = MutableLiveData<MovieList>()
            val callResult : Call<MovieList> = apiInterface.getSimilarMovies(movieId,apiKey)

            callResult.enqueue(
                object : Callback<MovieList>
                {
                    override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                        if(!response.isSuccessful)
                        {

                        }

                        movies?.value = response.body()
                    }

                    override fun onFailure(call: Call<MovieList>, t: Throwable) {

                    }

                }
            )

            return movies
        }

        fun getCasts(
            movieId: Int,
            apiKey:String = api_key
        ) : MutableLiveData<CastList>?
        {
            var casts : MutableLiveData<CastList>? = MutableLiveData<CastList>()
            val callResult : Call<CastList> = apiInterface.getCasts(movieId,apiKey)

            callResult.enqueue(
                object : Callback<CastList>
                {
                    override fun onResponse(call: Call<CastList>, response: Response<CastList>) {
                        if(!response.isSuccessful)
                        {

                        }

                        Log.i("casts","response success")
                        casts?.value = response.body()
                    }

                    override fun onFailure(call: Call<CastList>, t: Throwable) {

                    }

                }
            )

            return casts
        }

        fun getMoviesByGenre(
            apiKey:String = api_key,
            lang:String = language,
            sort:String = "popularity.desc",
            includeAdult:String = "false",
            includeVideo:String = "false",
            page:String = "1",
            genre:String
        ) : MutableLiveData<MovieList>?
        {
            var movies : MutableLiveData<MovieList>? = MutableLiveData<MovieList>()
            val callResult : Call<MovieList> = apiInterface.getMoviesByGenre(apiKey,lang,sort,includeAdult,includeVideo,page,genre)

            callResult.enqueue(
                object : Callback<MovieList>
                {
                    override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                        if(!response.isSuccessful)
                        {
                            Log.i("action","Unsucces " + response.message())
                        }

                        Log.i("action","Succes " + genre +" "+ response.body()?.results?.size)
                        movies?.value = response.body()
                    }

                    override fun onFailure(call: Call<MovieList>, t: Throwable) {

                    }

                }
            )

            return movies
        }

        fun searchMovies(
            searchWord:String,
            apiKey:String = api_key,
            page:String = "1"
        ) : MutableLiveData<MovieList>?
        {
            var movies : MutableLiveData<MovieList>? = MutableLiveData<MovieList>()
            val callResult : Call<MovieList> = apiInterface.searchMovies(searchWord,apiKey,page)

            callResult.enqueue(
                object : Callback<MovieList>
                {
                    override fun onResponse(call: Call<MovieList>, response: Response<MovieList>) {
                        if(!response.isSuccessful)
                        {

                        }

                        Log.i("search","result success"+response.body()?.results?.size)
                        movies?.value = response.body()
                    }

                    override fun onFailure(call: Call<MovieList>, t: Throwable) {

                    }

                }
            )

            return movies
        }

        fun getPerson(
            personId : Int,
            apiKey: String = api_key
        ) : MutableLiveData<PersonDesc>?
        {
            var person : MutableLiveData<PersonDesc>? = MutableLiveData()
            val callResult : Call<PersonDesc> = apiInterface.getPeople(personId,apiKey)

            callResult.enqueue(
                object : Callback<PersonDesc>
                {
                    override fun onResponse(
                        call: Call<PersonDesc>,
                        response: Response<PersonDesc>
                    ) {
                        if(!response.isSuccessful)
                        {

                        }
                        person?.value = response.body()
                        Log.i("person",response.body()?.actingName.toString())
                    }

                    override fun onFailure(call: Call<PersonDesc>, t: Throwable) {

                    }

                }
            )

            return person
        }

        fun getReviews(
            apiKey: String = api_key,
            movieId: Int
        ) : MutableLiveData<ReviewList>?
        {
            var reviews : MutableLiveData<ReviewList>? = MutableLiveData()
            val callResult : Call<ReviewList> = apiInterface.getReviews(movieId,apiKey)

            callResult.enqueue(
                object : Callback<ReviewList>
                {
                    override fun onResponse(
                        call: Call<ReviewList>,
                        response: Response<ReviewList>
                    ) {
                        if(!response.isSuccessful)
                        {

                        }
                        reviews?.value = response.body()
                    }

                    override fun onFailure(call: Call<ReviewList>, t: Throwable) {

                    }
                }
            )
            return reviews
        }
    }

}