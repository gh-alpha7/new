package com.example.alpha.parkit

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import kotlinx.android.synthetic.main.activity_history.*
import kotlinx.android.synthetic.main.booking.view.*

class HistoryActivity : AppCompatActivity() {

    var bookingList= ArrayList<Booking>()
    var adapter:BookingAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        bookingList.add(Booking("Sun, Dec 02, 18","location","09:30 AM","10:01 AM","₹21",R.drawable.googleg_standard_color_18))
        bookingList.add(Booking("Sun, Dec 02, 18","location","09:30 AM","10:01 AM","₹21",R.drawable.googleg_standard_color_18))
        bookingList.add(Booking("Sun, Dec 02, 18","location","09:30 AM","10:01 AM","₹21",R.drawable.googleg_standard_color_18))
        bookingList.add(Booking("Sun, Dec 02, 18","location","09:30 AM","10:01 AM","₹21",R.drawable.googleg_standard_color_18))
        bookingList.add(Booking("Sun, Dec 02, 18","location","09:30 AM","10:01 AM","₹21",R.drawable.googleg_standard_color_18))
        bookingList.add(Booking("Sun, Dec 02, 18","location","09:30 AM","10:01 AM","₹21",R.drawable.googleg_standard_color_18))


        adapter= BookingAdapter(this,bookingList)
        bookinglistview.adapter=adapter
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
