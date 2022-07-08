package code.dashboard
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import code.common.GroupOrderModal
import code.common.OrderModal
import code.utils.*
import code.view.BaseActivity
import com.amtz.v2home.R
import com.amtz.v2home.databinding.ActivityGroupBookingFormBinding
import com.amtz.v2home.databinding.ActivityGroupOrderListBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class GroupOrderListActivity : BaseActivity() {

    private lateinit var binding: ActivityGroupOrderListBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupOrderListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.header.tvHeader.setText("Booking Order List")
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
            Log.v("hgdfsa", OSettings.getString("token").toString())
            AndroidNetworking.get(AppUrls.group_booking)
                .addHeaders("auth", OSettings.getString("token"))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        AppUtils.hideDialog()
                        Log.v("lkjhgfdsa", response.toString());
                        parseJson(response);
                    }
                    override fun onError(anError: ANError) {
                        Log.v("lkjhgfdsa", anError.message.toString());
                        AppUtils.hideDialog()
                    }
                })
        }
    }

    private fun parseJson(jsonObjs: JSONObject) {
        var arrayListPackage: ArrayList<GroupOrderModal> = ArrayList<GroupOrderModal>()
        try {
            if (jsonObjs.getString(AppConstants.resCode) == "200") {

                val jsonArray = jsonObjs.getJSONArray("result")

                for (i in 0 until jsonArray.length()) {

                    try {
                        val packageModal = GroupOrderModal()

                        val jsonObj=jsonArray.getJSONObject(i);

                        val id = jsonObj.getString("id");
                        packageModal.id=id

                        val subject = jsonObj.getString("subject");
                        packageModal.subject=subject

                        val contact_number = jsonObj.getString("contact_number");
                        packageModal.contact_number=contact_number

                        val total_number = jsonObj.getString("total_number");
                        packageModal.total_number=total_number

                        val message = jsonObj.getString("message");
                        packageModal.message=message

                        val request_check_up_date = jsonObj.getString("request_check_up_date");
                        packageModal.request_check_up_date=request_check_up_date

                        val state_name_id = jsonObj.getString("state_name_id");
                        packageModal.state_name_id=state_name_id

                        val district = jsonObj.getString("district");
                        packageModal.district=district

                        val pincode = jsonObj.getString("pincode");
                        packageModal.pincode=pincode

                        val address = jsonObj.getString("address");
                        packageModal.address=address

//                      val approved_check_up_date = jsonObj.getString("approved_check_up_date");
//                      packageModal.approved_check_up_date=approved_check_up_date

//                      val approve_remarks = jsonObj.getString("approve_remarks");
//                      packageModal.approve_remarks=approve_remarks

                        val status = jsonObj.getString("status");
                        packageModal.status=status

                        arrayListPackage.add(packageModal)

                    } catch (e: Exception) {
                        Log.v("gbfvds", e.message.toString())
                    }
                }
                binding.rvList.layoutManager = LinearLayoutManager(mActivity)
                val adapter = GroupOrderAdapter(arrayListPackage)
                binding.rvList.adapter = adapter
            }
        } catch (e: JSONException) {
            Log.v("gbfvds", e.message.toString())
            e.printStackTrace()
        }
    }

}