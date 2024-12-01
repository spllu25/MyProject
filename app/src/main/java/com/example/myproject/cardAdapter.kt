package com.example.myproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class cardAdapter(
    private var cards: MutableList<Card>,
    private val context: Context,
    private val isCartFragment: Boolean,
    val userId : Int
) : RecyclerView.Adapter<cardAdapter.myViewHolder>() {


    class myViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.image)
        val title: TextView = view.findViewById(R.id.textTitle)
        val opisaniye: TextView = view.findViewById(R.id.textOpis)
        val likedButton: Button = view.findViewById(R.id.buttonLike)
        val toBuyButton: Button = view.findViewById(R.id.buttonBuy)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card, parent, false)
        return myViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cards.size
    }

    fun updateCards(newCards: List<Card>) {
        cards.clear()
        cards.addAll(newCards)
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val card = cards[position]
        if (isCartFragment) {
            holder.likedButton.isEnabled = false
        }

        holder.title.text = card.title
        holder.opisaniye.text = card.txt
        val imgId = context.resources.getIdentifier(card.img, "drawable", context.packageName)
        holder.img.setImageResource(imgId)

        holder.likedButton.text = if (card.isFav) "★" else "☆"
        holder.toBuyButton.text = if (card.quantityPurch > 0) "+(${card.quantityPurch})" else "+"

        holder.likedButton.setOnClickListener {
            card.isFav = !card.isFav
            holder.likedButton.text = if (card.isFav) "★" else "☆"

            CoroutineScope(Dispatchers.IO).launch {
                chooseManager.dbHelper?.updateIsFav(card.id, card.isFav)
                chooseManager.loadCards(userId)
            }
        }

        holder.toBuyButton.setOnClickListener {
            if (isCartFragment) {
                if (card.quantityPurch > 1) {
                    card.quantityPurch -= 1
                    holder.toBuyButton.text = "+(${card.quantityPurch})"
                }
                else if (card.isPurch) {
                    card.isPurch = false
                    card.quantityPurch = 0
                    holder.toBuyButton.text = "+"
                }
            }
            else {
                if (!card.isPurch) {
                    card.isPurch = true
                    card.quantityPurch = 1
                } else {
                    card.quantityPurch += 1
                }
                holder.toBuyButton.text = "+(${card.quantityPurch})"
            }
            CoroutineScope(Dispatchers.IO).launch {
                chooseManager.dbHelper?.updateQuantity(card.id, card.quantityPurch)
                chooseManager.dbHelper?.updateIsPurch(card.id, card.isPurch)
                chooseManager.loadCards(userId)
            }
        }
    }
}
