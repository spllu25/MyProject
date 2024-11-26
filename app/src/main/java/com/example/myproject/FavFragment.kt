package com.example.myproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/*class FavFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fav, container, false)
        val favCardsList: RecyclerView = view.findViewById(R.id.favcards)

        CoroutineScope(Dispatchers.Main).launch {
            val cards = withContext(Dispatchers.IO) {
                chooseManager.dbHelper?.loadCards()?.filter { it.isFav } ?: listOf()
            }.toMutableList()

            favCardsList.layoutManager = LinearLayoutManager(requireContext())
            favCardsList.adapter = cardAdapter(cards, requireContext(), false)

        }
        return view
    }
}*/
class FavFragment : Fragment() {
    private lateinit var favCardsList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fav, container, false)
        favCardsList = view.findViewById(R.id.favcards)

        return view
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.Main).launch {
            val cards = withContext(Dispatchers.IO) {
                chooseManager.dbHelper?.loadCards()?.filter { it.isFav } ?: listOf()
            }
            favCardsList.layoutManager = LinearLayoutManager(requireContext())
            favCardsList.adapter = cardAdapter(cards.toMutableList(), requireContext(), false)
        }
    }
}
