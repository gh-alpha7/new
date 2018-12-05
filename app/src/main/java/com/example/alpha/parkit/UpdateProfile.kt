package com.example.alpha.parkit

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_update_profile.*

class UpdateProfile : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profile)
        setTitle("Profile");

        updateView.text = intent.getStringExtra("view")
        updateText.setText(intent.getStringExtra("text"))



    }
}
