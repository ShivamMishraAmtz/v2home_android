package code.basic
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import code.utils.AppSettings
import code.utils.AppUrls
import code.utils.AppUtils
import code.utils.OSettings
import code.view.BaseActivity
import com.amtz.v2home.databinding.ActivityLoginBinding
import com.amtz.v2home.databinding.ActivityOtpBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONObject

class OtpActivity : BaseActivity() {
    private lateinit var binding: ActivityOtpBinding
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVerify.setOnClickListener {

            validate();
        }

    }

    private fun validate() {
        if (AppUtils.isEmpty(binding.etOtp)) {
            binding.etOtp.requestFocus()
            Toast.makeText(mActivity, "Please Enter OTP", Toast.LENGTH_LONG).show()
        }  else {
            hitSubmitApi();
        }
    }

    private fun hitSubmitApi() {
        if (AppUtils.isNetworkAvailable(mActivity!!)) {
            AppUtils.showRequestDialog(mActivity!!)
            AndroidNetworking.post(AppUrls.otp_verify)
                .addBodyParameter("user_id",OSettings.getString(AppSettings.userId))
                .addBodyParameter("verification_code", binding.etOtp.getText().toString().trim { it <= ' ' })
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        AppUtils.hideDialog()
                        Log.v("yurwe", response.toString());
                        if (response.getString("res_code").equals("200")) {
                            Log.v("yurwe", response.toString());

                            val intent = Intent(mActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finishAffinity()

                            // parseJson(response);
                        }
                    }

                    override fun onError(anError: ANError) {
                        AppUtils.hideDialog()
                        Log.v("yurwe", anError.message.toString());
                    }
                })
        }
    }




}