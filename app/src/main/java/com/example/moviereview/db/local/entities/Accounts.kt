package com.example.moviereview.db.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tblAuthentication")
class Accounts(
    @PrimaryKey(autoGenerate = false)
    val email:String,
    val password:String,
){

}
