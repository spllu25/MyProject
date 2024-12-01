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

class HomeFragment : Fragment() {
    private lateinit var cardsList: RecyclerView
    private lateinit var adapter: cardAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        cardsList = view.findViewById(R.id.cards)
        cardsList.layoutManager = LinearLayoutManager(requireContext())
        adapter = cardAdapter(mutableListOf(), requireContext(), false, -1)
        cardsList.adapter = adapter

        CoroutineScope(Dispatchers.Main).launch {
            val userId = (requireActivity() as MainActivity).getUserId()
            val userCards = withContext(Dispatchers.IO) {
                chooseManager.dbHelper?.loadCards(userId)?.toMutableList() ?: mutableListOf()
            }
            if (userCards.isEmpty()) {
                val staticCards = arrayListOf<Card>()
                staticCards.add(Card(1,"image1", getString(R.string.title1), getString(R.string.txt1), false, false, false, 0))
                staticCards.add(Card(2,"image2", getString(R.string.title2), getString(R.string.txt2), false, false, false, 0))
                staticCards.add(Card(3,"image3", getString(R.string.title3), getString(R.string.txt3), false, false, false, 0))
                staticCards.add(Card(4,"image4", getString(R.string.title4), getString(R.string.txt4), false, false, false, 0))
                staticCards.add(Card(5,"image1", getString(R.string.title5), getString(R.string.txt5), false, false, false, 0))
                staticCards.add(Card(6,"image1", getString(R.string.title6), getString(R.string.txt6), false, false, false, 0))
                withContext(Dispatchers.IO) {
                    for (i in staticCards)
                        chooseManager.dbHelper?.saveCard(i, userId)
                }
                userCards.addAll(staticCards)
            }
            adapter.updateCards(userCards)
        }
        return view
    }
}

