package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfilActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        val userSurname: EditText = findViewById(R.id.surname)
        val userName: EditText = findViewById(R.id.name)
        val userDadname: EditText = findViewById(R.id.dadname)
        val userNumb: EditText = findViewById(R.id.login)
        val userPass: EditText = findViewById(R.id.pass)

        var sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
        userSurname.setText(sharedPreferences.getString("surname", ""))
        userName.setText(sharedPreferences.getString("name", ""))
        userDadname.setText(sharedPreferences.getString("dadname", ""))
        userNumb.setText(sharedPreferences.getString("login", ""))
        userPass.setText(sharedPreferences.getString("pass", ""))

        val saveButton: Button = findViewById(R.id.buttonSave)
        saveButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val logoutButton: Button = findViewById(R.id.buttonLogout)
        logoutButton.setOnClickListener {

            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}