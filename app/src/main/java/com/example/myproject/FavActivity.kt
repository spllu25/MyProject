package com.example.myproject

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FavActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_fav)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main1)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val favoritesTable: LinearLayout = findViewById(R.id.main1)

        // Получаем данные из Intent
        val cardText = intent.getStringExtra("card_text")
        val cardImageResId = intent.getIntExtra("card_image", -1)

        // Проверяем, что данные переданы
        if (cardText != null && cardImageResId != -1) {
            val card = Card.createCard(this, cardText, cardImageResId)
            favoritesTable.addView(card)
        }
    }
}
