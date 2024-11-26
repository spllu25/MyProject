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

class ProfileFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_profile, container, false)
        val usercardsList: RecyclerView = view.findViewById(R.id.usercards)
        val cards = arrayListOf<Card>()

        usercardsList.layoutManager = LinearLayoutManager(requireContext())
        usercardsList.adapter = cardAdapter(cards, requireContext(), true)

        val buttonSignup: Button = view.findViewById(R.id.button)
        buttonSignup.setOnClickListener {
            val intent = Intent(context, ProfilActivity::class.java)
            startActivity(intent)
        }
        return view
    }
}