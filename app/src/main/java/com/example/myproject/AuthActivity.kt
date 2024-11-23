package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        val userLogin: EditText = findViewById(R.id.login)
        val userPass: EditText = findViewById(R.id.password)
        val buttonLogin: Button = findViewById(R.id.buttonSignin)

        buttonLogin.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val pass = userPass.text.toString().trim()

            if (login.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                val db = dbHelp(this, null)

                CoroutineScope(Dispatchers.Main).launch {
                    val user = db.getUser(login, pass)
                    if (user != null) {
                        val sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE)
                        with(sharedPreferences.edit()) {
                            putString("surname", user.surname)
                            putString("name", user.name)
                            putString("dadname", user.dadname)
                            putString("login", user.login)
                            putString("pass", user.pass)
                            apply()
                        }
                        val intent = Intent(this@AuthActivity, ProfilActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@AuthActivity, "Неверно", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        val buttonSignup: Button = findViewById(R.id.buttonSignup)
        buttonSignup.setOnClickListener {
            val intent = Intent(this, RegActivity::class.java)
            startActivity(intent)
        }
    }
}
