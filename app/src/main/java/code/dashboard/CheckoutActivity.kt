package code.dashboard

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import code.utils.*
import code.utils.WebServices.postApi
import code.view.BaseActivity
import com.amtz.v2home.databinding.ActivityCheckoutBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONException
import org.json.JSONObject

class CheckoutActivity : BaseActivity(), PaymentResultListener {

    lateinit var binding: ActivityCheckoutBinding

    private var checkout: Checkout? = null

    var paymentId: String = ""


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.serviceName.setText(OSettings.getString(AppSettings.package_name))
        binding.tvDayDate.setText(OSettings.getString(AppSettings.booking_date))
        binding.tvFinalPrice.setText(AppConstants.currency + OSettings.getString(AppSettings.package_amount))
        binding.tvFinalPriceGST.setText(AppConstants.currency + OSettings.getString(AppSettings.package_amount))

        if (OSettings.getString(AppSettings.tax) == "0") {
            binding.liGst.visibility = View.GONE
        }


        if (OSettings.getString(AppSettings.user_role) != "user") {
            binding.btnSubmit.setText("Confirm")
        }
        else
        {
            binding.btnSubmit.setText("Checkout")
        }

        binding.slot.setText(
            OSettings.getString(AppSettings.time_from) + " To " + OSettings.getString(
                AppSettings.time_to
            )
        )

        binding.btnSubmit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                if (OSettings.getString(AppSettings.user_role) == "user") {
                    hitSubmitApi();
                } else {
                    hitSubmitApi();
                }
            }
        })


        binding.header.tvHeader.setText("Order Details")
        binding.header.liBack.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                onBackPressed()
            }
        })

    }

    private fun hitSubmitApi() {
        Log.v(
            "kyjthgf",
            "\narea_location_id" + OSettings.getString(AppSettings.area_id) +
                    "\nday_slot_id" + OSettings.getString(AppSettings.slot_id) +
                    "\ncontact_id" + OSettings.getString(AppSettings.contact_id) +
                    "\npackage_id" + OSettings.getString(AppSettings.package_id) +
                    "\nweek_id" + OSettings.getString(AppSettings.week_id) +
                    "\ncustomer_user_id" + OSettings.getString(AppSettings.customer_id) +
                    "\ncalendar" + OSettings.getString(AppSettings.booking_date)
        )
        Log.v("hgfds", OSettings.getString(AppSettings.customer_id)!!)
        Log.v("auth", OSettings.getString("token")!!)
        if (AppUtils.isNetworkAvailable(mActivity!!)) {
            AppUtils.showRequestDialog(mActivity!!)
            AndroidNetworking.post(AppUrls.place_order)
                .addHeaders("auth", OSettings.getString("token"))
                .addHeaders("Content-Type", "application/json")
                .addBodyParameter("area_location_id", OSettings.getString(AppSettings.area_id))
                .addBodyParameter("day_slot_id", OSettings.getString(AppSettings.slot_id))
                .addBodyParameter("contact_id", OSettings.getString(AppSettings.contact_id))
                .addBodyParameter("package_id", OSettings.getString(AppSettings.package_id))
                .addBodyParameter("week_id", OSettings.getString(AppSettings.week_id))
                .addBodyParameter("customer_user_id", OSettings.getString(AppSettings.customer_id))
                .addBodyParameter("calendar", OSettings.getString(AppSettings.booking_date))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        AppUtils.hideDialog()
                        Log.v("yurwe", response.toString());
                        parseJson(response);
                    }

                    override fun onError(anError: ANError) {
                        AppUtils.hideDialog()
                        Log.v("yurwe", anError.message.toString());
                    }
                })
        }
    }


    private fun parseJson(jsonObject: JSONObject) {
        try {
            if (jsonObject.getString(AppConstants.resCode) == "200") {

                if (OSettings.getString(AppSettings.user_role) == "user") {
                    val jsonObject = jsonObject.getJSONObject("result")
                    Log.v("gdfvddsa", jsonObject.toString())
                    paymentId = jsonObject.getString("payment_id")
                    startPayment(
                        AppUtils.returnDouble(jsonObject.getString("total_amount")),
                        jsonObject.getString("token_key"), jsonObject.getString("payment_order_id")
                    )
                } else {
                    Toast.makeText(
                        mActivity,
                        "Order is placed successfully!",
                        Toast.LENGTH_LONG
                    ).show()

                    startActivity(Intent(mActivity, CompounderActivity::class.java))
                    finish()
                }

            } else {
                Toast.makeText(
                    mActivity,
                    jsonObject.getString(AppConstants.resMsg),
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: JSONException) {
            Log.v("gdfvddsa", e.toString())
            e.printStackTrace()
        }
    }


    override fun onPaymentSuccess(s: String?) {
        hitUpdatePaymentApi("2", s.toString())
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        hitUpdatePaymentApi("3", p1.toString())
    }


    private fun startPayment(totalPrice: Double, key: String, paymentOrderId: String) {
        checkout = Checkout()
        checkout!!.setKeyID(key)
        try {
            val options = JSONObject()
            options.put("name", OSettings.getString(AppSettings.name))
            options.put("description", "Payment")
            options.put("send_sms_hash", true)
            options.put("allow_rotation", false)
            options.put("order_id", paymentOrderId)
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png")
            options.put("currency", "INR")
            options.put("amount", (totalPrice * 100).toString())
            val preFill = JSONObject()
            preFill.put("email", OSettings.getString(AppSettings.email))
            preFill.put("contact", OSettings.getString(AppSettings.phone))
            options.put("prefill", preFill)
            checkout!!.open(mActivity, options)
        } catch (e: Exception) {
            Toast.makeText(mActivity, "Error in payment: " + e.message, Toast.LENGTH_SHORT)
                .show()
            e.printStackTrace()
        }
    }


    private fun hitUpdatePaymentApi(status: String, transactionId: String) {
        if (AppUtils.isNetworkAvailable(mActivity!!)) {
            AppUtils.showRequestDialog(mActivity!!)
            AndroidNetworking.post(AppUrls.payment_status)
                .addHeaders("auth", OSettings.getString("token"))
                .addHeaders("Content-Type", "application/json")
                .addBodyParameter("payment_id", paymentId)
                .addBodyParameter("transaction_id", transactionId)
                .addBodyParameter("payment_status", status)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        AppUtils.hideDialog()
                        Log.v("grbgfevds", response.toString())
                        if (response != null) {
                            parseUpdatePaymentJson(response, status)
                        }
                    }

                    override fun onError(anError: ANError) {
                        AppUtils.hideDialog()
                        Log.v("yurwe", anError.message.toString());
                    }
                })
        }
    }


    private fun parseUpdatePaymentJson(jsonObject: JSONObject, type: String) {
        try {
            if (jsonObject.getString(AppConstants.resCode) == "200") {

                if (OSettings.getString(AppSettings.user_role) == "user") {

                    Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_LONG)
                        .show()

                    startActivity(Intent(mActivity, DashboardActivity::class.java))
                    finish()
                } else {

                    Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_LONG)
                        .show()

                    startActivity(Intent(mActivity, CompounderActivity::class.java))
                    finish()
                }
            } else {
                Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_LONG)
                    .show()
            }
        } catch (e: JSONException) {

            Log.v("tgrfdsw", e.message.toString())

            e.printStackTrace()
        }
    }


}