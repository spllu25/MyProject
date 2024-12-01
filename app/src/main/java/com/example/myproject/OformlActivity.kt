package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class OformlActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oforml)

        val stoimost = intent.getIntExtra("cost", -1)
        val userId = intent.getIntExtra("userId", -1)

        val client: EditText = findViewById(R.id.poluchatel)
        val address: EditText = findViewById(R.id.address)
        val date: EditText = findViewById(R.id.date)
        val cost: TextView = findViewById(R.id.cost)
        val buttonBuy: Button = findViewById(R.id.buttonBuy)
        cost.text = stoimost.toString()

        val db = dbUser(this, null)
        val dbOrd = dbCard(this)

        CoroutineScope(Dispatchers.Main).launch {
            val user = withContext(Dispatchers.IO) { db.getUserById(userId) }
            if (user != null) { client.setText(user.surname) }
            else {
                Toast.makeText(this@OformlActivity, "Пользователь не найден", Toast.LENGTH_SHORT).show()
            }
        }

        buttonBuy.setOnClickListener {
            if (client.text.isNullOrEmpty() || address.text.isNullOrEmpty() || date.text.isNullOrEmpty()) {
                Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_SHORT).show()
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    withContext(Dispatchers.IO) {
                        val cards=chooseManager.dbHelper?.loadCards(userId)?.filter { it.isPurch }?: listOf()
                        dbOrd.saveOrder(userId, client.text.toString(), address.text.toString(), date.text.toString(),cost.text.toString(),cards)

                        cards.forEach { card ->
                            chooseManager.dbHelper?.updateIsPurch(card.id, false)
                            chooseManager.dbHelper?.updateIsOrdered(card.id, true)
                        }
                    }

                    Toast.makeText(this@OformlActivity, "Заказ оформлен!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@OformlActivity, MainActivity::class.java)
                    intent.putExtra("navigateToProfile", true)
                    startActivity(intent)
                    finish()
                }
            }
        }

    }
}


