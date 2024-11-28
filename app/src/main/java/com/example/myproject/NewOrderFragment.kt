package com.example.myproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NewOrderFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_order, container, false)

        val base: Spinner = view.findViewById(R.id.base)
        val filling: Spinner = view.findViewById(R.id.filling)
        val cream: Spinner = view.findViewById(R.id.cream)
        val color: Spinner = view.findViewById(R.id.color)

        val dataBase = listOf("Основа", "Опция 1", "Опция 2", "Опция 3")
        val dataFilling = listOf("Начинка", "Опция 1", "Опция 2", "Опция 3")
        val dataCream = listOf("Крем", "Опция 1", "Опция 2", "Опция 3")
        val dataColor = listOf("Цвет", "Опция 1", "Опция 2", "Опция 3")

        val adapterBase = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dataBase)
        adapterBase.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val adapterFilling = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dataFilling)
        adapterFilling.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val adapterCream = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dataCream)
        adapterCream.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val adapterColor = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dataColor)
        adapterColor.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        base.adapter = adapterBase
        filling.adapter = adapterFilling
        cream.adapter = adapterCream
        color.adapter = adapterColor

        val buttonLogin: Button = view.findViewById(R.id.buttonLogin)
        buttonLogin.setOnClickListener {
            val selected1 = base.selectedItem.toString()
            val selected2 = filling.selectedItem.toString()
            val selected3 = cream.selectedItem.toString()
            val selected4 = color.selectedItem.toString()

            Toast.makeText(
                requireContext(),
                "Вы выбрали: $selected1, $selected2, $selected3, $selected4",
                Toast.LENGTH_SHORT
            ).show()
        }
        return view
    }
}
