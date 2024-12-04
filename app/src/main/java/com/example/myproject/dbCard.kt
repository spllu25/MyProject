package com.example.myproject

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

data class Card(
    val id: Int,
    val title: String,
    val txt: String,
    val img: String,
    var isFav: Boolean,
    var isPurch: Boolean,
    var isOrdered: Boolean,
    var quantityPurch: Int,
    val date: String
)

class dbCard(context: Context) : SQLiteOpenHelper(context, "cards.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
        CREATE TABLE cards (
    id INTEGER PRIMARY KEY,
    userId INTEGER NOT NULL,
    title TEXT NOT NULL,
    description TEXT NOT NULL,
    image TEXT NOT NULL,
    isFav INTEGER NOT NULL,
    isPurch INTEGER NOT NULL,
    isOrdered INTEGER NOT NULL DEFAULT 0, 
    quantity INTEGER NOT NULL DEFAULT 0,
    date TEXT, 
    FOREIGN KEY(userId) REFERENCES users(id)
)

    """.trimIndent()
        db?.execSQL(createTableQuery)
    }


    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS cards")
        onCreate(db)
    }

    suspend fun saveCard(card: Card, userId: Int) = withContext(Dispatchers.IO) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id", generateCardId(userId, card.id))
            put("title", card.title)
            put("description", card.txt)
            put("image", card.img)
            put("isFav", if (card.isFav) 1 else 0)
            put("isPurch", if (card.isPurch) 1 else 0)
            put("quantity", card.quantityPurch)
            put("date", card.date)
            put("userId", userId)
        }
        db.insertOrThrow("cards", null, values)
        db.close()
    }

    suspend fun saveOrder(
        userId: Int,
        clientName: String,
        address: String,
        date: String,
        totalCost: String,
        cards: List<Card>
    ) = withContext(Dispatchers.IO) {
        val db = writableDatabase
        cards.forEach { card ->
            val values = ContentValues().apply {
                put("id", generateCardId(userId, card.id + 1))
                put("title", card.title)
                put("description", card.txt)
                put("image", card.img)
                put("isFav", 0)
                put("isPurch", 0)
                put("isOrdered", 1)
                put("quantity", card.quantityPurch)
                put("date", date)
                put("userId", userId)
            }
            db.insertOrThrow("cards", null, values)
        }
        db.close()
    }


    fun generateCardId(userId: Int, localId: Int): Int {
        return userId * 10000 + localId
    }

    suspend fun loadCards(userId: Int): List<Card> = withContext(Dispatchers.IO) {
        val db = readableDatabase
        val cursor = db.query(
            "cards",
            null,
            "userId = ?",
            arrayOf(userId.toString()),
            null,
            null,
            null
        )
        val cards = mutableListOf<Card>()
        while (cursor.moveToNext()) {
            val card = Card(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
                txt = cursor.getString(cursor.getColumnIndexOrThrow("description")),
                img = cursor.getString(cursor.getColumnIndexOrThrow("image")),
                isFav = cursor.getInt(cursor.getColumnIndexOrThrow("isFav")) == 1,
                isPurch = cursor.getInt(cursor.getColumnIndexOrThrow("isPurch")) == 1,
                isOrdered = cursor.getInt(cursor.getColumnIndexOrThrow("isOrdered")) == 1,
                quantityPurch = cursor.getInt(cursor.getColumnIndexOrThrow("quantity")),
                date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
            )
            cards.add(card)
        }
        cursor.close()
        db.close()
        return@withContext cards
    }

    suspend fun clearCards(userId: Int) = withContext(Dispatchers.IO) {
        val db = writableDatabase
        val rangeStart = userId * 10000
        val rangeEnd = rangeStart + 9999
        db.delete("cards", "id BETWEEN ? AND ?", arrayOf(rangeStart.toString(), rangeEnd.toString()))
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

