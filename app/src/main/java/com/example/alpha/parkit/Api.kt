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

<<<<<<< HEAD
        val BASE_URL = "http://paytm-env.vbh285k2fk.us-east-2.elasticbeanstalk.com/paytm/"
=======
        val BASE_URL = "http://35.243.241.140/paytm/"
>>>>>>> 9776825679d684e621b5debd816d0699c836fda9
    }

}