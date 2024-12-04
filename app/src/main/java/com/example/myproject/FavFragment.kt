package com.example.myproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FavFragment : Fragment() {
    private lateinit var favCardsList: RecyclerView
    private lateinit var adapter: cardAdapter
    private var userId: Int =-1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_fav, container, false)
        favCardsList = view.findViewById(R.id.favcards)
        userId = (requireActivity() as MainActivity).getUserId()

        adapter = cardAdapter(mutableListOf(), requireContext(), false, userId)
        favCardsList.layoutManager = LinearLayoutManager(requireContext())
        favCardsList.adapter = adapter

        return view
    }

    override fun onResume() {
        super.onResume()
        loadFavCards()
    }

    private fun loadFavCards() {
        viewLifecycleOwner.lifecycleScope.launch {
            val cards = withContext(Dispatchers.IO) {
                chooseManager.dbHelper?.loadCards(userId)?.filter { it.isFav } ?: listOf()
            }
            adapter.updateCards(cards.toMutableList())
        }
    }
}

