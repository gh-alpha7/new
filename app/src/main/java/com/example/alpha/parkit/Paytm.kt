package com.example.alpha.parkit

import android.util.Log

import com.google.gson.annotations.SerializedName

import java.util.UUID

/**
 * Created by Belal on 1/10/2018.
 */

class Paytm(
    @field:SerializedName("MID")
    internal var mId: String, channelId: String,
    txnAmount: String,
    website: String,
    callBackUrl: String,
    industryTypeId: String
) {

    @SerializedName("ORDER_ID")
    var orderId: String
        internal set

    @SerializedName("CUST_ID")
    var custId: String
        internal set

    @SerializedName("CHANNEL_ID")
    var channelId: String
        internal set

    @SerializedName("TXN_AMOUNT")
    var txnAmount: String
        internal set

    @SerializedName("WEBSITE")
    var website: String
        internal set

    @SerializedName("CALLBACK_URL")
    var callBackUrl: String
        internal set

    @SerializedName("INDUSTRY_TYPE_ID")
    var industryTypeId: String
        internal set

    init {
        this.orderId = generateString()
        this.custId = generateString()
        this.channelId = channelId
        this.txnAmount = txnAmount
        this.website = website
        this.callBackUrl = callBackUrl
        this.industryTypeId = industryTypeId

        Log.d("orderId", orderId)
        Log.d("customerId", custId)
    }

    fun getmId(): String {
        return mId
    }

    /*
    * The following method we are using to generate a random string everytime
    * As we need a unique customer id and order id everytime
    * For real scenario you can implement it with your own application logic
    * */
    private fun generateString(): String {
        val uuid = UUID.randomUUID().toString()
        return uuid.replace("-".toRegex(), "")
    }
}