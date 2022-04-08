package code.dashboard

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import code.common.OrderModal
import code.utils.*
import code.view.BaseActivity
import com.amtz.v2home.databinding.ActivityOrderListBinding
import com.amtz.v2home.databinding.ActivityPaymentOrderBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class PaymentOrderActivity : BaseActivity() {

    lateinit var binding: ActivityPaymentOrderBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.header.tvHeader.setText("Payment History")
        binding.header.liBack.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                onBackPressed()
            }
        })
        getPaymentApi()
    }

    fun getPaymentApi() {
        if (AppUtils.isNetworkAvailable(mActivity!!)) {
            AppUtils.showRequestDialog(mActivity!!)
            AndroidNetworking.get(AppUrls.payment_order_list)
                .addHeaders("auth", OSettings.getString("token"))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        AppUtils.hideDialog()
                        Log.v("hgdfsa", response.toString());
                        parseJson(response);
                    }
                    override fun onError(anError: ANError) {
                        AppUtils.hideDialog()
                    }
                })
        }
    }

    private fun parseJson(jsonObjs: JSONObject) {
        var arrayListPackage: ArrayList<PaymentModal> = ArrayList<PaymentModal>()
        try {
            if (jsonObjs.getString(AppConstants.resCode) == "200") {

                val jsonArray = jsonObjs.getJSONArray("result")
                for (i in 0 until jsonArray.length()) {
                    try {
                        val packageModal = PaymentModal()

                        val jsonObj=jsonArray.getJSONObject(i);

                        val id = jsonObj.getString("id");
                        packageModal.id=id

                        val order_name_id = jsonObj.getString("order_name_id");
                        packageModal.order_name_id=order_name_id

                        val customer_id = jsonObj.getString("customer_id");
                        packageModal.customer_id=customer_id

                        val payment_order_id = jsonObj.getString("payment_order_id");
                        packageModal.payment_order_id=payment_order_id

                        val amount = jsonObj.getString("amount");
                        packageModal.amount=amount

                        val transaction = jsonObj.getString("transaction");
                        packageModal.transaction=transaction

                        val tax = jsonObj.getString("tax");
                        packageModal.tax=tax

                        val total_amount = jsonObj.getString("total_amount");
                        packageModal.total_amount=total_amount

                        val get_payment_status_display = jsonObj.getString("payment_status");
                        packageModal.get_payment_status_display=get_payment_status_display

                        val payment_date = jsonObj.getString("payment_date");
                        packageModal.payment_date=payment_date

                        arrayListPackage.add(packageModal)


                    } catch (e: Exception) {
                        Log.v("gbfvds", e.message.toString())
                    }
                }
                binding.rvList.layoutManager = LinearLayoutManager(mActivity)
                val adapter = PaymentAdapter(arrayListPackage)
                binding.rvList.adapter = adapter

            }
        } catch (e: JSONException) {

            Log.v("gbfvds", e.message.toString())

            e.printStackTrace()
        }

    }

}