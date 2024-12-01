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


class ProfilActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)
        val userId = intent.getIntExtra("userId", -1)

        val userSurname: EditText = findViewById(R.id.surname)
        val userName: EditText = findViewById(R.id.name)
        val userDadname: EditText = findViewById(R.id.dadname)
        val userNumb: EditText = findViewById(R.id.login)
        val userPass: EditText = findViewById(R.id.pass)

        val db = dbUser(this, null)

        CoroutineScope(Dispatchers.Main).launch {
            val user = db.getUserById(userId)
            userSurname.setText(user!!.surname)
            userName.setText(user.name)
            userDadname.setText(user.dadname)
            userNumb.setText(user.login)
            userPass.setText(user.pass)
        }

        val saveButton: Button = findViewById(R.id.buttonSave)
        saveButton.setOnClickListener {
            val updatedUser = User(
                name = userName.text.toString(),
                surname = userSurname.text.toString(),
                dadname = userDadname.text.toString(),
                login = userNumb.text.toString(),
                pass = userPass.text.toString()
            )

            CoroutineScope(Dispatchers.Main).launch {
                db.updateUser(updatedUser,userId)
                Toast.makeText(this@ProfilActivity, "Данные сохранены!", Toast.LENGTH_SHORT).show()
            }
            val intent = Intent(this@ProfilActivity, MainActivity::class.java)
            intent.putExtra("navigateToProfile", true)
            startActivity(intent)
            finish()
        }

        val logoutButton: Button = findViewById(R.id.buttonLogout)
        logoutButton.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
        }
    }
}
