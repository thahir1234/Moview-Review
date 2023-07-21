package com.example.moviereview.db.local.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "tblFavouriteLists", foreignKeys = [ForeignKey(Users::class, parentColumns = ["email"], childColumns = ["email"], onDelete = ForeignKey.CASCADE), ForeignKey(Lists::class, parentColumns = ["listId"], childColumns = ["listId"], onDelete = ForeignKey.CASCADE)])
data class FavouriteLists(
    val email:String,
    val listId:Int
)
{
    @PrimaryKey(autoGenerate = true)
    var sNo:Int = 0
}