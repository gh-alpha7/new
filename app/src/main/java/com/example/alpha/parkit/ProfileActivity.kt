package com.example.alpha.parkit

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("Profile")
        setContentView(R.layout.activity_profile)
        var fname= findViewById<TextView>(R.id.firstName)
        var email=findViewById<TextView>(R.id.email)
        var sname=findViewById<TextView>(R.id.lastName)
        var phno=findViewById<TextView>(R.id.phone)
        var name=intent.getStringExtra("name")
        var fname1=name.split("\\s".toRegex())[0]
        var sname1:String?="***"
        if(name.split("\\s".toRegex()).size==2) {
            sname1 = name.split("\\s".toRegex())[1]
        }
        var profilePic=findViewById<TextView>(R.id.profilePic)
        profilePic.text=Character.toString(name[0])
        email.text=intent.getStringExtra("email")
        phno.text = intent.getStringExtra("phNo")
        fname.text=fname1
        sname.text=sname1
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
