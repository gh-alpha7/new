package com.example.alpha.parkit


import com.google.gson.annotations.SerializedName

/**
 * Created by Belal on 1/10/2018.
 */

class Checksum(
    @field:SerializedName("CHECKSUMHASH")
    val checksumHash: String, @field:SerializedName("ORDER_ID")
    val orderId: String, @field:SerializedName("payt_STATUS")
    val paytStatus: String


)