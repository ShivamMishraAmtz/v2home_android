package code.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import code.basic.OtpActivity
import code.utils.*
import code.view.BaseActivity
import com.amtz.v2home.databinding.ActivityAddContactBinding
import com.amtz.v2home.databinding.ActivityCaseListBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONException
import org.json.JSONObject

class AddContactActivity : BaseActivity() {

    private lateinit var binding: ActivityAddContactBinding

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityAddContactBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.header.tvHeader.setText("Contact Person")

        binding.etName.setText(OSettings.getString(AppSettings.customer_name))

        binding.etMobile.setText(OSettings.getString(AppSettings.customer_mobile))

        binding.btnSubmit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                validate()
            }
        })

        binding.rvMain.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                AppUtils.hideSoftKeyboard(mActivity)
            }
        })

        binding.liMain.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                AppUtils.hideSoftKeyboard(mActivity)
            }
        })

        binding.header.liBack.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                onBackPressed()
            }
        })

    }

    private fun validate() {
        if (AppUtils.isEmpty(binding.etName)) {
            binding.etName.requestFocus()
            Toast.makeText(mActivity, "Please Enter Name", Toast.LENGTH_LONG).show()
        } else if (AppUtils.isEmpty(binding.etAge)) {
            binding.etAge.requestFocus()
            Toast.makeText(mActivity, "Please Enter Your Age", Toast.LENGTH_LONG).show()
        } else if (AppUtils.isEmpty(binding.etMobile)) {
            binding.etMobile.requestFocus()
            Toast.makeText(mActivity, "Please Enter Mobile No", Toast.LENGTH_LONG).show()
        } else if (binding.etMobile.getText().toString().length != 10) {
            binding.etMobile.requestFocus()
            Toast.makeText(mActivity, "Please Enter Valid Mobile No", Toast.LENGTH_LONG).show()
        } else {
            hitSubmitApi();
        }
    }

    private fun hitSubmitApi() {
        if (AppUtils.isNetworkAvailable(mActivity!!)) {
            AppUtils.showRequestDialog(mActivity!!)
            var data = "";
            if (binding.radioButtonMale.isChecked) {
                data = "1";
            } else {
                data = "2";
            }
            AndroidNetworking.post(AppUrls.contact_list)
                .addHeaders("auth", OSettings.getString("token"))
                .addBodyParameter(
                    "customer_name",
                    binding.etName.getText().toString().trim { it <= ' ' })
                .addBodyParameter(
                    "customer_user_id",
                    OSettings.getString(AppSettings.customer_id).toString().trim { it <= ' ' })
                .addBodyParameter("gender", data)
                .addBodyParameter("age", binding.etAge.getText().toString().trim { it <= ' ' })
                .addBodyParameter(
                    "mobile_number",
                    binding.etMobile.getText().toString().trim { it <= ' ' })
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
            if (response != null) {
                if (response.getString(AppConstants.resCode) == "200") {
                    Toast.makeText(mActivity, response.getString("message"), Toast.LENGTH_LONG)
                        .show()
                    if (OSettings.getString(AppSettings.user_role) == "user") {
                        startActivity(Intent(mActivity, CaseListActivity::class.java))
                        finish()
                    } else {
                        val jsonObject = response!!.getJSONObject("result")
                        OSettings.putString(
                            AppSettings.contact_id,
                            jsonObject.getString("contact_user_id")
                        );
                        startActivity(Intent(mActivity, CheckoutActivity::class.java))
                        finish()
                    }
                } else {
                    Log.v("gfds", response.getString(AppConstants.resCode));
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            Log.v("gfds", e.message.toString());
        }
    }
}