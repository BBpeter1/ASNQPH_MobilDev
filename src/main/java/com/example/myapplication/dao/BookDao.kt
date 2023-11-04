package com.example.myapplication.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.myapplication.entity.Book

@Dao
interface BookDao {

    @Query("SELECT * FROM book ORDER BY id DESC")
    fun getAllBooks(): List<Book>

}