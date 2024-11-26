package com.example.myproject

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class dbCard(context: Context) : SQLiteOpenHelper(context, "cards.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {

        val createTableQuery = """
            CREATE TABLE cards (
                id INTEGER PRIMARY KEY,
                title TEXT NOT NULL,
                description TEXT NOT NULL,
                image TEXT NOT NULL,
                isFav INTEGER NOT NULL,
                isPurch INTEGER NOT NULL,
                quantity INTEGER NOT NULL DEFAULT 0
            )
        """.trimIndent()
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS cards")
        onCreate(db)
    }

    suspend fun saveCard(card: Card) = withContext(Dispatchers.IO) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id", card.id)
            put("title", card.title)
            put("description", card.txt)
            put("image", card.img)
            put("isFav", if (card.isFav) 1 else 0)
            put("isPurch", if (card.isPurch) 1 else 0)
            put("quantity", card.quantity)
        }
        db.insertWithOnConflict("cards", null, values, SQLiteDatabase.CONFLICT_REPLACE)
        db.close()
    }

    suspend fun loadCards(): List<Card> = withContext(Dispatchers.IO) {
        val db = readableDatabase
        val cursor = db.query("cards", null, null, null, null, null, null)
        val cards = mutableListOf<Card>()
        while (cursor.moveToNext()) {
            val card = Card(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
                txt = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                img = cursor.getString(cursor.getColumnIndexOrThrow("image")),
                isFav = cursor.getInt(cursor.getColumnIndexOrThrow("isFav")) == 1,
                isPurch = cursor.getInt(cursor.getColumnIndexOrThrow("isPurch")) == 1,
                quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"))
            )
            cards.add(card)
        }
        cursor.close()
        db.close()
        return@withContext cards
    }

    suspend fun clearCards() = withContext(Dispatchers.IO) {
        val db = writableDatabase
        db.execSQL("DELETE FROM cards")
        db.close()
    }

    suspend fun updateIsFav(cardId: Int, isFav: Boolean) = withContext(Dispatchers.IO) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("isFav", if (isFav) 1 else 0)
        }
        db.update("cards", values, "id=?", arrayOf(cardId.toString()))
        db.close()
    }

    suspend fun updateIsPurch(cardId: Int, isPurch: Boolean) = withContext(Dispatchers.IO) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("isPurch", if (isPurch) 1 else 0)
        }
        db.update("cards", values, "id=?", arrayOf(cardId.toString()))
        db.close()
    }

    suspend fun updateQuantity(cardId: Int, quantity: Int) = withContext(Dispatchers.IO) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("quantity", quantity)
        }
        db.update("cards", values, "id=?", arrayOf(cardId.toString()))
        db.close()
    }
}