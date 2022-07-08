package code.basic

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import code.utils.AppConstants
import code.utils.AppUrls
import code.utils.AppUtils
import code.view.BaseActivity
import com.amtz.v2home.databinding.ActivityForgotBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONException
import org.json.JSONObject

//For ForgotPassword
class ForgotPasswordActivity : BaseActivity() {
    private lateinit var binding: ActivityForgotBinding
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSubmit.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                validate();
            }
        })
    }

    private fun validate() {
        if (AppUtils.isEmpty(binding.etEmail)) {
            binding.etEmail.requestFocus()
            Toast.makeText(mActivity, "Please Enter Email Address", Toast.LENGTH_LONG).show()
        } else {
            submitApi();
        }
    }

    fun submitApi() {
        Log.v("jhgfd", binding.etEmail.getText().toString())
        if (AppUtils.isNetworkAvailable(mActivity!!)) {
            AppUtils.showRequestDialog(mActivity!!)
            AndroidNetworking.post(AppUrls.forgot_password)
                .addBodyParameter("email", binding.etEmail.getText().toString())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        AppUtils.hideDialog()
                        Log.v("khjgfdsa", response.toString());
                        parseJson(response);
                    }
                    override fun onError(anError: ANError) {
                        AppUtils.hideDialog()
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
                    startActivity(Intent(mActivity, LoginActivity::class.java))
                } else {
                    Toast.makeText(mActivity, response.getString("message"), Toast.LENGTH_LONG)
                        .show()
                    Log.v("gfds", response.getString(AppConstants.resCode));
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            Toast.makeText(mActivity, "Credentials is not valid", Toast.LENGTH_LONG).show()
            Log.v("gfds", e.message.toString());
        }
    }


}