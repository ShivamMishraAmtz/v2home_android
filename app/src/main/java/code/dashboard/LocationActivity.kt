package code.dashboard;

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.SearchView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import code.common.*
import code.utils.*
import code.view.BaseActivity
import com.amtz.v2home.R
import com.amtz.v2home.databinding.ActivityLocationBinding
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import org.json.JSONException
import org.json.JSONObject

class LocationActvity : BaseActivity(), ItemListener {
    var adapters: TodoSectionAdapter? = null;

    lateinit var arrayListCity: ArrayList<StateModal>;
    lateinit var binding: ActivityLocationBinding

    var arraylist: ArrayList<AnimalNames> = ArrayList<AnimalNames>()
    lateinit var animalNameList: Array<String>;
    var adapter: ListViewAdapter? = null

    var arraylistCity: ArrayList<AnimalNames> = ArrayList<AnimalNames>()
    lateinit var animalNameListCity: Array<String>
    var adapterCity: ListViewAdapter? = null

    var arraylistPin: ArrayList<AnimalNames> = ArrayList<AnimalNames>()
    lateinit var animalNameListPin: Array<String>
    var adapterPin: ListViewAdapter? = null

    lateinit var arrayListState: ArrayList<StateModal>;
    lateinit var arrayListPin: ArrayList<StateModal>;
    var dist_id: String = "";
    var area_id: String = "";

    public var sectionArray: ArrayList<TodoSectionModel> = ArrayList<TodoSectionModel>()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.search.requestFocus()

        binding.header.tvHeader.setText("Select Location")
        binding.header.liBack.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                onBackPressed()
            }
        })


        binding.liAll.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {

                AppUtils.hideSoftKeyboard(mActivity)
            }
        })




        hitStateApi()
    }

    fun setState() {
        try {
            for (i in 0 until arrayListState.size) {
                val animalNames = AnimalNames(arrayListState.get(i).state_name)
                // Binds all strings into an array
                if (!arraylist.contains(animalNames))
                    arraylist.add(animalNames)
            }
            adapter = ListViewAdapter(this, arraylist)
            binding.listview.setAdapter(adapter);
            binding.listview.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
                binding.search.setQuery(arraylist!![position].animalName, false);
                // binding.search.setInputType(InputType.TYPE_NULL)
                binding.listview.visibility = View.GONE
                AppUtils.hideSoftKeyboard(mActivity)

                hitCityApi(arrayListState.get(position).id)
            })
            binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    val text = newText!!
                    adapter!!.filter(text)
                    if (text.length <= 0) {
                        binding.listview.visibility = View.GONE;

                        binding.listviewCity.visibility = View.GONE;
                        binding.searchArea.setText("")
                        binding.searchPin.setQuery("", false)
                        binding.searchCity.setQuery("", false)
                        sectionArray.clear()
//                        adapters!!.notifyDataSetChanged()
                    } else {
                        binding.listview.visibility = View.VISIBLE;

                    }
                    return true
                }
            })
        } catch (e: Exception) {
            Log.v("mujythrgfe", e.message.toString())
        }
    }

    fun setCity() {
        animalNameListCity = arrayOf(
            "Vishakhapatnam", "VijayaGram", "Viajaywada", "Gajuwaka"
        )
        arraylistCity.clear()
        for (i in 0 until arrayListCity.size) {
            val animalNames = AnimalNames(arrayListCity.get(i).state_name)
            arraylistCity.add(animalNames)
        }
        adapterCity = ListViewAdapter(this, arraylistCity)
        binding.listviewCity.setAdapter(adapterCity);
        binding.listviewCity.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->
            binding.searchCity.setQuery(arraylistCity!![position].animalName, false);
            //binding.searchCity.setInputType(InputType.TYPE_NULL)
            binding.listviewCity.visibility = View.GONE
            dist_id = arrayListCity.get(position).id
            AppUtils.hideSoftKeyboard(mActivity)


        })

        binding.searchCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val text = newText!!
                adapterCity!!.filter(text)
                if (text.length <= 0) {

                    binding.listviewCity.visibility = View.GONE;
                    binding.searchArea.setText("")
                    binding.searchPin.setQuery("", false)
                    sectionArray.clear()

                    try {
                        arrayListPin.clear()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

//                    adapters!!.notifyDataSetChanged()

                    try {
                        adapterPin!!.notifyDataSetChanged()

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                } else {
                    binding.listviewCity.visibility = View.VISIBLE;
                }
                return true
            }
        })

        binding.btnSignIn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {
                if (OSettings.getInt(AppSettings.position) == -1) {
                    Toast.makeText(mActivity, "Please Select the slots", Toast.LENGTH_LONG).show()
                } else {
                    if (OSettings.getString(AppSettings.user_role) == "user") {
                        OSettings.putString(AppSettings.area_id, area_id)
                        startActivity(Intent(mActivity, CaseListActivity::class.java))
                        finish()
                    } else {
                        OSettings.putString(AppSettings.area_id, area_id)
                        startActivity(Intent(mActivity, AddContactActivity::class.java))
                        finish()
                    }

                }
            }
        })

        binding.searchArea.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {


            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                try {
                    if (count != 0) {
                        binding.searchPin.setQuery("", false)
                        sectionArray.clear()
                        binding.recyclerView.visibility = View.GONE
                        binding.listviewPin.visibility = View.VISIBLE
                        hitAreaApi(dist_id);
                    } else {
                        sectionArray.clear()

                    }


                } catch (ex: Exception) {

                }

            }
        })


    }

    fun setArea() {
        arraylistPin.clear()
        for (i in 0 until arrayListPin.size) {
            val animalNames = AnimalNames(arrayListPin.get(i).state_name)
            // Binds all strings into an array
            arraylistPin.add(animalNames)
        }
        Log.v("hfngdbfsds", arraylistPin.size.toString() + "blacnk")
        adapterPin = ListViewAdapter(this, arraylistPin)
        binding.listviewPin.setAdapter(adapterPin);
        binding.listviewPin.setOnItemClickListener(OnItemClickListener { parent, view, position, id ->

            AppUtils.hideSoftKeyboard(mActivity)
            binding.searchPin.setQuery(arraylistPin!![position].animalName, false);
            binding.listviewPin.visibility = View.GONE
            area_id = arrayListPin.get(position).id;
            binding.searchPin.visibility = View.VISIBLE
            binding.searchArea.visibility = View.GONE
            hitSlotApi(area_id)

//
//            //binding.searchPin.setInputType(InputType.TYPE_NULL)
//
//
//
//
//
//
        })
        binding.searchPin.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val text = newText!!
                adapterPin!!.filter(text)
                if (text.length <= 0) {
                    binding.listviewPin.visibility = View.GONE;
                    binding.searchPin.visibility = View.GONE;
                    binding.searchArea.visibility = View.VISIBLE;
                    binding.searchArea.setText("")
                } else {
                    binding.listviewPin.visibility = View.VISIBLE;
                }
                return true
            }
        })
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
                for (i in 0 until jsonArray.length()) {
                    try {
                        val stateModal = StateModal()
                        val jsonObject1 = jsonArray.getJSONObject(i)
                        val id = jsonObject1.getString("id")
                        stateModal.id = id;
                        val state_name = jsonObject1.getString("state_name")
                        stateModal.state_name = state_name
                        arrayListState.add(stateModal);
                    } catch (e: Exception) {
                        Log.v("gbfdas", e.message.toString());
                    }
                }
                Log.v("jyhtrew", arrayListState.get(0).state_name);
                setState();
            }
        } catch (e: JSONException) {
            e.printStackTrace()

            Log.v("jyhtrew", e.message.toString());
        }
    }

    fun hitCityApi(state_id: String) {
        Log.v("fgdfsds", state_id)
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

                setCity()
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    fun hitAreaApi(dist_id: String) {
        Log.v("district_id", dist_id)
        Log.v("pincode", "530031")
        Log.v("auth", OSettings.getString("token").toString())

        if (AppUtils.isNetworkAvailable(mActivity!!)) {
            //AppUtils.showRequestDialog(mActivity!!)
            AndroidNetworking.post(AppUrls.pincode_list)
                .addHeaders("auth", OSettings.getString("token"))
                .addBodyParameter("district_id", dist_id)
                .addBodyParameter("pincode", binding.searchArea.getText().toString())
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        //AppUtils.hideDialog()
                        Log.v("mnhgfe", response.toString());
                        parseAreaJson(response);
                    }

                    override fun onError(anError: ANError) {
                        //AppUtils.hideDialog()
                        Log.v("mnhgfe", anError.errorBody);
                        binding.searchPin.visibility = View.GONE
                        binding.searchPin.requestFocus()

                    }
                })
        }
    }

    private fun parseAreaJson(jsonObject: JSONObject) {
        arrayListPin = ArrayList<StateModal>()
        try {
            if (jsonObject.getString(AppConstants.resCode) == "200") {
                val jsonArray = jsonObject.getJSONArray("result")
                for (i in 0 until jsonArray.length()) {
                    try {
                        val stateModal = StateModal()
                        val jsonObject1 = jsonArray.getJSONObject(i)
                        val id = jsonObject1.getString("id")
                        stateModal.id = id;
                        val state_name = jsonObject1.getString("pincode_area_name")
                        stateModal.state_name = state_name
                        arrayListPin.add(stateModal)
                    } catch (e: Exception) {
                    }
                }
                setArea()
            } else {
                setArea()
                binding.searchPin.visibility = View.GONE
                binding.searchPin.requestFocus()

            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }

    fun hitSlotApi(area_id: String) {
        if (AppUtils.isNetworkAvailable(mActivity!!)) {
            AppUtils.showRequestDialog(mActivity!!)
            AndroidNetworking.post(AppUrls.booking_slot_list)
                .addHeaders("auth", OSettings.getString("token"))
                .addBodyParameter("pincode_area_id", area_id)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        AppUtils.hideDialog()

                        Log.v("mnhgfe", response.toString());
                        parsePackageJson(response);
                    }

                    override fun onError(anError: ANError) {
                        AppUtils.hideDialog()
                        Log.v("mnhgfe", anError.errorBody);
                    }
                })
        }
    }

    fun parsePackageJson(jsonObject: JSONObject) {
        sectionArray.clear()
        try {
            if (jsonObject.getString(AppConstants.resCode) == "200") {
                val jsonArray = jsonObject.getJSONArray("result")

                binding.recyclerView.visibility = View.VISIBLE

                for (i in 0 until jsonArray.length()) {
                    try {
                        val sectionModel = TodoSectionModel()
                        val week_id = jsonArray.getJSONObject(i).getString("week_id");
                        val weekname = jsonArray.getJSONObject(i).getString("week_name");
                        val day_date = jsonArray.getJSONObject(i).getString("day_date");
                        val week_status = jsonArray.getJSONObject(i).getBoolean("week_status");

                        var result = jsonArray.getJSONObject(i).getJSONArray("result");

                        Log.v("ytr", result.length().toString())

                        var slotArrayList: ArrayList<SlotModal> = ArrayList<SlotModal>()

                        for (j in 0..result.length() - 1) {
                            val slotModal = SlotModal()
                            val day_slot_id = result.getJSONObject(j).getString("day_slot_id");
                            val start_time = result.getJSONObject(j).getString("start_time");
                            val end_time = result.getJSONObject(j).getString("end_time");
                            val total_available = result.getJSONObject(j).getString("total_available");
                            slotModal.week_id = week_id;
                            slotModal.week_name = weekname;
                            slotModal.day_date = day_date;
                            slotModal.day_slot_id = day_slot_id;
                            slotModal.start_time = start_time;
                            slotModal.end_time = end_time;
                            slotModal.total_available = total_available;
                            slotArrayList.add(slotModal)
                        }
                        sectionModel.weekname = weekname;
                        sectionModel.date = day_date;
                        sectionModel.weekid = week_id;
                        sectionModel.items = slotArrayList;

                        if (result.length() != 0 && week_status)
                            sectionArray.add(sectionModel);

                        Log.v("bgfds", sectionArray.toString());


                    } catch (e: Exception) {

                        Log.v("gbfvds", e.message.toString())
                    }
                }

                Log.v("wertyui", sectionArray.get(0).items.get(0).week_name.toString())
                adapters = TodoSectionAdapter(this, sectionArray)
                binding.recyclerView?.setHasFixedSize(true)
                binding.recyclerView?.layoutManager = LinearLayoutManager(this)
                binding.recyclerView?.adapter = adapters
            }
        } catch (e: JSONException) {
            Log.v("gbfvds", e.message.toString())
            e.printStackTrace()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onRemove(pos: Int) {
        binding.btnSignIn.setBackgroundColor(mActivity?.resources?.getColor(R.color.colorPrimary)!!)
        binding.btnSignIn.setBackgroundTintList(
            ColorStateList.valueOf(
                mActivity?.resources!!.getColor(
                    R.color.colorPrimary
                )
            )
        );
        OSettings.putInt(AppSettings.position, pos)
        adapters?.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        OSettings.putInt(AppSettings.position, -1)


    }
}