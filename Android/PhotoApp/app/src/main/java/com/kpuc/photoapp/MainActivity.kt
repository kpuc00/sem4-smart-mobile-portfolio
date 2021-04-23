package com.kpuc.photoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kpuc.photoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Welcome back!"

        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)
        b.btnLogin.setOnClickListener {
            if (b.etEmail.text.trim()
                    .isNotEmpty() && b.etPassword.text.trim().isNotEmpty()
            ) {
                Toast.makeText(this, "Login clicked", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Input required", Toast.LENGTH_LONG).show()
            }
        }
        b.tvNotRegistered.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}