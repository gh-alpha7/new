package com.example.alpha.parkit


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import com.paytm.pgsdk.Log

import com.paytm.pgsdk.PaytmOrder
import com.paytm.pgsdk.PaytmPGService
import com.paytm.pgsdk.PaytmPaymentTransactionCallback

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.widget.EditText
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.util.*
import kotlin.collections.HashMap


//implementing PaytmPaymentTransactionCallback to track the payment result.
class PaymentActivity : AppCompatActivity() {

    //the textview in the interface where we have the price
    var textViewPrice: TextView? = null
    var mid:String="parkin46421047711652"
    var order_id:String?=null
    var callback_url:String="https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp"
    var channel_id:String="WAP"
    var checksumhash:String?=null
    var industry_type_id:String="Retail"
    var website:String="APPSTAGING"
    var txn_amount:String="1"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        //getting the textview
        textViewPrice = findViewById(R.id.costText)


        //attaching a click listener to the button buy
        findViewById<View>(R.id.payButton).setOnClickListener {
            //calling the method generateCheckSum() which will generate the paytm checksum for payment

            generateChecksum()
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

<<<<<<< HEAD
    override fun onStart() {
        super.onStart()
        //initOrderId();
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
=======
    private fun generateCheckSum() {
        val text = "Checksum generation started"
        val duration = Toast.LENGTH_SHORT

        val toast = Toast.makeText(applicationContext, text, duration)
        toast.show()

        Log.d("pressed","entered")
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


        val toast2 = Toast.makeText(applicationContext, txnAmount, duration)
        toast2.show()
        //making the call to generate checksum
        call.enqueue(object : Callback<Checksum> {
            override fun onResponse(call: Call<Checksum>, response: Response<Checksum>) {

                //once we get the checksum we will initiailize the payment.
                //the method is taking the checksum we got and the paytm object as the parameter
                print("passed")
                initializePaytmPayment(response.body().checksumHash, paytm)

            }

            override fun onFailure(call: Call<Checksum>, t: Throwable) {
                print("failed")
            }
        })
>>>>>>> 9776825679d684e621b5debd816d0699c836fda9
    }


<<<<<<< HEAD
    fun generateChecksum(){
        var r= Random(System.currentTimeMillis())
        order_id="Order"+(1+r.nextInt(2))*1000+r.nextInt(1001)
=======
        val text = "Initialized Payment"
        val duration = Toast.LENGTH_SHORT

        val toast = Toast.makeText(applicationContext, text, duration)
        toast.show()

        //getting paytm service
        val Service = PaytmPGService.getStagingService()

        //use this when using for production
        //PaytmPGService Service = PaytmPGService.getProductionService();
>>>>>>> 9776825679d684e621b5debd816d0699c836fda9

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
            com.android.volley.Response.Listener{ response ->
                checksumhash=response.optString("CHECKSUMHASH")
                print(response.optString("ORDER_ID"))
                onStartTransaction()

            },
            com.android.volley.Response.ErrorListener { error ->
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
                    Log.d("LOG", "Payment Transaction is successful $inResponse")
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
                    Toast.makeText(this@PaymentActivity, "Back pressed. Transaction cancelled", Toast.LENGTH_LONG)
                        .show()
                }

                override fun onTransactionCancel(inErrorMessage: String, inResponse: Bundle) {
                    Log.d("LOG", "Payment Transaction Failed $inErrorMessage")
                    Toast.makeText(baseContext, "Payment Transaction Failed ", Toast.LENGTH_LONG).show()
                }

            })
    }

}