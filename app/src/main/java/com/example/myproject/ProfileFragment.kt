package com.example.myproject

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileFragment : Fragment() {
    private lateinit var newOrdersList: RecyclerView
    private lateinit var archiveOrdersList: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        newOrdersList = view.findViewById(R.id.newOrdersList)
        archiveOrdersList = view.findViewById(R.id.archiveOrdersList)

        CoroutineScope(Dispatchers.Main).launch {
            val userId = (requireActivity() as MainActivity).getUserId()

            val orders = withContext(Dispatchers.IO) {
                dbOrders(requireContext()).loadOrders(userId)
            }

            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val newOrders = orders.filter { it.date >= currentDate }
            val archiveOrders = orders.filter { it.date < currentDate }

            newOrdersList.layoutManager = LinearLayoutManager(requireContext())
            archiveOrdersList.layoutManager = LinearLayoutManager(requireContext())

            newOrdersList.adapter = OrderAdapter(newOrders)
            archiveOrdersList.adapter = OrderAdapter(archiveOrders)
        }

        return view
    }
}

