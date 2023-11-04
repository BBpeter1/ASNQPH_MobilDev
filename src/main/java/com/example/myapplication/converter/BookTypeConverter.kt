package com.example.myapplication.converter

import androidx.room.TypeConverter
import com.example.myapplication.entity.Book
import com.google.gson.Gson

class BookTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromBook(book: Book): String {
        return gson.toJson(book)
    }

    @TypeConverter
    fun toBook(json: String): Book {
        return gson.fromJson(json, Book::class.java)
    }
}
