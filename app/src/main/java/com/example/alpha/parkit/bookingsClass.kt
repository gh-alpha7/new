package com.example.alpha.parkit

import android.media.Image

class Booking{
    var date1:String?=null
    var location:String?=null
    var inTime:String?=null
    var outTime:String?=null
    var money:String?=null
    var image:Int?=null
    constructor(date1:String,location:String,inTime:String,outTime:String,money:String,image:Int){
        this.date1=date1
        this.location=location
        this.inTime=inTime
        this.outTime=outTime
        this.money=money
        this.image=image
    }

}