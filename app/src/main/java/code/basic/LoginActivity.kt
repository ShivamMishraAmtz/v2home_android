package code.basic

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import code.dashboard.CompounderActivity
import code.dashboard.DashboardActivity
import code.utils.*
import code.utils.AppUtils.hideDialog
import code.utils.AppUtils.isEmailValid
import code.utils.AppUtils.isEmpty
import code.utils.AppUtils.isNetworkAvailable
import code.utils.AppUtils.showRequestDialog
import code.view.BaseActivity
import com.amtz.v2home.databinding.ActivityLoginBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONException
import org.json.JSONObject

//To Login
class LoginActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvCreateNew.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                startActivity(Intent(mActivity, SignUpActivity::class.java))
            }
        })

        binding.btnSignIn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                validate();
            }
        })

        binding.tvForgotPassword.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                startActivity(Intent(mActivity, ForgotPasswordActivity::class.java))
            }
        })
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view = currentFocus
        if (view != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) && view is EditText && !view.javaClass.name.startsWith(
                "android.webkit."
            )
        ) {
            val scrcoords = IntArray(2)
            view.getLocationOnScreen(scrcoords)
            val x = ev.rawX + view.getLeft() - scrcoords[0]
            val y = ev.rawY + view.getTop() - scrcoords[1]
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()) (this.getSystemService(
                INPUT_METHOD_SERVICE
            ) as InputMethodManager).hideSoftInputFromWindow(
                this.window.decorView.applicationWindowToken, 0
            )
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

    private fun validate() {

        if (isEmpty(binding.etEmail)) {
            binding.etEmail.requestFocus()
            Toast.makeText(mActivity, "Please Enter Email Address", Toast.LENGTH_LONG).show()

        } else if (!isEmailValid(binding.etEmail.getText().toString().trim { it <= ' ' })) {
            binding.etEmail.requestFocus()
            Toast.makeText(mActivity, "Please Enter Valid Email", Toast.LENGTH_LONG).show()

        } else if (isEmpty(binding.etPassword)) {
            binding.etPassword.requestFocus()
            Toast.makeText(mActivity, "Please Enter Password", Toast.LENGTH_LONG).show()

        } else {
            hitTokenApi();
        }
    }

    fun hitTokenApi() {
        if (isNetworkAvailable(mActivity!!)) {
            showRequestDialog(mActivity!!)
            AndroidNetworking.post(AppUrls.token)
                .addHeaders("Content-Type", "application/json")
                .addBodyParameter("device_type", "1")
                .addBodyParameter("mobile_device_id", "757998")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        hideDialog()
                        Log.v("khjgfdsa", response.toString());
                        if (response.getString("res_code").equals("200")) {

                            var json: JSONObject = response.getJSONObject("result");

                            var token: String = json.getString("token");
                            OSettings.putString("token", token);
                            hitLoginApi();
                        }
                    }

                    override fun onError(anError: ANError) {
                        hideDialog()

                        Log.v("khjgfdsa", anError.errorDetail);
                    }
                })
        }
    }

    private fun hitLoginApi() {

        Log.v("gtrbrfdsa",OSettings.getString("token").toString())
        Log.v("kljkhgfd",binding.etEmail.getText().toString())
        Log.v("wertyu",binding.etPassword.getText().toString())

        if (isNetworkAvailable(mActivity!!)) {
            showRequestDialog(mActivity!!)
            AndroidNetworking.post(AppUrls.login)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("auth", OSettings.getString("token"))
                .addBodyParameter("email", binding.etEmail.getText().toString().trim { it <= ' ' })
                .addBodyParameter(
                    "password",
                    binding.etPassword.getText().toString().trim { it <= ' ' })
                .addBodyParameter("mobile_device_id", "757998")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        hideDialog()
                        Log.v("yurwe", response.toString());
                        parseJson(response);
                    }

                    override fun onError(anError: ANError) {
                        hideDialog()
                        Log.v("yurwe", anError.message.toString());
                    }
                })
        }
    }

    private fun parseJson(response: JSONObject?) {
        Log.v("dsfdasf", response.toString())
        try {
            val jsonObject = response!!.getJSONObject("result")
            if (response.getString(AppConstants.resCode) == "200") {

                var pk: String = jsonObject.getString("pk");
                OSettings.putString(AppSettings.uid, pk);

                var first_name: String = jsonObject.getString("first_name");
                OSettings.putString(AppSettings.first_name, first_name);

                var last_name: String = jsonObject.getString("last_name");
                OSettings.putString(AppSettings.last_name, last_name);

                var full_name: String = jsonObject.getString("full_name");
                OSettings.putString(AppSettings.name, full_name);

                var email: String = jsonObject.getString("email");
                OSettings.putString(AppSettings.email, email);

                var user_role: String = jsonObject.getString("user_role");
                OSettings.putString(AppSettings.user_role, user_role);

                var mobile_number: String = jsonObject.getString("mobile_number");
                OSettings.putString(AppSettings.phone, mobile_number);

                var address: String = jsonObject.getString("address");
                OSettings.putString(AppSettings.address, address);

                var state_id: String = jsonObject.getString("state_id");
                OSettings.putString(AppSettings.stateId, state_id);

                var district_id: String = jsonObject.getString("district_id");
                OSettings.putString(AppSettings.district_id, district_id);

                var pincode: String = jsonObject.getString("pincode");
                OSettings.putString(AppSettings.pincode, pincode);

                var login_id: String = jsonObject.getString("login_id");
                OSettings.putString(AppSettings.loginId, login_id);

                if (user_role == "user") {
                    startActivity(Intent(mActivity, DashboardActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(mActivity, CompounderActivity::class.java))
                    finish()
                }

            } else {

                Toast.makeText(mActivity, "Login credentials is not valid", Toast.LENGTH_LONG).show()

            }
        } catch (e: JSONException) {

            Toast.makeText(mActivity, "Login credentials is not valid", Toast.LENGTH_LONG).show()


            Log.v("gfdwqs", e.message.toString())

            e.printStackTrace()
        }
    }

}