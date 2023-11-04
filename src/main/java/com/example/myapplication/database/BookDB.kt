package com.example.myapplication.database

import com.example.myapplication.converter.BookTypeConverter
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.myapplication.converter.CartItemTypeConverter
import com.example.myapplication.converter.UserTypeConverter
import com.example.myapplication.dao.CartDao
import com.example.myapplication.dao.BookDao
import com.example.myapplication.dao.OrderDao
import com.example.myapplication.dao.UserDao
import com.example.myapplication.entity.Cart
import com.example.myapplication.entity.Book
import com.example.myapplication.entity.Order
import com.example.myapplication.entity.User

@Database(entities = [Book::class, User::class, Cart::class, Order::class], version = 7,exportSchema = false)
@TypeConverters(BookTypeConverter::class, CartItemTypeConverter::class, UserTypeConverter::class)
abstract class BookDB: RoomDatabase() {

    companion object{

        private var bookDB:BookDB? = null

        @Synchronized
        fun getDatabase(context: Context): BookDB{
            if (bookDB == null){
                bookDB = Room.databaseBuilder(
                    context,
                    BookDB::class.java,
                    "foods.db"
                ).fallbackToDestructiveMigration().build()
            }
            return bookDB!!
        }
    }

    abstract fun getBookDao():BookDao
    abstract fun getUserDao():UserDao
    abstract fun getCartDao(): CartDao
    abstract fun getOrderDao(): OrderDao

}