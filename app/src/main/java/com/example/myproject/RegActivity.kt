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

class RegActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reg)

        val userSurname: EditText = findViewById(R.id.surname)
        val userName: EditText = findViewById(R.id.name)
        val userDadname: EditText = findViewById(R.id.dadname)
        val userNumb: EditText = findViewById(R.id.numbPhone)
        val userPass: EditText = findViewById(R.id.passReg)
        val buttonReg: Button = findViewById(R.id.buttonRegister)
        val buttonLogin: Button = findViewById(R.id.buttonLogin)

        buttonReg.setOnClickListener {
            val surname = userSurname.text.toString().trim()
            val name = userName.text.toString().trim()
            val dadname = userDadname.text.toString().trim()
            val login = userNumb.text.toString().trim()
            val pass = userPass.text.toString().trim()

            if (surname.isEmpty() || name.isEmpty() || dadname.isEmpty() || login.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                val user = User(surname, name, dadname, login, pass)
                val db = dbUser(this, null)
                CoroutineScope(Dispatchers.Main).launch {
                    val isAdded = db.addUser(user)
                    if (isAdded) {
                        Toast.makeText(this@RegActivity, "$login добавлен", Toast.LENGTH_SHORT).show()
                        userSurname.text.clear()
                        userName.text.clear()
                        userDadname.text.clear()
                        userNumb.text.clear()
                        userPass.text.clear()
                    } else {
                        Toast.makeText(this@RegActivity, "$login уже используется", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        buttonLogin.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }
    }
}
