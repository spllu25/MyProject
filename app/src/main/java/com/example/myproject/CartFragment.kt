package com.example.myproject

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class CartFragment : Fragment() {
    private lateinit var purchCardsList: RecyclerView
    private lateinit var adapter: cardAdapter
    private var userId: Int =-1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        purchCardsList = view.findViewById(R.id.addedCards)
        userId = (requireActivity() as MainActivity).getUserId()

        adapter = cardAdapter(mutableListOf(), requireContext(), true, userId)
        purchCardsList.layoutManager = LinearLayoutManager(requireContext())
        purchCardsList.adapter = adapter

        view.findViewById<Button>(R.id.buttonBuy).setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val intent = Intent(context, OformlActivity::class.java).apply {
                    putExtra("cost", 100)
                    putExtra("userId", userId)
                }
                startActivity(intent)
            }
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        loadCards()
    }

    private fun loadCards() {
        viewLifecycleOwner.lifecycleScope.launch {
            val cards = withContext(Dispatchers.IO) {
                chooseManager.dbHelper?.loadCards(userId)?.filter { it.isPurch } ?: listOf()
            }
            adapter.updateCards(cards.toMutableList())
        }
    }
}

