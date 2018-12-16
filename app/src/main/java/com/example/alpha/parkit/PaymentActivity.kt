package com.example.alpha.parkit


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast

import com.paytm.pgsdk.PaytmOrder
import com.paytm.pgsdk.PaytmPGService
import com.paytm.pgsdk.PaytmPaymentTransactionCallback

import java.util.HashMap

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//implementing PaytmPaymentTransactionCallback to track the payment result.
class PaymentActivity : AppCompatActivity(), PaytmPaymentTransactionCallback {

    //the textview in the interface where we have the price
    var textViewPrice: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //getting the textview
        textViewPrice = findViewById(R.id.textViewPrice)


        //attaching a click listener to the button buy
        findViewById<View>(R.id.buttonBuy).setOnClickListener {
            //calling the method generateCheckSum() which will generate the paytm checksum for payment
            generateCheckSum()
        }

    }

    private fun generateCheckSum() {

        //getting the tax amount first.
        val txnAmount = textViewPrice!!.text.toString().trim { it <= ' ' }

        //creating a retrofit object.
        val retrofit = Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //creating the retrofit api service
        val apiService = retrofit.create(Api::class.java)

        //creating paytm object
        //containing all the values required
        val paytm = Paytm(
            Constants.M_ID,
            Constants.CHANNEL_ID,
            txnAmount,
            Constants.WEBSITE,
            Constants.CALLBACK_URL,
            Constants.INDUSTRY_TYPE_ID
        )

        //creating a call object from the apiService
        val call = apiService.getChecksum(
            paytm.getmId(),
            paytm.orderId,
            paytm.custId,
            paytm.channelId,
            paytm.txnAmount,
            paytm.website,
            paytm.callBackUrl,
            paytm.industryTypeId
        )

        //making the call to generate checksum
        call.enqueue(object : Callback<Checksum> {
            override fun onResponse(call: Call<Checksum>, response: Response<Checksum>) {

                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
                initializePaytmPayment(response.body().checksumHash, paytm)
            }

            override fun onFailure(call: Call<Checksum>, t: Throwable) {

            }
        })
    }

    private fun initializePaytmPayment(checksumHash: String, paytm: Paytm) {

        //getting paytm service
        val Service = PaytmPGService.getStagingService()

        //use this when using for production
        //PaytmPGService Service = PaytmPGService.getProductionService();

        //creating a hashmap and adding all the values required
        val paramMap = HashMap<String, String>()
        paramMap["MID"] = Constants.M_ID
        paramMap["ORDER_ID"] = paytm.orderId
        paramMap["CUST_ID"] = paytm.custId
        paramMap["CHANNEL_ID"] = paytm.channelId
        paramMap["TXN_AMOUNT"] = paytm.txnAmount
        paramMap["WEBSITE"] = paytm.website
        paramMap["CALLBACK_URL"] = paytm.callBackUrl
        paramMap["CHECKSUMHASH"] = checksumHash
        paramMap["INDUSTRY_TYPE_ID"] = paytm.industryTypeId


        //creating a paytm order object using the hashmap
        val order = PaytmOrder(paramMap)

        //intializing the paytm service
        Service.initialize(order, null)

        //finally starting the payment transaction
        Service.startPaymentTransaction(this, true, true, this)

    }

    //all these overriden method is to detect the payment result accordingly
    override fun onTransactionResponse(bundle: Bundle) {

        Toast.makeText(this, bundle.toString(), Toast.LENGTH_LONG).show()
    }

    override fun networkNotAvailable() {
        Toast.makeText(this, "Network error", Toast.LENGTH_LONG).show()
    }

    override fun clientAuthenticationFailed(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }

    override fun someUIErrorOccurred(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }

    override fun onErrorLoadingWebPage(i: Int, s: String, s1: String) {
        Toast.makeText(this, s, Toast.LENGTH_LONG).show()
    }

    override fun onBackPressedCancelTransaction() {
        Toast.makeText(this, "Back Pressed", Toast.LENGTH_LONG).show()
    }

    override fun onTransactionCancel(s: String, bundle: Bundle) {
        Toast.makeText(this, s + bundle.toString(), Toast.LENGTH_LONG).show()
    }
}