package code.dashboard

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import code.common.OrderModal
import code.common.PackageModal
import code.utils.*
import code.view.BaseActivity
import com.amtz.v2home.R
import com.amtz.v2home.databinding.ActivityLocationBinding
import com.amtz.v2home.databinding.ActivityOrderListBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class OrderListActivity : BaseActivity() {

    lateinit var binding: ActivityOrderListBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.tvHeader.setText("Order List")
        binding.header.liBack.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                onBackPressed()
            }
        })
        getOrderApi()
    }

    fun getOrderApi() {
        if (AppUtils.isNetworkAvailable(mActivity!!)) {
            AppUtils.showRequestDialog(mActivity!!)
            Log.v("hgdfsa",OSettings.getString("token").toString())
            AndroidNetworking.get(AppUrls.order_list)
                .addHeaders("auth",OSettings.getString("token"))
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
        var arrayListPackage: ArrayList<OrderModal> = ArrayList<OrderModal>()
        try {
            if (jsonObjs.getString(AppConstants.resCode) == "200") {

                val jsonArray = jsonObjs.getJSONArray("result")
                for (i in 0 until jsonArray.length()) {
                    try {
                        val packageModal = OrderModal()

                        val jsonObj=jsonArray.getJSONObject(i);

                        val id = jsonObj.getString("id");
                        packageModal.id=id
                        val order_number = jsonObj.getString("order_number");
                        packageModal.order_number=order_number
                        val customer_id = jsonObj.getString("customer_id");
                        packageModal.customer_id=customer_id
                        val package_amount = jsonObj.getString("package_amount");
                        packageModal.package_amount=package_amount
                        val package_tax = jsonObj.getString("package_tax");
                        packageModal.package_tax=package_tax
                        val total_amount = jsonObj.getString("total_amount");
                        packageModal.total_amount=total_amount
                        val get_order_status_display = jsonObj.getString("order_status");
                        packageModal.get_order_status_display=get_order_status_display
                        val order_date_time = jsonObj.getString("order_date_time");
                        packageModal.order_date_time=order_date_time
                        arrayListPackage.add(packageModal)


                    } catch (e: Exception) {
                        Log.v("gbfvds", e.message.toString())
                    }
                }
                binding.rvList.layoutManager = LinearLayoutManager(mActivity)
                val adapter = OrderAdapter(arrayListPackage, mActivity!!)
                binding.rvList.adapter = adapter

            }
        } catch (e: JSONException) {

            Log.v("gbfvds", e.message.toString())

            e.printStackTrace()
        }

    }

}