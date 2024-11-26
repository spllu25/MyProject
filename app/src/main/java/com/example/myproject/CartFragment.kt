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


/*class CartFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        val purchCardsList: RecyclerView = view.findViewById(R.id.addedcards)

        CoroutineScope(Dispatchers.Main).launch {
            val cards = withContext(Dispatchers.IO) {
                chooseManager.dbHelper?.loadCards()?.filter { it.isPurch } ?: listOf()
            }
            purchCardsList.layoutManager = LinearLayoutManager(requireContext())
            purchCardsList.adapter = cardAdapter(cards.toMutableList(), requireContext(), true)

        }

        return view
    }
}*/
class CartFragment : Fragment() {
    private lateinit var purchCardsList: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)
        purchCardsList = view.findViewById(R.id.addedcards)

        return view
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.Main).launch {
            val cards = withContext(Dispatchers.IO) {
                chooseManager.dbHelper?.loadCards()?.filter { it.isPurch } ?: listOf()
            }
            purchCardsList.layoutManager = LinearLayoutManager(requireContext())
            purchCardsList.adapter = cardAdapter(cards.toMutableList(), requireContext(), true)
        }
    }

}
