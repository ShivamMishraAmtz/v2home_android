package code.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import code.common.ContactsModal
import code.common.PackageModal
import code.utils.*
import code.view.BaseActivity
import com.amtz.v2home.R
import com.amtz.v2home.databinding.ActivityCaseListBinding
import com.amtz.v2home.databinding.ActivityDashboardBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONException
import org.json.JSONObject

class CaseListActivity : BaseActivity() {

    private lateinit var binding: ActivityCaseListBinding

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCaseListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.fabAdd.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                startActivity(Intent(mActivity, AddContactActivity::class.java))
            }
        })

        binding.header.tvHeader.setText("Select Person")
        binding.header.liBack.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                onBackPressed()
            }
        })

        getCaseList()
    }

    fun getCaseList() {
        if (AppUtils.isNetworkAvailable(mActivity!!)) {
            AppUtils.showRequestDialog(mActivity!!)
            AndroidNetworking.get(AppUrls.contact_list)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("auth", OSettings.getString("token"))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        AppUtils.hideDialog()
                        Log.v("hkgfds", response.toString());
                        parsePackageJson(response);
                    }
                    override fun onError(anError: ANError) {
                        AppUtils.hideDialog()
                    }
                })
        }
    }

    private fun parsePackageJson(jsonObject: JSONObject) {
        var arrayListContact: ArrayList<ContactsModal> = ArrayList<ContactsModal>()
        try {
            if (jsonObject.getString(AppConstants.resCode) == "200") {
                val jsonArray = jsonObject.getJSONArray("result")
                for (i in 0 until jsonArray.length()) {
                    try {
                        val contactsModal = ContactsModal()
                        val jsonObj=jsonArray.getJSONObject(i);

                        val id=jsonObj.getString("id");
                        val customer_name=jsonObj.getString("customer_name");
                        val mobile_number=jsonObj.getString("mobile_number");
                        val gender=jsonObj.getString("gender");
                        val age=jsonObj.getString("age");
                        val create_date=jsonObj.getString("create_date");

                        contactsModal.id=id;
                        contactsModal.customer_name=customer_name;
                        contactsModal.mobile_number=mobile_number;
                        contactsModal.gender=gender;
                        contactsModal.age=age;
                        contactsModal.create_date=create_date;

                        arrayListContact.add(contactsModal)

                    } catch (e: Exception) {
                    }
                }
                arrayListContact.reverse()
                binding.rvItems.layoutManager = LinearLayoutManager(this)
                val adapter = ContactAdapter(arrayListContact, mActivity!!)
                binding.rvItems.adapter = adapter
            }
        } catch (e: JSONException) {

            Log.v("gbfvds",e.message.toString())

            e.printStackTrace()
        }

    }


}