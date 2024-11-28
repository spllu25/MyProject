package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProfilActivity : AppCompatActivity() {
    //private var userId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)
        val userId = intent.getIntExtra("userId", -1)

        Log.e("ProfilActivity", "Received User ID: $userId")

        val userSurname: EditText = findViewById(R.id.surname)
        val userName: EditText = findViewById(R.id.name)
        val userDadname: EditText = findViewById(R.id.dadname)
        val userNumb: EditText = findViewById(R.id.login)
        val userPass: EditText = findViewById(R.id.pass)

        val sharedPreferences = getSharedPreferences("UserData_$userId", MODE_PRIVATE)

        userSurname.setText(sharedPreferences.getString("surname", ""))
        userName.setText(sharedPreferences.getString("name", ""))
        userDadname.setText(sharedPreferences.getString("dadname", ""))
        userNumb.setText(sharedPreferences.getString("login", ""))
        userPass.setText(sharedPreferences.getString("pass", ""))

        val saveButton: Button = findViewById(R.id.buttonSave)
        saveButton.setOnClickListener {
            sharedPreferences.edit()
                .putString("surname", userSurname.text.toString())
                .putString("name", userName.text.toString())
                .putString("dadname", userDadname.text.toString())
                .putString("login", userNumb.text.toString())
                .putString("pass", userPass.text.toString())
                .apply()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val logoutButton: Button = findViewById(R.id.buttonLogout)
        logoutButton.setOnClickListener {
            sharedPreferences.edit().clear().apply()
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }
    }
}
