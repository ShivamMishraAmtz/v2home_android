package code.dashboard

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import code.basic.LoginActivity
import code.common.ContactsModal
import code.common.OrderModal
import code.utils.*
import code.view.BaseActivity
import com.amtz.v2home.R
import com.amtz.v2home.databinding.ActivityCompounderBinding
import com.amtz.v2home.databinding.ActivityDashboardBinding
import com.amtz.v2home.databinding.ActivityLoginBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

class CompounderActivity : BaseActivity() {

    private lateinit var binding: ActivityCompounderBinding

    var arrayListPackage: ArrayList<OrderModal> = ArrayList<OrderModal>()


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompounderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.main.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.v("onQueryTextSubmit",query.toString())
                search(query)
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                Log.v("onQueryTextSubmit",newText.toString())
                search(newText)
                return true
            }
        })

        binding.main.bottom.liMenu.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                openDrawer()
            }
        })

        binding.tvChangePassword.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                closeDrawer()
                startActivity(Intent(mActivity, ChangePasswordActivity::class.java))
            }
        })

        binding.main.bottom.liProfile.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                startActivity(Intent(mActivity, ProfileActivity::class.java))
            }
        })

        binding.tvUpdateProfile.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                startActivity(Intent(mActivity, ProfileActivity::class.java))
            }
        })

        binding.tvLogout.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                OSettings.clearSharedPreference()
                startActivity(Intent(mActivity, LoginActivity::class.java))
                finishAffinity()
                finish()
            }
        })

        binding.tvOrderList.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                closeDrawer()
                if (OSettings.getString(AppSettings.user_role) == "user") {
                    binding.tvOrderList.setText("Order List")
                    startActivity(Intent(mActivity, OrderListActivity::class.java))
                } else {
                    binding.tvOrderList.setText("Patient List")
                    startActivity(Intent(mActivity, PatientListActivity::class.java))
                }
            }
        })

        binding.tvAddPetient.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                closeDrawer()
                startActivity(Intent(mActivity, PetientActivity::class.java))
            }
        })

        binding.main.ivAdd.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                closeDrawer()
                startActivity(Intent(mActivity, PetientActivity::class.java))
            }
        })

        binding.tvPrivacy.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://www.amtz.in/v2home-privacy/"))
                startActivity(browserIntent)
            }
        })

        binding.tvTerm.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://www.amtz.in/terms-conditions/"))
                startActivity(browserIntent)
            }
        })

        binding.tvAboutUs.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://www.amtz.in/index/"))
                startActivity(browserIntent)
            }
        })

        binding.tvContactUs.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://www.amtz.in/contact-us/"))
                startActivity(browserIntent)
            }
        })

        binding.main.bottom.liContact.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                closeDrawer()
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://www.amtz.in/contact-us/"))
                startActivity(browserIntent)            }
        })

        if (OSettings.getString(AppSettings.user_role) == "user") {
            binding.tvOrderList.setText("Order List")
        } else {
            binding.tvOrderList.setText("Patient List")
        }
    }


    override fun onResume() {
        super.onResume()
        getOrderList()

    }

    private fun search(text: String?) {
        try {
            var matchedPeople: ArrayList<OrderModal> = ArrayList<OrderModal>()
            text?.let {
                arrayListPackage.forEach { person ->
                    if (person.order_number.contains(text, true) || person.unique_number.contains(
                            text,
                            true
                        )
                    ) {
                        matchedPeople.add(person)
                    }
                }
                if (matchedPeople.isEmpty()) {
                    Toast.makeText(this, "No Record Found!", Toast.LENGTH_SHORT).show()
                }
                updateRecyclerView(matchedPeople)
            }
        }
        catch (e:Exception)
        {
            e.printStackTrace()
        }


    }

    private fun updateRecyclerView(matchedPeople: ArrayList<OrderModal>) {
        binding.main.rvList.apply {
            val adapter = OrderListAdapter(matchedPeople, mActivity!!)
            binding.main.rvList.adapter = adapter
        }
    }


    //    //Open DRAWER menu
    private fun openDrawer() {
        if (!binding.drawerLayout.isDrawerOpen(binding.scrollSideMenu)) binding.drawerLayout.openDrawer(
            binding.scrollSideMenu
        )
    }

    //    //CLOSE DRAWER MAIN
    fun closeDrawer() {
        if (binding.drawerLayout.isDrawerOpen(binding.scrollSideMenu)) {
            binding.drawerLayout.closeDrawer(binding.scrollSideMenu)
        }
    }


    fun getOrderList() {

        Log.v("rgefdasa",OSettings.getString("token").toString())

        if (AppUtils.isNetworkAvailable(mActivity!!)) {
            AppUtils.showRequestDialog(mActivity!!)
            AndroidNetworking.post(AppUrls.peasant_order_list)
                .addHeaders("Content-Type", "application/json")
                .addHeaders("auth", OSettings.getString("token"))
                .addBodyParameter("current_date", AppUtils.currentDate)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        AppUtils.hideDialog()
                        Log.v("kjlhgfdsa", response.toString());
                        parseJson(response);
                    }

                    override fun onError(anError: ANError) {
                        AppUtils.hideDialog()
                    }
                })
        }
    }


    private fun parseJson(jsonObjs: JSONObject) {
        try {
            if (jsonObjs.getString(AppConstants.resCode) == "200") {
                arrayListPackage.clear()

                val jsonResult:JSONObject=jsonObjs.getJSONObject("result");

                binding.main.tvCompleted.setText(jsonResult.getString("completed"))
                binding.main.tvTotal.setText(jsonResult.getString("total"))

                val jsonArray = jsonResult.getJSONArray("order_details")
                for (i in 0 until jsonArray.length()) {
                    try {
                        val packageModal = OrderModal()

                        val jsonObj = jsonArray.getJSONObject(i);

                        val id = jsonObj.getString("id");
                        packageModal.id = id

                        val order_number = jsonObj.getString("order_number");
                        packageModal.order_number = order_number


                        val unique_number = jsonObj.getString("unique_number");
                        packageModal.unique_number = unique_number


                        val customer_id = jsonObj.getString("customer_name");
                        packageModal.customer_name = customer_id

                        val gender = jsonObj.getString("gender");
                        packageModal.gender = gender


                        val age = jsonObj.getString("age");
                        packageModal.age = age


                        val mobile_number = jsonObj.getString("mobile_number");
                        packageModal.mobile_number = mobile_number


                        val start_time = jsonObj.getString("start_time");
                        packageModal.start_time = start_time


                        val end_time = jsonObj.getString("end_time");
                        packageModal.end_time = end_time


                        val order_status = jsonObj.getString("order_status");
                        packageModal.order_status = order_status


                        arrayListPackage.add(packageModal)


                    } catch (e: Exception) {
                        Log.v("jhmghnfbdsa", e.message.toString())
                    }
                }
                binding.main.tvCountOrder.setText(" ("+arrayListPackage.size.toString()+")")
                binding.main.rvList.layoutManager = LinearLayoutManager(mActivity)
                val adapter = OrderListAdapter(arrayListPackage, mActivity!!)
                binding.main.rvList.adapter = adapter

            }
        } catch (e: JSONException) {

            Log.v("gbfvds", e.message.toString())

            e.printStackTrace()
        }

    }


}