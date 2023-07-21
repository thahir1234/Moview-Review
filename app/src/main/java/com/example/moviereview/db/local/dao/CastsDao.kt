package com.example.moviereview.db.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.moviereview.db.local.entities.Casts

@Dao
interface CastsDao {

    @Upsert
    suspend fun addCast(cast:Casts)

    @Query("Select * from tblCasts")
    fun getAllCasts() : LiveData<List<Casts>>

    @Query("Select * from tblCasts where movieId = :movieId")
    fun getCats(movieId:Int): LiveData<List<Casts>>
}