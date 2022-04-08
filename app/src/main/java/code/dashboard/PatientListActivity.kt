package code.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import code.common.OrderModal
import code.common.PatientModal
import code.utils.*
import code.view.BaseActivity
import com.amtz.v2home.databinding.ActivityCompounderBinding
import com.amtz.v2home.databinding.ActivityPatientListBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class PatientListActivity : BaseActivity() {

    private lateinit var binding: ActivityPatientListBinding


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.header.tvHeader.setText("Patient List")
        binding.header.liBack.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
               onBackPressed()
            }
        })


        getPatient()
    }

    fun getPatient() {
        if (AppUtils.isNetworkAvailable(mActivity!!)) {
            AppUtils.showRequestDialog(mActivity!!)
            AndroidNetworking.get(AppUrls.peasant_list)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("auth", OSettings.getString("token"))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        AppUtils.hideDialog()
                        Log.v("kjlhgfdsa", response.toString());
                        parseJson(response);
                    }

                    override fun onError(anError: ANError) {
                        AppUtils.hideDialog()
                        Log.v("kjlhgfdsa", anError.message.toString());

                    }
                })
        }
    }


    private fun parseJson(jsonObjs: JSONObject) {
        var arrayListPackage: ArrayList<PatientModal> = ArrayList<PatientModal>()
        try {
            if (jsonObjs.getString(AppConstants.resCode) == "200") {

                val jsonArray = jsonObjs.getJSONArray("result")
                for (i in 0 until jsonArray.length()) {
                    try {
                        val packageModal = PatientModal()

                        val jsonObj = jsonArray.getJSONObject(i);

                        val id = jsonObj.getString("id");
                        packageModal.id = id

                        val first_name = jsonObj.getString("first_name");
                        packageModal.first_name = first_name

                        val last_name = jsonObj.getString("last_name");
                        packageModal.last_name = last_name

                        val unique_number = jsonObj.getString("unique_number");
                        packageModal.unique_number = unique_number

                        val mobile_number = jsonObj.getString("mobile_number");
                        packageModal.mobile_number = mobile_number

                        val state_name_id = jsonObj.getString("state_name_id");
                        packageModal.state_name_id = state_name_id

                        val district_name_id = jsonObj.getString("district_name_id");
                        packageModal.district_name_id = district_name_id

                        val pincode = jsonObj.getString("pincode");
                        packageModal.pincode = pincode

                        arrayListPackage.add(packageModal)


                    } catch (e: Exception) {
                        Log.v("gbfvds", e.message.toString())
                    }
                }


                binding.rvItems.layoutManager = LinearLayoutManager(mActivity)
                val adapter = PatientListAdapter(arrayListPackage, mActivity!!)
                binding.rvItems.adapter = adapter

            }
        } catch (e: JSONException) {

            Log.v("gbfvds", e.message.toString())

            e.printStackTrace()
        }

    }


}