package code.utils

import android.app.Activity

//Shared Preference
public final class AppSettings(mActivity: Activity?) : OSettings(mActivity) {
    companion object {


        const val PREFS_MAIN_FILE = "PREFS_OXY2HOME_FILE"
        const val uid = "uid"
        const val first_name = "first_name"
        const val last_name = "last_name"
        const val userId = "userId"
        const val stateId = "stateId"
        const val district_id = "district_id"
        const val login_id = "login_id"
        const val user_role = "user_role"
        const val loginId = "loginId"
        const val fcmToken = "fcmToken"
        const val name = "name"
        const val email = "email"
        const val phone = "phone"
        const val other_number = "other_number"
        const val address = "address"
        const val accessToken = "accessToken"
        const val isLoginSaved = "isLoginSaved"
        const val landmark = "landmark"
        const val pincode = "pincode"
        const val profilePhoto = "profilePhoto"
        const val image_path = "image_path"
        const val password = "password"
        //Swasa Part
        const val accessSwasaToken = "accessSwasaToken"
        const val isShowDialog = "isShowDialog"
        const val allreadyLogin = "allreadyLogin"
        const val position = "position"

        const val tax = "tax"
        const val package_id = "package_id"
        const val slot_id = "slot_id"
        const val booking_date = "booking_date"
        const val area_id = "area_id"
        const val week_id = "week_id"
        const val contact_id = "contact_id"
        const val package_name = "package_name"
        const val package_amount = "package_amount"
        const val time_from = "time_from"
        const val time_to = "time_to"

        const val customer_id = "customer_id"
        const val customer_name = "customer_name"
        const val customer_mobile = "customer_mobile"
    }
}