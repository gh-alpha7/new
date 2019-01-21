package com.example.alpha.parkit

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Toast
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.booking.view.*


class HistoryActivity : AppCompatActivity() {

    var bookingList= ArrayList<Booking>()
    var adapter:BookingAdapter?=null

    lateinit var db: FirebaseFirestore
    lateinit var user: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        setTitle("History")

        db = FirebaseFirestore.getInstance()
        user = FirebaseAuth.getInstance()

        getHistory()

    }

    fun getHistory(){
        //Toast.makeText(this@HistoryActivity, "Started", Toast.LENGTH_SHORT).show()
        db.collection("Bookings").whereEqualTo("user",user.currentUser!!.uid)
            .get().addOnSuccessListener { documents ->
                //Toast.makeText(this@HistoryActivity, "Ended", Toast.LENGTH_SHORT).show()
                for (document in documents) {

                    val jsonObject = document.data
                    //Toast.makeText(this@HistoryActivity, jsonObject.toString(), Toast.LENGTH_SHORT).show()

                    bookingList.add(
                        Booking(
                            jsonObject.get("owner").toString(),
                            jsonObject.get("place").toString(),
                            jsonObject.get("user").toString(),
                            jsonObject.get("duration").toString(),
                            jsonObject.get("amount").toString(),
                            R.drawable.googleg_standard_color_18))
                }
            }
            .addOnFailureListener { exception ->

                Toast.makeText(this@HistoryActivity, "Error getting documents: " + exception, Toast.LENGTH_SHORT).show()
            }.addOnCompleteListener(OnCompleteListener {
                adapter= BookingAdapter(this,bookingList)
                bookinglistview.adapter=adapter
            })

    }

    class BookingAdapter:BaseAdapter{
        var bookingList=ArrayList<Booking>()
        var context:Context?=null
        constructor(context:Context,bookingList: ArrayList<Booking>):super(){
            this.bookingList=bookingList
            this.context=context
        }
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var bookin=bookingList[position]
            var inflater=context!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var myView= inflater.inflate(R.layout.booking,null)
            myView.date.text=bookin.date1!!
            myView.location.text=bookin.location!!
            myView.intime.text=bookin.inTime!!
            myView.outtime.text=bookin.outTime!!
            myView.money.text=bookin.money!!
            myView.imageView2.setImageResource(bookin.image!!)
            return myView
        }

        override fun getItem(position: Int): Any {
            return bookingList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return bookingList.size
        }

    }
}
