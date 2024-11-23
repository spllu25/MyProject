package com.example.myproject

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ProfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        val userSurname: EditText = findViewById(R.id.surname)
        val userName: EditText = findViewById(R.id.name)
        val userDadname: EditText = findViewById(R.id.dadname)
        val userNumb: EditText = findViewById(R.id.login)
        val userPass: EditText = findViewById(R.id.pass)

        val sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
        userSurname.setText(sharedPreferences.getString("surname", ""))
        userName.setText(sharedPreferences.getString("name", ""))
        userDadname.setText(sharedPreferences.getString("dadname", ""))
        userNumb.setText(sharedPreferences.getString("login", ""))
        userPass.setText(sharedPreferences.getString("pass", ""))
    }

}