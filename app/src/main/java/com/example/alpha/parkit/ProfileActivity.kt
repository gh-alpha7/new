package com.example.alpha.parkit

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Profile")
        setContentView(R.layout.activity_profile)
        val name = intent.getStringExtra("name")
    }

    fun emailClick(view: View){
        email.text
        val intent = Intent(this,UpdateProfile::class.java).apply {
            putExtra("view","Email")
            putExtra("text",email.text)
        }
        startActivity(intent)
    }

    fun lastNameClick(view: View){
        val intent = Intent(this,UpdateProfile::class.java).apply {
            putExtra("view","Last Name")
            putExtra("text",lastName.text)
        }
        startActivity(intent)
    }
    fun firstNameClick(view: View){
        val intent = Intent(this,UpdateProfile::class.java).apply {
            putExtra("view","First Name")
            putExtra("text",firstName.text)
        }
        startActivity(intent)
    }
    fun phNoClick(view: View){
        val intent = Intent(this,UpdateProfile::class.java).apply {
            putExtra("view","Phone")
            putExtra("text",phone.text)
        }
        startActivity(intent)
    }
}
