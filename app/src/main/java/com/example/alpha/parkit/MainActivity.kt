package com.example.alpha.parkit

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.alpha.parkit.R.id.image
import com.example.alpha.parkit.R.id.imageView
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        //View map_holder =  findViewById (R.id.map_holder);
        //View map = map_holder.findViewBId (R.id.map)

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.qrscan -> {
                try {

                    val intent = Intent("com.google.zxing.client.android.SCAN")
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE") // "PRODUCT_MODE for bar codes
                    intent.putExtra("PROMPT_MESSAGE", "Point the camera at the code")
                    intent.putExtra("SCAN_CAMERA_ID", 1)
                    startActivityForResult(intent, 0)

                } catch (e: Exception) {

                    var marketUri = Uri.parse("market://details?id=com.google.zxing.client.android")
                    val marketIntent = Intent(Intent.ACTION_VIEW, marketUri)
                    startActivity(marketIntent)

                }
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {

            R.id.history -> {
                val intent = Intent(this, HistoryActivity::class.java).apply {

                }
                startActivity(intent)
            }
            R.id.qr -> {

            }
            R.id.signout -> {

            }
            R.id.maps_link -> {
                val i = Intent(this, maps::class.java)
                //val intent = Intent(this, maps::class.java)
                startActivity(i)
            }
            R.id.nav_share -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun profileImageClick(view: View) {

        val intent = Intent(this, ProfileActivity::class.java).apply {
            putExtra("name", "Subham Singh")
            putExtra("email", "shubhamsngh067@gmail.com")
            putExtra("phNo", "8862944302")
            putExtra("pass", "******")
        }
        startActivity(intent)

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                val contents = data!!.getStringExtra("SCAN_RESULT")
            }
            if (resultCode == RESULT_CANCELED) {
                //handle cancel
            }
        }
    }


}
