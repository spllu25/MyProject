package com.example.myproject


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

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

            if (login == "" || pass == "")
                Toast.makeText(this,"Заполните все поля",Toast.LENGTH_SHORT).show()
            else {
                val db= dbHelp(this,null)
                val isEntr= db.getUser(login,pass)
                if (isEntr){
                    userLogin.text.clear()
                    userPass.text.clear()
                    val intent= Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                else
                    Toast.makeText(this,"Неверно",Toast.LENGTH_SHORT).show()
            }
        }


        val buttonSignup: Button = findViewById(R.id.buttonSignup)
        buttonSignup.setOnClickListener{
            val intent= Intent(this, RegActivity::class.java)
            startActivity(intent)
        }
    }
}