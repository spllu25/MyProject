package com.example.myproject
import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

object Card {
        fun createCard(context: Context, text: String, imageResId: Int): LinearLayout {
            val cardLayout = LinearLayout(context)
            cardLayout.orientation = LinearLayout.HORIZONTAL
            cardLayout.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            cardLayout.setPadding(16, 16, 16, 16)

            val imageView = ImageView(context)
            imageView.setImageResource(imageResId)
            imageView.layoutParams = LinearLayout.LayoutParams(200, 200)
            cardLayout.addView(imageView)

            val textView = TextView(context)
            textView.text = text
            textView.setPadding(16, 0, 0, 0)
            textView.textSize = 16f
            textView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            cardLayout.addView(textView)

            return cardLayout
        }
    }

