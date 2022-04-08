package code.utils

import android.content.Intent
import android.util.Log
import code.basic.LoginActivity
import code.utils.AppUtils.hideDialog
import code.utils.AppUtils.hideSoftKeyboard
import code.utils.AppUtils.isNetworkAvailable
import code.utils.AppUtils.md5
import code.utils.AppUtils.showRequestDialog
import code.view.BaseActivity
import com.amtz.v2home.R
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONException
import org.json.JSONObject


//For calling api
object WebServices {
    fun postApi(
        mActivity: BaseActivity,
        url: String?,
        jsonObject: JSONObject,
        loader: Boolean,
        softKeyboard: Boolean, apiCallback: WebServicesCallback
    ) {
        if (isNetworkAvailable(mActivity)) {
            if (loader) showRequestDialog(mActivity)
            if (softKeyboard) hideSoftKeyboard(mActivity)
            Log.v("postApi-URL", url!!)
            Log.v("postApi-jsonObject", jsonObject.toString())
            Log.v(
                "PackageName",
                md5(mActivity.packageName) + md5(mActivity.getString(R.string.app_name))
            )
            AndroidNetworking.post(url)
                .addHeaders(
                    "PackageName",
                    md5(mActivity.packageName) + md5(mActivity.getString(R.string.app_name))
                )
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        if (loader) hideDialog()
                        Log.v("postApi-response", response.toString())
                        apiCallback.OnJsonSuccess(response)
                        try {
                            val jsonObject = response.getJSONObject(AppConstants.projectName)

                            //Invalid token
                            if (jsonObject.getString(AppConstants.resCode) == "500") {

                                mActivity.startActivity(
                                    Intent(
                                        mActivity,
                                        LoginActivity::class.java
                                    )
                                )
                                mActivity.finishAffinity()
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    }

                    override fun onError(anError: ANError) {
                        hideDialog()
                        apiCallback.OnFail(anError.getErrorBody())
                        Log.v("postApi-error", java.lang.String.valueOf(anError.getErrorCode()))
                        Log.v("postApi-error", java.lang.String.valueOf(anError.getErrorBody()))
                        Log.v("postApi-error", java.lang.String.valueOf(anError.getErrorDetail()))
                    }
                })
        }
    }

    fun getApi(
        mActivity: BaseActivity?, url: String?,
        loader: Boolean,
        softKeyboard: Boolean,
        apiCallback: WebServicesCallback
    ) {
        if (loader) showRequestDialog(mActivity!!)
        if (softKeyboard) hideSoftKeyboard(mActivity)
        Log.v("getApi - apiUrl", url!!)
        AndroidNetworking.get(url)
            .setPriority(Priority.IMMEDIATE)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    if (loader) hideDialog()
                    Log.v("getApi-response", response.toString())
                    apiCallback.OnJsonSuccess(response)
                }

                override fun onError(anError: ANError) {
                    hideDialog()
                    try {
                        val jsonObject = JSONObject(anError.getErrorBody())
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    Log.v("respUser", java.lang.String.valueOf(anError.getErrorDetail()))
                    Log.v("respUser", java.lang.String.valueOf(anError.getErrorBody()))
                    Log.v("respUser", java.lang.String.valueOf(anError.getErrorCode()))
                }
            })
    }
}