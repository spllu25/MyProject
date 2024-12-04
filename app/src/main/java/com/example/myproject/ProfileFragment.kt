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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ProfileFragment : Fragment() {
    private lateinit var newOrdersList: RecyclerView
    private lateinit var archiveOrdersList: RecyclerView
    private lateinit var db: dbCard
    private var userId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val buttonToSettings: Button = view.findViewById(R.id.buttonProfileSettings)

        viewLifecycleOwner.lifecycleScope.launch {
            userId = (requireActivity() as MainActivity).getUserId()
        }
        newOrdersList = view.findViewById(R.id.newOrdersList)
        archiveOrdersList = view.findViewById(R.id.archiveOrdersList)

        newOrdersList.layoutManager = LinearLayoutManager(requireContext())
        archiveOrdersList.layoutManager = LinearLayoutManager(requireContext())

        newOrdersList.adapter = cardAdapter(mutableListOf(), requireContext(), false, userId)

        db = dbCard(requireContext())

        buttonToSettings.setOnClickListener{
            val intent = Intent(context, ProfilActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        loadOrders()
    }

    private fun loadOrders() {
        viewLifecycleOwner.lifecycleScope.launch {
            val orderedCards = withContext(Dispatchers.IO) {
                db.loadCards(userId).filter { it.isOrdered }
            }

            val currentDate = Calendar.getInstance()
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val newOrders = orderedCards.filter { card ->
                val cardDate = formatter.parse(card.date ?: "")
                cardDate != null && !cardDate.after(currentDate.time)
            }

            val archiveOrders = orderedCards.filter { card ->
                val cardDate = formatter.parse(card.date ?: "")
                cardDate != null && cardDate.before(currentDate.time)
            }

            newOrdersList.adapter = cardAdapter(newOrders.toMutableList(), requireContext(), false, userId)
            archiveOrdersList.adapter = cardAdapter(archiveOrders.toMutableList(), requireContext(), false, userId)
        }
    }
}
