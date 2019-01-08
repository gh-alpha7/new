package com.example.alpha.parkit

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*
import android.Manifest;
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener
import android.location.LocationManager
import android.os.AsyncTask
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.requestPermissions
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log
import android.widget.Toast;
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.alpha.parkit.R.id.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList
import java.util.HashMap

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback,LocationListener {


    private lateinit var mMap: GoogleMap
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationManager: LocationManager? = null
    private val MIN_TIME: Long = 400
    private val MIN_DISTANCE = 1000f
    private var curLatLng:LatLng?=null
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        bookFloat.visibility = View.GONE
//
//        fab.setOnClickListener { view ->
//            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                .setAction("Action", null).show()
//        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        //View map_holder =  findViewById (R.id.map_holder);
        //View map = map_holder.findViewBId (R.id.map)

        nav_view.setNavigationItemSelectedListener(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val autocompleteFragment =
            fragmentManager.findFragmentById(place_autocomplete_fragment) as PlaceAutocompleteFragment




        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
//                mMap.clear()
                mMap.addMarker(MarkerOptions().position(place.latLng).title(place.name.toString()))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(place.latLng))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.latLng, 12.0f))
            }

            override fun onError(status: Status) {

            }
        })
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?;
        locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER


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
            R.id.aboutus -> {

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
                val intent = Intent(this, PaymentActivity::class.java).apply {

                }
                startActivity(intent)

            }
            R.id.signout -> {

            }
            R.id.maps_link -> {
                val i = Intent(this, MapsActivity::class.java)
                //val intent = Intent(this, maps::class.java)
                startActivity(i)
            }
            R.id.nav_share -> {
                try {
                    val i = Intent(Intent.ACTION_SEND)
                    i.type = "text/plain"
                    i.putExtra(Intent.EXTRA_SUBJECT, "My application name")
                    var sAux = "\nLet me recommend you this application\n\n"
                    sAux = sAux + "https://play.google.com/store/apps/details?id=the.package.id \n\n"
                    i.putExtra(Intent.EXTRA_TEXT, sAux)
                    startActivity(Intent.createChooser(i, "choose one"))
                } catch (e: Exception) {
                    //e.toString();
                }


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
    fun qrcodeclick(view: View){
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
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                val contents = data!!.getStringExtra("SCAN_RESULT")
                Toast.makeText(this, "scanned", Toast.LENGTH_SHORT).show()// display toast
            }
            if (resultCode == RESULT_CANCELED) {
                //handle cancel
            }
        }
    }
    private val onMyLocationButtonClickListener = GoogleMap.OnMyLocationButtonClickListener {
        mMap.setMinZoomPreference(15f)
        false
    }

    private val onMyLocationClickListener = GoogleMap.OnMyLocationClickListener { location ->
        mMap.setMinZoomPreference(12f)

        val circleOptions = CircleOptions()
        circleOptions.center(
            LatLng(
                location.latitude,
                location.longitude
            )
        )
//
//        circleOptions.radius(200.0)
//        circleOptions.fillColor(Color.RED)
//        circleOptions.strokeWidth(6f)
//
//        mMap.addCircle(circleOptions)
//        mMap.setMinZoomPreference(15f)
    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocationIfPermitted()
                } else {
                    showDefaultLocation()
                }
                return
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        enableMyLocationIfPermitted()
        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener)
        mMap.setOnMyLocationClickListener(onMyLocationClickListener)

//        mMap.setOnMapLongClickListener {
//
////            mMap.clear()
//            mMap.addMarker(MarkerOptions().position(it)
//                .draggable(true))
//        }



        mMap.setOnMarkerClickListener { marker ->
            print(marker.id)
            // if marker source is clicked
            Toast.makeText(this, marker.position.toString(), Toast.LENGTH_SHORT).show()// display toast

            bookFloat.visibility = View.VISIBLE

            var origin:LatLng = this!!.curLatLng!!;
            var dest:LatLng = marker.position;

            // Getting URL to the Google Directions API
            var url:String = getDirectionsUrl(origin, dest)
            print(url)
            var downloadTask =  DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);



            true
        }
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setMinZoomPreference(11F)
        showDefaultLocation()


    }




    private fun enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else if (mMap != null) {
            mMap.isMyLocationEnabled = true
        }
    }

    private fun showDefaultLocation() {

        val bangalore = LatLng(12.9716, 77.5946)
        val ban1=LatLng(12.98,77.1)
        mMap.setMinZoomPreference(15f)
//        mMap.addMarker(MarkerOptions().position(bangalore).title("Marker in Bangalore"))
//        mMap.addMarker(MarkerOptions()
//            .position(bangalore)
//            .icon(BitmapDescriptorFactory.fromResource(R.drawable.spaceimage)))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(bangalore))




    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun onProviderDisabled(provider: String?) {

    }

    override fun onLocationChanged(location: Location?) {
        curLatLng =  LatLng(location!!.getLatitude(), location.getLongitude());
        var cameraUpdate = CameraUpdateFactory.newLatLngZoom(curLatLng, 10F);
        mMap.animateCamera(cameraUpdate);
    }

    private fun getDirectionsUrl(origin: LatLng, dest: LatLng): String {

        // Origin of route
        val str_origin = "origin=" + origin.latitude + "," + origin.longitude

        // Destination of route
        val str_dest = "destination=" + dest.latitude + "," + dest.longitude

        // Sensor enabled
        val sensor = "sensor=false"
        val api_key="key=AIzaSyAoncE3HjFoXY3gJZucv4yPfQuWXCSya58"
        // Building the parameters to the web service
        val parameters = "$str_origin&$str_dest&$sensor&$api_key"

        // Output format
        val output = "json"

        // Building the url to the web service

        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters"
    }

    /** A method to download json data from url  */
    @Throws(IOException::class)
    private fun downloadUrl(strUrl: String): String {
        var data = ""
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null
        val directionsRequest = object : StringRequest(
            Request.Method.GET, strUrl, Response.Listener<String> {
                response ->
            data=response
            // Get routes

        }, Response.ErrorListener {
                _ ->
        }){}
        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(directionsRequest)
        return data
    }

    // Fetches data from url passed
    private inner class DownloadTask : AsyncTask<String, Void, String>() {

        // Downloading data in non-ui thread
        override fun doInBackground(vararg url: String): String {

            // For storing data from web service
            var data = ""

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0])
            } catch (e: Exception) {
                Log.d("Background Task", e.toString())
            }

            return data
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        override fun onPostExecute(result: String) {
            super.onPostExecute(result)

            val parserTask = ParserTask()

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result)
        }
    }

    /** A class to parse the Google Places in JSON format  */
    private inner class ParserTask : AsyncTask<String, Int, List<List<HashMap<String, String>>>>() {

        // Parsing the data in non-ui thread
        override fun doInBackground(vararg jsonData: String): List<List<HashMap<String, String>>>? {

            val jObject: JSONObject
            var routes: List<List<HashMap<String, String>>>? = null

            try {

                jObject = JSONObject(jsonData[0])!!
                val parser = DirectionsJSONParser()!!
                print(jObject)
                // Starts parsing data
                routes = parser.parse(jObject)
            } catch (e: Exception) {
                e.printStackTrace()

            }

            return routes
        }

        // Executes in UI thread, after the parsing process
        override fun onPostExecute(result: List<List<HashMap<String, String>>>) {
            var points: ArrayList<LatLng>? = null
            var lineOptions: PolylineOptions? = null
            val markerOptions = MarkerOptions()

            // Traversing through all the routes
            for (i in result.indices) {
                points = ArrayList()
                lineOptions = PolylineOptions()

                // Fetching i-th route
                val path = result[i]

                // Fetching all the points in i-th route
                for (j in path.indices) {
                    val point = path[j]

                    val lat = java.lang.Double.parseDouble(point["lat"])
                    val lng = java.lang.Double.parseDouble(point["lng"])
                    val position = LatLng(lat, lng)

                    points.add(position)
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points)
                lineOptions.width(10f)
                lineOptions.color(Color.rgb(93, 173, 226))
            }

            // Drawing polyline in the Google Map for the i-th route
            mMap!!.addPolyline(lineOptions)
        }
    }



}
