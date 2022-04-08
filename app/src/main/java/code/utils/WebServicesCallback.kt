package code.utils

import org.json.JSONObject

//interface for calling api
interface WebServicesCallback {
    fun OnJsonSuccess(response: JSONObject?)
    fun OnFail(response: String?)
}
