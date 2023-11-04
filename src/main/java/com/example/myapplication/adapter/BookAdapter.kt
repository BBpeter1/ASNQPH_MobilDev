package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.dao.CartDao
import com.example.myapplication.database.BookDB
import com.example.myapplication.entity.Cart
import com.example.myapplication.entity.Book
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookAdapter : RecyclerView.Adapter<BookAdapter.RecipeViewHolder>() {

    var listener: OnItemClickListener? = null
    var ctx: Context? = null
    var arrBook = ArrayList<Book>()

    class RecipeViewHolder(view: View): RecyclerView.ViewHolder(view){

    }

    fun setData(arrData : List<Book>){
        arrBook = arrData as ArrayList<Book>
    }

    fun setClickListener(listener1: OnItemClickListener){
        listener = listener1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        ctx = parent.context
        return RecipeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cardelement_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return arrBook.size
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {

        val dishNameTextView = holder.itemView.findViewById<TextView>(R.id.bookName)
        val dishDesc = holder.itemView.findViewById<TextView>(R.id.bookDescription)
        val dishPrice = holder.itemView.findViewById<TextView>(R.id.bookPrice)
        val plusButton = holder.itemView.findViewById<Button>(R.id.plusButton)

        dishNameTextView.text = arrBook[position].bookName
        dishDesc.text = arrBook[position].description
        dishPrice.text = "Ár: " + arrBook[position].price.toString() + "ft"

        val imageView = holder.itemView.findViewById<ImageView>(R.id.foodPicture)
        val imageUrl = arrBook[position].picture
        Picasso.get().load(imageUrl).into(imageView)

        val sharedPreferences = holder.itemView.context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val savedUsername = sharedPreferences.getString("loggedInUser", "")

        if (savedUsername.isNullOrEmpty()) {
            plusButton.visibility = View.INVISIBLE
        } else {
            plusButton.visibility = View.VISIBLE

            plusButton.setOnClickListener { view ->

                val food = arrBook[position]
                val cartDao = BookDB.getDatabase(view.context).getCartDao()

                addToCart(food, cartDao)

                Toast.makeText(
                    view.context,
                    "Hozzáadva a kosárhoz!",
                    Toast.LENGTH_SHORT
                ).show()

            }
        }

    }

    fun addToCart(book: Book, cartDao: CartDao) {

        CoroutineScope(Dispatchers.IO).launch {
            val existingCartItem = cartDao.checkIfAdded(book)

            if (existingCartItem != null) {
                existingCartItem.quantity++
                cartDao.updateQuantity(existingCartItem)
            } else {
                val newCart = Cart(0, book, 1)
                cartDao.insertCartItem(newCart)
            }

        }

    }

    interface OnItemClickListener{
        fun onClicked(categoryName:String)
    }

}