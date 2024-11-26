package com.example.myproject

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class dbUser(val context: Context, val factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, "app", factory, 1) {

    override fun onCreate(db: SQLiteDatabase?) {
        val userTable = """
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                surname TEXT NOT NULL,
                name TEXT NOT NULL,
                dadname TEXT NOT NULL,
                login TEXT NOT NULL UNIQUE,
                password TEXT NOT NULL
            )
        """.trimIndent()

        val ordersTable = """
            CREATE TABLE orders (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                order_details TEXT,
                FOREIGN KEY(user_id) REFERENCES users(id)
            )
        """.trimIndent()

        val favoritesTable = """
            CREATE TABLE favorites (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER,
                item_details TEXT,
                FOREIGN KEY(user_id) REFERENCES users(id)
            )
        """.trimIndent()

        db?.execSQL(userTable)
        db?.execSQL(ordersTable)
        db?.execSQL(favoritesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS users")
        db?.execSQL("DROP TABLE IF EXISTS orders")
        db?.execSQL("DROP TABLE IF EXISTS favorites")
        onCreate(db)
    }

    suspend fun addUser(user: User): Boolean = withContext(Dispatchers.IO) {
        if (isUser(user.login)) return@withContext false
        val values = ContentValues().apply {
            put("surname", user.surname)
            put("name", user.name)
            put("dadname", user.dadname)
            put("login", user.login)
            put("password", user.pass)
        }
        val db = writableDatabase
        db.insert("users", null, values)
        db.close()
        return@withContext true
    }

    suspend fun isUser(login: String): Boolean = withContext(Dispatchers.IO) {
        val db = readableDatabase
        val query = db.rawQuery("SELECT * FROM users WHERE login=?", arrayOf(login))
        val result = query.moveToFirst()
        query.close()
        db.close()
        return@withContext result
    }

    suspend fun getUser(login: String, pass: String): User? = withContext(Dispatchers.IO) {
        val db = readableDatabase
        val query = db.rawQuery("SELECT * FROM users WHERE login=? AND password=?", arrayOf(login, pass))
        if (query.moveToFirst()) {
            val user = User(
                query.getString(query.getColumnIndexOrThrow("surname")),
                query.getString(query.getColumnIndexOrThrow("name")),
                query.getString(query.getColumnIndexOrThrow("dadname")),
                query.getString(query.getColumnIndexOrThrow("login")),
                query.getString(query.getColumnIndexOrThrow("password"))
            )
            query.close()
            db.close()
            return@withContext user
        }
        query.close()
        db.close()
        return@withContext null
    }

}
