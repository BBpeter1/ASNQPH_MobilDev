package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.CartActivity
import com.example.myapplication.CartItemClickListener
import com.example.myapplication.R
import com.example.myapplication.database.BookDB
import com.example.myapplication.entity.Cart
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartAdapter(private val myListener: CartItemClickListener) : RecyclerView.Adapter<CartAdapter.FoodHolder>()  {

    var listener: BookAdapter.OnItemClickListener? = null
    var ctx: Context? = null
    var arrCarts = ArrayList<Cart>()

    class FoodHolder(view: View): RecyclerView.ViewHolder(view){

    }

    fun setData(arrData : List<Cart>){
        arrCarts = arrData as ArrayList<Cart>
    }

    fun setClickListener(listener1: BookAdapter.OnItemClickListener){
        listener = listener1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        ctx = parent.context
        return FoodHolder(LayoutInflater.from(parent.context).inflate(R.layout.checkout_card_element_layout,parent,false))
    }

    override fun onBindViewHolder(holder: FoodHolder, position: Int) {

        val dishNameTextView = holder.itemView.findViewById<TextView>(R.id.bookName)
        val dishPrice = holder.itemView.findViewById<TextView>(R.id.bookPrice)
        val dishQuantity = holder.itemView.findViewById<TextView>(R.id.quantityText)

        dishNameTextView.text = arrCarts[position].bookAdded.bookName
        dishPrice.text = "Price: " + arrCarts[position].bookAdded.price.toString() + "$"
        dishQuantity.text = arrCarts[position].quantity.toString()


        val imageView = holder.itemView.findViewById<ImageView>(R.id.foodPicture)
        val imageUrl = arrCarts[position].bookAdded.picture
        Picasso.get().load(imageUrl).into(imageView)

        val minusBtn = holder.itemView.findViewById<Button>(R.id.minusQuantity)
        val plusBtn = holder.itemView.findViewById<Button>(R.id.plusQuantity)
        val deleteItem = holder.itemView.findViewById<Button>(R.id.deleteItem)

        deleteItem.setOnClickListener { view ->
            CoroutineScope(Dispatchers.IO).launch {
                val cartDao = BookDB.getDatabase(view.context).getCartDao()
                cartDao.deleteItem(arrCarts[position])

                (view.context as? CartActivity)?.runOnUiThread {
                    myListener.onItemDelete(arrCarts[position])
                    arrCarts.removeAt(position)
                    notifyDataSetChanged()
                }
            }
        }

        minusBtn.setOnClickListener { view ->
            CoroutineScope(Dispatchers.IO).launch {
                val cartDao = BookDB.getDatabase(view.context).getCartDao()
                val cartItem = cartDao.getCartItem(arrCarts[position].id)

                cartItem.quantity--
                cartDao.updateQuantity(cartItem)
                arrCarts[position].quantity--

                (view.context as? CartActivity)?.runOnUiThread {
                    if(cartItem.quantity<=0){
                        CoroutineScope(Dispatchers.IO).launch {

                            cartDao.deleteItem(arrCarts[position])

                            (view.context as? CartActivity)?.runOnUiThread {
                                myListener.onItemQuantityMinus(cartItem.bookAdded.price)
                                arrCarts.removeAt(position)
                                notifyDataSetChanged()
                            }
                        }
                    }else{
                        dishQuantity.text = cartItem.quantity.toString()
                        myListener.onItemQuantityMinus(cartItem.bookAdded.price)
                    }
                }
            }
        }

        plusBtn.setOnClickListener { view ->
            CoroutineScope(Dispatchers.IO).launch {
                val cartDao = BookDB.getDatabase(view.context).getCartDao()
                val cartItem = cartDao.getCartItem(arrCarts[position].id)

                cartItem.quantity++
                cartDao.updateQuantity(cartItem)
                arrCarts[position].quantity++

                (view.context as? CartActivity)?.runOnUiThread {
                    dishQuantity.text = cartItem.quantity.toString()
                    myListener.onItemQuantityPlus(cartItem.bookAdded.price)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return arrCarts.size
    }

    fun clear() {
        arrCarts.clear()
        notifyDataSetChanged()
    }

}