package com.example.myproject

import android.content.Intent
import android.os.Bundle
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


class CartFragment : Fragment() {
    private lateinit var purchCardsList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        purchCardsList = view.findViewById(R.id.addedCards)
        val buttonBuy: Button = view.findViewById(R.id.buttonBuy)
        buttonBuy.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                val userId = (requireActivity() as MainActivity).getUserId()
                val intent = Intent(context, OformlActivity::class.java)
                intent.putExtra("cost", 100)
                intent.putExtra("userId", userId)
                startActivity(intent)
            }
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.Main).launch {
            val userId = (requireActivity() as MainActivity).getUserId()
            val cards = withContext(Dispatchers.IO) {
                chooseManager.dbHelper?.loadCards(userId)?.filter { it.isPurch } ?: listOf()
            }
            purchCardsList.layoutManager = LinearLayoutManager(requireContext())
            purchCardsList.adapter = cardAdapter(cards.toMutableList(), requireContext(), true, userId)
            purchCardsList.adapter?.notifyDataSetChanged()
        }
    }

}
