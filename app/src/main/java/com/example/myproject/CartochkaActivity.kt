package com.example.myproject

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartochkaActivity : AppCompatActivity() {
    private lateinit var titleView: TextView
    private lateinit var descriptionView: TextView
    private lateinit var imageView: ImageView
    private lateinit var favButton: Button
    private lateinit var cartButton: Button

    private var cardId: Int = -1
    private var isFav: Boolean = false
    private var isPurch: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cartochka)

        titleView = findViewById(R.id.cardTitle)
        descriptionView = findViewById(R.id.cardDescription)
        imageView = findViewById(R.id.cardImage)
        favButton = findViewById(R.id.buttonFav)
        cartButton = findViewById(R.id.buttonCart)

        val cardTitle = intent.getStringExtra("cardTitle")
        val cardDescription = intent.getStringExtra("cardDescription")
        val cardImage = intent.getStringExtra("cardImage")
        cardId = intent.getIntExtra("cardId", -1)
        isFav = intent.getBooleanExtra("isFav", false)
        isPurch = intent.getBooleanExtra("isPurch", false)

        titleView.text = cardTitle
        descriptionView.text = cardDescription
        cardImage?.let {
            val imageRes = resources.getIdentifier(it, "drawable", packageName)
            imageView.setImageResource(imageRes)
        }

        updateButtons()

        favButton.setOnClickListener {
            isFav = !isFav
            updateButtons()
            CoroutineScope(Dispatchers.IO).launch {
                chooseManager.dbHelper?.updateIsFav(cardId, isFav)
            }
        }

        cartButton.setOnClickListener {
            isPurch = !isPurch
            updateButtons()
            CoroutineScope(Dispatchers.IO).launch {
                chooseManager.dbHelper?.updateIsPurch(cardId, isPurch)
            }
        }
    }

    private fun updateButtons() {
        favButton.text = if (isFav) "★" else "☆"
        cartButton.text = if (isPurch) "В корзине" else "Добавить в корзину"
    }
}
