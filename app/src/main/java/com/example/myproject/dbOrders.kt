package com.example.myproject

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class dbOrders(context: Context) : SQLiteOpenHelper(context, "orders.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = """
        CREATE TABLE orders (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            userId INTEGER NOT NULL,
            clientName TEXT NOT NULL,
            address TEXT NOT NULL,
            date TEXT NOT NULL,
            FOREIGN KEY(userId) REFERENCES users(id)
        )
        """.trimIndent()
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS orders")
        onCreate(db)
    }

    suspend fun saveOrder(userId: Int, clientName: String, address: String, date: String) = withContext(Dispatchers.IO) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("userId", userId)
            put("clientName", clientName)
            put("address", address)
            put("date", date)
        }
        db.insertOrThrow("orders", null, values)
        db.close()
    }


    suspend fun loadOrders(userId: Int): List<Order> = withContext(Dispatchers.IO) {
        val db = readableDatabase
        val cursor = db.query(
            "orders",
            null,
            "userId = ?",
            arrayOf(userId.toString()),
            null,
            null,
            null
        )
        val orders = mutableListOf<Order>()
        while (cursor.moveToNext()) {
            val order = Order(
                id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                userId = cursor.getInt(cursor.getColumnIndexOrThrow("userId")),
                clientName = cursor.getString(cursor.getColumnIndexOrThrow("clientName")),
                address = cursor.getString(cursor.getColumnIndexOrThrow("address")),
                date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
            )
            orders.add(order)
        }
        cursor.close()
        db.close()
        return@withContext orders
    }
}

data class Order(
    val id: Int,
    val userId: Int,
    val clientName: String,
    val address: String,
    val date: String
)

