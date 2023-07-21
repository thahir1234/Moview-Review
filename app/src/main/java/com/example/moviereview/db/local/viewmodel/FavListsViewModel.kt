package com.example.moviereview.db.local.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.moviereview.db.local.MovieDatabase
import com.example.moviereview.db.local.entities.FavouriteLists
import com.example.moviereview.db.local.entities.FavouriteMovies
import com.example.moviereview.db.local.repositories.FavMoviesRepository
import com.example.moviereview.db.local.repositories.FavouriteListsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavListsViewModel (application: Application) : AndroidViewModel(application) {
    private val repository : FavouriteListsRepository

    lateinit var favListsByEmail: LiveData<List<FavouriteLists>>
    init{
        val favouriteListsDao = MovieDatabase.getDatabase(application)?.favouriteListsDao()
        repository = FavouriteListsRepository(favouriteListsDao!!)
    }

    fun addFavList(favList: FavouriteLists)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFavList(favList)
        }
    }

    fun getFavListByEmail(email:String)
    {
        favListsByEmail = repository.getFavListsByEmail(email)
    }

    fun deleteFavMList(email: String,listId:Int)
    {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFavList(email,listId)
        }
    }
}