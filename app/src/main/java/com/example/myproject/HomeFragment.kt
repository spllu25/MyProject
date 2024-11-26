package com.example.myproject

import androidx.fragment.app.viewModels
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val cardsList: RecyclerView = view.findViewById(R.id.cards)

        cardsList.layoutManager = LinearLayoutManager(requireContext())

        CoroutineScope(Dispatchers.Main).launch {
            val dbCards = withContext(Dispatchers.IO) {
                chooseManager.dbHelper?.loadCards() ?: listOf()
            }.toMutableList()

            if (dbCards.isEmpty()) {
                val staticCards = listOf(
                    Card(1, "image1", getString(R.string.title1), getString(R.string.txt1), false, false, 0)
                )
                dbCards.addAll(staticCards)
                withContext(Dispatchers.IO) {
                    for (card in staticCards) {
                        chooseManager.dbHelper?.saveCard(card)
                    }
                }
            }

            cardsList.adapter = cardAdapter(dbCards, requireContext(), false)
        }

        return view
    }


}

