package code.basic

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
import com.amtz.v2home.databinding.ActivitySignUpBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONException
import org.json.JSONObject

//SignUp
class SignUpActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivitySignUpBinding

    lateinit var arrayListState: ArrayList<StateModal>;
    lateinit var arrayListCity: ArrayList<StateModal>;

    var state_id: String = "";
    var district_name: String = "";

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hitStateApi();

        binding.btnSignUp.setOnClickListener {

            validate();
        }

    }

    override fun onClick(v: View) {
        when (v.id) {

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
    }


    private fun validate() {
        if (AppUtils.isEmpty(binding.etFName)) {
            binding.etFName.requestFocus()
            Toast.makeText(mActivity, "Please Enter First Name", Toast.LENGTH_LONG).show()
        } else if (AppUtils.isEmpty(binding.etLName)) {
            binding.etLName.requestFocus()
            Toast.makeText(mActivity, "Please Enter Last Name", Toast.LENGTH_LONG).show()
        } else if (AppUtils.isEmpty(binding.etEmail)) {
            binding.etEmail.requestFocus()
            Toast.makeText(mActivity, "Please Enter Email Address", Toast.LENGTH_LONG).show()
        } else if (!AppUtils.isEmailValid(
                binding.etEmail.getText().toString().trim { it <= ' ' })
        ) {
            binding.etEmail.requestFocus()
            Toast.makeText(mActivity, "Please Enter Valid Email", Toast.LENGTH_LONG).show()
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
        } else if (AppUtils.isEmpty(binding.etPin)) {
            Toast.makeText(mActivity, "Please Enter Pin Code", Toast.LENGTH_LONG).show()
        } else if (AppUtils.isEmpty(binding.etPassword)) {
            binding.etPassword.requestFocus()
            Toast.makeText(mActivity, "Please Enter Password", Toast.LENGTH_LONG).show()
        } else {
            hitSubmitApi();
        }
    }


    private fun hitSubmitApi() {
        if (AppUtils.isNetworkAvailable(mActivity!!)) {
            AppUtils.showRequestDialog(mActivity!!)
            AndroidNetworking.post(AppUrls.register_account)

                .addBodyParameter(
                    "first_name",
                    binding.etFName.getText().toString().trim { it <= ' ' })
                .addBodyParameter(
                    "last_name",
                    binding.etLName.getText().toString().trim { it <= ' ' })
                .addBodyParameter("email", binding.etEmail.getText().toString().trim { it <= ' ' })
                .addBodyParameter(
                    "mobile",
                    binding.etMobile.getText().toString().trim { it <= ' ' })
                .addBodyParameter("state_id", state_id)
                .addBodyParameter("district_id", district_name)
                .addBodyParameter("pincode", binding.etPin.getText().toString().trim { it <= ' ' })
                .addBodyParameter(
                    "password",
                    binding.etPassword.getText().toString().trim { it <= ' ' })
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
            val jsonObject = response!!.getJSONObject("result")
            if (response.getString(AppConstants.resCode) == "202") {
                //UnVerified
                OSettings.putString(AppSettings.userId,jsonObject.getString("user_id"));
                val intent = Intent(mActivity, OtpActivity::class.java)
                intent.putExtra("email",binding.etEmail.getText().toString().trim { it <= ' ' })
                startActivity(intent)
                finishAffinity()
            } else {

                Log.v("gfds",jsonObject.getString(AppConstants.resCode));
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            Log.v("gfds",e.message.toString());

        }
    }



}