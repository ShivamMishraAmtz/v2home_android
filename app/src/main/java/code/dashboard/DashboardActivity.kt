package code.dashboard
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import code.basic.GroupBookingFormActivity
import code.basic.LoginActivity
import code.basic.ViewPagerAdapter
import code.common.BannerModal
import code.common.PackageModal
import code.utils.*
import code.view.BaseActivity
import com.amtz.v2home.R
import com.amtz.v2home.databinding.ActivityContactUsBinding
import com.amtz.v2home.databinding.ActivityDashboardBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONException
import org.json.JSONObject
import java.util.*

class DashboardActivity : BaseActivity() {

    private lateinit var binding: ActivityDashboardBinding
    var images = intArrayOf(
        R.mipmap.slide_image_1,
        R.mipmap.slide_image_2,
        R.mipmap.slide_image_3);
    var myCustomPagerAdapter: ViewPagerAdapter? = null
    var currentPage = 0
    var timer: Timer? = null
    val DELAY_MS: Long = 500 //delay in milliseconds before task is to be executed
    val PERIOD_MS: Long = 3000 // time in milliseconds between successive task executions.
    val NUM_PAGES: Int = 4

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getBanners();

        binding.main.btnSignIn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                if (OSettings.getString(AppSettings.user_role) == "user") {
                    startActivity(Intent(mActivity, LocationActvity::class.java))
                } else {
                    startActivity(Intent(mActivity, LocationActvity::class.java))
                    finish()
                }
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
                closeDrawer()
                startActivity(Intent(mActivity, ProfileActivity::class.java))
            }
        })

        binding.main.bottom.liContact.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                closeDrawer()
                val browserIntent =
                    Intent(Intent.ACTION_VIEW, Uri.parse("https://www.amtz.in/contact-us/"))
                startActivity(browserIntent)
            }
        })

        binding.tvUpdateProfile.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                closeDrawer()
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

        binding.tvGroupBooking.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                closeDrawer()
                startActivity(Intent(mActivity, GroupBookingFormActivity::class.java))
            }
        })

        binding.tvOrderList.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                closeDrawer()
                if (OSettings.getString(AppSettings.user_role) == "user") {
                    startActivity(Intent(mActivity, OrderListActivity::class.java))
                } else {
                    startActivity(Intent(mActivity, PatientListActivity::class.java))
                }
            }
        })

        binding.tvGroupOrderList.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                closeDrawer()
                startActivity(Intent(mActivity, GroupOrderListActivity::class.java))
            }
        })

        binding.tvPaymentHistory.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                closeDrawer()
                startActivity(Intent(mActivity, PaymentOrderActivity::class.java))
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

        if (OSettings.getString(AppSettings.user_role) == "user") {
            binding.vPay.visibility = View.VISIBLE
            binding.tvPaymentHistory.visibility = View.VISIBLE
            binding.vPatient.visibility = View.GONE
            binding.tvAddPetient.visibility = View.GONE
            binding.tvOrderList.setText("Order List")
        } else {
            binding.vPay.visibility = View.GONE
            binding.tvPaymentHistory.visibility = View.GONE
            binding.vPatient.visibility = View.VISIBLE
            binding.tvAddPetient.visibility = View.VISIBLE
            binding.main.viewPager.visibility = View.GONE
            binding.tvOrderList.setText("Patient List")
        }
    }

    private fun bannerCode(arrayListBanner: ArrayList<BannerModal>) {
        try {
            myCustomPagerAdapter = ViewPagerAdapter(this@DashboardActivity, arrayListBanner);
            binding.main.viewPager!!.setAdapter(myCustomPagerAdapter);
            /*After setting the adapter use the timer */
            /*After setting the adapter use the timer */
            val handler = Handler()
            val Update = Runnable {
                if (currentPage === NUM_PAGES - 1) {
                    currentPage = 0
                }
                binding.main.viewPager!!.setCurrentItem(currentPage++, true)
            }
            timer = Timer() // This will create a new Thread
            timer!!.schedule(object : TimerTask() {
                override fun run() {
                    handler.post(Update)
                }
            }, DELAY_MS, PERIOD_MS);
        } catch (e: Exception) {
            e.printStackTrace()
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

    fun getBanners() {
        if (AppUtils.isNetworkAvailable(mActivity!!)) {
            AppUtils.showRequestDialog(mActivity!!)
            AndroidNetworking.get(AppUrls.get_banners)
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        AppUtils.hideDialog()
                        Log.v("hkgfds", response.toString());
                        parseBannerJson(response);
                    }
                    override fun onError(anError: ANError) {
                        AppUtils.hideDialog()
                    }
                })
        }
    }

    private fun parseBannerJson(jsonObject: JSONObject) {
        var arrayListBanner: ArrayList<BannerModal> = ArrayList<BannerModal>()
        try {
            if (jsonObject.getString(AppConstants.resCode) == "200") {
                val jsonArray = jsonObject.getJSONArray("result")
                for (i in 0 until jsonArray.length()) {
                    try {
                        val bannerModal = BannerModal()
                        val jsonObject1 = jsonArray.getJSONObject(i)
                        val images = jsonObject1.getString("images")
                        bannerModal.images = images;
                        arrayListBanner.add(bannerModal)
                        Log.v("ArraykiSize", arrayListBanner.size.toString())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                bannerCode(arrayListBanner)
            }
        } catch (e: JSONException) {
            Log.v("gbfvds", e.message.toString())
            e.printStackTrace()
        }
    }

    fun getPackageApi() {
        if (AppUtils.isNetworkAvailable(mActivity!!)) {
            AppUtils.showRequestDialog(mActivity!!)
            AndroidNetworking.get(AppUrls.package_list)
                .addHeaders("Content-Type", "application/json")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        AppUtils.hideDialog()
                        Log.v("packageApi", response.toString());
                        parsePackageJson(response);
                    }
                    override fun onError(anError: ANError) {
                        AppUtils.hideDialog()
                        Log.v("packageApi", anError.errorBody.toString());
                    }
                })
        }
    }

    override fun onResume() {
        super.onResume()
        getPackageApi();
    }

    private fun parsePackageJson(jsonObject: JSONObject) {
        var arrayListPackage: ArrayList<PackageModal> = ArrayList<PackageModal>()
        try {
            if (jsonObject.getString(AppConstants.resCode) == "200") {
                val jsonObj = jsonObject.getJSONObject("result")
                val package_amount = jsonObj.getString("package_amount")
                val tax_percentage = jsonObj.getString("tax_percentage")
                val category_id = jsonObj.getString("category_id")

                OSettings.putString(AppSettings.tax, tax_percentage)
                OSettings.putString(AppSettings.package_id, category_id)

                val jsonArray = jsonObj.getJSONArray("subcategory")
                for (i in 0 until jsonArray.length()) {
                    try {
                        val packageModal = PackageModal()

                        val category_id = jsonObj.getString("category_id");
                        val category_name = jsonObj.getString("category_name");

                        val jsonObject1 = jsonArray.getJSONObject(i)
                        val id = jsonObject1.getString("id")
                        val sub_category_name = jsonObject1.getString("sub_category_name")
                        val sub_category_description = jsonObject1.getString("sub_category_description");

                        packageModal.cat_id = category_id;
                        packageModal.cat_name = category_name;
                        packageModal.subcat_id = id;
                        packageModal.subcat_name = sub_category_name;
                        packageModal.sub_category_description = sub_category_description;
                        arrayListPackage.add(packageModal)
                    } catch (e: Exception) {
                    }
                }
                OSettings.putString(AppSettings.package_name, arrayListPackage[0].cat_name)
                OSettings.putString(AppSettings.package_amount, package_amount)
                binding.main.catName.setText(arrayListPackage[0].cat_name);
                binding.main.tvPrice.setText(AppConstants.currency + package_amount + "/-");
                binding.main.rvItems.layoutManager = LinearLayoutManager(this)
                // This will pass the ArrayList to our Adapter
                val adapter = CustomAdapter(arrayListPackage)
                // Setting the Adapter with the recyclerview
                binding.main.rvItems.adapter = adapter
            }
        } catch (e: JSONException) {
            Log.v("gbfvds", e.message.toString())
            e.printStackTrace()
        }
    }

}