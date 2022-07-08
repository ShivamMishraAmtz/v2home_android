package code.dashboard

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import code.utils.AppConstants
import code.utils.AppUrls
import code.utils.AppUtils
import code.utils.OSettings
import code.view.BaseActivity
import com.amtz.v2home.databinding.ActivityCaseListBinding
import com.amtz.v2home.databinding.ActivityChangePasswordBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONException
import org.json.JSONObject

class ChangePasswordActivity : BaseActivity() {

    private lateinit var binding: ActivityChangePasswordBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.header.tvHeader.setText("Change Password")
        binding.header.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnChange.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                validate()
            }
        })

        binding.header.tvHeader.setText("Change Password")

        binding.header.liBack.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                onBackPressed()
            }
        })

    }

    private fun validate() {
        if (AppUtils.isEmpty(binding.etOldPassword)) {
            binding.etOldPassword.requestFocus()
            Toast.makeText(mActivity, "Please Enter Your Old Password", Toast.LENGTH_LONG).show()
        } else if (AppUtils.isEmpty(binding.etNewPassword)) {
            binding.etNewPassword.requestFocus()
            Toast.makeText(mActivity, "Please Enter Your New Password", Toast.LENGTH_LONG).show()
        } else if (AppUtils.isEmpty(binding.etConfirmPassword)) {
            binding.etConfirmPassword.requestFocus()
            Toast.makeText(mActivity, "Please Confirm Your New Password", Toast.LENGTH_LONG).show()
        } else {
            hitSubmitApi();
        }
    }


    private fun hitSubmitApi() {
        if (AppUtils.isNetworkAvailable(mActivity!!)) {
            AppUtils.showRequestDialog(mActivity!!)
            AndroidNetworking.post(AppUrls.change_password)
                .addHeaders("auth", OSettings.getString("token"))
                .addHeaders("Content-Type", "application/json")
                .addBodyParameter(
                    "old_password",
                    binding.etOldPassword.getText().toString().trim { it <= ' ' })
                .addBodyParameter(
                    "new_password",
                    binding.etConfirmPassword.getText().toString().trim { it <= ' ' })
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
                    startActivity(Intent(mActivity, CaseListActivity::class.java))
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