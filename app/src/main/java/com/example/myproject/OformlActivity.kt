package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


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

        lifecycleScope.launch {
            val user = withContext(Dispatchers.IO) { db.getUserById(userId) }
            user?.let {
                client.setText(user.surname)
            } ?: Toast.makeText(this@OformlActivity, "Пользователь не найден", Toast.LENGTH_SHORT).show()
        }

        buttonBuy.setOnClickListener {
            val inputDate = date.text.toString()
            val currentDate = Calendar.getInstance()
            currentDate.add(Calendar.DAY_OF_YEAR, 1)
            val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            try {
                val parsedDate = formatter.parse(inputDate)
                if (parsedDate != null && parsedDate.before(currentDate.time)) {
                    Toast.makeText(this, "Дата должна быть не раньше завтрашнего дня!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            } catch (e: ParseException) {
                Toast.makeText(this, "Введите корректную дату в формате yyyy-MM-dd", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    val originalCards = chooseManager.dbHelper?.loadCards(userId)?.filter { it.isPurch } ?: listOf()
                    val updatedCards = originalCards.map { originalCard ->
                        originalCard.copy(
                            id = chooseManager.dbHelper!!.generateCardId(userId, originalCard.id + 1),
                            isOrdered = true,
                            date = inputDate
                        )
                    }

                    updatedCards.forEach { newCard ->
                        chooseManager.dbHelper?.saveCard(newCard, userId)
                    }

                    originalCards.forEach { originalCard ->
                        chooseManager.dbHelper?.updateIsPurch(originalCard.id, false)
                    }

                    chooseManager.dbHelper?.saveOrder(
                        userId, client.text.toString(), address.text.toString(),
                        inputDate, cost.text.toString(), updatedCards
                    )
                }

                Toast.makeText(this@OformlActivity, "Заказ оформлен!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@OformlActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}



