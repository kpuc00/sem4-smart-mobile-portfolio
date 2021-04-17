package com.kpuc.photoapp

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kpuc.photoapp.databinding.ActivityRegisterBinding


class RegisterActivity : AppCompatActivity() {
    private lateinit var b: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Register"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        b = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.btnRegister.setOnClickListener {
            if (b.etEmail.text.trim()
                    .isNotEmpty() && b.etFullname.text.trim()
                    .isNotEmpty() && b.etPassword.text.trim()
                    .isNotEmpty() && b.etConfirmPassword.text.trim().isNotEmpty()
            ) {
                Toast.makeText(this, "Register clicked", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Input required", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun registerUser(){
        val mPrefs = getPreferences(MODE_PRIVATE)

    }

    //back button
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}