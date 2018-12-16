package com.example.alpha.parkit


import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by Belal on 1/10/2018.
 */

interface Api {

    @FormUrlEncoded
    @POST("generateChecksum.php")
    fun getChecksum(
        @Field("MID") mId: String,
        @Field("ORDER_ID") orderId: String,
        @Field("CUST_ID") custId: String,
        @Field("CHANNEL_ID") channelId: String,
        @Field("TXN_AMOUNT") txnAmount: String,
        @Field("WEBSITE") website: String,
        @Field("CALLBACK_URL") callbackUrl: String,
        @Field("INDUSTRY_TYPE_ID") industryTypeId: String
    ): Call<Checksum>

    companion object {

        //this is the URL of the paytm folder that we added in the server
        //make sure you are using your ip else it will not work
        val BASE_URL = "http://192.168.101.1/paytm/"
    }

}