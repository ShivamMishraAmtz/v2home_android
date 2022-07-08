package code.dashboard

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import code.basic.OtpActivity
import code.utils.AppConstants
import code.utils.AppUrls
import code.utils.AppUtils
import code.utils.OSettings
import code.view.BaseActivity
import com.amtz.v2home.databinding.ActivityOrderDetail2Binding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONException
import org.json.JSONObject

class OrderDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityOrderDetail2Binding

    var id = "";
    var order_number = "";
    var unique_number = "";
    var customer_name = "";
    var gender = "";
    var mobile_number = "";
    var age = "";
    var start_time = "";
    var end_time = "";
    var order_status = "";
    var customer_id = "";
    var package_amount = "";
    var package_tax = "";
    var total_amount = "";
    var get_order_status_display = "";
    var order_date_time = "";


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetail2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.header.tvHeader.setText("Order Details")
        binding.header.ivBack.setOnClickListener {
            onBackPressed()
        }
        updateData()

        binding.btnStatuss.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                if (order_status == "2") {
                    order_status="4";
                    hitUpdateApi(id, "4")
                } else if (order_status == "4") {
//                    order_status="3";
//                    hitUpdateApi(id, "3")
                    val intent = Intent(mActivity, ReportFromActivity::class.java)
                    intent.putExtra("order_number", id)
                    startActivity(intent)
                }
                Log.v("order_number",order_number)
            }
        })
    }

    private fun hitUpdateApi(req_id: String, status: String) {
        if (AppUtils.isNetworkAvailable(mActivity!!)) {
            AppUtils.showRequestDialog(mActivity!!)
            AndroidNetworking.post(AppUrls.order_status)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("auth", OSettings.getString("token"))
                .addBodyParameter("orde_request_id", req_id)
                .addBodyParameter("order_status", status)
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

    private fun parseJson(response: JSONObject?) {
        Log.v("dsfdasf", response.toString())
        try {
            if (response?.getString(AppConstants.resCode) == "200") {
                Toast.makeText(mActivity, "Status Updated Successfully!", Toast.LENGTH_LONG).show()
                startActivity(Intent(mActivity, CompounderActivity::class.java))
                finish()
            } else {
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            Log.v("gfds", e.message.toString());

        }
    }


    private fun updateData()
    {
        try {
            id = intent.getStringExtra("id").toString();
            order_number = intent.getStringExtra("order_number").toString();
            unique_number = intent.getStringExtra("unique_number").toString();
            customer_name = intent.getStringExtra("customer_name").toString();
            gender = intent.getStringExtra("gender").toString();
            mobile_number = intent.getStringExtra("mobile_number").toString();
            age = intent.getStringExtra("age").toString();
            start_time = intent.getStringExtra("start_time").toString();
            end_time = intent.getStringExtra("end_time").toString();
            order_status = intent.getStringExtra("order_status").toString();
            customer_id = intent.getStringExtra("customer_id").toString();
            package_amount = intent.getStringExtra("package_amount").toString();
            package_tax = intent.getStringExtra("package_tax").toString();
            total_amount = intent.getStringExtra("total_amount").toString();
            get_order_status_display = intent.getStringExtra("get_order_status_display").toString();
            order_date_time = intent.getStringExtra("order_date_time").toString();
            binding.textViewShortDesc.setText("Name: "+customer_name)
            binding.textViewTitle.setText("Order No: #" + order_number)
            binding.btnMobile.setText("Mobile No: " + mobile_number)
            binding.btnTime.setText(start_time + " To " + end_time);
            binding.textViewUnique.setText("Unique No: " + unique_number);
            binding.btnPrice.setText("Total Amount: " + total_amount);
            if (order_status == "2") {
                binding.btnStatus.text = "Ready For Test"
                binding.btnStatuss.text = "Complete This Case"
            } else if (order_status == "4") {
                binding.btnStatus.text = "Completed"
                binding.btnStatuss.text = "Generate Report"
            } else if (order_status == "3") {
                binding.btnStatus.text = "Report Generated"
                binding.btnStatuss.text = "Report Generated"
            }
            binding.btnPrice.text =
                "Gender: " + gender

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }


}