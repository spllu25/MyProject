package com.example.myproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderAdapter(private val orders: List<Order>) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {
    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val clientName: TextView = view.findViewById(R.id.clientName)
        val address: TextView = view.findViewById(R.id.address)
        val date: TextView = view.findViewById(R.id.date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return OrderViewHolder(view)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orders[position]
        holder.clientName.text = order.clientName
        holder.address.text = order.address
        holder.date.text = order.date
    }

    override fun getItemCount(): Int = orders.size
}
