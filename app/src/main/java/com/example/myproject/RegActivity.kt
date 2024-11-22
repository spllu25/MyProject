package com.example.myproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class RegActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reg)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets }

        val userSurname: EditText = findViewById(R.id.surname)
        val userName: EditText = findViewById(R.id.name)
        val userDadname: EditText = findViewById(R.id.dadname)
        val userNumb: EditText = findViewById(R.id.numbPhone)
        val userPass: EditText = findViewById(R.id.passReg)
        val buttonReg: Button = findViewById(R.id.buttonRegister)

        buttonReg.setOnClickListener {
            val surname = userSurname.text.toString().trim()
            val name = userName.text.toString().trim()
            val dadname = userDadname.text.toString().trim()
            val login = userNumb.text.toString().trim()
            val pass = userPass.text.toString().trim()

            if (surname == "" || name == "" || dadname == "" || login == "" || pass == "")
                Toast.makeText(this,"Заполните все поля", Toast.LENGTH_SHORT).show()
            else {
                val user = User(login,pass)
                val db= dbHelp(this,null)
                db.addUser(user)
                Toast.makeText(this,"$login добавлен", Toast.LENGTH_SHORT).show()

                userSurname.text.clear()
                userName.text.clear()
                userDadname.text.clear()
                userNumb.text.clear()
                userPass.text.clear()
            }
        }

        val buttonLog: Button = findViewById(R.id.buttonLogin)
        buttonLog.setOnClickListener{
            val intent= Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }
    }
}
