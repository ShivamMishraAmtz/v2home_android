package code.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import code.common.StateModal
import code.utils.*
import code.view.BaseActivity
import com.amtz.v2home.databinding.ActivityProfileBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONException
import org.json.JSONObject

class ProfileActivity : BaseActivity() {

    private lateinit var binding: ActivityProfileBinding
    lateinit var arrayListState: ArrayList<StateModal>;
    lateinit var arrayListCity: ArrayList<StateModal>;
    var state_id: String = "";
    var district_name: String = "";


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.header.tvHeader.setText("Profile")
        updateOnce();
        hitStateApi();
        binding.header.ivBack.setOnClickListener {
            onBackPressed()
        }
        binding.btnSignUp.setOnClickListener {
            validate();
        }
        binding.header.tvHeader.setText("My Profile")
        binding.header.liBack.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                onBackPressed()
            }
        })

    }

    private fun validate() {
        if (AppUtils.isEmpty(binding.etFName)) {
            binding.etFName.requestFocus()
            Toast.makeText(mActivity, "Please Enter First Name", Toast.LENGTH_LONG).show()
        } else if (AppUtils.isEmpty(binding.etLName)) {
            binding.etLName.requestFocus()
            Toast.makeText(mActivity, "Please Enter Last Name", Toast.LENGTH_LONG).show()
        } else if (AppUtils.isEmpty(binding.etMobile)) {
            binding.etMobile.requestFocus()
            Toast.makeText(mActivity, "Please Enter Mobile No", Toast.LENGTH_LONG).show()
        } else if (binding.etMobile.getText().toString().length != 10) {
            binding.etMobile.requestFocus()
            Toast.makeText(mActivity, "Please Enter Valid Mobile No", Toast.LENGTH_LONG).show()
        } else if (state_id == "") {
            Toast.makeText(mActivity, "Please Select Your State", Toast.LENGTH_LONG).show()
        } else if (district_name == "") {
            Toast.makeText(mActivity, "Please Select Your District", Toast.LENGTH_LONG).show()
        } else if (AppUtils.isEmpty(binding.etAddress)) {
            Toast.makeText(mActivity, "Please Enter Your Address", Toast.LENGTH_LONG).show()
        } else if (AppUtils.isEmpty(binding.etPin)) {
            Toast.makeText(mActivity, "Please Enter Pin Code", Toast.LENGTH_LONG).show()
        } else {
            hitSubmitApi();
        }
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


    fun hitStateApi() {
        if (AppUtils.isNetworkAvailable(mActivity!!)) {
            AppUtils.showRequestDialog(mActivity!!)
            AndroidNetworking.get(AppUrls.state_list)
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        AppUtils.hideDialog()
                        Log.v("hkgfds", response.toString());
                        parseStateJson(response);
                    }

                    override fun onError(anError: ANError) {
                        AppUtils.hideDialog()
                    }
                })
        }
    }


    private fun parseStateJson(jsonObject: JSONObject) {
        arrayListState = ArrayList<StateModal>()
        try {
            if (jsonObject.getString(AppConstants.resCode) == "200") {
                val jsonArray = jsonObject.getJSONArray("result")
                val stateModals = StateModal()
                stateModals.id = "";
                stateModals.state_name = "Select State";
                arrayListState.add(stateModals)
                for (i in 0 until jsonArray.length()) {
                    try {
                        val stateModal = StateModal()
                        val jsonObject1 = jsonArray.getJSONObject(i)
                        val id = jsonObject1.getString("id")
                        stateModal.id = id;
                        val state_name = jsonObject1.getString("state_name")
                        stateModal.state_name = state_name
                        arrayListState.add(stateModal)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val adapter: ArrayAdapter<StateModal> = ArrayAdapter<StateModal>(
            this, android.R.layout.simple_spinner_dropdown_item, arrayListState
        )
        binding.spState.setAdapter(adapter)
        // binding.spState.setSelection(2)
        binding.spState.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                state_id = arrayListState.get(position).id
                hitCityApi(state_id)
                //hitGetCityApi(arrayListState.get(position).getId())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        binding.spState.setSelection(getStateById())
    }


    fun hitCityApi(state_id: String) {
        if (AppUtils.isNetworkAvailable(mActivity!!)) {
            AppUtils.showRequestDialog(mActivity!!)
            AndroidNetworking.post(AppUrls.district_list)
                .addBodyParameter("state_id", state_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        AppUtils.hideDialog()
                        Log.v("kjghfdsa", response.toString());
                        parseCityJson(response);
                    }

                    override fun onError(anError: ANError) {
                        AppUtils.hideDialog()
                    }
                })
        }
    }

    private fun parseCityJson(jsonObject: JSONObject) {
        arrayListCity = ArrayList<StateModal>()
        try {
            if (jsonObject.getString(AppConstants.resCode) == "200") {
                val jsonArray = jsonObject.getJSONArray("result")
                val stateModals = StateModal()
                stateModals.id = "";
                stateModals.state_name = "Select City";
                arrayListCity.add(stateModals)
                for (i in 0 until jsonArray.length()) {
                    try {
                        val stateModal = StateModal()
                        val jsonObject1 = jsonArray.getJSONObject(i)
                        val id = jsonObject1.getString("id")
                        stateModal.id = id;
                        val state_name = jsonObject1.getString("district_name")
                        stateModal.state_name = state_name
                        arrayListCity.add(stateModal)
                    } catch (e: Exception) {
                    }
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val adapter: ArrayAdapter<StateModal> = ArrayAdapter<StateModal>(
            this, android.R.layout.simple_spinner_dropdown_item, arrayListCity
        )
        binding.spCity.setAdapter(adapter)
        // binding.spState.setSelection(2)
        binding.spCity.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {
                district_name = arrayListCity.get(position).id
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })

        binding.spCity.setSelection(getDistrictById())

    }

    private fun hitSubmitApi() {
        if (AppUtils.isNetworkAvailable(mActivity!!)) {
            AppUtils.showRequestDialog(mActivity!!)
            AndroidNetworking.post(AppUrls.update_profile)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("auth", OSettings.getString("token"))
                .addBodyParameter(
                    "first_name",
                    binding.etFName.getText().toString().trim { it <= ' ' })
                .addBodyParameter(
                    "last_name",
                    binding.etLName.getText().toString().trim { it <= ' ' })
                .addBodyParameter(
                    "mobile_number",
                    binding.etMobile.getText().toString().trim { it <= ' ' })
                .addBodyParameter("state_id", state_id)
                .addBodyParameter("district_id", district_name)
                .addBodyParameter(
                    "address",
                    binding.etAddress.getText().toString().trim { it <= ' ' })
                .addBodyParameter("pincode", binding.etPin.getText().toString().trim { it <= ' ' })
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
            if (response?.getString(AppConstants.resCode) == "200") {
                Toast.makeText(mActivity, "Profile Updated Successfully!", Toast.LENGTH_LONG).show()


                OSettings.putString(AppSettings.first_name, binding.etFName.getText().toString());

                OSettings.putString(AppSettings.last_name, binding.etLName.getText().toString());

                OSettings.putString(AppSettings.phone, binding.etMobile.getText().toString());

                OSettings.putString(AppSettings.address, binding.etAddress.getText().toString());

                OSettings.putString(AppSettings.pincode, binding.etPin.getText().toString());

                OSettings.putString(AppSettings.stateId, state_id);

                OSettings.putString(AppSettings.district_id, district_name);


                if (OSettings.getString(AppSettings.user_role) == "user") {
                    startActivity(Intent(mActivity, DashboardActivity::class.java))
                    finish()
                } else {
                    startActivity(Intent(mActivity, CompounderActivity::class.java))
                    finish()
                }
            } else {


            }
        } catch (e: JSONException) {
            e.printStackTrace()
            Log.v("gfds", e.message.toString());

        }
    }

    fun updateOnce() {
        binding.etFName.setText(OSettings.getString(AppSettings.first_name))
        binding.etLName.setText(OSettings.getString(AppSettings.last_name))
        binding.etMobile.setText(OSettings.getString(AppSettings.phone))
        binding.etAddress.setText(OSettings.getString(AppSettings.address))
        binding.etPin.setText(OSettings.getString(AppSettings.pincode))
    }


    private fun getStateById(): Int {
        for (i in arrayListState.indices) {
            if (OSettings.getString(AppSettings.stateId) == arrayListState.get(i).id) {
                return i
            }
        }
        return 0
    }

    private fun getDistrictById(): Int {
        for (i in arrayListCity.indices) {
            if (OSettings.getString(AppSettings.district_id) == arrayListCity.get(i).id) {
                return i
            }
        }
        return 0
    }


}