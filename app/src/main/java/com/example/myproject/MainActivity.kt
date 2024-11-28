package com.example.myproject

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userId = intent.getIntExtra("userId", -1)
        Log.e("MyFragment", "Received User ID: $userId")
        if (chooseManager.dbHelper == null) {
            chooseManager.initialize(this, userId)
        }

        replaceFragment(HomeFragment())

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            val fragment = when (menuItem.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_favorites -> FavFragment()
                R.id.nav_new_order -> NewOrderFragment()
                R.id.nav_cart -> CartFragment()
                R.id.nav_profile -> ProfileFragment()
                else -> null
            }
            fragment?.let { replaceFragment(it) }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.IO) {
                chooseManager.saveCards()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
        }
    }
    fun getUserId(): Int {
        return intent.getIntExtra("userId", -1)
    }
}

