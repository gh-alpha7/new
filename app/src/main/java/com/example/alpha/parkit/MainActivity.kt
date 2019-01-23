package com.example.alpha.parkit

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import android.Manifest;
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ClipData
import android.content.ComponentName
import android.content.Context
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.icu.text.SimpleDateFormat
import android.location.Location;
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityCompat.requestPermissions
import android.support.v4.content.ContextCompat;
import android.view.*
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.alpha.parkit.R.id.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.paytm.pgsdk.PaytmOrder
import com.paytm.pgsdk.PaytmPGService
import com.paytm.pgsdk.PaytmPaymentTransactionCallback
import org.json.JSONObject
import java.security.acl.Owner
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback,LocationListener {

    private lateinit var mMap: GoogleMap
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationManager: LocationManager? = null
    private val MIN_TIME: Long = 400
    private val MIN_DISTANCE = 1000f
    private var curLatLng:LatLng?=null
    private var book_flag=0
    var textViewPrice: TextView? = null
    var mid:String="parkin46421047711652"
    var order_id:String?=null
    var callback_url:String="https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp"
    var channel_id:String="WAP"
    var checksumhash:String?=null
    var industry_type_id:String="Retail"
    var website:String="APPSTAGING"
    var txn_amount:String="1"
    var dest:LatLng?=null
    var origin:LatLng?=null
    var myView:View?=null
    var popupWindow:PopupWindow?=null
    var vehicle:String="Bike"
    var emaiL:String?=""
    var OwnerId:String=""
    var OwnerPhone:String=""
    var SelectedLocationID = ""
    var arrayOfMarker:Array<String> = arrayOf()
    var selectedMarker:LatLng?=null
    var upi:TextView?=null
    lateinit var db: FirebaseFirestore
    lateinit var user: FirebaseAuth

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

            db = FirebaseFirestore.getInstance()
            user = FirebaseAuth.getInstance()
        val sharedPref = this.getSharedPreferences("com.example.alpha.alphaPark", 0)
        emaiL = sharedPref.getString("Email", "");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        }
//        bookFloat.visibility = View.GONE
//        navigate.visibility=View.GONE
        var inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        myView = inflater.inflate(R.layout.activity_spot_book, null)
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
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        setUserData()
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.bookSpace -> {
                if(selectedMarker!=null) {
                    clickBook()
                }
                else {
                    Toast.makeText(this@MainActivity, "Please select the parking marker", Toast.LENGTH_SHORT).show()
                }
            }
            R.id.navigate -> {
                if(selectedMarker!=null) {
                    clickNav()
                }
                else {
                    Toast.makeText(this@MainActivity, "Please select the parking marker", Toast.LENGTH_SHORT).show()
                }

            }

            R.id.refresh -> {
                clickRefresh()
            }
        }
        false
    }


    /////-----------------funciton to run when book button is clicked
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun clickBook(){

        var pos=selectedMarker.toString()
        var flag:Boolean=arrayOfMarker.contains(pos)
        upi=myView!!.findViewById<EditText>(R.id.upi)


        if(flag) {
            upi!!.isEnabled
            upi!!.setText(OwnerPhone)
            popupWindow = PopupWindow(
                myView, // Custom view to show in popup window
                LinearLayout.LayoutParams.WRAP_CONTENT, // Width of popup window
                LinearLayout.LayoutParams.WRAP_CONTENT, // Window height
                true
            )
            popupWindow!!.setOutsideTouchable(false);
            var d: Drawable = ColorDrawable(Color.WHITE)
            popupWindow!!.setBackgroundDrawable(d)


            var layout: LinearLayout = LinearLayout(this)
            popupWindow!!.elevation = 20f

            popupWindow!!.showAtLocation(layout, Gravity.CENTER, 10, 10)
            print(R.id.spinner1)
            val spinner: Spinner? = myView!!.findViewById(R.id.spinner1)

            print(spinner)
            if (spinner != null) {
                ArrayAdapter.createFromResource(
                    this,
                    R.array.vehicle,
                    android.R.layout.simple_spinner_item
                ).also { adapter ->
                    // Specify the layout to use when the list of choices appears
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    // Apply the adapter to the spinner
                    spinner.adapter = adapter
                }
                spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                        // Display the selected item text on text view
                        vehicle = parent.getItemAtPosition(position) as String

                    }

                    override fun onNothingSelected(parent: AdapterView<*>) {
                        // Another interface callback
                        vehicle = "Bike"
                    }
                }


            }
        }




    }

    lateinit var amount: String
    lateinit var dur: String
    ////--------funciton to run when confirm is pressed from booking details page
    fun paytmbutton(view: View){
        val currencyUnit = "INR"
        var amountEdit:EditText= myView!!.findViewById<EditText>(R.id.amount)
        var upiEdit:EditText= myView!!.findViewById<EditText>(R.id.upi)
        var durHold:EditText= myView!!.findViewById<EditText>(R.id.bookTime)

        amount= amountEdit.text.toString()
        var upi=upiEdit.text.toString()
        dur=durHold.text.toString()
        if(amount=="" || upi =="") {
            Toast.makeText(this, "Please fill fields", Toast.LENGTH_SHORT).show()
        }
        else {
            popupWindow!!.dismiss()
            val intent = Intent()
            val bundle = Bundle()
            bundle.putString(PaytmConstants.TRANSACTION_AMOUNT, amount)
            bundle.putString(
                PaytmConstants.PAYEE_MOBILE_NUMBER,
                upi
            )
            bundle.putBoolean(PaytmConstants.IS_MOBILE_NUMBER_EDITABLE, false)
            bundle.putBoolean(PaytmConstants.IS_AMOUNT_EDITABLE, false)
            intent.component = ComponentName("net.one97.paytm", "net.one97.paytm.AJRJarvisSplash")
            intent.putExtra(PaytmConstants.PAYMENT_MODE, 1)
            intent.putExtra(PaytmConstants.MERCHANT_DATA, bundle)
            startActivityForResult(intent, 103)

        }
    }

    /////--------runs when nav icon is pressed, redirects to google maps
    fun clickNav(){
        val intent = Intent(
            android.content.Intent.ACTION_VIEW,
            Uri.parse("http://maps.google.com/maps?saddr="+origin!!.latitude.toString()+","+origin!!.longitude.toString()+"&daddr="+dest!!.latitude.toString()+","+dest!!.longitude.toString())
        )
        startActivity(intent)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            //super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        return false
        menuInflater.inflate(R.menu.main, menu)

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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {

            R.id.history -> {
                val intent = Intent(this, HistoryActivity::class.java).apply {

                }
                startActivity(intent)
            }

            R.id.signout -> {
                signOut()
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

            R.id.bookSpace -> {
                clickBook()
                Toast.makeText(this@MainActivity, "clickbook", Toast.LENGTH_SHORT).show()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun profileImageClick(view: View) {
        var name:String="subham"
        var email:String=" "
        var phno:String=" "
        FirebaseFirestore.getInstance().collection("Users")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get()
            .addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (!task.result!!.exists()) return@OnCompleteListener
                    val jsonObject = task.result!!.data
                    if (jsonObject!!.containsKey("uname")) {
                        name=jsonObject.get("uname").toString()
                        email=jsonObject.get("mail").toString()
                        phno=jsonObject.get("phno").toString()
                        val intent = Intent(this, ProfileActivity::class.java).apply {
                            putExtra("name", name)
                            putExtra("email", email)
                            putExtra("phNo", phno)
                        }
                        startActivity(intent)

                    }

                } else {
                    Toast.makeText(this@MainActivity, task.exception!!.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })



    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun onProviderDisabled(provider: String?) {

    }
    fun generateChecksum(){
        var r= Random(System.currentTimeMillis())
        order_id="Order"+(1+r.nextInt(2))*1000+r.nextInt(1001)

        var url:String="http://paytm-env.vbh285k2fk.us-east-2.elasticbeanstalk.com/paytm/generateChecksum.php"
        var params=HashMap<String,String>()
        params.put("MID","parkin46421047711652")
        params.put("ORDER_ID", order_id!!)
        params.put("CUST_ID","15123")
        params.put("INDUSTRY_TYPE_ID","Retail")
        params.put("CHANNEL_ID","WAP")
        params.put("TXN_AMOUNT","1")
        params.put("WEBSITE","APPSTAGING")
        params.put("CALLBACK_URL", "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=$order_id")


        var param=JSONObject(params)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.POST, url, param,
            Response.Listener{ response ->
                checksumhash=response.optString("CHECKSUMHASH")
                print(response.optString("ORDER_ID"))
                onStartTransaction()

            },
            Response.ErrorListener { error ->
                // TODO: Handle error
            }
        )

        Volley.newRequestQueue(this).add(jsonObjectRequest)
    }

    fun onStartTransaction() {
        val Service = PaytmPGService.getStagingService()
        val paramMap = HashMap<String, String>()

        // these are mandatory parameters

        paramMap["CALLBACK_URL"] = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=$order_id"
        paramMap["CHANNEL_ID"] = "WAP"
        paramMap["CHECKSUMHASH"] = checksumhash!!
        paramMap["CUST_ID"] = "15123"
        paramMap["INDUSTRY_TYPE_ID"] = "Retail"
        paramMap["MID"] = "parkin46421047711652"
        paramMap["ORDER_ID"] = order_id!!
        paramMap["TXN_AMOUNT"] = "1"
        paramMap["WEBSITE"] = "APPSTAGING"



        val Order = PaytmOrder(paramMap)


        Service.initialize(Order, null)

        Service.startPaymentTransaction(this, true, true,
            object : PaytmPaymentTransactionCallback {
                override fun someUIErrorOccurred(inErrorMessage: String) {
                    // Some UI Error Occurred in Payment Gateway Activity.
                    // // This may be due to initialization of views in
                    // Payment Gateway Activity or may be due to //
                    // initialization    of webview. // Error Message details
                    // the error occurred.
                }

                /*@Override
					public void onTransactionSuccess(Bundle inResponse) {
						// After successful transaction this method gets called.
						// // Response bundle contains the merchant response
						// parameters.
						Log.d("LOG", "Payment Transaction is successful " + inResponse);
						Toast.makeText(getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
					}
					@Override
					public void onTransactionFailure(String inErrorMessage,
							Bundle inResponse) {
						// This method gets called if transaction failed. //
						// Here in this case transaction is completed, but with
						// a failure. // Error Message describes the reason for
						// failure. // Response bundle contains the merchant
						// response parameters.
						Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
						Toast.makeText(getBaseContext(), "Payment Transaction Failed ", Toast.LENGTH_LONG).show();
					}*/

                override fun onTransactionResponse(inResponse: Bundle) {
                    com.paytm.pgsdk.Log.d("LOG", "Payment Transaction is successful $inResponse")
                    Toast.makeText(
                        applicationContext,
                        "Payment Transaction response " + inResponse.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun networkNotAvailable() { // If network is not
                    // available, then this
                    // method gets called.
                }

                override fun clientAuthenticationFailed(inErrorMessage: String) {
                    // This method gets called if client authentication
                    // failed. // Failure may be due to following reasons //
                    // 1. Server error or downtime. // 2. Server unable to
                    // generate checksum or checksum response is not in
                    // proper format. // 3. Server failed to authenticate
                    // that client. That is value of payt_STATUS is 2. //
                    // Error Message describes the reason for failure.
                }

                override fun onErrorLoadingWebPage(
                    iniErrorCode: Int,
                    inErrorMessage: String, inFailingUrl: String
                ) {

                }

                // had to be added: NOTE
                override fun onBackPressedCancelTransaction() {
                    Toast.makeText(this@MainActivity, "Back pressed. Transaction cancelled", Toast.LENGTH_LONG)
                        .show()
                }

                override fun onTransactionCancel(inErrorMessage: String, inResponse: Bundle) {
                    com.paytm.pgsdk.Log.d("LOG", "Payment Transaction Failed $inErrorMessage")
                    Toast.makeText(baseContext, "Payment Transaction Failed ", Toast.LENGTH_LONG).show()
                }

            })
    }

    fun getTime(view: View){

        val cal = Calendar.getInstance()
        val lblDate = myView!!.findViewById<EditText>(R.id.in_time)

        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)

            lblDate.setText(SimpleDateFormat("HH:mm").format(cal.time))
        }

        TimePickerDialog(this@MainActivity, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false).show()
    }
    fun datPick(view:View){
        val cal = Calendar.getInstance()
        val y = cal.get(Calendar.YEAR)
        val m = cal.get(Calendar.MONTH)
        val d = cal.get(Calendar.DAY_OF_MONTH)
        val lblDate = myView!!.findViewById<EditText>(R.id.in_date)

        val datepickerdialog: DatePickerDialog = DatePickerDialog(this@MainActivity,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            // Display Selected date in textbox
            lblDate.setText("" + dayOfMonth + " " + monthOfYear + ", " + year)
        }, y, m, d)

        datepickerdialog.show()
    }

    ///////-----------------------------------------------------Location and Map Managing Functions
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
        populatePage()

        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener)
        mMap.setOnMyLocationClickListener(onMyLocationClickListener)
        //mMap.setMinZoomPreference(15F)
//        mMap.setOnMapLongClickListener {
//
////            mMap.clear()
//            mMap.addMarker(MarkerOptions().position(it)
//                .draggable(true))
//        }



        mMap.setOnMarkerClickListener { marker ->
            selectedMarker=marker.position
            SelectedLocationID = selectedMarker!!.latitude.toString()+selectedMarker!!.longitude.toString()
            OwnerId=getOwnerId(selectedMarker!!.latitude.toString()+selectedMarker!!.longitude.toString())
            //getOwnerDetails(OwnerId)
            print(marker.id)
            val placeID: String = marker.position.latitude.toString()+marker.position.longitude.toString()
            // if marker source is clicked
            Toast.makeText(this, placeID, Toast.LENGTH_SHORT).show()// display toast

//            bookFloat.visibility = View.VISIBLE
//            navigate.visibility=View.VISIBLE
//            bookFloat.setOnClickListener(View.OnClickListener {
//                //startBooking(placeID)
//                Toast.makeText(this, "startBooking", Toast.LENGTH_SHORT).show()// display toast
//            })
            origin = this!!.curLatLng!!;
            dest = marker.position;

            // Getting URL to the Google Directions API
            var url:String = getDirectionsUrl(origin!!, dest!!)
            print(url)
//            val path: MutableList<List<LatLng>> = ArrayList()
//            val directionsRequest = object : StringRequest(
//                Request.Method.GET, url, Response.Listener<String> {
//                        response ->
//                    val jsonResponse = JSONObject(response)
//                    // Get routes
//                    val routes = jsonResponse.getJSONArray("routes")
//                    val legs = routes.getJSONObject(0).getJSONArray("legs")
//                    val steps = legs.getJSONObject(0).getJSONArray("steps")
//                    for (i in 0 until steps.length()) {
//                        val points = steps.getJSONObject(i).getJSONObject("polyline").getString("points")
//                        //path.add(PolyUtil.decode(points))
//                    }
//                    for (i in 0 until path.size) {
//                        //mMap!!.addPolyline(PolylineOptions().addAll(path[i]).color(Color.rgb(93, 173, 226)))
//                    }
//
//                }, Response.ErrorListener {
//                        _ ->
//                }){}
//            val requestQueue = Volley.newRequestQueue(this)
//            requestQueue.add(directionsRequest)

            true
        }
        //mMap.setMinZoomPreference(11F)
        //showDefaultLocation()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        print(requestCode)
        print(resultCode)
        print(data)
        if (data != null) {

            setBooking(OwnerId, SelectedLocationID, amount, dur)

            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
        }
    }
    private val onMyLocationButtonClickListener = GoogleMap.OnMyLocationButtonClickListener {
        mMap.setMinZoomPreference(12f)

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

        curLatLng=LatLng(
            location.latitude,
            location.longitude
        )
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

    fun clickRefresh(){
        mMap.clear()
        populatePage()
    }

    private fun showDefaultLocation() {

        val bangalore = LatLng(12.9716, 77.5946)
        val ban1=LatLng(12.98,77.1)
//        mMap.setMinZoomPreference(15f)

//        mMap.addMarker(MarkerOptions().position(bangalore).title("Marker in Bangalore"))
//        mMap.addMarker(MarkerOptions()
//            .position(bangalore)
//            .icon(BitmapDescriptorFactory.fromResource(R.drawable.spaceimage)))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(bangalore))




    }

    fun populatePage(){
        db.collection("points")
            .get().addOnSuccessListener { documents ->
                for (document in documents) {
                    val jsonObject = document.data
                    //Toast.makeText(this@MainActivity, jsonObject.toString(), Toast.LENGTH_SHORT).show()
                    //var place: Place = Place.
                    if (jsonObject!!.containsKey("Lat")) {
                        var place:LatLng =  LatLng(jsonObject.get("Lat").toString().toDouble(),jsonObject.get("Lon").toString().toDouble())
                        //fnameHold.setText(jsonObject.get("uname").toString())
                        //Toast.makeText(this@MainActivity, fnameHold.text, Toast.LENGTH_SHORT).show()
                        //Toast.makeText(this@MainActivity, jsonObject.get("uname").toString(), Toast.LENGTH_SHORT).show()
                        arrayOfMarker=arrayOfMarker.plus(place.toString())
                        mMap.addMarker(MarkerOptions()
                            .position(place)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.spaceimage)))

                    }
                    if (jsonObject.containsKey("mail")) {
                        //emailHold.text =  jsonObject.get("mail").toString()
                        //edit_email.setKeyListener(null);
                    }

                }
            }
            .addOnFailureListener { exception ->

                Toast.makeText(this@MainActivity, "Error getting documents: "+exception, Toast.LENGTH_SHORT).show()
            }

    }

    /////--------------------------------------------------------------Database Managing Functions
    fun signOut() {
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(
            this@MainActivity, "Logging Out",
            Toast.LENGTH_SHORT
        ).show()
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
    }

    fun setUserData(){


        var uemail = findViewById<View>(R.id.nav_view) as NavigationView
        var navHead = uemail.getHeaderView(0)
        var emailHold = navHead.findViewById(R.id.main_page_email) as TextView
        var fnameHold = navHead.findViewById(R.id.main_page_uname) as TextView
        var profileText=navHead.findViewById(R.id.navProfilePic) as TextView
        FirebaseFirestore.getInstance().collection("Users")
            .document(FirebaseAuth.getInstance().currentUser!!.uid)
            .get()
            .addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (!task.result!!.exists()) return@OnCompleteListener
                    val jsonObject = task.result!!.data
                    if (jsonObject!!.containsKey("uname")) {
                        var nameText=jsonObject.get("uname").toString()
                        fnameHold.text=nameText
                        var text=fnameHold.text.toString()
                        profileText.text=Character.toString(nameText[0])
                        print(text)
                    }
                    if (jsonObject.containsKey("mail")) {
                        emailHold.setText(jsonObject.get("mail").toString());
                        //edit_email.setKeyListener(null);
                    }
                } else {
                    Toast.makeText(this@MainActivity, task.exception!!.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })

    }

    fun getOwnerId(Location : String): String {
        print(Location)
        //ar ownerID: String
        db.collection("points").document(Location).get()
            .addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (!task.result!!.exists()) return@OnCompleteListener
                    val jsonObject = task.result!!.data
                    if (jsonObject!!.containsKey("Owner")) {
                        OwnerId= jsonObject.get("Owner").toString()
                        getOwnerDetails((OwnerId))
                    }


                } else {
                    Toast.makeText(this@MainActivity, task.exception!!.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })

        return OwnerId
    }

    fun getOwnerDetails(ownerID: String) {
        print(ownerID)
//        val jsonObje= HashMap<String, Any>()
        db.collection("Users").document(ownerID).get()
            .addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (!task.result!!.exists()) return@OnCompleteListener
                    val jsonObject = task.result!!.data
                    if (jsonObject!!.containsKey("phno")) {
                        OwnerPhone=jsonObject.get("phno").toString()

                    }

                } else {
                    Toast.makeText(this@MainActivity, task.exception!!.localizedMessage, Toast.LENGTH_SHORT).show()

                }
            })

    }

    fun getPlaceDetails(placeID: String): HashMap<String,Any>{
        val jsonObje= HashMap<String, Any>()
        db.collection("places").document(placeID).get()
            .addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    if (!task.result!!.exists()) return@OnCompleteListener
                    val jsonObject = task.result!!.data
                    if (jsonObject!!.containsKey("Name")) {
                        jsonObje.set("Name",jsonObject.containsKey("Name"))
                    }
                    if (jsonObject.containsKey("Owner")) {
                        //edit_email.setKeyListener(null);
                        jsonObje.set("Owner",jsonObject.containsKey("Owner"))
                    }
                    if (jsonObject.containsKey("ID")) {
                        //edit_email.setKeyListener(null);
                        jsonObje.set("ID",jsonObject.containsKey("ID"))
                    }

                } else {
                    Toast.makeText(this@MainActivity, task.exception!!.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            })
        return jsonObje
    }

    fun setBooking(ownerID: String,placeID: String, amount: String, duration: String): Boolean{
        val items= HashMap<String, Any>()
        var retur: Boolean = true
        items.put("user",user.currentUser!!.uid)
        items.put("owner",ownerID)
        items.put("place",placeID)
        items.put("duration",duration)
        items.put("amount",amount)

        db.collection("Bookings").add(items)
            .addOnCompleteListener(OnCompleteListener { task ->
                if (task.isSuccessful) {
                    retur = true
                    Toast.makeText(this@MainActivity, "Booking Successful", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "Booking Failed, Amount will be refunded", Toast.LENGTH_SHORT).show()

                    retur = false
                }
            })
        return retur
    }


}

