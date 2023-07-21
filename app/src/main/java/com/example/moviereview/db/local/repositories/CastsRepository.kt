package com.example.moviereview.db.local.repositories

import androidx.lifecycle.LiveData
import com.example.moviereview.db.local.dao.CastsDao
import com.example.moviereview.db.local.entities.Casts

class CastsRepository(private val castsDao: CastsDao) {

    lateinit var casts: LiveData<List<Casts>>

    suspend fun addCast(cast: Casts)
    {
        castsDao.addCast(cast)
    }

    fun getAllCasts() : LiveData<List<Casts>>
    {
        return castsDao.getAllCasts()
    }
    fun getCasts(movieId:Int) : LiveData<List<Casts>>
    {
        casts = castsDao.getCats(movieId)
        return casts
    }
}