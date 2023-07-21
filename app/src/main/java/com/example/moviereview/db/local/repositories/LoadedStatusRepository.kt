package com.example.moviereview.db.local.repositories

import com.example.moviereview.db.local.dao.LoadedStatusDao
import com.example.moviereview.db.local.entities.LoadedStatus

class LoadedStatusRepository(private val loadedStatusDao: LoadedStatusDao) {

    var allData = loadedStatusDao.getAllData()

    suspend fun addLoadedStatus(loadedStatus: LoadedStatus)
    {
        loadedStatusDao.addLoadedStatus(loadedStatus)
    }

    suspend fun deleteAllData()
    {
        loadedStatusDao.deleteAllData()
    }

    suspend fun updatePopular()
    {
        loadedStatusDao.updatePopular()
    }

    suspend fun updateTrending()
    {
        loadedStatusDao.updateTrending()
    }
}