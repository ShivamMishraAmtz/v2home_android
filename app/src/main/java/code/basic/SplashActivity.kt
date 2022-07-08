package code.basic

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import code.dashboard.CompounderActivity
import code.dashboard.DashboardActivity
import code.utils.*
import code.view.BaseActivity
import com.amtz.v2home.R
import com.amtz.v2home.databinding.ActivitySplashBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONException
import org.json.JSONObject

class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding

    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initDashboard();
    }

    private fun initDashboard() {
        hitVersionUpdate()
    }

    private fun jumpApi() {

        Log.v("fvdcsaxZ",OSettings.getString(AppSettings.user_role).toString())


        if (OSettings.getString(AppSettings.user_role)?.equals("") == true) {
            startActivity(Intent(mActivity, LoginActivity::class.java))
            finish()
        } else {
            if (OSettings.getString(AppSettings.user_role) == "user") {
                startActivity(Intent(mActivity, DashboardActivity::class.java))
                finish()
            } else {
                startActivity(Intent(mActivity, CompounderActivity::class.java))
                finish()
            }
        }
    }

    private fun hitVersionUpdate() {
        if (AppUtils.isNetworkAvailable(mActivity!!)) {
            // AppUtils.showRequestDialog(mActivity!!)
            AndroidNetworking.post(AppUrls.version_control)
                .addBodyParameter("application_type", "1")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        //AppUtils.hideDialog()
                        Log.v("yurwe", response.toString());
                        parseJson(response);
                    }
                    override fun onError(anError: ANError) {
                        //AppUtils.hideDialog()
                        //jumpApi()
                        Log.v("yurwe", anError.message.toString());
                    }
                })
        }
    }

    private fun parseJson(jsonObject: JSONObject?) {
        try {
            if (jsonObject!!.getString(AppConstants.resCode) == "200") {
                val jsonObject1 = jsonObject.getJSONObject("result")
                val version_name = jsonObject1.getString("version_name")
                val type = jsonObject1.getString("status")
                try {
                    val pInfo =
                        mActivity!!.packageManager.getPackageInfo(mActivity!!.packageName, 0)
                    val version = pInfo.versionName
                    //int verCode = pInfo.versionCode;
                    if (version.toFloat() < version_name.toFloat()) {
                        showUpdateDialog(type)
                    } else {
                        Log.v("AndroidVersiionName", version)
                        Log.v("VersiionName", version_name)
                        jumpApi()
                    }
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                    Toast.makeText(mActivity, "Fail", Toast.LENGTH_LONG).show()
                    jumpApi()
                }
            } else {
                jumpApi()
            }
        } catch (e: JSONException) {
            jumpApi()
            e.printStackTrace()
        }
    }

    private fun showUpdateDialog(type: String) {
        val builder = AlertDialog.Builder(
            mActivity!!
        )
        // Get the layout inflater
        val inflater = mActivity!!.layoutInflater
        builder.setCancelable(false)
        builder.setView(inflater.inflate(R.layout.force_update_layout, null)) // Add action buttons
            .setPositiveButton("UPDATE", DialogInterface.OnClickListener { dialog, id ->
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.amtz.v2home")
                    )
                )
                dialog.dismiss()
            })
        if (type.equals("2", ignoreCase = true)) {
            builder.setNegativeButton(
                "SKIP"
            ) { dialog, which -> jumpApi() }
        }
        builder.create()
        builder.show()
    }


}