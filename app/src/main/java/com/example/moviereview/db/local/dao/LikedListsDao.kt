package com.example.moviereview.db.local.dao
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.moviereview.db.local.entities.LikedLists
import com.example.moviereview.db.local.entities.LikedReviews
import com.example.moviereview.db.local.entities.Lists

@Dao
interface LikedListsDao {

    @Upsert
    suspend fun addLikedList(likedList: LikedLists)

    @Query("select * from tblLikedLists where email = :email")
    public fun getLikedListsByEmail(email : String) : LiveData<List<LikedLists>>

    @Query("select * from tblLikedLists where listId = :listId")
    fun getLikedListsByListId(listId:Int) : LiveData<List<LikedLists>>

    @Query("select * from tblLikedLists where email = :email and listId = :listId")
    fun getLikedListsByBoth(email: String,listId: Int) : LiveData<List<LikedLists>>

    @Query("delete from tblLikedLists where email = :email and listId = :listId")
    suspend fun deleteLikedList(email: String,listId: Int)
}