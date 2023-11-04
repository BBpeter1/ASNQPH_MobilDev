package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.BookAdapter
import com.example.myapplication.entity.Book
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FirstScreenActivity : ComponentActivity() , View.OnClickListener {

    var arrBook = ArrayList<Book>()
    var bookAdapter = BookAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)

        arrBook.add(Book(1, "Harry Potter", 10,"Leírás1!", "https://static1.lira.hu/upload/M_28/rek1/253/1277253.jpg"))
        arrBook.add(Book(2, "Star Wars", 15,"Leírás2", "https://static1.lira.hu/upload/M_28/rek1/815/2991815.jpg"))
        bookAdapter.setData(arrBook)

        val recyclerView = findViewById<RecyclerView>(R.id.bookCardElement)
        recyclerView.adapter = bookAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        bottomNavigationHandler()

    }

    override fun onClick(p0: View?) {
        var intent = Intent(this@FirstScreenActivity, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun bottomNavigationHandler() {

        val cart = findViewById<FloatingActionButton>(R.id.cartIcon)
        cart.setOnClickListener {
            var intent = Intent(this@FirstScreenActivity, CartActivity::class.java)
            startActivity(intent)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.profile -> {
                    var intent = Intent(this@FirstScreenActivity, ProfileActivity::class.java)
                    startActivity(intent)
                    finish()
                    return@setOnNavigationItemSelectedListener true
                }

                else -> false
            }
        }
    }

}
